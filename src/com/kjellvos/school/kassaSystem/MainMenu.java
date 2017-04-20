package com.kjellvos.school.kassaSystem;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.database.ReceiptItem;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import com.kjellvos.school.kassaSystem.database.Database;
import com.sun.org.apache.regexp.internal.RE;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Map;
import java.util.ServiceConfigurationError;

/**
 * Created by kjell on 10-3-2017.
 */
public class MainMenu extends MainScene implements SceneImplementation {
    private Database database;

    private Scene scene;

    private Button bonnenButton, kaartenButton, corrigeerButton, betaalButton;
    private Accordion categoriesAccordion;
    private TableView bonTableView;
    private TableColumn nameTableColumn, singlePriceTableColumn, amountTableColumn, totalPriceTableColumn;
    private Text totaalText;

    private GridHandler gridHandler;

    private ObservableList<ReceiptItem> receiptItems;

    public MainMenu(Stage stage) {
        super(stage);
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

        super.getPrimaryStage().setTitle("Kassa V0.1");
        super.getPrimaryStage().setWidth(800D);
        super.getPrimaryStage().setHeight(600D);
        super.getPrimaryStage().setResizable(false);
        super.changeScene(this);
        super.getPrimaryStage().show();
    }

    public Scene createAndGetScene() {
        gridHandler = new GridHandler();

        bonnenButton = new Button("Bonnen");
        kaartenButton = new Button("Kaarten");
        corrigeerButton = new Button("Corrigeer");
        betaalButton = new Button("Betaal");

        receiptItems = FXCollections.observableArrayList();
        ObservableList categories = database.getCategories();
        TitledPane[] titledPanes = new TitledPane[categories.size()];
        Image[][] images = new Image[categories.size()][];
        Text[][] names = new Text[categories.size()][];
        Text[][] descriptions = new Text[categories.size()][];
        categoriesAccordion = new Accordion();
        Font font = new Font("Monospaced", 18);
        for (int i = 0; i < categories.size(); i++) {
            ObservableList items = database.getItemsByCategorie(((Categorie)categories.get(i)).getId());
            images[i] = new Image[items.size()];
            names[i] = new Text[items.size()];
            descriptions[i] = new Text[items.size()];
            VBox vBox = new VBox();
            for (int j = 0; j < items.size(); j++) {
                HBox hBox = new HBox();

                int id = ((Item)items.get(j)).getId();

                images[i][j] = ((Item)items.get(j)).getImage();
                ImageView imageView = new ImageView(images[i][j]);
                imageView.setFitHeight(30D);
                imageView.setFitWidth(30D);

                VBox vBox1 = new VBox();

                String temp = ((Item)items.get(j)).getName();
                if (temp.length() > 30) {
                    temp = temp.substring(0, 27) + "...";
                }
                names[i][j] = new Text(temp);

                Separator separator = new Separator();
                separator.setOrientation(Orientation.VERTICAL);
                separator.setValignment(VPos.CENTER);
                separator.setPrefHeight(30D);

                String temp1 = ((Item)items.get(j)).getDescription();
                if (temp1.length() > 30) {
                    temp1 = temp1.substring(0, 27) + "...";
                }
                descriptions[i][j] = new Text(temp1);

                Separator separator1 = new Separator();
                separator1.setOrientation(Orientation.VERTICAL);
                separator1.setValignment(VPos.CENTER);
                separator1.setPrefHeight(30D);

                Button buyButton = new Button("Toevoegen");
                buyButton.setOnMouseClicked(event -> {
                    addToReceipt(id);
                });

                Separator separator2 = new Separator();
                separator2.setOrientation(Orientation.VERTICAL);
                separator2.setHalignment(HPos.CENTER);

                String priceString = ((Item) items.get(j)).getPrice();
                String[] parts = priceString.split("\\.");
                if (parts[1].length() == 1) {
                    parts[1] = parts[1] + "0";
                }
                TextField price = new TextField(parts[0] + "." + parts[1] + "€");
                price.setPrefWidth(100D);
                price.setEditable(false);

                vBox1.getChildren().addAll(names[i][j], descriptions[i][j]);
                hBox.getChildren().addAll(price, separator, buyButton, separator1, imageView, separator2, vBox1);

                Separator separator3 = new Separator();
                separator3.setOrientation(Orientation.HORIZONTAL);
                separator3.setHalignment(HPos.CENTER);
                vBox.getChildren().addAll(hBox, separator3);
            }
            titledPanes[i] = new TitledPane(((Categorie)categories.get(i)).getName(), vBox);
        }
        categoriesAccordion.getPanes().addAll(titledPanes);

        bonTableView = new TableView();
        bonTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bonTableView.setEditable(true);

        nameTableColumn = new TableColumn("Naam");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<ReceiptItem, String>("name"));
        nameTableColumn.setEditable(false);

