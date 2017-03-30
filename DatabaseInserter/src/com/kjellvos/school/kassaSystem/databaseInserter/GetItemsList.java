package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.database.Price;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.util.Optional;

/**
 * Created by kjevo on 3/25/17.
 */

/**
 * TODO Knop inbouwen voor meer info, die je naar pagina stuurt met info over alle prijzen en editen van de item en de prijzen verwijderen/editen
 */
public class GetItemsList implements SceneImplementation {
    private MainMenu mainMenu;
    private GridHandler gridHandler;

    private Scene scene;

    private Button backToLastMenuButton, addNewItem, deleteItem;
    private TableView tableView;
    private TableColumn idTableColumn, categorieTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn;

    public GetItemsList(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void reload() {
        tableView.setItems(mainMenu.getDatabase().getItemsList());
    }

    public Scene createAndGetScene(){
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            mainMenu.returnToPreviousScene();
        });

        tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idTableColumn = new TableColumn("ID");
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));

        categorieTableColumn = new TableColumn("Categorie");
        categorieTableColumn.setCellValueFactory(new PropertyValueFactory<Price, String>("categorie"));

        nameTableColumn = new TableColumn("Naam");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

        descriptionTableColumn = new TableColumn("Beschrijving");
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));

        priceTableColumn = new TableColumn("Prijs");
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("price"));

        moreInfoTableColumn = new TableColumn("Meer info/editen");
        moreInfoTableColumn.setCellValueFactory(new PropertyValueFactory<Item, Button>("button"));

        tableView.setItems(mainMenu.getDatabase().getItemsList());
        tableView.getColumns().addAll(idTableColumn, categorieTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn);

        addNewItem = new Button("Nieuwe item toevoegen.");
        addNewItem.setOnMouseClicked(event -> {
            mainMenu.changeScene(mainMenu.getAddNewItem());
        });

        deleteItem = new Button("Item verwijderen.");
        deleteItem.setOnMouseClicked(event -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                Item item = (Item) tableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Zeker weten?");
                alert.setHeaderText("Weet je het zeker?");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                alert.setContentText("Weet je zeker dat je deze item, zijn prijs en tijdelijke prijsen wil verwijderen?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    mainMenu.getDatabase().deleteItem(item.getId());
                }
                reload();
            }else{
                mainMenu.getRegexAndFocusFunctions().showNothingSelectedAlert();
            }
        });

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);
        gridHandler.add(0,1, tableView, 2, 5, false);
        gridHandler.add(0, 6, addNewItem, false);
        gridHandler.add(1, 6, deleteItem, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
