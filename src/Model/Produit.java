package Model;

public class Produit {
    int ProduitId;
    String Nom;
    double Prix;
    double PrixReduction;
    String Image;

    public Produit(int produitId, String nom, double prix, double prixReduction, String image) {
        ProduitId = produitId;
        Nom = nom;
        Prix = prix;
        PrixReduction = prixReduction;
        Image = image;
    }

    public Produit(String nom, double prix, double prixReduction, String image) {
        Nom = nom;
        Prix = prix;
        PrixReduction = prixReduction;
        Image = image;
    }

    // getters
    public int getProduitId() { return ProduitId; }
    public String getNom() { return Nom; }
    public double getPrix() { return Prix; }
    public double getPrixReduction() { return PrixReduction; }
    public String getImage() { return Image; }


}
