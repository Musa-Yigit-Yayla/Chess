/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
public class EmptyPane extends StackPane{
    private String position;
    private Rectangle outerSquare;
    
    public EmptyPane(String pos){
        this.position = pos;
        this.setEventHandling(); //Set the event handling procedure from the constructor, user can manually set it again aswell
    }
    private void setOnClickedAction(){
        System.out.println("An empty pane has been clicked");
        //If we have a selected piece, try and move that piece to this empty pane
        if(App.selectedPiece != null){
            String selectedColor = App.selectedPiece.getColor();
            
            System.out.println("The selected piece is not null");
            
            boolean turnChecker = (selectedColor.equals(Piece.WHITE_COLOR) && Move.getTurn() == Move.WHITE_TURN) || (selectedColor.equals(Piece.BLACK_COLOR) && Move.getTurn() == Move.BLACK_TURN);  
            System.out.println("Turnchecker is " + turnChecker);
            if(turnChecker){
                
                //Move the selected piece to this location if possible
                
                //!!! WE MUST RETRIEVE THE LOCATION IF THIS EMPTY PANE CAREFULLY !!!!
                String nextPos = this.position;
                /*for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        StackPane currNode = App.getPieceHolderNode(i, j);
                        if(this.equals(currNode)){
                            nextPos = Piece.positions[i][j];
                        }
                    }
                }*/
                //ObservableList<Node> pieceHolderNodes = App.getPieceHolder().getChildren();
                if(nextPos == null){
                    System.out.println("CURRENT EMPTY PANE POSITION IS NULL");
                }
                else{
                    System.out.println("Next pos is " + nextPos);
                }
                App.selectedPiece.move(nextPos);
            }
        }
    }
    //Check whether user intends to castle his/her king
    //Invoke this when user presses an empty pane (square in checker board) which may enable our king to castle providing that king and rooks are not moved
    //You may not have to complete this method, go through implementing move functions of piece subclasses, then take a look at here if necessary
    private boolean isCastling(){
        //retrieve nodes from pieceHolder pane of App class and find this empty pane
        GridPane pieceHolder = App.getPieceHolder();
        
        return false; //ToDo
    }
    public void setOuterFrame(boolean isAdded) {
        if(this.outerSquare != null){
            //remove the previous outer square representation
            this.getChildren().remove(this.outerSquare);
            this.outerSquare = null;
        }
        if(isAdded){
            this.outerSquare = new Rectangle(PiecePane.OUTER_SQUARE_LENGTH, PiecePane.OUTER_SQUARE_LENGTH);
            this.outerSquare.setFill(PiecePane.FILL_COLOR);
            this.outerSquare.setStroke(PiecePane.MOVEABLE_COLOR);
            this.outerSquare.setStrokeWidth(PiecePane.OUTER_SQUARE_STROKE_WIDTH);
            this.getChildren().add(this.outerSquare);
        }
    }
    //Pass the square's coordinates, and it will check whether the square in the given position is threatened by any enemy piece(s)
    //Use when castling and after each move to see whether a king is checked
    //Can be invoked on any square regardless of whether it holds a piece or not
    //@return true when the square is threatened by the enemy
    public static boolean isSquareThreatened(String pos, String friendlyColor){
        String enemyColor = null;
        if(friendlyColor.equals(Piece.WHITE_COLOR)){
            enemyColor = Piece.BLACK_COLOR;
        }
        else{
            enemyColor = Piece.WHITE_COLOR;
        }
        //Get all enemy colored pieces
        ArrayList<Piece> enemyPieces = new ArrayList<>();
        for(Piece e: App.currentPieces){
            //traverse each and every alive piece and obtain the ones that are considered as enemy
            if(e.getColor().equals(enemyColor)){
                enemyPieces.add(e);
            }
        }
        boolean result = false;
        
        
        
        for(int i = 0; i < enemyPieces.size(); i++){
            Piece currPiece = enemyPieces.get(i);
            //int currRow = currPiece.getRow();
            //int currColumn = currPiece.getColumn();
            Object[] currMoveables;
            if(!(currPiece instanceof King)){
                currMoveables = currPiece.showMoveables(); // !! CAREFUL, THIS LINE MAY LEAD TO INFINITE RECURSION DUE TO KING'S METHOD
                for(int j = 0; j < currMoveables.length; i++){
                    if(((String)(currMoveables[j])).equals(pos)){
                        return true;
                    }
                }
            }
            else{
                //check manually so as to avoid infinite recursion
                //current piece is guaranteed to be the enemy king
                int row = Piece.getRow(pos);
                int column = Piece.getColumn(pos);
                
                int kingRow = currPiece.getRow();
                int kingColumn = currPiece.getColumn();
                for(int k = -1; k < 2; k++){
                    for(int j = -1; j < 2; j++){
                        int currRow = kingRow + k;
                        int currColumn = kingColumn + j;
                        
                        if(currRow == kingRow && currColumn == kingColumn){
                            continue;
                        }
                        if(currRow == row && currColumn == column){
                            return true;
                        }
                    }
                }
                
            }
            //else if(currPiece instanceof Bishop){
                
            //}
        }
        //after having checked each and every enemy piece for whether it threatens our square, if we haven't returned yet
        //we can return the result which is a boolean false
        return result;
    }

    public void setEventHandling() {
       this.setOnMouseClicked( e -> {
          this.setOnClickedAction();
       });
    }    
}
