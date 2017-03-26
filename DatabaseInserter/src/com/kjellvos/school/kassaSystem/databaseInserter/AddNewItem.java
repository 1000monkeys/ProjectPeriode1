package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by kjevo on 3/24/17.
 */
public class AddNewItem {
    Main main;
    GridHandler gridHandler;

    Button backToLastMenuButton, pickImageButton, submitButton;
    Text pickImageText, enterNameText, enterDescriptionText, priceText;
    TextField enterNameTextField, enterDescriptionTextField, priceTextField;

    FileChooser fileChooser;
    File file;

    public AddNewItem(Main main){
        this.main = main;
    }

    public Scene createAndGetScene() {
        gridHandler = new GridHandler();
        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            main.returnToPreviousScene();
        });

        enterNameText = new Text("Voer de naam in:");
        enterNameTextField = new TextField();

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
            file = handlePickImageButtonClick();
        });

        submitButton = new Button("Invoeren!");
        submitButton.setOnMouseClicked(event -> {
            main.getDatabase().newItemUpload(enterNameTextField.getText(), enterDescriptionTextField.getText(), Float.parseFloat(priceTextField.getText().substring(1, priceTextField.getText().length())), file);
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

        gridHandler.add(0, 5, submitButton, 2, 1, false);

        return gridHandler.getGridAsScene();
    }

    public File handlePickImageButtonClick() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies afbeelding.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        return fileChooser.showOpenDialog(main.getPrimaryStage());
    }
}
