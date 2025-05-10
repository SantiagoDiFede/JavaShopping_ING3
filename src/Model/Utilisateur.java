package Model;


public class Utilisateur {
    private int utilisateurId;
    private String utilisateurLogin;
    private String utilisateurPassword;
    private String utilisateurName;
    private String utilisateurMail;
    private Boolean isAdmin = false;

    // constructeur
    public Utilisateur(String utilisateurLogin, String utilisateurPassword, String utilisateurName, String utilisateurMail, Boolean isAdmin) {
        this.utilisateurLogin = utilisateurLogin;
        this.utilisateurPassword = utilisateurPassword;
        this.utilisateurName = utilisateurName;
        this.utilisateurMail = utilisateurMail;
        this.isAdmin = isAdmin;
    }

    public Utilisateur(int utilisateurId, String utilisateurLogin, String utilisateurPassword, String utilisateurName, String utilisateurMail, Boolean isAdmin) {
        this.utilisateurId = utilisateurId;
        this.utilisateurLogin = utilisateurLogin;
        this.utilisateurPassword = utilisateurPassword;
        this.utilisateurName = utilisateurName;
        this.utilisateurMail = utilisateurMail;
        this.isAdmin = isAdmin;
    }


    // getters
    public int getutilisateurId() { return utilisateurId; }
    public String getutilisateurLogin() { return utilisateurLogin; }
    public String getutilisateurPassword() { return utilisateurPassword; }
    public String getutilisateurName() { return utilisateurName; }
    public String getutilisateurMail() { return utilisateurMail; }
    public Boolean isAdmin() { return isAdmin; }

    // setters
    public void setutilisateurPassword(String utilisateurPassword) { this.utilisateurPassword = utilisateurPassword; }
    public void setutilisateurName(String utilisateurName) { this.utilisateurName = utilisateurName; }
    public void setutilisateurMail(String utilisateurMail) { this.utilisateurMail = utilisateurMail; }
    public void setAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }




}

