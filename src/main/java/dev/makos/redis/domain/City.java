package dev.makos.redis.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter

@Entity
@Table(schema = "world", name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 35)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String district;

    private Integer population;

}
