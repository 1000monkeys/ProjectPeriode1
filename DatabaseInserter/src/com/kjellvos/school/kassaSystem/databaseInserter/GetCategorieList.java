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
 * Created by kjevo on 3/28/17.
 */

//TODO Add delete button and add an add new categorie button, maybe edit?
    //      >Delete should only happen if there are no items currently using the categorie.
public class GetCategorieList implements SceneImplementation {
    MainMenu mainMenu;
    GridHandler gridHandler;

    Scene scene;

    private Button backToLastMenuButton, addNewCategorie, deleteCategorie;
    private TableView tableView;
    private TableColumn idTableColumn, nameTableColumn;

    public GetCategorieList(MainMenu mainMenu){
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
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));

        nameTableColumn = new TableColumn("Naam");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

        tableView.setItems(mainMenu.getDatabase().getCategorieList());
        tableView.getColumns().addAll(idTableColumn, nameTableColumn);

        addNewCategorie = new Button("Nieuwe categorie toevoegen.");
        addNewCategorie.setOnMouseClicked(event -> {
            mainMenu.changeScene(mainMenu.getAddNewCategorie());
        });

        deleteCategorie = new Button("Categorie verwijderen.");
        deleteCategorie.setOnMouseClicked(event -> {
            //TODO
        });

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);
        gridHandler.add(0,1, tableView, 2, 5, false);
        gridHandler.add(0, 6, addNewCategorie, false);
        gridHandler.add(1, 6, deleteCategorie, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public void reload() {
        tableView.setItems(mainMenu.getDatabase().getCategorieList());
    }

    @Override
    public Scene getScene() {
        return  scene;
    }
}
