package Controleur;

import DAO.UserDAO;
import Modele.User;
import Modele.User.Role;

import java.sql.Connection;
import java.time.LocalDate;

/**
 * Le contrôleur pour gérer le processus d'inscription d'un nouveau client.
 * Il permet d'ajouter un client dans la base de données après avoir vérifié que les informations sont valides.
 */
public class InscriptionControleur {

    /**
     * Inscrit un nouveau client en ajoutant ses informations dans la base de données.
     * Les informations de l'utilisateur sont vérifiées pour s'assurer qu'aucun champ requis n'est vide.
     * @param connexion La connexion à la base de données.
     * @param nom Le nom du client.
     * @param prenom Le prénom du client.
     * @param dateNaissance La date de naissance du client.
     * @param tel Le numéro de téléphone du client.
     * @param email L'email du client.
     * @param mdp Le mot de passe du client.
     * @return Un booléen indiquant si l'inscription a été réussie ou non. Retourne false si l'un des champs est vide.
     */
    public static boolean inscrireClient(Connection connexion, String nom, String prenom, LocalDate dateNaissance, String tel, String email, String mdp) {
        // Vérification que tous les champs sont remplis
        if (nom == null || prenom == null || dateNaissance == null || tel == null || email == null || mdp == null) {
            return false; // Protection contre champs vides
        }

        // Création de l'objet client
        User client = new User(nom, prenom, dateNaissance, email, tel, mdp, Role.CLIENT);

        // Sauvegarde du client dans la base de données via le DAO
        UserDAO userDAO = new UserDAO(connexion);
        return userDAO.save(client);
    }
}
