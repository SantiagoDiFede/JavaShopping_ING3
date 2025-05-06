package Dao;

// import des packages
import Model.Commande;
import java.util.ArrayList;


public interface CommandeDao {
    /**
     * Récupérer de la base de données tous les objets des commandes dans une liste
     * @return : liste retournée des objets des commandes récupérés
     */
    public ArrayList<Commande> getAll();

    /**
     Ajouter une nouvelle commande en paramètre dans la base de données
     @params : commande = objet de Commande à insérer dans la base de données
     */
    public void ajouter(Commande commande) ;

    /**
     * Permet de chercher et récupérer un objet de Utilisateur dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Commande cherché et retourné
     */
    public Commande chercher(int id);

    /**
     * Permet de modifier les données du nom de l'objet de la classe Commande en paramètre
     * dans la base de données à partir de l'id de cet objet en paramètre
     * @param : commande = objet en paramètre de la classe Commande à mettre à jour
     * @return : objet commande en paramètre mis à jour  dans la base de données à retourner
     */
    public Commande modifier(Commande commande);

    /**
     * Supprimer un objet de la classe Commande en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un commande, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id de commande supprimé.
     * @params : commande = objet de Commande en paramètre à supprimer de la base de données
     */
    public void supprimer (int commandeId);

}

