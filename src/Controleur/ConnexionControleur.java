package Controleur;

import DAO.UserDAO;
import Modele.User;
import Vue.AdminVue;
import Vue.UserVue;
import javafx.scene.control.Alert;

import java.sql.Connection;

/**
 * Le contrôleur pour gérer le processus de connexion de l'utilisateur.
 * Il interagit avec la vue et le modèle pour effectuer la logique de connexion et rediriger l'utilisateur.
 */
public class ConnexionControleur {

    private UserDAO userDAO;
    private Connection connexion;

    /**
     * Constructeur du contrôleur de connexion.
     * @param connexion La connexion à la base de données.
     */
    public ConnexionControleur(Connection connexion) {
        this.connexion = connexion;
        this.userDAO = new UserDAO(connexion);
    }

    /**
     * Récupère la connexion à la base de données.
     * @return La connexion à la base de données.
     */
    public Connection getConnexion() {
        return connexion;
    }

    /**
     * Connecte un utilisateur en fonction de son email et de son mot de passe.
     * @param email L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @return Un objet User représentant l'utilisateur connecté, ou null si les informations sont incorrectes.
     */
    public User connecterUtilisateur(String email, String password) {
        return userDAO.findByEmailAndPassword(email, password);
    }

    /**
     * Gère la redirection de l'utilisateur en fonction de son rôle après une connexion réussie.
     * Si l'utilisateur est un administrateur, il est redirigé vers la vue administrateur.
     * Si l'utilisateur est un utilisateur standard, il est redirigé vers la vue utilisateur.
     * @param utilisateur L'utilisateur connecté.
     */
    public void gererConnexionReussie(User utilisateur) {
        if (utilisateur.getRole() == User.Role.ADMIN) {
            new AdminVue(utilisateur.getUserNom(), connexion, utilisateur);
        } else {
            new UserVue(utilisateur.getUserNom(), connexion, utilisateur);
        }
    }

    /**
     * Affiche une alerte en cas de connexion échouée.
     * Un message d'erreur est affiché lorsque les informations d'identification sont incorrectes.
     */
    public void afficherErreurConnexion() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("La connexion a échouée");
        alert.setHeaderText(null);
        alert.setContentText("Email ou mot de passe incorrect.");
        alert.showAndWait();
    }
}
