package ru.sigma.sigmmix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_user")
public class User {

    public enum RoleType {
        MONITOR,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String login;
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING) // Хранить значение Enum как строку
    private RoleType role;
    private String telegramId; // может Long?


    public User() { }

    public User(@NotNull String login, @NotNull String password, RoleType role, String telegramId) {
        this.login=login;
        this.password=password;
        this.role=role;
        this.telegramId=telegramId;
    }

    public User(@NotNull String login, @NotNull String password, RoleType role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }


    public @NotNull String getLogin() {
        return login;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public RoleType getRole() {
        return role;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", telegramId='" + telegramId + '\'' +
                '}';
    }

}

