/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

import java.util.ArrayList;
import javafx.scene.layout.GridPane;
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
   public Pawn(char color, double value, int row, int column){
        super(color, value, row, column);
    }
   public void promote(){
           
   }
   @Override
   public void move(String nextPos){
       String currPos = super.getPosition();
       StackPane destinationPane = App.getPieceHolderNode(Piece.getRow(nextPos), Piece.getColumn(nextPos));
       super.move(nextPos);
       String newCurrPos = super.getPosition();
       if((destinationPane instanceof EmptyPane)){
           System.out.println("Destination pane is an EmptyPane instance");
       }
       if(Piece.getColumn(currPos) != Piece.getColumn(newCurrPos)){
           System.out.println("Previous position and the newly moved position differ by their columns");
       }
       boolean isEnPassanted = (destinationPane instanceof EmptyPane) && (Piece.getColumn(currPos) != Piece.getColumn(newCurrPos));
       boolean isMoved = (nextPos.equals(newCurrPos) && (!currPos.equals(newCurrPos)));
       
       System.out.println("isEnPassanted is: " + isEnPassanted);
       if(isMoved){
           //alternate the move turn and create a new Move instance that will be used as the last move
           if(isEnPassanted){
               //this is an en-passant move, we must remove the enemy pawn from the pieceHolder
               GridPane pieceHolder = App.getPieceHolder();
               //int dx = Piece.getColumn(newCurrPos) - Piece.getColumn(currPos);
               String enemyPawnPos = Piece.positions[Piece.getRow(currPos)][Piece.getColumn(newCurrPos)];
               int enemyPawnRow = Piece.getRow(enemyPawnPos);
               int enemyPawnColumn = Piece.getColumn(enemyPawnPos);
               pieceHolder.getChildren().remove(App.getPieceHolderNode(enemyPawnRow, enemyPawnColumn));
               EmptyPane emptyPane = new EmptyPane(enemyPawnPos);
               pieceHolder.add(emptyPane, enemyPawnRow, enemyPawnColumn);
           }
           this.isMoved = isMoved;
           Move move = new Move(this, currPos, newCurrPos);
       }
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
            dy = -1; //!!! PAY ATTENTION HERE
        }
        else{
            dy = 1;
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
                StackPane nextCurrSquare = App.getPieceHolderNode(row + 2 * dy, column);
                if(nextCurrSquare instanceof EmptyPane && isFirstAdded){
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
                    //new pos represents the lately moved enemy pawn's current position
                    int[] newPosValues = Piece.getNumericPosition(newPos);
                    int newPosRow = newPosValues[0];
                    int newPosColumn = newPosValues[1];
                    String enPassantPos = Piece.positions[newPosRow + dy][newPosColumn];
                    moveables.add(enPassantPos);
                }
            }
        }
        return moveables.toArray();
    }
    
    @Override
    public void take(Piece taker) {
        super.take(taker);
    }
    public static String[] showMoveables(double[][] state, int row, int column){
        String friendlyColor = Piece.WHITE_COLOR;
        String enemyColor = Piece.BLACK_COLOR;
        
        if(state[row][column] < 0){
            String temp = friendlyColor;
            friendlyColor = enemyColor;
            enemyColor = temp;
        }
        
        int dy;
        if(friendlyColor.equals(WHITE_COLOR)){
            dy = 1;
        }
        else{
            dy = -1;
        }
        
        boolean isMoved = true;
        if(friendlyColor.equals(Piece.WHITE_COLOR) && row == 6){
            isMoved = false;
        }
        else if(friendlyColor.equals(Piece.BLACK_COLOR) && row == 1){
            isMoved = false;
        }
        
        ArrayList<String> moveables = new ArrayList<>();
        boolean isFirstAdded = false;
        //check whether we can kickstart by moving two squares
        if(!isMoved){
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
                boolean isEnPassantable = row == possibleEnemy.getRow() && Math.abs(Integer.parseInt(prevPos.substring(1)) - Integer.parseInt(newPos.substring(1))) == 2;
                if(isEnPassantable){
                    //new pos represents the lately moved enemy pawn's current position
                    int[] newPosValues = Piece.getNumericPosition(newPos);
                    int newPosRow = newPosValues[0];
                    int newPosColumn = newPosValues[1];
                    String enPassantPos = Piece.positions[newPosRow + dy][newPosColumn];
                    moveables.add(enPassantPos);
                }
            }
        }
        String[] returnArr = new String[moveables.size()];
        for(int i = 0; i < returnArr.length; i++){
            returnArr[i] = moveables.get(i);
        }
        return returnArr;
    }
}
