package com.mycompany.chessfx;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
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
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * JavaFX App
 */
public class App extends Application {
    //public constants
    public static final double BP_WIDTH = 1000.0;
    public static final double BP_HEIGHT = 1000.0;
    public static final double SQUARE_LENGTH = 90.0; // 100 pixels
    public static final double PROMOTION_BOX_SPACING = 500;
    public static final double TURN_CIRCLE_RADIUS = 50.0;
    public static final double TURN_CIRCLE_STROKE_WIDTH = 4.0;
    public static final Color WHITE_SQUARE = Color.BURLYWOOD;
    public static final Color BLACK_SQUARE = Color.SADDLEBROWN;
    public static final Color TURN_CIRCLE_WHITE = Color.WHITESMOKE;
    public static final Color TURN_CIRCLE_BLACK = Color.BLACK;
    public static final Color TURN_CIRCLE_STROKE = Color.GREEN;
    
    public static ArrayList<Piece> currentPieces = new ArrayList<>(); // pieces that are alive and displayed
    public static ArrayList<Piece> takenPieces = new ArrayList<>(); // taken pieces
    public static Piece selectedPiece = null; // currently selected piece, will be used to paint the selected square to green
    
    private static Scene scene;
    private BorderPane bp = new BorderPane(); //highest level container, set scene's pane to this
    private StackPane stackPane = new StackPane();//high level container, set this into borderPane's center container to this stackpane
    private double[][] gameGrid = new double[8][8]; // a grid for representing the current game status, - value for black pieces, + for white
    private GridPane checkerBoard = new GridPane(); // gridPane for holding the squares
    private static GridPane pieceHolder = new GridPane(); // gridPane for holding the individual PiecePanes
    private VBox promotionBox = new VBox(); //vbox for holding both of the promotion panes
    private static Circle turnCircle = new Circle();
    //1 point for pawn, 3 for knight, 3.15 for bishop, 5 for rook, 9 for queen, 90 for king
    private static double gamePoints = 0.0; //negative means black is winning based on taken pieces, positive implies white is winning
    
