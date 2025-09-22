package Vue;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Classe représentant la vue d'accueil du parc d'attractions.
 * Affiche une image de fond avec un message de bienvenue et des boutons de navigation.
 */
public class AccueilVue {

    private final Connection connexion;

    /**
     * Constructeur de la vue d'accueil.
     *
     * @param connexion La connexion à la base de données utilisée pour les autres vues
     */
    public AccueilVue(Connection connexion) {
        this.connexion = connexion;
        Platform.runLater(this::creerFenetreAccueil);
    }

    /**
     * Crée et affiche la fenêtre principale de l'accueil.
     * Configure l'image de fond, le texte de bienvenue et les boutons de navigation.
     */
    private void creerFenetreAccueil() {
        Stage stage = new Stage();
        stage.setTitle("Parc d'Attractions - Page d'Accueil");

        // Charger l'image de fond
        ImageView backgroundImage = chargerImageAccueil();
        backgroundImage.setPreserveRatio(false);
        backgroundImage.fitWidthProperty().bind(stage.widthProperty());
        backgroundImage.fitHeightProperty().bind(stage.heightProperty());

        // Créer le BorderPane principal
        BorderPane mainPane = new BorderPane();

        // Partie haute - Texte de bienvenue avec sous-titre remonté
        VBox headerBox = creerHeader();
        headerBox.setPadding(new Insets(40, 0, 0, 0)); // Marge supérieure réduite pour remonter le texte
        BorderPane.setAlignment(headerBox, Pos.TOP_CENTER);
        mainPane.setTop(headerBox);

        // Partie basse - Boutons en horizontal
        HBox boutonsBox = creerBoutonsHorizontal(stage);
        boutonsBox.setPadding(new Insets(0, 0, 60, 0));
        BorderPane.setAlignment(boutonsBox, Pos.BOTTOM_CENTER);
        mainPane.setBottom(boutonsBox);

        // Créer la disposition finale
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImage, mainPane);

        // Configurer la scène
        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crée le header contenant le titre et le sous-titre de bienvenue.
     *
     * @return VBox contenant les éléments textuels de bienvenue
     */
    private VBox creerHeader() {
        Label titre = new Label("Bienvenue au Parc d'Attractions !");
        titre.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label sousTitre = new Label("Plongez dans un univers d'aventures et de sensations fortes.");
        sousTitre.setStyle("-fx-font-size: 19px; -fx-text-fill: #34495e; -fx-translate-y: -15;"); // Translation vers le haut

        VBox headerBox = new VBox(8, titre, sousTitre); // Espacement réduit entre titre et sous-titre
        headerBox.setAlignment(Pos.CENTER);
        return headerBox;
    }

    /**
     * Crée la barre de boutons horizontale en bas de l'écran.
     *
     * @param stage La fenêtre principale à fermer lors des navigations
     * @return HBox contenant les boutons de navigation
     */
    private HBox creerBoutonsHorizontal(Stage stage) {
        Button btnConnexion = creerBouton("Se connecter", () -> {
            new ConnexionVue(connexion);
            stage.close();
        });

        Button btnClient = creerBouton("Inscription", () -> {
            new InscriptionVue(connexion);
            stage.close();
        });

        Button btnInvite = creerBouton("Mode Invité", () -> {
            new AttractionsListeVue(connexion, null);
            stage.close();
        });

        HBox boutonsBox = new HBox(25, btnConnexion, btnClient, btnInvite);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(25));
        return boutonsBox;
    }

    /**
     * Charge l'image de fond de l'accueil.
     *
     * @return ImageView contenant l'image chargée ou une ImageView vide en cas d'erreur
     */
    private ImageView chargerImageAccueil() {
        try {
            Image image = new Image(getClass().getResource("/Vue/accueil.jpg").toExternalForm());
            return new ImageView(image);
        } catch (Exception e) {
            System.out.println("Image non trouvée !");
            e.printStackTrace();
            return new ImageView();
        }
    }

    /**
     * Crée un bouton stylisé avec une action associée.
     *
     * @param texte Le texte à afficher sur le bouton
     * @param action L'action à exécuter lors du clic
     * @return Le bouton configuré
     */
    private Button creerBouton(String texte, Runnable action) {
        Button button = new Button(texte);
        button.setStyle(
                "-fx-background-color: #A7001E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 12 25;");
        button.setPrefWidth(220);
        button.setOnAction(e -> action.run());
        return button;
    }
}