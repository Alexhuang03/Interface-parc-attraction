package Vue;

import Modele.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Interface graphique pour l'espace client.
 * Permet d'accéder aux attractions, réservations, informations personnelles et de se déconnecter.
 */
public class UserVue {

    private final Connection connexion;

    /**
     * Crée et affiche la fenêtre de l'espace client.
     *
     * @param nomClient Nom du client connecté.
     * @param connexion Connexion à la base de données.
     * @param client    Informations du client connecté.
     */
    public UserVue(String nomClient, Connection connexion,User client) {
        this.connexion = connexion;

            Stage stage = new Stage();
            stage.setTitle("Espace Client");

            VBox headerBox = creerHeader(nomClient);
            VBox boutonsBox = creerBoutons(stage, client);

            VBox content = new VBox(20, headerBox, boutonsBox);
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(30));
            content.setStyle("-fx-background-color: #f5f5f5;");

            Scene scene = new Scene(content, 500, 400);
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Crée l'en-tête de la page avec un message de bienvenue.
     *
     * @param nomClient Nom du client.
     * @return VBox contenant le titre.
     */
    private VBox creerHeader(String nomClient) {
        Label titre = new Label("Bienvenue " + nomClient);
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.setPadding(new Insets(10, 0, 20, 0));
        return titreBox;
    }

    /**
     * Crée les boutons d'actions disponibles pour le client.
     *
     * @param stage  Fenêtre principale pour la fermer en cas de déconnexion.
     * @param client Informations du client connecté.
     * @return VBox contenant les boutons.
     */
    private VBox creerBoutons(Stage stage, User client) {
        Button btnAttractions = creerBouton("Voir les attractions disponibles", () -> {
            new AttractionsListeVue(connexion, client);
        });

        Button btnInfos = creerBouton("Voir mes informations", () -> {
            new ProfileVue(connexion, client);
        });

        Button btnDeconnexion = creerBouton("Se déconnecter", () -> {
            stage.close();
            new AccueilVue(connexion);
        });

        VBox boutonsBox = new VBox(15,
                btnAttractions,
                btnInfos,
                btnDeconnexion
        );
        boutonsBox.setAlignment(Pos.CENTER);
        return boutonsBox;
    }

    /**
     * Crée un bouton stylisé avec une action au clic.
     *
     * @param texte  Texte du bouton.
     * @param action Action à exécuter lors du clic.
     * @return Bouton stylisé.
     */
    private Button creerBouton(String texte,Runnable action) {
        Button button = new Button(texte);
        button.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10 25;"
        );
        button.setMaxWidth(300);
        button.setCursor(javafx.scene.Cursor.HAND);
        button.setOnAction(e -> action.run());
        return button;
    }
}
