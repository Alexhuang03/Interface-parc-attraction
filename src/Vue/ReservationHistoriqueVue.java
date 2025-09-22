package Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Cette classe représente la vue d'historique des réservations d'un client.
 * Elle affiche une liste des réservations et un bouton pour fermer la fenêtre.
 */
public class ReservationHistoriqueVue {

    /**
     * Constructeur de la vue.
     *
     * @param nomClient              nom du client connecté
     * @param historiqueReservations liste des réservations effectuées
     */
    public ReservationHistoriqueVue(String nomClient, List<String> historiqueReservations, boolean estInvite) {
            Stage stage = new Stage();
            // Définir le titre selon le type d'utilisateur
            String titreTexte = estInvite
                    ? "Historique de vos réservations (invité) : " + nomClient
                    : "Historique des réservations pour " + nomClient;
            stage.setTitle(titreTexte);

            // Titre de la page
            Label titre = new Label(titreTexte);
            titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
            VBox titreBox = new VBox(titre);
            titreBox.setAlignment(Pos.CENTER);
            titreBox.setPadding(new Insets(10, 0, 20, 0));

            // Liste affichant les réservations
            ListView<String> listView = new ListView<>();
            listView.getItems().addAll(historiqueReservations);
            listView.setStyle("-fx-font-size: 14px;");

            // Bouton pour fermer la fenêtre
            Button btnFermer = new Button("Fermer");
            styleButton(btnFermer);
            btnFermer.setOnAction(e -> stage.close());

            // Mise en page
            VBox layout = new VBox(20, titreBox, listView, btnFermer);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(30));
            layout.setStyle("-fx-background-color: #f5f5f5;");
            layout.setPrefWidth(500);

            Scene scene = new Scene(layout, 600, 450);
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Applique un style cohérent à un bouton.
     *
     * @param button bouton à styliser
     */
    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10 30;"
        );
        button.setCursor(javafx.scene.Cursor.HAND);
    }
}
