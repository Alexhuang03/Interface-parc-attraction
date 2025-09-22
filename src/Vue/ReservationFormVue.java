package Vue;

import Controleur.ReservationControleur;
import Modele.Attraction;
import Modele.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
/**
 * Classe représentant le formulaire de réservation d'une attraction,
 * que ce soit pour un utilisateur connecté ou un invité.
 */
public class ReservationFormVue {
    /** Contrôleur de gestion des réservations */
    private final ReservationControleur reservationControleur;

    /**
     * Constructeur affichant le formulaire de réservation.
     *
     * @param user       Utilisateur connecté, ou null si mode invité
     * @param attraction Attraction à réserver
     * @param connexion  Connexion active à la base de données
     */
    public ReservationFormVue(User user, Attraction attraction, Connection connexion, Stage previousStage) {
        this.reservationControleur = new ReservationControleur(connexion); // On prépare le contrôleur
         Stage stage = new Stage();
        if (previousStage != null) {
            previousStage.close();
        }
            stage.setTitle("Réserver une attraction");

            VBox root = new VBox(15);
        Button retourBtn = new Button("← Retour");
        styleSmallButton(retourBtn);
        retourBtn.setOnAction(ev -> {
            stage.close();
            if (user == null) {
                new AttractionsListeVue(connexion, null); // retour au choix attraction invité
            } else {
                new AttractionsListeVue(connexion, user); // retour au choix attraction utilisateur
            }
        });

// Barre de retour en haut
        HBox retourBar = new HBox(retourBtn);
        retourBar.setAlignment(Pos.TOP_LEFT);
        retourBar.setPadding(new Insets(5, 10, 0, 10));
            root.setPadding(new Insets(25));
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: #f5f5f5;");

            Label titre = new Label((user == null ? "🎫 Réservation invité" : "🎟️ Réserver ") + attraction.getNom());
            titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("Choisir une date de réservation");
            datePicker.setMaxWidth(300);

            TextField nomField = new TextField();
            nomField.setPromptText("Nom complet");
            nomField.setMaxWidth(300);

            TextField emailField = new TextField();
            emailField.setPromptText("Adresse email");
            emailField.setMaxWidth(300);

            // Les champs nom/email ne sont visibles QUE pour les invités
            if (user != null) {
                nomField.setVisible(false);
                emailField.setVisible(false);
            }

            Button reserverBtn = new Button("Réserver");
            reserverBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 8;");

            reserverBtn.setOnAction(e -> {
                if (datePicker.getValue() == null) {
                    showAlert("Erreur", "Veuillez sélectionner une date.");
                    return;
                }

                if (user == null) { // Mode invité
                    String nom = nomField.getText();
                    String email = emailField.getText();

                    if (nom.isEmpty() || email.isEmpty()) {
                        showAlert("Erreur", "Veuillez remplir tous les champs.");
                        return;
                    }

                    Date selectedDate = Date.valueOf(datePicker.getValue());

                    // Appel du contrôleur
                    reservationControleur.creerReservationInviteAvecPaiement(nom, email, attraction, selectedDate, () -> {
                        showAlert("Succès", "Réservation et paiement effectués !");
                        new AccueilVue(connexion);
                    });

                } else { // Utilisateur connecté
                    Date selectedDate = Date.valueOf(datePicker.getValue());

                    reservationControleur.creerReservationAvecPaiement(user, attraction, selectedDate, () -> {
                        showAlert("Succès", "Réservation et paiement effectués !");
                        stage.close();
                    });
                }
            });

        root.getChildren().addAll(retourBar, titre);
        if (user == null) root.getChildren().addAll(nomField, emailField); // Ajout des champs invités seulement
        root.getChildren().addAll(datePicker, reserverBtn);

            stage.setScene(new Scene(root, 400, user == null ? 400 : 300));
            stage.show();

    }
    /**
     * Affiche une alerte simple d'information.
     *
     * @param title   Titre de l'alerte
     * @param message Message à afficher
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Applique un style spécial pour les petits boutons (bouton retour).
     *
     * @param button Bouton à styliser
     */
    private void styleSmallButton(Button button) {
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #3498db;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 4 10;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-width: 1;"
        );
        button.setCursor(javafx.scene.Cursor.HAND);
    }

}
