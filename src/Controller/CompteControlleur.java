package Controller;
import Dao.CommandeDao;
import Dao.CommandeDaoImpl;
import Dao.CommandeLigneDao;
import Dao.CommandeLigneDaoImpl;
import Dao.DaoFactory;
import Dao.ProduitDao;
import Dao.ProduitDaoImpl;
import Dao.UtilisateurDaoImpl;
import Model.Commande;
import Model.CommandeLigne;
import Model.Produit;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CompteControlleur {

    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    private Utilisateur utilisateurConnecte;
    private Utilisateur utilisateurCompte;
    private CommandeDao commandeDao;
    private CommandeLigneDao commandeLigneDao;
    private ProduitDao produitDao;

    @FXML
    private TextField nomUtilisateurField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button modifierButton;
    @FXML
    private Button deconnexionButton;

    List<Commande> commandes;

    @FXML
    private Button retourMagasinButton;


    public CompteControlleur() {
        // JavaFX a besoin de ce constructeur pour charger le FXML
    }


    public void initData(DaoFactory daoFactory, Utilisateur utilisateurConnecte, Utilisateur utilisateurCompte) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateurConnecte;
        this.utilisateurCompte = utilisateurCompte;
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.commandes = commandeDao.getCommandesUtilisateur(utilisateurCompte.getutilisateurId());
        afficherInformationsCompte();
        setCommandes(commandes);

    }

    @FXML
    public void initialize() {
        modifierButton.setOnAction(e -> ModifierCompte());
        deconnexionButton.setOnAction(e -> Deconnexion());
        retourMagasinButton.setOnAction(e -> retourMagasin());

    }

    /**
     * Permet à un utilisateur de modifier ses informations de compte
     *
     * @return L'utilisateur modifié ou null en cas d'erreur
     */
    public void ModifierCompte() {
        try {
            //Vérifier que l'utilisateur est connecté
            if (utilisateurConnecte == null) {
                System.out.println("Vous devez être connecté pour modifier votre compte.");
                return;
            }

            //Récupérer les nouvelles informations
            String nom = nomUtilisateurField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            //Mettre à jour l'utilisateur
            utilisateurConnecte.setutilisateurName(nom);
            utilisateurConnecte.setutilisateurMail(email);
            utilisateurConnecte.setutilisateurPassword(password);

            //Sauvegarder les modifications
            utilisateurDao.modifier(utilisateurConnecte);

            System.out.println("Informations du compte mises à jour avec succès.");

        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du compte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void Deconnexion() {
        try {
            //Vérifier que l'utilisateur est connecté
            if (utilisateurConnecte == null) {
                System.out.println("Vous devez être connecté pour vous déconnecter.");
                return;
            }

            //Déconnexion de l'utilisateur
            utilisateurConnecte = null;
            System.out.println("Déconnexion réussie.");

            //Retourner à la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            ConnexionControlleur controller = new ConnexionControlleur(daoFactory);
            loader.setController(controller);
            Parent root = loader.load();
            Stage currentStage = (Stage) deconnexionButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page de connexion");
            currentStage.show();



        } catch (IOException e) {
            System.out.println("Erreur lors de la déconnexion: " + e.getMessage());
        }
    }



    /**
     * Affiche les informations du compte de l'utilisateur connecté
     */
    public void afficherInformationsCompte() {
        if (utilisateurConnecte != null) {
            nomUtilisateurField.setText(utilisateurConnecte.getutilisateurName());
            emailField.setText(utilisateurConnecte.getutilisateurMail());
            passwordField.setText(utilisateurConnecte.getutilisateurPassword());
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    @FXML
    private VBox commandesContainer;

    public void setCommandes(List<Commande> commandes) {
        commandesContainer.getChildren().clear(); // reset l'affichage

        // Trier les commandes les plus récentes en premier
        commandes.sort(Comparator.comparing(Commande::getCommandeId).reversed());
        int commandeNumero = commandes.size();

        for (Commande commande : commandes) {

            VBox commandeBox = new VBox();
            commandeBox.setAlignment(Pos.CENTER_LEFT);
            commandeBox.setSpacing(5);
            commandeBox.setPadding(new Insets(5));

            Label label = new Label(" - #" + commandeNumero);
            label.setFont(new Font(18));
            commandeNumero--;

            Button btn = new Button("Voir Facture");
//            btn.setOnAction(e -> afficherFacture(commande)); // ta méthode personnalisée
            btn.setPadding(new Insets(5, 10, 5, 10));


            commandeBox.getChildren().addAll(label, btn);
            commandesContainer.getChildren().add(commandeBox);
        }
    }

    /**
     * Accède au magasin pour consulter les produits et passer des commandes
     *
     * @return Le contrôleur du magasin ou null en cas d'erreur
     */
    @FXML
    private void retourMagasin() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shop.fxml"));
            Parent root = loader.load();
            MagasinControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) emailField.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du magasin");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}