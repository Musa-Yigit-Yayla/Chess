/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
import javafx.scene.layout.StackPane;
public class EmptyPane extends StackPane{
    public EmptyPane(){
        this.setOnClicked();
    }
    private void setOnClicked(){
        
    }
    //Check whether user intends to castle his/her king
    private boolean isCastling(){
        
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
        
    }
}
