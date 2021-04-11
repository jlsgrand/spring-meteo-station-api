package fr.jgrand.springmeteostationapi.controller;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import fr.jgrand.springmeteostationapi.repository.MeasureRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/measures")
@CrossOrigin("*")
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

    @GetMapping("/top")
    public ResponseEntity<Measure> getTopMeasureByType(@RequestParam("measure-type") MeasureType measureType) {
        Optional<Measure> lastMeasure = measureRepository.findFirstByTypeOrderByValueDesc(measureType);
        return lastMeasure.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Measure> getMeasuresByType(@RequestParam("measure-type") MeasureType measureType,
                                           @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                           @RequestParam("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return measureRepository.findByTypeAndMeasureDateBetween(measureType, startDate, endDate);
    }

    @PostMapping
    public ResponseEntity<Measure> storeMeasure(@RequestBody Measure measure) {
        return ResponseEntity.ok(measureRepository.save(measure));
    }
}
