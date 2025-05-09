package Dao;

// import des packages
import Model.Commande;

import java.sql.*;
import java.util.ArrayList;

/**
 * implémentation MySQL du stockage dans la base de données des méthodes définies dans l'interface
 * commandeDao.
 */
public class CommandeDaoImpl implements CommandeDao {
    // attribut privé pour l'objet du DaoFactoru
    private DaoFactory daoFactory;

    // constructeur dépendant de la classe DaoFactory
    public CommandeDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    /**
     * Récupérer de la base de données tous les objets des commandes dans une liste
     * @return : liste retournée des objets des commandes récupérés
     */
    public ArrayList<Commande> getAll() {
        ArrayList<Commande> listeCommandes = new ArrayList<Commande>();

        /*
            Récupérer la liste des commandes de la base de données dans listeCommandes
        */
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des commandes de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from Commande");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int commandeId= resultats.getInt(1);
                int utilisateurId = resultats.getInt(2);
                double prixTotal = resultats.getDouble(3);
                String statutCommande = resultats.getString(4);



                //instancier un objet de Produit avec ces 3 champs en paramètres
                Commande commande = new Commande(commandeId,utilisateurId,prixTotal,statutCommande);
                // ajouter ce produit à listeProduits
                listeCommandes.add(commande);
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
    public void ajouter(Commande commande){
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            Statement statement = connexion.createStatement();

            // récupération du nom et prix de l'objet commande en paramètre
            int utilisateurId = commande.getUtilisateurId();
            double prixTotal = commande.getPrixTotal();
            String statutCommande = commande.getStatutCommande();

            // Exécution de la requête INSERT pour ajouter le commande dans la base de données
            statement.executeUpdate("INSERT INTO Commande (commandeID, utilisateurID, prixTotal, statutCommande) VALUES ('"+ utilisateurId +"', '"+ prixTotal +"', '"+ statutCommande +"')");
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
    public Commande chercher(int id)  {

        Commande commande = null;
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des commandes de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from Commande where commandeID = " + id);

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            if (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int commandeId= resultats.getInt(1);
                int utilisateurId = resultats.getInt(2);
                double prixTotal = resultats.getDouble(3);
                String statutCommande = resultats.getString(4);

                //instancier un objet de Produit avec ces 3 champs en paramètres
                commande = new Commande(commandeId,utilisateurId,prixTotal,statutCommande);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de commandes impossible");
        }

        return commande;
    }




    /**
     * Permet de modifier les données de l'objet de la classe Commande en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : commande = objet en paramètre de la classe Commande à mettre à jour à partir de son id
     * @return : objet commande en paramètre mis à jour  dans la base de données à retourner
     */
    public Commande modifier(Commande commande) {

        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête UPDATE pour modifier le commande dans la base de données
            statement.executeUpdate("UPDATE Commande SET utilisateurID='"+ commande.getUtilisateurId() +"', prixTotal='"+ commande.getPrixTotal() +"', statutCommande='"+ commande.getStatutCommande() +"' WHERE commandeID="+ commande.getCommandeId());

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Modification du commande impossible");
        }
        return commande;
    }

    @Override
    /**
     * Supprimer un objet de la classe Commande en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un commande, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du commande supprimé.
     * @params : commande = objet de Commande en paramètre à supprimer de la base de données
     */
    public void supprimer (int commandeId) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête DELETE pour supprimer le commande dans la base de données
            statement.executeUpdate("DELETE FROM Commande WHERE commandeID="+ commandeId);

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du commande impossible");
        }

    }

    @Override
/**
 * Récupère toutes les commandes d'un utilisateur spécifique
 * @param utilisateurId ID de l'utilisateur dont on veut récupérer les commandes
 * @return Liste des commandes de l'utilisateur
 */
public ArrayList<Commande> getCommandesUtilisateur(int utilisateurId) {
    //Liste vide pour stocker les commandes de l'utilisateur
    ArrayList<Commande> listeCommandes = new ArrayList<Commande>();

    try {
        // connexion
        Connection connexion = daoFactory.getConnection();
        
        //Selectionner toutes les commandes de l' utilisateur 
        //et les trier par ordre decroissant pour avoir les commandes les plus récentes en premier
        PreparedStatement preparedStatement = connexion.prepareStatement(
            "SELECT * FROM Commande WHERE UtilisateurID = ? ORDER BY CommandeID DESC");
        preparedStatement.setInt(1, utilisateurId);

        
        ResultSet resultats = preparedStatement.executeQuery();

        //on parcours les resultats ligne par ligne
        while (resultats.next()) {
            // récupérer les champs de la table Commande dans la base de données
            int commandeId = resultats.getInt("CommandeID");
            int utilisId = resultats.getInt("UtilisateurID");
            double prixTotal = resultats.getDouble("PrixTotal");
            String statutCommande = resultats.getString("StatutCommande");

            //nouvel objet de Commande avec ces champs en paramètres
            Commande commande = new Commande(commandeId, utilisId, prixTotal, statutCommande);
            
            //ajouter cette commande à listeCommandes
            listeCommandes.add(commande);
        }
    }
    catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Récupération des commandes de l'utilisateur impossible");
    }

    return listeCommandes;
}
}
