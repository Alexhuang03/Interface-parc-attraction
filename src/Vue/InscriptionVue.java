package Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import Controleur.InscriptionControleur;
import java.sql.Connection;

/**
 * Fenêtre d'inscription d'un client.
 * Permet de créer un compte client avec nom, prénom, date de naissance, téléphone, email et mot de passe.
 */
public class InscriptionVue {

    /** Connexion à la base de données */
    private Connection connexion;

    /**
     * Initialise l'interface d'inscription pour un client.
     *
     * Cette méthode crée l'interface graphique, incluant des champs pour l'inscription du client,
     * un bouton pour soumettre les informations et un bouton pour revenir à la page d'accueil.
     *
     * @param connexion Connexion à la base de données.
     */
    public InscriptionVue(Connection connexion) {
        this.connexion = connexion;

        // Lancement de l'interface JavaFX dans le thread approprié
            // Création de la fenêtre d'inscription
            Stage stage = new Stage();
            stage.setTitle("Inscription Client");

            // Création du titre
            VBox header = creerHeader();

            // Création du formulaire d'inscription
            GridPane formGrid = creerFormulaire();

            // Création des boutons (Inscription et Retour)
            Button inscriptionBtn = new Button("S'inscrire");
            styleButton(inscriptionBtn);  // Applique un style au bouton d'inscription
            Button retourBtn = new Button("← Retour");
            styleSmallButton(retourBtn);  // Applique un style au bouton de retour

            // Action du bouton retour
            retourBtn.setOnAction(ev -> {
                stage.close();
                new AccueilVue(connexion); // Retour à l'écran d'accueil
            });

            // Action du bouton d'inscription
            inscriptionBtn.setOnAction(e -> {
                // Appel au contrôleur pour inscrire le client
                boolean success = InscriptionControleur.inscrireClient(
                        connexion,
                        ((TextField) formGrid.getChildren().get(1)).getText(),
                        ((TextField) formGrid.getChildren().get(3)).getText(),
                        ((DatePicker) formGrid.getChildren().get(5)).getValue(),
                        ((TextField) formGrid.getChildren().get(7)).getText(),
                        ((TextField) formGrid.getChildren().get(9)).getText(),
                        ((PasswordField) formGrid.getChildren().get(11)).getText()
                );

                // Affichage du résultat de l'inscription
                Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle(success ? "Inscription réussie" : "Erreur");
                alert.setHeaderText(null);
                alert.setContentText(success ? "Client inscrit avec succès !" : "Erreur lors de l'inscription.");
                alert.showAndWait();

                // Si l'inscription est réussie, retour à l'accueil
                if (success) new AccueilVue(connexion);
            });

            // Mise en page de l'interface
            HBox topBar = new HBox(retourBtn);
            topBar.setAlignment(Pos.TOP_LEFT);
            topBar.setPadding(new Insets(5, 10, 0, 10));

            VBox layout = new VBox(25, header, formGrid, inscriptionBtn);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20, 40, 20, 40)); // Espaces autour du contenu

            // Utilisation d'un BorderPane pour gérer la disposition de l'écran
            BorderPane root = new BorderPane();
            root.setTop(topBar);
            root.setCenter(layout);
            root.setStyle("-fx-background-color: #f5f5f5;");

            // Création de la scène et affichage
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Crée l'en-tête de la page avec le titre "Créer un compte client".
     *
     * @return VBox contenant le titre.
     */
    private VBox creerHeader() {
        Label titre = new Label("Créer un compte client");
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(titre);
        header.setAlignment(Pos.CENTER);
        return header;
    }

    /**
     * Crée le formulaire d'inscription avec des champs pour le nom, prénom, date de naissance, etc.
     *
     * @return GridPane contenant les champs de formulaire.
     */
    private GridPane creerFormulaire() {
        // Création des champs de saisie
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        DatePicker dateNaissancePicker = new DatePicker();
        TextField telField = new TextField();
        TextField emailField = new TextField();
        PasswordField mdpField = new PasswordField();

        // Création de la grille pour organiser les champs
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(40, 50, 40, 50));

        // Ajout des labels et des champs dans la grille
        formGrid.add(new Label("Nom :"), 0, 0);
        formGrid.add(nomField, 1, 0);
        formGrid.add(new Label("Prénom :"), 0, 1);
        formGrid.add(prenomField, 1, 1);
        formGrid.add(new Label("Date de naissance :"), 0, 2);
        formGrid.add(dateNaissancePicker, 1, 2);
        formGrid.add(new Label("Numéro de téléphone :"), 0, 3);
        formGrid.add(telField, 1, 3);
        formGrid.add(new Label("Email :"), 0, 4);
        formGrid.add(emailField, 1, 4);
        formGrid.add(new Label("Mot de passe :"), 0, 5);
        formGrid.add(mdpField, 1, 5);

        return formGrid;
    }

    /**
     * Applique un style visuel au bouton d'inscription.
     *
     * @param button Le bouton à styliser.
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

    /**
     * Applique un style visuel au bouton "Retour".
     *
     * @param button Le bouton à styliser.
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
