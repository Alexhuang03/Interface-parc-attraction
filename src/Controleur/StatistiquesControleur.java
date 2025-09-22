package Controleur;

import DAO.ReservationDAO;

import java.sql.Connection;
import java.util.Map;

/**
 * Contrôleur qui gère les opérations liées aux statistiques des réservations.
 * Il interagit avec la classe DAO pour récupérer les données relatives aux réservations.
 */
public class StatistiquesControleur {

    private ReservationDAO reservationDAO;

    /**
     * Constructeur du contrôleur des statistiques.
     * @param connexion La connexion à la base de données utilisée pour l'accès aux données.
     */
    public StatistiquesControleur(Connection connexion) {
        this.reservationDAO = new ReservationDAO(connexion);
    }

    /**
     * Récupère les statistiques des réservations par attraction.
     * Cette méthode utilise la classe DAO pour obtenir le nombre de réservations par attraction.
     * @return Un map où les clés sont les noms des attractions et les valeurs sont le nombre de réservations pour chaque attraction.
     */
    public Map<String, Integer> getReservationsParAttraction() {
        return reservationDAO.getReservationsParAttraction();
    }
}
