package com.cse412;

import java.sql.ResultSet;


import java.util.Random;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.io.FileInputStream; // not sure if needed, but for the images
import java.sql.*;



public class Main extends Application {

    //private static Database db;



  public void start(Stage primaryStage) {

   //Create a border pane as the root
    BorderPane rootPane2 = new BorderPane();

    //centerPane1 is a GridPane, it contains 3 labels and 3 text fields
    
    GridPane centerPane2 = new GridPane();
    
	centerPane2.setAlignment(Pos.CENTER);   // centered alignment
    centerPane2.setPadding(new Insets(1, 1, 1, 1));   // this is the spacing from the perimeter of the window
    centerPane2.setHgap(10); // the spacing between objects horizontally
    centerPane2.setVgap(10);  // the spacing between objects horizontally

	
    Label feed = new Label("Feed");

    feed.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 15));   // double check the font--
    feed.setTextFill(Color.HOTPINK);

    TextField search = new TextField();
    search.setPromptText("Search Here");
    search.setPrefWidth(450);
    search.setPrefHeight(40);
	  
//	Image pic = new Image(new FileInputStream("image path"));
	
/*	ImageView view = new ImageView(pic);
	
	view.setX(25);		// double check these dimensions
	view.setY(25);
	
	view.setFitHeight(555);
	view.setFitWidth(600);
    
	view.setPreserveRatio(true);  */
	TextField temp = new TextField();
    temp.setPromptText("(Temporary TextField --> In place of picture)");
    temp.setPrefWidth(100);
    temp.setPrefHeight(400);

    Button searchIt = new Button("Search");
    searchIt.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    searchIt.setStyle("-fx-background-color: LIGHTGREEN");
    
    Button close = new Button("Close Search");
    close.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    close.setStyle("-fx-background-color: LIGHTSALMON");
	
      HBox hBox = new HBox(); 
	 hBox.setPadding(new Insets(10, 10, 10, 10));
	  hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
	hBox.getChildren().addAll(search, searchIt, close);
	  
	
	 Label comment = new Label("Comments");

	comment.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));  // double check the font--
	comment.setTextFill(Color.INDIGO);

    Label comment1 = new Label("[Insert Comment1]");
    comment1.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
    comment1.setTextFill(Color.TRANSPARENT);
    Label comment2 = new Label("[Insert Comment2]");
    comment2.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
    comment2.setTextFill(Color.TRANSPARENT);
    
    
  Random rand = new Random(); 
  int upperbound = 301;
  int randNum = rand.nextInt(upperbound);
    
  Label numLikes = new Label(randNum + " Likes");

	numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));  // double check the font--
	numLikes.setTextFill(Color.TEAL);
    
     HBox hBox2 = new HBox(); 
	 hBox2.setPadding(new Insets(10, 10, 10, 10));
	  hBox2.setSpacing(5);
        hBox2.setAlignment(Pos.CENTER_RIGHT);
    
     Button logOut = new Button("Log Out");
    logOut.setMaxSize(100.0, 100.0);

    
	 VBox vBox = new VBox();
	 vBox.setPadding(new Insets(10, 10, 10, 10));
	  vBox.setSpacing(5);
       //vBox.setAlignment(Pos.CENTER);
	vBox.getChildren().addAll(feed, hBox, temp, numLikes, comment, comment1, comment2, logOut);
	 
    rootPane2.setCenter(vBox);
   // centerPane2.add(vBox, 0, 0);
    //centerPane2.add(hBox, 0, 1);

    // it's like x and y coordinates, but use the window dimensions to kind of see how big a unit is

// Create a scene and place it in the stage
    
    Scene scene2 = new Scene(rootPane2, 700, 600);    // also x and y correlated
    
    primaryStage.setTitle("Social Media App"); // Set the stage title
    primaryStage.setScene(scene2); // Place the scene in the stage
    primaryStage.show(); // Display the stage
  }

 public static void main(String[] args) {
      launch(args);
  }
}
