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
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des utilisateurs de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from Utilisateur");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int utilisateurId= resultats.getInt(1);
                String utilisateurLogin = resultats.getString(2);
                String utilisateurPassword = resultats.getString(2);
                String utilisateurName = resultats.getString(2);
                String utilisateurMail = resultats.getString(3);
                Boolean utilisateurIsAdmin = resultats.getBoolean(4);


                //instancier un objet de Produit avec ces 3 champs en paramètres
                Utilisateur utilisateur = new Utilisateur(utilisateurId,utilisateurLogin,utilisateurPassword,utilisateurName,utilisateurMail,utilisateurIsAdmin);

                // ajouter ce produit à listeProduits
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
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération du nom et prix de l'objet product en paramètre
            String utilisateurLogin = utilisateur.getutilisateurLogin();
            String utilisateurPassword = utilisateur.getutilisateurPassword();
            String utilisateurName = utilisateur.getutilisateurName();
            String utilisateurMail = utilisateur.getutilisateurMail();
            Boolean isAdmin = utilisateur.getIsAdmin();

            // Exécution de la requête INSERT pour ajouter le utilisateur dans la base de données
            statement.executeUpdate("INSERT INTO utilisateurs (utilisateurLogin, utilisateurPassword, utilisateurName, utilisateurMail,isAdmin) VALUES ('"+utilisateurLogin+"', '"+utilisateurPassword+"', '"+utilisateurName+"', '"+utilisateurMail+"','"+isAdmin+"')");

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout du utilisateur impossible");
        }

    }

    /**
     * Permet de chercher et récupérer un objet de Utilisateur dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Utilisateur cherché et retourné
     */
    public Utilisateur chercher(int id)  {
        return null;
    }

    /**
     * Permet de chercher et récupérer un objet de Utilisateur dans la base de données via un string en paramètre
     * @param : nom
     * @return : objet de classe Utilisateur cherché et retourné
     */
    public Utilisateur chercher(String search)  {
        return null;
    }


    /**
     * Permet de modifier les données de l'objet de la classe Utilisateur en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : utilisateur = objet en paramètre de la classe Utilisateur à mettre à jour à partir de son id
     * @return : objet utilisateur en paramètre mis à jour  dans la base de données à retourner
     */
    public Utilisateur modifier(Utilisateur utilisateur) {

        return utilisateur;
    }

    @Override
    /**
     * Supprimer un objet de la classe Utilisateur en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un utilisateur, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du utilisateur supprimé.
     * @params : utilisateur = objet de Utilisateur en paramètre à supprimer de la base de données
     */
    public void supprimer (Utilisateur utilisateur) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête DELETE pour supprimer le utilisateur dans la base de données
            statement.executeUpdate("DELETE FROM utilisateurs WHERE utilisateurID="+ utilisateur.getutilisateurId());

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du utilisateur impossible");
        }

    }
}


