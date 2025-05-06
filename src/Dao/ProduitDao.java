package Dao;

// import des packages
import Model.Produit;
import java.util.ArrayList;

/**
 * On utilise une interface ProduitDao pour définir les méthodes d'accès aux données de la table utilisateurs,
 * indépendamment de la méthode de stockage. On indique juste des noms de méthodes ici.
 */
public interface ProduitDao {
    /**
     * Récupérer de la base de données tous les objets des utilisateurs dans une liste
     * @return : liste retournée des objets des utilisateurs récupérés
     */
    public ArrayList<Produit> getAll();

    /**
     Ajouter un nouveau produit en paramètre dans la base de données
     @params : produit = objet de Produit à insérer dans la base de données
     */
    public void ajouter(Produit produit) ;

    /**
     * Permet de chercher et récupérer un objet de Produit dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Produit cherché et retourné
     */
    public Produit chercher(int id);

    /**
     * Permet de modifier les données du nom de l'objet de la classe Produit en paramètre
     * dans la base de données à partir de l'id de cet objet en paramètre
     * @param : produit = objet en paramètre de la classe Produit à mettre à jour
     * @return : objet produit en paramètre mis à jour  dans la base de données à retourner
     */
    public Produit modifier(Produit produit);

    /**
     * Supprimer un objet de la classe Produit en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un produit, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du produit supprimé.
     * @params : produit = objet de Produit en paramètre à supprimer de la base de données
     */
    public void supprimer (int produitId);

}