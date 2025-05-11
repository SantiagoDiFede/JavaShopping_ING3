package Controller;

import Model.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AjouterProduitControlleur {

    @FXML private TextField nomField;
    @FXML private TextField prixField;
    @FXML private TextField prixReductionField;
    @FXML private TextField qteReductionField;
    @FXML private TextField imageField;

    private AdminControlleur adminController;


    /**
     * Initialise le contrôleur.
     */
    public void setAdminController(AdminControlleur controller) {
        this.adminController = controller;
    }

    /**
     * annule l'ajout du produit et ferme la fenêtre.
     */
    @FXML
    private void annuler() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    /**
     * Ajoute un produit avec les informations fournies dans les champs de texte.
     * Ferme la fenêtre après l'ajout.
     */
    @FXML
    private void ajouter() {
        String nom = nomField.getText();
        double prix = Double.parseDouble(prixField.getText());
        double prixReduction = Double.parseDouble(prixReductionField.getText());
        int qteReduction = Integer.parseInt(qteReductionField.getText());
        String image = imageField.getText();


        Produit nouveauProduit = new Produit(nom, prix, prixReduction, qteReduction, image);

        adminController.ajouterProduitDepuisPopup(nouveauProduit);
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}