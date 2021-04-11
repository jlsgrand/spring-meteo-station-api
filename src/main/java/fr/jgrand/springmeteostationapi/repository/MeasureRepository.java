package fr.jgrand.springmeteostationapi.repository;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {

    Optional<Measure> findFirstByTypeOrderByMeasureDateDesc(MeasureType measureType);

    List<Measure> findByTypeAndMeasureDateBetween(MeasureType measureType, LocalDateTime startDate, LocalDateTime endDate);
}
