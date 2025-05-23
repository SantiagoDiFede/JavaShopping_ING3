package Controller;

import Dao.DaoFactory;
import Dao.UtilisateurDaoImpl;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
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
     * Constructeur par défaut
     *
     */
    public ConnexionControlleur() {
        // JavaFX a besoin de ce constructeur pour charger le FXML
    }

    @FXML
    private TextField mailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField signupEmailField;

    @FXML
    private TextField signupNameField;

    @FXML
    private TextField signupLoginField;


    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private Button signupButton;


    /**
     * Initialise le contrôleur
     * Définit les actions des boutons de connexion et d'inscription
     */
    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> connexion());
        signupButton.setOnAction(event -> inscription());
    }

    /**
     *
     * Permet à un utilisateur de s'inscrire en saisissant ses informations
     * @return L'utilisateur créé ou null si l'inscription a échoué
     *
     */
    public void inscription() {
        String mail = signupEmailField.getText();
        String name = signupNameField.getText();
        String login = signupLoginField.getText();
        String password = signupPasswordField.getText();

        // Vérifier si l'utilisateur existe déjà
        Utilisateur utilisateurExistant = utilisateurDao.chercher(mail);

        if (utilisateurExistant != null) {
            System.out.println("L'utilisateur existe déjà.");
            return;
        }
        // Créer un nouvel utilisateur
        Utilisateur nouvelUtilisateur = new Utilisateur(login, password, name,mail , false);
        utilisateurDao.ajouter(nouvelUtilisateur);
        System.out.println("Inscription réussie de " + login);
        allerMagasin(daoFactory, nouvelUtilisateur);
    }
    
    /**
     * 
     * Permet à un utilisateur de se connecter en saisissant ses informations
     * @return L'utilisateur connecté ou null si la connexion a échoué
     * 
     */
    public void connexion() {
        String mail = mailField.getText();
        String password = passwordField.getText();

        Utilisateur utilisateur = utilisateurDao.chercher(mail);
        if (utilisateur != null && utilisateur.getutilisateurPassword().equals(password)) {
            System.out.println("Connexion réussie de " + mail);
            utilisateurConnecte = utilisateur;
            allerMagasin(daoFactory, utilisateur);
        } else {
            System.out.println("Échec de la connexion.");
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


    /**
     *
     * Récupère l'utilisateur actuellement connecté
     * @return L'utilisateur connecté ou null si aucun utilisateur n'est connecté
     *
     */
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

    /**
     *
     * Ouvre la page du magasin
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur L'utilisateur connecté
     *
     */
    public void  allerMagasin(DaoFactory daoFactory, Utilisateur utilisateur) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shop.fxml"));
            Parent root = loader.load();
            MagasinControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateur); // injecte les données après le load
            Stage currentStage = (Stage) loginButton.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page du magasin");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}