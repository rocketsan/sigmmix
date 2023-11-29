package ru.sigma.sigmmix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_rawdata")
public class RawData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    private Host host;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "timestamp_")
    private Timestamp timestamp;

    // Отслеживаемые параметры //////////
    private double memoryUtilization;
    private double cpuUtilization;

    /////////////////////////////////////

    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull Host getHost() {
        return host;
    }

    public void setHost(@NotNull Host host) {
        this.host = host;
    }

    public @NotNull Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Timestamp timestamp_) {
        this.timestamp = timestamp_;
    }

    public double getMemoryUtilization() {
        return memoryUtilization;
    }

    public void setMemoryUtilization(double memoryUtilization) {
        this.memoryUtilization = memoryUtilization;
    }

    /**
     * Возвращает список строк с названиями атрибутов данного класса
     */
    public static List<String> getMetricFields() {
        Class<?> myClass = RawData.class;
        Field[] fields = myClass.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType() == double.class) {
                fieldNames.add(field.getName());
            }
        }
        fieldNames.sort(null);
        //System.out.println(fieldNames);
        return fieldNames;
    }

    @Override
    public String toString() {
        return "RawData{" +
                "id=" + id +
                ", host=" + host.getIpAddress() +
                ", timestamp=" + timestamp +
                ", memoryUtilization=" + memoryUtilization +
                ", cpuUtilization=" + cpuUtilization +
                '}';
    }
}
