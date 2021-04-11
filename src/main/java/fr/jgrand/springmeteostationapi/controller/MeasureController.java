package fr.jgrand.springmeteostationapi.controller;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import fr.jgrand.springmeteostationapi.repository.MeasureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/measures")
public class MeasureController {

    private final MeasureRepository measureRepository;

    public MeasureController(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    @GetMapping("/last")
    public ResponseEntity<Measure> getLastMeasureByType(@RequestParam("measure-type") MeasureType measureType) {
        Optional<Measure> lastMeasure = measureRepository.findFirstByTypeOrderByMeasureDateDesc(measureType);
        return lastMeasure.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Measure> getMeasuresByType(@RequestParam("measure-type") MeasureType measureType) {
        return measureRepository.findByType(measureType);
    }

    @PostMapping
    public ResponseEntity<Measure> storeMeasure(@RequestBody Measure measure) {
        measure.setMeasureDateToNow();
        return ResponseEntity.ok(measureRepository.save(measure));
    }
}
