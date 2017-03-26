package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Price;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Created by kjevo on 3/26/17.
 */

/**
 * TODO Automatisch invullen oude waarden
 */
public class OverviewItem {
    Main main;
    GridHandler gridHandler;

    Button backToLastMenuButton, pickImageButton, submitButton;
    Text pickImageText, enterNameText, enterDescriptionText, priceText;
    TextField enterNameTextField, enterDescriptionTextField, priceTextField;

    TableView tableView;
    TableColumn idTableColumn, fromWhenTableColumn, tillWhenTableColumn, priceTableColumn;

    File file;

    public OverviewItem(Main main) {
        this.main = main;
    }

    public Scene createAndGetScene(int id) {
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            main.returnToPreviousScene();
        });

        enterNameText = new Text("Voer de naam in:");
        enterNameTextField = new TextField();
        enterNameText.setText("test");

        enterDescriptionText = new Text("Voer een korte beschrijving in:");
        enterDescriptionTextField = new TextField();

        priceText = new Text("Voer de standaard prijs in:");
        priceTextField = new TextField();
        priceTextField.setText("€0.01");
        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().doPriceRegex(priceTextField, oldValue, newValue);
        });

        pickImageText = new Text("Kies een bijpassende afbeelding:");
        pickImageButton = new Button("Afbeelding kiezen.");
        pickImageButton.setOnMouseClicked(event -> {
            file = main.getAddNewItem().handlePickImageButtonClick();
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

        submitButton = new Button("Invoeren!");
        submitButton.setOnMouseClicked(event -> {
            main.getDatabase().itemUpdate(enterNameTextField.getText(), enterDescriptionTextField.getText(), Float.parseFloat(priceTextField.getText().substring(1, priceTextField.getText().length())), file);
        });

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0, 1, enterNameText, false);
        gridHandler.add(1, 1, enterNameTextField, false);

        gridHandler.add(0, 2, enterDescriptionText, false);
        gridHandler.add(1, 2, enterDescriptionTextField, false);

        gridHandler.add(0, 3, priceText, false);
        gridHandler.add(1, 3, priceTextField, false);

        gridHandler.add(0, 4, pickImageText, false);
        gridHandler.add(1, 4, pickImageButton, false);

        gridHandler.add(0, 5, tableView, 2, 4, false);

        gridHandler.add(0, 9, submitButton, 2, 1, false);

        return gridHandler.getGridAsScene();
    }
}
