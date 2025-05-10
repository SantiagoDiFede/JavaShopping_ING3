package Model;

public class CommandeLigne {

    int CommandeLigneId;
    int CommandeId;
    int ProduitId;
    int Qte;

    // constructeur
    public CommandeLigne(int commandeLigneId, int commandeId, int produitId, int qte) {
        CommandeLigneId = commandeLigneId;
        CommandeId = commandeId;
        ProduitId = produitId;
        Qte = qte;
    }

    // constructeur sans id
    public CommandeLigne(int commandeId, int produitId, int qte) {
        CommandeId = commandeId;
        ProduitId = produitId;
        Qte = qte;
    }

    // getters
    public int getCommandeLigneId() {
        return CommandeLigneId;
    }
    public int getCommandeId() {
        return CommandeId;
    }
    public int getProduitId() {
        return ProduitId;
    }
    public int getQte() {
        return Qte;
    }

    // setters
    public void setCommandeLigneId(int commandeLigneId) {
        CommandeLigneId = commandeLigneId;
    }
    public void setCommandeId(int commandeId) {
        CommandeId = commandeId;
    }
    public void setProduitId(int produitId) {
        ProduitId = produitId;
    }
    public void setQte(int qte) {
        Qte = qte;
    }
}
