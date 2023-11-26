package ru.sigma.sigmmix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_host")
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String hostname;
    @NotNull
    private String ipAddress;
    @NotNull
    private String className;
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

    public @NotNull String getClassName() {
        return className;
    }

    public void setClassName(@NotNull String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", className=" + className +
                ", isActive=" + isActive +
                ", isRemoved=" + isRemoved +
                '}';
    }
}
