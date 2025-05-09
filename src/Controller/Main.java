package Controller;

import Dao.DaoFactory;
import Model.Utilisateur;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialisation de la factory DAO avec les paramètres de connexion à la base de données
        // Utilisation du nom correct de la base de données "Shopping_ING3" et pas de mot de passe
        DaoFactory daoFactory = DaoFactory.getInstance("shoppingjava", "root", "");
        
        // Création du contrôleur de connexion
        ConnexionControlleur connexionControlleur = new ConnexionControlleur(daoFactory);
        
        // Menu principal
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
//                case 1:
//                    // Inscription
//                    Utilisateur nouvelUtilisateur = connexionControlleur.inscription();
//                    if (nouvelUtilisateur != null) {
//                        // Si l'inscription réussit, on passe au menu client
//                        menuClient(daoFactory, nouvelUtilisateur);
//                    }
//                    break;
//
//                case 2:
//                    // Connexion
//                    Utilisateur utilisateurConnecte = connexionControlleur.connexion();
//                    if (utilisateurConnecte != null) {
//                        // Si la connexion réussit, on passe au menu client
//                        menuClient(daoFactory, utilisateurConnecte);
//                    }
//                    break;
                    
                case 3:
                    System.out.println("Au revoir !");
                    break;
                    
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
        } while (choix != 3);
        
        // Fermeture de la connexion à la base de données
        daoFactory.disconnect();
    }
    
    /**
     * Affiche le menu client et gère les interactions
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     */
    private static void menuClient(DaoFactory daoFactory, Utilisateur utilisateur) {
        // Création du contrôleur client avec l'utilisateur connecté
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
                    // Modifier le compte
                    Utilisateur utilisateurModifie = clientControlleur.ModifierCompte();
                    if (utilisateurModifie != null) {
                        utilisateur = utilisateurModifie;
                        System.out.println("Vos informations ont été mises à jour.");
                    }
                    break;
                    
                case 2:
                    // Voir l'historique des commandes
                    clientControlleur.GetHistorique();
                    break;
                    
                case 3:
                    // Aller au magasin
                    MagasinControlleur magasinControlleur = clientControlleur.allerMagasin();
                    break;
                    
                case 4:
                    System.out.println("Déconnexion...");
                    return; // Retour au menu principal
                    
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
        } while (choix != 4);
    }
}