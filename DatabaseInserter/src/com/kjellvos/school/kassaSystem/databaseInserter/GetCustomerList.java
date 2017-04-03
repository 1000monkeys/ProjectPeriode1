package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.CustomerCard;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.embed.swt.CustomTransfer;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.util.Optional;

/**
 * Created by kjevo on 3/31/17.
 */
public class GetCustomerList implements SceneImplementation {
    private MainMenu mainMenu;
    private GridHandler gridHandler;

    private Scene scene;

    private Button backToLastMenuButton, newCustomerCard, deleteCustomerCard;
    private TableView tableView;
    private TableColumn idTableColumn, firstNameColumn, lastNameColumn, streetNameColumn, moreInfoTableColumn;

    public GetCustomerList(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    @Override
    public Scene createAndGetScene() {
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            mainMenu.returnToPreviousScene();
        });

        tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idTableColumn = new TableColumn("ID");
        idTableColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, Integer>("id"));

        firstNameColumn = new TableColumn("Voornaam");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, String>("firstName"));

        lastNameColumn = new TableColumn("Achternaam");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, String>("lastName"));

        streetNameColumn = new TableColumn("Straat naam");
        streetNameColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, String>("streetName"));

        moreInfoTableColumn = new TableColumn("Meer info/editen");
        moreInfoTableColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, Button>("button"));

        tableView.setItems(mainMenu.getDatabase().getCustomersList());
        tableView.getColumns().addAll(idTableColumn, firstNameColumn, lastNameColumn, streetNameColumn, moreInfoTableColumn);

        newCustomerCard = new Button("Nieuwe klantenkaart aanmaken.");
        newCustomerCard.setOnMouseClicked(event -> {
            mainMenu.changeScene(mainMenu.getAddNewCustomerCard());
        });

        deleteCustomerCard = new Button("Klantenkaart verwijderen.");
        deleteCustomerCard.setOnMouseClicked(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                CustomerCard customerCard = (CustomerCard) tableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Zeker weten?");
                alert.setHeaderText("Weet je het zeker?");
                alert.setContentText("Weet je zeker dat je deze klantenkaart wil verwijderen?");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    mainMenu.getDatabase().deleteCustomerCard(customerCard.getId());
                }
                reload();
            }else{
                mainMenu.getRegexAndFocusFunctions().showNothingSelectedAlert();
            }
        });

        gridHandler.add(0,0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0,1, tableView, 2, 5, false);

        gridHandler.add(0, 6, newCustomerCard, false);
        gridHandler.add(1, 6, deleteCustomerCard, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public void reload() {
        tableView.setItems(mainMenu.getDatabase().getCustomersList());
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
