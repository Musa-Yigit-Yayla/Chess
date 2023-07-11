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
        return true; //ToDo
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
        int counter = 3;
        if(currColumn < 0){
            currColumn = 0;
            counter--; // decrement counter by one since we have a square that is out of bounds, (we will check only 2 squares at this row)
        }
        
        
        while(currRow >= 0 && currColumn >= 0 && currColumn < 8 && counter > 0){
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
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, enemyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        
        currColumn = column - 1;
        currRow = row;
        counter = 3;
        if(currColumn < 0){
            currColumn = 0;
            counter--;
        }
        while(currRow >= 0 && currColumn >= 0 && currColumn < 8 && counter > 0){
            if(currRow == row && currColumn == column){
                counter--;
                continue;
            }
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
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, enemyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        
        currColumn = column - 1;
        currRow = row;
        counter = 3;
        if(currColumn < 0){
            currColumn = 0;
            counter--;
        }
        while(currRow >= 0 && currColumn >= 0 && currColumn < 8 && counter > 0){
            if(currRow == row && currColumn == column){
                counter--;
                continue;
            }
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
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, enemyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        
        currColumn = column - 1;
        currRow = row + 1;
        counter = 3;
        if(currColumn < 0){
            currColumn = 0;
            counter--;
        }
        while(currRow >= 0 && currColumn >= 0 && currColumn < 8 && counter > 0){
            if(currRow == row && currColumn == column){
                counter--;
                continue;
            }
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
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, enemyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        
        //check whether we can castle
        //Initially retrieve rooks that haven't been moved
        if(!this.isMoved){
            Rook[] friendlyRooks = new Rook[2];
            int index = 0;
            
            ArrayList<Piece> alivePieces = App.currentPieces;
            for(Piece p: alivePieces){
                if(p.getColor().equals(friendlyColor) && p instanceof Rook){
                    friendlyRooks[index++] = (Rook)p;
                }
            }
            for(int i = 0; i < index; i++){
                Rook curr = friendlyRooks[i];
                if(!curr.getIsMoved()){
                    //check the squares in between
                    int rookColumn = curr.getColumn();
                    int j;
                    int max;
                    if(rookColumn > column){
                        j = column;
                        max = rookColumn;
                    }
                    else{
                        j = rookColumn;
                        max = column;
                    }
                    boolean isCastlingPossible = true;
                    while(j <= max){
                        String currPos = Piece.positions[row][j];
                        if(EmptyPane.isSquareThreatened(currPos, friendlyColor)){
                            isCastlingPossible = false;
                            break; //castling with this square is not possible
                        }
                        j++;
                    }
                    if(isCastlingPossible){
                        String currPos;
                        if(rookColumn - column > 0){
                            //rook is at our right
                            currPos = Piece.positions[row][column + 2];
                        }
                        else{
                            //rook is at our left
                            currPos = Piece.positions[row][column - 2];
                        }
                        moveables.add(currPos); // calculate the position where the king will appear after castling is done
                    }
                }
            }
        }
        return moveables.toArray();
    }

    @Override
    public void take() {
        
    }
}
