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
            Connection connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "SELECT * FROM CommandeLigne WHERE CommandeID = ?");
            preparedStatement.setInt(1, commandeID);

            // récupération des commandes de la base de données avec la requete SELECT
            ResultSet resultats = preparedStatement.executeQuery();

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int commandeLigneId= resultats.getInt("CommandeLigneID");
                int commandeId = resultats.getInt("CommandeID");
                int produitID = resultats.getInt("ProduitID");
                int Qte = resultats.getInt("Qte");



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

            int CommandeId = commandeLigne.getCommandeId();
            int ProduitId = commandeLigne.getProduitId();
            int Qte = commandeLigne.getQte();

            statement.executeUpdate("INSERT INTO CommandeLigne (CommandeID, ProduitID, Qte) VALUES ('" + CommandeId + "', '" + ProduitId + "', '" + Qte + "')");


        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout de la commande impossible");
        }

    }

    /**
     * Permet de chercher et récupérer un objet de Commande dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Commande cherché et retourné
     */
    public CommandeLigne chercher(int id)  {
        CommandeLigne commandeLigne = null;
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "SELECT * FROM CommandeLigne WHERE CommandeLigneID = ?");
            preparedStatement.setInt(1, id);

            // récupération des commandes de la base de données avec la requete SELECT
            ResultSet resultats = preparedStatement.executeQuery();

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            if (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int commandeLigneId = resultats.getInt("CommandeLigneID");
                int commandeId = resultats.getInt("CommandeID");
                int produitID = resultats.getInt("ProduitID");
                int Qte = resultats.getInt("Qte");

                //instancier un objet de Produit avec ces 3 champs en paramètres
                commandeLigne = new CommandeLigne(commandeLigneId,commandeId,produitID,Qte);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de commandes impossible");
        }

        return commandeLigne;
    }




    /**
     * Permet de modifier les données de l'objet de la classe Commande en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : commande = objet en paramètre de la classe Commande à mettre à jour à partir de son id
     * @return : objet commande en paramètre mis à jour  dans la base de données à retourner
     */
    public CommandeLigne modifier(CommandeLigne commandeLigne) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "UPDATE CommandeLigne SET Qte = ? WHERE CommandeLigneID = ?");

            preparedStatement.setInt(1, commandeLigne.getQte());
            preparedStatement.setInt(2, commandeLigne.getCommandeLigneId());

            // Exécution de la requête UPDATE pour modifier le utilisateur dans la base de données
            preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Modification du produit impossible");
        }
        return commandeLigne;
    }

    /**
     * Mettre à jour la quantité d'une commande ligne
     * @param commandeLigneId ID de la commande ligne à mettre à jour
     * @param qte Nouvelle quantité
     */
    public void updateQte(int commandeLigneId, int qte) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(
                    "UPDATE CommandeLigne SET Qte = ? WHERE CommandeLigneID = ?");

            preparedStatement.setInt(1, qte);
            preparedStatement.setInt(2, commandeLigneId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Modification de la quantité de la commande ligne impossible");
        }
    }
    @Override
    /**
     * Supprimer un objet de la classe Commande en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un commande, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du commande supprimé.
     * @params : commande = objet de Commande en paramètre à supprimer de la base de données
     */
    public void supprimer (int commandeLigneId) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "DELETE FROM CommandeLigne WHERE CommandeLigneID = ?");
             
            preparedStatement.setInt(1, commandeLigneId);    

            // Exécution de la requête DELETE pour supprimer le commande dans la base de données
            preparedStatement.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du commande impossible");
        }

    }

    /**
     * Récupère le total des ventes d'un produit spécifique
     * @param produitId ID du produit dont on veut récupérer le total des ventes
     * @return Total des ventes du produit
     */
    public int ventesTotal(int produitId) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();
            PreparedStatement preparedStatement = connexion.prepareStatement(
                "SELECT SUM(Qte) FROM CommandeLigne WHERE ProduitID = ?");
            preparedStatement.setInt(1, produitId);

            // Exécution de la requête SELECT pour récupérer le total des ventes
            ResultSet resultats = preparedStatement.executeQuery();
            if (resultats.next()) {
                return resultats.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération du total des ventes");
        }
        return 0;
    }
}
