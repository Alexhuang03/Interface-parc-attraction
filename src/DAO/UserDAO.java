package DAO;

import java.util.*;
import Modele.User;
import Modele.User.Role;
import java.sql.*;

/**
 * Classe `UserDAO` qui permet d'interagir avec la table `user` de la base de données.
 * Elle fournit des méthodes pour enregistrer un utilisateur, retrouver un utilisateur par email et mot de passe,
 * récupérer tous les clients et effectuer des mises à jour sur un utilisateur.
 */
public class UserDAO {
    private Connection connexion;

    /**
     * Constructeur de la classe `UserDAO`.
     * @param connexion La connexion à la base de données utilisée pour exécuter les requêtes SQL.
     */
    public UserDAO(Connection connexion) {
        this.connexion = connexion;
    }

    /**
     * Enregistre un utilisateur dans la base de données.
     * Si la réduction de l'utilisateur est à 0.0, elle est calculée en fonction de la date de naissance et du type de client.
     * @param user L'utilisateur à enregistrer.
     * @return `true` si l'utilisateur a été enregistré avec succès, `false` sinon.
     */
    public boolean save(User user) {
        String sql = "INSERT INTO user (nom, prenom, email, tel, mdp, date_naissance, type_client, reduction, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserNom());
            statement.setString(2, user.getUserPrenom());
            statement.setString(3, user.getUserEmail());
            statement.setString(4, user.getUserTel());
            statement.setString(5, user.getUserPassword());
            statement.setDate(6, java.sql.Date.valueOf(user.getDateNaissance()));
            statement.setString(7, user.getTypeClient().toString());
            statement.setDouble(8, user.getReduction());
            statement.setString(9, user.getRole().name().toLowerCase());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec lors de l'insertion de l'utilisateur, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setUserId(generatedId);
                    System.out.println("Nouvel ID utilisateur : " + generatedId);
                } else {
                    throw new SQLException("Échec lors de l'insertion : aucun ID généré retourné.");
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recherche un utilisateur par son email et son mot de passe.
     * @param email L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @return L'objet `User` correspondant, ou `null` si aucun utilisateur n'est trouvé.
     */
    public User findByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND mdp = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("email"),
                        rs.getString("tel"),
                        rs.getString("mdp"),
                        Role.valueOf(rs.getString("role").toUpperCase())
                );
                user.setUserId(rs.getInt("id_user"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Récupère tous les utilisateurs ayant le rôle "client".
     * @return Une liste d'objets `User` représentant les clients.
     */
    public List<User> getAllClients() {
        List<User> clients = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = 'client'";

        try (PreparedStatement stmt = connexion.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User.Role role = User.Role.valueOf(rs.getString("role").toUpperCase());
                User.TypeClient type = User.TypeClient.valueOf(rs.getString("type_client").toUpperCase());
                User client = new User(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("email"),
                        rs.getString("tel"),
                        rs.getString("mdp"),
                        role,
                        type,
                        rs.getFloat("reduction")
                );
                clients.add(client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    /**
     * Met à jour les informations d'un utilisateur dans la base de données.
     * @param user L'utilisateur avec les nouvelles informations.
     * @return `true` si l'utilisateur a été mis à jour avec succès, `false` sinon.
     */
    public boolean update(User user) {
        user.calculerTypeEtReduction();
        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, date_naissance = ?, tel = ?, mdp = ?, reduction = ?, type_client = ?, role = ? WHERE id_user = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, user.getUserNom());
            stmt.setString(2, user.getUserPrenom());
            stmt.setString(3, user.getUserEmail());
            stmt.setDate(4, java.sql.Date.valueOf(user.getDateNaissance()));
            stmt.setString(5, user.getUserTel());
            stmt.setString(6, user.getUserPassword());
            stmt.setDouble(7, user.getReduction());
            stmt.setString(8, user.getTypeClient().toString());
            stmt.setString(9, user.getRole().name().toLowerCase());
            stmt.setInt(10, user.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
