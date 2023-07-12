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
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
    
    public static ArrayList<Piece> currentPieces = new ArrayList<>(); // pieces that are alive and displayed
    public static ArrayList<Piece> takenPieces = new ArrayList<>(); // taken pieces
    public static Piece selectedPiece; // currently selected piece, will be used to paint the selected square to green
    
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
        newImg.setFitHeight(80.0);
        newImg.setFitWidth(80.0);
        pane.getChildren().add(newImg);
        pane.setPrefSize(90, 90);
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
    public static StackPane getPieceHolderNode(int row, int column){
        Node result = null;
        ObservableList<Node> children = pieceHolder.getChildren();

        for (Node node : children){
            if(pieceHolder.getRowIndex(node) == row && pieceHolder.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

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
                if(currKing.isChecked()){
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
                        if(!isChecked(nextGameState, friendlyColor)){
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
                            if(!isChecked(nextGameState, friendlyColor)){
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
                            if(!isChecked(nextGameState, friendlyColor)){
                                return false;
                            }
                        }
                    }
                }
                //If not, we should finally check whether we can obstruct the path from the enemy piece to our king with a friendly piece
                
            }
        }
        
    }
    public boolean isStaleMate(){
        String friendlyColor;
        if(Move.getTurn() == 1){
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
        if(!friendlyKing.isChecked() && kingMoveables.length != 0){//
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
    //Static utility method for checking whether if a move is valid, instantiate game state representing aftermath of a possible move and check
    //Whether our friendly king is not checked after that move
    public static boolean isChecked(double[][] state, String kingColor){
        //ToDo
    }
    //Method to retrieve gamestate representation subsequent to a move that could be made
    public double[][] retrieveGameState(String takerPos, String takenPos){
        double[][] result = new double[8][8];
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                StackPane currPane = (StackPane)(getGridNode(pieceHolder, i, j));
                if(currPane instanceof PiecePane){
                    PiecePane currPiecePane = (PiecePane)(currPane);
                    Piece p = currPiecePane.getPiece();
                    if(p.getPosition().equals(takerPos)){
                        continue;
                    }
                    else if(p.getPosition().equals(takenPos)){
                        //Retrieve the taker piece 
                        int[] takerPositions = Piece.getNumericPosition(takerPos);
                        int takerRow = takerPositions[0];
                        int takerColumn = takerPositions[1];
                        Piece takerPiece = ((PiecePane)getGridNode(pieceHolder, takerRow, takerColumn)).getPiece();
                        result[i][j] = takerPiece.getColumn();
                    }
                    else{
                        result[i][j] = p.getPoints();
                    }
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
                    //enemy bishop is northwest location
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
                    //north east
                    int currRow = kingRow - 1;
                    int currColumn = kingRow + 1;
                    
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
            else{
                //perform bishop operation
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
                    //enemy bishop is northwest location
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
                    //north east
                    int currRow = kingRow - 1;
                    int currColumn = kingRow + 1;
                    
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
    }
    if(resultList.isEmpty()){
        return null;
    }
        return (String[])(resultList.toArray());
    }
}
    