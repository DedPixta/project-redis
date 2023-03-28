package dev.makos.redis.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter

@Entity
@Table(schema = "world", name = "country")
public class Country {

    @Id
    private Integer id;

    @Column(length = 3)
    private String code;

    @Column(name = "code_2", length = 2)
    private String alternativeCode;

    @Column(length = 52)
    private String name;

    @Column(name = "continent")
    @Enumerated(value = EnumType.ORDINAL)
    private Continent continent;

    @Column(length = 26)
    private String region;

    @Column(name = "surface_area")
    private BigDecimal surfaceArea;

    @Column(name = "indep_year")
    private Short indepYear;

    private Integer population;

    @Column(name = "life_expectancy")
    private BigDecimal lifeExpectancy;

    private BigDecimal GNP;

    @Column(name = "gnpo_id")
    private BigDecimal GNPOId;

    @Column(name = "local_name", length = 45)
    private String localName;

    @Column(name ="government_form", length = 45)
    private String governmentForm;

    @Column(name ="head_of_state", length = 60)
    private String headOfState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capital")
    private City capital;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Set<CountryLanguage> languages;

}
