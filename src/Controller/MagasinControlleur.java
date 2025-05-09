package Controller;
import Dao.DaoFactory;
import Dao.ProduitDaoImpl;
import Dao.UtilisateurDaoImpl;
import Model.Produit;
import Model.Utilisateur;

public class MagasinControlleur {

    DaoFactory daoFactory;

    ProduitDaoImpl produitDao;
    UtilisateurDaoImpl utilisateurDao;
    Utilisateur utilisateurConnecte;

    /**
     * Constructeur du contrôleur de magasin
     * @param daoFactory Factory pour accéder aux DAO
     */
    public MagasinControlleur(DaoFactory daoFactory,Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.produitDao = new ProduitDaoImpl(daoFactory);
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        this.utilisateurConnecte = utilisateur;
    }


    public void GetInventaire() {
        produitDao.getAll();
    }

    public void SupprimerProduit(int produitId, int utilisateurId){
        // Vérifier si l'utilisateur est un administrateur
        Utilisateur user = utilisateurDao.chercher(utilisateurId);
        if (user == null || !user.isAdmin()) {
            throw new IllegalArgumentException("L'utilisateur n'est pas un administrateur.");
        }
        produitDao.supprimer(produitId);
    }

    public void AjouterProduit(Produit produit) {
        produitDao.ajouter(produit);
    }

    public void ModifierProduit(Produit produit) {
        produitDao.modifier(produit);
    }
}
