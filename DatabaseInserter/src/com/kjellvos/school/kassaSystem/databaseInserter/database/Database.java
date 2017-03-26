package com.kjellvos.school.kassaSystem.databaseInserter.database;

import com.kjellvos.school.kassaSystem.databaseInserter.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

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

    public void newItemUpload(String name, String description, float price, File image){
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            FileInputStream fileInputStream = new FileInputStream(image);
            String sql =    "BEGIN;\n" +
                            "  INSERT INTO Items SET Name=?, Description=?;\n" +
                            "  SET @ItemsId = LAST_INSERT_ID();\n" +
                            "  INSERT INTO Prices SET ItemsID=@ItemsId, Price=?, DefaultPrice=?;\n" +
                            "  INSERT INTO ItemsImages set ItemsId=@ItemsId, Image=?;\n" +
                            "COMMIT;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setFloat(3, price);
            preparedStatement.setBoolean(4, true);
            preparedStatement.setBlob(5, fileInputStream);
            preparedStatement.execute();
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

    //TODO checker inbouwen of voor die datum al een aangepaste prijs bestaat
    public void newTemporaryPriceUpload(int id, LocalDateTime from, LocalDateTime till, float price){
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            Timestamp fromTimestamp = Timestamp.valueOf(from);
            Timestamp tillTimeStamp = Timestamp.valueOf(till);

            String sql = "INSERT INTO Prices SET ItemsID=?, FromWhen=?, TillWhen=?, Price=?, DefaultPrice=FALSE;";
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

    public void itemUpdate(String name, String description, float price, File image){
        //TODO
    }

    public Item getItemInfo(int passedId){
        try{
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT Items.ID, Items.Name, Items.Description, Prices.Price, ItemsImages.Image FROM Items LEFT JOIN Prices ON Items.ID = Prices.ItemsID LEFT JOiN ItemsImages ON Items.ID = ItemsImages.ItemsId WHERE Prices.DefaultPrice=true AND Items.ID=?;";
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

            String sql = "SELECT Items.ID, Items.Name, Items.Description, Prices.Price FROM Items LEFT JOIN Prices ON Items.ID = Prices.ItemsID WHERE Prices.DefaultPrice=true;";
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
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
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
}
