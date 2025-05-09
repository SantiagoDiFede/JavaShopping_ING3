package Controller;
import Dao.CommandeDao;
import Dao.CommandeDaoImpl;
import Dao.CommandeLigneDao;
import Dao.CommandeLigneDaoImpl;
import Dao.DaoFactory;
import Dao.ProduitDao;
import Dao.ProduitDaoImpl;
import Dao.UtilisateurDaoImpl;
import Model.Commande;
import Model.CommandeLigne;
import Model.Produit;
import Model.Utilisateur;
import java.util.ArrayList;
import java.util.Scanner;

public class CompteControlleur {
    
    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    private Utilisateur utilisateurConnecte;
    private CommandeDao commandeDao;
    private CommandeLigneDao commandeLigneDao;
    private ProduitDao produitDao;
    
    /**
     * 
     * Constructeur du contrôleur client
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     * 
     */
    public CompteControlleur(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        this.commandeLigneDao = new CommandeLigneDaoImpl(daoFactory);
        this.produitDao = new ProduitDaoImpl(daoFactory);
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
    /**
 * Récupère et affiche l'historique des commandes de l'utilisateur connecté
 * @return Liste des commandes de l'utilisateur ou null en cas d'erreur
 */
public ArrayList<Commande> GetHistorique() {
    try {
        //Vérifier que l'utilisateur est connecté
        if (utilisateurConnecte == null) {
            System.out.println("Vous devez être connecté pour consulter votre historique.");
            return null;
        }
        
        //Récupérer les commandes de l'utilisateur
        ArrayList<Commande> commandes = commandeDao.getCommandesUtilisateur(utilisateurConnecte.getutilisateurId());
        
        //Afficher les commandes
        if (commandes.isEmpty()) {// si l'utilisateur n'a pas de commandes
            System.out.println("Vous n'avez pas encore passé de commande.");
        } else {
            System.out.println("\n=== HISTORIQUE DES COMMANDES ===");
            
            // Pour chaque commande, afficher le numéro de commande, le statut et le prix total
            for (Commande commande : commandes) {
                System.out.println("\n------------------------------------------");
                System.out.println("Commande #" + commande.getCommandeId());
                System.out.println("Statut: " + commande.getStatutCommande());
                System.out.println("Prix total: " + commande.getPrixTotal() + " Euros");
                
                // Pour chaque commande récupérer les lignes de cette commande
                ArrayList<CommandeLigne> lignes = commandeLigneDao.getAllFromCommande(commande.getCommandeId());
                
                if (!lignes.isEmpty()) {
                    System.out.println("\nDétails de la commande:");
                    System.out.println("----------------------");
                    
                    //Pour chaque ligne de commande
                    for (CommandeLigne ligne : lignes) {
                        //Récupérer les informations du produit pour avoir la quantité, le prix, le nom
                        Produit produit = produitDao.chercher(ligne.getProduitId());
                        
                        if (produit != null) {
                            System.out.println(ligne.getQte() + "x " + produit.getNom() + 
                                              " - Prix unitaire: " + produit.getPrix() + " Euros" +
                                              " - Total: " + (produit.getPrix() * ligne.getQte()) + " Euros");
                        } else {//Si le produit n'existe pas
                            System.out.println(ligne.getQte() + "x Produit inconnu (ID: " + ligne.getProduitId() + ")");
                        }
                    }
                } else {
                    System.out.println("\nAucun détail disponible pour cette commande.");
                }
            }
            System.out.println("\n------------------------------------------");
        }
        
        return commandes;
        
    } catch (Exception e) {
        System.out.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    
}