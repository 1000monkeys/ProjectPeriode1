package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.database.Price;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;

import javax.security.auth.callback.Callback;
import java.util.Optional;

/**
 * Created by kjevo on 3/28/17.
 */

public class GetCategorieList implements SceneImplementation {
    private MainMenu mainMenu;
    private GridHandler gridHandler;

    private Scene scene;

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
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                Categorie categorie = (Categorie) tableView.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Zeker weten?");
                alert.setHeaderText("Weet je het zeker?");
                alert.setContentText("Weet je zeker dat je deze categorie wil verwijderen? En de items die dit als categorie hebben naar geen categorie wil zetten?");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    mainMenu.getDatabase().deleteCategorie(categorie.getId());
                }
                reload();
            }else{
                mainMenu.getRegexAndFocusFunctions().showNothingSelectedAlert();
            }
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
