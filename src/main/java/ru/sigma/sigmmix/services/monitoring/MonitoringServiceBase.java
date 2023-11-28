package ru.sigma.sigmmix.services.monitoring;

import java.io.IOException;

public abstract class MonitoringServiceBase {

    /* Утилизаия памяти (не работает?) */
    abstract public double getMemoryUtilization() throws Exception;

    /* Утилизация CPU */
    abstract public double getCPUUtilization() throws Exception;

    /* обязательно закрывать коннекцию после завершения поллинга! */
    abstract void destroy() throws IOException;

}
