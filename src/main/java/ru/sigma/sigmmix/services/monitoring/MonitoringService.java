package ru.sigma.sigmmix.services.monitoring;

import java.io.IOException;

public abstract class MonitoringService {

    /* Для упрощения бизнес-процессов отслеживаем только 1 параметр */
    abstract double getMemoryUtilization(String IPAddress) throws Exception;

    abstract void destroy() throws IOException;

}
