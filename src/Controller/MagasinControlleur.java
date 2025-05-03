package Controller;
import Dao.DaoFactory;
import Dao.ProduitDaoImpl;
import Model.Produit;

public class MagasinControlleur {

    DaoFactory daoFactory;

    ProduitDaoImpl produitDao = new ProduitDaoImpl(daoFactory);

    public void GetInventaire() {
        produitDao.getAll();
    }

    public void SupprimerProduit(int id) {
        Produit produit = produitDao.chercher(id);
        produitDao.supprimer(produit);
    }

    public void AjouterProduit(Produit produit) {
        produitDao.ajouter(produit);
    }

    public void ModifierProduit(Produit produit) {
        produitDao.modifier(produit);
    }
}
