package com.inventory.controller;

import com.inventory.dao.CategoryDAO;
import com.inventory.model.Category;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CategoryController {

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, Integer> colId;
    @FXML
    private TableColumn<Category, String> colName;

    @FXML
    private VBox actionPanel;
    @FXML
    private TextField nameField;
    @FXML
    private Label errorLabel;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private CategoryDAO categoryDAO = new CategoryDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        loadCategories();

        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getName());
            }
        });
    }

    private void loadCategories() {
        categoryTable.setItems(categoryDAO.getAllCategories());
    }

    @FXML
    public void handleAddCategory() {
        // verif vide
        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Le nom est obligatoire.");
            return;
        }

        Category c = new Category();
        c.setName(nameField.getText());

        categoryDAO.addCategory(c);
        refreshUI();
    }

    @FXML
    public void handleUpdateCategory() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Sélectionnez une catégorie.");
            return;
        }

        if (nameField.getText().isEmpty()) {
            errorLabel.setText("Le nom est obligatoire.");
            return;
        }

        selected.setName(nameField.getText());
        categoryDAO.updateCategory(selected);
        refreshUI();
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText("Catégorie mise à jour.");
    }

    @FXML
    public void handleDeleteCategory() {
        // suppression
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            categoryDAO.deleteCategory(selected.getId());
            refreshUI();
        } else {
            errorLabel.setText("Sélectionnez une catégorie.");
        }
    }

    private void refreshUI() {
        loadCategories();
        nameField.clear();
        errorLabel.setText("");
        categoryTable.getSelectionModel().clearSelection();
    }
}
