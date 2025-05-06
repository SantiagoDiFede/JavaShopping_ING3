package Controller;
import Dao.DaoFactory;
import Dao.UtilisateurDaoImpl;
import Model.Utilisateur;
import java.util.Scanner;

public class ClientControlleur {
    
    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    private Utilisateur utilisateurConnecte;
    
    /**
     * 
     * Constructeur du contrôleur client
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     * 
     */
    public ClientControlleur(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        
        this.utilisateurConnecte = utilisateur;
    }
    
    /**
     * Permet à un utilisateur de modifier ses informations de compte
     * @return L'utilisateur modifié ou null en cas d'erreur
     */
    public Utilisateur ModifierCompte() {
        Scanner scanner = new Scanner(System.in);
        
        try {
            //Vérifier que l'utilisateur est connecté
            if (utilisateurConnecte == null) {
                System.out.println("Vous devez être connecté pour modifier votre compte.");
                return null;
            }
            
            System.out.println("MODIFICATION DU COMPTE");
            
            //Saisie du nouveau login
            System.out.print("Saisissez votre nouveau login :");
            String login = scanner.nextLine();
            
            //Saisie du nouveau nom
            System.out.print("Saisissez votre nouveau nom :");
            String name = scanner.nextLine();
            
            //Saisie du nouvel email
            System.out.print("Saisissez votre nouvel email :");
            String email = scanner.nextLine();
            
            //Saisie du nouveau mot de passe
            System.out.print("Saisissez votre nouveau mot de passe :");
            String password = scanner.nextLine();
            
            //Créer un nouvel objet Utilisateur avec les nouvelles informations
            Utilisateur utilisateurModifie = new Utilisateur(
                utilisateurConnecte.getutilisateurId(),
                login,
                password,
                name,
                email,
                utilisateurConnecte.isAdmin()
            );
            
            //Modifier l'utilisateur dans la base de données
            utilisateurDao.modifier(utilisateurModifie);
            
            //Mettre à jour l'utilisateur connecté
            this.utilisateurConnecte = utilisateurModifie;
            
            System.out.println("Compte modifié avec succès.");
            return utilisateurModifie;
            
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification du compte: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
}