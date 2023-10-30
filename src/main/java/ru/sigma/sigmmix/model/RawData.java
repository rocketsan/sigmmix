package ru.sigma.sigmmix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

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
    private Timestamp timestamp_;

}
