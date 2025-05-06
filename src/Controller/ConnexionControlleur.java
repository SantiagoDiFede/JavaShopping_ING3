package Controller;

import Dao.DaoFactory;
import Dao.UtilisateurDaoImpl;
import Model.Utilisateur;

public class ConnexionControlleur {
    
    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    
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
     * Permet à un utilisateur de s'inscrire
     * @param login Login de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @param name Nom de l'utilisateur
     * @param email Email de l'utilisateur
     * @return L'utilisateur créé ou null si l'inscription a échoué
     * 
     */
    public Utilisateur inscription(String login, String password, String name, String email) {
        try {
            //Vérifier si le login existe déjà
            Utilisateur existant = utilisateurDao.chercher(login);
            // si le login est déja existant
            if (existant != null) {
                System.out.println("Ce login est déjà utilisé");
                return null;
            }
            
            //Créer un nouvel utilisateur, pas admin par défaut
            Utilisateur nouvelUtilisateur = new Utilisateur(login, password, name, email, false);
            
            //Ajouter l'utilisateur dans la base de données
            utilisateurDao.ajouter(nouvelUtilisateur);
            
            //Afficher un message de succes de l'inscription
            System.out.println("Inscription réussie de l'utilisateur: " + login);
            return nouvelUtilisateur;
            
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 
     * Permet à un utilisateur de se connecter
     * @param login Login de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return L'utilisateur connecté ou null si la connexion a échoué
     * 
     */
    public Utilisateur connexion(String login, String password) {
        try {
            //Chercher l'utilisateur avec son login
            Utilisateur utilisateur = utilisateurDao.chercher(login);
            
            /**Vérifier si l'utilisateur existe 
            *ET
            *Compare le mot de passe stocké dans la base de données avec le mot de passe saisi lors de la tentative de connexion.
            */
            if (utilisateur != null && utilisateur.getutilisateurPassword().equals(password)) {
                System.out.println("Connexion réussie pour l'utilisateur: " + login);
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
}