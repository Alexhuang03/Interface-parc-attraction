package Vue;

import Controleur.ClientControleur;
import Modele.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;


/**
 * Vue Swing pour la gestion des clients.
 * Permet d'afficher, modifier et sauvegarder les informations des clients dans un tableau interactif.
 */
public class ClientListeVue extends JFrame {

    private JTable table;
    private JButton btnSauvegarder;
    private ClientControleur controleur;
    /**
     * Constructeur : crée et affiche la fenêtre de gestion des clients.
     *
     * @param connexion Connexion à la base de données
     */
    public ClientListeVue(Connection connexion) {
        this.controleur = new ClientControleur(connexion);

        setTitle("Gestion des clients");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        // Colonnes du tableau
        String[] colonnes = {"ID", "Nom", "Prénom", "Date de naissance", "Email", "Type", "Réduction (%)", "Téléphone", "Mot de passe", "Rôle"};

        // Modèle du tableau
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // L'ID ne doit pas être modifiable
            }
        };

        // Charger les clients via le contrôleur
        List<User> clients = controleur.getAllClients();
        for (User c : clients) {
            model.addRow(new Object[]{
                    c.getUserId(),
                    c.getUserNom(),
                    c.getUserPrenom(),
                    c.getDateNaissance(),
                    c.getUserEmail(),
                    c.getTypeClient().toString(),
                    c.getReduction(),
                    c.getUserTel(),
                    c.getUserPassword(),
                    c.getRole().toString()
            });
        }

        // Créer le tableau
        table = new JTable(model);

        // Combobox pour Type de Client
        String[] typesClient = {"ENFANT", "ADULTE", "SENIOR", "INVITE"};
        JComboBox<String> typeCombo = new JComboBox<>(typesClient);
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(typeCombo));

        // Combobox pour Rôle
        String[] roles = {"CLIENT", "ADMIN"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        table.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(roleCombo));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bouton pour enregistrer
        btnSauvegarder = new JButton("Enregistrer les modifications");
        btnSauvegarder.addActionListener(e -> {
            controleur.sauvegarderModifications(model);
            JOptionPane.showMessageDialog(this, "Modifications enregistrées avec succès !");
        });
        add(btnSauvegarder, BorderLayout.SOUTH);

        setVisible(true);
    }
}
