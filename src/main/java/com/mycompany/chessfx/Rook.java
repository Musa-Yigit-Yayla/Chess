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
public class Rook extends Piece{
    private boolean isMoved = false;
    
    public Rook(char color){
        super(color, 5);
        if(this.getColor().equals(WHITE_COLOR)){
            this.setPoints(WHITE_ROOK_POINTS);
        }
        else{
            this.setPoints(BLACK_ROOK_POINTS);
        }
    }
    public Rook(char color, double value, int row, int column){
        super(color, value, row, column);
    }
    public boolean getIsMoved(){
        return this.isMoved;
    }
    @Override
    public void move(String nextPos){
       String currPos = super.getPosition();
       super.move(nextPos);
       String newCurrPos = super.getPosition();
       boolean isMoved = (nextPos.equals(newCurrPos) && (!currPos.equals(newCurrPos)));
       if(isMoved){
           //alternate the move turn and create a new Move instance that will be used as the last move
           this.isMoved = isMoved;
           Move move = new Move(this, currPos, newCurrPos);
       }
    }

    //We will traverse vertically and horizontally
    //Similar to Bishop, if our path is obstructed by an enemy piece, we will include that pos, if obstructed by friendly we will exclude it
    //We won't check whether the move's validity regarding exposing the friendly king, that will be chechked with a method that is to be invoked
    //from overriden move functions
    @Override
    public Object[] showMoveables() {
        final int row = super.getRow();
        final int column = super.getColumn();
        
        String friendlyColor = super.getColor();
        String enemyColor = super.getEnemyColor();
        
        ArrayList<String> moveables = new ArrayList<>();
        
        int currRow;
        int currColumn;
        //first calculate horizontal squares if any
        //move towards left
        currColumn = column - 1;
        while(currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(row, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[row][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[row][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currColumn--;
        }
        
        //move towards right
        currColumn = column + 1;
        while(currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(row, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[row][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[row][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currColumn++;
        }
        
        currColumn = column; // set the currColumn back to original position
        currRow = row - 1; // first go up
        while(currRow >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, column);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][column]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][column]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currRow--;
        }
        currRow = row + 1;
        while(currRow < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, column);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][column]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][column]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currRow++;
        }
        return moveables.toArray();
    }
    //Invoke after we have castled our king with this rook
    //Sets the isMoved datafield true once and for all
    public void setMoved(){
        this.isMoved = true;
    }
    public static String[] showMoveables(double[][] state, int row, int column){
        
        String friendlyColor = Piece.WHITE_COLOR;
        String enemyColor = Piece.BLACK_COLOR;
        
        if(state[row][column] < 0){
            String temp = friendlyColor;
            friendlyColor = enemyColor;
            enemyColor = temp;
        }
        
        ArrayList<String> moveables = new ArrayList<>();
        
        int currRow;
        int currColumn;
        //first calculate horizontal squares if any
        //move towards left
        currColumn = column - 1;
        while(currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(row, currColumn);
            if(currSquare instanceof EmptyPane || ((state[row][currColumn]) == 0)){
                moveables.add(Piece.positions[row][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[row][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currColumn--;
        }
        
        //move towards right
        currColumn = column + 1;
        while(currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(row, currColumn);
            if(currSquare instanceof EmptyPane || ((state[row][currColumn]) == 0)){
                moveables.add(Piece.positions[row][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[row][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currColumn++;
        }
        
        currColumn = column; // set the currColumn back to original position
        currRow = row - 1; // first go up
        while(currRow >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, column);
            if(currSquare instanceof EmptyPane || ((state[currRow][column]) == 0)){
                moveables.add(Piece.positions[currRow][column]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][column]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currRow--;
        }
        currRow = row + 1;
        while(currRow < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, column);
            if(currSquare instanceof EmptyPane || ((state[currRow][column]) == 0)){
                moveables.add(Piece.positions[currRow][column]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][column]);
                }
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currRow++;
        }
        String[] returnArr = new String[moveables.size()];
        for(int i = 0; i < returnArr.length; i++){
            returnArr[i] = moveables.get(i);
        }
        return returnArr;
    }
    //The only difference with the original showMoveables is that this version does not incorporate the enemy piece once it has encountered it
    public static String[] showMoveables2(double[][] state, int row, int column){
        
        String friendlyColor = Piece.WHITE_COLOR;
        String enemyColor = Piece.BLACK_COLOR;
        
        if(state[row][column] < 0){
            String temp = friendlyColor;
            friendlyColor = enemyColor;
            enemyColor = temp;
        }
        
        ArrayList<String> moveables = new ArrayList<>();
        
        int currRow;
        int currColumn;
        //first calculate horizontal squares if any
        //move towards left
        currColumn = column - 1;
        while(currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(row, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[row][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                /*PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[row][currColumn]);
                }*/
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currColumn--;
        }
        
        //move towards right
        currColumn = column + 1;
        while(currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(row, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[row][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                /*PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[row][currColumn]);
                }*/
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currColumn++;
        }
        
        currColumn = column; // set the currColumn back to original position
        currRow = row - 1; // first go up
        while(currRow >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, column);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][column]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                /*
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][column]);
                }*/
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currRow--;
        }
        currRow = row + 1;
        while(currRow < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, column);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][column]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                /*
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][column]);
                }*/
                //If we have a friendly piece, we don't add it
                break;
            
            }
            currRow++;
        }
        String[] returnArr = new String[moveables.size()];
        for(int i = 0; i < returnArr.length; i++){
            returnArr[i] = moveables.get(i);
        }
        return returnArr;
    }
    @Override
    public void take(Piece taker) {
        super.take(taker);
    }
}
