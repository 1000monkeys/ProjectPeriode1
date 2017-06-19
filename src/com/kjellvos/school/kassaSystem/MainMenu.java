package com.kjellvos.school.kassaSystem;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import com.kjellvos.school.kassaSystem.common.database.*;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import com.kjellvos.school.kassaSystem.database.Database;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by kjell on 10-3-2017.
 */
public class MainMenu extends MainScene implements SceneImplementation {
    private Database database;

    private Scene scene;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");

    private Button kaartenButton, betaalButton;
    private Accordion categoriesAccordion;
    private TableView bonTableView, customerCardTableView, bonnenTableView;
    private TableColumn idTableColumn, firstNameColumn, lastNameColumn, streetNameColumn, moreInfoTableColumn, nameTableColumn, singlePriceTableColumn, amountTableColumn, totalPriceTableColumn, timeTableColumn;
    private Text totalText;

    private GridHandler gridHandler;

    private ObservableList<ReceiptItem> receiptItems;
    private Dialog<Pair<String, String>> dialog;

    private CustomerCard customerCard = null;

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

        kaartenButton = new Button("Kaarten");
        kaartenButton.setOnMouseClicked(event -> {
            selectCard();
        });
        betaalButton = new Button("Betaal");
        betaalButton.setOnMouseClicked(event -> {
            if (receiptItems.size() > 0) {
                makeReceipt();
            }else{
                showNoItems();
            }
        });

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

        totalText = new Text("\t\t0.00€");

        bonTableView.getColumns().addAll(nameTableColumn, singlePriceTableColumn, amountTableColumn, totalPriceTableColumn);
        bonTableView.setItems(receiptItems);

        gridHandler.add(0, 0, categoriesAccordion, 6, 10, false);

        gridHandler.add(6, 9, betaalButton, 2, 1, false);
        gridHandler.add(8, 9, kaartenButton, 2, 1, false);
        gridHandler.add(6, 8, new Text("Totaal: "), 1, 1, false);
        gridHandler.add(7, 8, totalText, 3, 1, false);
        gridHandler.add(6, 0, bonTableView, 4, 8, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    private void showBonnen() {
        dialog = new Dialog<>();
        dialog.setTitle("Klantenkaart selecteren");
        dialog.setHeaderText("Welke klant?");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        bonnenTableView = new TableView();
        bonnenTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idTableColumn = new TableColumn("ID");
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Receipt, Integer>("id"));

        firstNameColumn = new TableColumn("Voornaam");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Receipt, String>("firstName"));

        lastNameColumn = new TableColumn("Achternaam");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Receipt, String>("lastName"));

        timeTableColumn = new TableColumn("Time");
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<Receipt, String>("whenReceipt"));

        bonnenTableView.setItems(database.getCustomersList());
        bonnenTableView.getColumns().addAll(idTableColumn, firstNameColumn, lastNameColumn, timeTableColumn);

        bonnenTableView.setItems(database.getReceipts());

        gridPane.add(bonnenTableView, 0, 0);
        dialog.setResultConverter(dialogButton -> {
            return null;
        });

        dialog.getDialogPane().setContent(gridPane);

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

    private void selectCard() {
        dialog = new Dialog<>();
        dialog.setTitle("Klantenkaart selecteren");
        dialog.setHeaderText("Welke klant?");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        //gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(new Label("Naam:"), 0, 0);
        TextField filterField = new TextField("Zoeken op datum");
        gridPane.add(filterField, 1, 0);

        customerCardTableView = new TableView();
        customerCardTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idTableColumn = new TableColumn("ID");
        idTableColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, Integer>("id"));

        firstNameColumn = new TableColumn("Voornaam");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, String>("firstName"));

        lastNameColumn = new TableColumn("Achternaam");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, String>("lastName"));

        streetNameColumn = new TableColumn("Straat naam");
        streetNameColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, String>("streetName"));

        moreInfoTableColumn = new TableColumn("Selecteren");
        moreInfoTableColumn.setCellValueFactory(new PropertyValueFactory<CustomerCard, Button>("button"));

        customerCardTableView.setItems(database.getCustomersList());
        customerCardTableView.getColumns().addAll(idTableColumn, firstNameColumn, lastNameColumn, streetNameColumn, moreInfoTableColumn);

        FilteredList<CustomerCard> filteredData = new FilteredList<>(database.getCustomersList(), p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customerCard -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (customerCard.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (customerCard.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<CustomerCard> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(customerCardTableView.comparatorProperty());

        customerCardTableView.setItems(sortedData);

        gridPane.add(customerCardTableView, 0, 1, 2, 5);
        dialog.setResultConverter(dialogButton -> {
            return null;
        });

        dialog.getDialogPane().setContent(gridPane);

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

    private void makeReceipt() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Receipt");
        dialog.setHeaderText("Your receipt.");

        ButtonType OkButtonType = new ButtonType("Betaal", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(OkButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(25);
        gridPane.setVgap(25);
        //gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(new Label("Items"), 0, 0, 4, 1);
        int i = 0;
        gridPane.add(new Label("Naam:"), 0, 1);
        gridPane.add(new Label("Prijs:"), 1, 1);
        gridPane.add(new Label("Aantal:"), 2, 1);
        gridPane.add(new Label("Totaal:"), 3, 1);
        while (i < receiptItems.size()){
            gridPane.add(new Label(receiptItems.get(i).getName()), 0, i+2);
            gridPane.add(new Label(Double.toString(receiptItems.get(i).getPrice())), 1, i+2);
            gridPane.add(new Label(Integer.toString(receiptItems.get(i).getAmount())), 2, i+2);
            gridPane.add(new Label(receiptItems.get(i).getTotalPrice()), 3, i+2);
            i++;
        }

        gridPane.add(new Label("Totaal:"), 0, i+2, 2, 1);
        gridPane.add(new Label(recalculateTotal()),2, i+2, 2, 1);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == OkButtonType) {
                if (customerCard != null) {
                    database.uploadReceipt(receiptItems, customerCard.getId());
                }else{
                    database.uploadReceipt(receiptItems, 0);
                }
                receiptItems.clear();
                customerCard = null;
            }
            return null;
        });

        dialog.getDialogPane().setContent(gridPane);

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

    public String recalculateTotal(){
        double total = 0D;
        for (int i = 0; i < receiptItems.size(); i++) {
            total += Double.parseDouble(receiptItems.get(i).getTotalPrice());
        }
        BigDecimal bigDecimal = new BigDecimal(total);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        totalText.setText("\t\t" + bigDecimal.toPlainString() + "€");
        return bigDecimal.toPlainString();
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

    public void setCustomerCard(CustomerCard customerCard){
        this.customerCard = customerCard;
        dialog.close();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void reload() {
        //Should do nothing
    }

    private void showNoItems() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("0 items!");
        alert.setHeaderText("U kunt niet afrekenen u heeft 0 items!");
        alert.setContentText("U moet echt meer dan 0 items kopen om af te rekenen!");
        alert.showAndWait();
    }
}
