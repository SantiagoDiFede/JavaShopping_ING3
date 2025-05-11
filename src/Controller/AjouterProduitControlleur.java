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

    public void setAdminController(AdminControlleur controller) {
        this.adminController = controller;
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

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