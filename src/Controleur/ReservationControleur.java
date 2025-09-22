package Controleur;

import DAO.ReservationDAO;
import Modele.Attraction;
import Modele.Reservation;
import Modele.User;
import Vue.PaiementVue;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Contrôleur qui gère les opérations liées aux réservations et aux paiements.
 * Il interagit avec les classes DAO et Vue pour effectuer des actions sur les données et afficher les informations.
 */
public class ReservationControleur {

    private ReservationDAO reservationDAO;

    /**
     * Constructeur du contrôleur de réservation.
     * @param connexion La connexion à la base de données utilisée pour l'accès aux données.
     */
    public ReservationControleur(Connection connexion) {
        this.reservationDAO = new ReservationDAO(connexion);
    }

    /**
     * Récupère toutes les réservations depuis la base de données.
     * @return Une liste de toutes les réservations.
     */
    public List<Reservation> recupererToutesReservations() {
        return reservationDAO.recupererToutesReservations();
    }

    /**
     * Met à jour le statut d'une réservation.
     * @param reservation La réservation dont le statut doit être mis à jour.
     */
    public void mettreAJourStatut(Reservation reservation) {
        reservationDAO.mettreAJourStatut(reservation);
    }

    /**
     * Crée une nouvelle réservation avec paiement pour un utilisateur.
     * Ce processus inclut la création d'une réservation et le paiement de l'attraction.
     * @param user L'utilisateur qui effectue la réservation.
     * @param attraction L'attraction pour laquelle l'utilisateur souhaite réserver.
     * @param dateReservation La date à laquelle l'utilisateur souhaite effectuer la réservation.
     * @param onSuccess Une action à exécuter après la création de la réservation (par exemple, une mise à jour de l'interface).
     */
    public void creerReservationAvecPaiement(User user, Attraction attraction, Date dateReservation, Runnable onSuccess) {
        PaiementVue paiementVue = new PaiementVue();
        paiementVue.afficher(attraction.getPrix(), paiement -> {
            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setAttraction(attraction);
            reservation.setDateReservation(dateReservation);
            reservation.setStatut(Reservation.StatutReservation.confirmee);
            reservation.setPaiement(paiement);

            reservationDAO.creationReservation(reservation);
            if (onSuccess != null) onSuccess.run();
        }, user);
    }

    /**
     * Crée une nouvelle réservation avec paiement pour un invité.
     * Ce processus inclut la création d'une réservation et le paiement de l'attraction, sans nécessiter de compte utilisateur.
     * @param nom Le nom de l'invité.
     * @param email L'email de l'invité.
     * @param attraction L'attraction pour laquelle l'invité souhaite réserver.
     * @param dateReservation La date à laquelle l'invité souhaite effectuer la réservation.
     * @param onSuccess Une action à exécuter après la création de la réservation (par exemple, une mise à jour de l'interface).
     */
    public void creerReservationInviteAvecPaiement(String nom, String email, Attraction attraction, Date dateReservation, Runnable onSuccess) {
        PaiementVue paiementVue = new PaiementVue();
        paiementVue.afficher(attraction.getPrix(), paiement -> {
            Reservation reservation = new Reservation();
            reservation.setInviteNom(nom);
            reservation.setInviteEmail(email);
            reservation.setAttraction(attraction);
            reservation.setDateReservation(dateReservation);
            reservation.setStatut(Reservation.StatutReservation.confirmee);
            reservation.setPaiement(paiement);

            reservationDAO.creationReservationInvite(reservation);
            if (onSuccess != null) onSuccess.run();
        }, null);
    }

    /**
     * Récupère toutes les réservations d'un invité donné par son adresse email.
     * @param emailInvite L'email de l'invité pour lequel récupérer les réservations.
     * @return Une liste des réservations sous forme de chaînes de caractères pour l'invité spécifié.
     */
    public List<String> recupererReservationsInvite(String emailInvite) {
        return reservationDAO.getReservationsInvite(emailInvite);
    }
}
