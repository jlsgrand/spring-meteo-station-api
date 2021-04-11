package fr.jgrand.springmeteostationapi.repository;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {

    Optional<Measure> findFirstByTypeOrderByMeasureDateDesc(MeasureType measureType);

    List<Measure> findByType(MeasureType measureType);
}
