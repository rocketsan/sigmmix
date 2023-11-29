package ru.sigma.sigmmix.services.monitoring;

import java.io.IOException;

public abstract class MonitoringServiceBase {

    /* Утилизация CPU (load average 1 min) */
    abstract public double getCPUUtilization() throws Exception;

    /* Утилизаия памяти */
    abstract public double getMemoryUtilization() throws Exception;

    /**
     * обязательно закрывать коннекцию после завершения поллинга!
     * открывается коннекция в конструкторе
     */
    abstract void destroy() throws IOException;

}
