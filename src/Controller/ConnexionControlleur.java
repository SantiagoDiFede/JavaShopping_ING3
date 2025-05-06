package Controller;

import Dao.DaoFactory;
import Dao.UtilisateurDaoImpl;
import Model.Utilisateur;
import java.util.Scanner;

public class ConnexionControlleur {
    
    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    private Utilisateur utilisateurConnecte; // Nouvel attribut pour stocker l'utilisateur connecté
    
    /**
     * 
     * Constructeur du contrôleur de connexion
     * @param daoFactory Factory pour accéder aux DAO
     * 
     */
    public ConnexionControlleur(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
    }
    
    /**
     * 
     * Permet à un utilisateur de s'inscrire en saisissant ses informations
     * @return L'utilisateur créé ou null si l'inscription a échoué
     * 
     */
    public Utilisateur inscription() {
        Scanner scanner =new Scanner(System.in);
        try {
            System.out.println("INSCRIPTION");

            //Saisie du login
            System.out.print("Saisissez votre login :");
            String login = scanner.nextLine();

            //Vérifier si le login existe déjà
            Utilisateur existant = utilisateurDao.chercher(login);
            // si le login est déja existant
            if (existant != null) {
                System.out.print("Ce login est déjà utilisé");
                return null;
            }
            //Saisie du nom
            System.out.print("Saisissez votre nom :");
            String name = scanner.nextLine();

            //Saisie de l'email
            System.out.print("Saisissez votre email ;");
            String email = scanner.nextLine();

            //Saisie du mot de passe
            System.out.print("Saisissez votre mot de passe :");
            String password = scanner.nextLine();

            //Créer un nouvel utilisateur, pas admin par défaut
            Utilisateur nouvelUtilisateur = new Utilisateur(login, password, name, email, false);
            
            //Ajouter l'utilisateur dans la base de données
            utilisateurDao.ajouter(nouvelUtilisateur);
            
            //Afficher un message de succes de l'inscription
            System.out.println("Inscription réussie de l'utilisateur: " + login);

            // Connecter automatiquement l'utilisateur après inscription
            this.utilisateurConnecte = nouvelUtilisateur;
            
            return nouvelUtilisateur;
            
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * Permet à un utilisateur de se connecter en saisissant ses informations
     * @return L'utilisateur connecté ou null si la connexion a échoué
     * 
     */
    public Utilisateur connexion() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("CONNEXION");

            //Saisie du login
            System.out.print("Saisissez votre login :");
            String login = scanner.nextLine();

            //Saisie du mot de passse
            System.out.print("Saisissez votre mot de passe :");
            String password = scanner.nextLine();


            //Chercher l'utilisateur avec son login
            Utilisateur utilisateur = utilisateurDao.chercher(login);
            
            /**Vérifier si l'utilisateur existe 
            *ET
            *Compare le mot de passe stocké dans la base de données avec le mot de passe saisi lors de la tentative de connexion.
            */
            if (utilisateur != null && utilisateur.getutilisateurPassword().equals(password)) {
                System.out.println("Connexion réussie pour l'utilisateur: " + login);

                // Stocker l'utilisateur connecté
                this.utilisateurConnecte = utilisateur;

                return utilisateur;

            } else {
                System.out.println("Login ou mot de passe incorrect");
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * Vérifie si un utilisateur existe et est administrateur
     * @param utilisateur L'utilisateur à vérifier
     * @return true si l'utilisateur est administrateur, false sinon
     * 
     */
    public boolean estAdmin(Utilisateur utilisateur) {
        return utilisateur != null && utilisateur.isAdmin();
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    /**
     * 
     * Déconnecte l'utilisateur actuel
     * @return true si la déconnexion est réussie, false sinon
     * 
     */
    public boolean deconnexion() {
        if (utilisateurConnecte != null) {
            utilisateurConnecte = null;
            System.out.println("Déconnexion réussie.");
            return true;
        } else {
            System.out.println("Aucun utilisateur n'est connecté.");
            return false;
        }
    }
}