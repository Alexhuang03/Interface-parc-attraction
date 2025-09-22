package Modele;

import java.time.LocalDate;
import java.time.Period;

/**
 * Classe représentant un utilisateur du système.
 * Gère les informations personnelles, les rôles et les réductions applicables.
 */
public class User {
    /**
     * Enumération des rôles possibles d'un utilisateur.
     */
    public enum Role {
        /** Utilisateur standard */
        CLIENT,
        /** Administrateur du système */
        ADMIN
    }

    /**
     * Enumération des types de clients.
     */
    public enum TypeClient {
        /** Client enfant (moins de 12 ans) */
        ENFANT,
        /** Client adulte (12-64 ans) */
        ADULTE,
        /** Client senior (65 ans et plus) */
        SENIOR,
        /** Invité sans compte */
        INVITE
    }

    private int userId;
    private String userNom;
    private String userPrenom;
    private LocalDate dateNaissance;
    private String userEmail;
    private String userTel;
    private String userPassword;
    private Role role;
    private TypeClient typeClient;
    private double reduction;

    /**
     * Constructeur complet pour un utilisateur existant.
     * @param userId Identifiant unique de l'utilisateur
     * @param userNom Nom de famille
     * @param userPrenom Prénom
     * @param dateNaissance Date de naissance
     * @param userEmail Adresse email
     * @param userTel Numéro de téléphone
     * @param userPassword Mot de passe
     * @param role Rôle de l'utilisateur
     * @param typeClient Type de client
     * @param reduction Pourcentage de réduction applicable
     */
    public User(int userId, String userNom, String userPrenom, LocalDate dateNaissance, String userEmail, String userTel, String userPassword, Role role, TypeClient typeClient, double reduction) {
        this.userId = userId;
        this.userNom = userNom;
        this.userPrenom = userPrenom;
        this.dateNaissance = dateNaissance;
        this.userEmail = userEmail;
        this.userTel = userTel;
        this.userPassword = userPassword;
        this.role = role;
        this.typeClient = typeClient;
        this.reduction = reduction;
    }

    /**
     * Constructeur avec calcul automatique du type et de la réduction.
     * @param nom Nom de famille
     * @param prenom Prénom
     * @param dateNaissance Date de naissance
     * @param email Adresse email
     * @param tel Numéro de téléphone
     * @param mdp Mot de passe
     * @param role Rôle de l'utilisateur
     */
    public User(String nom, String prenom, LocalDate dateNaissance, String email, String tel, String mdp, Role role) {
        this.userNom = nom;
        this.userPrenom = prenom;
        this.dateNaissance = dateNaissance;
        this.userEmail = email;
        this.userTel = tel;
        this.userPassword = mdp;
        this.role = role;
        calculerTypeEtReduction();
    }

    /**
     * Constructeur par défaut.
     */
    public User(){}

    /**
     * Calcule automatiquement le type de client et la réduction en fonction de l'âge.
     */
    public void calculerTypeEtReduction() {
        if (this.role != Role.CLIENT || dateNaissance == null) {
            this.typeClient = TypeClient.INVITE;
            this.reduction = 0;
            return;
        }

        int age = Period.between(dateNaissance, LocalDate.now()).getYears();
        if (age < 12) {
            this.typeClient = TypeClient.ENFANT;
            this.reduction = 0.50;
        } else if (age >= 65) {
            this.typeClient = TypeClient.SENIOR;
            this.reduction = 0.30;
        } else {
            this.typeClient = TypeClient.ADULTE;
            this.reduction = 0.15;
        }
    }

    /**
     * @return L'identifiant de l'utilisateur
     */
    public int getUserId() { return userId; }

    /**
     * @return Le nom de famille
     */
    public String getUserNom() { return userNom; }

    /**
     * @return Le prénom
     */
    public String getUserPrenom() { return userPrenom; }

    /**
     * @return L'adresse email
     */
    public String getUserEmail() { return userEmail; }

    /**
     * @return Le numéro de téléphone
     */
    public String getUserTel() { return userTel; }

    /**
     * @return Le mot de passe
     */
    public String getUserPassword() { return userPassword; }

    /**
     * @return Le rôle de l'utilisateur
     */
    public Role getRole() { return role; }

    /**
     * @return Le type de client
     */
    public TypeClient getTypeClient() { return typeClient; }

    /**
     * @return Le pourcentage de réduction applicable
     */
    public double getReduction() {return reduction;}

    /**
     * @return La date de naissance
     */
    public LocalDate getDateNaissance() { return dateNaissance; }

    // Setters
    /**
     * @param userId L'identifiant à définir
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * @param userNom Le nom de famille à définir
     */
    public void setUserNom(String userNom) { this.userNom = userNom; }

    /**
     * @param userPrenom Le prénom à définir
     */
    public void setUserPrenom(String userPrenom) { this.userPrenom = userPrenom; }

    /**
     * @param userEmail L'adresse email à définir
     */
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    /**
     * @param userTel Le numéro de téléphone à définir
     */
    public void setUserTel(String userTel) { this.userTel = userTel; }

    /**
     * @param userPassword Le mot de passe à définir
     */
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    /**
     * @param role Le rôle à définir
     */
    public void setRole(Role role) { this.role = role; }

    /**
     * @param typeClient Le type de client à définir
     */
    public void setTypeClient(TypeClient typeClient) { this.typeClient = typeClient; }

    /**
     * @param reduction Le pourcentage de réduction à définir
     */
    public void setReduction(float reduction) {this.reduction = reduction;}

    /**
     * @param dateNaissance La date de naissance à définir
     */
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
}