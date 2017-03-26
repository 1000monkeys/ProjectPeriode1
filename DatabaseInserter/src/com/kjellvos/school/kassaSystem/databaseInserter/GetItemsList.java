package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Item;
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
public class GetItemsList {
    private Main main;
    private GridHandler gridHandler;

    private Button backToLastMenuButton;
    private TableView tableView;
    private TableColumn idTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn;

    public GetItemsList(Main main) {
        this.main = main;
    }

    public Scene createAndGetScene(){
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            main.returnToPreviousScene();
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

        tableView.setItems(main.getDatabase().getItemsList());
        tableView.getColumns().addAll(idTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn);

        gridHandler.add(0, 0, backToLastMenuButton, false);
        gridHandler.add(0,1, tableView, 1, 5, false);

        return gridHandler.getGridAsScene();
    }
}
