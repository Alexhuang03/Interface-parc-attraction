package Modele;
import java.util.*;

/**
 * Classe représentant une réservation dans le système.
 * Gère les informations des réservations faites par les utilisateurs ou les invités.
 */
public class Reservation {
    /**
     * Enumération des différents statuts possibles d'une réservation.
     */
    public enum StatutReservation{
        /** Réservation en attente de confirmation */
        en_attente,
        /** Réservation confirmée */
        confirmee,
        /** Réservation annulée */
        annulee
    }

    private int idReservation;    // Clé primaire
    private User user;            // Clé étrangère vers User
    private Attraction attraction; // Clé étrangère vers Attraction
    private Date dateReservation;
    private StatutReservation statut;  // Utilisation de l'enum StatutReservation
    private Paiement paiement;

    // Champs pour invités
    private String inviteNom;
    private String inviteEmail;

    /**
     * Constructeur principal pour une réservation.
     * @param idReservation L'identifiant unique de la réservation
     * @param user L'utilisateur faisant la réservation
     * @param attraction L'attraction réservée
     * @param dateReservation La date de la réservation
     * @param statut Le statut initial de la réservation
     */
    public Reservation(int idReservation, User user, Attraction attraction, Date dateReservation, StatutReservation statut) {
        this.idReservation = idReservation;
        this.user = user;
        this.attraction = attraction;
        this.dateReservation = dateReservation;
        this.statut = statut;
    }

    /**
     * Constructeur par défaut.
     */
    public Reservation(){}

    /**
     * Associe un paiement à la réservation.
     * @param paiement Le paiement à associer
     */
    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    /**
     * Retourne une représentation textuelle de la réservation.
     * @return Une chaîne de caractères décrivant la réservation
     */
    @Override
    public String toString() {
        return "Réservation ID " + idReservation +
                " - User: " + (user != null ? user.getUserNom() + " (ID:" + user.getUserId() + ")" : "null") +
                " - Attraction: " + (attraction != null ? attraction.getNom() : "null") +
                " - Date: " + dateReservation +
                " - Statut: " + statut;
    }

    /** Getters et Setters */

    /**
     * @return L'identifiant de la réservation
     */
    public int getIdReservation() { return idReservation; }

    /**
     * @return L'utilisateur associé à la réservation
     */
    public User getUser() { return user; }

    /**
     * @return L'attraction réservée
     */
    public Attraction getAttraction() { return attraction; }

    /**
     * @return La date de la réservation
     */
    public Date getDateReservation() { return dateReservation; }

    /**
     * @return Le statut actuel de la réservation
     */
    public StatutReservation getStatut() { return statut; }

    /**
     * @param statut Le nouveau statut à définir pour la réservation
     */
    public void setStatut(StatutReservation statut) { this.statut = statut; }

    /**
     * @param idReservation L'identifiant à définir pour la réservation
     */
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    /**
     * @param user L'utilisateur à associer à la réservation
     */
    public void setUser(User user) { this.user = user; }

    /**
     * @param attraction L'attraction à associer à la réservation
     */
    public void setAttraction(Attraction attraction) { this.attraction = attraction; }

    /**
     * @param dateReservation La date à définir pour la réservation
     */
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }

    /**
     * @return Le nom de l'invité (pour les réservations d'invités)
     */
    public String getInviteNom() { return inviteNom; }

    /**
     * @return L'email de l'invité (pour les réservations d'invités)
     */
    public String getInviteEmail() { return inviteEmail; }

    /**
     * @param inviteNom Le nom à définir pour l'invité
     */
    public void setInviteNom(String inviteNom) { this.inviteNom = inviteNom; }

    /**
     * @param inviteEmail L'email à définir pour l'invité
     */
    public void setInviteEmail(String inviteEmail) { this.inviteEmail = inviteEmail; }
}