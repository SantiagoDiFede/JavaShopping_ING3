package Dao;

// import des packages
import Model.Produit;

import java.sql.*;
import java.util.ArrayList;

/**
 * implémentation MySQL du stockage dans la base de données des méthodes définies dans l'interface
 * produitDao.
 */
public class ProduitDaoImpl implements ProduitDao {
    // attribut privé pour l'objet du DaoFactoru
    private DaoFactory daoFactory;

    // constructeur dépendant de la classe DaoFactory
    public ProduitDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    /**
     * Récupérer de la base de données tous les objets des produits dans une liste
     * @return : liste retournée des objets des produits récupérés
     */
    public ArrayList<Produit> getAll() {
        ArrayList<Produit> listeProduits = new ArrayList<Produit>();

        /*
            Récupérer la liste des produits de la base de données dans listeProduits
        */
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération des produits de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from Produit");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int produitId= resultats.getInt(1);
                String produitNom = resultats.getString(2);
                double produitPrix = resultats.getDouble(3);
                double produitPrixReduction = resultats.getDouble(4);
                int produitQteReduction = resultats.getInt(5);
                String produitImage = resultats.getString(6);


                //instancier un objet de Produit avec ces 3 champs en paramètres
                Produit produit = new Produit(produitId,produitNom,produitPrix,produitPrixReduction,produitQteReduction,produitImage);

                // ajouter ce produit à listeProduits
                listeProduits.add(produit);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de produits impossible");
        }

        return listeProduits;
    }

    /**
     Ajouter un nouveau produit en paramètre dans la base de données
     @param : produit = objet de Produit à insérer dans la base de données
     */
    public void ajouter(Produit produit){
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération du nom et prix de l'objet product en paramètre
            String produitNom = produit.getNom();
            double produitPrix = produit.getPrix();
            double produitPrixReduction = produit.getPrixReduction();
            int produitQteReduction = produit.getQteReduction();
            String produitImage = produit.getImage();

            // Exécution de la requête INSERT pour ajouter le produit dans la base de données
            statement.executeUpdate("INSERT INTO Produit (Nom, Prix, PrixReduction,QteReduction, Image) VALUES ('"+produitNom+"', '"+produitPrix+"', '"+produitPrixReduction+"', '"+produitQteReduction+"', '"+produitImage+"')");

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout du produit impossible");
        }

    }

    /**
     * Permet de chercher et récupérer un objet de Produit dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Produit cherché et retourné
     */
    public Produit chercher(int id) {

        Produit produit = null;
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération des produits de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from Produit where produitID=" + id);

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            if (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int produitId = resultats.getInt(1);
                String produitNom = resultats.getString(2);
                double produitPrix = resultats.getDouble(3);
                double produitPrixReduction = resultats.getDouble(4);
                int produitQteReduction = resultats.getInt(5);
                String produitImage = resultats.getString(6);
                //instancier un objet de Produit avec ces 3 champs en paramètres
                produit = new Produit(produitId, produitNom, produitPrix, produitPrixReduction, produitQteReduction, produitImage);
            }
        } catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Produit non trouvé");
        }
        return produit;
    }




    /**
     * Permet de modifier les données de l'objet de la classe Produit en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : produit = objet en paramètre de la classe Produit à mettre à jour à partir de son id
     * @return : objet produit en paramètre mis à jour  dans la base de données à retourner
     */
    public Produit modifier(Produit produit) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération du nom et prix de l'objet produit en paramètre
            String produitNom = produit.getNom();
            double produitPrix = produit.getPrix();
            double produitPrixReduction = produit.getPrixReduction();
            int produitQteReduction = produit.getQteReduction();
            String produitImage = produit.getImage();

            // Exécution de la requête UPDATE pour modifier le produit dans la base de données
            statement.executeUpdate("UPDATE Produit SET Nom='"+produitNom+"', Prix='"+produitPrix+"', PrixReduction='"+produitPrixReduction+"', QteReduction='"+produitQteReduction+"', Image='"+produitImage+"' WHERE ProduitID="+produit.getProduitId());

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Modification du produit impossible");
        }
        return produit;
    }

    @Override
    /**
     * Supprimer un objet de la classe Produit en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un produit, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du produit supprimé.
     * @params : produit = objet de Produit en paramètre à supprimer de la base de données
     */
    public void supprimer (int produitId) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // Exécution de la requête DELETE pour supprimer le produit dans la base de données
            statement.executeUpdate("DELETE FROM Produit WHERE produitId="+ produitId);

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du produit impossible");
        }

    }
}
