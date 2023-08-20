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
    public King(char color, double value, int row, int column){
        super(color, value, row, column);
    }
    @Override
    public void move(String nextPos) {
       String currPos = super.getPosition();
       super.move(nextPos);
       String newCurrPos = super.getPosition();
       boolean isMoved = (nextPos.equals(newCurrPos) && (!currPos.equals(newCurrPos)));
       if(isMoved){
           //alternate the move turn and create a new Move instance that will be used as the last move
           this.isMoved = isMoved;
           Move move = new Move(this, currPos, newCurrPos);
           
           //check whether we should be moving the correlated rook as well if we have castled our king with it
           int currPosRow = Piece.getRow(currPos);
           int currPosColumn = Piece.getColumn(currPos);
           int newCurrPosRow = Piece.getRow(newCurrPos);
           int newCurrPosColumn = Piece.getColumn(newCurrPos);
           if(currPosRow ==  newCurrPosRow && Math.abs(currPosColumn - newCurrPosColumn) > 1){
               //if this if condition evaluates to true then it implies that we have castled our king
               //Now we have to move our rook properly
               boolean kingMovedLeft = currPosColumn > newCurrPosColumn;
               int initialRookColumn;
               if(kingMovedLeft){
                   initialRookColumn = 0;
                   //move the rook to the right of the king's new position, make sure you retrieve the right rook
                   PiecePane rookPane = (PiecePane)(App.getPieceHolderNode(currPosRow, initialRookColumn));
                   Rook castledRook = (Rook)(rookPane.getPiece());
                   
                   castledRook.setMoved();
               }
           }
       }
    }
    public void castle(){
        
    }
    public boolean isChecked(){
        return true; //ToDo
    }

    @Override
    public Object[] showMoveables() {
        System.out.println("showMoveables of the King has been invoked");
        
        final int row = super.getRow();
        final int column = super.getColumn();
        
        String friendlyColor = super.getColor();
        String enemyColor = super.getEnemyColor();
        
        ArrayList<String> moveables = new ArrayList<>();
        
        //check for surrounding squares
        //start from bottom left corner move with respect to clockwise direction
        int currRow = row - 1;
        int currColumn = column - 1;
        int counter = 3;
        if(currColumn < 0){
            currColumn = 0;
            counter--; // decrement counter by one since we have a square that is out of bounds, (we will check only 2 squares at this row)
        }
        
        System.out.println("Before the first while loop");
        while(currRow >= 0 && currColumn >= 0 && currColumn < 8 && counter > 0){
            System.out.println("currRow: " + currRow + " currColumn: " + currColumn + " counter: " + counter);
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            
            String currPosition = Piece.positions[currRow][currColumn];
            if(currSquare instanceof EmptyPane){
                if(!EmptyPane.isSquareThreatened( currPosition, friendlyColor)){
                    //moveable square
                    moveables.add(currPosition);
                }
            }
            else if(currSquare instanceof PiecePane){//if condition is added for readability
                //check whether currSquare contains an enemy piece and ensure that, that piece is not covered by another enemy piece
                Piece currPiece = ((PiecePane)(currSquare)).getPiece();
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, friendlyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        System.out.println("After the first while loop");
        //start from the upper left and move towards right
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
                currColumn++;
                continue;
            }
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            
            String currPosition = Piece.positions[currRow][currColumn];
            if(currSquare instanceof EmptyPane){
                if(!EmptyPane.isSquareThreatened( currPosition, friendlyColor)){
                    //moveable square
                    moveables.add(currPosition);
                }
            }
            else if(currSquare instanceof PiecePane){// enemy king spotted
                //check whether currSquare contains an enemy piece and ensure that, that piece is not covered by another enemy piece
                Piece currPiece = ((PiecePane)(currSquare)).getPiece();
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, friendlyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        System.out.println("After the second while loop");
        //Traverse the upper row
        currColumn = column - 1;
        currRow = row + 1;
        counter = 3;
        if(currColumn < 0){
            currColumn = 0;
            counter--;
        }
        while(currRow >= 0 && currRow < 8 && currColumn >= 0 && currColumn < 8 && counter > 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            
            String currPosition = Piece.positions[currRow][currColumn];
            if(currSquare instanceof EmptyPane){
                if(!EmptyPane.isSquareThreatened( currPosition, friendlyColor)){
                    //moveable square
                    moveables.add(currPosition);
                }
            }
            else if(currSquare instanceof PiecePane){// enemy king spotted
                //check whether currSquare contains an enemy piece and ensure that, that piece is not covered by another enemy piece
                Piece currPiece = ((PiecePane)(currSquare)).getPiece();
                if(currPiece.getColor().equals(enemyColor) && !EmptyPane.isSquareThreatened(currPosition, friendlyColor)){
                    moveables.add(currPosition);
                }
            }
            currColumn++;
            counter--;
        }
        System.out.println("After the third while loop");
        /*
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
        }*/
        System.out.println("We are about to check whether our king can perform any castling");
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
                    boolean isRookOnLeft;
                    int j;
                    int max;
                    if(rookColumn > column){
                        j = column;
                        max = rookColumn;
                        isRookOnLeft = false;
                    }
                    else{
                        j = rookColumn;
                        max = column;
                        isRookOnLeft = true;
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
    public void take(Piece takerPiece) {
        //empty method stub in King, we cannot take a king
        return;
    }

}
