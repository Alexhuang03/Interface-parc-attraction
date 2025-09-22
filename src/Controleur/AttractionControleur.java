package Controleur;

import DAO.AttractionDAO;
import Modele.Attraction;
import DAO.ReservationDAO;
import Modele.Reservation;
import Modele.User;

import java.sql.Connection;
import java.util.List;

/**
 * Le contrôleur qui gère les opérations liées aux attractions et aux réservations.
 * Il interagit avec les classes DAO pour effectuer des actions sur les données et la logique métier.
 */
public class AttractionControleur {

    private AttractionDAO attractionDAO;
    private ReservationDAO reservationDAO;

    /**
     * Constructeur du contrôleur.
     * @param connexion La connexion à la base de données.
     */
    public AttractionControleur(Connection connexion) {
        this.attractionDAO = new AttractionDAO(connexion);
        this.reservationDAO = new ReservationDAO(connexion);
    }

    /**
     * Ajoute une attraction dans la base de données.
     * @param nom Le nom de l'attraction.
     * @param type Le type de l'attraction.
     * @param capaciteStr La capacité de l'attraction (sous forme de chaîne).
     * @param duree La durée de l'attraction.
     * @param prixStr Le prix de l'attraction (sous forme de chaîne).
     * @param statutStr Le statut de l'attraction (sous forme de chaîne).
     * @param description La description de l'attraction.
     * @return true si l'attraction a été ajoutée avec succès, false sinon.
     */
    public boolean ajouterAttraction(String nom, String type, String capaciteStr, String duree, String prixStr, String statutStr, String description) {
        try {
            int capacite = Integer.parseInt(capaciteStr);
            float prix = Float.parseFloat(prixStr);
            Attraction.StatutAttraction statut = Attraction.StatutAttraction.valueOf(statutStr);

            Attraction attraction = new Attraction(0, nom, type, description, capacite, duree, prix, statut);

            return attractionDAO.insert(attraction);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime une attraction de la base de données.
     * @param idAttraction L'identifiant de l'attraction à supprimer.
     * @return true si l'attraction a été supprimée avec succès, false sinon.
     */
    public boolean supprimerAttraction(int idAttraction) {
        return attractionDAO.deleteById(idAttraction);
    }

    /**
     * Récupère toutes les attractions de la base de données.
     * @return Une liste d'attractions.
     */
    public List<Attraction> getAttractions() {
        return attractionDAO.findAll();
    }

    /**
     * Récupère toutes les attractions actives de la base de données.
     * @return Une liste d'attractions actives.
     */
    public List<Attraction> getAttractionsActives() {
        return attractionDAO.getAllActive();
    }

    /**
     * Récupère toutes les réservations d'un utilisateur spécifique.
     * @param user L'utilisateur pour lequel récupérer les réservations.
     * @return Une liste de réservations.
     */
    public List<Reservation> getReservationsUser(User user) {
        return reservationDAO.getReservations(user);
    }

    /**
     * Valide les informations d'une attraction avant son ajout ou modification.
     * @param a L'attraction à valider.
     * @return true si l'attraction est valide, false sinon.
     */
    private boolean validerAttraction(Attraction a) {
        if (a.getNom() == null || a.getNom().trim().isEmpty()) {
            return false;
        }
        if (a.getCapacite() <= 0) {
            return false;
        }
        if (a.getPrix() < 0) {
            return false;
        }
        return true;
    }

    /**
     * Sauvegarde les modifications des attractions dans la base de données.
     * @param attractions La liste des attractions à sauvegarder.
     * @return true si toutes les modifications ont été sauvegardées avec succès, false sinon.
     */
    public boolean sauvegarderModifications(List<Attraction> attractions) {
        boolean allSuccess = true;
        for (Attraction attraction : attractions) {
            if (!validerAttraction(attraction)) {
                System.out.println("Validation échouée pour: " + attraction.getNom());
                allSuccess = false;
                continue;
            }

            if (!attractionDAO.updateAttraction(attraction)) {
                System.out.println("Échec DAO pour: " + attraction.getNom());
                allSuccess = false;
            }
        }
        return allSuccess;
    }

}
