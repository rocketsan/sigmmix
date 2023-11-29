package ru.sigma.sigmmix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_susbcription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;
    @ManyToOne
    @NotNull
    private Host host;
    @NotNull
    private String metricaName;
    @NotNull
    private Double thresholdValue;
    @NotNull
    private String template;

    private boolean isActive;

    public Subscription() {
        this.template = "Сработал триггер превышения порога значения атрибута {subscription.metricaName} > {subscription.thresholdValue}\nХост: {host.ipAddress}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public @NotNull Host getHost() {
        return host;
    }

    public void setHost(@NotNull Host host) {
        this.host = host;
    }

    public String getMetricaName() {
        return metricaName;
    }

    public void setMetricaName(String metricaName) {
        this.metricaName = metricaName;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", user=" + user.getLogin() +
                ", host=" + host.getIpAddress() +
                ", metricaName='" + metricaName + '\'' +
                ", thresholdValue=" + thresholdValue +
                //", template='" + template + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
