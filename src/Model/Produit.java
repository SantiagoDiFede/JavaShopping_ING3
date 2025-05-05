package Model;

public class Produit {
    int ProduitId;
    String Nom;
    double Prix;
    double PrixReduction;
    int  QteReduction;
    String Image;

    public Produit(int produitId, String nom, double prix, double prixReduction,int QteReduction, String image) {
        ProduitId = produitId;
        Nom = nom;
        Prix = prix;
        PrixReduction = prixReduction;
        this.QteReduction = QteReduction;
        Image = image;
    }

    public Produit(String nom, double prix, double prixReduction,int qteReduction,  String image) {
        Nom = nom;
        Prix = prix;
        PrixReduction = prixReduction;
        this.QteReduction = qteReduction;
        Image = image;
    }

    // getters
    public int getProduitId() { return ProduitId; }
    public String getNom() { return Nom; }
    public double getPrix() { return Prix; }
    public double getPrixReduction() { return PrixReduction; }
    public int getQteReduction() { return QteReduction; }
    public String getImage() { return Image; }


}
