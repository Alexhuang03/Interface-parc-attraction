package Controleur;

import Vue.AccueilVue;
import java.sql.Connection;
import java.sql.DriverManager;


/**
 * Classe principale de l'application qui sert de point d'entrée.
 *
 * Cette classe a deux responsabilités principales :
 *
 *   Initialiser l'application JavaFX et lancer l'interface utilisateur
 *  Établir la connexion à la base de données
 *
 */
public class Main {

    /**
     * Point d'entrée principal de l'application.
     *
     * <p>Cette méthode :</p>
     * <ul>
     *   <li>Initialise la plateforme JavaFX</li>
     *   <li>Établit la connexion à la base de données</li>
     *   <li>Lance l'interface d'accueil de l'application</li>
     * </ul>
     *
     * @param args Arguments de ligne de commande (non utilisés dans cette application)
     */
    public static void main(String[] args) {
        // Lancer JavaFX proprement (initialisation du thread JavaFX)
        javafx.application.Platform.startup(() -> {
            Connection connexion = connecterBD();
            new AccueilVue(connexion); // Lancer la fenêtre JavaFX
        });
    }

    /**
     * Établit une connexion à la base de données MySQL.
     *
     *Les paramètres de connexion sont :
     *
     *   URL : jdbc:mysql://localhost:3306/attraction
     *   Utilisateur : root
     *   Mot de passe : vide
     *
     *
     * @return Un objet Connection représentant la connexion à la base de données,
     *         ou null si la connexion a échoué
     */
    public static Connection connecterBD() {
        try {
            String url = "jdbc:mysql://localhost:3306/attraction";
            String user = "root";
            String password = "";
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}