package Model;


public class Client {
    private int clientId;
    private String clientLogin;
    private String clientPassword;
    private String clientFirstName;
    private String clientLastName;
    private String clientMail;

    // constructeur
    public Client(int clientId, String clientLogin, String clientPassword, String clientFirstName, String clientLastName, String clientMail) {
        this.clientId = clientId;
        this.clientLogin = clientLogin;
        this.clientPassword = clientPassword;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientMail = clientMail;
    }


    // getters
    public int getClientId() { return clientId; }
    public String getClientLogin() { return clientLogin; }
    public String getClientPassword() { return clientPassword; }
    public String getClientFirstName() { return clientFirstName; }
    public String getClientLastName() { return clientLastName; }
    public String getClientMail() { return clientMail; }


}

