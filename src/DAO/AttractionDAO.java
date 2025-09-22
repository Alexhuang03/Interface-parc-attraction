package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Modele.Attraction;
import Modele.Attraction.StatutAttraction;

/**
 * Classe DAO pour gérer les opérations sur la table `attraction` dans la base de données.
 * Elle permet d'effectuer des opérations CRUD (Create, Read, Update, Delete) sur les objets `Attraction`.
 */
public class AttractionDAO {

    private Connection connexion;

    /**
     * Constructeur du DAO pour les attractions.
     * @param connexion La connexion à la base de données utilisée pour exécuter les requêtes SQL.
     */
    public AttractionDAO(Connection connexion) {
        this.connexion = connexion;
    }

    /**
     * Insère une nouvelle attraction dans la base de données.
     * @param attraction L'attraction à insérer.
     * @return true si l'insertion a réussi, false sinon.
     */
    public boolean insert(Attraction attraction) {
        try {
            String sql = "INSERT INTO attraction (nom, type, description, capacite, duree, prix, statut) VALUES (?, ?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, attraction.getNom());
            stmt.setString(2, attraction.getType());
            stmt.setString(3, attraction.getDescription());
            stmt.setInt(4, attraction.getCapacite());
            stmt.setString(5, attraction.getDuree());
            stmt.setFloat(6, attraction.getPrix());
            stmt.setString(7, attraction.getStatut().toString()); // Enum to string

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour les informations d'une attraction dans la base de données.
     * @param a L'attraction avec les nouvelles informations.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean updateAttraction(Attraction a) {
        try (PreparedStatement stmt = connexion.prepareStatement(
                "UPDATE attraction SET nom = ?, type = ?, description = ?, capacite = ?, duree = ?, prix = ?, statut = ? WHERE id_attraction = ?")) {

            stmt.setString(1, a.getNom());
            stmt.setString(2, a.getType());
            stmt.setString(3, a.getDescription());
            stmt.setInt(4, a.getCapacite());
            stmt.setString(5, a.getDuree());
            stmt.setFloat(6, a.getPrix());
            stmt.setString(7, a.getStatut().toString());
            stmt.setInt(8, a.getIdAttraction());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère toutes les attractions de la base de données.
     * @return Une liste d'attractions.
     */
    public List<Attraction> findAll() {
        List<Attraction> attractions = new ArrayList<>();
        try {
            String sql = "SELECT * FROM attraction";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Attraction a = new Attraction(
                        rs.getInt("id_attraction"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("capacite"),
                        rs.getString("duree"),
                        rs.getFloat("prix"),
                        StatutAttraction.valueOf(rs.getString("statut"))
                );
                attractions.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attractions;
    }

    /**
     * Récupère toutes les attractions actives de la base de données.
     * @return Une liste d'attractions actives.
     */
    public List<Attraction> getAllActive() {
        List<Attraction> attractionsActives = new ArrayList<>();
        try {
            String sql = "SELECT * FROM attraction WHERE statut = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, StatutAttraction.active.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Attraction a = new Attraction(
                        rs.getInt("id_attraction"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("capacite"),
                        rs.getString("duree"),
                        rs.getFloat("prix"),
                        StatutAttraction.valueOf(rs.getString("statut"))
                );
                attractionsActives.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attractionsActives;
    }

    /**
     * Supprime une attraction de la base de données en fonction de son ID.
     * @param id L'ID de l'attraction à supprimer.
     * @return true si l'attraction a été supprimée avec succès, false sinon.
     */
    public boolean deleteById(int id) {
        try {
            String sql = "DELETE FROM attraction WHERE id_attraction = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}