package Modele;
import java.util.*;

/**
 * Classe représentant un paiement dans le système.
 * Gère les informations et les opérations liées aux paiements des réservations.
 */
public class Paiement {
    /**
     * Enumération des différents statuts possibles d'un paiement.
     */
    public enum StatutPaiement{
        /** Paiement en attente de traitement */
        EN_ATTENTE,
        /** Paiement effectué avec succès */
        EFFECTUE,
        /** Paiement refusé ou remboursé */
        REFUSE
    }

    private int idPaiement;
    private float montant;
    private Date datePaiement;
    private StatutPaiement statut;
    private User user;
    private Reservation reservation;
    private String moyenPaiement;

    /**
     * Constructeur avec utilisateur et montant.
     * Initialise le paiement avec un statut EN_ATTENTE et la date courante.
     * @param user L'utilisateur effectuant le paiement
     * @param montant Le montant du paiement
     */
    public Paiement(User user, float montant) {
        this.user = user;
        this.montant = montant;
        this.datePaiement = new Date();
        this.statut = StatutPaiement.EN_ATTENTE; // Statut initial
    }

    /** Getters et Setters */

    /**
     * @return L'identifiant du paiement
     */
    public int getIdPaiement() {return idPaiement;}

    /**
     * @param idPaiement L'identifiant à définir pour le paiement
     */
    public void setIdPaiement(int idPaiement) {this.idPaiement = idPaiement;}

    /**
     * @return Le montant du paiement
     */
    public float getMontant() {return montant;}

    /**
     * @param montant Le montant à définir pour le paiement
     */
    public void setMontant(float montant) {this.montant = montant;}

    /**
     * @return La date du paiement
     */
    public Date getDatePaiement() {return datePaiement;}

    /**
     * @param datePaiement La date à définir pour le paiement
     */
    public void setDatePaiement(Date datePaiement) {this.datePaiement = datePaiement;}

    /**
     * @return Le statut actuel du paiement
     */
    public StatutPaiement getStatut() {return statut;}

    /**
     * @param statut Le statut à définir pour le paiement
     */
    public void setStatut(StatutPaiement statut) {this.statut = statut;}

    /**
     * @return L'utilisateur associé au paiement
     */
    public User getUser() {return user;}

    /**
     * @param user L'utilisateur à associer au paiement
     */
    public void setUser(User user) {this.user = user;}

    /**
     * @return La réservation associée au paiement
     */
    public Reservation getReservation() {return reservation;}

    /**
     * @param reservation La réservation à associer au paiement
     */
    public void setReservation(Reservation reservation) {this.reservation = reservation;}

    /**
     * @return Le moyen de paiement utilisé
     */
    public String getMoyenPaiement() { return moyenPaiement; }

    /**
     * @param moyenPaiement Le moyen de paiement à définir
     */
    public void setMoyenPaiement(String moyenPaiement) { this.moyenPaiement = moyenPaiement; }
}