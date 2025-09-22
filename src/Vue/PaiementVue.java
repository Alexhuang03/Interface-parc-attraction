package Vue;

import Modele.Paiement;
import Modele.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Cette classe représente une fenêtre de paiement.
 * L'utilisateur saisit ses informations de carte pour effectuer un paiement.
 */
public class PaiementVue {

    /**
     * Affiche la fenêtre de paiement et exécute une action après paiement réussi.
     *
     * @param montant            Montant à payer
     * @param onPaiementEffectue Callback à exécuter une fois le paiement validé
     * @param user               Utilisateur effectuant le paiement (peut être null pour un invité)
     */
    public void afficher(float montant, Consumer<Paiement> onPaiementEffectue, User user) {
        Stage paiementStage = new Stage();
        paiementStage.initModality(Modality.APPLICATION_MODAL);
        paiementStage.setTitle("Paiement");

        Label montantLabel = new Label("Montant à payer: " + montant + "€");
        montantLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label numeroCarteLabel = new Label("Numéro de carte :");
        TextField numeroCarteField = new TextField();
        numeroCarteField.setPromptText("1234 5678 9012 3456");

        Label expirationLabel = new Label("Date d'expiration (MM/AA):");
        TextField expirationField = new TextField();
        expirationField.setPromptText("MM/AA");

        Label cvvLabel = new Label("CVV :");
        PasswordField cvvField = new PasswordField();
        cvvField.setPromptText("123");

        // Bouton Payer par carte
        Button payerCarteButton = new Button("Payer par carte");
        payerCarteButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        payerCarteButton.setOnAction(ev -> {
            // Validation des champs
            if (numeroCarteField.getText().isEmpty() ||
                    expirationField.getText().isEmpty() ||
                    cvvField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs de paiement.");
                return;
            }

            // Validation du numéro de carte (16 chiffres)
            String numeroCarte = numeroCarteField.getText().replaceAll("\\s+", "");
            if (numeroCarte.length() != 16 || !numeroCarte.matches("\\d{16}")) {
                showAlert("Erreur", "Le numéro de carte doit contenir exactement 16 chiffres.");
                return;
            }

            // Validation de la date d'expiration
            try {
                String[] parts = expirationField.getText().split("/");
                if (parts.length != 2) {
                    showAlert("Erreur", "Format de date invalide. Utilisez MM/AA.");
                    return;
                }

                int mois = Integer.parseInt(parts[0]);
                int annee = Integer.parseInt(parts[1]);

                if (mois < 1 || mois > 12) {
                    showAlert("Erreur", "Le mois doit être entre 01 et 12.");
                    return;
                }

                // Convertir en LocalDate (année 2000 + AA)
                LocalDate expirationDate = LocalDate.of(2000 + annee, mois, 1).withDayOfMonth(
                        LocalDate.of(2000 + annee, mois, 1).lengthOfMonth()
                );

                if (expirationDate.isBefore(LocalDate.now())) {
                    showAlert("Erreur", "La carte est expirée. Veuillez utiliser une carte valide.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "La date doit contenir uniquement des chiffres (format MM/AA).");
                return;
            } catch (Exception e) {
                showAlert("Erreur", "Format de date invalide. Utilisez MM/AA.");
                return;
            }

            // Validation du CVV (3 chiffres)
            if (cvvField.getText().length() != 3 || !cvvField.getText().matches("\\d{3}")) {
                showAlert("Erreur", "Le CVV doit contenir exactement 3 chiffres.");
                return;
            }

            // Si toutes les validations passent
            Paiement paiement = new Paiement(user, montant);
            paiement.setMoyenPaiement("Carte");
            paiement.setStatut(Paiement.StatutPaiement.EFFECTUE);
            onPaiementEffectue.accept(paiement);
            paiementStage.close();
        });

        // Bouton Payer en espèces
        Button payerEspecesButton = new Button("Payer en espèces");
        payerEspecesButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        payerEspecesButton.setOnAction(ev -> {
            Paiement paiement = new Paiement(user, montant);
            paiement.setMoyenPaiement("Espèces");
            paiement.setStatut(Paiement.StatutPaiement.EN_ATTENTE);
            onPaiementEffectue.accept(paiement);
            paiementStage.close();
        });

        // Layout pour les boutons
        HBox boutonsLayout = new HBox(10, payerCarteButton, payerEspecesButton);
        boutonsLayout.setAlignment(Pos.CENTER);

        VBox root = new VBox(15,
                montantLabel,
                numeroCarteLabel, numeroCarteField,
                expirationLabel, expirationField,
                cvvLabel, cvvField,
                boutonsLayout
        );
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        paiementStage.setScene(new Scene(root, 350, 300));
        paiementStage.showAndWait();
    }

    /**
     * Affiche une alerte d'erreur avec un titre et un message donnés.
     *
     * @param title   Titre de la fenêtre d'alerte
     * @param message Message d'erreur affiché
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
