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
 * Interface graphique pour l'espace administrateur.
 * Permet de gérer les attractions, les clients, les réservations, et d'accéder aux statistiques.
 */
public class AdminVue {

    private final Connection connexion;

    /**
     * Crée et affiche la fenêtre de l'espace administrateur.
     *
     * @param adminNom  Nom de l'administrateur connecté.
     * @param connexion Connexion à la base de données.
     * @param user      Informations de l'utilisateur connecté.
     */
    public AdminVue(String adminNom, Connection connexion, User user) {
        this.connexion = connexion;

        // Initialisation de la fenêtre principale
        Stage stage = new Stage();
        stage.setTitle("Espace Administrateur");

        VBox headerBox = creerHeader(adminNom);// Création de l'en-tête

        VBox boutonsBox = creerBoutons(stage, user);// Création des boutons

        // Mise en page principale
        VBox root = new VBox(20, headerBox, boutonsBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Affichage de la scène
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crée l'en-tête avec le nom de l'administrateur.
     *
     * @param adminNom Nom de l'administrateur.
     * @return VBox contenant le titre d'accueil.
     */
    private VBox creerHeader(String adminNom) {
        Label titre = new Label("Bienvenue, " + adminNom);
        titre.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.setPadding(new Insets(20, 0, 10, 0));
        return titreBox;
    }

    /**
     * Crée tous les boutons d'actions de l'administrateur.
     *
     * @param stage Fenêtre actuelle (pour la fermer en cas de déconnexion).
     * @param user  Informations de l'utilisateur connecté.
     * @return VBox contenant tous les boutons.
     */
    private VBox creerBoutons(Stage stage, User user) {
        Button btnModifierAttraction = creerBouton("Modifier les attractions", () -> new AttractionsAdminVue(connexion));
        Button btnAjouterAttraction = creerBouton("Ajouter une attraction", () -> new AttractionAjoutVue(connexion));
        Button btnGererClients = creerBouton("Gérer les clients", () -> new ClientListeVue(connexion));
        Button btnGererReservations = creerBouton("Gérer les réservations", () -> new ReservationAdminVue(connexion));
        Button btnVoirStatistiques = creerBouton("Voir statistiques attractions", () -> new StatistiquesAttractionsVue().afficher(connexion));
        Button btnVoirInfos = creerBouton("Voir mes informations", () -> new ProfileVue(connexion, user));
        Button btnDeconnexion = creerBouton("Se déconnecter", "#95a5a6", () -> {
            stage.close();
            new AccueilVue(connexion);
        });

        VBox boutonsBox = new VBox(15,
                btnModifierAttraction,
                btnAjouterAttraction,
                btnGererClients,
                btnGererReservations,
                btnVoirStatistiques,
                btnVoirInfos,
                btnDeconnexion
        );
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(30));
        boutonsBox.setMaxWidth(350);
        return boutonsBox;
    }

    /**
     * Crée un bouton stylisé avec une action au clic (couleur par défaut bleue).
     *
     * @param texte  Texte du bouton.
     * @param action Action à exécuter lors du clic.
     * @return Bouton stylisé.
     */
    private Button creerBouton(String texte, Runnable action) {
        return creerBouton(texte, "#3498db", action);
    }

    /**
     * Crée un bouton stylisé avec une couleur personnalisée et une action au clic.
     *
     * @param texte    Texte du bouton.
     * @param colorHex Couleur de fond en hexadécimal.
     * @param action   Action à exécuter lors du clic.
     * @return Bouton stylisé.
     */
    private Button creerBouton(String texte, String colorHex, Runnable action) {
        Button button = new Button(texte);
        button.setStyle(
                "-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 12 25;"
        );
        button.setPrefWidth(300);
        button.setOnAction(e -> action.run());
        return button;
    }
}
