package Vue;

import Controleur.AttractionControleur;
import Modele.Attraction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.FloatStringConverter;
import java.sql.Connection;

/**
 * Vue permettant de consulter, modifier, supprimer et enregistrer
 * les attractions du parc via une interface JavaFX.
 */
public class AttractionsAdminVue {
    /**
     * Constructeur de la vue d'administration des attractions.
     * Affiche une table modifiable avec options de sauvegarde, suppression et retour.
     *
     * @param connexion Connexion active à la base de données
     */
    public AttractionsAdminVue(Connection connexion) {
        Stage stage = new Stage();
        stage.setTitle("Modifier les attractions");

        AttractionControleur controleur = new AttractionControleur(connexion);

        // Bouton Retour
        Button btnRetour = new Button("← Retour");
        btnRetour.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #3498db;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1px;" +      // Épaisseur de la bordure
                        "-fx-border-radius: 5px;" +     // Arrondi des coins de la bordure
                        "-fx-cursor: hand;"
        );
        btnRetour.setOnAction(e -> stage.close());

        HBox topBar = new HBox(btnRetour);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));

        // Titre principal
        Label titre = new Label("Liste des différentes attractions");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.setPadding(new Insets(10, 0, 20, 0));

        // TableView modifiable
        TableView<Attraction> tableView = new TableView<>();
        tableView.setEditable(true);

        ObservableList<Attraction> data = FXCollections.observableArrayList(
                controleur.getAttractions()
        );

        // Colonne ID - Non modifiable
        TableColumn<Attraction, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idAttraction"));
        colId.setEditable(false);

        // Colonne Nom - Modifiable
        TableColumn<Attraction, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colNom.setCellFactory(TextFieldTableCell.forTableColumn());
        colNom.setOnEditCommit(e -> e.getRowValue().setNom(e.getNewValue()));
        colNom.setEditable(true);

        // Colonne Type - Modifiable
        TableColumn<Attraction, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colType.setCellFactory(ComboBoxTableCell.forTableColumn("Manège", "Spectacle", "Parcours", "Aquatique"));
        colType.setOnEditCommit(e -> e.getRowValue().setType(e.getNewValue()));
        colType.setEditable(true);

        // Colonne Description - Modifiable
        TableColumn<Attraction, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        colDescription.setOnEditCommit(e -> e.getRowValue().setDescription(e.getNewValue()));
        colDescription.setEditable(true);

        // Colonne Capacité - Modifiable
        TableColumn<Attraction, Integer> colCapacite = new TableColumn<>("Capacité");
        colCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        colCapacite.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCapacite.setOnEditCommit(e -> e.getRowValue().setCapacite(e.getNewValue()));
        colCapacite.setEditable(true);

        // Colonne Durée - Modifiable
        TableColumn<Attraction, String> colDuree = new TableColumn<>("Durée");
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colDuree.setCellFactory(TextFieldTableCell.forTableColumn());
        colDuree.setOnEditCommit(e -> e.getRowValue().setDuree(e.getNewValue()));
        colDuree.setEditable(true);

        // Colonne Prix - Modifiable
        TableColumn<Attraction, Float> colPrix = new TableColumn<>("Prix");
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colPrix.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colPrix.setOnEditCommit(e -> e.getRowValue().setPrix(e.getNewValue()));
        colPrix.setEditable(true);

        // Colonne Statut - Modifiable
        TableColumn<Attraction, Attraction.StatutAttraction> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setCellFactory(ComboBoxTableCell.forTableColumn(Attraction.StatutAttraction.values()));
        colStatut.setOnEditCommit(e -> e.getRowValue().setStatut(e.getNewValue()));
        colStatut.setEditable(true);

        // Colonne Action - Suppression
        TableColumn<Attraction, Void> colSupprimer = new TableColumn<>("Action");
        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btnSupprimer = new Button("Supprimer");
            {
                btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 8;");
                btnSupprimer.setOnAction(event -> {
                    Attraction attraction = getTableView().getItems().get(getIndex());
                    boolean confirmed = showConfirmationDialog("Supprimer l'attraction " + attraction.getNom() + " ?");
                    if (confirmed) {
                        if (controleur.supprimerAttraction(attraction.getIdAttraction())) {
                            data.remove(attraction);
                        } else {
                            showErrorDialog("Erreur lors de la suppression !");
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnSupprimer);
                }
            }
        });

        tableView.getColumns().addAll(colId, colNom, colType, colDescription, colCapacite, colDuree, colPrix, colStatut, colSupprimer);
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Bouton Sauvegarde
        Button btnSauvegarder = new Button("Enregistrer les modifications");
        btnSauvegarder.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10 30;"
        );
        btnSauvegarder.setCursor(javafx.scene.Cursor.HAND);
        btnSauvegarder.setOnAction(e -> {
            boolean success = controleur.sauvegarderModifications(data);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Toutes les modifications ont été enregistrées !");
                alert.showAndWait();

                // Rafraîchir les données depuis la base
                data.setAll(controleur.getAttractions());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText(null);
                alert.setContentText("Certaines modifications n'ont pas pu être enregistrées !");
                alert.showAndWait();
            }
        });

        // Layout principal avec espacement de 15px
        VBox layout = new VBox(15, topBar, titreBox, tableView, btnSauvegarder);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f5f5f5;");

        Scene scene = new Scene(layout, 1000, 900);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Affiche une boîte de confirmation.
     *
     * @param message Message de confirmation
     * @return true si l'utilisateur confirme, false sinon
     */
    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Affiche une boîte de dialogue d'erreur.
     *
     * @param message Message à afficher
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}