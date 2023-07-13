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
public class Bishop extends Piece{

    public Bishop(char color) {
        super(color, 3.15);
        if(this.getColor().equals(Piece.WHITE_COLOR)){
            this.setPoints(WHITE_BISHOP_POINTS);
        }
        else{
            this.setPoints(BLACK_BISHOP_POINTS);
        }
    }
    public Bishop(char color, double value, int row, int column){
        super(color, value, row, column);
    }
    @Override
    public void move() {
        
    }

    //Logic is to traverse squares that we can go diagonally, and we will finish that diagonal direction as soon as we encounter
    //a friendly piece (we exclude that), or an enemy piece(we incorporate that)
    //also bounds checking is done
    //We don't check whether after this move we have a check or not, (whether if the move is invalid due to exposing friendly king)
    //@return Object[] holding individual String objects
    @Override
    public Object[] showMoveables() {
        final int row = this.getRow();
        final int column = this.getColumn();
        
        String friendlyColor = super.getColor();
        String enemyColor = super.getEnemyColor();
        
        ArrayList<String> moveables = new ArrayList<>();
        
        //start from upper left move with clockwise direction
        //going towards upper left
        int currRow = row - 1;
        int currColumn = column - 1;
        
        GridPane pieceHolder = App.getPieceHolder();
        
        while(currRow >= 0 && currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
            currRow--;
            currColumn++;
        }
        //continue with towards upper right
        currRow = row - 1;
        currColumn = column + 1;
        
        while(currRow >= 0 && currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
            
            currRow--;
            currColumn++;
        }
        
        //continue towards lower right
        currRow = row + 1;
        currColumn = column + 1;
        
        while(currRow < 8 && currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
        }
        //lower left
        currRow = row + 1;
        currColumn = column - 1;
        
        while(currRow < 8 && currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
        }
        
        return moveables.toArray();
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
        
        //start from upper left move with clockwise direction
        //going towards upper left
        int currRow = row - 1;
        int currColumn = column - 1;
        
        GridPane pieceHolder = App.getPieceHolder();
        
        while(currRow >= 0 && currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
            currRow--;
            currColumn++;
        }
        //continue with towards upper right
        currRow = row - 1;
        currColumn = column + 1;
        
        while(currRow >= 0 && currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
            
            currRow--;
            currColumn++;
        }
        
        //continue towards lower right
        currRow = row + 1;
        currColumn = column + 1;
        
        while(currRow < 8 && currColumn < 8){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
        }
        //lower left
        currRow = row + 1;
        currColumn = column - 1;
        
        while(currRow < 8 && currColumn >= 0){
            StackPane currSquare = App.getPieceHolderNode(currRow, currColumn);
            if(currSquare instanceof EmptyPane){
                moveables.add(Piece.positions[currRow][currColumn]); // since the curr pos is empty pane add the position string correspondance
            }
            //There is no other possibility, a square can only be PiecePane or EmptyPane instance, however this is to underline
            //the type of the currSquare instance
            else if(currSquare instanceof PiecePane){
                PiecePane currPiecePane = (PiecePane)(currSquare);
                Piece currPiece = currPiecePane.getPiece();
                
                //We add the square with enemy check aswell if possible
                if(currPiece.getColor().equals(enemyColor)){
                    moveables.add(Piece.positions[currRow][currColumn]);
                }
                //If we have a friendly piece, we don't add it
                break;
            }
        }
        
        return (String[])moveables.toArray();
    }
    @Override
    public void take(Piece takerPiece) {
        
    }
    
}
