package com.mycompany.chessfx;

import java.io.File;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import java.util.ArrayList;
import javafx.scene.shape.Circle;

/**
 * JavaFX App
 */
public class App extends Application {
    //public constants
    public static final double BP_WIDTH = 1000.0;
    public static final double BP_HEIGHT = 1000.0;
    public static final double SQUARE_LENGTH = 90.0; // 100 pixels
    public static final Color WHITE_SQUARE = Color.BURLYWOOD;
    public static final Color BLACK_SQUARE = Color.SADDLEBROWN;
    
    public ArrayList<Piece> currentPieces = new ArrayList<>(); // pieces that are alive and displayed
    public ArrayList<Piece> takenPieces = new ArrayList<>(); // taken pieces
    
    private static Scene scene;
    private BorderPane bp = new BorderPane(); //highest level container, set scene's pane to this
    private StackPane stackPane = new StackPane();//high level container, set this into borderPane's center container to this stackpane
    private double[][] gameGrid = new double[8][8]; // a grid for representing the current game status, - value for black pieces, + for white
    private GridPane checkerBoard = new GridPane(); // gridPane for holding the squares
    private static GridPane pieceHolder = new GridPane(); // gridPane for holding the individual PiecePanes
    //1 point for pawn, 3 for knight, 3.15 for bishop, 5 for rook, 9 for queen, 90 for king
    
    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("primary"), 1000, 1000);
        this.setGameGrid();
        this.setCheckerBoard();
        this.setPieceHolder();
        this.setStackPane();
        this.setBorderPane();
        //scene.setRoot(); 
        this.scene = new Scene(this.bp, 1000, 1000);// !!!CHANGE THIS LATER ON!!!
        stage.setScene(scene);
        stage.show();
        
        //Debugging stage
        Stage stage2 = new Stage();
        StackPane pane = new StackPane();
        Scene scene2 = new Scene(pane);
        
        Piece debugPiece = new King('w');
        //pane.getChildren().add(debugPiece.getPiecePane());
        String fp = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-black.png";
        ImageView newImg = new ImageView(new Image(new File(fp).toURI().toString()));
        pane.getChildren().add(newImg);
        scene2.setRoot(pane);
        
        stage2.setScene(scene2);
        stage2.show();
        
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
        int i = 8, j;
        int charValue = 104; // starts from 'a'
        Label lbl = null;
        for(j = 0; j < 8; j++){
            char ch = (char)(charValue - j);
            String curr =  "" + ch;
            lbl = new Label(curr);
            this.checkerBoard.add(lbl , i, j);
        }
        for(i = 7, j = 8; i >= 0; i--){
            String curr = "" + (i + 1);
            lbl = new Label(curr);
            this.checkerBoard.add(lbl, i, j);
        }
        this.checkerBoard.setHgap(0);
        this.checkerBoard.setVgap(0);
        //add the checker board into the stack pane
        this.stackPane.getChildren().add(this.checkerBoard);
    }
    //Called in the very first initialization process
    private void setPieceHolder(){
        pieceHolder.setHgap(0);
        pieceHolder.setVgap(0);
        Piece currPiece = null;
        for(int i = 0; i < 8; i++){
            if(i > 1 && i < 6){
                for(int j = 0; j < 8; j++){
                    //add empty stackpanes so we can have gaps in the pieceHolder
                    StackPane emptyPane = new StackPane();
                    emptyPane.setPrefSize(SQUARE_LENGTH, SQUARE_LENGTH);
                    pieceHolder.add(emptyPane, j, i);
                }
            }
            else if(i == 0){
                //set high tier black pieces starting from left
                for(int j = 0; j < 8; j++){
                    switch(j){
                        case 0: currPiece = new Rook('b'); break;
                        case 1: currPiece = new Knight('b'); break;
                        case 2: currPiece = new Bishop('b'); break;
                        case 3: currPiece = new Queen('b'); break;
                        case 4: currPiece = new King('b'); break;
                        case 5: currPiece = new Bishop('b'); break;
                        case 6: currPiece = new Knight('b'); break;
                        case 7: currPiece = new Rook('b'); break;
                    }
                    currPiece.setPosition(i, j);
                    this.currentPieces.add(currPiece);
                    
                    currPiece.getPiecePane().setContainer(this.pieceHolder);
                }
            }
            else if(i == 1){
                for(int j = 0; j < 8; j++){
                    //black pawns
                    currPiece = new Pawn('b');
                    this.currentPieces.add(currPiece);
                    currPiece.setPosition(i, j);
                    currPiece.getPiecePane().setContainer(this.pieceHolder);
                }
            }
            else if(i == 6){
                //add white pawns
                for(int j = 0; j < 8; j++){
                    //black pawns
                    currPiece = new Pawn('w');
                    this.currentPieces.add(currPiece);
                    currPiece.setPosition(i, j);
                    currPiece.getPiecePane().setContainer(this.pieceHolder);
                }
            }
            else if(i == 7){
                //set high tier white pieces starting from left
                for(int j = 0; j < 8; j++){
                    switch(j){
                        case 0: currPiece = new Rook('w'); break;
                        case 1: currPiece = new Knight('w'); break;
                        case 2: currPiece = new Bishop('w'); break;
                        case 3: currPiece = new Queen('w'); break;
                        case 4: currPiece = new King('w'); break;
                        case 5: currPiece = new Bishop('w'); break;
                        case 6: currPiece = new Knight('w'); break;
                        case 7: currPiece = new Rook('w'); break;
                    }
                    currPiece.setPosition(i, j);
                    this.currentPieces.add(currPiece);
                    currPiece.getPiecePane().setContainer(this.pieceHolder);
                }
            }
        }
        //pieceHolder.add(new Circle(50), 8, 8); // for debugging purposes
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
    //Checker board is already added into our stackPane, set other properties if necessary and set the pieceHolder
    private void setStackPane() {
        //add the piece holder, it already must have been set
        this.stackPane.getChildren().add(this.pieceHolder);
    }

    private void setBorderPane() {
        this.bp.setCenter(this.stackPane);
        this.bp.setPrefSize(BP_WIDTH, BP_HEIGHT);
    }
    public static GridPane getPieceHolder(){
        return pieceHolder;
    }
}