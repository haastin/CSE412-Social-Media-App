package com.cse412;

import java.sql.ResultSet;

import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.*;
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

// THE CODE BELOW IS WHAT GETS ADDED TO MAIN ==========================================================================================================  
	 
BorderPane rootPane2 = new BorderPane();
    GridPane centerPane2 = new GridPane();
    
    centerPane2.setAlignment(Pos.CENTER);   // centered alignment
    centerPane2.setPadding(new Insets(1, 1, 1, 1));   // this is the spacing from the perimeter of the window
    centerPane2.setHgap(10); // the spacing between objects horizontally
    centerPane2.setVgap(10);  // the spacing between objects horizontally

    
    Label feed = new Label("Feed");

    feed.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 25));   
    feed.setTextFill(Color.HOTPINK);


    Button ownPhotos = new Button("View Your Photos");
    ownPhotos.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    ownPhotos.setStyle("-fx-background-color: TRANSPARENT");
    
    TextField search = new TextField();
   search.setPromptText("Search Here");
    search.setPrefWidth(250);
    search.setPrefHeight(40); 


   ComboBox suggest = new ComboBox();
   suggest.getItems().addAll(
      "Top Ten Users", 
      "Most Popular Tags", 
      "Recommended Friends",
      "You May Also Like");
    
 suggest.setPromptText("Suggestions");
 /* suggest.getEditor().textProperty().addListener(new ChangeListener<String>() {

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("yuh");
    }
    });  */
    
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


String path = url;
String pathToOpen = url;
// replace the above with pid

Image image = new Image(path);
ImageView view = new ImageView(image);
    
view.setX(25);		// double check these dimensions
view.setY(25);
    
view.setFitHeight(100);
view.setFitWidth(100);
    
view.setPreserveRatio(true);    

    
    /* TextField temp = new TextField();
    temp.setPromptText("(Temporary TextField --> In place of picture)");
    temp.setPrefWidth(100);
    temp.setPrefHeight(400);    */

    Button searchIt = new Button("Search");
    searchIt.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    searchIt.setStyle("-fx-background-color: LIGHTGREEN");
    
    Button clear = new Button("Clear");
    clear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    clear.setStyle("-fx-background-color: LIGHTSALMON");
    
    HBox hBox = new HBox(); 
    hBox.setPadding(new Insets(10, 10, 10, 10));
    hBox.setSpacing(5);
    hBox.setAlignment(Pos.CENTER);
    hBox.getChildren().addAll(suggest, search, searchIt, clear);
      
    
    Label comment0 = new Label("Comments");

    comment0.setFont(Font.font("Verdana", FontPosture.REGULAR, 15));  
    comment0.setTextFill(Color.INDIGO);

    Label comment1 = new Label("");
    comment1.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
    comment1.setTextFill(Color.BLACK);

    for (i = 0; i < sizeOf(comments); i++) {
        comment1.setText(comments[i] + "\n");
    }

   /* Label comment2 = new Label("[Insert Comment2]");
    comment2.setFont(Font.font("Verdana", FontPosture.REGULAR, 10));
    comment2.setTextFill(Color.TRANSPARENT);    */
    
    
 /* Random rand = new Random(); 
  int upperbound = 301;
  int randNum = rand.nextInt(upperbound); */
    
    Label numLikes = new Label(total_likes + " Likes");

    numLikes.setFont(Font.font("Verdana", FontPosture.REGULAR, 15)); 
    numLikes.setTextFill(Color.TEAL);
    
    HBox hBox2 = new HBox(); 
    hBox2.setPadding(new Insets(10, 10, 10, 10));
    hBox2.setSpacing(5);
   
    hBox2.setAlignment(Pos.CENTER_RIGHT);
    hBox2.getChildren().add(view);  // adding image to Hbox 

    Button logOut = new Button("Log Out");
    logOut.setMaxSize(100.0, 100.0);

    HBox hBox3 = new HBox(); 
    hBox3.setPadding(new Insets(10, 10, 10, 10));
    hBox3.setSpacing(20);
    hBox3.setAlignment(Pos.CENTER);
    hBox3.getChildren().add(ownPhotos, logOut);  // adding image to Hbox 

    
    VBox vBox = new VBox();
    vBox.setPadding(new Insets(10, 10, 10, 20));
    vBox.setSpacing(5);
    //vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(feed, hBox3, hBox, hBox2, numLikes, comment0, comment1);

    for (i = 0; i < sizeOf(comments); i++) {

        vbox.getChildren().add(new Label(comments[i]));
    }
    
    rootPane2.setCenter(vBox);
  
    // Create a scene and place it in the stage
    Scene feedScene = new Scene(rootPane2, 700, 600);    // also x and y correlated
    
    primaryStage.setTitle("Social Media App"); // Set the stage title
    primaryStage.setScene(feedScene); // Place the scene in the stage
    primaryStage.show(); // Display the stage

    // comboBox listeners below

    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {

            if(suggest.getValue().toString().equals("Top Ten Users")) {
                primaryStage.setScene(feedScene);    // change to top ten users page
            }

            else if(suggest.getValue().toString().equals("Most Popular Tags")) {
                primaryStage.setScene(feedScene);    // change to most popular tags page
            }

            else if(suggest.getValue().toString().equals("Recommended Friends")) {
                primaryStage.setScene(feedScene);    //  change to recommended friends page
            }

            else if(suggest.getValue().toString().equals("You May Also Like")) {
                primaryStage.setScene(feedScene);    // change to you may also like page
            }
              
            }
        };

   suggest.setOnAction(event);
    
   // button listener below

   searchIt.setOnAction((ActionEvent f) -> {
     // primaryStage.setScen suggest-scene);    // based on the scene names of th suggest pg
    });
    
    clear.setOnAction((ActionEvent g) -> {
      //suggest.setPromptText("Suggestions");
      primaryStage.setScene(feedScene);  // refreshes the page basically
      
    });
    
    logOut.setOnAction((ActionEvent h) -> {
      primaryStage.setScene(scene1);  // based on the scene name of the welcome pg
    });
    
  }

 /*public static void main(String[] args) {

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
