package Dao;

// import des packages
import Model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;

/**
 * implémentation MySQL du stockage dans la base de données des méthodes définies dans l'interface
 * utilisateurDao.
 */
public class UtilisateurDaoImpl implements UtilisateurDao {
    // attribut privé pour l'objet du DaoFactoru
    private DaoFactory daoFactory;

    // constructeur dépendant de la classe DaoFactory
    public UtilisateurDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    /**
     * Récupérer de la base de données tous les objets des utilisateurs dans une liste
     * @return : liste retournée des objets des utilisateurs récupérés
     */
    public ArrayList<Utilisateur> getAll() {
        ArrayList<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();

        /*
            Récupérer la liste des utilisateurs de la base de données dans listeUtilisateurs
        */
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            
            PreparedStatement preparedStatement = connexion.prepareStatement("SELECT * FROM Utilisateur");

            // récupération des utilisateurs de la base de données avec la requete SELECT
            ResultSet resultats = preparedStatement.executeQuery();

            // Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les champs de la table Utilisateur dans la base de données
                int utilisateurId = resultats.getInt("UtilisateurID");
                String utilisateurLogin = resultats.getString("Login");
                String utilisateurPassword = resultats.getString("Password");
                String utilisateurName = resultats.getString("Name");
                String utilisateurMail = resultats.getString("Mail");
                Boolean utilisateurIsAdmin = resultats.getBoolean("isAdmin");

                //instancier un objet de Utilisateur avec ces champs en paramètres
                Utilisateur utilisateur = new Utilisateur(utilisateurId, utilisateurLogin, utilisateurPassword, utilisateurName, utilisateurMail, utilisateurIsAdmin);

                // ajouter cet utilisateur à listeUtilisateurs
                listeUtilisateurs.add(utilisateur);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de utilisateurs impossible");
        }

        return listeUtilisateurs;
    }

    /**
     Ajouter un nouveau utilisateur en paramètre dans la base de données
     @params : utilisateur = objet de Utilisateur à insérer dans la base de données
     */
    public void ajouter(Utilisateur utilisateur){
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            
            // Préparation de la requête avec des paramètres
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "INSERT INTO Utilisateur(Login, Password, Name, Mail, isAdmin) VALUES (?, ?, ?, ?, ?)");
            
            // récupération des informations de l'objet utilisateur en paramètre
            preparedStatement.setString(1, utilisateur.getutilisateurLogin());
            preparedStatement.setString(2, utilisateur.getutilisateurPassword());
            preparedStatement.setString(3, utilisateur.getutilisateurName());
            preparedStatement.setString(4, utilisateur.getutilisateurMail());
            preparedStatement.setBoolean(5, utilisateur.isAdmin());

            // Exécution de la requête INSERT pour ajouter l'utilisateur dans la base de données
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout de l'utilisateur impossible");
        }
    }

    /**
     * Permet de chercher et récupérer un objet de Utilisateur dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Utilisateur cherché et retourné
     */
    public Utilisateur chercher(int id) {
        Utilisateur utilisateur = null;
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération des utilisateurs de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("SELECT * FROM Utilisateur WHERE UtilisateurID=" + id);

            // Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            if (resultats.next()) {
                // récupérer les champs de la table Utilisateur dans la base de données
                int utilisateurId = resultats.getInt("UtilisateurID");
                String utilisateurLogin = resultats.getString("Login");
                String utilisateurPassword = resultats.getString("Password");
                String utilisateurName = resultats.getString("Name");
                String utilisateurMail = resultats.getString("Mail");
                Boolean utilisateurIsAdmin = resultats.getBoolean("isAdmin");
                
                // Création de l'objet Utilisateur avec les données récupérées
                utilisateur = new Utilisateur(utilisateurId, utilisateurLogin, utilisateurPassword, utilisateurName, utilisateurMail, utilisateurIsAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Utilisateur introuvable");
        }
        return utilisateur;
    }

    /**
     * Permet de chercher et récupérer un objet de Utilisateur dans la base de données via son login en paramètre
     * @param : login
     * @return : objet de classe Utilisateur cherché et retourné
     */
    public Utilisateur chercher(String search) {
        Utilisateur utilisateur = null;
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            
            // Préparation de la requête avec un paramètre
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "SELECT * FROM Utilisateur WHERE Login = ?");
            preparedStatement.setString(1, search);

            // récupération des utilisateurs de la base de données avec la requete SELECT
            ResultSet resultats = preparedStatement.executeQuery();

            // Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            if (resultats.next()) {
                // récupérer les champs de la table Utilisateur dans la base de données
                int utilisateurId = resultats.getInt("UtilisateurID");
                String utilisateurLogin = resultats.getString("Login");
                String utilisateurPassword = resultats.getString("Password");
                String utilisateurName = resultats.getString("Name");
                String utilisateurMail = resultats.getString("Mail");
                Boolean utilisateurIsAdmin = resultats.getBoolean("isAdmin");
                
                // Création de l'objet Utilisateur avec les données récupérées
                utilisateur = new Utilisateur(utilisateurId, utilisateurLogin, utilisateurPassword, utilisateurName, utilisateurMail, utilisateurIsAdmin);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Recherche de l'utilisateur impossible");
        }

        return utilisateur;
    }

    /**
     * Permet de modifier les données de l'objet de la classe Utilisateur en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : utilisateur = objet en paramètre de la classe Utilisateur à mettre à jour à partir de son id
     * @return : objet utilisateur en paramètre mis à jour dans la base de données à retourner
     */
    public Utilisateur modifier(Utilisateur utilisateur) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            
            // Préparation de la requête avec des paramètres
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "UPDATE Utilisateur SET Login = ?, Password = ?, Name = ?, Mail = ?, isAdmin = ? WHERE UtilisateurID = ?");
            
            preparedStatement.setString(1, utilisateur.getutilisateurLogin());
            preparedStatement.setString(2, utilisateur.getutilisateurPassword());
            preparedStatement.setString(3, utilisateur.getutilisateurName());
            preparedStatement.setString(4, utilisateur.getutilisateurMail());
            preparedStatement.setBoolean(5, utilisateur.isAdmin());
            preparedStatement.setInt(6, utilisateur.getutilisateurId());

            // Exécution de la requête UPDATE pour modifier l'utilisateur dans la base de données
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Modification de l'utilisateur impossible");
        }

        return utilisateur;
    }

    @Override
    /**
     * Supprimer un objet de la classe Utilisateur en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un utilisateur, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du utilisateur supprimé.
     * @params : utilisateur = objet de Utilisateur en paramètre à supprimer de la base de données
     */
    public void supprimer(Utilisateur utilisateur) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            
            // Préparation de la requête avec un paramètre
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "DELETE FROM Utilisateur WHERE UtilisateurID = ?");
            preparedStatement.setInt(1, utilisateur.getutilisateurId());

            // Exécution de la requête DELETE pour supprimer l'utilisateur dans la base de données
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression de l'utilisateur impossible");
        }
    }
}