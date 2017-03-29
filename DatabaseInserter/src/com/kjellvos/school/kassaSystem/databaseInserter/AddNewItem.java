package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by kjevo on 3/24/17.
 */
public class AddNewItem implements SceneImplementation {
    private MainMenu mainMenu;
    private GridHandler gridHandler;

    private Scene scene;

    private Button backToLastMenuButton, pickImageButton, submitButton;
    private Text pickImageText, enterNameText, enterDescriptionText, priceText, categorieText;
    private TextField enterNameTextField, enterDescriptionTextField, priceTextField;
    private ComboBox categorieComboBox;

    private FileChooser fileChooser;
    private File file;

    private ObservableList categories;

    public AddNewItem(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    @Override
    public void reload() {
        mainMenu.returnToPreviousScene();
        mainMenu.changeScene(mainMenu.getAddNewItem());
    }

    public Scene createAndGetScene() {
        gridHandler = new GridHandler();
        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            mainMenu.returnToPreviousScene();
        });

        enterNameText = new Text("Voer de naam in:");
        enterNameTextField = new TextField();

        enterDescriptionText = new Text("Voer een korte beschrijving in:");
        enterDescriptionTextField = new TextField();

        priceText = new Text("Voer de standaard prijs in:");
        priceTextField = new TextField();
        priceTextField.setText("€0.01");
        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            mainMenu.getRegexAndFocusFunctions().doPriceRegex(priceTextField, oldValue, newValue);
        });

        categorieText = new Text("Selecteer een categorie:");
        categories = mainMenu.getDatabase().getCategorieNamesList();
        categorieComboBox = new ComboBox(categories);

        pickImageText = new Text("Kies een bijpassende afbeelding:");
        pickImageButton = new Button("Afbeelding kiezen.");
        pickImageButton.setOnMouseClicked(event -> {
            file = handlePickImageButtonClick();
        });

        submitButton = new Button("Invoeren!");
        submitButton.setOnMouseClicked(event -> {
            mainMenu.getDatabase().newItemUpload(enterNameTextField.getText(), enterDescriptionTextField.getText(), Float.parseFloat(priceTextField.getText().substring(1, priceTextField.getText().length())), categorieComboBox.getSelectionModel().getSelectedItem().toString(), file);
        });

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0, 1, enterNameText, false);
        gridHandler.add(1, 1, enterNameTextField, false);

        gridHandler.add(0, 2, enterDescriptionText, false);
        gridHandler.add(1, 2, enterDescriptionTextField, false);

        gridHandler.add(0, 3, priceText, false);
        gridHandler.add(1, 3, priceTextField, false);

        gridHandler.add(0, 4, categorieText, false);
        gridHandler.add(1, 4, categorieComboBox, false);

        gridHandler.add(0, 5, pickImageText, false);
        gridHandler.add(1, 5, pickImageButton, false);

        gridHandler.add(0, 6, submitButton, 2, 1, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public File handlePickImageButtonClick() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Kies afbeelding.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        return fileChooser.showOpenDialog(mainMenu.getPrimaryStage());
    }
}
