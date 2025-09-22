package Modele;

/**
 * Classe `Attraction` qui représente une attraction dans le système.
 * Chaque attraction possède des informations comme son nom, type, description, capacité, durée, prix et statut.
 */
public class Attraction {

    /**
     * Enumération `StatutAttraction` qui définit les deux statuts possibles d'une attraction.
     * - `active` : L'attraction est active et disponible pour les réservations.
     * - `inactive` : L'attraction est inactive et non disponible.
     */
    public enum StatutAttraction {
        active,
        inactive
    }

    private int idAttraction;
    private String nom;
    private String type;
    private String description;
    private int capacite;
    private String duree;
    private float prix;
    private StatutAttraction statut;

    /**
     * Constructeur de la classe `Attraction`.
     * @param idAttraction L'ID unique de l'attraction.
     * @param nom Le nom de l'attraction.
     * @param type Le type de l'attraction (par exemple, rollercoaster, manège).
     * @param description Une description détaillée de l'attraction.
     * @param capacite La capacité d'accueil de l'attraction.
     * @param duree La durée de l'attraction.
     * @param prix Le prix d'accès à l'attraction.
     * @param statut Le statut actuel de l'attraction (active ou inactive).
     */
    public Attraction(int idAttraction, String nom, String type, String description, int capacite, String duree, float prix, StatutAttraction statut) {
        this.idAttraction = idAttraction;
        this.nom = nom;
        this.type = type;
        this.description = description;
        this.capacite = capacite;
        this.duree = duree;
        this.prix = prix;
        this.statut = statut;
    }

    /**
     * Calcule le prix de l'attraction après réduction en fonction du type de client.
     * Si l'utilisateur est invité (non connecté), il paie le plein tarif.
     * Si l'utilisateur est connecté, la réduction de l'utilisateur est appliquée.
     * @param user L'utilisateur pour lequel le prix est calculé.
     * @return Le prix de l'attraction après application de la réduction.
     */
    public double getPrixAvecReduction(User user){
        if (user == null) {
            return prix; // Plein tarif pour invité
        }
        return prix*(1-user.getReduction()); // Prix après réduction
    }

    /**
     * Constructeur par défaut.
     * Permet de créer une instance d'Attraction sans initialiser les attributs.
     */
    public Attraction(){}

    /** Getters et Setters */

    /**
     * Retourne l'ID de l'attraction.
     * @return L'ID de l'attraction.
     */
    public int getIdAttraction() {
        return idAttraction;
    }

    /**
     * Définit l'ID de l'attraction.
     * @param idAttraction L'ID à attribuer à l'attraction.
     */
    public void setIdAttraction(int idAttraction) {
        this.idAttraction = idAttraction;
    }

    /**
     * Retourne le nom de l'attraction.
     * @return Le nom de l'attraction.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'attraction.
     * @param nom Le nom à attribuer à l'attraction.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le type de l'attraction.
     * @return Le type de l'attraction.
     */
    public String getType() {
        return type;
    }

    /**
     * Définit le type de l'attraction.
     * @param type Le type à attribuer à l'attraction.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retourne la capacité d'accueil de l'attraction.
     * @return La capacité de l'attraction.
     */
    public int getCapacite() {
        return capacite;
    }

    /**
     * Définit la capacité d'accueil de l'attraction.
     * @param capacite La capacité à attribuer à l'attraction.
     */
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    /**
     * Retourne la durée de l'attraction.
     * @return La durée de l'attraction.
     */
    public String getDuree() {
        return duree;
    }

    /**
     * Définit la durée de l'attraction.
     * @param duree La durée à attribuer à l'attraction.
     */
    public void setDuree(String duree) {
        this.duree = duree;
    }

    /**
     * Retourne le prix de l'attraction.
     * @return Le prix de l'attraction.
     */
    public float getPrix() {
        return prix;
    }

    /**
     * Définit le prix de l'attraction.
     * @param prix Le prix à attribuer à l'attraction.
     */
    public void setPrix(float prix) {
        this.prix = prix;
    }

    /**
     * Retourne le statut actuel de l'attraction.
     * @return Le statut de l'attraction (active ou inactive).
     */
    public StatutAttraction getStatut() {
        return statut;
    }

    /**
     * Définit le statut de l'attraction.
     * @param statut Le statut à attribuer à l'attraction (active ou inactive).
     */
    public void setStatut(StatutAttraction statut) {
        this.statut = statut;
    }

    /**
     * Retourne la description de l'attraction.
     * @return La description de l'attraction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description de l'attraction.
     * @param description La description à attribuer à l'attraction.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
