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



public class WelcomePage extends Application {

    private static Database db;



  public void start(Stage primaryStage) {

   //Create a border pane as the root
    BorderPane rootPane1 = new BorderPane();

    //centerPane1 is a GridPane, it contains 3 labels and 3 text fields
    
    GridPane centerPane1 = new GridPane();
    
	  centerPane1.setAlignment(Pos.CENTER);   // centered alignment
    centerPane1.setPadding(new Insets(10, 10, 10, 10));   // this is the spacing from the perimeter of the window
    centerPane1.setHgap(10); // the spacing between objects horizontally
    centerPane1.setVgap(10);  // the spacing between objects horizontally

	//Create 3 labels
  
    Label Username = new Label("USERNAME");
    Label Password = new Label("PASSWORD");
    Label Welcome = new Label("WELCOME TO OUR SOCIAL MEDIA APP");

    Welcome.setFont(new Font("Times New Roman", 20));   // double check the font--
    Welcome.setTextFill(Color.CRIMSON);

    //Create 2 text fields
    
    TextField UsernameField = new TextField();
    TextField PassField = new TextField();

   	//add the 3 labels and 3 text fields accordingly
    rootPane1.setCenter(centerPane1);
    centerPane1.add(Username, 0, 4);     
    centerPane1.add(UsernameField, 0, 5);
    centerPane1.add(Password, 0, 6);
    centerPane1.add(PassField, 0, 7);
    centerPane1.add(Welcome, 0, 0);

    // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

// Create a scene and place it in the stage
    
    Scene scene1 = new Scene(rootPane1, 700, 600);    // also x and y correlated
    
    primaryStage.setTitle("Social Media App"); // Set the stage title
    primaryStage.setScene(scene1); // Place the scene in the stage
    primaryStage.show(); // Display the stage
  }

public static void main(String[] args) {

    try{
        db = new Database();
        ResultSet rs = db.getAllUserInfo(1);
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        
        while(rs.next()){

            for(int i = 1; i < colCount; i++){
                String columnName = rsmd.getColumnName(i);
                Object value = rs.getObject(i);
                System.out.println(columnName + " = " + value);
            }
        }
    }
    catch(SQLException e){

    }
    catch(ClassNotFoundException e){

    }
      launch(args);
  }
}
