package dev.makos.redis.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;

@Getter

@Entity
@Table(schema = "world", name = "country_language")
public class CountryLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(length = 30)
    private String language;

    @Column(name = "is_official", columnDefinition = "BIT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isOfficial;

    private BigDecimal percentage;

}
