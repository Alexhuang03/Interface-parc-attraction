package Controleur;

import DAO.UserDAO;
import Modele.User;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur pour gérer les actions sur la liste des clients.
 */
public class ClientControleur {

    private Connection connexion;
    private UserDAO userDAO;

    public ClientControleur(Connection connexion) {
        this.connexion = connexion;
        this.userDAO = new UserDAO(connexion);
    }

    public List<User> getAllClients() {
        return userDAO.getAllClients();
    }

    public void sauvegarderModifications(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            String nom = (String) model.getValueAt(i, 1);
            String prenom = (String) model.getValueAt(i, 2);
            LocalDate dateNaissance = LocalDate.parse(model.getValueAt(i, 3).toString());
            String email = (String) model.getValueAt(i, 4);
            String tel = (String) model.getValueAt(i, 7);
            String password = (String) model.getValueAt(i, 8);
            String roleStr = (String) model.getValueAt(i, 9);
            User.Role role = User.Role.valueOf(roleStr);

            // Créer l'objet User
            User user = new User(nom, prenom, dateNaissance, email, tel, password, role);
            user.setUserId(id);

            // Calculer Type et Réduction
            user.calculerTypeEtReduction();

            // Sauvegarder dans la base
            userDAO.update(user);

            // Mettre à jour les colonnes recalculées dans le tableau
            model.setValueAt(user.getTypeClient().toString(), i, 5);
            model.setValueAt(user.getReduction(), i, 6);
        }
    }
}
