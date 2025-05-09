package Controller;

import Dao.DaoFactory;
import Model.Produit;
import Model.Utilisateur;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DaoFactory daoFactory = DaoFactory.getInstance("Shopping_ING3", "root", "");
        
        //Création du contrôleur de connexion
        ConnexionControlleur connexionControlleur = new ConnexionControlleur(daoFactory);
        
        //Menu principal
        Scanner scanner = new Scanner(System.in);
        int choix = 0;
        
        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. S'inscrire");
            System.out.println("2. Se connecter");
            System.out.println("3. Quitter");
            System.out.print("Votre choix : ");
            
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
                continue;
            }
            
            switch (choix) {
                case 1:
                    //Inscription
                    Utilisateur nouvelUtilisateur = connexionControlleur.inscription();
                    if (nouvelUtilisateur != null) {
                        //Si l'inscription réussit, on passe au menu client
                        menuClient(daoFactory, nouvelUtilisateur);
                    }
                    break;
                    
                case 2:
                    //Connexion
                    Utilisateur utilisateurConnecte = connexionControlleur.connexion();
                    if (utilisateurConnecte != null) {
                        //Si la connexion réussit, on passe au menu client
                        menuClient(daoFactory, utilisateurConnecte);
                    }
                    break;
                    
                case 3:
                    System.out.println("Au revoir.");
                    break;
                    
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
        } while (choix != 3);
        
        //Fermeture de la connexion à la base de données
        daoFactory.disconnect();
    }
    
    /**
     * Affiche le menu client et gère les interactions
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     */
    private static void menuClient(DaoFactory daoFactory, Utilisateur utilisateur) {
        //Création du contrôleur client avec l'utilisateur connecté
        CompteControlleur clientControlleur = new CompteControlleur(daoFactory, utilisateur);
        
        Scanner scanner = new Scanner(System.in);
        int choix = 0;
        
        do {
            System.out.println("\n===== MENU CLIENT =====");
            System.out.println("Bienvenue, " + utilisateur.getutilisateurName() + " !");
            System.out.println("1. Modifier mon compte");
            System.out.println("2. Voir mon historique de commandes");
            System.out.println("3. Aller au magasin");
            System.out.println("4. Se déconnecter");
            System.out.print("Votre choix : ");
            
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
                continue;
            }
            
            switch (choix) {
                case 1:
                    //Modifier le compte
                    Utilisateur utilisateurModifie = clientControlleur.ModifierCompte();
                    if (utilisateurModifie != null) {
                        utilisateur = utilisateurModifie;
                        System.out.println("Vos informations ont été mises à jour.");
                    }
                    break;
                    
                case 2:
                    //Voir l'historique des commandes
                    clientControlleur.GetHistorique();
                    break;
                    
                case 3:
                    //Aller au magasin
                    MagasinControlleur magasinControlleur = clientControlleur.allerMagasin();
                    if (magasinControlleur != null) {
                        menuMagasin(daoFactory, utilisateur, magasinControlleur);
                    }
                    break;
                    
                case 4:
                    System.out.println("Déconnexion...");
                    return; //Retour au menu principal
                    
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
        } while (choix != 4);
    }
    /**
     * Affiche le menu du magasin et gère les interactions
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     * @param magasinControlleur Contrôleur du magasin
     */
    private static void menuMagasin(DaoFactory daoFactory, Utilisateur utilisateur, MagasinControlleur magasinControlleur) {
        Scanner scanner = new Scanner(System.in);
        int choix = 0;
        
        //Création du contrôleur de paiement
        ControleurPaiement paiementControlleur = new ControleurPaiement(daoFactory, utilisateur, magasinControlleur);
        
        do {
            System.out.println("\n===== MENU MAGASIN =====");
            System.out.println("1. Voir les produits disponibles");
            System.out.println("2. Ajouter un produit au panier");
            System.out.println("3. Modifier la quantité d'un produit dans le panier");
            System.out.println("4. Supprimer un produit du panier");
            System.out.println("5. Voir mon panier");
            System.out.println("6. Passer commande");
            System.out.println("7. Vider mon panier");
            System.out.println("8. Retour au menu client");
            System.out.print("Votre choix : ");
            
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
                continue;
            }
            
            switch (choix) {
                case 1:
                    //Voir les produits disponibles
                    ArrayList<Produit> produits = magasinControlleur.GetInventaire();
                    System.out.println("\n=== PRODUITS DISPONIBLES ===");
                    for (Produit produit : produits) {
                        System.out.println(produit.getProduitId() + " - " + produit.getNom() + " - Prix: " +
                        produit.getPrix() + " Euros");
                        
                        //Afficher le prix réduit si disponible
                        if (produit.getPrixReduction() > 0 && produit.getQteReduction() > 0) {
                            System.out.println("   Promotion: " + produit.getPrixReduction() + 
                            " Euros à partir de " + produit.getQteReduction() + " unités");
                        }
                    }
                    break;
                    
                case 2:
                    //Ajouter un produit au panier
                    magasinControlleur.ajouterAuPanier();
                    break;
                    
                case 3:
                    //Modifier la quantité d'un produit
                    magasinControlleur.modifierQuantitePanier();
                    break;
                    
                case 4:
                    //Supprimer un produit du panier
                    magasinControlleur.supprimerDuPanier();
                    break;
                    
                case 5:
                    //Voir le panier
                    paiementControlleur.afficherPanier();
                    break;
                    
                case 6:
                    //Passer commande
                    int resultat = paiementControlleur.procederAuPaiement();
                    if (resultat == 1) {
                        System.out.println("Merci pour votre commande !");
                    }
                    break;
                    
                case 7:
                    //Vider le panier
                    magasinControlleur.viderPanier();
                    break;
                    
                case 8:
                    System.out.println("Retour au menu client.");
                    return;// Retour au menu client
                    
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
        } while (choix != 8);
    }
}