package Controller;

import Dao.CommandeDaoImpl;
import Dao.CommandeLigneDaoImpl;
import Dao.DaoFactory;
import Dao.ProduitDaoImpl;
import Model.Commande;
import Model.CommandeLigne;
import Model.Produit;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProduitControlleur {
    @FXML
    private ImageView imageProduit;
    @FXML private Label nomProduit;
    @FXML private Label prixProduit;

    @FXML private VBox adminSection;

    @FXML private TextField champNom;
    @FXML private TextField champPrix;
    @FXML private TextField champPrixReduction;
    @FXML private TextField champQteReduction;
    @FXML private TextField champImage;

    @FXML private Button btnSupprimer;
    @FXML private Button btnModifier;

    DaoFactory daoFactory;
    ProduitDaoImpl produitDao;
    Utilisateur utilisateurConnecte;
    CommandeDaoImpl commandeDao;
    CommandeLigneDaoImpl commandeLigneDao;

    private Produit produit;


    /**
     * Initialise le contrôleur avec les données nécessaires.
     */
    public void initData(DaoFactory daoFactory, Utilisateur utilisateur, Produit produit) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;
        this.produit = produit;
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
        afficherProduit();
        adminSection.setVisible(false);
        if (utilisateur.isAdmin()) {
            adminSection.setVisible(true);
            champNom.setText(produit.getNom());
            champPrix.setText(String.valueOf(produit.getPrix()));
            champPrixReduction.setText(String.valueOf(produit.getPrixReduction()));
            champQteReduction.setText(String.valueOf(produit.getQteReduction()));
            champImage.setText(produit.getImage());
        }
    }

    /**
     * Ajoute le produit au panier de l'utilisateur connecté.
     * @throws InterruptedException
     */
    @FXML
    private void ajouterAuPanier() throws InterruptedException {
        List<Commande> commandes = commandeDao.getCommandesUtilisateur(utilisateurConnecte.getUtilisateurId());
            for (Commande commande: commandes) {
                if (Objects.equals(commande.getStatutCommande(), "En cours")) {
                    List< CommandeLigne> commandeLignes = commandeLigneDao.getAllFromCommande(commande.getCommandeId());
                    for (CommandeLigne commandeLigne : commandeLignes) {
                        if (commandeLigne.getProduitId() == produit.getProduitId()) {
                            commandeLigne.setQte(commandeLigne.getQte() + 1);
                            commandeLigneDao.modifier(commandeLigne);
                            System.out.println("Produit déjà dans le panier, quantité augmentée !");
                            return;
                        }
                    }
                    CommandeLigne commandeLigne = new CommandeLigne(0, commande.getCommandeId(), produit.getProduitId(), 1);
                    commandeLigneDao.ajouter(commandeLigne);
                    System.out.println("Produit ajouté au panier !");
                    return;
                }
            }
            Commande commande = new Commande(utilisateurConnecte.getUtilisateurId(), 0, "En cours");
            commandeDao.ajouter(commande);
            commande = commandeDao.getLastCommande(utilisateurConnecte.getUtilisateurId());
            CommandeLigne commandeLigne = new CommandeLigne(commande.getCommandeId(), produit.getProduitId(), 1);
            commandeLigneDao.ajouter(commandeLigne);
            System.out.println("Produit ajouté au panier !");
        }


    /**
     * initialise les boutons de la page produit
     */
    @FXML
    public void initialize() {
        btnSupprimer.setOnAction(e -> supprimerProduit());
        btnModifier.setOnAction(e -> modifierProduit());
    }



    /**
     * Supprime le produit de la base de données.
     */
    private void supprimerProduit() {
        if (produit != null && utilisateurConnecte.isAdmin()) {
            produitDao.supprimer(produit.getProduitId());
            System.out.println("Produit supprimé !");
            retourMagasin();
        }
    }

    /**
     * Modifie le produit dans la base de données.
     */
    private void modifierProduit() {
        try {
            produitDao.modifier(new Produit(produit.getProduitId(), champNom.getText(), Double.parseDouble(champPrix.getText()),Double.parseDouble(champPrixReduction.getText()), Integer.parseInt(champQteReduction.getText()), champImage.getText()));
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Retourne à la page du magasin.
     */
    @FXML
    private void retourMagasin() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shop.fxml"));
            Parent root = loader.load();
            MagasinControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) nomProduit.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du magasin");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Affiche les informations du produit dans la vue.
     */
    private void afficherProduit() {
        Image image = new Image(getClass().getResource("/" + produit.getImage()).toExternalForm(), 150, 150, true, true); // largeur, hauteur
        imageProduit.setImage(image);
        nomProduit.setText(produit.getNom());
        prixProduit.setText(String.valueOf(produit.getPrix()));
    }
}
