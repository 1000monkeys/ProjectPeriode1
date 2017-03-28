package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by kjevo on 3/24/17.
 */
public class AddNewTemporaryPrice implements SceneImplementation {
    Main main;
    GridHandler gridHandler;

    Scene scene;

    Button backToLastMenuButton, submitButton;
    Text validFromDateText, validFromTimeText, validTillDateText, validTillTimeText, priceText;
    DatePicker validFromDatePicker, validTillDatePicker;
    TextField validFromTimeTextField, validTillTimeTextField, priceTextField;

    int id;

    public AddNewTemporaryPrice(Main main) {
        this.main = main;
    }

    @Override
    public Scene createAndGetScene() {
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            main.returnToPreviousScene();
        });

        LocalDate now = LocalDate.now();
        validFromDateText = new Text("Geldig vanaf deze datum:");
        now = now.plusDays(1L);
        validFromDatePicker = new DatePicker(now);

        validFromTimeText = new Text("Geldig vanaf deze tijd:");
        validFromTimeTextField = new TextField();
        validFromTimeTextField.setText("00:00:01");
        validFromTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().doTimeRegex(validFromTimeTextField, oldValue, newValue);
        });
        validFromTimeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validFromTimeTextField, newValue);
        });

        validTillDateText = new Text("Geldig tot deze datum:");
        validTillDatePicker = new DatePicker(now);

        validTillTimeText = new Text("Geldig tot deze tijd:");
        validTillTimeTextField = new TextField();
        validTillTimeTextField.setText("23:59:59");
        validTillTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().doTimeRegex(validTillTimeTextField, oldValue, newValue);
        });
        validTillTimeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validTillTimeTextField, newValue);
        });

        priceText = new Text("Prijs:");
        priceTextField = new TextField();
        priceTextField.setText("€0.01");
        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            main.getRegexAndFocusFunctions().doPriceRegex(priceTextField, oldValue, newValue);
        });

        submitButton = new Button("Toevoegen!");
        submitButton.setOnMouseEntered((MouseEvent event) -> {
            main.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validTillTimeTextField, false);
            main.getRegexAndFocusFunctions().catchWrongInputOnFocusLeaveTime(validFromTimeTextField, false);
            main.getRegexAndFocusFunctions().catchWrongInputOnFocusLeavePrice(priceTextField, false);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String from = validFromDatePicker.getValue().toString() + " " + validFromTimeTextField.getText();
        String till = validTillDatePicker.getValue().toString() + " " + validTillTimeTextField.getText();

        LocalDateTime fromDateTime = LocalDateTime.parse(from, formatter);
        LocalDateTime tillDateTime = LocalDateTime.parse(till, formatter);

        String price = priceTextField.getText();
        price = price.substring(1, price.length());
        if(main.getDatabase().checkNewTemporaryPriceUpload(id, fromDateTime, tillDateTime)) {
            if (fromDateTime == tillDateTime || fromDateTime.isBefore(tillDateTime)) {
                if (fromDateTime.isAfter(LocalDateTime.now())) {
                    main.getDatabase().newTemporaryPriceUpload(id, fromDateTime, tillDateTime, Float.parseFloat(price));
                    main.returnToPreviousScene();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Oops!");
                    alert.setHeaderText("Er gaat iets fout!");
                    alert.setContentText("Je kan niet een andere prijs in het verleden toevoegen!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Start datum na begin datum!");
                alert.setHeaderText("De start datum is na het begin datum.");
                alert.setContentText("Dit kan natuurlijk helemaal niet!");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops!");
            alert.setHeaderText("Er gaat iets fout!");
            alert.setContentText("Je kan niet 2 prijzen tegelijk hebben!");
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
