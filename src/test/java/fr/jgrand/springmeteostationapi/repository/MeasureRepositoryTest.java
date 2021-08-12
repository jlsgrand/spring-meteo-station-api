package fr.jgrand.springmeteostationapi.repository;

import fr.jgrand.springmeteostationapi.model.Measure;
import fr.jgrand.springmeteostationapi.model.MeasureType;
import fr.jgrand.springmeteostationapi.model.MeasureUnit;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * J'utilise l'annotation @DataJpaTest pour que Spring puisse charger le "contexte" qui va bien pour tester mon repository.
 * Spring va mettre en place tous les composants nécessaires pour tester le repository. Il va notamment lancer une base
 * de données embarqué (c'est pour ça que j'ai ajouté la dépendance H2 dans le pom.xml).
 */
@DataJpaTest
public class MeasureRepositoryTest {

    // Le TestEntityManager va nous permettre d'écrire en base de données pour pouvoir ensuite tester le repository
    @Autowired
    private TestEntityManager entityManager;

    // J'injecte mon MeasureRepository pour pouvoir le tester
    @Autowired
    private MeasureRepository repository;

    /**
     * Je teste ici ce qu'il se passe quand je demande la dernière mesure de type CO2 à mon repository.
     * Je m'attends à ce qu'il me renvoie la mesure la plus récente de type CO2 stockée en base.
     */
    @Test
    void shouldFindLastMeasure() {
        // Je sauvegarde deux mesures de test en base de données (celle avec la valeur 400 sera la plus récente).
        this.entityManager.persist(new Measure(MeasureType.CO2, MeasureUnit.PPM, BigDecimal.valueOf(400), LocalDateTime.now()));
        this.entityManager.persist(new Measure(MeasureType.CO2, MeasureUnit.PPM, BigDecimal.valueOf(405), LocalDateTime.now().minusDays(1)));

        // Je demande au repository de récupérer la dernière mesure
        Optional<Measure> measure = this.repository.findFirstByTypeOrderByMeasureDateDesc(MeasureType.CO2);

        // Je m'assure qu'elle est bien présente
        assertThat(measure).isNotEmpty();

        // Ensuite je m'assure que la mesure retrouvée est bien celle qui a la valeur 400 (la plus récente).
        Condition<Measure> isLast = new Condition<>(measure1 -> measure1.getValue().compareTo(BigDecimal.valueOf(400)) == 0, "Last Measure value is 400");
        assertThat(measure).hasValueSatisfying(isLast);
    }

    // TODO : A vous de jouer pour les autres fonctions du repository.
}