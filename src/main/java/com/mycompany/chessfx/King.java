/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

import java.util.ArrayList;
import javafx.scene.layout.StackPane;

/**
 *
 * @author yigit
 */
public class King extends Piece{
    private boolean isMoved = false;
    
    public King(char color){
        super(color, 90);
        if(super.getColor().equals(Piece.WHITE_COLOR)){
            this.setPoints(WHITE_KING_POINTS);
        }
        else{
            this.setPoints(BLACK_KING_POINTS);
        }
    }
    @Override
    public void move() {
    
    }
    public void castle(){
        
    }
    public boolean isChecked(){
        return true;
    }

    @Override
    public Object[] showMoveables() {
        final int row = super.getRow();
        final int column = super.getColumn();
        
        String friendlyColor = super.getColor();
        String enemyColor = super.getEnemyColor();
        
        ArrayList<String> moveables = new ArrayList<>();
        
        //check for surrounding squares
        //start from top left corner move with respect to clockwise direction
        int currRow = row - 1;
        int currColumn = column - 1;
        if(currColumn < 0){
            currColumn = 0;
        }
        
        while(currRow >= 0 && currColumn >= 0 && currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            
            String currPosition = Piece.positions[currRow][currColumn];
            if(currSquare instanceof EmptyPane){
                if(!EmptyPane.isSquareThreatened( currPosition, enemyColor)){
                    //moveable square
                    moveables.add(currPosition);
                }
            }
            else{// enemy king spotted
                //check whether currSquare contains an enemy piece and ensure that, that piece is not covered by another enemy piece
                Piece currPiece = ((PiecePane)(currSquare)).getPiece();
            }
            currRow--;
            currColumn++;
        }
    }
}
