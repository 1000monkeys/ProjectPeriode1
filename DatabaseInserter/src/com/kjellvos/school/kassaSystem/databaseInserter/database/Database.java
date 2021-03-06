package com.kjellvos.school.kassaSystem.databaseInserter.database;

import com.kjellvos.school.kassaSystem.common.Extensions.DatabaseExt;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.CustomerCard;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.database.Price;
import com.kjellvos.school.kassaSystem.databaseInserter.MainMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Created by kjevo on 3/24/17.
 */
public class Database extends DatabaseExt {
    MainMenu mainMenu;

    public static final String noCategory = "Geen categorie.";

    public Database(MainMenu mainMenu) throws SQLException{
        super();
        this.mainMenu = mainMenu;
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void newItemUpload(String name, String description, float price, String categorie, File image){
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            int categorieId = 0 ;
            String sql = "SELECT ID FROM Categories WHERE Name=?;";

            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, categorie);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    categorieId = resultSet.getInt("ID");
                }
            }else{
                showOopsAlert();
                System.out.println("Error 14");
            }

            FileInputStream fileInputStream = new FileInputStream(image);
            sql =   "BEGIN;\n" +
                    "   INSERT INTO Items SET Name=?, Description=?;\n" +
                    "   SET @ItemsId = LAST_INSERT_ID();\n" +
                    "   INSERT INTO DefaultPrices SET ItemsID=@ItemsId, Price=?;\n" +
                    "   INSERT INTO ItemsImages SET ItemsId=@ItemsId, Image=?;\n" +
                    "   INSERT INTO CategorieItems SET ItemsId=@ItemsId, CategorieId=?;\n" +
                    "COMMIT;\n";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, name);
            super.getPreparedStatement().setString(2, description);
            super.getPreparedStatement().setFloat(3, price);
            super.getPreparedStatement().setBlob(4, fileInputStream);
            super.getPreparedStatement().setInt(5, categorieId);
            super.getPreparedStatement().addBatch();

            int[] insertedId = new int[1];
            insertedId[0] = -3;
            try {
                insertedId = super.getPreparedStatement().executeBatch();
            }catch (BatchUpdateException e){
                e.printStackTrace();
                int i = 0;
                while (i < insertedId.length){
                    if (insertedId[i] == Statement.EXECUTE_FAILED) {
                        System.out.println("Error 13!");
                        showOopsAlert();
                    }
                    i++;
                }
            }finally {
                super.getConnection().close();
                super.getPreparedStatement().close();
                showSuccesfullyUploaded();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newTemporaryPriceUpload(int id, LocalDateTime from, LocalDateTime till, float price){
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            Timestamp fromTimestamp = Timestamp.valueOf(from);
            Timestamp tillTimeStamp = Timestamp.valueOf(till);

            String sql = "INSERT INTO Prices SET ItemsID=?, FromWhen=?, TillWhen=?, Price=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, id);
            super.getPreparedStatement().setTimestamp(2, fromTimestamp);
            super.getPreparedStatement().setTimestamp(3, tillTimeStamp);
            super.getPreparedStatement().setFloat(4, price);
            super.getPreparedStatement().addBatch();

            //TODO figure out way to make for sure it's inserted
            mainMenu.getCurrentScene().reload();
            int[] insertedId = new int[1];
            insertedId[0] = -3;
            try {
                insertedId = super.getPreparedStatement().executeBatch();
            }catch (BatchUpdateException e){
                e.printStackTrace();
                int i = 0;
                while (i < insertedId.length){
                    if (insertedId[i] == Statement.EXECUTE_FAILED) {
                        System.out.println("Error 12! Failed item's id: " + id);
                        showOopsAlert();
                    }
                    i++;
                }
            }finally {
                super.getConnection().close();
                super.getPreparedStatement().close();
                showSuccesfullyUploaded();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void updateItem(int id, String name, String description, String price, String categorie, File image) {
        try {
            super.setConnection(super.getBasicDataSource().getConnection());
            String sql = "SELECT ID FROM Categories WHERE Name=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, categorie);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if(resultSet.next()){
                int categorieId = resultSet.getInt("ID");
                sql =   "BEGIN;\n" +
                        "   UPDATE Items SET Name=?, Description=? WHERE ID=?;" +
                        "   UPDATE DefaultPrices SET Price=? WHERE ItemsID=?;";

                FileInputStream fileInputStream = null;
                if (image != null) {
                    fileInputStream = new FileInputStream(image);
                    sql +=      "   UPDATE ItemsImages SET Image=? WHERE ItemsID=?";
                }
                sql +=      "   UPDATE CategorieItems SET CategorieId=? WHERE ItemsId=?;";
                sql +=      "COMMIT;";

                super.setPreparedStatement(super.getConnection().prepareStatement(sql));
                super.getPreparedStatement().setString(1, name);
                super.getPreparedStatement().setString(2, description);
                super.getPreparedStatement().setInt(3, id);
                super.getPreparedStatement().setString(4, price);
                super.getPreparedStatement().setInt(5, id);
                if (image != null && fileInputStream != null) {
                    super.getPreparedStatement().setBlob(6, fileInputStream);
                    super.getPreparedStatement().setInt(7, id);
                }
                if (image == null) {
                    super.getPreparedStatement().setInt(6, categorieId);
                    super.getPreparedStatement().setInt(7, id);
                }else if (image != null) {
                    super.getPreparedStatement().setInt(8, categorieId);
                    super.getPreparedStatement().setInt(9, id);
                }
                super.getPreparedStatement().addBatch();

                int[] insertedId = new int[1];
                insertedId[0] = -3;
                try {
                    insertedId = super.getPreparedStatement().executeBatch();
                }catch (BatchUpdateException e){
                    e.printStackTrace();
                    int i = 0;
                    while (i < insertedId.length){
                        if (insertedId[i] == Statement.EXECUTE_FAILED) {
                            System.out.println("Error 11! Failed item's id: " + id);
                            showOopsAlert();
                        }
                        i++;
                    }
                }finally {
                    super.getConnection().close();
                    super.getPreparedStatement().close();
                    showSuccesfullyUpdated();
                }
            }else{
                showOopsAlert();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Item getItemInfo(int passedId){
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql =    "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName, ItemsImages.Image FROM Items \n" +
                    "   LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID\n" +
                    "   LEFT JOIN ItemsImages ON Items.ID = ItemsImages.ItemsId\n" +
                    "   LEFT JOIN CategorieItems ON CategorieItems.ItemsId=?\n" +
                    "   LEFT JOIN Categories ON Categories.ID=CategorieItems.CategorieId\n" +
                    "WHERE Items.ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, passedId);
            super.getPreparedStatement().setInt(2, passedId);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String description = resultSet.getString("Description");
                    String price = resultSet.getString("Price");
                    String categorie = resultSet.getString("CName");
                    Blob imageBlob = resultSet.getBlob("Image");
                    InputStream imageInputStream = imageBlob.getBinaryStream();
                    Image image = new Image(imageInputStream);

                    return new Item(id, name, description, price, categorie, image);
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @SuppressWarnings("Duplicates")
    public ObservableList getItemsList() {
        ObservableList<Item> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName FROM Items LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID LEFT JOIN CategorieItems ON CategorieItems.ItemsId = Items.ID LEFT JOIN Categories ON Categories.ID = CategorieItems.CategorieId;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String description = resultSet.getString("Description");
                    String price = resultSet.getString("Price");
                    String categorie = resultSet.getString("CName");


                    Button button = new Button("Meer info/editen");
                    button.setOnMouseClicked(event -> {
                        mainMenu.changeScene(mainMenu.getOverviewItem(id));
                    });

                    data.add(new Item(id, name, description, price, categorie, button));
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }


    @SuppressWarnings("Duplicates")
    public ObservableList getItemsList(int categorieId) {
        ObservableList<Item> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName FROM Items LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID LEFT JOIN CategorieItems ON CategorieItems.ItemsId = Items.ID LEFT JOIN Categories ON Categories.ID = CategorieItems.CategorieId WHERE Categories.ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, categorieId);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String description = resultSet.getString("Description");
                    String price = resultSet.getString("Price");

                    Button button = new Button("Meer info/editen");
                    button.setOnMouseClicked(event -> {
                        mainMenu.changeScene(mainMenu.getOverviewItem(id));
                    });

                    data.add(new Item(id, name, description, price, button));
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public ObservableList getPricesOfItem(int passedId){
        ObservableList<Price> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT Prices.ID, Prices.FromWhen, Prices.TillWhen, Prices.Price FROM Prices WHERE Prices.ItemsID=? ORDER BY Prices.FromWhen ASC, Prices.TillWhen ASC;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, passedId);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                Timestamp fromWhenTimestamp = resultSet.getTimestamp("FromWhen");
                LocalDateTime fromWhen = null;
                if (fromWhenTimestamp != null) {
                    fromWhen = fromWhenTimestamp.toLocalDateTime();
                }
                Timestamp tillWhenTimestamp = resultSet.getTimestamp("TillWhen");
                LocalDateTime tillWhen = null;
                if (tillWhenTimestamp != null) {
                    tillWhen = tillWhenTimestamp.toLocalDateTime();
                }
                String price = resultSet.getString("Price");

                data.add(new Price(id, fromWhen, tillWhen, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * Geen problemen stuurt true
     * @param id
     * @param fromDateTime
     * @param tillDateTime
     * @return
     */
    public boolean checkNewTemporaryPriceUpload(int id, LocalDateTime fromDateTime, LocalDateTime tillDateTime) {
        try {
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT Prices.FromWhen, Prices.TillWhen FROM Prices WHERE ItemsID=? AND Prices.TillWhen > NOW();";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, id);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            while (resultSet.next()) {
                Timestamp fromWhenTimestamp = resultSet.getTimestamp("FromWhen");
                LocalDateTime fromWhen = null;
                if (fromWhenTimestamp != null) {
                    fromWhen = fromWhenTimestamp.toLocalDateTime();
                }
                Timestamp tillWhenTimestamp = resultSet.getTimestamp("TillWhen");
                LocalDateTime tillWhen = null;
                if (tillWhenTimestamp != null) {
                    tillWhen = tillWhenTimestamp.toLocalDateTime();
                }
                if (tillWhen != null && fromWhen != null) {
                    if (!fromDateTime.isAfter(tillWhen)) {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public int updateTemporaryPrice(int itemsId, int pricesId, Timestamp tillwhen){
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "UPDATE Prices SET TillWhen=? WHERE ItemsID=? AND ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setTimestamp(1, tillwhen);
            super.getPreparedStatement().setInt(2, itemsId);
            super.getPreparedStatement().setInt(3, pricesId);
            return super.getPreparedStatement().executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void deleteTemporaryPrice(int itemsId, int pricesId) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT Prices.FromWhen, Prices.TillWhen FROM Prices WHERE ItemsID=? AND Prices.ID=?";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, itemsId);
            super.getPreparedStatement().setInt(2, pricesId);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();

            while (resultSet.next()) {
                Timestamp fromWhen = resultSet.getTimestamp("FromWhen");

                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                //check if it is already active
                if (now.before(fromWhen)) {
                    //no problem go ahead and delete
                    sql = "DELETE FROM Prices WHERE ItemsID=? AND Prices.ID=?";
                    super.setPreparedStatement(super.getConnection().prepareStatement(sql));
                    super.getPreparedStatement().setInt(1, itemsId);
                    super.getPreparedStatement().setInt(2, pricesId);
                    int amountOfDeletions = super.getPreparedStatement().executeUpdate();

                    if (amountOfDeletions == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Verwijderd!");
                        alert.setHeaderText("Succesvol verwijderd!");
                        alert.setContentText("De tijdelijke prijs is succesvol verwijderd!");
                        alert.showAndWait();
                    }else if (amountOfDeletions == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Er ging iets fout!");
                        alert.setContentText("Het item kon niet uit de database verwijderd worden!");
                        alert.showAndWait();
                    }else{
                        showOopsAlert();
                        System.out.println("Error 7");
                    }
                }else{
                    //problem cannot delete price because some people might have already bought this item at this price.
                    //So update the old price to stop now
                    Timestamp tillWhen = now;
                    int amountOfUpdatedRows = updateTemporaryPrice(itemsId, pricesId, tillWhen);

                    if (amountOfUpdatedRows == 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Niet aangepast!");
                        alert.setHeaderText("Onsuccesvol aangepast!");
                        alert.setContentText("Omdat de prijs zijn begin datum en tijd voor nu zijn, kunnen we hem niet verwijderen i.v.m. de mogelijkheid dat iemand dit product al voor deze prijs gekocht heeft! Maar dit is mislukt! Er ging iets mis tussen de java applicatie en de database!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                        alert.showAndWait();
                    }else if(amountOfUpdatedRows == 1){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Aangepast!");
                        alert.setHeaderText("Succesvol aangepast!");
                        alert.setContentText("Omdat de prijs zijn begin datum en tijd voor nu zijn, kunnen we hem niet verwijderen i.v.m. de mogelijkheid dat iemand dit product al voor deze prijs gekocht heeft!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                        alert.showAndWait();
                    }else{
                        showOopsAlert();
                        System.out.println("Error 6");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void newCategorieUpload(String categorieName) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT * FROM Categories WHERE Name=?";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, categorieName);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (!resultSet.next()){
                sql = "INSERT INTO Categories SET Name=?;";
                super.setPreparedStatement(super.getConnection().prepareStatement(sql));
                super.getPreparedStatement().setString(1, categorieName);
                int amountOfRowsInserted = super.getPreparedStatement().executeUpdate();
                if (amountOfRowsInserted == 1) {
                    showSuccesfullyUploaded();
                }else{
                    showOopsAlert();
                    System.out.println("Error 5");
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bestaat al!");
                alert.setHeaderText("Deze categorie bestaat al!");
                alert.setContentText("De categorie was niet geupload omdat hij al bestaat!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList getCategorieList(){
        ObservableList<Categorie> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT * FROM Categories;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");

                    Button button = new Button("Meer info/editen");
                    button.setOnMouseClicked(event -> {
                        mainMenu.changeScene(mainMenu.getOverviewCategorie(id));
                    });

                    data.add(new Categorie(id, name, button));
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 3");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public ObservableList getCategorieNamesList(){
        ObservableList<String> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT Name FROM Categories;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    String name = resultSet.getString("Name");

                    data.add(name);
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 4");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public void deleteItem(int itemsId) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "DELETE FROM Items WHERE ID=?";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, itemsId);
            int amountOfDeletions = super.getPreparedStatement().executeUpdate();

            if (amountOfDeletions == 1) {
                showSuccesfullyDeleted();
            }else{
                System.out.println("Error 15");
                showOopsAlert();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteCategorie(int categorieId){
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT ID FROM Categories WHERE Name=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, noCategory);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            int key = 0;
            if (!resultSet.next()) {
                sql = "INSERT INTO Categories SET Name=?;";
                super.setPreparedStatement(super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS));
                super.getPreparedStatement().setString(1, noCategory);
                key = super.getPreparedStatement().executeUpdate();
            }else{
                resultSet.beforeFirst();
                while (resultSet.next()){
                    key = resultSet.getInt("ID");
                }
            }

            System.out.println(key);

            if (key != 0) {
                if (key != categorieId) {
                    sql = "UPDATE CategorieItems SET CategorieId=? WHERE CategorieId=?;";
                    super.setPreparedStatement(super.getConnection().prepareStatement(sql));
                    super.getPreparedStatement().setInt(1, key);
                    super.getPreparedStatement().setInt(2, categorieId);
                    super.getPreparedStatement().executeUpdate();

                    sql = "DELETE FROM Categories WHERE ID=?;";
                    super.setPreparedStatement(super.getConnection().prepareStatement(sql));
                    super.getPreparedStatement().setInt(1, categorieId);
                    int amountOfDeletions = super.getPreparedStatement().executeUpdate();
                    if (amountOfDeletions == 1) {
                        showSuccesfullyDeleted();
                    } else {
                        System.out.println("Error 16");
                        showOopsAlert();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Dat mag niet!");
                    alert.setHeaderText("U mag deze categorie niet verwijderen.");
                    alert.setContentText("U mag deze categorie niet verwijderen, Dit ivm als een item een categorie had en u verwijderd die categorie hij deze hoort te krijgen.");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                    alert.showAndWait();
                }
            }else{
                showOopsAlert();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList getCustomersList() {
        ObservableList<CustomerCard> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "SELECT ID, FirstName, LastName, StreetName FROM CustomerCards;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String streetName = resultSet.getString("StreetName");

                    Button button = new Button("Meer info/editen");
                    button.setOnMouseClicked(event -> {
                        mainMenu.changeScene(mainMenu.getOverviewCustomer(id));
                    });

                    data.add(new CustomerCard(id, firstName, lastName, streetName, button));
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 17");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public void newCustomerCardUpload(String firstName, String lastName, String streetName, String houseNumber, String telephoneNumber) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "INSERT INTO CustomerCards SET FirstName=?, LastName=?, StreetName=?, HouseNumber=?, TelephoneNumber=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, firstName);
            super.getPreparedStatement().setString(2, lastName);
            super.getPreparedStatement().setString(3, streetName);
            super.getPreparedStatement().setInt(4, Integer.parseInt(houseNumber));
            super.getPreparedStatement().setString(5, telephoneNumber);

            int amountOfRowsInserted = super.getPreparedStatement().executeUpdate();
            if (amountOfRowsInserted == 1) {
                showSuccesfullyUploaded();
            }else{
                showOopsAlert();
                System.out.println("Error 18");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public CustomerCard getCustomerCardInfo(int passedId) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql =    "SELECT * FROM CustomerCards WHERE ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, passedId);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String streetName = resultSet.getString("StreetName");
                    int houseNumber = resultSet.getInt("HouseNumber");
                    String telephoneNumber = resultSet.getString("TelephoneNumber");

                    return new CustomerCard(id, firstName, lastName, streetName, houseNumber, telephoneNumber);
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 18");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void updateCustomerCard(String firstName, String lastName, String streetName, int houseNumber, String telephoneNumber, int id) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "UPDATE CustomerCards SET FirstName=?, LastName=?, StreetName=?, HouseNumber=?, TelephoneNumber=? WHERE ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, firstName);
            super.getPreparedStatement().setString(2, lastName);
            super.getPreparedStatement().setString(3, streetName);
            super.getPreparedStatement().setInt(4, houseNumber);
            super.getPreparedStatement().setString(5, telephoneNumber);
            super.getPreparedStatement().setInt(6, id);

            int amountOfRowsInserted = super.getPreparedStatement().executeUpdate();
            if (amountOfRowsInserted == 1) {
                showSuccesfullyUpdated();
            }else{
                showOopsAlert();
                System.out.println("Error 20");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateCategorie(int id, String name) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "UPDATE Categories SET Name=? WHERE ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setString(1, name);
            super.getPreparedStatement().setInt(2, id);

            int amountOfRowsInserted = super.getPreparedStatement().executeUpdate();
            if (amountOfRowsInserted == 1) {
                showSuccesfullyUpdated();
            }else{
                showOopsAlert();
                System.out.println("Error 21");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Categorie getCategorieInfo(int passedId){
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql =    "SELECT * FROM Categories WHERE ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, passedId);
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");

                    return new Categorie(id, name);
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 18");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void deleteCustomerCard(int id) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql = "DELETE FROM CustomerCards WHERE ID=?";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, id);
            int amountOfDeletions = super.getPreparedStatement().executeUpdate();

            if (amountOfDeletions == 1) {
                showSuccesfullyDeleted();
            }else{
                System.out.println("Error 22");
                showOopsAlert();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                super.getPreparedStatement().close();
                super.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSuccesfullyUpdated() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succesvol geupdate!");
        alert.setHeaderText("De waarden zijn succesvol aangepast!");
        alert.setContentText("Hij heeft nu de nieuwe waarden!");
        alert.showAndWait();
    }

    public void showOopsAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Whoops!");
        alert.setHeaderText("Er ging iets royaal mis!");
        alert.setContentText("Vraag uw administrator om hulp!");
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }

    public void showNoDataAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Whoops!");
        alert.setHeaderText("Er lijkt geen data te zijn!");
        alert.setContentText("Als er wel data hoort te zijn, vraag dan uw administrator om hulp!");
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }

    public void showSuccesfullyUploaded(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Geupload!");
        alert.setHeaderText("Succesvol geupload!");
        alert.setContentText("Het item is succesbvol geupload!");
        alert.showAndWait();
    }

    private void showSuccesfullyDeleted() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Verwijderd!");
        alert.setHeaderText("Succesvol verwijderd!");
        alert.setContentText("Het item is succesvol verwijderd!");
        alert.showAndWait();
    }
}