    public static void displayMoveables() {
        if(App.selectedPiece != null){
            eraseMoveablesFromPane();
            //retrieve the moveable squares of the selected piece and paint the stroke of those squares
            Object[] moveables = selectedPiece.showMoveables();
            if(!(App.selectedPiece instanceof King)){
                for(int i = 0; i < moveables.length; i++){
                    String currMoveable = (String)(moveables[i]);
                    //for each moveable that we have check if the game state is valid after we make that move
                    double[][] possibleState = App.retrieveGameState(App.selectedPiece.getPosition(), currMoveable);
                    String friendlyKingPos = App.getKingPosition(possibleState, App.selectedPiece.getColor());
                    System.out.println("We are displaying the following matrix from displayMoveables of App.java, currMoveable is: " + currMoveable);
                    //App.printMatrix(possibleState);
                    if(!App.isChecked(possibleState, App.selectedPiece.getColor(), friendlyKingPos)){
                        //you can simply display this square as a moveable square
                        System.out.println("Apparently displayed possible state satisfies our " + App.selectedPiece.getColor() + " king to be not checked");
                        //Retrieve the moveable square's pane
                        int[] currMoveablePositions = Piece.getNumericPosition(currMoveable);
                        int currMoveableRow = currMoveablePositions[0];
                        int currMoveableColumn = currMoveablePositions[1];

                        StackPane moveableSquare = App.getPieceHolderNode(currMoveableRow, currMoveableColumn);
                        if(moveableSquare instanceof PiecePane){
                            PiecePane currMoveablePane = (PiecePane)(moveableSquare);
                            //ensure that the selected pane to be moved is an enemy piece which is not a king instance
                            Piece currMoveablePiece = currMoveablePane.getPiece();
                            if(!(currMoveablePiece.getColor().equals(App.selectedPiece.getColor())) && !(currMoveablePiece instanceof King)){
                                currMoveablePane.setOuterFrame(true, false);
                            }
                        }
                        else if(moveableSquare instanceof EmptyPane){
                            EmptyPane currMoveablePane = (EmptyPane)(moveableSquare);
                            currMoveablePane.setOuterFrame(true);
                        }
                    }
                }
                //draw the outer frame of the selected piece as well
                PiecePane selectedPiecePane = App.selectedPiece.getPiecePane();
                selectedPiecePane.setOuterFrame(false, true);
            }
            else{
                //we are trying to display the moveable squares of a king
                //we can directly invoke the show moveables of the king since we already check whether a moveable square is a valid move
                //in-place on the showMoveables method of the king
                Object[] moveableSquares = App.selectedPiece.showMoveables();
                for(Object o: moveableSquares){
                    String currMoveable = (String)(o);
                    //Retrieve the moveable square's pane
                        int[] currMoveablePositions = Piece.getNumericPosition(currMoveable);
                        int currMoveableRow = currMoveablePositions[0];
                        int currMoveableColumn = currMoveablePositions[1];
                        
                    StackPane currMoveableSquare = App.getPieceHolderNode(currMoveableRow, currMoveableColumn);
                    //since we have already checked in the showMoveable method of the King, we can simply draw these squares as moveables
                    if(currMoveableSquare instanceof PiecePane){
                        ((PiecePane)(currMoveableSquare)).setOuterFrame(true, false);
                    }
                    else if(currMoveableSquare instanceof EmptyPane){
                        ((EmptyPane)(currMoveableSquare)).setOuterFrame(true);
                    }
                }
                //If the friendly king is not currently checked then draw the outer frame of the selected piece as well
                String kingPos = App.selectedPiece.getPosition();
                String kingColor = App.selectedPiece.getColor();
                boolean isChecked = App.isChecked(App.retrieveGameState(kingPos, kingPos), kingColor, kingPos);
                if(!isChecked){
                    PiecePane selectedPiecePane = App.selectedPiece.getPiecePane();
                    selectedPiecePane.setOuterFrame(false, true);
                }
            }
            
        }
    }
    //Invoke when we are able to cover our king from checking piece(s) by moving an alternative piece onto the path
    //Caller is responsible of ensuring that the validMoveables are valid positions which do not expose their friendly king
    public static void displayValidMoveables(ArrayList<String> validMoveables, String friendlyColor){
        eraseMoveablesFromPane();
        if(validMoveables.size() == 0){
            //we have no valid moveables for currently selected piece to cover the king, hence draw its king as checked and return
            double[][] currState = App.retrieveGameState(Piece.positions[0][0], Piece.positions[0][0]);
            String friendlyKingPos = App.getKingPosition(currState, friendlyColor);
            King king = (King)((PiecePane)(App.getPieceHolderNode(Piece.getRow(friendlyKingPos), Piece.getColumn(friendlyKingPos)))).getPiece();
            king.setCheckedFrame();
            return;
        }
        for(String currPos: validMoveables){
            StackPane currPane = App.getPieceHolderNode(Piece.getRow(currPos), Piece.getColumn(currPos));
            if(currPane instanceof EmptyPane){
                ((EmptyPane)(currPane)).setOuterFrame(true);
            }
            else if(currPane instanceof PiecePane){
                PiecePane currMoveablePane = (PiecePane)(currPane);
                //ensure that the selected pane to be moved is an enemy piece which is not a king instance
                Piece currMoveablePiece = currMoveablePane.getPiece();
                if(!(currMoveablePiece.getColor().equals(App.selectedPiece.getColor())) && !(currMoveablePiece instanceof King)){
                    currMoveablePane.setOuterFrame(true, false);
                }
            }
        }
        //If the friendly king is not currently checked then draw the outer frame of the selected piece as well
                String kingPos = App.selectedPiece.getPosition();
                String kingColor = App.selectedPiece.getColor();
                boolean isChecked = App.isChecked(App.retrieveGameState(kingPos, kingPos), kingColor, kingPos);
                if(!isChecked){
                    PiecePane selectedPiecePane = App.selectedPiece.getPiecePane();
                    selectedPiecePane.setOuterFrame(false, true);
                }
    }
    //Method to stop displaying each and every moveable that is being displayed right now
    //Invoke each time when the displayMoveables has been invoked
    public static void eraseMoveablesFromPane(){
        //Traverse each and every node in the pieceHolder pane and regardless of whether they have an outer rectangle or not, remove the rectangles
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                StackPane currNode = App.getPieceHolderNode(i, j);
                if(currNode instanceof PiecePane){
                    PiecePane currPane = (PiecePane)(currNode);
                    currPane.setOuterFrame(false, false);
                }
                else if(currNode instanceof EmptyPane){
                    EmptyPane currPane = (EmptyPane)(currNode);
                    currPane.setOuterFrame(false);
                }
            }
        }
    }
    //Invoke each and every time after a move has been made. Will be invoked from the Piece super class of Piece sub-classes.
    public static void displayEnemyKingAsChecked(String enemyColor){
        
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("primary"), 1000, 1000);
        this.setGameGrid();
        this.setCheckerBoard();
        this.setPieceHolder();
        this.setStackPane();
        this.setBorderPane();
        this.setPromotionBox();
        this.setTurnCircle();
        //scene.setRoot(); 
        this.scene = new Scene(this.bp, 1000, 1000);// !!!CHANGE THIS LATER ON!!!
        stage.setScene(scene);
        stage.show();
        
        //Debugging stage
        /*Stage stage2 = new Stage();
        StackPane pane = new StackPane();
        Scene scene2 = new Scene(pane);
        
        Piece debugPiece = new King('w');
        //pane.getChildren().add(debugPiece.getPiecePane());
        String fp = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-black.png";
        ImageView newImg = new ImageView(new Image(new File(fp).toURI().toString()));
        newImg.setFitHeight(80.0);
        newImg.setFitWidth(80.0);
        pane.getChildren().add(newImg);
        pane.setPrefSize(90, 90);
        scene2.setRoot(pane);
        
        stage2.setScene(scene2);
        stage2.show();*/
        
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
                if(i == 7){
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
        int k = 0, t;
        int charValue = 104; // starts from 'a'
        Label lbl = null;
        for(j = 0, t = 7; j < 8; j++, t--){
            
            String curr = "" + (t + 1);
            lbl = new Label(curr);
            this.checkerBoard.add(lbl , i, j);
        }
        for(i = 7, j = 8; i >= 0; i--, k++){
            char ch = (char)(charValue - k);
            String curr =  "" + ch;
            
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
                    String currPos = Piece.positions[i][j];
                    EmptyPane emptyPane = new EmptyPane(currPos);
                    emptyPane.setEventHandling(); //set the event handling procedure after each and every instantiation of an EmptyPane
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
    public static void updateGamePoints(double takenPiece){
        gamePoints -= takenPiece;
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
    private void setPromotionBox(){
        PromotionPane whitePromotion = new PromotionPane(true);
        PromotionPane blackPromotion = new PromotionPane(false);
        this.promotionBox.getChildren().addAll(whitePromotion, blackPromotion);
        this.promotionBox.setSpacing(App.PROMOTION_BOX_SPACING);
        //add the promotion box to the right side of the bp
        this.bp.setRight(this.promotionBox);
    }
    private void setTurnCircle(){
        this.turnCircle.setRadius(TURN_CIRCLE_RADIUS);
        this.turnCircle.setFill(App.TURN_CIRCLE_WHITE);
        this.turnCircle.setStroke(App.TURN_CIRCLE_STROKE);
        this.turnCircle.setStrokeWidth(TURN_CIRCLE_STROKE_WIDTH);
        //add the turn circle to the left side of the bp
        this.bp.setLeft(this.turnCircle);
    }
    //Switch the circle's color to the opposite on each invoke
    public static void switchCircleColor(){
        Color currColor = (Color)turnCircle.getFill();
        Color newColor;
        if(currColor.equals(App.TURN_CIRCLE_WHITE)){
            newColor = App.TURN_CIRCLE_BLACK;
        }
        else{
            newColor = App.TURN_CIRCLE_WHITE;
        }
        turnCircle.setFill(newColor);
    }
    public static GridPane getPieceHolder(){
        return pieceHolder;
    }
    public static StackPane getPieceHolderNode(int row, int column){
        Node result = null;
        ObservableList<Node> children = pieceHolder.getChildren();

        for (Node node : children){
            if(pieceHolder.getRowIndex(node) == row && pieceHolder.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        /*if(result == null){
            System.out.println("!!!1Attention, we are returning null as an asked node from getPieceHolderNode function of App class");
            System.out.println("!!!1 Row is: " + row + ", Column is: " + column);
        }
        else{
            System.out.println("!!!2Attention, we are returning nonnull as an asked node from getPieceHolderNode function of App class");
            System.out.println("!!!2 Row is: " + row + ", Column is: " + column);
            
            //Below commented code is for stack tracing
            //StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            //for(int i = 0; i < stacktrace.length; i++){
                //System.out.println(stacktrace[i].getMethodName() + " of " + stacktrace[i].getClassName() + " has invoked getPieceHolderNode");
            //}
        }*/
        return (StackPane)result;
    }
    //Utility functions for checking game status
    //Invoke when check status changes to true for a king
    public boolean isMated(){
        boolean result = true;
        
        King checkedKing = null;
        String friendlyColor = null;
        String enemyColor = null;
        
        //Retrieve the checked king
        for(Piece p: currentPieces){
            if(p instanceof King){
                King currKing = (King)(p);
                //Check whether the current king is threatened by using the static isChecked function
                boolean isCurrKingChecked = App.isChecked(App.retrieveGameState(currKing.getPosition(), currKing.getPosition())
                        , currKing.getColor(), currKing.getPosition());
                if(isCurrKingChecked){
                    checkedKing = currKing;
                    friendlyColor = checkedKing.getColor();
                    enemyColor = checkedKing.getEnemyColor();
                    break;
                }
            }
        }
        String[] kingMoveables = (String[])checkedKing.showMoveables(); // squares that our king can move
        if(kingMoveables != null && kingMoveables.length > 0){
            return false;
        }
        //Retrieve all of the alive enemy pieces
        ArrayList<Piece> enemies = new ArrayList<>(); //all alive enemies except the enemy king
        for(Piece p: currentPieces){
            if(p.getColor().equals(enemyColor) && !(p instanceof King)){
                enemies.add(p);
            }
        }
        //Retrieve the enemies that check our king
        ArrayList<Piece> checkingEnemies = new ArrayList<>();// enemies that check our king
        for(Piece p: enemies){
            String[] currMoveables = (String[])(p.showMoveables());
            for(int i = 0; i < currMoveables.length; i++){
                String currKingPos = checkedKing.getPosition();
                if(currMoveables[i].equals(currKingPos)){
                    checkingEnemies.add(p);
                    break;
                }
            }
        }
        ArrayList<Piece> friendlyPieces = new ArrayList<>();
        //retrieve each and every alive friendly piece except from our king
        for(Piece p: currentPieces){
            if(p.getColor().equals(friendlyColor) && !(p instanceof King)){
                friendlyPieces.add(p);
            }
        }
        if(checkingEnemies.size() == 1){
            String enemyPos = checkingEnemies.get(0).getPosition();
            
            //check if we can take the checking enemy by a piece except from our king, without exposing the king
            for(Piece p: friendlyPieces){
                String[] friendlyMoveables = (String[])(p.showMoveables());
                String currFriendlyPos = p.getPosition();
                
                for(int i = 0; i < friendlyMoveables.length; i++){
                    String currMoveable = friendlyMoveables[i];
                    if(currMoveable.equals(enemyPos)){
                        double[][] nextGameState = this.retrieveGameState(currFriendlyPos, enemyPos);
                        if(!isChecked(nextGameState, friendlyColor, checkedKing.getPosition())){
                            return false;
                        }
                    }
                }
            }
            
        }
        //Traverse each and every checking piece and apply logic that we have created
        for(Piece p: checkingEnemies){
            String enemyPos = p.getPosition();
            if(p instanceof Knight || p instanceof Pawn){
                
                //We are obligated to take p since we cannot obstruct the path, traverse each friendly alive pieces see whether we can take p
                for(Piece currFriendly: friendlyPieces){
                    String[] friendlyMoveables = (String[])(currFriendly.showMoveables());
                    for(int i = 0; i < friendlyMoveables.length; i++){
                        String currMoveable = friendlyMoveables[i];
                        if(currMoveable.equals(enemyPos)){
                            double[][] nextGameState = this.retrieveGameState(currFriendly.getPosition(), enemyPos);
                            if(!isChecked(nextGameState, friendlyColor, checkedKing.getPosition())){
                                return false;
                            }
                        }
                    }
                }
            }
            else{//Queen Bishop Rook
                
                //Same procedure that we have applied if we can save our king by taking a checking piece
                for(Piece currFriendly: friendlyPieces){
                    String[] friendlyMoveables = (String[])(currFriendly.showMoveables());
                    for(int i = 0; i < friendlyMoveables.length; i++){
                        String currMoveable = friendlyMoveables[i];
                        if(currMoveable.equals(enemyPos)){
                            double[][] nextGameState = this.retrieveGameState(currFriendly.getPosition(), enemyPos);
                            if(!isChecked(nextGameState, friendlyColor, checkedKing.getPosition())){
                                return false;
                            }
                        }
                    }
                }
                //If not, we should finally check whether we can obstruct the path from the enemy piece to our king with a friendly piece
                
                for(Piece enemyPiece: checkingEnemies){
                  String[] path = getPath(checkedKing, enemyPiece);
                  //check whether after obstructing this path the game state turns into an unchecked position
                  for(int i = 0; i < path.length; i++){
                      String currSquare = path[i];
                      for(int j = 0; j < friendlyPieces.size(); j++){
                          Piece currFriendly = friendlyPieces.get(i);
                          String[] currMoveables = (String[])currFriendly.showMoveables();
                          if(contains(currMoveables, currFriendly)){
                              //check whether we can obstruct the path by moving our currFriendly into that pos
                              double[][] nextState = this.retrieveGameState(currFriendly.getPosition(), currSquare);
                              if(!isChecked(nextState, friendlyColor, checkedKing.getPosition())){
                                  return false;
                              }
                          }
                      }
                  }
                }
                
            }
        }
        return true;
    }
    public boolean isStaleMate(){
        String friendlyColor;
        if(Move.getTurn() == true){
            friendlyColor = Piece.WHITE_COLOR;
        }
        else{
            friendlyColor = Piece.BLACK_COLOR;
        }
        King friendlyKing = null;
        ArrayList<Piece> friendlyPieces = new ArrayList<>(); // list of alive friendly pieces except the king
        for(Piece p: App.currentPieces){
            if(p.getColor().equals(friendlyColor)){
                if(p instanceof King){
                    friendlyKing = (King)p;
                    continue;
                }
                friendlyPieces.add(p);
            }
        }
        //initially ensure that our king is not checked and whether it has any possible moves
        String[] kingMoveables = (String[])(friendlyKing.showMoveables());
        boolean isFriendlyKingChecked = App.isChecked(App.retrieveGameState(friendlyKing.getPosition(), friendlyKing.getPosition())
                        , friendlyKing.getColor(), friendlyKing.getPosition());
        if(!isFriendlyKingChecked && kingMoveables.length != 0){//
            return false;
        }
        //ensure that we cannot move any other piece
        for(int i = 0; i < friendlyPieces.size(); i++){
            String[] currMoveables = (String[])(friendlyPieces.get(i).showMoveables());
            if(currMoveables.length != 0){
                return false;
            }
        }
        return true;
    }
    //Static utility method for checking whether if a move is valid, pass a game state representing aftermath of a possible move and check
    //Whether our friendly king is checked after that move
    //Return true if friendly king is checked
    public static boolean isChecked(double[][] state, String kingColor, String kingPosition){
        //retrieve each and every alive enemy piece positions except for enemy king
        ArrayList<Integer> enemyRows = new ArrayList<>();
        ArrayList<Integer> enemyColumns = new ArrayList<>();
        
        //print the current given state for debugging purposes
        //App.printMatrix(state);
        
        String friendlyColor;
        String enemyColor;
        boolean isWhiteFriendly = kingColor.equals(Piece.WHITE_COLOR);
        if(isWhiteFriendly){
            friendlyColor = Piece.WHITE_COLOR;
            enemyColor = Piece.BLACK_COLOR;
        }
        else{
            friendlyColor = Piece.BLACK_COLOR;
            enemyColor = Piece.WHITE_COLOR;
        }
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                double curr = state[i][j];
                if(curr != 0 && Math.abs(curr) != 90){
                    if(enemyColor.equals(Piece.BLACK_COLOR) && curr < 0){
                        enemyRows.add(i);
                        enemyColumns.add(j);
                    }
                    else if(enemyColor.equals(Piece.WHITE_COLOR) && curr > 0){
                        enemyRows.add(i);
                        enemyColumns.add(j);
                    }
                }
                /*if(curr != 0 && Math.abs(curr) != 90){
                    enemyRows.add(i);
                    enemyColumns.add(j);
                }*/
                
            }
        }
        System.out.println("+++++++++++++++++++ King Pos is: " + kingPosition + " enemy color is " + enemyColor);
        for(int i = 0; i < enemyRows.size(); i++){
            int currRow = enemyRows.get(i);
            int currColumn = enemyColumns.get(i);
            
            double currValue = state[currRow][currColumn];
            double currValueAbs = Math.abs(currValue);
            
            String currPos = Piece.positions[currRow][currColumn];
            
            char enemyCharColor = 'w';
            if(enemyColor.equals(Piece.BLACK_COLOR)){
                enemyCharColor = 'b';
            }
            //Piece currEnemy = null;
            if(currValueAbs == 1.0){
                //currEnemy = new Pawn(enemyCharColor, currValue, currRow, currColumn);
                String[] moveables = Pawn.showMoveables(state, currRow, currColumn);
                System.out.println("Pawn moveables are: " + Arrays.toString(moveables));
                for(int j = 0; j < moveables.length; j++){
                    int pawnRow = Piece.getRow(currPos);
                    int pawnColumn = Piece.getColumn(currPos);
                    int kingRow = Piece.getRow(kingPosition);
                    int kingColumn = Piece.getColumn(kingPosition);
                    System.out.println("$$$$$$$For pawn moveables, j is: " + j + "\npawnColum & kingColumn is (respectively): " + pawnColumn + ", " + kingColumn);
                    boolean canPawnCheck = false;
                    //set the canPawnCheck accordingly
                    if(((enemyCharColor == 'w' && pawnRow - kingRow == 1) || (enemyCharColor == 'b' && pawnRow - kingRow == -1)) && Math.abs(pawnColumn - kingColumn) == 1){
                        canPawnCheck = true;
                    }
                    /*else{
                        //retrieve the last move and check if it was a kick start of a friendly pawn and the current enemy pawn can en passant it
                        Move lastMove = Move.getLastMove();
                        Piece lastMovedPiece 
                        if(){ //check for en passant
                        
                        }
                    }*/
                    //if(moveables[j].equals(kingPosition) && pawnColumn != kingColumn){
                    if(canPawnCheck){
                        System.out.println("$$$$$$We can check the king with a pawn");
                        //ensure that it is diagonal to the pawn by the above if block
                        return true;
                    }
                }
            }
            else if(currValueAbs == 3.0){
                //currEnemy = new Knight(enemyCharColor, currValue, currRow, currColumn);
                String[] moveables = Knight.showMoveables(state, currRow, currColumn);
                System.out.println("Knight moveables are: " + Arrays.toString(moveables));
                for(int j = 0; j < moveables.length; j++){
                    if(moveables[j].equals(kingPosition)){
                        return true;
                    }
                }
            }
            else if(currValueAbs == 3.15){
                //currEnemy = new Bishop(enemyCharColor, currValue, currRow, currColumn);
                String[] moveables = Bishop.showMoveables(state, currRow, currColumn);
                System.out.println("Bishop moveables are: " + Arrays.toString(moveables));
                for(int j = 0; j < moveables.length; j++){
                    if(moveables[j].equals(kingPosition)){
                        System.out.println("$$$$$$We can check the king with a bishop, king pos is and currMoveable are respectively " + kingPosition);
                        return true;
                    }
                }
            }
            else if(currValueAbs == 5.0){
                //currEnemy = new Rook(enemyCharColor, currValue, currRow, currColumn);
                String[] moveables = Rook.showMoveables(state, currRow, currColumn);
                System.out.println("Rook moveables are: " + Arrays.toString(moveables));
                for(int j = 0; j < moveables.length; j++){
                    if(moveables[j].equals(kingPosition)){
                        System.out.println("$$$$$$We can check the king with a rook, king pos is and currMoveable are respectively " + kingPosition);
                        return true;
                    }
                }
            }
            else if(currValueAbs == 9.0){
                //currEnemy = new Queen(enemyCharColor, currValue, currRow, currColumn);
                String[] moveables = Queen.showMoveables(state, currRow, currColumn);
                System.out.println("Queen moveables are: " + Arrays.toString(moveables));
                for(int j = 0; j < moveables.length; j++){
                    if(moveables[j].equals(kingPosition)){
                        System.out.println("$$$$$$We can check the king with a queen, king pos is and currMoveable are respectively " + kingPosition);
                        return true;
                    }
                }
            }
            
        }
        return false;
    }
    
    //Method to retrieve gamestate representation subsequent to a move that could be made
    public static double[][] retrieveGameState(String takerPos, String takenPos){
        double[][] result = new double[8][8];
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                StackPane currPane = (StackPane)(getGridNode(pieceHolder, i, j));
                String currPos = Piece.positions[i][j];
                if(currPane instanceof PiecePane){
                    PiecePane currPiecePane = (PiecePane)(currPane);
                    Piece p = currPiecePane.getPiece();
                    if(p.getPosition().equals(takenPos)){
                        //Retrieve the taker piece 
                        int[] takerPositions = Piece.getNumericPosition(takerPos);
                        int takerRow = takerPositions[0];
                        int takerColumn = takerPositions[1];
                        Piece takerPiece = ((PiecePane)getGridNode(pieceHolder, takerRow, takerColumn)).getPiece();
                        result[i][j] = takerPiece.getPoints();
                    }
                    else if(p.getPosition().equals(takerPos)){
                        result[i][j] = 0.0;
                        continue;
                    }
                    else{
                        result[i][j] = p.getPoints();
                    }
                }
                else if(currPane instanceof EmptyPane && currPos.equals(takenPos)){
                   //Retrieve the taker piece 
                    int[] takerPositions = Piece.getNumericPosition(takerPos);
                    int takerRow = takerPositions[0];
                    int takerColumn = takerPositions[1];
                    Piece takerPiece = ((PiecePane)getGridNode(pieceHolder, takerRow, takerColumn)).getPiece();
                    result[i][j] = takerPiece.getPoints();
                }
            }
        }
        return result;
    }
    /*
    *Use for retrieving a node from gridpane by specifying the positions and passing GridPane reference
    *No index validation
    *User is liable of type casting the node they have been returned if necessary, in the caller
    *@return desired Node
    */
    public static Node getGridNode(GridPane pane, int row, int column){
        Node result = null;
        ObservableList<Node> children = pane.getChildren();

        for (Node node : children) {
            if(pane.getRowIndex(node) == row && pane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
    //Method for retrieving a path from given friendly king to given checking enemy piece which is an instance of queen or rook or bishop
    //Returns null if there is no path in between two pieces (If they are next to each other vertically, horizontally, or diagonally)
    public static String[] getPath(King friendlyKing, Piece enemyPiece){
        Bishop bishop = null;
        Rook rook = null;
        Queen queen = null;
        
        String[] result = null;
        ArrayList<String> resultList = new ArrayList<>();
        if(enemyPiece instanceof Bishop){
            bishop = (Bishop)(enemyPiece);
        }
        else if(enemyPiece instanceof Rook){
            rook = (Rook)(enemyPiece);
        }
        else if(enemyPiece instanceof Queen){
            queen = (Queen)(enemyPiece);
        }
        
        int kingRow = friendlyKing.getRow();
        int kingColumn = friendlyKing.getColumn();
        
        int enemyRow = enemyPiece.getRow();
        int enemyColumn = enemyPiece.getColumn();
        
        
        //ArrayList<String> path = new ArrayList<>();
        if(bishop != null && Math.abs((kingRow - enemyRow)) == Math.abs(kingColumn - enemyColumn)){
            int biggerRow = kingRow;
            int biggerColumn = kingColumn;
            
            char biggerRowPiece = 'k';
            char biggerColumnPiece = 'k'; //'k' for king, 'b' for bishop
            
            if(kingRow < enemyRow){
                biggerRow = enemyRow;
                biggerRowPiece = 'b';
            }
            if(kingColumn < enemyColumn){
                biggerColumn = enemyColumn;
                biggerColumnPiece = 'b';
            }
            
            if(biggerRowPiece == 'k'){
                //king is lower than bishop
                if(biggerColumnPiece == 'k'){
                    //enemy bishop is northeast location
                    int currRow =  kingRow - 1;
                    int currColumn = kingColumn - 1;
                    
                    while(currRow > enemyRow && currColumn > enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow--;
                        currColumn--;
                    }
                }
                else{
                    //north west
                    int currRow = kingRow - 1;
                    int currColumn = kingColumn + 1;
                    
                    while(currRow > enemyRow && currColumn < enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow--;
                        currColumn++;
                    }
                }
            }
            else{
                if(biggerColumnPiece == 'k'){
                    //southwest direction is towards enemy bishop
                    int currRow = kingRow + 1;
                    int currColumn = kingColumn - 1;
                    
                    while(currRow < enemyRow && currColumn > enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow++;
                        currColumn--;
                    }
                }
                else{
                    //southeast direction is towards enemy bishop
                    int currRow = kingRow + 1;
                    int currColumn = kingColumn + 1;
                    
                    while(currRow < enemyRow && currColumn < enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow++;
                        currColumn++;
                    }
                }
            }
        }
        else if(rook != null && ((enemyRow == kingRow) ^ (enemyColumn == kingColumn))){
            if(enemyRow == kingRow){
                //same row but columns differ
                int biggerColumn = kingColumn;
                char biggerColumnPiece = 'k';
                
                if(enemyColumn > kingColumn){
                    biggerColumn = enemyColumn;
                    biggerColumnPiece = 'r'; //'r' for rook 'k' for king
                }
                if(biggerColumnPiece == 'k'){
                    //go towards left
                    int currColumn = kingColumn - 1;
                    
                    while(currColumn > enemyColumn){
                        String currPos = Piece.positions[kingRow][currColumn];
                        resultList.add(currPos);
                        currColumn--;
                    }
                }
                else{
                    //go towards right from our king
                    int currColumn = kingColumn + 1;
                    while(currColumn < enemyColumn){
                        String currPos = Piece.positions[kingRow][currColumn];
                        resultList.add(currPos);
                        currColumn++;
                    }
                }
            }
            else{
                //columns are same nevertheless rows differ
                int biggerRow = kingRow;
                char biggerRowPiece = 'k';
                
                if(enemyRow > kingRow){
                    biggerRow = enemyColumn;
                    biggerRowPiece = 'r'; //'r' for rook 'k' for king
                }
                if(biggerRowPiece == 'k'){
                    //go towards left
                    int currRow = kingRow - 1;
                    
                    while(currRow > enemyRow){
                        String currPos = Piece.positions[currRow][kingColumn];
                        resultList.add(currPos);
                        currRow--;
                    }
                }
                else{
                    //go towards right from our king
                    int currRow = kingRow + 1;
                    while(currRow < enemyColumn){
                        String currPos = Piece.positions[currRow][kingColumn];
                        resultList.add(currPos);
                        currRow++;
                    }
                }
            }
        }
        else if(queen != null){
            //perform either bishop or rook operations based on king's position
            //initially check for rook operations
            if(enemyRow == kingRow || enemyColumn == kingColumn){
                //perform rook operation
                if(enemyRow == kingRow){
                //same row but columns differ
                int biggerColumn = kingColumn;
                char biggerColumnPiece = 'k';
                
                if(enemyColumn > kingColumn){
                    biggerColumn = enemyColumn;
                    biggerColumnPiece = 'r'; //'r' for rook 'k' for king
                }
                if(biggerColumnPiece == 'k'){
                    //go towards left
                    int currColumn = kingColumn - 1;
                    
                    while(currColumn > enemyColumn){
                        String currPos = Piece.positions[kingRow][currColumn];
                        resultList.add(currPos);
                        currColumn--;
                    }
                }
                else{
                    //go towards right from our king
                    int currColumn = kingColumn + 1;
                    while(currColumn < enemyColumn){
                        String currPos = Piece.positions[kingRow][currColumn];
                        resultList.add(currPos);
                        currColumn++;
                    }
                }
            }
            
        }
        else{
            //Apparently our queen behaves like a bishop when checking the opponent's king
            int biggerRow = kingRow;
            int biggerColumn = kingColumn;
            
            char biggerRowPiece = 'k';
            char biggerColumnPiece = 'k'; //'k' for king, 'b' for bishop
            
            if(kingRow < enemyRow){
                biggerRow = enemyRow;
                biggerRowPiece = 'b';
            }
            if(kingColumn < enemyColumn){
                biggerColumn = enemyColumn;
                biggerColumnPiece = 'b';
            }
            
            if(biggerRowPiece == 'k'){
                //king is lower than bishop
                if(biggerColumnPiece == 'k'){
                    //enemy bishop is northeast location
                    int currRow =  kingRow - 1;
                    int currColumn = kingColumn - 1;
                    
                    while(currRow > enemyRow && currColumn > enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow--;
                        currColumn--;
                    }
                }
                else{
                    //north west
                    int currRow = kingRow - 1;
                    int currColumn = kingColumn + 1;
                    
                    while(currRow > enemyRow && currColumn < enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow--;
                        currColumn++;
                    }
                }
            }
            else{
                if(biggerColumnPiece == 'k'){
                    //southwest direction is towards enemy bishop
                    int currRow = kingRow + 1;
                    int currColumn = kingColumn - 1;
                    
                    while(currRow < enemyRow && currColumn > enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow++;
                        currColumn--;
                    }
                }
                else{
                    //southeast direction is towards enemy bishop
                    int currRow = kingRow + 1;
                    int currColumn = kingColumn + 1;
                    
                    while(currRow < enemyRow && currColumn < enemyColumn){
                        String currPos = Piece.positions[currRow][currColumn];
                        resultList.add(currPos);
                        currRow++;
                        currColumn++;
                    }
                }
            }
            
        }
    }
    if(resultList.isEmpty()){
        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ RETURNING NULL FROM GET PATH");
        return null;
    }
        Object[] resultObjects = (resultList.toArray());
        result = new String[resultObjects.length];
        for(int i = 0; i < resultObjects.length; i++){
            result[i] = (String)(resultObjects[i]);
        }
        System.out.println(result.toString());
        return result;
    }
    public static boolean  contains(Object[] arr, Object elt){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(elt)){
                return true;
            }
        }
        return false;
    }
    public static String getKingPosition(double[][] state, String kingColor){
        double kingValue = Piece.WHITE_KING_POINTS;
        if(kingColor.equals(Piece.BLACK_COLOR)){
            kingValue *= -1;
        }
        String kingPos = null;
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                if(state[i][j] == kingValue){
                    kingPos = Piece.positions[i][j];
                    break;
                }
            }
        }
        System.out.println("Returned king value is: " + kingValue + ", and returned king position is " + kingPos);
        return kingPos;
    }
    public static void printMatrix(double[][] state){
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[i].length; j++){
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
    }
    //Evaluate a given state to check whether it is already checked or not
    //@return true when the given state is already checked
    public static boolean evaluateStateForCheck(ArrayList<Piece> checkingEnemies, String currMoveable, String kingPos){
        //You may stem the process of evaluating whether the friendly king is checked by starting from the friendly king's pos
        //Alternatively we can simply invoke getPath function on each enemy piece and store the ones which check the enemy king
        //If there is none then the we return false otherwise true
        
        //iterate through all of the checkingEnemies' path's between the king and if all of the paths contain currMoveable then return false
        boolean result = false;
        System.out.println("King Pos in evaluateState is " + kingPos);
        King friendlyKing = (King)((PiecePane)(App.getGridNode(pieceHolder, Piece.getRow(kingPos), Piece.getColumn(kingPos)))).getPiece();
        
        //retrieve the current state of the game
        double[][] state = App.retrieveGameState(kingPos, kingPos);
        state[Piece.getRow(kingPos)][Piece.getRow(kingPos)] = friendlyKing.getPoints();
        for(Piece checkingEnemy: checkingEnemies){
            //String[] currPath = App.getPath(friendlyKing, checkingEnemy);
            String[] currPath = null;
            if(checkingEnemy instanceof Bishop){
                currPath = Bishop.showMoveables2(state, checkingEnemy.getRow(), checkingEnemy.getColumn());
            }
            else if(checkingEnemy instanceof Rook){
                currPath = Rook.showMoveables2(state, checkingEnemy.getRow(), checkingEnemy.getColumn());
            }
            else if(checkingEnemy instanceof Queen){
                currPath = Queen.showMoveables2(state, checkingEnemy.getRow(), checkingEnemy.getColumn());
            }
            if(currPath != null){
                boolean found = false;
                System.out.println("?????Curr moveable is " + currMoveable);
                System.out.println("???????We are in evaluateStateForCheck in app class to check the current path");
                for(int i = 0; i < currPath.length; i++){
                    System.out.println("?????????Curr square along the path is " + currPath[i]);
                    if(currPath[i].equals(currMoveable)){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    return true;
                }
            }
        }
        return result;
    }
}
    