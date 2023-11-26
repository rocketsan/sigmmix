package ru.sigma.sigmmix.services.monitoring;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sigma.sigmmix.repositories.HostRepository;
import ru.sigma.sigmmix.repositories.RawDataRepository;

import java.io.IOException;

@Service
public class SNMPMonitoringService extends MonitoringService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private RawDataRepository rawDataRepository;

    private Snmp snmp;
    private PDU pdu;

    /**
     * Создание объектов для управления SNMP
     */
    @PostConstruct
    public void initSNMP() throws IOException {

        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();

        // Создание объекта PDU для выполнения SNMP GET запроса
        pdu = new PDU();
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.4.11.0"))); // OID для используемой памяти
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.4.6.0")));  // OID для свободной памяти

        pdu.setType(PDU.GET);
    }

    /**
     * Код, который должен выполниться при завершении приложения - закрытие сессии smnp (а надо ли?)
     * @throws IOException если что-то пошло не так
     */
    @PreDestroy
    public void onDestroy() throws IOException {
        snmp.close();
    }

    /**
     * Реализация логики для опроса удаленного сервера через SNMP и получения значения утилизации памяти
     * @param ipAddress адрес хоста, по которому нужно получить утилизацию
     * @return Значение текущей утилизации памяти хоста в процентах
     */
    public double getMemoryUtilization(String ipAddress) throws Exception {
        // Создание объекта CommunityTarget для настройки адреса и комьюнити
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(new UdpAddress(ipAddress + "/161"));
        target.setRetries(2);
        target.setTimeout(1500);

        // Отправка SNMP GET запроса
        ResponseEvent response = snmp.send(pdu, target);
        PDU responsePDU = response.getResponse();

        // Обработка ответа
        double memoryUsagePercentage;
        if (responsePDU != null) {
            VariableBinding usedMemoryVar = responsePDU.get(0);
            VariableBinding freeMemoryVar = responsePDU.get(1);

            long usedMemory = usedMemoryVar.getVariable().toLong();
            long freeMemory = freeMemoryVar.getVariable().toLong();
            long totalMemory = usedMemory + freeMemory;

            memoryUsagePercentage = ((double) usedMemory / totalMemory) * 100; // todo: Выглядит как лажа! Перепроверить!
            //System.out.println("Используемая память в процентах: " + memoryUsagePercentage + "%");
        } else {
            throw new Exception("Общая ошибка при получении snmp-данных");
        }
        return memoryUsagePercentage;
    }

}
