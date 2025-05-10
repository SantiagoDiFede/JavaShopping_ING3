package Model;

public class Commande {

    private int commandeId;
    private int utilisateurId;
    private double prixTotal;

    private String StatutCommande;

    // constructeur
    public Commande(int commandeId, int utilisateurId, double prixTotal, String statutCommande) {
        this.commandeId = commandeId;
        this.utilisateurId = utilisateurId;
        this.prixTotal = prixTotal;
        this.StatutCommande = statutCommande;
    }

    // constructeur sans id
    public Commande(int utilisateurId, double prixTotal, String statutCommande) {
        this.utilisateurId = utilisateurId;
        this.prixTotal = prixTotal;
        this.StatutCommande = statutCommande;
    }

    // getters
    public int getCommandeId() {
        return commandeId;
    }
    public int getUtilisateurId() {
        return utilisateurId;
    }
    public double getPrixTotal() {
        return prixTotal;
    }
    public String getStatutCommande() {return StatutCommande;}

    // setters
    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }
    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }
    public void setStatutCommande(String statutCommande) {
        StatutCommande = statutCommande;
    }
}

