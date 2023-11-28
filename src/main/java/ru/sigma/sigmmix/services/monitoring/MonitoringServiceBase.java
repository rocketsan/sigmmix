package ru.sigma.sigmmix.services.monitoring;

import java.io.IOException;

public abstract class MonitoringServiceBase {

    /* Для упрощения бизнес-процессов отслеживаем только 1 параметр */
    abstract double getMemoryUtilization(String IPAddress) throws Exception;

    /* обязательно закрывать коннекцию после завершения поллинга! */
    abstract void destroy() throws IOException;

}
