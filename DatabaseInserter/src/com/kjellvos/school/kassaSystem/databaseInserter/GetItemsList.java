package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn idTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn;

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

        nameTableColumn = new TableColumn("Naam");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

        descriptionTableColumn = new TableColumn("Beschrijving");
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));

        priceTableColumn = new TableColumn("Prijs");
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("price"));

        moreInfoTableColumn = new TableColumn("Meer info/editen");
        moreInfoTableColumn.setCellValueFactory(new PropertyValueFactory<Item, Button>("button"));

        tableView.setItems(mainMenu.getDatabase().getItemsList());
        tableView.getColumns().addAll(idTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn);

        addNewItem = new Button("Nieuwe item toevoegen.");
        addNewItem.setOnMouseClicked(event -> {
            mainMenu.changeScene(mainMenu.getAddNewItem());
        });

        deleteItem = new Button("Item verwijderen.");
        deleteItem.setOnMouseClicked(event -> {
            //TODO
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
