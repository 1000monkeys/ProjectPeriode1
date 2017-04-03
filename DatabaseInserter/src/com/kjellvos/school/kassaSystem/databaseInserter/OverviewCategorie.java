package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.database.Price;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/**
 * Created by kjell on 3-4-2017.
 */
public class OverviewCategorie implements SceneImplementation {
    private MainMenu mainMenu;
    private GridHandler gridHandler;

    private Scene scene;

    private Button backToLastMenuButton, submitButton;
    private Text categorieText;
    private TextField categorieTextField;
    private TableView tableView;
    private TableColumn idTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn;


    private int id;
    private Categorie categorie;

    public OverviewCategorie(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public Scene createAndGetScene() {
        categorie = mainMenu.getDatabase().getCategorieInfo(id);
        gridHandler = new GridHandler();

        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            mainMenu.returnToPreviousScene();
        });

        categorieText = new Text("Categorie naam:");
        categorieTextField = new TextField();
        categorieTextField.setText(categorie.getName());

        submitButton = new Button("Invoeren!");
        submitButton.setOnMouseClicked(event -> {
            if (!categorie.getName().equals(mainMenu.getDatabase().noCategory) && categorieTextField.getText().length() > 2) {
                mainMenu.getDatabase().updateCategorie(id, categorieTextField.getText());
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("U mag deze categorie niet aanpassen!");
                alert.setHeaderText("Sorry!");
                alert.setContentText("Sorry, u mag deze categorie niet aanpassen!");
                alert.showAndWait();
            }
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

        tableView.setItems(mainMenu.getDatabase().getItemsList(id));
        tableView.getColumns().addAll(idTableColumn, nameTableColumn, descriptionTableColumn, priceTableColumn, moreInfoTableColumn);

        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0, 1, categorieText, false);
        gridHandler.add(1, 1, categorieTextField, false);

        gridHandler.add(0, 2, submitButton, 2, 1, false);


        gridHandler.add(0, 3, tableView, 2, 6, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public void reload() {
        tableView.setItems(mainMenu.getDatabase().getItemsList(id));
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public void setId(int id) {
        this.id = id;
    }
}
