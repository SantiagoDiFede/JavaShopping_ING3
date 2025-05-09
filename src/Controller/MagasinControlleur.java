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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MagasinControlleur {

    DaoFactory daoFactory;

    ProduitDaoImpl produitDao;
    UtilisateurDaoImpl utilisateurDao;
    private CommandeDao commandeDao;
    private CommandeLigneDao commandeLigneDao;
    private Utilisateur utilisateurConnecte;
    private Map<Integer, Integer> panier = new HashMap<>();

    /**
 * Constructeur avec daoFactory et utilisateur
 * @param daoFactory Factory pour accéder aux DAO
 * @param utilisateur Utilisateur connecté
 */
public MagasinControlleur(DaoFactory daoFactory, Utilisateur utilisateur) {
    this.daoFactory = daoFactory;
    this.produitDao = new ProduitDaoImpl(daoFactory);
    this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
    this.commandeDao = new CommandeDaoImpl(daoFactory);
    this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
    this.utilisateurConnecte = utilisateur;
}

    public ArrayList<Produit> GetInventaire() {
    return produitDao.getAll();
}

    public void SupprimerProduit(int produitId, int utilisateurId){
        // Vérifier si l'utilisateur est un administrateur
        Utilisateur user = utilisateurDao.chercher(utilisateurId);
        if (user == null || !user.isAdmin()) {
            throw new IllegalArgumentException("L'utilisateur n'est pas un administrateur.");
        }
        produitDao.supprimer(produitId);
    }

    public void AjouterProduit(Produit produit) {
        produitDao.ajouter(produit);
    }

    public void ModifierProduit(Produit produit) {
        produitDao.modifier(produit);
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
