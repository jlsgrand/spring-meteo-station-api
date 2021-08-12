package fr.jgrand.springmeteostationapi.controller;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import fr.jgrand.springmeteostationapi.model.MeasureUnit;
import fr.jgrand.springmeteostationapi.repository.MeasureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * J'utilise l'annotation @WebMvcTest pour que Spring puisse charger le "contexte" qui va bien pour tester mon controller.
 * Spring va mettre en place tous les composants nécessaires pour tester le controller.
 */
@WebMvcTest(MeasureController.class)
public class MeasureControllerTests {

    // MockMvc permet de simuler les requêtes HTTP à envoyer au controller
    @Autowired
    private MockMvc mvc;

    // Ici j'utilise l'annotation @MockBean pour que Spring puisse injecter un "faux" MeasureRepository dans mon controller
    @MockBean
    private MeasureRepository measureRepository;

    /**
     * Je teste le fonctionnement de mon controller quand on lui demande la dernière mesure pour le type CO2 et que celle-ci existe.
     * Je m'attends à ce que mon controller renvoie une réponse HTTP avec code 200
     */
    @Test
    void lastMeasureFound() throws Exception {
        /*
         * Ici je "mock" le fonctionnement de mon repository. Comme je veux tester mon controller et son fonctionnement
         * seulement, j'utilise un mock (un faux) MeasureController. Je dois donc mocker les fonctions qui m'intéressent
         * ==> dans le cas d'un appel controller pour récupérer une "last measure" c'est la fonction findFirstByTypeOrderByMeasureDateDesc
         * du repo qui sera appelée. C'est donc elle que je dois "mocker".
         */
        Measure testMeasure = new Measure(MeasureType.CO2, MeasureUnit.PPM, BigDecimal.valueOf(406), LocalDateTime.now());
        when(this.measureRepository.findFirstByTypeOrderByMeasureDateDesc(MeasureType.CO2)).thenReturn(Optional.of(testMeasure));

        /*
         * Ici j'envoie une requête à mon controller sur /api/measures/last?measure-type=CO2.
         * Et je m'attends à ce que mon controller me réponde OK vu que le repo renvoie bien une mesure.
         */
        this.mvc.perform(get("/api/measures/last?measure-type=CO2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(406))
                .andExpect(jsonPath("$.type").value("CO2"));
    }

    /**
     * Je teste le fonctionnement de mon controller quand on lui demande la dernière mesure pour le type CO2 mais on imagine
     * qu'il n'y a pas encore de mesure dans la base de données pour ce type de mesure. Dans ce cas, mon controller doit répondre
     * par un code 404 : NOT_FOUND.
     */
    @Test
    void lastMeasureNotFound() throws Exception {
        when(this.measureRepository.findFirstByTypeOrderByMeasureDateDesc(MeasureType.CO2)).thenReturn(Optional.empty());

        /*
         * Ici j'envoie une requête à mon controller sur /api/measures/last?measure-type=CO2.
         * Et je m'attends à ce que mon controller me réponde NOT_FOUND vu que le repo ne renvoie pas de mesure.
         */
        this.mvc.perform(get("/api/measures/last?measure-type=CO2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Je teste le fonctionnement de mon controller quand on lui demande la dernière mesure pour un type qui n'existe pas.
     * Dans ce cas, mon controller doit répondr par un code 400 : BAD_REQUEST.
     */
    @Test
    void lastMeasureNotFoundBecauseOfWrongMeasureType() throws Exception {
        when(this.measureRepository.findFirstByTypeOrderByMeasureDateDesc(any())).thenReturn(Optional.empty());

        /*
         * Ici j'envoie une requête à mon controller sur /api/measures/last?measure-type=KikooLol.
         * Et je m'attends à ce que mon controller me réponde BAD_REQUEST vu que le MeasureType n'existe pas.
         */
        this.mvc.perform(get("/api/measures/last?measure-type=KikooLol").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // TODO : A vous de jouer pour les autres fonctions du controller.
}