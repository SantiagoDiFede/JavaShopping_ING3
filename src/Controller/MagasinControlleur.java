package Controller;
import Dao.CommandeDao;
import Dao.CommandeDaoImpl;
import Dao.CommandeLigneDao;
import Dao.CommandeLigneDaoImpl;
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

public class MagasinControlleur {

    DaoFactory daoFactory;
    ProduitDaoImpl produitDao;
    UtilisateurDaoImpl utilisateurDao;
    Utilisateur utilisateurConnecte;


    @FXML
    private GridPane produitsContainer;


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
        afficherProduits();
    }


    public void initData(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        afficherProduits();
    }

    @FXML
    private void initialize() {
        // appelée automatiquement, mais daoFactory peut être null ici
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
                System.out.println("Image path:/" + produit.getImage());
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
    /**
     * Ajoute un produit au panier
     * @param produitId ID du produit à ajouter
     * @param quantite Quantité à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean ajouterAuPanier() {
    try {
        Scanner scanner = new Scanner(System.in);
        
        //Afficher la liste des produits disponibles
        ArrayList<Produit> produits = GetInventaire();
        System.out.println("\n=== PRODUITS DISPONIBLES ===");
        for (Produit produit : produits) {
            System.out.println(produit.getProduitId() + " - " + produit.getNom() + " - " + produit.getPrix() + " Euros");
        }
        
        //Demander l'ID du produit
        System.out.print("\nEntrez l'ID du produit à ajouter : ");
        int produitId;
        try {
            produitId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de produit invalide.");
            return false;
        }
        
        //Vérifier que le produit existe
        Produit produit = produitDao.chercher(produitId);
        if (produit == null) {
            System.out.println("Le produit avec l'ID " + produitId + " n'existe pas.");
            return false;
        }
        
        //Demander la quantité
        System.out.print("Entrez la quantité : ");
        int quantite;
        try {
            quantite = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Quantité invalide.");
            return false;
        }
        
        //Vérifier que la quantité est positive
        if (quantite <= 0) {
            System.out.println("La quantité doit être positive.");
            return false;
        }
        
        //Ajouter le produit au panier ou mettre à jour la quantité
        if (panier.containsKey(produitId)) {
            int quantiteActuelle = panier.get(produitId);
            panier.put(produitId, quantiteActuelle + quantite);
        } else {
            panier.put(produitId, quantite);
        }
        
        System.out.println(quantite + "x " + produit.getNom() + " ajouté(s) au panier.");
        return true;
        
    } catch (Exception e) {
        System.out.println("Erreur lors de l'ajout au panier: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * Supprime un produit du panier en demandant l'ID à l'utilisateur
 * @return true si la suppression a réussi, false sinon
 */
public boolean supprimerDuPanier() {
    try {
        Scanner scanner = new Scanner(System.in);
        
        //Vérifier si le panier est vide
        if (panier.isEmpty()) {
            System.out.println("Votre panier est vide.");
            return false;
        }
        
        //Afficher le contenu du panier
        System.out.println("\n=== VOTRE PANIER ===");
        for (Map.Entry<Integer, Integer> entry : panier.entrySet()) {
            int produitId = entry.getKey();
            int quantite = entry.getValue();
            
            Produit produit = produitDao.chercher(produitId);
            if (produit != null) {
                System.out.println(produitId + " - " + quantite + "x " + produit.getNom());
            } else {
                System.out.println(produitId + " - " + quantite + "x Produit inconnu");
            }
        }
        
        //Demander l'ID du produit à supprimer
        System.out.print("\nEntrez l'ID du produit à supprimer : ");
        int produitId;
        try {
            produitId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de produit invalide.");
            return false;
        }
        
        //Vérifier que le produit est dans le panier
        if (!panier.containsKey(produitId)) {
            System.out.println("Le produit avec l'ID " + produitId + " n'est pas dans votre panier.");
            return false;
        }
        
        //Récupérer le nom du produit pour le message
        Produit produit = produitDao.chercher(produitId);
        String nomProduit = (produit != null) ? produit.getNom() : "Produit inconnu";
        
        //Supprimer le produit du panier
        panier.remove(produitId);
        
        System.out.println(nomProduit + " supprimé du panier.");
        return true;
        
    } catch (Exception e) {
        System.out.println("Erreur lors de la suppression du panier: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

/**
 * Modifie la quantité d'un produit dans le panier 
 * @return true si la modification a réussi, false sinon
 */
public boolean modifierQuantitePanier() {
    try {
        Scanner scanner = new Scanner(System.in);
        
        //Vérifier si le panier est vide
        if (panier.isEmpty()) {
            System.out.println("Votre panier est vide.");
            return false;
        }
        
        //Afficher le contenu du panier
        System.out.println("\n=== VOTRE PANIER ===");
        for (Map.Entry<Integer, Integer> entry : panier.entrySet()) {
            int produitId = entry.getKey();
            int quantite = entry.getValue();
            
            Produit produit = produitDao.chercher(produitId);
            if (produit != null) {
                System.out.println(produitId + " - " + quantite + "x " + produit.getNom());
            } else {
                System.out.println(produitId + " - " + quantite + "x Produit inconnu");
            }
        }
        
        //Demander l'ID du produit à modifier
        System.out.print("\nEntrez l'ID du produit à modifier : ");
        int produitId;
        try {
            produitId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de produit invalide.");
            return false;
        }
        
        //Vérifier que le produit est dans le panier
        if (!panier.containsKey(produitId)) {
            System.out.println("Le produit avec l'ID " + produitId + " n'est pas dans votre panier.");
            return false;
        }
        
        //Demander la nouvelle quantité
        System.out.print("Entrez la nouvelle quantité : ");
        int nouvelleQuantite;
        try {
            nouvelleQuantite = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Quantité invalide.");
            return false;
        }
        
        //Vérifier que la quantité est positive
        if (nouvelleQuantite <= 0) {
            System.out.println("La quantité doit être positive.");
            return false;
        }
        
        //Récupérer le nom du produit pour le message
        Produit produit = produitDao.chercher(produitId);
        String nomProduit = (produit != null) ? produit.getNom() : "Produit inconnu";
        
        //Mettre à jour la quantité
        panier.put(produitId, nouvelleQuantite);
        
        System.out.println("Quantité de " + nomProduit + " mise à jour à " + nouvelleQuantite + ".");
        return true;
        
    } catch (Exception e) {
        System.out.println("Erreur lors de la modification de la quantité: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    /**
     * Vide le panier
     */
    public void viderPanier() {
        panier.clear();
        System.out.println("Votre panier a été vidé.");
    }
    
    /**
     * Vérifie si le panier est vide
     * @return true si le panier est vide, false sinon
     */
    public boolean estPanierVide() {
        return panier.isEmpty();
    }
    
    /**
     * Retourne le nombre d'articles dans le panier
     * @return Nombre d'articles dans le panier
     */
    public int getNombreArticlesPanier() {
        int total = 0;
        for (int quantite : panier.values()) {
            total += quantite;
        }
        return total;
    }
    /**
     * Retourne le panier actuel
     * @return Map contenant les produits du panier (clé = ID du produit, valeur = quantité)
     */
    public Map<Integer, Integer> getPanier() {
        return this.panier;
    }
    
    /**
     * Retourne l'utilisateur connecté
     * @return Utilisateur connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return this.utilisateurConnecte;
    }
}
