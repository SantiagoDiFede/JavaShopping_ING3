package Controller;

import Dao.CommandeDaoImpl;
import Dao.DaoFactory;
import Dao.UtilisateurDaoImpl;
import Model.Commande;
import Model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AdminControlleur {
    private DaoFactory daoFactory;
    private UtilisateurDaoImpl utilisateurDao;
    private CommandeDaoImpl commandeDao;
    private Utilisateur utilisateurConnecte;
    private CompteControlleur compteControlleur;
    @FXML
    private VBox commandesContainer;

    @FXML
    private VBox clientsContainer;


    /**
     * Constructeur du contrôleur d'administration
     * @param daoFactory Factory pour accéder aux DAO
     */
    public AdminControlleur(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;


    }

    /**
     * Constructeur par défaut
     */
    public AdminControlleur() {
        // JavaFX a besoin de ce constructeur pour charger le FXML
    }

    /**
     * Méthode pour initialiser les données du contrôleur
     * @param daoFactory Factory pour accéder aux DAO
     * @param utilisateur Utilisateur connecté
     */
    public void initData(DaoFactory daoFactory, Utilisateur utilisateur) {
        this.daoFactory = daoFactory;
        this.utilisateurConnecte = utilisateur;
        this.utilisateurDao = new UtilisateurDaoImpl(daoFactory);
        this.commandeDao = new CommandeDaoImpl(daoFactory);
        List<Commande> commandes = commandeDao.getAll();
        setCommandes(commandes);
        List<Utilisateur> clients = utilisateurDao.getAll();
        setClients(clients);
    }

    public void setCommandes(List<Commande> commandes) {
        commandesContainer.getChildren().clear();

        commandes.sort(Comparator.comparing(Commande::getCommandeId).reversed());
        for (int i = 0; i < commandes.size(); i++) {
            if (Objects.equals(commandes.get(i).getStatutCommande(), "En cours")) {
                commandes.remove(i);
            }
        }
        int commandeNumero = commandes.size();


        for (Commande commande : commandes) {


            HBox commandeBox = new HBox(100);
            commandeBox.setAlignment(Pos.CENTER_LEFT);
            commandeBox.setPadding(new Insets(5));

            Label label = new Label("#" + commandeNumero--);
            Button btn = new Button("Facture");
            btn.setOnAction(e -> afficherFacture(commande.getCommandeId()));

            commandeBox.getChildren().addAll(label, btn);
            commandesContainer.getChildren().add(commandeBox);
        }
    }

    public void afficherFacture(int commandeId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/payment.fxml"));
            Parent root = loader.load();
            PanierControlleur controller = loader.getController();
            controller.initData(daoFactory, utilisateurConnecte, utilisateurConnecte, commandeId); // injecte les données après le load
            Stage currentStage = (Stage) commandesContainer.getScene().getWindow(); // ou un autre bouton/label
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Page de facture n°" + commandeId);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClients(List<Utilisateur> clients) {
        clientsContainer.getChildren().clear();

        for (Utilisateur client : clients) {
            HBox clientBox = new HBox(150);
            clientBox.setAlignment(Pos.CENTER_LEFT);
            clientBox.setPadding(new Insets(10, 0, 10, 50));

            Label idLabel = new Label("-ID_" + client.getUtilisateurId());
            Button deleteBtn = new Button();
            ImageView icon = new ImageView(new Image(getClass().getResource("/icons8-close-50.png").toExternalForm()));
            icon.setFitHeight(22);
            icon.setFitWidth(22);
            deleteBtn.setGraphic(icon);

            deleteBtn.setOnAction(e -> {
                utilisateurDao = new UtilisateurDaoImpl(daoFactory);
                utilisateurDao.supprimer(client);
                clientsContainer.getChildren().remove(clientBox);
            });

            clientBox.getChildren().addAll(idLabel, deleteBtn);
            clientsContainer.getChildren().add(clientBox);
        }
    }
}
