package Vue;

import Controleur.ReservationControleur;
import Controleur.AttractionControleur;
import Modele.Attraction;
import Modele.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.*;

/**
 * Classe représentant la vue de la liste des attractions disponibles.
 * Affiche les attractions sous forme de cartes avec images et informations détaillées.
 * Permet la réservation et la consultation des réservations existantes.
 */
public class AttractionsListeVue {

    private final Connection connexion;
    private final User user;
    private final List<Attraction> attractions;
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();

    /**
     * Constructeur de la vue des attractions.
     *
     * @param connexion La connexion à la base de données
     * @param user L'utilisateur connecté (null si mode invité)
     */
    public AttractionsListeVue(Connection connexion, User user) {
        this.connexion = connexion;
        this.user = user;
        this.attractions = new AttractionControleur(connexion).getAttractionsActives();
        afficher();
    }

    /**
     * Affiche la fenêtre principale avec la liste des attractions.
     */
    private void afficher() {
        Stage stage = new Stage();
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);

        // Top section
        VBox topBox = createTopSection();

        // Attractions list
        if (attractions.isEmpty()) {
            content.getChildren().add(new Label("Aucune attraction disponible"));
        } else {
            attractions.forEach(a -> content.getChildren().add(createAttractionCard(a, stage)));
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(scrollPane);

        stage.setScene(new Scene(root, 900, 700));
        stage.show();
    }

    /**
     * Crée la section supérieure de la vue (message de bienvenue et boutons).
     *
     * @return VBox contenant les éléments de la section supérieure
     */
    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);

        if (user != null) {
            Label welcomeLabel = new Label("Bienvenue " + user.getUserNom() + " !");
            welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Button viewReservationsBtn = new Button("Voir mes réservations");
            viewReservationsBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");
            viewReservationsBtn.setOnAction(e -> showReservations());

            topBox.getChildren().addAll(welcomeLabel, viewReservationsBtn);
        } else {
            setupGuestView(topBox);
        }
        return topBox;
    }

    /**
     * Crée une carte représentant une attraction.
     *
     * @param attraction L'attraction à afficher
     * @param stage La fenêtre parente
     * @return HBox contenant la carte de l'attraction
     */
    private HBox createAttractionCard(Attraction attraction, Stage stage) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ddd; -fx-border-radius: 10;");
        card.setAlignment(Pos.CENTER_LEFT);

        // Image à gauche
        ImageView imageView = getAttractionImage(attraction);
        imageView.setFitWidth(300);
        imageView.setFitHeight(200);

        // Conteneur pour les informations à droite
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(0, 0, 0, 20));

        // Nom de l'attraction
        Label nameLabel = new Label(attraction.getNom());
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Détails
        VBox detailsBox = new VBox(5);
        detailsBox.getChildren().addAll(
                createDetailRow("Type: ", attraction.getType()),
                createDetailRow("Durée: ", attraction.getDuree() + " min"),
                createDetailRow("Capacité: ", String.valueOf(attraction.getCapacite())),
                createDetailRow("Prix: ", String.format("%.2f€", attraction.getPrixAvecReduction(user)))
        );

        // Description
        Label description = new Label(attraction.getDescription() != null ? attraction.getDescription() : "Pas de description");
        description.setWrapText(true);
        description.setMaxWidth(400);

        // Bouton Réserver
        Button reserveBtn = new Button("Réserver");
        reserveBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        reserveBtn.setOnAction(e -> new ReservationFormVue(user, attraction, connexion, stage));

        infoBox.getChildren().addAll(nameLabel, detailsBox, description, reserveBtn);
        card.getChildren().addAll(imageView, infoBox);

        return card;
    }

    /**
     * Crée une ligne de détail avec un libellé et une valeur.
     *
     * @param label Le libellé du détail
     * @param value La valeur du détail
     * @return HBox contenant la ligne de détail formatée
     */
    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox(5);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-weight: bold;");
        Label val = new Label(value);
        row.getChildren().addAll(lbl, val);
        return row;
    }

    /**
     * Récupère l'image associée à une attraction.
     *
     * @param attraction L'attraction dont on veut l'image
     * @return ImageView contenant l'image de l'attraction
     */
    private ImageView getAttractionImage(Attraction attraction) {
        Map<String, String> imageMap = Map.of(
                "Att1", "Att1.jpg",
                "Att2", "Att2.jpg",
                "Att3", "Att3.jpg",
                "Att4", "Att4.jpg"
        );

        // Récupère le nom du fichier image correspondant ou utilise une image par défaut
        String imageName = imageMap.getOrDefault(attraction.getNom(), "default.jpg");

        // Chemin relatif depuis les ressources
        String imagePath = "/Vue/" + imageName; // Adaptez ce chemin

        // Charge l'image depuis les ressources
        Image image = new Image(getClass().getResourceAsStream(imagePath));

        // Crée et configure l'ImageView
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(300);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        return imageView;
    }

    /**
     * Configure la vue pour le mode invité.
     *
     * @param topBox Le conteneur dans lequel ajouter les éléments
     */
    private void setupGuestView(VBox topBox) {
        // Bouton Retour (aligné à gauche)
        Button backBtn = new Button("← Retour");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db;");
        backBtn.setOnAction(e -> {
            new AccueilVue(connexion);
            ((Stage) backBtn.getScene().getWindow()).close();
        });

        HBox retourBar = new HBox(backBtn);
        retourBar.setAlignment(Pos.TOP_LEFT);
        retourBar.setPadding(new Insets(5, 10, 0, 10));

        // Titre
        Label title = new Label("🎟️ Attractions disponibles (Mode Invité)");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Barre de recherche
        TextField emailField = new TextField();
        emailField.setPromptText("Entrez votre email");
        emailField.setMaxWidth(250);

        Button viewReservationsBtn = new Button("Voir mes réservations");
        viewReservationsBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");
        viewReservationsBtn.setOnAction(e -> showGuestReservations(emailField.getText()));

        HBox searchBar = new HBox(10, emailField, viewReservationsBtn);
        searchBar.setAlignment(Pos.CENTER);
        searchBar.setPadding(new Insets(10, 0, 10, 0)); // Ajout de padding

        // Organisation
        topBox.getChildren().addAll(
                retourBar,
                title,
                searchBar
        );
    }

    /**
     * Affiche l'historique des réservations pour l'utilisateur connecté.
     */
    private void showReservations() {
        List<String> reservations = new AttractionControleur(connexion)
                .getReservationsUser(user)
                .stream()
                .map(Object::toString)
                .toList();
        new ReservationHistoriqueVue(user.getUserNom(), reservations, false);
    }

    /**
     * Affiche l'historique des réservations pour un invité (via email).
     *
     * @param email L'email de l'invité
     */
    private void showGuestReservations(String email) {
        if (email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Veuillez entrer votre email").showAndWait();
            return;
        }
        List<String> reservations = new ReservationControleur(connexion)
                .recupererReservationsInvite(email);
        new ReservationHistoriqueVue(email, reservations, true);
    }
}