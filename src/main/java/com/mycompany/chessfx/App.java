package com.mycompany.chessfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.paint.Color;

/**
 * JavaFX App
 */
public class App extends Application {
    //public constants
    public static final double BP_WIDTH = 1000.0;
    public static final double BP_HEIGHT = 1000.0;
    public static final double SQUARE_LENGTH = 100.0; // 100 pixels
    public static final Color WHITE_SQUARE = Color.BURLYWOOD;
    public static final Color BLACK_SQUARE = Color.SADDLEBROWN;
    
    private static Scene scene;
    private BorderPane bp = new BorderPane(); //highest level container, set scene's pane to this
    private StackPane stackPane = new StackPane();//high level container, set this into borderPane's center container to this stackpane
    private double[][] gameGrid = new double[8][8]; // a grid for representing the current game status, - value for black pieces, + for white
    private GridPane checkerBoard = new GridPane(); // gridPane for holding the squares
    private GridPane pieceHolder = new GridPane(); // gridPane for holding the individual PiecePanes
    //1 point for pawn, 3 for knight, 3.15 for bishop, 5 for rook, 9 for queen, 90 for king
    
    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("primary"), 1000, 1000);
        this.setStackPane();
        this.setBorderPane();
        this.setGameGrid();
        this.setCheckerBoard();
        this.setPieceHolder();
        //scene.setRoot(); 
        this.scene = new Scene(this.bp, 1000, 1000);// !!!CHANGE THIS LATER ON!!!
        stage.setScene(scene);
        stage.show();
    }
    //For setting the gameGrid data field, called only during initialization process
    private void setGameGrid(){
        //set the black pieces
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 8; j++){
                if(i == 0){
                    if(j == 0 || j == 7){
                        this.gameGrid[i][j] = -5.0;
                    }
                    else if(j == 1 || j == 6){
                        this.gameGrid[i][j] = -3.0;
                    }
                    else if(j == 2 || j == 5){
                        this.gameGrid[i][j] = -3.15;
                    }
                    else if(j == 3){ //queen
                        this.gameGrid[i][j] = -9.0;
                    }
                    else{
                        this.gameGrid[i][j] = -90.0; // king
                    }
                }
                else{
                    //set all to pawns
                    this.gameGrid[i][j] = -1.0;
                }
            }
        }
        
        //set the white pieces to the bottom
        for(int i = 7; i >= 6; i--){
            for(int j = 0; j < 8; j++){
                if(i == 0){
                    if(j == 0 || j == 7){
                        this.gameGrid[i][j] = 5.0;
                    }
                    else if(j == 1 || j == 6){
                        this.gameGrid[i][j] = 3.0;
                    }
                    else if(j == 2 || j == 5){
                        this.gameGrid[i][j] = 3.15;
                    }
                    else if(j == 3){ //queen
                        this.gameGrid[i][j] = 9.0;
                    }
                    else{
                        this.gameGrid[i][j] = 90.0; // king
                    }
                }
                else{
                    //set all to pawns
                    this.gameGrid[i][j] = 1.0;
                }
            }
        }
    }
    //Called in the very first initialization process
    private void setCheckerBoard(){
        //0, 0 is a8 and is white
        Rectangle rect = null;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                rect = new Rectangle();
                rect.setWidth(SQUARE_LENGTH);
                rect.setHeight(SQUARE_LENGTH);
                if(i % 2 == 0){
                    //start first with white
                    if(j % 2 == 0){
                        //white
                        rect.setFill(WHITE_SQUARE);
                        rect.setStroke(WHITE_SQUARE);
                    }
                    else{
                        //black
                        rect.setFill(BLACK_SQUARE);
                        rect.setStroke(BLACK_SQUARE);
                    }
                }
                else{
                    //start with black initially
                    if(j % 2 == 0){
                        //black
                        rect.setFill(BLACK_SQUARE);
                        rect.setStroke(BLACK_SQUARE);
                    }
                    else{
                        //white
                        rect.setFill(WHITE_SQUARE);
                        rect.setStroke(WHITE_SQUARE);
                    }
                }
                this.checkerBoard.add(rect, i, j);
            }
        }
        this.checkerBoard.setHgap(0);
        this.checkerBoard.setVgap(0);
        //add the checker board into the stack pane
        this.stackPane.getChildren().add(this.checkerBoard);
    }
    //Called in the very first initialization process
    private void setPieceHolder(){
        
    }
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }

    private void setStackPane() {
        
    }

    private void setBorderPane() {
        this.bp.setCenter(this.stackPane);
        this.bp.setPrefSize(BP_WIDTH, BP_HEIGHT);
    }
}