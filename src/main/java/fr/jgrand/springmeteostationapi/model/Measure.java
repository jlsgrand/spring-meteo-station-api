package fr.jgrand.springmeteostationapi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Measure {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private MeasureType type;

    @Enumerated(EnumType.STRING)
    private MeasureUnit unit;

    @Column(precision = 6, scale = 2)
    private BigDecimal value;

    private LocalDateTime measureDate;

    public Long getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public MeasureType getType() {
        return type;
    }

    public MeasureUnit getUnit() {
        return unit;
    }

    public LocalDateTime getMeasureDate() {
        return measureDate;
    }

}
