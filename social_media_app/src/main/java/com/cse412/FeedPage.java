package com.cse412;

import java.sql.ResultSet;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import java.sql.*;



public class FeedPage extends Application {

    private static Database db;



  public void start(Stage primaryStage) {

   //Create a border pane as the root
    BorderPane rootPane2 = new BorderPane();

    //centerPane1 is a GridPane, it contains 3 labels and 3 text fields
    
    GridPane centerPane2 = new GridPane();
    
	  centerPane2.setAlignment(Pos.CENTER);   // centered alignment
    centerPane2.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
    centerPane2.setHgap(10); // the spacing between objects horizontally
    centerPane2.setVgap(10);  // the spacing between objects horizontally

	
  //  Label Feed = new Label("Feed");

  //  Feed.setFont(new Font("Times New Roman", 20));   // double check the font--
  //  Feed.setTextFill(Color.CRIMSON);

    rootPane2.setCenter(centerPane2);
  //  centerPane1.add(Feed, 0, 0);

    // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

// Create a scene and place it in the stage
    
    Scene scene2 = new Scene(rootPane2, 700, 600);    // also x and y correlated
    
    primaryStage.setTitle("Feed"); // Set the stage title
    primaryStage.setScene(scene2); // Place the scene in the stage
    primaryStage.show(); // Display the stage
  }

public static void main(String[] args) {

    try{
        db = new Database();
  
    }
    catch(SQLException e){

    }
    catch(ClassNotFoundException e){

    }
      launch(args);
  }
}
