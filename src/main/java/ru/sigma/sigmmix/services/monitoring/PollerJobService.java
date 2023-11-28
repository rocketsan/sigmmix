package ru.sigma.sigmmix.services.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.model.RawData;
import ru.sigma.sigmmix.repositories.HostRepository;
import ru.sigma.sigmmix.repositories.RawDataRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PollerJobService {

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private RawDataRepository rawDataRepository;

    /**
     * Отслеживание метрик активных хостов
     */
    @Scheduled(initialDelay = 3000, fixedRateString = "${application.poller.period}")  // каждыйе 10 секунд через 3 секунды после старта
    private void monitorActiveHosts() throws Exception {
        System.out.print("*"); // debug
        List<Host> activeHosts = hostRepository.findByisActive(true); // мониторим только ативные хосты
        for (Host host : activeHosts) {
            System.out.println(host);

            try {
                // Создаём объект соответствующего хосту класса мониторинга (SNMPServiceImpl и др.)
                String className = host.getClassName();
                Class<?> myClass = Class.forName(className);
                Constructor<?> constructor = myClass.getConstructor(String.class);

                MonitoringServiceBase myObject = (MonitoringServiceBase) constructor.newInstance(host.getIpAddress());
                // Вызываем метод getMemoryUtilization() с параметром host.ipAddress
                //Method method = myClass.getMethod("getMemoryUtilization", String.class);
                Method method = myClass.getMethod("getCPUUtilization");
                Object result = method.invoke(myObject);
                //System.out.println("result = "+result+", "+result.getClass());

                // приводим результат к требуемому формату double
                double cpuUtilization = 0;
                if (result instanceof Double)
                    cpuUtilization = (Double) result;

                myObject.destroy(); // закрываем ресурсы

                // Сохранение отслеживаемого параметра в БД
                RawData rawData = new RawData();
                rawData.setHost(host);
                rawData.setTimestamp(new Timestamp(System.currentTimeMillis()));
                rawData.setCpuUtilization(cpuUtilization);

                System.out.println(rawData); // debug
                rawDataRepository.save(rawData);
            } catch (Exception e) {
                System.err.println("В процедуре мониторинга хостов выброшено исключение: "+e.getMessage());
                e.printStackTrace();
            }

        }
    }
}
