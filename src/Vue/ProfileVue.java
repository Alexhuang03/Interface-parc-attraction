package Vue;

import Modele.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Cette classe affiche la vue du profil d'un utilisateur connecté.
 * Elle montre les informations du client de manière jolie et organisée.
 */
public class ProfileVue {

    private final Connection connexion;

    /**
     * Constructeur de la vue du profil.
     *
     * @param connexion la connexion à la base de données (non utilisée ici mais pourrait servir pour modifier le profil).
     * @param user l'utilisateur dont on veut afficher les informations.
     */
    public ProfileVue(Connection connexion, User user) {
        this.connexion = connexion;

        // Création d'une nouvelle fenêtre (Stage)
        Stage stage = new Stage();
        stage.setTitle("Mon Profil");

        // Titre principal en haut de la fenêtre
        Label titre = new Label("👤 Mon Profil");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 26)); // Police plus grande et en gras
        titre.setTextFill(Color.web("#2c3e50")); // Couleur du texte

        // Création des labels pour afficher les informations du client
        Label nomLabel = creerValeurLabel(user.getUserNom());
        Label prenomLabel = creerValeurLabel(user.getUserPrenom());
        Label telLabel = creerValeurLabel(user.getUserTel());
        Label emailLabel = creerValeurLabel(user.getUserEmail());
        Label dateNaissanceLabel = creerValeurLabel(user.getDateNaissance().toString());
        Label typeClientLabel = creerValeurLabel(
                user.getTypeClient() != null ? user.getTypeClient().toString() : "Non défini"
        );
        Label reductionLabel = creerValeurLabel((int) (user.getReduction() * 100) + " %");

        // Mise en page sous forme de grille pour aligner les informations
        GridPane form = new GridPane();
        form.setHgap(20); // Espace horizontal entre colonnes
        form.setVgap(20); // Espace vertical entre lignes
        form.setPadding(new Insets(20)); // Marge intérieure
        form.setAlignment(Pos.CENTER); // Centrer le contenu
        form.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-border-color: #dcdcdc; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;"
        );

        // Ajout des champs et valeurs dans la grille
        form.add(creerChampLabel("Nom :"), 0, 0); form.add(nomLabel, 1, 0);
        form.add(creerChampLabel("Prénom :"), 0, 1); form.add(prenomLabel, 1, 1);
        form.add(creerChampLabel("Téléphone :"), 0, 2); form.add(telLabel, 1, 2);
        form.add(creerChampLabel("Email :"), 0, 3); form.add(emailLabel, 1, 3);
        form.add(creerChampLabel("Date de naissance :"), 0, 4); form.add(dateNaissanceLabel, 1, 4);
        form.add(creerChampLabel("Type de client :"), 0, 5); form.add(typeClientLabel, 1, 5);
        form.add(creerChampLabel("Réduction :"), 0, 6); form.add(reductionLabel, 1, 6);

        // Boîte verticale pour organiser le titre et le formulaire
        VBox layout = new VBox(30, titre, form);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #ecf0f1;"); // Couleur de fond

        // Création de la scène et affichage
        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crée un label pour les champs (ex : "Nom :", "Email :").
     *
     * @param texte le texte à afficher pour le champ.
     * @return un Label stylisé pour les champs.
     */
    private Label creerChampLabel(String texte) {
        Label label = new Label(texte);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Texte en gras pour les titres
        label.setTextFill(Color.web("#34495e"));
        return label;
    }

    /**
     * Crée un label pour les valeurs (ex : "Dupont", "exemple@email.com").
     *
     * @param texte le texte à afficher pour la valeur.
     * @return un Label stylisé pour les valeurs.
     */
    private Label creerValeurLabel(String texte) {
        Label label = new Label(texte);
        label.setFont(Font.font("Arial", 14)); // Texte normal pour les valeurs
        label.setTextFill(Color.web("#2c3e50"));
        return label;
    }
}
