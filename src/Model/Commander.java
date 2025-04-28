package Model;

public class Commander {
    private int clientId;
    private int produitId;
    private int quantite;

    // constructeur
    public Commander (int clientId, int produitId, int quantite) {
        this.clientId = clientId;
        this.produitId = produitId;
        this.quantite = quantite;
    }

    // getters
    public int getClientId() { return clientId; }
    public int getProduitId() { return produitId; }
    public int getQuantite() { return quantite; }
}

