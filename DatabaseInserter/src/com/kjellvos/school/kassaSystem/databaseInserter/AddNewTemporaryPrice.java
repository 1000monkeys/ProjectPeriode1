package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by kjevo on 3/24/17.
 */
public class AddNewTemporaryPrice implements SceneImplementation {
    MainMenu mainMenu;
    GridHandler gridHandler;

    Scene scene;

    Button backToLastMenuButton, submitButton;
    Text validFromDateText, validFromTimeText, validTillDateText, validTillTimeText, priceText;
    DatePicker validFromDatePicker, validTillDatePicker;
    TextField validFromTimeTextField, validTillTimeTextField, priceTextField;

    int id;

    public AddNewTemporaryPrice(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public Scene createAndGetScene() {
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            mainMenu.returnToPreviousScene();
        });

        LocalDate now = LocalDate.now();
        validFromDateText = new Text("Geldig vanaf deze datum:");
        now = now.plusDays(1L);
        validFromDatePicker = new DatePicker(now);

        validFromTimeText = new Text("Geldig vanaf deze tijd:");
        validFromTimeTextField = new TextField();
        validFromTimeTextField.setText("00:00:01");
        validFromTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            mainMenu.getRegexAndFocusFunctions().doTimeRegex(validFromTimeTextField, oldValue, newValue);
        });
        validFromTimeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            mainMenu.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validFromTimeTextField, newValue);
        });

        validTillDateText = new Text("Geldig tot deze datum:");
        validTillDatePicker = new DatePicker(now);

        validTillTimeText = new Text("Geldig tot deze tijd:");
        validTillTimeTextField = new TextField();
        validTillTimeTextField.setText("23:59:59");
        validTillTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            mainMenu.getRegexAndFocusFunctions().doTimeRegex(validTillTimeTextField, oldValue, newValue);
        });
        validTillTimeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            mainMenu.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validTillTimeTextField, newValue);
        });

        priceText = new Text("Prijs:");
        priceTextField = new TextField();
        priceTextField.setText("€0.01");
        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            mainMenu.getRegexAndFocusFunctions().doPriceRegex(priceTextField, oldValue, newValue);
        });

        submitButton = new Button("Toevoegen!");
        submitButton.setOnMouseEntered((MouseEvent event) -> {
            mainMenu.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validTillTimeTextField, false);
            mainMenu.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validFromTimeTextField, false);
            mainMenu.getRegexAndFocusFunctions().catchWrongInputOnFocusLeavePrice(priceTextField, false);
        });
        submitButton.setOnMouseClicked((MouseEvent event) -> {
            doSubmit(id);
        });

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0, 1, validFromDateText, false);
        gridHandler.add(1, 1, validFromDatePicker, false);

        gridHandler.add(0, 2, validFromTimeText, false);
        gridHandler.add(1, 2, validFromTimeTextField, false);

        gridHandler.add(0, 3, validTillDateText, false);
        gridHandler.add(1, 3, validTillDatePicker, false);

        gridHandler.add(0, 4, validTillTimeText, false);
        gridHandler.add(1, 4, validTillTimeTextField, false);

        gridHandler.add(0, 5, priceText, false);
        gridHandler.add(1, 5, priceTextField, false);

        gridHandler.add(0, 6, submitButton, 2, 1, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    private void doSubmit(int id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String from = validFromDatePicker.getValue().toString() + " " + validFromTimeTextField.getText();
        String till = validTillDatePicker.getValue().toString() + " " + validTillTimeTextField.getText();

        LocalDateTime fromDateTime = null;
        LocalDateTime tillDateTime = null;
        try{
            fromDateTime = LocalDateTime.parse(from, formatter);
            tillDateTime = LocalDateTime.parse(till, formatter);
        }catch (DateTimeParseException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Datum / tijd error!");
            alert.setHeaderText("Er is iets mis met uw invoer!");
            alert.setContentText(e.getParsedString());
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
            alert.showAndWait();
        }

        String price = priceTextField.getText();
        price = price.substring(1, price.length());
        if(fromDateTime != null && tillDateTime != null && mainMenu.getDatabase().checkNewTemporaryPriceUpload(id, fromDateTime, tillDateTime)) {
            if (fromDateTime == tillDateTime || fromDateTime.isBefore(tillDateTime)) {
                if (fromDateTime.isAfter(LocalDateTime.now())) {
                    mainMenu.getDatabase().newTemporaryPriceUpload(id, fromDateTime, tillDateTime, Float.parseFloat(price));
                    mainMenu.returnToPreviousScene();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Oops!");
                    alert.setHeaderText("Er gaat iets fout!");
                    alert.setContentText("Je kan niet een andere prijs in het verleden toevoegen!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Start datum na begin datum!");
                alert.setHeaderText("De start datum is na het begin datum.");
                alert.setContentText("Dit kan natuurlijk helemaal niet!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops!");
            alert.setHeaderText("Er gaat iets fout!");
            alert.setContentText("Je kan niet 2 prijzen tegelijk hebben!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
            alert.showAndWait();
        }
    }

    @Override
    public void reload() {
        //TODO
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void setId(int id) {
        this.id = id;
    }
}
