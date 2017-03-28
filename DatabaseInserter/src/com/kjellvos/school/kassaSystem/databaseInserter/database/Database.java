package com.kjellvos.school.kassaSystem.databaseInserter.database;

import com.kjellvos.school.kassaSystem.databaseInserter.Main;
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
public class Database {
    Main main;
    Connection connection;
    PreparedStatement preparedStatement;

    static final String DB_URL = "jdbc:mariadb://localhost:3306/KassaSystem?allowMultiQueries=true";
    static final String USER = "KassaSystem";
    static final String PASS = "password123321";

    public Database(Main main) {
        this.main = main;
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public void newItemUpload(String name, String description, float price, File image){
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            FileInputStream fileInputStream = new FileInputStream(image);
            String sql =    "BEGIN;\n" +
                            "  INSERT INTO Items SET Name=?, Description=?;\n" +
                            "  SET @ItemsId = LAST_INSERT_ID();\n" +
                            "  INSERT INTO DefaultPrices SET ItemsID=@ItemsId, Price=?;\n" +
                            "  INSERT INTO ItemsImages set ItemsId=@ItemsId, Image=?;\n" +
                            "COMMIT;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setFloat(3, price);
            preparedStatement.setBlob(4, fileInputStream);
            preparedStatement.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Geupload!");
            alert.setHeaderText("Succesvol geupload!");
            alert.setContentText("Het item is succesbvol geupload!");
            alert.showAndWait();
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
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            Timestamp fromTimestamp = Timestamp.valueOf(from);
            Timestamp tillTimeStamp = Timestamp.valueOf(till);

            String sql = "INSERT INTO Prices SET ItemsID=?, FromWhen=?, TillWhen=?, Price=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, fromTimestamp);
            preparedStatement.setTimestamp(3, tillTimeStamp);
            preparedStatement.setFloat(4, price);

            preparedStatement.execute();
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
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

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


            preparedStatement.execute();
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
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, ItemsImages.Image FROM Items LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID LEFT JOIN ItemsImages ON Items.ID = ItemsImages.ItemsId WHERE Items.ID=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, passedId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                float price = resultSet.getFloat("Price");
                Blob imageBlob = resultSet.getBlob("Image");
                InputStream imageInputStream = imageBlob.getBinaryStream();
                Image image = new Image(imageInputStream);

                return new Item(id, name, description, price, image);
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
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price FROM Items LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID;";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                float price = resultSet.getFloat("Price");

                Button button = new Button("Meer info/editen");
                button.setOnMouseClicked(event -> {
                    main.changeScene(main.getOverviewItem(id));
                });

                data.add(new Item(id, name, description, price, button));
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
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT Prices.ID, Prices.FromWhen, Prices.TillWhen, Prices.Price FROM Prices WHERE Prices.ItemsID=? ORDER BY Prices.FromWhen ASC, Prices.TillWhen ASC;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, passedId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
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
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

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
                        System.out.println("1");
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

    public void updateTemporaryPrice(int itemsId, int pricesId, Timestamp tillwhen){
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "UPDATE Prices SET TillWhen=? WHERE ItemsID=? AND ID=?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, tillwhen);
            preparedStatement.setInt(2, itemsId);
            preparedStatement.setInt(3, pricesId);
            preparedStatement.executeUpdate();
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

    public void deleteTemporaryPrice(int itemsId, int pricesId) {
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

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
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, itemsId);
                    preparedStatement.setInt(2, pricesId);
                    preparedStatement.executeUpdate();


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Verwijderd!");
                    alert.setHeaderText("Succesvol verwijderd!");
                    alert.setContentText("De tijdelijke prijs is succesvol verwijderd!");
                    alert.showAndWait();
                    main.getScene().reload();
                }else{
                    //problem cannot delete price because some people might have already bought this item at this price.
                    //So update the old price to stop now
                    Timestamp tillWhen = now;
                    updateTemporaryPrice(itemsId, pricesId, tillWhen);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Aangepast!");
                    alert.setHeaderText("Succesvol aangepast!");
                    alert.setContentText("Omdat de prijs zijn begin datum en tijd voor nu zijn, kunnen we hem niet verwijderen i.v.m. de mogelijkheid dat iemand dit product al voor deze prijs gekocht heeft!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

                    alert.showAndWait();
                    main.getScene().reload();
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
}
