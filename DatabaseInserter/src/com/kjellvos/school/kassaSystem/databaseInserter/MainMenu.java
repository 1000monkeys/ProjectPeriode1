package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import com.kjellvos.school.kassaSystem.common.functions.RegexAndFocusFunctions;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Database;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by kjevo on 3/24/17.
 */
public class MainMenu extends MainScene implements SceneImplementation {
    private GridHandler gridHandler;
    private Database database;
    private RegexAndFocusFunctions regexAndFocusFunctions;
    private GetItemsList getItemsList;
    private GetCategorieList getCategorieList;
    private GetCustomerList getCustomerList;
    private AddNewItem addNewItem;
    private AddNewCategorie addNewCategorie;
    private AddNewCustomerCard addNewCustomerCard;
    private AddNewTemporaryPrice addNewTemporaryPrice;
    private OverviewItem overviewItem;
    private OverviewCustomer overviewCustomer;
    private OverviewCategorie overviewCategorie;


    Scene scene;

    Button itemOverview, addNewCustomer, categorieOverview;

    public MainMenu(Stage stage){
        super(stage);
        regexAndFocusFunctions = new RegexAndFocusFunctions();
        try {
            database = new Database(this);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Whoops!");
            alert.setHeaderText("Er lijkt geen database te zijn!");
            alert.setContentText("Als er wel databasse hoort te zijn, vraag dan uw administrator om hulp!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
            alert.showAndWait();

            Platform.exit();
            e.printStackTrace();
        }
        getItemsList = new GetItemsList(this);
        getCategorieList = new GetCategorieList(this);
        getCustomerList = new GetCustomerList(this);
        addNewItem = new AddNewItem(this);
        addNewCategorie = new AddNewCategorie(this);
        addNewCustomerCard = new AddNewCustomerCard(this);
        addNewTemporaryPrice = new AddNewTemporaryPrice(this);
        overviewItem = new OverviewItem(this);
        overviewCustomer = new OverviewCustomer(this);
        overviewCategorie = new OverviewCategorie(this);

        super.getPrimaryStage().setTitle("DatabaseInserter V0.4");
        super.getPrimaryStage().setWidth(800D);
        super.getPrimaryStage().setHeight(600D);
        super.changeScene(this);
        super.getPrimaryStage().show();
    }

    @Override
    public void reload() {

    }

    public Scene createAndGetScene(){
        gridHandler = new GridHandler();

        itemOverview = new Button("Item aanpassen, toevoegen, verwijderen of tijdelijke prijs aan item toevoegen.");
        itemOverview.setOnMouseClicked(event -> {
            changeScene(getItemsList());
        });

        addNewCustomer = new Button("Klantenkaart registreren, aanpassen of verwijderen.");
        addNewCustomer.setOnMouseClicked(event -> {
            changeScene(getCustomerList());
        });

        categorieOverview = new Button("Categorie aanpassen, toevoegen verwijderen.");
        categorieOverview.setOnMouseClicked(event -> {
            changeScene(getCategorieList());
        });

        gridHandler.add(0, 0, itemOverview, false);
        gridHandler.add(0, 1, addNewCustomer, false);
        gridHandler.add(0, 2, categorieOverview, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public AddNewItem getAddNewItem() {
        return addNewItem;
    }

    public AddNewTemporaryPrice getAddNewTemporaryPrice(int id) {
        addNewTemporaryPrice.setId(id);
        return addNewTemporaryPrice;
    }

    public Database getDatabase() {
        return database;
    }

    public RegexAndFocusFunctions getRegexAndFocusFunctions() {
        return regexAndFocusFunctions;
    }

    public GetItemsList getItemsList() {
        return getItemsList;
    }

    public OverviewItem getOverviewItem(int id){
        overviewItem.setId(id);
        return overviewItem;
    }

    public AddNewCategorie getAddNewCategorie() {
        return addNewCategorie;
    }

    public GetCategorieList getCategorieList() {
        return getCategorieList;
    }

    public SceneImplementation getCustomerList() {
        return getCustomerList;
    }

    public AddNewCustomerCard getAddNewCustomerCard() {
        return addNewCustomerCard;
    }

    public SceneImplementation getOverviewCustomer(int id) {
        overviewCustomer.setId(id);
        return overviewCustomer;
    }

    public SceneImplementation getOverviewCategorie(int id) {
        overviewCategorie.setId(id);
        return overviewCategorie;
    }
}
