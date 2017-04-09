package com.kjellvos.school.kassaSystem.database;

import com.kjellvos.school.kassaSystem.Main;
import com.kjellvos.school.kassaSystem.MainMenu;
import com.kjellvos.school.kassaSystem.common.Extensions.DatabaseExt;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.InputStream;
import java.sql.*;

/**
 * Created by kjevo on 3/29/17.
 */
@SuppressWarnings("Duplicates")
public class Database extends DatabaseExt{
    MainMenu mainMenu;

    public Database(MainMenu mainMenu) throws SQLException {
        super();
        this.mainMenu = mainMenu;
    }

    public ObservableList getItemsByCategorie(int passedId){
        ObservableList<Item> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql =    "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName, ItemsImages.Image FROM Items \n" +
                            "   LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID\n" +
                            "   LEFT JOIN ItemsImages ON Items.ID = ItemsImages.ItemsId\n" +
                            "   LEFT JOIN CategorieItems ON CategorieItems.ItemsId=Items.ID\n" +
                            "   LEFT JOIN Categories ON Categories.ID=CategorieItems.CategorieId\n" +
                            "   WHERE Categories.ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, passedId);
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

                    sql = "SELECT Price FROM Prices WHERE FromWhen > NOW() AND NOW() < TillWhen AND ItemsID=?;";
                    super.setPreparedStatement(super.getConnection().prepareStatement(sql));
                    super.getPreparedStatement().setInt(1, id);
                    ResultSet resultSet1 = super.getPreparedStatement().executeQuery();
                    while (resultSet1.next()){
                        price = resultSet1.getString("Price");
                    }

                    data.add(new Item(id, name, description, price, categorie, image));
                }
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

        return data;
    }

    public ObservableList getCategories() {
        ObservableList<Categorie> data = FXCollections.observableArrayList();
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql =    "SELECT * FROM Categories;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            ResultSet resultSet = super.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                resultSet.beforeFirst();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");

                    data.add(new Categorie(id, name));
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
        return data;
    }

    public Item getItem(int passedId) {
        try{
            super.setConnection(super.getBasicDataSource().getConnection());

            String sql =    "SELECT Items.ID, Items.Name, Items.Description, DefaultPrices.Price, Categories.Name AS CName, ItemsImages.Image FROM Items \n" +
                            "   LEFT JOIN DefaultPrices ON Items.ID = DefaultPrices.ItemsID\n" +
                            "   LEFT JOIN ItemsImages ON Items.ID = ItemsImages.ItemsId\n" +
                            "   LEFT JOIN CategorieItems ON CategorieItems.ItemsId=Items.ID\n" +
                            "   LEFT JOIN Categories ON Categories.ID=CategorieItems.CategorieId\n" +
                            "WHERE Items.ID=?;";
            super.setPreparedStatement(super.getConnection().prepareStatement(sql));
            super.getPreparedStatement().setInt(1, passedId);
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

    public void showNoDataAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Whoops!");
        alert.setHeaderText("Er lijkt geen data te zijn!");
        alert.setContentText("Als er wel data hoort te zijn, vraag dan uw administrator om hulp!");
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));

        alert.showAndWait();
    }
}