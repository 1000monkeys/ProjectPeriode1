package com.kjellvos.school.kassaSystem.databaseInserter.database;

import com.kjellvos.school.kassaSystem.databaseInserter.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Created by kjevo on 3/24/17.
 */
public class Database {
    Main main;
    BasicDataSource basicDataSource;
    Connection connection;
    PreparedStatement preparedStatement;

    public Database(Main main) throws SQLException, PropertyVetoException, SQLException, IOException {
        this.main = main;

        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        System.setProperty(Context.PROVIDER_URL, "file:///tmp");
        InitialContext ic = null;
        try {
            ic = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername("KassaSystem");
        basicDataSource.setPassword("password123321");
        basicDataSource.setUrl("jdbc:mysql://213.154.224.189/KassaSystem");

        connection = basicDataSource.getConnection();
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void newItemUpload(String name, String description, float price, String categorie, File image){
        try{
            connection = basicDataSource.getConnection();

            int categorieId = 0 ;
            String sql = "SELECT ID FROM Categories WHERE Name=?;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categorie);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                categorieId = resultSet.getInt("ID");
            }

            FileInputStream fileInputStream = new FileInputStream(image);
            sql =   "BEGIN;\n" +
                    "   INSERT INTO Items SET Name=?, Description=?;\n" +
                    "   SET @ItemsId = LAST_INSERT_ID();\n" +
                    "   INSERT INTO DefaultPrices SET ItemsID=@ItemsId, Price=?;\n" +
                    "   INSERT INTO ItemsImages set ItemsId=@ItemsId, Image=?;\n" +
                    "   INSERT INTO CategorieItems SET ItemsId=@ItemsId, CategorieId=?;\n" +
                    "COMMIT;\n";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setFloat(3, price);
            preparedStatement.setBlob(4, fileInputStream);
            preparedStatement.setInt(5, categorieId);
            preparedStatement.executeUpdate();
            //TODO figure out way to make sure it's inserted.
            //if () {
                showSuccesfullyUploaded();
            //}else{
            //    showOopsAlert();
            //    System.out.println("Error 10");
            //}
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void newTemporaryPriceUpload(int id, LocalDateTime from, LocalDateTime till, float price){
        try{
            connection = basicDataSource.getConnection();

            Timestamp fromTimestamp = Timestamp.valueOf(from);
            Timestamp tillTimeStamp = Timestamp.valueOf(till);

            String sql = "INSERT INTO Prices SET ItemsID=?, FromWhen=?, TillWhen=?, Price=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, fromTimestamp);
            preparedStatement.setTimestamp(3, tillTimeStamp);
            preparedStatement.setFloat(4, price);

            boolean inserted = preparedStatement.execute();

            //TODO figure out way to make for sure it's inserted
            //if(inserted){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ingevoerd!");
                alert.setHeaderText("Succesvol ingevoerd!");
                alert.setContentText("De waarden zijn succesvol ingevoerd!");
                alert.showAndWait();
                main.getScene().reload();
            //}else{
            //   showOopsAlert();
            //    System.out.println("Error 9");
            //}
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void itemUpdate(int id, String name, String description, float price, File image){
        try{
            connection = basicDataSource.getConnection();

            if (image != null) {
                FileInputStream fileInputStream = new FileInputStream(image);
                String sql =    "BEGIN;\n" +
                                "   UPDATE Items SET Name=?, Description=? WHERE ID=?;" +
                                "   UPDATE DefaultPrices SET Price=? WHERE ItemsID=?;" +
                                "   UPDATE ItemsImages SET Image=? WHERE ItemsID=?" +
                                "COMMIT;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, id);
                preparedStatement.setFloat(4, price);
                preparedStatement.setInt(5, id);
                preparedStatement.setBlob(6, fileInputStream);
                preparedStatement.setInt(7, id);
            }else {
                String sql =    "BEGIN;\n" +
                                "   UPDATE Items SET Name=?, Description=? WHERE ID=?;" +
                                "   UPDATE DefaultPrices SET Price=? WHERE ItemsID=?;" +
                                "COMMIT;";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, id);
                preparedStatement.setFloat(4, price);
                preparedStatement.setInt(5, id);
            }


            boolean updated = preparedStatement.execute();
            if (updated) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Succesvol geupdate!");
                alert.setHeaderText("De waarden zijn succesvol aangepast!");
                alert.setContentText("Het item heeft nu de nieuwe waarden!");
                alert.showAndWait();
                main.getScene().reload();
            }else{
                showOopsAlert();
                System.out.println("Error 8");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Item getItemInfo(int passedId){
        try{
            connection = basicDataSource.getConnection();

            String sql =    "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName, ItemsImages.Image FROM Items \n" +
                            "   LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID\n" +
                            "   LEFT JOIN ItemsImages ON Items.ID = ItemsImages.ItemsId\n" +
                            "   LEFT JOIN CategorieItems ON CategorieItems.ItemsId=?\n" +
                            "   LEFT JOIN Categories ON Categories.ID=CategorieItems.CategorieId\n" +
                            "WHERE Items.ID=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, passedId);
            preparedStatement.setInt(2, passedId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String description = resultSet.getString("Description");
                    float price = resultSet.getFloat("Price");
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
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public ObservableList getItemsList() {
        ObservableList<Item> data = FXCollections.observableArrayList();
        try{
            connection = basicDataSource.getConnection();

            String sql = "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName FROM Items LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID LEFT JOIN CategorieItems ON CategorieItems.ItemsId = Items.ID LEFT JOIN Categories ON Categories.ID = CategorieItems.CategorieId;";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String description = resultSet.getString("Description");
                    float price = resultSet.getFloat("Price");
                    String categorie = resultSet.getString("CName");


                    Button button = new Button("Meer info/editen");
                    button.setOnMouseClicked(event -> {
                        main.changeScene(main.getOverviewItem(id));
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
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public ObservableList getPricesOfItem(int passedId){
        ObservableList<Price> data = FXCollections.observableArrayList();
        try{
            connection = basicDataSource.getConnection();

            String sql = "SELECT Prices.ID, Prices.FromWhen, Prices.TillWhen, Prices.Price FROM Prices WHERE Prices.ItemsID=? ORDER BY Prices.FromWhen ASC, Prices.TillWhen ASC;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, passedId);
            ResultSet resultSet = preparedStatement.executeQuery();;
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
                float price = resultSet.getFloat("Price");

                data.add(new Price(id, fromWhen, tillWhen, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                preparedStatement.close();
                connection.close();
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
            connection = basicDataSource.getConnection();

            String sql = "SELECT Prices.FromWhen, Prices.TillWhen FROM Prices WHERE ItemsID=? AND Prices.TillWhen > NOW();";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
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
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public int updateTemporaryPrice(int itemsId, int pricesId, Timestamp tillwhen){
        try{
            connection = basicDataSource.getConnection();

            String sql = "UPDATE Prices SET TillWhen=? WHERE ItemsID=? AND ID=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, tillwhen);
            preparedStatement.setInt(2, itemsId);
            preparedStatement.setInt(3, pricesId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void deleteTemporaryPrice(int itemsId, int pricesId) {
        try{
            connection = basicDataSource.getConnection();

            String sql = "SELECT Prices.FromWhen, Prices.TillWhen FROM Prices WHERE ItemsID=? AND Prices.ID=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, itemsId);
            preparedStatement.setInt(2, pricesId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Timestamp fromWhen = resultSet.getTimestamp("FromWhen");

                Timestamp now = Timestamp.valueOf(LocalDateTime.now());

                //check if it is already active
                if (now.before(fromWhen)) {
                    //no problem go ahead and delete
                    sql = "DELETE FROM Prices WHERE ItemsID=? AND Prices.ID=?";
                    preparedStatement = connection.prepareStatement(sql );
                    preparedStatement.setInt(1, itemsId);
                    preparedStatement.setInt(2, pricesId);
                    int amountOfDeletions = preparedStatement.executeUpdate();

                    if (amountOfDeletions == 1) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Verwijderd!");
                        alert.setHeaderText("Succesvol verwijderd!");
                        alert.setContentText("De tijdelijke prijs is succesvol verwijderd!");
                        alert.showAndWait();
                        main.getScene().reload();
                    }else if (amountOfDeletions == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Er ging iets fout!");
                        alert.setContentText("Het item kon niet uit de database verwijderd worden!");
                        alert.showAndWait();
                        main.getScene().reload();
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
                        alert.setContentText("Omdat de prijs zijn begin datum en tijd voor nu zijn, kunnen we hem niet verwijderen i.v.m. de mogelijkheid dat iemand dit product al voor deze prijs gekocht heeft! Maar dit is mislukt! Er ging iets mis tussen de java applicate en de database!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                        alert.showAndWait();
                        main.getScene().reload();
                    }else if(amountOfUpdatedRows == 1){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Aangepast!");
                        alert.setHeaderText("Succesvol aangepast!");
                        alert.setContentText("Omdat de prijs zijn begin datum en tijd voor nu zijn, kunnen we hem niet verwijderen i.v.m. de mogelijkheid dat iemand dit product al voor deze prijs gekocht heeft!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                        alert.showAndWait();
                        main.getScene().reload();
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
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void newCategorieUpload(String categorieName) {
        try{
            connection = basicDataSource.getConnection();

            String sql = "INSERT INTO Categories SET Name=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categorieName);
            int amountOfRowsInserted = preparedStatement.executeUpdate();
            if (amountOfRowsInserted == 1) {
                showSuccesfullyUploaded();
            }else{
                showOopsAlert();
                System.out.println("Error 5");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList getCategorieList(){
        ObservableList<Categorie> data = FXCollections.observableArrayList();
        try{
            connection = basicDataSource.getConnection();

            String sql = "SELECT * FROM Categories;";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");

                    data.add(new Categorie(id, name));
                }
            }else{
                showNoDataAlert();
                System.out.println("Error 3");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public ObservableList getCategorieNamesList(){
        ObservableList<String> data = FXCollections.observableArrayList();
        try{
            connection = basicDataSource.getConnection();

            String sql = "SELECT * FROM Categories;";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
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
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public void showOopsAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Whoops!");
        alert.setHeaderText("Er ging iets royaal mis!");
        alert.setContentText("Vraag uw administrator om hulp!");
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }

    public void showNoDataAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
}
