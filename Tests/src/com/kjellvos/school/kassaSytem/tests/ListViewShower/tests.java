package com.kjellvos.school.kassaSytem.tests.ListViewShower;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apac

import java.util.ArrayList;

/**
 * Created by kjell on 11-3-2017.
 */
public class tests extends Application{
    Pane pane;
    HBox hBox, hBoxAmount;
    VBox vBox, vBoxItem;

    //HBox en VBox gebruiken??
    @Override
    public void start(Stage primaryStage) throws Exception {
        ArrayList items = new ArrayList();
        items.add(new Item(0, 12.99F, "1Kg of pure poop"));
        items.add(new Item(1, 25.87F, "i don't know"));

        pane = new Pane();


        vBox = new VBox();
        vBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        double width = 300D, height = 100D;
        vBox.setPrefSize(width, height);
        for (int i = 0; i < items.size(); i++){
            hBox = new HBox();
            vBoxItem = new VBox();
            vBoxItem.setAlignment(Pos.CENTER_LEFT);
            hBoxAmount = new HBox();
            hBoxAmount.setAlignment(Pos.CENTER);

            Text id = new Text((Integer.toString(((Item)items.get(i)).getId())));
            Text productName = new Text(((Item)items.get(i)).getProductName());
            Text price = new Text("\u20AC" + (Float.toString(((Item)items.get(i)).getPrice())));
            Text amount = new Text("Amount:");
            Button plus = new Button("+");
            Button minus = new Button("-");
            TextField textField = new TextField();
            ImageView imageView = new ImageView();
            imageView.setImage(new Image("file:/home/kjevo/Pictures/Wallpapers/Wallpaper.png"));
            imageView.setFitHeight(100D);
            imageView.setFitWidth(100D);

            vBoxItem.setMinWidth(width);
            vBoxItem.setPrefHeight(width);
            vBoxItem.setMaxHeight(width);
            vBoxItem.setMinHeight(height);
            vBoxItem.setPrefHeight(height);
            vBoxItem.setMaxHeight(height);
            vBoxItem.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            vBoxItem.getChildren().add(id);
            vBoxItem.getChildren().add(new Separator(Orientation.HORIZONTAL));
            vBoxItem.getChildren().add(productName);
            vBoxItem.getChildren().add(new Separator(Orientation.HORIZONTAL));
            vBoxItem.getChildren().add(price);
            vBoxItem.getChildren().add(new Separator(Orientation.HORIZONTAL));
            hBoxAmount.getChildren().add(amount);
            hBoxAmount.getChildren().add(plus);
            hBoxAmount.getChildren().add(textField);
            hBoxAmount.getChildren().add(minus);
            vBoxItem.getChildren().add(hBoxAmount);
            hBox.getChildren().add(vBoxItem);
            hBox.getChildren().add(new Separator(Orientation.VERTICAL));
            hBox.getChildren().add(imageView);

            vBox.getChildren().add(new Separator());
            vBox.getChildren().add(hBox);
        }

        pane.getChildren().addAll(vBox);

        primaryStage.setScene(new Scene(pane));
        primaryStage.setMinWidth(800D);
        primaryStage.setMinHeight(600D);
        primaryStage.show();
    }
}
