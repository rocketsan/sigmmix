package ru.sigma.sigmmix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_host")
public class Host {

    public enum InterfaceType {
        SNMP,
        SSH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String hostname;
    @NotNull
    private String ipAddress;
    @Enumerated(EnumType.STRING) // Хранить значение Enum как строку
    @NotNull
    private InterfaceType interfaceType;
    private boolean isActive;
    private boolean isRemoved;

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public @NotNull String getHostname() {
        return hostname;
    }

    public void setHostname(@NotNull String name) {
        this.hostname = name;
    }

    public @NotNull String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(@NotNull String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public @NotNull InterfaceType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(@NotNull InterfaceType interfaceType) {
        this.interfaceType = interfaceType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", interfaceType=" + interfaceType +
                ", isActive=" + isActive +
                ", isRemoved=" + isRemoved +
                '}';
    }
}
