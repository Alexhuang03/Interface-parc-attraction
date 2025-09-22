package Vue;

import Controleur.ConnexionControleur;
import Modele.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
/**
 * Vue JavaFX permettant à un utilisateur (client ou administrateur) de se connecter à son compte.
 * Affiche un formulaire de connexion et un bouton retour vers l'accueil.
 */
public class ConnexionVue {
    private ConnexionControleur controleur;
    /**
     * Constructeur de la fenêtre de connexion.
     *
     * @param connexion Connexion à la base de données
     */
    public ConnexionVue(Connection connexion) {
        this.controleur = new ConnexionControleur(connexion);
        Platform.runLater(this::creerEtAfficher);
    }
    /**
     * Crée et affiche l'interface utilisateur pour la connexion.
     */
    private void creerEtAfficher() {
        Stage stage = new Stage();
        stage.setTitle("Connexion");

        // Création des composants UI
        Label titre = creerTitre();
        TextField emailField = new TextField();
        emailField.setPromptText("exemple@mail.com");
        PasswordField mdpField = new PasswordField();
        mdpField.setPromptText("••••••••");
        Button connexionButton = creerBoutonConnexion(emailField, mdpField, stage);
        Button retourBtn = creerBoutonRetour(stage);

        // Organisation du layout
        GridPane formGrid = creerFormulaire(emailField, mdpField);
        VBox layoutPrincipal = new VBox(25, titre, formGrid, connexionButton);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(20, 40, 20, 40));

        BorderPane root = new BorderPane();
        root.setTop(creerBarreRetour(retourBtn));
        root.setCenter(layoutPrincipal);
        root.setStyle("-fx-background-color: #f5f5f5;");
        BorderPane.setAlignment(layoutPrincipal, Pos.CENTER);
        stage.setScene(new Scene(root, 900, 700));
        stage.show();
    }
    /**
     * Crée le titre de la page de connexion.
     *
     * @return Label stylisé pour le titre
         */
    private Label creerTitre() {
        Label titre = new Label("Connexion à votre compte");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        return titre;
    }
    /**
     * Crée le formulaire avec les champs email et mot de passe.
     *
     * @param emailField Champ texte pour l'email
     * @param mdpField   Champ mot de passe
     * @return Grille contenant les champs
     */
    private GridPane creerFormulaire(TextField emailField, PasswordField mdpField) {
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(30, 50, 30, 50));
        formGrid.setAlignment(Pos.CENTER);
        formGrid.add(new Label("Email :"), 0, 0);
        formGrid.add(emailField, 1, 0);
        formGrid.add(new Label("Mot de passe :"), 0, 1);
        formGrid.add(mdpField, 1, 1);
        return formGrid;
    }
    /**
     * Crée le bouton "Se connecter" et définit son comportement.
     *
     * @param emailField Champ email
     * @param mdpField   Champ mot de passe
     * @param stage      Fenêtre actuelle
     * @return Bouton stylisé prêt à l'emploi
     */
    private Button creerBoutonConnexion(TextField emailField, PasswordField mdpField, Stage stage) {
        Button btn = new Button("Se connecter");
        styleButton(btn);

        btn.setOnAction(e -> {
            User utilisateur = controleur.connecterUtilisateur(
                    emailField.getText(),
                    mdpField.getText()
            );

            if (utilisateur != null) {
                controleur.gererConnexionReussie(utilisateur); // Retirez le deuxième paramètre
                stage.close();
            } else {
                controleur.afficherErreurConnexion();
            }
        });

        return btn;
    }
    /**
     * Crée un bouton de retour vers l'accueil.
     *
     * @param stage Fenêtre actuelle
     * @return Bouton retour
     */
    private Button creerBoutonRetour(Stage stage) {
        Button btn = new Button("← Retour");
        styleSmallButton(btn);
        btn.setOnAction(ev -> {
            stage.close();
            new AccueilVue(controleur.getConnexion()); // Utilisez directement le getter
        });
        return btn;
    }
    /**
     * Crée une barre supérieure contenant uniquement le bouton retour.
     *
     * @param retourBtn Bouton retour
     * @return HBox alignée en haut
     */
    private HBox creerBarreRetour(Button retourBtn) {
        HBox topBar = new HBox(retourBtn);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(5, 10, 0, 10));
        return topBar;
    }
    /**
     * Applique un style visuel aux boutons principaux.
     *
     * @param button Bouton à styliser
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
     * Applique un style visuel spécial pour les petits boutons (retour).
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