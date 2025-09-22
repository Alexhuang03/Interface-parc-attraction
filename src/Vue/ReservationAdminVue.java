package Vue;

import Controleur.ReservationControleur;
import Modele.Reservation;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;
/**
 * Classe représentant l'interface de gestion des réservations pour l'administrateur.
 * Permet de visualiser toutes les réservations et de modifier leur statut.
 */
public class ReservationAdminVue {
    /** Contrôleur permettant la gestion des réservations */
    private final ReservationControleur controleur;
    /**
     * Constructeur qui initialise et affiche la vue des réservations administrateur.
     *
     * @param connexion Connexion active à la base de données
     */
    public ReservationAdminVue(Connection connexion) {
        this.controleur = new ReservationControleur(connexion);
        afficher();
    }

    /**
     * Affiche la fenêtre principale listant toutes les réservations existantes.
     */
    public void afficher() {
            Stage stage = new Stage();
            stage.setTitle("Gestion des Réservations");

            VBox root = new VBox(15);
            root.setPadding(new Insets(20));

            Label titre = new Label("Liste des réservations");
            titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            List<Reservation> reservations = controleur.recupererToutesReservations(); // méthode à ajouter

            VBox reservationList = new VBox(10);
            for (Reservation res : reservations) {
                HBox ligne = new HBox(10);
                ligne.setPadding(new Insets(10));
                ligne.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8;");
                ligne.setSpacing(10);

                Label infos = new Label(
                        "ID: " + res.getIdReservation() +
                                " | Client: " + (res.getUser() != null
                                ? res.getUser().getUserNom()
                                : res.getInviteNom() + " (invité)") +
                                " | Attraction: " + res.getAttraction().getNom() +
                                " | Date: " + res.getDateReservation()
                );
                infos.setPrefWidth(400);

                ComboBox<Reservation.StatutReservation> statutCombo = new ComboBox<>();
                statutCombo.setItems(FXCollections.observableArrayList(Reservation.StatutReservation.values()));
                statutCombo.setValue(res.getStatut());

                Button modifierBtn = new Button("Modifier");
                modifierBtn.setOnAction(e -> {
                    Reservation.StatutReservation nouveauStatut = statutCombo.getValue();
                    res.setStatut(nouveauStatut);
                    controleur.mettreAJourStatut(res); // méthode à ajouter
                    showAlert("Succès", "Statut mis à jour !");
                });

                ligne.getChildren().addAll(infos, statutCombo, modifierBtn);
                reservationList.getChildren().add(ligne);
            }

            ScrollPane scrollPane = new ScrollPane(reservationList);
            scrollPane.setFitToWidth(true);

            root.getChildren().addAll(titre, scrollPane);

            Scene scene = new Scene(root, 700, 500);
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Affiche une alerte simple d'information.
     *
     * @param titre   Titre de l'alerte
     * @param message Message à afficher dans l'alerte
     */
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
