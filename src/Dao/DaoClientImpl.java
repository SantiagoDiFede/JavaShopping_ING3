package Dao;

// import des packages
import Model.Client;
import java.sql.*;
import java.util.ArrayList;

/**
 * implémentation MySQL du stockage dans la base de données des méthodes définies dans l'interface
 * ClientDao.
 */
public class DaoClientImpl implements DaoClient {
    // attribut privé pour l'objet du DaoFactoru
    private DaoFactory daoFactory;

    // constructeur dépendant de la classe DaoFactory
    public DaoClientImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    /**
     * Récupérer de la base de données tous les objets des clients dans une liste
     * @return : liste retournée des objets des clients récupérés
     */
    public ArrayList<Client> getAll() {
        ArrayList<Client> listeClients = new ArrayList<Client>();

        /*
            Récupérer la liste des clients de la base de données dans listeClients
        */
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération des clients de la base de données avec la requete SELECT
            ResultSet resultats = statement.executeQuery("select * from clients");

            // 	Se déplacer sur le prochain enregistrement : retourne false si la fin est atteinte
            while (resultats.next()) {
                // récupérer les 3 champs de la table produits dans la base de données
                int clientId= resultats.getInt(1);
                String clientLogin = resultats.getString(2);
                String clientPassword = resultats.getString(2);
                String clientFirstName = resultats.getString(2);
                String clientLastName = resultats.getString(2);
                String clientMail = resultats.getString(3);


                //instancier un objet de Produit avec ces 3 champs en paramètres
                Client client = new Client(clientId,clientLogin,clientPassword,clientFirstName,clientLastName,clientMail);

                // ajouter ce produit à listeProduits
                listeClients.add(client);
            }
        }
        catch (SQLException e) {
            //traitement de l'exception
            e.printStackTrace();
            System.out.println("Création de la liste de clients impossible");
        }

        return listeClients;
    }

    /**
     Ajouter un nouveau client en paramètre dans la base de données
     @params : client = objet de Client à insérer dans la base de données
     */
    public void ajouter(Client client){
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // récupération du nom et prix de l'objet product en paramètre
            String clientLogin = client.getClientLogin();
            String clientPassword = client.getClientPassword();
            String clientFirstName = client.getClientFirstName();
            String clientLastName = client.getClientLastName();
            String clientMail = client.getClientMail();

            // Exécution de la requête INSERT pour ajouter le client dans la base de données
            statement.executeUpdate("INSERT INTO clients (clientLogin, clientPassword, clientFirstName, clientLastName, clientMail) VALUES ('"+clientLogin+"', '"+clientPassword+"', '"+clientFirstName+"', '"+clientLastName+"', '"+clientMail+"')");

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ajout du client impossible");
        }

    }

    /**
     * Permet de chercher et récupérer un objet de Client dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Client cherché et retourné
     */
    public Client chercher(int id)  {
        return null;
    }

    /**
     * Permet de chercher et récupérer un objet de Client dans la base de données via un string en paramètre
     * @param : nom
     * @return : objet de classe Client cherché et retourné
     */
    public Client chercher(String search)  {
        return null;
    }


    /**
     * Permet de modifier les données de l'objet de la classe Client en paramètre
     * dans la base de données à partir de cet objet en paramètre
     * @param : client = objet en paramètre de la classe Client à mettre à jour à partir de son id
     * @return : objet client en paramètre mis à jour  dans la base de données à retourner
     */
    public Client modifier(Client client) {

        return client;
    }

    @Override
    /**
     * Supprimer un objet de la classe Client en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un client, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du client supprimé.
     * @params : client = objet de Client en paramètre à supprimer de la base de données
     */
    public void supprimer (Client client) {
        try {
            // connexion
            Connection connexion = daoFactory.getConnection();;
            Statement statement = connexion.createStatement();

            // Exécution de la requête DELETE pour supprimer le client dans la base de données
            statement.executeUpdate("DELETE FROM clients WHERE clientID="+client.getClientId());

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Suppression du client impossible");
        }

    }
}


