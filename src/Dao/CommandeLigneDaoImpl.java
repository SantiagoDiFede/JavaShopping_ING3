package Dao;

// import des packages
import Model.CommandeLigne;

import java.sql.*;
import java.util.ArrayList;

/**
 * implémentation MySQL du stockage dans la base de données des méthodes définies dans l'interface
 * commandeDao.
 */
public class CommandeLigneDaoImpl implements CommandeLigneDao {
    // attribut privé pour l'objet du DaoFactoru
    private DaoFactory daoFactory;

    // constructeur dépendant de la classe DaoFactory
    public CommandeLigneDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    /**
     * Récupérer de la base de données tous les objets des commandes dans une liste
     * @return : liste retournée des objets des commandes récupérés
     */
    public ArrayList<CommandeLigne> getAllFromCommande(int commandeID) {
        ArrayList<CommandeLigne> listeCommandes = new ArrayList<CommandeLigne>();

        /*
            Récupérer la liste des commandes de la base de données dans listeCommandes
        */
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des commandes de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from CommandeLigne where commandeID = " + commandeID);

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int commandeLigneId= resultats.getInt(1);
                int commandeId = resultats.getInt(2);
                int produitID = resultats.getInt(3);
                int Qte = resultats.getInt(4);



                //instancier un objet de Produit avec ces 3 champs en paramètres
                CommandeLigne commandeLigne = new CommandeLigne(commandeLigneId,commandeId,produitID,Qte);
                // ajouter ce produit à listeProduits
                listeCommandes.add(commandeLigne);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de commandes impossible");
        }

        return listeCommandes;
    }

    /**
     Ajouter un nouveau commande en paramètre dans la base de données
     @params : commande = objet de Commande à insérer dans la base de données
     */
    public void ajouter(CommandeLigne commandeLigne){
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération du nom et prix de l'objet commande en paramètre
            int commandeId = commandeLigne.getCommandeId();
            int produitId = commandeLigne.getProduitId();
            int Qte = commandeLigne.getQte();

            // Exécution de la requête INSERT pour ajouter le commande dans la base de données
            statement.executeUpdate("INSERT INTO CommandeLigne (commandeLigneID, commandeID, produitID, Qte) VALUES ('"+ commandeId +"', '"+ produitId +"', '"+ Qte +"')");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout du commande impossible");
        }

    }

    /**
     * Permet de chercher et récupérer un objet de Commande dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Commande cherché et retourné
     */
    public CommandeLigne chercher(int id)  {
        return null;
    }

    /**
     * Permet de chercher et récupérer un objet de Commande dans la base de données via un string en paramètre
     * @param : nom
     * @return : objet de classe Commande cherché et retourné
     */
    public CommandeLigne chercher(String search)  {
        return null;
    }


    /**
     * Permet de modifier les données de l'objet de la classe Commande en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : commande = objet en paramètre de la classe Commande à mettre à jour à partir de son id
     * @return : objet commande en paramètre mis à jour  dans la base de données à retourner
     */
    public CommandeLigne modifier(CommandeLigne commandeLigne) {

        return commandeLigne;
    }

    @Override
    /**
     * Supprimer un objet de la classe Commande en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un commande, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du commande supprimé.
     * @params : commande = objet de Commande en paramètre à supprimer de la base de données
     */
    public void supprimer (CommandeLigne commandeLigne) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête DELETE pour supprimer le commande dans la base de données
            statement.executeUpdate("DELETE FROM CommandeLigne WHERE commandeID="+ commandeLigne.getCommandeLigneId());

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du commande impossible");
        }

    }
}
