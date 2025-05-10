package Controller;
import Dao.DaoFactory;
import Dao.ProduitDaoImpl;
import Dao.UtilisateurDaoImpl;
import Model.Produit;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MagasinControlleur {

    DaoFactory daoFactory;
    ProduitDaoImpl produitDao;
    UtilisateurDaoImpl utilisateurDao;
    Utilisateur utilisateurConnecte;


    @FXML
    private GridPane produitsContainer;

    @FXML private Label adminLabel;
    @FXML private Label compteLabel;
    @FXML private Label panierLabel;


    public MagasinControlleur() {
        // JavaFX a besoin de ce constructeur pour charger le FXML
    }

    /**
     * Constructeur du contrôleur de magasin
     * @param daoFactory Factory pour accéder aux DAO
     */
    public MagasinControlleur(DaoFactory daoFactory,Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        this.utilisateurConnecte = utilisateur;

    }


    public void initData(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        if(utilisateurConnecte.isAdmin()) {
            adminLabel.setVisible(true);
        }
        afficherProduits();
    }


    public void afficherProduits() {
        if (produitsContainer == null) {
            System.out.println("produitsContainer est null !");
            return;
        }

        List<Produit> produits = produitDao.getAll();
        produitsContainer.getChildren().clear();

        int column = 0;
        int row = 0;
        int maxColumns = 3; // nombre de colonnes par ligne

        for (Produit produit : produits) {
            // Image
            ImageView imageView = new ImageView();
            try {
                Image image = new Image(getClass().getResource("/" + produit.getImage()).toExternalForm(), 150, 150, true, true); // largeur, hauteur
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }

            // Nom & prix
            Label nom = new Label(produit.getNom());
            Label prix = new Label("Prix : " + produit.getPrix() + " €");

            VBox produitBox = new VBox(imageView, nom, prix);
            produitBox.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-alignment: center;");

            // Ajouter de la marge autour de chaque produit
            VBox.setMargin(produitBox, new Insets(15));

            produitBox.setSpacing(20); // espace entre les VBoxes des produits
            produitsContainer.setPadding(new Insets(20)); // espace intérieur global du conteneur

            produitBox.setOnMouseClicked(event -> {
                allerPageProduit(daoFactory, utilisateurConnecte, produit);
            });

            produitsContainer.add(produitBox, column, row);

            column++;
            if (column == maxColumns) {
                column = 0;
                row++;
            }
        }
    }

    public void allerPageProduit(DaoFactory daoFactory, Utilisateur utilisateur, Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/produit.fxml"));
            Parent root = loader.load();
            ProduitControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateur,produit); // injecte les données après le load
            Stage currentStage = (Stage) produitsContainer.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du produit");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerCompte() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/account.fxml"));
            Parent root = loader.load();
            CompteControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte, utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) compteLabel.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du compte");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void allerPanier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/payment.fxml"));
            Parent root = loader.load();
            PanierControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte,utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) panierLabel.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du panier");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Parent root = loader.load();
            AdminControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) panierLabel.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du panier");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
