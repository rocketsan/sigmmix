package ru.sigma.sigmmix.services.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.sigma.sigmmix.MainApp;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.model.RawData;
import ru.sigma.sigmmix.model.Subscription;
import ru.sigma.sigmmix.repositories.HostRepository;
import ru.sigma.sigmmix.repositories.RawDataRepository;
import ru.sigma.sigmmix.repositories.SubscriptionRepository;
import ru.sigma.sigmmix.services.telegram.NotifierBot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PollerJobService {

    /* Создаём аннотацию, чтобы помечать ею те поля, которые потом будем использовать в шаблонизаторе */
    @Deprecated
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FieldName {
    }

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
                // todo: нужно поллить все метрики класса MonitoringServiceBase
                double cpuUtilization = getData(host, "getCPUUtilization");
                double memoryUtilization = getData(host, "getMemoryUtilization");

                // Сохранение отслеживаемого параметра в БД
                RawData rawData = new RawData();
                rawData.setHost(host);
                rawData.setTimestamp(new Timestamp(System.currentTimeMillis()));
                rawData.setCpuUtilization(cpuUtilization);
                rawData.setMemoryUtilization(memoryUtilization);

                System.out.println("CPU="+cpuUtilization+", RAM="+memoryUtilization); // debug

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

    private double getData(Host host, String methodName) throws Exception {
        // Создаём объект соответствующего хосту класса мониторинга (SNMPServiceImpl и др.)
        String className = host.getClassName();
        Class<?> myClass = Class.forName(className);
        Constructor<?> constructor = myClass.getConstructor(String.class);
        MonitoringServiceBase myObject = (MonitoringServiceBase) constructor.newInstance(host.getIpAddress());

        Method method = myClass.getMethod(methodName);
        Object result = method.invoke(myObject);
        //System.out.println("result = "+result+", "+result.getClass());

        // приводим результат к требуемому формату double
        double value = (result instanceof Double) ? (Double) result : 0;

        myObject.destroy(); // закрываем ресурсы

        return value;
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
                // СРАБОТАЛ ТРИГГЕР!!!
                // 1. Выключаем subscription чтобы он больше не срабатывал
                subscription.setActive(false);
                subscriptionRepository.save(subscription);
                // 2. Отправляем сообщение в телеграм
                sendBotMessage(subscription, rawData, currentValue);
            }

        }
    }

    private void sendBotMessage(Subscription subscription, RawData rawData, double currentValue) {
        String template = subscription.getTemplate();
        //System.out.println("template = "+ template); // debug
        Host host = rawData.getHost();
        Map<String, Object> attributes = new HashMap<>();
        // биндим для шаблона макросы всех значимых объектов
        //attributes.put("subscription", subscription);
        //attributes.put("host", host);
        attributes.put("metricaName", subscription.getMetricaName());
        attributes.put("thresholdValue", subscription.getThresholdValue());
        attributes.put("ipAddress", host.getIpAddress());
        attributes.put("value", currentValue);
        String processedTemplate = process(template, attributes);
        System.out.println("message = " + processedTemplate);

        // непосредственно отправка сообщения пользователю
        NotifierBot bot = (NotifierBot) MainApp.context.getBean("notifierBot", TelegramLongPollingBot.class);
        bot.sendMessage(subscription.getUser().getTelegramId(), processedTemplate);
    }

    /**
     * Простой кастомный шаблонизатор. Подставляет поля объектов из мапы attributes в шаблон template.
     * @param template - шаблон сообщения
     * @param attributes - карта полей объектов
     * @return - шаблон сообщения с подставленными значениями из карты полей объектов
     */
    private String process(String template, Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            Object attributeValue = entry.getValue();
            template = template.replace(placeholder, attributeValue.toString());
        }
        return template;
    }

    /**
     * Простой кастомный шаблонизатор. Подставляет поля объектов из мапы attributes в шаблон template. 
     * Исползует reflections.
     * В качестве полей объктов используются поля, аннтированные специальной анннотацией @FieldName
     * @param template - шаблон сообщения
     * @param attributes - карта полей объектов
     * @return - шаблон сообщения с подставленными значениями из карты полей объектов
     */
    @Deprecated
    private String processOld(String template, Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String objectKey = entry.getKey();
            Object objectValue = entry.getValue();
            // System.out.println("key = "+objectKey+", value = " + objectValue);

            // Получаем все поля объекта с аннотацией @FieldName
            Field[] fields = objectValue.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(FieldName.class)) {
                    String fieldName = field.getName();
                    String placeholder = "{" + objectKey + "." + fieldName + "}";
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(objectValue);
                        template = template.replace(placeholder, fieldValue.toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return template;
    }
}
