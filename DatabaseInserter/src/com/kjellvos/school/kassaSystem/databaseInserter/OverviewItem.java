package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Item;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Price;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

/**
 * Created by kjevo on 3/26/17.
 */

/**
 * TODO Automatisch invullen oude waarden
 */
public class OverviewItem implements SceneImplementation {
    Main main;
    GridHandler gridHandler;
    Item item;

    Scene scene;

    Button backToLastMenuButton, pickImageButton, showImageButton, addTemporaryPriceButton, deleteTemporaryPriceButton, submitButton;
    Text pickImageText, enterNameText, enterDescriptionText, priceText, categorieText;
    TextField enterNameTextField, enterDescriptionTextField, priceTextField;

    ComboBox categorieComboBox;

    TableView tableView;
    TableColumn idTableColumn, fromWhenTableColumn, tillWhenTableColumn, priceTableColumn;

    File file;
    Image image;

    int id;
    ObservableList categories;

    public OverviewItem(Main main) {
        this.main = main;
    }

    @Override
    public Scene createAndGetScene() {
        item = main.getDatabase().getItemInfo(id);

        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            main.returnToPreviousScene();
        });

        enterNameText = new Text("De naam:");
        enterNameTextField = new TextField();
        enterNameTextField.setText(item.getName());

        enterDescriptionText = new Text("De korte beschrijving:");
        enterDescriptionTextField = new TextField();
        enterDescriptionTextField.setText(item.getDescription());

        priceText = new Text("De standaard prijs:");
        priceTextField = new TextField();
        priceTextField.setText("€" + item.getPrice());
        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().doPriceRegex(priceTextField, oldValue, newValue);
        });

        categorieText = new Text("Selecteer een categorie:");
        categories = main.getDatabase().getCategorieNamesList();
        categorieComboBox = new ComboBox(categories);
        categorieComboBox.getSelectionModel().select(item.getCategorie());

        pickImageText = new Text("Afbeelding aanpassen:");
        image = item.getImage();
        showImageButton = new Button("Afbeelding laten zien.");
        showImageButton.setOnMouseClicked(event -> {
            Dialog<String> dialog = new Dialog();
            dialog.setTitle("Afbeelding product");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(400D);
            imageView.setFitHeight(400D);
            dialog.getDialogPane().setContent(imageView);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

            dialog.showAndWait();
        });
        pickImageButton = new Button("Afbeelding kiezen.");
        pickImageButton.setOnMouseClicked(event -> {
            file = main.getAddNewItem().handlePickImageButtonClick();
            try {
                if (file != null) {
                    image = new Image(new FileInputStream(file));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idTableColumn = new TableColumn("ID");
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Price, Integer>("id"));

        fromWhenTableColumn = new TableColumn("Vanaf wanneer");
        fromWhenTableColumn.setCellValueFactory(new PropertyValueFactory<Price, LocalDateTime>("fromWhen"));

        tillWhenTableColumn = new TableColumn("Tot wanneer");
        tillWhenTableColumn.setCellValueFactory(new PropertyValueFactory<Price, LocalDateTime>("tillWhen"));

        priceTableColumn = new TableColumn("Prijs");
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<Price, Float>("price"));

        tableView.setItems(main.getDatabase().getPricesOfItem(id));
        tableView.getColumns().addAll(idTableColumn, fromWhenTableColumn, tillWhenTableColumn, priceTableColumn);

        addTemporaryPriceButton = new Button("Toevoegen.");
        addTemporaryPriceButton.setOnMouseClicked(event -> {
            main.changeScene(main.getAddNewTemporaryPrice(id));
        });

        deleteTemporaryPriceButton = new Button("Verwijderen.");
        deleteTemporaryPriceButton.setOnMouseClicked(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                Price price = (Price) tableView.getSelectionModel().getSelectedItem();
                main.getDatabase().deleteTemporaryPrice(id, price.getId());
            }else{
                //TODO
            }
        });

        submitButton = new Button("Aanpassen!");
        submitButton.setOnMouseEntered((MouseEvent event) -> {
            main.getRegexAndFocusFunctions().catchWrongInputOnFocusLeavePrice(priceTextField, false);
        });
        submitButton.setOnMouseClicked(event -> {
            main.getDatabase().itemUpdate(id, enterNameTextField.getText(), enterDescriptionTextField.getText(), priceTextField.getText().substring(1, priceTextField.getText().length()), file);
        });

        gridHandler.add(0, 0, backToLastMenuButton, 4, 1, false);

        gridHandler.add(0, 1, enterNameText, 2, 1, false);
        gridHandler.add(2, 1, enterNameTextField, 2, 1, false);

        gridHandler.add(0, 2, enterDescriptionText, 2, 1, false);
        gridHandler.add(2, 2, enterDescriptionTextField, 2, 1, false);

        gridHandler.add(0, 3, priceText, 2, 1, false);
        gridHandler.add(2, 3, priceTextField, 2, 1, false);

        gridHandler.add(0, 4, categorieText, 2, 1, false);
        gridHandler.add(2, 4, categorieComboBox, 2, 1, false);

        gridHandler.add(0, 5, pickImageText, 2, 1, false);
        gridHandler.add(2, 5, showImageButton, 1, 1, false);
        gridHandler.add(3, 5, pickImageButton, 1, 1, false);

        gridHandler.add(0, 6, submitButton, 4, 1, false);

        gridHandler.add(0, 7, tableView, 3, 5, false);
        gridHandler.add(3, 7, addTemporaryPriceButton, 1, 4, false);
        gridHandler.add(3, 11, deleteTemporaryPriceButton, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public void reload() {
        tableView.setItems(main.getDatabase().getPricesOfItem(id));
        categorieComboBox = new ComboBox(categories);
        categorieComboBox.getSelectionModel().select(new String(item.getCategorie()));
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void setId(int id) {
        this.id = id;
    }
}
