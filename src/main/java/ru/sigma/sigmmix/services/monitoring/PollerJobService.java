package ru.sigma.sigmmix.services.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.model.RawData;
import ru.sigma.sigmmix.model.Subscription;
import ru.sigma.sigmmix.repositories.HostRepository;
import ru.sigma.sigmmix.repositories.RawDataRepository;
import ru.sigma.sigmmix.repositories.SubscriptionRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PollerJobService {

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private RawDataRepository rawDataRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * Отслеживание метрик активных хостов
     */
    @Scheduled(initialDelay = 3000, fixedRateString = "${application.poller.period}")  // каждыйе 10 секунд через 3 секунды после старта
    private void monitorActiveHosts() throws Exception {
        System.out.print("*"); // debug
        List<Host> activeHosts = hostRepository.findByisActive(true); // мониторим только ативные хосты
        for (Host host : activeHosts) {
            System.out.print(host.getIpAddress()+" "); // debug

            try {
                // Создаём объект соответствующего хосту класса мониторинга (SNMPServiceImpl и др.)
                String className = host.getClassName();
                Class<?> myClass = Class.forName(className);
                Constructor<?> constructor = myClass.getConstructor(String.class);
                MonitoringServiceBase myObject = (MonitoringServiceBase) constructor.newInstance(host.getIpAddress());

                // мониторим пока только одну фиксированную метрику cpuUtilization
                // todo: нужно поллить все метрики класса MonitoringServiceBase
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

                //System.out.println(rawData); // debug
                RawData newItem = rawDataRepository.save(rawData);

                // Проверка првышения порогов отслеживаемых метрик
                checkMetricsThreshold(newItem);

            } catch (Exception e) {
                System.err.println("В процедуре мониторинга хостов выброшено исключение: "+e.getMessage());
                e.printStackTrace();
            }

        }
    }

    /**
     * Проверка всех превышений метрик
     * @param rawData - сохранённый результат значений ресурсов
     */
    private void checkMetricsThreshold(RawData rawData) throws Exception {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByisActive(true);
        for (Subscription subscription : activeSubscriptions) {
            //System.out.println("Subscription: " + subscription); // debug
            double threholdValue = subscription.getThresholdValue();
            double currentValue = 0;
            // получение значения отслеживанемого атрибута
            Class<?> myClass = rawData.getClass();
            Field field = myClass.getDeclaredField(subscription.getMetricaName());
            field.setAccessible(true); // разрешаем доступ к приватному атрибуту
            Object value = field.get(rawData);
            //System.out.println("currentValue = "+value + ", "+value.getClass());
            if (value instanceof Double)
                currentValue = (Double)value;

            if (currentValue > threholdValue) {
                // ТРИГГЕР!!!
                System.out.println("Сработал триггер!");
                // выключаем subscription чтобы он больше не срабатывал
                subscription.setActive(false);
                subscriptionRepository.save(subscription);
                // todo telegram.sendMessage
            }

        }
    }
}
