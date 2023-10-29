package ru.sigma.sigmmix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_susbcription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Host host;


    public Subscription(User user) {
        this.user = user;
    }

    public Subscription() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
