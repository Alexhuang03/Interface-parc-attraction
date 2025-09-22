package DAO;

import Modele.*;
import Modele.Reservation.StatutReservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO pour la gestion des réservations.
 * Fournit des méthodes pour créer, récupérer et mettre à jour les réservations.
 */
public class ReservationDAO {
    private Connection connexion;

    /**Constructeur*/
    public ReservationDAO(Connection connexion) {
        this.connexion = connexion;
    }

    /** Création d'une réservation (avant paiement)*/
    public void creationReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (id_user, id_attraction, date_reservation, statut) VALUES (?, ?, ?, ?)";

        /** Préparation de la requête SQL avec récupération de la clé générée (id de réservation)*/
        try (PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getUser().getUserId());
            stmt.setInt(2, reservation.getAttraction().getIdAttraction());
            stmt.setDate(3, new java.sql.Date(reservation.getDateReservation().getTime()));
            stmt.setString(4, reservation.getStatut().name());

            /**Exécution de la requête*/
            int affectedRows = stmt.executeUpdate();
            // Si la réservation a bien été insérée, on récupère l'ID généré automatiquement
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Réservation créée avec succès. ID = " + id);
                }
            }
        } catch (SQLException e) { /**Message erreur si fail*/
            e.printStackTrace();
        }
    }

    /** Création d'une réservation pour invité */
    public void creationReservationInvite(Reservation reservation) {
        String sql = "INSERT INTO reservation (invite_nom, invite_email, id_attraction, date_reservation, statut) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reservation.getInviteNom());
            stmt.setString(2, reservation.getInviteEmail());
            stmt.setInt(3, reservation.getAttraction().getIdAttraction());
            stmt.setDate(4, new java.sql.Date(reservation.getDateReservation().getTime()));
            stmt.setString(5, reservation.getStatut().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Réservation invitée créée avec succès. ID = " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Récupérer les réservations d'un utilisateur (avec reconstruction des objets User et Attraction partiels)*/
    public List<Reservation> getReservations(User user) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, a.nom as nom_attraction FROM reservation r " + "JOIN attraction a ON r.id_attraction = a.id_attraction " + "WHERE r.id_user = ?";

        /**Préparation et exécution de la requête*/
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();

            /**Pour chaque ligne de résultat, on reconstitue une réservation*/
            while (rs.next()) {
                int reservationId = rs.getInt("id_reservation");
                int attractionId = rs.getInt("id_attraction");
                String attractionNom = rs.getString("nom_attraction");
                Date dateReservation = rs.getDate("date_reservation");
                String statutStr = rs.getString("statut");

                // Création de l'attraction avec son nom
                Attraction attraction = new Attraction(
                        attractionId,
                        attractionNom,
                        "", // type
                        "", // description
                        0, // capacité
                        "", // durée
                        0f, // prix
                        Attraction.StatutAttraction.active
                );

                /**Création de la réservation*/
                Reservation reservation = new Reservation(reservationId, user, attraction, dateReservation, StatutReservation.valueOf(statutStr));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Récupère l'historique des réservations pour un invité par son email.
     * @param email L'email de l'invité
     * @return Liste des réservations formatées sous forme de chaînes
     */
    public List<String> getReservationsInvite(String email) {
        List<String> historique = new ArrayList<>();
        String sql = "SELECT r.date_reservation, a.nom as nom_attraction FROM reservation r " +
                "JOIN attraction a ON r.id_attraction = a.id_attraction " +
                "WHERE r.invite_email = ?";

        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nomAttraction = rs.getString("nom_attraction");
                String date = rs.getDate("date_reservation").toString();
                historique.add("Attraction : " + nomAttraction + " | Date : " + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historique;
    }

    /**
     * Récupère les statistiques des réservations par attraction.
     * @return Map contenant le nom de l'attraction et le nombre de réservations
     */
    public Map<String, Integer> getReservationsParAttraction() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT a.nom, COUNT(*) AS total FROM reservation r " +
                "JOIN attraction a ON r.id_attraction = a.id_attraction " +
                "WHERE r.statut IN ('confirmee','en_attente') " +
                "GROUP BY a.nom ORDER BY total DESC";

        try (PreparedStatement stmt = connexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("nom"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Convertit un ResultSet en objet Reservation.
     * @param rs Le ResultSet à convertir
     * @return Un objet Reservation
     * @throws SQLException En cas d'erreur SQL
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_reservation");
        int idUser = rs.getInt("id_user");
        int idAttraction = rs.getInt("id_attraction");
        Date dateReservation = rs.getDate("date_reservation");
        String statutStr = rs.getString("statut");

        // Reconstruction utilisateur avec son ID uniquement (si null, utilisateur invité)
        User user = null;
        if (idUser != 0) {
            user = new User();
            user.setUserId(idUser);
        }

        // Reconstruction attraction avec son ID uniquement
        Attraction attraction = new Attraction();
        attraction.setIdAttraction(idAttraction);

        // Création de l'objet réservation
        Reservation reservation = new Reservation();
        reservation.setIdReservation(id);
        reservation.setUser(user);
        reservation.setAttraction(attraction);
        reservation.setDateReservation(dateReservation);
        reservation.setStatut(Reservation.StatutReservation.valueOf(statutStr));

        // Pour les invités (pas de user ID, mais nom et email)
        if (user == null) {
            reservation.setInviteNom(rs.getString("invite_nom"));
            reservation.setInviteEmail(rs.getString("invite_email"));
        }
        return reservation;
    }

    // Récupérer toutes les réservations
    public List<Reservation> recupererToutesReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, u.nom AS user_nom, a.nom AS attraction_nom " +
                "FROM reservation r " +
                "LEFT JOIN user u ON r.id_user = u.id_user " +
                "JOIN attraction a ON r.id_attraction = a.id_attraction";
        try {
            PreparedStatement stmt = connexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation res = mapResultSetToReservation(rs);
                // Complétons les noms
                if (res.getUser() != null) {
                    res.getUser().setUserNom(rs.getString("user_nom"));
                }
                res.getAttraction().setNom(rs.getString("attraction_nom"));

                reservations.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Mettre à jour le statut d'une réservation
    public void mettreAJourStatut(Reservation reservation) {
        try {
            String sql = "UPDATE Reservation SET statut = ? WHERE id_reservation = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setString(1, reservation.getStatut().name());
            stmt.setInt(2, reservation.getIdReservation());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}