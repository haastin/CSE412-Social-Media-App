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
import java.io.FileInputStream;
import java.sql.*;



public class FeedPage extends Application {

    private static Database db;

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

    feed.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 15));   
    feed.setTextFill(Color.HOTPINK);

    TextField search = new TextField();
    search.setPromptText("Search Here");
    search.setPrefWidth(450);
    search.setPrefHeight(40);

    /*
    URL url = getClass().getResource("/drawIcon.png");
Image image = ImageIO.read(url);

In case you want to create a javafx Image:

Image image = new Image("/drawIcon.png");
    */
    
//=================================================================================

    
/*   
  //Passing FileInputStream object as a parameter 
FileInputStream inputstream = new FileInputStream("C:\\images\\image.jpg"); 
Image image = new Image(inputstream); 
         
//Loading image from URL 
//Image image = new Image(new FileInputStream("url for the image"));

---------------------------------------------------------------------------------
  
  //Creating an image 
      Image image = new Image(new FileInputStream("path of the image"));  
      
      //Setting the image view 
      ImageView imageView = new ImageView(image); 
*/
    
// path --> CSE412-Social-MediaApp/social_media_app/src/main/resources/com/cse412/primary.fxml


String path = "https://www.vecteezy.com/free-vector/cute-smiley-face";
String pathToOpen = "https://www.vecteezy.com/free-vector/cute-smiley-face";
// replace the above with pid

Image image = new Image(path);
ImageView view = new ImageView(image);
	
	view.setX(25);		// double check these dimensions
	view.setY(25);
	
	view.setFitHeight(50);
	view.setFitWidth(50);
    
	view.setPreserveRatio(true);    


    
	TextField temp = new TextField();
    temp.setPromptText("(Temporary TextField --> In place of picture)");
    temp.setPrefWidth(100);
    temp.setPrefHeight(400);

    Button searchIt = new Button("Search");
    searchIt.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    searchIt.setStyle("-fx-background-color: LIGHTGREEN");
    
    Button clear = new Button("Clear Search");
    clear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    clear.setStyle("-fx-background-color: LIGHTSALMON");
	
  HBox hBox = new HBox(); 
	hBox.setPadding(new Insets(10, 10, 10, 10));
  hBox.setSpacing(5);
  hBox.setAlignment(Pos.CENTER);
	hBox.getChildren().addAll(search, searchIt, clear);
	  
	
	 Label comment = new Label("Comments");

	comment.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));  
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

	numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15)); 
	numLikes.setTextFill(Color.TEAL);
    
     HBox hBox2 = new HBox(); 
	 hBox2.setPadding(new Insets(10, 10, 10, 10));
	  hBox2.setSpacing(5);
   
        hBox2.setAlignment(Pos.CENTER_RIGHT);
    hBox2.getChildren().add(view);  // adding image to Hbox 

    
     Button logOut = new Button("Log Out");
    logOut.setMaxSize(100.0, 100.0);

    
	 VBox vBox = new VBox();
	 vBox.setPadding(new Insets(10, 10, 10, 10));
	  vBox.setSpacing(5);
      //vBox.setAlignment(Pos.CENTER);
	vBox.getChildren().addAll(feed, hBox, hBox2, numLikes, comment, comment1, comment2, logOut);
    
    rootPane2.setCenter(vBox);
  
    // Create a scene and place it in the stage
    Scene scene2 = new Scene(rootPane2, 700, 600);    // also x and y correlated
    
    primaryStage.setTitle("Social Media App"); // Set the stage title
    primaryStage.setScene(scene2); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    // button listeners below

    searchIt.setOnAction((ActionEvent g) -> {

     // primaryStage.setScene(search-scene);    // based on the scene names of the search pg
      
    });
    
    clear.setOnAction((ActionEvent g) -> {

      primaryStage.setScene(scene2);  // refreshes the page basically
      
    });
    
    logOut.setOnAction((ActionEvent g) -> {

      //  primaryStage.setScene(welcome-scene);  // based on the scene name of the welcome pg
      
    });
    
  }

 public static void main(String[] args) {

    try {
        db = new Database();
  
    }
    catch(SQLException e){

    }
    catch(ClassNotFoundException e){

    }
      launch(args);
  }
}
