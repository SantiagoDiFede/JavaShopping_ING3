package Controller;

import Dao.*;
import Model.Commande;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AdminControlleur {
    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    private CommandeDaoImpl commandeDao;
    private CommandeLigneDaoImpl commandeLigneDao;
    private ProduitDaoImpl produitDao;
    private Utilisateur utilisateurConnecte;
    private CompteControlleur compteControlleur;
    @FXML
    private VBox commandesContainer;

    @FXML
    private VBox clientsContainer;

    @FXML private VBox produitsContainer;
    @FXML private Button ajouterProduitButton;

    @FXML private Label ventesTotalesLabel;
    @FXML private VBox ventesParProduitContainer;


    /**
     * Constructeur du contrôleur d'administration
     * @param daoFactory Factory pour accéder aux DAO
     */
    public AdminControlleur(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;


    }

    /**
     * Constructeur par défaut
     */
    public AdminControlleur() {
        // JavaFX a besoin de ce constructeur pour charger le FXML
    }

    /**
     * Méthode pour initialiser les données du contrôleur
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     */
    public void initData(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
        this.produitDao = new ProduitDaoImpl(daoFactory);
        List<Commande> commandes = commandeDao.getAll();
        setCommandes(commandes);
        List<Utilisateur> clients = utilisateurDao.getAll();
        setClients(clients);
        List<Produit> produits = produitDao.getAll();
        setProduits(produits);
        afficherStatistiques(produits);
    }

    public void setCommandes(List<Commande> commandes) {
        commandesContainer.getChildren().clear();

        commandes.sort(Comparator.comparing(Commande::getCommandeId).reversed());
        for (int i = 0; i < commandes.size(); i++) {
            if (Objects.equals(commandes.get(i).getStatutCommande(), "En cours")) {
                commandes.remove(i);
            }
        }
        int commandeNumero = commandes.size();


        for (Commande commande : commandes) {


            HBox commandeBox = new HBox(100);
            commandeBox.setAlignment(Pos.CENTER_LEFT);
            commandeBox.setPadding(new Insets(5));

            Label label = new Label("#" + commandeNumero--);
            Button btn = new Button("Facture de l'utilisateur n°" + commande.getUtilisateurId());
            btn.setOnAction(e -> afficherFacture(commande.getCommandeId()));

            commandeBox.getChildren().addAll(label, btn);
            commandesContainer.getChildren().add(commandeBox);
        }
    }

    public void afficherFacture(int commandeId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/payment.fxml"));
            Parent root = loader.load();
            PanierControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte, utilisateurConnecte, commandeId); // injecte les données après le load
            Stage currentStage = (Stage) commandesContainer.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page de facture n°" + commandeId);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClients(List<Utilisateur> clients) {
        clientsContainer.getChildren().clear();

        for (Utilisateur client : clients) {
            HBox clientBox = new HBox(150);
            clientBox.setAlignment(Pos.CENTER_LEFT);
            clientBox.setPadding(new Insets(10, 0, 10, 50));

            clientBox.setOnMouseClicked(event -> {
                ouvrirProfilClient(client); // Méthode à créer
            });

            Label idLabel = new Label("ID_" + client.getUtilisateurId() + " : " + client.getutilisateurName());
            Button deleteBtn = new Button();
            ImageView icon = new ImageView(new Image(getClass().getResource("/icons8-close-50.png").toExternalForm()));
            icon.setFitHeight(22);
            icon.setFitWidth(22);
            deleteBtn.setGraphic(icon);

            deleteBtn.setOnAction(e -> {
                utilisateurDao = new UtilisateurDaoImpl(daoFactory);
                utilisateurDao.supprimer(client);
                clientsContainer.getChildren().remove(clientBox);
            });

            clientBox.getChildren().addAll(idLabel, deleteBtn);
            clientsContainer.getChildren().add(clientBox);
        }
    }

    public void setProduits(List<Produit> produits) {
        produitsContainer.getChildren().clear();

        for (Produit produit : produits) {
            HBox produitBox = new HBox(20);
            produitBox.setAlignment(Pos.CENTER_LEFT);
            produitBox.setPadding(new Insets(5, 10, 5, 10));
            produitBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

            Label nomLabel = new Label("- " + produit.getNom() + " (prix unitaire) : ");
            Label prixLabel = new Label(produit.getPrix() + "€");

            Button deleteBtn = new Button();
            ImageView icon = new ImageView(new Image(getClass().getResource("/icons8-close-50.png").toExternalForm()));
            icon.setFitHeight(16);
            icon.setFitWidth(16);
            deleteBtn.setGraphic(icon);
            deleteBtn.setOnAction(e -> supprimerProduit(produit.getProduitId()));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            produitBox.getChildren().addAll(nomLabel, prixLabel, spacer, deleteBtn);
            produitsContainer.getChildren().add(produitBox);
        }
    }

    private void ouvrirProfilClient(Utilisateur utilisateur) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/account.fxml"));
                Parent root = loader.load();
                CompteControlleur controller = loader.getController();
                controller.initData(daoFactory, utilisateurConnecte, utilisateur); // injecte les données après le load
                Stage currentStage = (Stage) produitsContainer.getScene().getWindow(); // ou un autre bouton/label
                currentStage.setScene(new Scene(root));
                currentStage.setTitle("Page du compte");
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }



    @FXML
    private void ajouterProduit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();

            AjouterProduitControlleur popupController = loader.getController();
            popupController.setAdminController(this);

            Stage popup = new Stage();
            popup.setTitle("Ajouter un Produit");
            popup.setScene(new Scene(root));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterProduitDepuisPopup(Produit produit) {
        System.out.println("Produit ajouté : " + produit.getNom());

        produitDao.ajouter(produit); // ou depuis une base
        setProduits(produitDao.getAll()); // Recharge l'affichage
    }

    private void supprimerProduit(int produitId) {
        produitDao.supprimer(produitId);
        setProduits(produitDao.getAll());
    }


    public void afficherStatistiques(List<Produit> produits) {
        double total = 0;
        for (Produit p : produits) {
            total = total + (p.getPrix() * commandeLigneDao.ventesTotal(p.getProduitId()));
        }
        ventesTotalesLabel.setText("Ventes totales : " + total + " €");

        ventesParProduitContainer.getChildren().clear();
        for (Produit p : produits) {
            Label label = new Label(p.getNom() + " : " + commandeLigneDao.ventesTotal(p.getProduitId()) + " ventes pour un total de " + (p.getPrix() * commandeLigneDao.ventesTotal(p.getProduitId())) + " €");
            ventesParProduitContainer.getChildren().add(label);
        }
    }

    @FXML
    private void retourMagasin() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shop.fxml"));
            Parent root = loader.load();
            MagasinControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) commandesContainer.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du magasin");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
