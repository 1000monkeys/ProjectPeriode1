package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.database.CustomerCard;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Created by kjevo on 4/1/17.
 */
public class OverviewCustomer implements SceneImplementation {
    private MainMenu mainMenu;
    private GridHandler gridHandler;

    private Scene scene;

    private Button backToLastMenuButton, submitButton;
    private Text firstNameText, lastNameText, streetNameText, houseNumbertext, telephoneNumberText;
    private TextField firstNameTextField, lastNameTextField, streetNameTextField, houseNumberTextField, telephoneNumberTextField;

    private int id;

    private CustomerCard customerCard;

    public OverviewCustomer(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public Scene createAndGetScene() {
        customerCard = mainMenu.getDatabase().getCustomerCardInfo(id);

        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            mainMenu.returnToPreviousScene();
        });

        firstNameText = new Text("Voornaam:");
        firstNameTextField = new TextField();
        firstNameTextField.setText(customerCard.getFirstName());

        lastNameText = new Text("Achternaam:");
        lastNameTextField = new TextField();
        lastNameTextField.setText(customerCard.getLastName());

        streetNameText = new Text("Straat naam:");
        streetNameTextField = new TextField();
        streetNameTextField.setText(customerCard.getStreetName());

        houseNumbertext = new Text("Huis nummer:");
        houseNumberTextField = new TextField();
        houseNumberTextField.setText(String.valueOf(customerCard.getHouseNumber()));

        telephoneNumberText = new Text("Telefoon nummer:");
        telephoneNumberTextField = new TextField();
        telephoneNumberTextField.setText(String.valueOf(customerCard.getTelephoneNumber()));

        submitButton = new Button("Invoeren!");
        submitButton.setOnMouseClicked(event -> {
            mainMenu.getDatabase().updateCustomerCard(firstNameTextField.getText(), lastNameTextField.getText(), streetNameTextField.getText(), houseNumberTextField.getText(), telephoneNumberTextField.getText());
        });

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0, 1, firstNameText, false);
        gridHandler.add(1, 1, firstNameTextField, false);

        gridHandler.add(0, 2, lastNameText, false);
        gridHandler.add(1, 2, lastNameTextField, false);

        gridHandler.add(0, 3, streetNameText, false);
        gridHandler.add(1, 3, streetNameTextField, false);

        gridHandler.add(0, 4, houseNumbertext, false);
        gridHandler.add(1, 4, houseNumberTextField, false);

        gridHandler.add(0, 5, telephoneNumberText, false);
        gridHandler.add(1, 5, telephoneNumberTextField, false);

        gridHandler.add(0, 6, submitButton, 2, 1, false);

        scene = gridHandler.getGridAsScene();
        return scene;
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
