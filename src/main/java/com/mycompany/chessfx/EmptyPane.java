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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
public class EmptyPane extends StackPane{
    public EmptyPane(){
        this.setOnClicked();
    }
    private void setOnClicked(){
        
    }
    //Check whether user intends to castle his/her king
    //Invoke this when user presses an empty pane (square in checker board) which may enable our king to castle providing that king and rooks are not moved
    //You may not have to complete this method, go through implementing move functions of piece subclasses, then take a look at here if necessary
    private boolean isCastling(){
        //retrieve nodes from pieceHolder pane of App class and find this empty pane
        GridPane pieceHolder = App.getPieceHolder();
        
        
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
            String[] currMoveables;
            if(!(currPiece instanceof King)){
                currMoveables = (String[])currPiece.showMoveables(); // !! CAREFUL, THIS LINE MAY LEAD TO INFINITE RECURSION DUE TO KING'S METHOD
                for(int j = 0; j < currMoveables.length; i++){
                    if(currMoveables[j].equals(pos)){
                        return true;
                    }
                }
            }
            else{
                //check manually so as to avoid infinite recursion
                
            }
            //else if(currPiece instanceof Bishop){
                
            //}
        }
        //after having checked each and every enemy piece for whether it threatens our square, if we haven't returned yet
        //we can return the result which is a boolean false
        return result;
    }
}
