package Vue;

import Controleur.StatistiquesControleur;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Map;

/**
 * Cette classe affiche une fenêtre avec un graphique
 * montrant la popularité des attractions (nombre de réservations).
 */
public class StatistiquesAttractionsVue {

    /**
     * Affiche la fenêtre avec le graphique des statistiques.
     *
     * @param connexion la connexion à la base de données utilisée pour récupérer les statistiques.
     */
    public void afficher(Connection connexion) {
        // Création de la fenêtre (Stage)
        Stage stage = new Stage();
        stage.setTitle("Attractions les plus populaires");

        // Création des axes du graphique
        CategoryAxis xAxis = new CategoryAxis(); // Axe horizontal : noms des attractions
        NumberAxis yAxis = new NumberAxis();      // Axe vertical : nombre de réservations

        xAxis.setLabel("Attraction");
        yAxis.setLabel("Réservations");

        // Création du graphique en barres
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Nombre de Réservations par Attraction");

        // Série de données pour le graphique
        XYChart.Series<String, Number> dataSeries= new XYChart.Series<>();
        dataSeries.setName("Popularité");

        // Récupération des données via le contrôleur
        StatistiquesControleur controleur = new StatistiquesControleur(connexion);
        Map<String, Integer> stats = controleur.getReservationsParAttraction();

        // Remplissage de la série avec les données
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Ajout de la série au graphique
        barChart.getData().add(dataSeries);

        // Organisation de la mise en page
        VBox root =new VBox(barChart);
        root.setPadding(new Insets(20));

        // Création et affichage de la scène
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
