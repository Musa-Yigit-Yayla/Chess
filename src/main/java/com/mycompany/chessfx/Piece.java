/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.lang.Comparable;

/**
 *
 * @author yigit
 */
public abstract class Piece implements Comparable{
    private String color;
    private PiecePane piecePane;
    private double points; //value of the piece
    private int row;
    private int column;
    private String currPosition;
    
    public static final String BLACK_COLOR = "black";
    public static final String WHITE_COLOR = "white";
    public static final double WHITE_PAWN_POINTS = 1.0;
    public static final double WHITE_ROOK_POINTS = 5.0;
    public static final double WHITE_KNIGHT_POINTS = 3.0;
    public static final double WHITE_BISHOP_POINTS = 3.15;
    public static final double WHITE_QUEEN_POINTS = 9.0;
    public static final double WHITE_KING_POINTS = 90.0;
    public static final double BLACK_PAWN_POINTS = -1.0;
    public static final double BLACK_ROOK_POINTS = -5.0;
    public static final double BLACK_KNIGHT_POINTS = -3.0;
    public static final double BLACK_BISHOP_POINTS = -3.15;
    public static final double BLACK_QUEEN_POINTS = -9.0;
    public static final double BLACK_KING_POINTS = -90.0;
    
    public static final String[][] positions = {{"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
                                                {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
                                                {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
                                                {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
                                                {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
                                                {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
                                                {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
                                                {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"},};
    
    //W or B, or w or b for color, respective points for pieces, bishop is 3.15
    public Piece(char color, double points){
        color = Character.toUpperCase(color);
        if(color == 'W'){
            this.color = WHITE_COLOR;
            
        }
        else{
            this.color = BLACK_COLOR;
            points *= -1; //passed points is assumed to be positive
        }
        this.points = points;
        this.piecePane = new PiecePane(this);
    }
    //When this constructor is invoked we don't instantiate peicePane, it is for instantiating temporary representations of an existent piece 
    public Piece(char color, double points, int row, int column){
        color = Character.toUpperCase(color);
        if(color == 'W'){
            this.color = WHITE_COLOR;
            //passed points is assumed to be positive
        }
        else{
            this.color = BLACK_COLOR;
            points *= -1; 
        }
        this.points = points;
        this.setPosition(row, column);
        this.piecePane = new PiecePane(this); //IMPORTANT, THIS LINE COULD BE PROBLEMATIC DUE TO EARLIER USE OF PIECE INSTANCES
    }
    public String getColor(){
        return this.color;
    }
    public PiecePane getPiecePane(){
        return this.piecePane;
    }
    public double getPoints(){
        return this.points;
    }
    //Set the value of piece, careful for color representation -, + signs
    public void setPoints(double points){
        this.points = points;
    }
    //Invoke from event handling methods when applicable
    public void move(String nextPos){
        System.out.println("---Next pos is " + nextPos);
        if((Move.getTurn() == Move.WHITE_TURN && this.getColor().equals(Piece.WHITE_COLOR) || (Move.getTurn() == Move.BLACK_TURN && this.getColor().equals(Piece.BLACK_COLOR)))){
            System.out.println("---We are in the move function of a piece and it's our turn");
            //retrieve the StackPane correspondance on the given position and evaluate whether it's a PiecePane instance or EmptyPane instance
            //then proceed accordingly
            
            int nextRow = Piece.getRow(nextPos);
            int nextColumn = Piece.getColumn(nextPos);
            
            GridPane pieceHolder = App.getPieceHolder();
            StackPane nextPosPane = (StackPane)(App.getPieceHolderNode(nextRow, nextColumn));
            
            //first we must ensure that we have this pos as a moveable location in this piece instance
            Object[] currMoveablesArray = (this.showMoveables());
            String[] currMoveables = new String[currMoveablesArray.length];
            //convert the object array containing strings into a new string array for easier use
            for(int i = 0; i < currMoveables.length; i++){
                String curr = (String)(currMoveablesArray[i]);
                currMoveables[i] = curr;
            }
            //retrieve the checking pieces !!!THE FOLLOWING BLOCK IS TAKEN FROM PIECEPANE
            
                double[][] presentState = App.retrieveGameState(this.getPosition(), this.getPosition());
            String presentKingPos = App.getKingPosition(presentState, this.color);
            King friendlyKing = (King)((PiecePane)(App.getGridNode(pieceHolder, Piece.getRow(presentKingPos), Piece.getColumn(presentKingPos)))).getPiece();
                    //retrieve each and every piece that checks our king by using getPath function
                    ArrayList<Piece> checkingEnemies = new ArrayList<>();
                    for(int i = 0; i < App.currentPieces.size(); i++){
                       Piece currPiece = App.currentPieces.get(i);
                       if(!(currPiece.getColor().equals(this.color)) && !(currPiece instanceof King )){
                           //we ensured that we have an enemy piece which is not the king, now we must check whether it checks our king
                           String[] path = App.getPath(friendlyKing, currPiece);
                           if(path != null){
                               checkingEnemies.add(currPiece);
                           }
                       }
                       
                    }
                    
            boolean x = false;
            boolean y = false;
            boolean u = false;
            boolean z = false;
            
            //THE FOLLOWING BLOCK IS TAKEN FROM PIECE PANE EVENT HANDLERS
            boolean isMoveable = false; //can we move to the given nextPos
            for(int i = 0; i < currMoveables.length; i++){
                String currMoveable = currMoveables[i];
                System.out.println("---Curr moveable of the piece is: " + currMoveable);
                if(currMoveable.equals(nextPos)){
                    double[][] nextPossibleState = App.retrieveGameState(this.currPosition, currMoveable);
                    String kingPos = App.getKingPosition(nextPossibleState, color);

                    //System.out.println("nextPossibleState is ");
                    //App.printMatrix(nextPossibleState);
                    x = currMoveable.equals(nextPos);
                    y = !(App.isChecked(nextPossibleState, color, kingPos));
                    if(!y){
                        u = !App.evaluateStateForCheck(checkingEnemies, currMoveable, kingPos);
                        z = (this instanceof King) || u;
                    }
                    boolean isFriendlyKingSafe = (x && (y || z));
                    //check the second condition of the above z boolean variable only when the first one is not satisfied
                    System.out.println("*******************************************X, Y, Z  are respectively " + x + "," + y + "," + z);
                    System.out.println("******************************isFriendlyKingSafe evaluates to " + isFriendlyKingSafe);
                    if(isFriendlyKingSafe){
                        System.out.println("!!!!!We are about to move a piece without exposing the friendly king");
                        isMoveable = true;
                        break;
                    }
                }
            }
            System.out.println("isMoveable is: " + isMoveable);
            if(!isMoveable){
                //return back to the caller since we don't have the given pos as a moveable piece
                return;
            }
            
            String friendlyColor = this.color;
            String enemyColor = this.getEnemyColor();
            if(nextPosPane instanceof PiecePane){
                System.out.println("Next pos pane is a PiecePane instance");
                Piece nextPosPiece = ((PiecePane)((PiecePane) (nextPosPane))).getPiece();
                if(nextPosPiece.getColor().equals(enemyColor) && !(nextPosPiece instanceof King)){
                    //Invoke the take method to nextPosPiece and pass this Piece instance as takerPiece parameter
                    nextPosPiece.take(this);
                }
                //Last but not least if the enemy king is checked after this successfull move simply set it's outer frame to red
                    double[][] currState = App.retrieveGameState(this.getPosition(), this.getPosition());
                    String enemyKingPos = App.getKingPosition(currState , this.getEnemyColor());
                    int enemyKingRow = Piece.getRow(enemyKingPos);
                    int enemyKingColumn = Piece.getColumn(enemyKingPos);
                    
                    if(App.isChecked(currState, this.getEnemyColor(), enemyKingPos)){
                        //set the outer frame of the enemy king
                        King enemyKing = ((King)((PiecePane)(App.getPieceHolderNode(enemyKingRow, enemyKingColumn))).getPiece());
                        enemyKing.setCheckedFrame();
                    }
            }
            //we need to have a logic flow separating what to do when the nextPosPane is an instance of EmptyPane
            else if(nextPosPane instanceof EmptyPane){ //bool logic is written just to enhance readability
                //move our piece to empty pane if applicable
                if(!y && u && !(this instanceof King)){
                    //here we are about to cover our king by moving this piece into designated location in-place
                    //I reckon if we made it into this code block by satisfying the required logic, we must be able to save our king from being checked
                    
                    //perform the required operations accordingly
                    System.out.println("????????????GOD SAVE THE KING!");
                    System.out.println("***Asked move is a valid move, friendly king is not checked");
                    //first remove the piece pane and the empty destination from gridHolder
                    pieceHolder.getChildren().remove(this.piecePane);
                    pieceHolder.getChildren().remove(nextPosPane);
                    
                    /*int nextRow = Piece.getRow(nextPos);
                    int nextColumn = Piece.getColumn(nextPos);*/
                    
                    int pieceRow = this.row;
                    int pieceColumn = this.column;
                    
                    //sdd the current piece to the next locations and don't forget to add the empty pane back again by instantiating a new one
                    EmptyPane newEmptyPane = new EmptyPane(Piece.positions[pieceRow][pieceColumn]);
                    newEmptyPane.setEventHandling();
                    
                    pieceHolder.add(this.piecePane, nextColumn, nextRow);
                    pieceHolder.add(newEmptyPane, pieceColumn, pieceRow);
                    
                    //set the row and column data fields of the piece appropriately
                    this.setPosition(nextRow, nextColumn);
                    //erase the moveables which were visible before we have made the move
                    App.eraseMoveablesFromPane();
                    
                    //Last but not least if the enemy king is checked after this successfull move simply set it's outer frame to red
                    double[][] currState = App.retrieveGameState(this.getPosition(), this.getPosition());
                    String enemyKingPos = App.getKingPosition(currState , this.getEnemyColor());
                    int enemyKingRow = Piece.getRow(enemyKingPos);
                    int enemyKingColumn = Piece.getColumn(enemyKingPos);
                    
                    if(App.isChecked(currState, this.getEnemyColor(), enemyKingPos)){
                        //set the outer frame of the enemy king
                        King enemyKing = ((King)((PiecePane)(App.getPieceHolderNode(enemyKingRow, enemyKingColumn))).getPiece());
                        enemyKing.setCheckedFrame();
                    }
                    
                    return;
                }
                //Check whether our friendly king is exposed or not after the move
                
                System.out.println("*** Current nextPosPane is an instance of EmptyPane");
                //check the next game state here directly
                double[][] nextState = App.retrieveGameState(this.getPosition(), nextPos);
                //Retrieve the friendly king's pos so we can pass it as an argument to stateChecker
                String friendlyKingPos = App.getKingPosition(nextState, this.color);
                if(!App.isChecked(nextState, this.color, friendlyKingPos)){
                    //move it to the empty pane and make the current pane an empty pane
                    System.out.println("***Asked move is a valid move, friendly king is not checked");
                    //first remove the piece pane and the empty destination from gridHolder
                    pieceHolder.getChildren().remove(this.piecePane);
                    pieceHolder.getChildren().remove(nextPosPane);
                    
                    /*int nextRow = Piece.getRow(nextPos);
                    int nextColumn = Piece.getColumn(nextPos);*/
                    
                    int pieceRow = this.row;
                    int pieceColumn = this.column;
                    
                    //sdd the current piece to the next locations and don't forget to add the empty pane back again by instantiating a new one
                    EmptyPane newEmptyPane = new EmptyPane(Piece.positions[pieceRow][pieceColumn]);
                    newEmptyPane.setEventHandling();
                    
                    pieceHolder.add(this.piecePane, nextColumn, nextRow);
                    pieceHolder.add(newEmptyPane, pieceColumn, pieceRow);
                    
                    //set the row and column data fields of the piece appropriately
                    this.setPosition(nextRow, nextColumn);
                    //erase the moveables which were visible before we have made the move
                    App.eraseMoveablesFromPane();
                    
                    //Last but not least if the enemy king is checked after this successfull move simply set it's outer frame to red
                    double[][] currState = App.retrieveGameState(this.getPosition(), this.getPosition());
                    String enemyKingPos = App.getKingPosition(currState , this.getEnemyColor());
                    int enemyKingRow = Piece.getRow(enemyKingPos);
                    int enemyKingColumn = Piece.getColumn(enemyKingPos);
                    
                    if(App.isChecked(currState, this.getEnemyColor(), enemyKingPos)){
                        //set the outer frame of the enemy king
                        King enemyKing = ((King)((PiecePane)(App.getPieceHolderNode(enemyKingRow, enemyKingColumn))).getPiece());
                        enemyKing.setCheckedFrame();
                    }
                }
                else{
                    System.out.println("*!*!*Asked move is not a valid move, friendly king is checked");
                }
            }
        }
        
    }
    public abstract Object[] showMoveables(); // return all moveable squares, as a string array once a piece object is clicked
    // will be used when an enemy piece takes a piece instance. We will have empty method stub in King subclass
    public void take(Piece takerPiece){
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        String currPosition = this.getPosition();
        if(takerPiece instanceof King){
            //check whether the enemy king can take this piece (Check whether this piece is guarded by any friendly piece)
            String kingColor = takerPiece.getColor();
            //Pass the enemy king color to the isSquareThreatened so as to see whether we have that square covered by a friendly piece
            if(!EmptyPane.isSquareThreatened(currPosition, kingColor)){
                //take current piece
                System.out.println("£££We are about to take an enemy piece with a king");
                this.takeHelper(takerPiece);
                //After having taken the piece we must add it to the corresponding takenPiecePane
                App.addTakenPiece(this);
            }
        }
        //calculate moveables of takerPiece and see whether enemy king is checked after having taken this piece
        else if(takerPiece != null){
            Object[] takerMoveablesObject = (takerPiece.showMoveables());
            String[] takerMoveables = new String[takerMoveablesObject.length];
            
            for(int i = 0; i < takerMoveables.length; i++){
                takerMoveables[i] = (String)(takerMoveablesObject[i]);
            }
            
            for(int i = 0; i < takerMoveables.length; i++){
                String currMoveable = takerMoveables[i];
                if(currPosition.equals(currMoveable)){
                    //check whether an takerPiece's friendly king is exposed after having taken this piece
                    double[][] nextState = App.retrieveGameState(takerPiece.getPosition(), currPosition);
                    //Retrieve taker's king's position
                    String enemyKingPosition = App.getKingPosition(nextState, takerPiece.getColor());
                    if(!App.isChecked(nextState, takerPiece.getColor(), enemyKingPosition)){
                        this.takeHelper(takerPiece);
                        //After having taken the piece we must add it to the corresponding takenPiecePane
                        App.addTakenPiece(this);
                    }
                    break;
                }
            }
        }
    }
    
    private void takeHelper(Piece taker){
        String friendlyColor = this.getColor();
        String enemyColor = this.getEnemyColor();
        
        if(taker.getColor().equals(enemyColor)){
            System.out.println("We are about to take a piece and update the pieceHolder accordingly");
            
            String takerPosition = taker.currPosition;
            String takenPosition = this.currPosition;
            
            int takenRow = this.row;
            int takenColumn = this.column;
            int[] takerPos = Piece.getNumericPosition(takerPosition);
            int takerRow = takerPos[0];
            int takerColumn = takerPos[1];
            GridPane pieceHolder = App.getPieceHolder();
            
            PiecePane takenPane = (PiecePane)(App.getGridNode(pieceHolder, takenRow, takenColumn));
            PiecePane takerPane = (PiecePane)(App.getGridNode(pieceHolder, takerRow, takerColumn));
            
            Piece takenPiece = takenPane.getPiece();
            double takenPoints = takenPiece.getPoints();
            
            App.updateGamePoints(takenPoints);
            App.currentPieces.remove(takenPiece);
            App.takenPieces.add(takenPiece); // add the piece that has been killed
            
            //Replace takenPane with takerPane and place an empty pane instance to taker pane position
            pieceHolder.getChildren().remove(takerPane);
            pieceHolder.add(new EmptyPane(taker.getPosition()), takerColumn, takerRow); //YOU MIGHT WANT TO REVERT THE LATEST 2 PARAMETERS
            pieceHolder.getChildren().remove(takenPane);
            pieceHolder.add(takerPane, takenColumn, takenRow); //YOU MIGHT WANT TO REVERT THE PARAMETERS HERE AS WELL
            taker.setPosition(takenRow, takenColumn);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Alternate the move turn by instantiating a new move
            Move newMove = new Move(taker, takerPosition, takenPosition);
        }
    }
    //Call this after the Piece instance is either successfuly has been moved or during initialization process
    //No bounds checking performed
    public void setPosition(int row, int column){
        this.row = row;
        this.column = column;
        this.currPosition = Piece.positions[row][column];
    }
    /*
    *@return the position as a whole string, specified in the static 2d matrix format of positions
    */
    public String getPosition(){
        return this.currPosition;
    }
    //Returns the column based on the position of the current piece instance
    //0 based indexing
    public int getColumn(){
        int result = -1;
        if(this.currPosition != null){
            char ch = this.currPosition.charAt(0);
            switch(ch){
                case 'a': result = 0; break;
                case 'b': result = 1; break;
                case 'c': result = 2; break;
                case 'd': result = 3; break;
                case 'e': result = 4; break;
                case 'f': result = 5; break;
                case 'g': result = 6; break;
                case 'h': result = 7; break;
            }
        }
        return result;
    }
    //Returns the row based on the position of the current piece instance
    //0 based indexing
    public int getRow(){
        int result = -1;
        if(this.currPosition != null){
            char ch = this.currPosition.charAt(1);
            switch(ch){
                case '8': result = 0; break;
                case '7': result = 1; break;
                case '6': result = 2; break;
                case '5': result = 3; break;
                case '4': result = 4; break;
                case '3': result = 5; break;
                case '2': result = 6; break;
                case '1': result = 7; break;
            }
        }
        return result;
    }
    public static int getColumn(String givenPos){
        int result = -1;
        if(givenPos != null){
            char ch = givenPos.charAt(0);
            switch(ch){
                case 'a': result = 0; break;
                case 'b': result = 1; break;
                case 'c': result = 2; break;
                case 'd': result = 3; break;
                case 'e': result = 4; break;
                case 'f': result = 5; break;
                case 'g': result = 6; break;
                case 'h': result = 7; break;
            }
        }
        return result;
    }
    //Static function to retrieve the numerical position for empty panes
    public static int getRow(String givenPos){
        int result = -1;
        if(givenPos != null){
            char ch = givenPos.charAt(1);
            switch(ch){
                case '8': result = 0; break;
                case '7': result = 1; break;
                case '6': result = 2; break;
                case '5': result = 3; break;
                case '4': result = 4; break;
                case '3': result = 5; break;
                case '2': result = 6; break;
                case '1': result = 7; break;
            }
        }
        return result;
    }
    public String getEnemyColor(){
        if(this.color.equals(WHITE_COLOR)){
            return BLACK_COLOR;
        }
        else{
            return WHITE_COLOR;
        }
    }
    //same implementations with getRow and getColumn combined in a static method
    //returns an array of length 2, first element is row second is column
    public static int[] getNumericPosition(String pos){
        int[] resultArr = new int[2];
        int result = -1;
        //Row
            char ch = pos.charAt(1);
            switch(ch){
                case '8': result = 0; break;
                case '7': result = 1; break;
                case '6': result = 2; break;
                case '5': result = 3; break;
                case '4': result = 4; break;
                case '3': result = 5; break;
                case '2': result = 6; break;
                case '1': result = 7; break;
            }
            resultArr[0] = result;
        result = -1;
        //Column
        ch = pos.charAt(0);
        switch(ch){
               case 'a': result = 0; break;
               case 'b': result = 1; break;
               case 'c': result = 2; break;
               case 'd': result = 3; break;
               case 'e': result = 4; break;
               case 'f': result = 5; break;
               case 'g': result = 6; break;
               case 'h': result = 7; break;
        }
        resultArr[1] = result; 
         
        return resultArr;
    }

    @Override
    //We favor comparing pieces with respect to their absolute points value, regardless of their color
    //We expect the client to pass a Piece sub instance
    //We throw a illegal argument exception if the passed object is not a piece sub instance
    public int compareTo(Object o) throws IllegalArgumentException{
        if(o instanceof Piece){
            double diff = Math.abs(this.points) - Math.abs(((Piece)(o)).points);
            if(diff > 0){
                return 1;
            }
            else if(diff == 0){
                return 0;
            }
            else{
                return -1;
            }
        }
        else{
            throw new IllegalArgumentException("Passed object is not a piece instance");
        }
    }
}