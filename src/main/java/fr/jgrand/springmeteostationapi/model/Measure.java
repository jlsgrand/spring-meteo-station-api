package fr.jgrand.springmeteostationapi.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

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

    private OffsetDateTime measureDate;

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

    public OffsetDateTime getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDateToNow() {
        this.measureDate = OffsetDateTime.now();
    }
}
