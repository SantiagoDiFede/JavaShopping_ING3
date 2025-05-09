package Controller;

import Dao.*;
import Model.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Contrôleur gérant l'affichage du panier et le processus de paiement
 */
public class ControleurPaiement {
    private DaoFactory daoFactory;
    private CommandeDao commandeDao;
    private CommandeLigneDao commandeLigneDao;
    private ProduitDao produitDao;
    private Utilisateur utilisateurConnecte;
    private MagasinControlleur magasinControlleur;
    
    /**
     * Constructeur du contrôleur de paiement
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     * @param magasinControlleur Contrôleur du magasin contenant le panier
     */
    public ControleurPaiement(DaoFactory daoFactory, Utilisateur utilisateur, MagasinControlleur magasinControlleur) {
        this.daoFactory = daoFactory;
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.utilisateurConnecte = utilisateur;
        this.magasinControlleur = magasinControlleur;
    }
    
    /**
     * Affiche le contenu du panier
     * @return Le montant total du panier
     */
    public double afficherPanier() {
        try {
            //Vérifier que l'utilisateur est connecté
            if (utilisateurConnecte == null) {
                System.out.println("Vous devez être connecté pour voir votre panier.");
                return 0;
            }
            
            //Récupérer le panier du MagasinControlleur
            Map<Integer, Integer> panier = magasinControlleur.getPanier();
            
            // Vérifier si le panier est vide
            if (panier.isEmpty()) {
                System.out.println("Votre panier est vide.");
                return 0;
            }
            
            System.out.println("\n===== VOTRE PANIER =====");
            System.out.println("------------------------");
            
            double total = 0;
            
            //Parcourir les produits du panier
            for (Map.Entry<Integer, Integer> entry : panier.entrySet()) {
                int produitId = entry.getKey();
                int quantite = entry.getValue();
                
                Produit produit = produitDao.chercher(produitId);
                if (produit != null) {
                    double prixUnitaire = produit.getPrix();
                    double prixTotal;
                    
                    //Appliquer le prix réduit 
                    if (produit.getPrixReduction() > 0 && produit.getQteReduction() > 0 && quantite >= produit.getQteReduction()) {
                        prixUnitaire = produit.getPrixReduction();
                        System.out.println(quantite + "x " + produit.getNom() + 
                                          " - Prix unitaire: " + prixUnitaire + " Euros (prix réduit)" +
                                          " - Total: " + (prixUnitaire * quantite) + " Euros");
                    } else {
                        System.out.println(quantite + "x " + produit.getNom() + 
                                          " - Prix unitaire: " + prixUnitaire + " Euros" +
                                          " - Total: " + (prixUnitaire * quantite) + " Euros");
                    }
                    
                    prixTotal = prixUnitaire * quantite;
                    total += prixTotal;
                } else {
                    System.out.println(quantite + "x Produit inconnu (ID: " + produitId + ")");
                }
            }
            
            System.out.println("------------------------");
            System.out.println("TOTAL: " + total + " Euros");
            System.out.println("------------------------");
            
            return total;
            
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage du panier: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
 * Procède au paiement et crée une commande
 * @return 1 si la commande a été créée avec succès, -1 en cas d'erreur
 */
public int procederAuPaiement() {
    try {
        //Vérifier que l'utilisateur est connecté
        if (utilisateurConnecte == null) {
            System.out.println("Vous devez être connecté pour passer une commande.");
            return -1;
        }
        
        //Récupérer le panier du MagasinControlleur
        Map<Integer, Integer> panier = magasinControlleur.getPanier();
        
        //Vérifier si le panier est vide
        if (panier.isEmpty()) {
            System.out.println("Votre panier est vide. Impossible de passer une commande.");
            return -1;
        }
        
        //Calculer le montant total
        double total = 0;
        for (Map.Entry<Integer, Integer> entry : panier.entrySet()) {
            int produitId = entry.getKey();
            int quantite = entry.getValue();
            
            Produit produit = produitDao.chercher(produitId);
            if (produit != null) {
                double prixUnitaire = produit.getPrix();
                
                //Appliquer le prix réduit si la quantité est suffisante
                if (produit.getPrixReduction() > 0 && produit.getQteReduction() > 0 && quantite >= produit.getQteReduction()) {
                    prixUnitaire = produit.getPrixReduction();
                }
                
                total += prixUnitaire * quantite;
            }
        }
        
        //Afficher le récapitulatif de la commande
        System.out.println("\n===== RÉCAPITULATIF DE LA COMMANDE =====");
        afficherPanier();
        
        //Demander confirmation
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nConfirmer la commande (OUI/NON) ? ");
        String confirmation = scanner.nextLine().trim().toUpperCase();
        
        if (!confirmation.equals("O")) {
            System.out.println("Commande annulée.");
            return -1;
        }
        
        
        //Créer la commande
        Commande commande = new Commande(utilisateurConnecte.getutilisateurId(), total, "En Préparation");
        commandeDao.ajouter(commande);
        
        //Récupérer la dernière commande créée pour cet utilisateur
        ArrayList<Commande> commandes = commandeDao.getCommandesUtilisateur(utilisateurConnecte.getutilisateurId());
        if (commandes.isEmpty()) {
            System.out.println("Erreur lors de la récupération de la commande.");
            return -1;
        }
        
        //La dernière commande est la première dans la liste  tri par ordre décroissant
        Commande derniereCommande = commandes.get(0);
        int commandeId = derniereCommande.getCommandeId();
        
        //Créer les lignes de commande
        for (Map.Entry<Integer, Integer> entry : panier.entrySet()) {
            int produitId = entry.getKey();
            int quantite = entry.getValue();
            
            CommandeLigne ligne = new CommandeLigne(commandeId, produitId, quantite);
            commandeLigneDao.ajouter(ligne);
        }
        
        System.out.println("Commande #" + commandeId + " créée !");
        
        //Vider le panier
        magasinControlleur.viderPanier();
        
        return 1;// Retourner 1 pour indiquer le succès
        
    } catch (Exception e) {
        System.out.println("Erreur lors du paiement: " + e.getMessage());
        e.printStackTrace();
        return -1;
    }
}
}