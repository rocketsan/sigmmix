package ru.sigma.sigmmix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_host")
public class Host {

    public enum InterfaceType {
        SNMP,
        AGENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String ipAddress;
    @Enumerated(EnumType.STRING) // Хранить значение Enum как строку
    private InterfaceType interfaceType;
    private boolean isActive;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
