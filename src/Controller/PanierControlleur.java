package Controller;

import Dao.*;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Contrôleur gérant l'affichage du panier et le processus de paiement
 */
public class PanierControlleur {
    private DaoFactory daoFactory;
    private CommandeDao commandeDao;
    private CommandeLigneDao commandeLigneDao;
    private ProduitDao produitDao;
    private Utilisateur utilisateurConnecte;
    private Utilisateur utilisateur;

    @FXML
    private VBox produitsContainer;

    @FXML
    private Label prixTotalLabel;
    double prixTotal = 0;
    @FXML
    private ScrollPane scrollPaneProduits;
    @FXML
    private TextField nomPayment;
    @FXML
    private PasswordField numPayment;
    @FXML
    private PasswordField cvcPayment;
    @FXML
    private TextField dateExp;
    

    public PanierControlleur() {

    }

    public void initData(DaoFactory daoFactory, Utilisateur utilisateurConnecte, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.utilisateurConnecte = utilisateurConnecte;
        this.utilisateur = utilisateur;
        afficherPanierEnCours();
    }

    public void afficherPanierEnCours() {
        Commande panier = commandeDao.getLastCommande(utilisateur.getutilisateurId());
        if (panier == null) return;

        List<CommandeLigne> lignes = commandeLigneDao.getAllFromCommande(panier.getCommandeId());
        produitsContainer.getChildren().clear();

        for (CommandeLigne ligne : lignes) {
            Produit produit = produitDao.chercher(ligne.getProduitId());

            // Créer dynamiquement l'élément produit
            HBox produitBox = new HBox(20);
            produitBox.getStyleClass().add("card");
            produitBox.setPadding(new Insets(10, 10, 10, 10));

            // Image
            ImageView imageView = new ImageView();
            System.out.println("Image path: " + produit.getImage());
            Image image = new Image(getClass().getResource("/" + produit.getImage()).toExternalForm());
            imageView.setImage(image);
            imageView.setFitHeight(95);
            imageView.setFitWidth(128);

            // Info produit
            VBox infoBox = new VBox(10);
            Label nomLabel = new Label(produit.getNom());
            nomLabel.setFont(new Font("System Bold", 23));
            Label prixUnitaire = new Label("Prix unitaire : " + produit.getPrix() + " €");

            infoBox.getChildren().addAll(nomLabel, prixUnitaire);

            // Quantité
            HBox quantiteBox = new HBox(10);
            Button moins = new Button("-");
            Label quantite = new Label(String.valueOf(ligne.getQte()));
            Button plus = new Button("+");

            moins.setOnAction(e -> {
                int qte = ligne.getQte();
                if (qte > 1) {
                    ligne.setQte(qte - 1);
                    commandeLigneDao.updateQte(ligne.getCommandeLigneId(), qte - 1);
                } else {
                    commandeLigneDao.supprimer(ligne.getCommandeLigneId());
                }
                rafraichirPanier();
            });

            plus.setOnAction(e -> {
                int qte = ligne.getQte();
                ligne.setQte(qte + 1);
                commandeLigneDao.updateQte(ligne.getCommandeLigneId(), qte + 1);
                rafraichirPanier();
            });

            quantiteBox.getChildren().addAll(moins, quantite, plus);

            // Prix total

            int qteTotale = ligne.getQte();
            int seuil = produit.getQteReduction();
            double prixNormal = produit.getPrix();
            double prixReduit = produit.getPrixReduction();

            int nbLots = seuil > 0 ? qteTotale / seuil : 0;
            int reste = seuil > 0 ? qteTotale % seuil : qteTotale;

            double total = nbLots * seuil * prixReduit + reste * prixNormal;
            Label prixTotal = new Label("Total : " + String.format("%.2f", total) + " €");
            prixTotal.setFont(new Font("System Bold", 15));

            produitBox.getChildren().addAll(imageView, infoBox, quantiteBox, prixTotal);
            produitsContainer.getChildren().add(produitBox);
        }
        mettreAJourTotal(lignes);
    }

    private void mettreAJourTotal(List<CommandeLigne> lignes) {
        double totalGeneral = 0.0;
        for (CommandeLigne ligne : lignes) {
            Produit produit = produitDao.chercher(ligne.getProduitId());

            int qteTotale = ligne.getQte();
            int seuil = produit.getQteReduction();
            double prixNormal = produit.getPrix();
            double prixReduit = produit.getPrixReduction();

            int nbLots = seuil > 0 ? qteTotale / seuil : 0;
            int reste = seuil > 0 ? qteTotale % seuil : qteTotale;

            totalGeneral += nbLots * seuil * prixReduit + reste * prixNormal;
        }

        prixTotalLabel.setText("TOTAL : " + String.format("%.2f", totalGeneral) + " €");
    }

    private void rafraichirPanier() {
        afficherPanierEnCours();
    }
    @FXML
    private void payer() {
        Commande panier = commandeDao.getLastCommande(utilisateur.getutilisateurId());
        // Vérification des informations de paiement
        String nom = nomPayment.getText();
        String num = numPayment.getText();
        String cvc = cvcPayment.getText();
        String date = dateExp.getText();
        if (panier != null) {
            if (nom.isEmpty() || num.isEmpty() || cvc.isEmpty() || date.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de paiement");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs de paiement.");
                alert.showAndWait();
                return;
            }
            panier.setStatutCommande("Payé");
            commandeDao.modifier(panier);
            System.out.println("Panier payé !");
            retourMagasin();
        }
    }

    @FXML
    private void retourMagasin() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shop.fxml"));
            Parent root = loader.load();
            MagasinControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte); // injecte les données après le load
            Stage currentStage = (Stage) prixTotalLabel.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du magasin");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}