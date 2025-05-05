package Dao;

// import des packages
import Model.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * On utilise une interface utilisateurDao pour définir les méthodes d'accès aux données de la table utilisateurs,
 * indépendamment de la méthode de stockage. On indique juste des noms de méthodes ici.
 */
public interface UtilisateurDao {
    /**
     * Récupérer de la base de données tous les objets des utilisateurs dans une liste
     * @return : liste retournée des objets des utilisateurs récupérés
     */
    public ArrayList<Utilisateur> getAll();

    /**
     Ajouter un nouveau utilisateur en paramètre dans la base de données
     @params : utilisateur = objet de Utilisateur à insérer dans la base de données
     */
    public void ajouter(Utilisateur utilisateur) ;

    /**
     * Permet de chercher et récupérer un objet de Utilisateur dans la base de données via son id en paramètre
     * @param : id
     * @return : objet de classe Utilisateur cherché et retourné
     */
    public Utilisateur chercher(int id) throws SQLException;

    /**
     * Permet de modifier les données du nom de l'objet de la classe Utilisateur en paramètre
     * dans la base de données à partir de l'id de cet objet en paramètre
     * @param : utilisateur = objet en paramètre de la classe Utilisateur à mettre à jour
     * @return : objet utilisateur en paramètre mis à jour  dans la base de données à retourner
     */
    public Utilisateur modifier(Utilisateur utilisateur);

    /**
     * Supprimer un objet de la classe Utilisateur en paramètre dans la base de données en respectant la contrainte
     * d'intégrité référentielle : en supprimant un utilisateur, supprimer aussi en cascade toutes les commandes de la
     * table commander qui ont l'id du utilisateur supprimé.
     * @params : utilisateur = objet de Utilisateur en paramètre à supprimer de la base de données
     */
    public void supprimer (Utilisateur utilisateur);

}