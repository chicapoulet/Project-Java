package com.inventory.controller;

import com.inventory.dao.CategoryDAO;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.util.SessionManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;

public class ProductController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> colId;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colCategory;
    @FXML
    private TableColumn<Product, BigDecimal> colPrice;
    @FXML
    private TableColumn<Product, Integer> colStock;

    @FXML
    private VBox actionPanel;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descField;
    @FXML
    private ComboBox<Category> categoryBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private Label errorLabel;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @FXML
    public void initialize() {
        // configuration table
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryName()));
        colPrice.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        colStock.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStock()));

        loadProducts();

        // recup categories
        categoryBox.setItems(categoryDAO.getAllCategories());

        setupRolePermissions();

        // ecouteur selection
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    // restrictions admin
    private void setupRolePermissions() {
        boolean isAdmin = SessionManager.isAdmin();

        if (!isAdmin) {
            nameField.setDisable(true);
            descField.setDisable(true);
            categoryBox.setDisable(true);
            priceField.setDisable(true);

            btnAdd.setVisible(false);
            btnAdd.setManaged(false);
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
        }
    }

    private void populateForm(Product p) {
        nameField.setText(p.getName());
        descField.setText(p.getDescription());
        priceField.setText(p.getPrice().toString());
        stockField.setText(String.valueOf(p.getStock()));

        for (Category c : categoryBox.getItems()) {
            if (c.getId() == p.getCategoryId()) {
                categoryBox.setValue(c);
                break;
            }
        }
    }

    // appel bdd
    private void loadProducts() {
        productTable.setItems(productDAO.getAllProducts());
    }

    @FXML
    public void handleAddProduct() {
        if (!SessionManager.isAdmin())
            return;

        try {
            if (validateForm()) {
                Product p = new Product();
                fillProductFromForm(p);
                productDAO.addProduct(p);
                refreshUI();
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Veuillez sélectionner un produit.");
            return;
        }

        try {
            if (SessionManager.isAdmin()) {
                if (validateForm()) {
                    fillProductFromForm(selected);
                    productDAO.updateProduct(selected);
                    refreshUI();
                    errorLabel.setStyle("-fx-text-fill: green;");
                    errorLabel.setText("Produit mis à jour.");
                }
            } else {
                String stockText = stockField.getText();
                if (stockText.isEmpty()) {
                    errorLabel.setText("Le stock est obligatoire.");
                    return;
                }
                int newStock = Integer.parseInt(stockText);
                productDAO.updateStock(selected.getId(), newStock);
                refreshUI();
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Stock mis à jour.");
            }
        } catch (NumberFormatException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Valeurs numériques invalides.");
        }
    }

    @FXML
    public void handleDeleteProduct() {
        if (!SessionManager.isAdmin())
            return;

        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            productDAO.deleteProduct(selected.getId());
            refreshUI();
        } else {
            errorLabel.setText("Veuillez sélectionner un produit.");
        }
    }

    // verif formulaire
    private boolean validateForm() {
        if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || categoryBox.getValue() == null) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Nom, Prix et Catégorie obligatoires.");
            return false;
        }
        return true;
    }

    private void fillProductFromForm(Product p) {
        p.setName(nameField.getText());
        p.setDescription(descField.getText());
        p.setCategoryId(categoryBox.getValue().getId());
        p.setCategoryName(categoryBox.getValue().getName());
        p.setPrice(new BigDecimal(priceField.getText().replace(",", ".")));
        String stockText = stockField.getText();
        p.setStock(stockText.isEmpty() ? 0 : Integer.parseInt(stockText));
    }

    private void refreshUI() {
        loadProducts();
        clearForm();
        errorLabel.setText("");
    }

    private void clearForm() {
        nameField.clear();
        descField.clear();
        priceField.clear();
        stockField.clear();
        categoryBox.getSelectionModel().clearSelection();
        productTable.getSelectionModel().clearSelection();
    }
}
