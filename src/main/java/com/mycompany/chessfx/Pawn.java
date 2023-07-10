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
public class Pawn extends Piece {
   private boolean isMoved = false;
   public Pawn(char color){
       super(color, 1);
       if(this.getColor().equals(Piece.WHITE_COLOR)){
           this.setPoints(WHITE_PAWN_POINTS);
       }
       else{
           this.setPoints(BLACK_PAWN_POINTS);
       }
   }
   public void promote(){
           
   }
   @Override
   public void move(){
       
   }
   
    //Similar to other methods, we do not check for whether friendly king is exposed
    @Override
    public Object[] showMoveables() {
        final int row = this.getRow();
        final int column = this.getColumn();
        
        String friendlyColor = this.getColor();
        String enemyColor = this.getEnemyColor();
        
        int dy;
        if(friendlyColor.equals(WHITE_COLOR)){
            dy = 1;
        }
        else{
            dy = -1;
        }
        
        ArrayList<String> moveables = new ArrayList<>();
        boolean isFirstAdded = false;
        //check whether we can kickstart by moving two squares
        if(!this.isMoved){
            //check whether two squares up ahead are empty
            
            if(row + 2 * dy < 8 && row + 2 * dy >= 0){
                //check whether we have the following two squares as EmptyPane instances
                
                StackPane currSquare = App.getPieceHolderNode(row + dy, column);
                
                isFirstAdded = false; // first moveable square
                if((currSquare instanceof EmptyPane)){
                   moveables.add(Piece.positions[row + dy][column]);
                   isFirstAdded = true;
                }
                currSquare = App.getPieceHolderNode(row + 2 * dy, column);
                if(currSquare instanceof EmptyPane && isFirstAdded){
                    moveables.add(Piece.positions[row + 2 * dy][column]);
                }
            }
        }
        //check for the next square upahead, left and right squares that we threaten, and en-passant respectively
        if(!isFirstAdded){
            StackPane currSquare = App.getPieceHolderNode(row + dy, column);              
            if((currSquare instanceof EmptyPane)){
                moveables.add(Piece.positions[row + dy][column]);
                isFirstAdded = true;
            }
        }
        //check for left and right diagonal squares
        if(column - 1 >= 0 && row + dy >= 0 && row + dy < 8){
            StackPane currSquare = App.getPieceHolderNode(row + dy, column - 1);
            if(currSquare instanceof PiecePane){
                Piece currPiece = ((PiecePane)(currSquare)).getPiece();
                if(currPiece.getColor().equals(enemyColor)){
                    //Since we reckon that current game state is a valid state, we disregard the case in which the piece at the position is an enemy
                    //king
                    moveables.add(Piece.positions[row + dy][column - 1]);
                }
            }
        }
        if(column + 1 < 8 && row + dy >= 0 && row + dy < 8){
            StackPane currSquare = App.getPieceHolderNode(row + dy, column + 1);
            if(currSquare instanceof PiecePane){
                Piece currPiece = ((PiecePane)(currSquare)).getPiece();
                if(currPiece.getColor().equals(enemyColor)){
                    //Since we reckon that current game state is a valid state, we disregard the case in which the piece at the position is an enemy
                    //king
                    moveables.add(Piece.positions[row + dy][column + 1]);
                }
            }
        }
        //check for en-passant by evaluating the last move
        Move lastMove = Move.getLastMove();
        if(lastMove != null && lastMove.getPiece() instanceof Pawn){
            Pawn possibleEnemy = (Pawn)lastMove.getPiece(); // logically we can expect this to be an enemy pawn but we will still check
            if(possibleEnemy.getColor().equals(enemyColor)){
                String prevPos = lastMove.getPrevPos();
                String newPos = lastMove.getNewPos();
                
                //check whether these positions differ by only two columns and whether if newPos is at the same level with our current pos of this pawn
                boolean isEnPassantable = this.getRow() == possibleEnemy.getRow() && Math.abs(Integer.parseInt(prevPos.substring(1)) - Integer.parseInt(newPos.substring(1))) == 2;
                if(isEnPassantable){
                    moveables.add(newPos);
                }
            }
        }
        return moveables.toArray();
    }
}
