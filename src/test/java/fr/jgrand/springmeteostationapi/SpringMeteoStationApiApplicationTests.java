package fr.jgrand.springmeteostationapi;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import fr.jgrand.springmeteostationapi.model.MeasureUnit;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * J'utilise l'annotation @SpringBootTest pour que Spring puisse lancer tout le serveur, et ce afin de tester mon code
 * dans l'environnement complet. Ca nous donne des tests plus long à exécuter mais plus proches de l'utilisation finale.
 *
 * J'utilise aussi l'annotation @TestMethodOrder pour m'assurer que mes tests seront toujours joués dans le même ordre.
 * Ici c'est important car je vérifie qu'après mes deux insertions en base, lorsque je demande la dernière mesure, j'ai bien la bonne.
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringMeteoStationApiApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Je teste le fonctionnement de mon API quand on lui demande de stocker une mesure pour le type CO2.
     * Je m'attends à ce que mon API renvoie une réponse HTTP avec code 200, et que les propriétés de mon object stocké
     * soient retournées aussi.
     */
    @Test
    public void t01saveFirstCO2Measure() {
        // Je crée une mesure à sauvegarder et je l'envoie à l'API
        Measure measureToSave = new Measure(MeasureType.CO2, MeasureUnit.PPM, BigDecimal.valueOf(405), LocalDateTime.now());
        ResponseEntity<Measure> apiResponse = this.restTemplate.postForEntity("/api/measures", measureToSave, Measure.class);

        // Je m'assure que la requête est OK et que je récupère bien la mesure sauvegardée
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiResponse.getBody()).hasFieldOrPropertyWithValue("value", BigDecimal.valueOf(405));
        assertThat(apiResponse.getBody()).hasFieldOrPropertyWithValue("type", MeasureType.CO2);
        assertThat(apiResponse.getBody()).hasFieldOrPropertyWithValue("unit", MeasureUnit.PPM);
    }

    /**
     * Je teste le fonctionnement de mon API quand on lui demande de stocker une mesure pour le type CO2.
     * Je m'attends à ce que mon API renvoie une réponse HTTP avec code 200, et que les propriétés de mon object stocké
     * soient retournées aussi.
     */
    @Test
    public void t02saveSecondCO2Measure() throws Exception {
        // Je crée une mesure à sauvegarder et je l'envoie à l'API
        Measure measureToSave = new Measure(MeasureType.CO2, MeasureUnit.PPM, BigDecimal.valueOf(400), LocalDateTime.now().minusDays(1));
        ResponseEntity<Measure> apiResponse = this.restTemplate.postForEntity("/api/measures", measureToSave, Measure.class);

        // Je m'assure que la requête est OK et que je récupère bien la mesure sauvegardée
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(apiResponse.getBody()).hasFieldOrPropertyWithValue("value", BigDecimal.valueOf(400));
        assertThat(apiResponse.getBody()).hasFieldOrPropertyWithValue("type", MeasureType.CO2);
        assertThat(apiResponse.getBody()).hasFieldOrPropertyWithValue("unit", MeasureUnit.PPM);
    }

    /**
     * Je teste le fonctionnement de mon API quand on lui demande de récupérer la dernière mesure pour le type CO2.
     * Je m'attends à ce que mon API renvoie la mesure avec la valeur 405 que j'ai stocké en premier mais qui est la
     * dernière en date.
     */
    @Test
    public void t03getLastCO2MeasureShouldReturn405() throws Exception {
        // Je demande la dernière mesure de type CO2 à l'API
        Measure measure = this.restTemplate.getForObject("/api/measures/last?measure-type=CO2", Measure.class);

        // Je m'assure que j'ai bien la mesure et que ses propriétés sont cohérentes
        assertThat(measure).isNotNull();
        assertThat(measure.getValue()).isEqualByComparingTo(BigDecimal.valueOf(405));
        assertThat(measure).hasFieldOrPropertyWithValue("type", MeasureType.CO2);
        assertThat(measure).hasFieldOrPropertyWithValue("unit", MeasureUnit.PPM);
    }

    /**
     * Je teste le fonctionnement de mon API quand on lui demande de récupérer la dernière mesure pour le type Température.
     * Je m'attends à ce que mon API ne renvoie pas de mesure car je n'en ai pas stocké pour le type Température.
     */
    @Test
    public void t04getLastTempMeasureShouldReturnNull() {
        // Je demande à l'API la dernière mesure de type Température
        Measure measure = this.restTemplate.getForObject("/api/measures/last?measure-type=TEMPERATURE", Measure.class);

        // Je vérifie qu'il n'y en a pas
        assertThat(measure).isNull();
    }

    // TODO : à vous de jouer pour tester les autres endpoints de l'API
}
