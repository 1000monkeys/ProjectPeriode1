package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Item;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by kjevo on 3/28/17.
 */
public class GetCategorieList implements SceneImplementation{
    Main main;
    GridHandler gridHandler;

    Scene scene;

    private Button backToLastMenuButton;
    private TableView tableView;
    private TableColumn idTableColumn, nameTableColumn;

    public GetCategorieList(Main main){
        this.main = main;
    }

    @Override
    public Scene createAndGetScene() {
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

        tableView.setItems(main.getDatabase().getCategorieList());
        tableView.getColumns().addAll(idTableColumn, nameTableColumn);

        gridHandler.add(0, 0, backToLastMenuButton, false);
        gridHandler.add(0,1, tableView, 1, 5, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public void reload() {
        tableView.setItems(main.getDatabase().getCategorieList());
    }

    @Override
    public Scene getScene() {
        return  scene;
    }
}
