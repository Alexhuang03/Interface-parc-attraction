package Vue;

import Controleur.AttractionControleur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Cette classe affiche une interface JavaFX permettant à un administrateur
 * d'ajouter une nouvelle attraction dans la base de données.
 */
public class AttractionAjoutVue {

    /**
     * Constructeur qui initialise et affiche la fenêtre d'ajout d'attraction.
     *
     * @param connexion Connexion à la base de données MySQL
     */
    public AttractionAjoutVue(Connection connexion) {
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Attraction");

            AttractionControleur controleur = new AttractionControleur(connexion);

            /** Titre principal */
            Label titre = new Label("Nouvelle attraction");
            titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            VBox headerBox = new VBox(titre);
            headerBox.setAlignment(Pos.CENTER);

            /** Champs du formulaire */
            TextField nomField = new TextField();
            ComboBox<String> typeCombo = new ComboBox<>();
            typeCombo.getItems().addAll("Attraction", "Spectacle", "Parcours", "Autres");

            TextField capaciteField = new TextField();
            TextField dureeField = new TextField();
            TextField prixField = new TextField();
            TextField descriptionField = new TextField();


        ComboBox<String> statutCombo = new ComboBox<>();
            statutCombo.getItems().addAll("active", "inactive");
            statutCombo.getSelectionModel().selectFirst();

            /** Grille du formulaire */
            GridPane formGrid = new GridPane();
            formGrid.setVgap(15);
            formGrid.setHgap(10);
            formGrid.setPadding(new Insets(30));

            formGrid.add(new Label("Nom :"), 0, 0); formGrid.add(nomField, 1, 0);
            formGrid.add(new Label("Type :"), 0, 1); formGrid.add(typeCombo, 1, 1);
            formGrid.add(new Label("Capacité :"), 0, 2); formGrid.add(capaciteField, 1, 2);
            formGrid.add(new Label("Durée :"), 0, 3); formGrid.add(dureeField, 1, 3);
            formGrid.add(new Label("Prix :"), 0, 4); formGrid.add(prixField, 1, 4);
            formGrid.add(new Label("Description :"), 0, 5);formGrid.add(descriptionField, 1, 5);
            formGrid.add(new Label("Statut :"), 0, 6); formGrid.add(statutCombo, 1, 6);

            /** Bouton d'ajout */
            Button ajouterBtn = new Button("Ajouter");
            styleButton(ajouterBtn);

            /** Action lors du clic sur "Ajouter" */
            ajouterBtn.setOnAction(e -> {
                try {
                    boolean success = controleur.ajouterAttraction(
                            nomField.getText(),
                            typeCombo.getValue(),
                            capaciteField.getText(),
                            dureeField.getText(),
                            prixField.getText(),
                            statutCombo.getValue(),
                            descriptionField.getText()
                    );

                    Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                    alert.setTitle(success ? "Ajout réussi" : "Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText(success ? "Attraction ajoutée !" : "Erreur lors de l'ajout.");
                    alert.showAndWait();

                    if (success) stage.close();

                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Champs invalides");
                    alert.setHeaderText(null);
                    alert.setContentText("Vérifier que tous les champs sont bien remplis.");
                    alert.showAndWait();
                }
            });

            /** Organisation globale de la fenêtre */
            VBox layout = new VBox(30, headerBox, formGrid, ajouterBtn);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(40));
            layout.setStyle("-fx-background-color: #f5f5f5;");

            Scene scene = new Scene(layout, 550, 550);
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Applique un style visuel au bouton.
     *
     * @param button Le bouton à styliser
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