        singlePriceTableColumn = new TableColumn("Prijs");
        singlePriceTableColumn.setCellValueFactory(new PropertyValueFactory<ReceiptItem, Double>("price"));
        singlePriceTableColumn.setEditable(false);

        amountTableColumn = new TableColumn("Aantal");
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<ReceiptItem, Integer>("amount"));
        amountTableColumn.setEditable(true);
        amountTableColumn.setCellFactory(TextFieldTableCell.<ReceiptItem, Integer>forTableColumn(new IntegerStringConverter()));
        amountTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ReceiptItem, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ReceiptItem, Integer> t) {
                if(t.getNewValue() == 0){
                    t.getTableView().getItems().remove(t.getTablePosition().getRow());
                }else{
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setAmount(t.getNewValue());
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setTotalPrice(t.getTableView().getItems().get(t.getTablePosition().getRow()).getAmount() * t.getTableView().getItems().get(t.getTablePosition().getRow()).getPrice());
                }
                recalculateTotal();
            }
        });

        totalPriceTableColumn = new TableColumn("Totaal");
        totalPriceTableColumn.setCellValueFactory(new PropertyValueFactory<ReceiptItem, String>("totalPrice"));
        totalPriceTableColumn.setEditable(false);

        totaalText = new Text("\t\t0.00€");

        bonTableView.getColumns().addAll(nameTableColumn, singlePriceTableColumn, amountTableColumn, totalPriceTableColumn);
        bonTableView.setItems(receiptItems);

        gridHandler.add(0, 0, categoriesAccordion, 6, 10, false);

        gridHandler.add(6, 9, corrigeerButton, 1, 1, false);
        gridHandler.add(7, 9, betaalButton, 1, 1, false);
        gridHandler.add(8, 9, bonnenButton, 1, 1, false);
        gridHandler.add(9, 9, kaartenButton, 1, 1, false);
        gridHandler.add(6, 8, new Text("Totaal: "), 1, 1, false);
        gridHandler.add(7, 8, totaalText, 3, 1, false);
        gridHandler.add(6, 0, bonTableView, 4, 8, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    public void recalculateTotal(){
        double total = 0D;
        for (int i = 0; i < receiptItems.size(); i++) {
            total += Double.parseDouble(receiptItems.get(i).getTotalPrice());
        }
        BigDecimal bigDecimal = new BigDecimal(total);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        totaalText.setText("\t\t" + bigDecimal.toPlainString() + "€");
    }

    public void addToReceipt(int passedId) {
        boolean alreadyAdded = false;
        for (int i = 0; i < receiptItems.size(); i++) {
            if (receiptItems.get(i).getId() == passedId) {
                alreadyAdded = true;
            }
        }

        if (!alreadyAdded) {
            Item item = database.getItem(passedId);
            receiptItems.add(new ReceiptItem(item.getId(), 1, item.getName(), Double.parseDouble(item.getPrice())));
        }else{
            for (int i = 0; i < receiptItems.size(); i++) {
                if (receiptItems.get(i).getId() == passedId) {
                    receiptItems.get(i).setAmount(receiptItems.get(i).getAmount()+1);
                    receiptItems.get(i).recaltulateTotal();
                }
            }
        }
        recalculateTotal();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void reload() {
        //Should do n othing
    }
}
