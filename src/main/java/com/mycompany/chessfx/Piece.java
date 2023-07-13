/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

import javafx.scene.layout.GridPane;

/**
 *
 * @author yigit
 */
public abstract class Piece {
    private String color;
    private PiecePane piecePane;
    private double points; //value of the piece
    private int row;
    private int column;
    private String currPosition;
    
    public static final String BLACK_COLOR = "black";
    public static final String WHITE_COLOR = "white";
    public static final double WHITE_PAWN_POINTS = 1.0;
    public static final double WHITE_ROOK_POINTS = 5.0;
    public static final double WHITE_KNIGHT_POINTS = 3.0;
    public static final double WHITE_BISHOP_POINTS = 3.15;
    public static final double WHITE_QUEEN_POINTS = 9.0;
    public static final double WHITE_KING_POINTS = 90.0;
    public static final double BLACK_PAWN_POINTS = -1.0;
    public static final double BLACK_ROOK_POINTS = -5.0;
    public static final double BLACK_KNIGHT_POINTS = -3.0;
    public static final double BLACK_BISHOP_POINTS = -3.15;
    public static final double BLACK_QUEEN_POINTS = -9.0;
    public static final double BLACK_KING_POINTS = -90.0;
    
    public static final String[][] positions = {{"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
                                                {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
                                                {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
                                                {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
                                                {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
                                                {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
                                                {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
                                                {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"},};
    
    //W or B, or w or b for color, respective points for pieces, bishop is 3.15
    public Piece(char color, double points){
        color = Character.toUpperCase(color);
        if(color == 'W'){
            this.color = WHITE_COLOR;
            points *= -1; //passed points is assumed to be positive
        }
        else{
            this.color = BLACK_COLOR;
        }
        this.points = points;
        this.piecePane = new PiecePane(this);
    }
    //When this constructor is invoked we don't instantiate peicePane, it is for instantiating temporary representations of an existent piece 
    public Piece(char color, double points, int row, int column){
        color = Character.toUpperCase(color);
        if(color == 'W'){
            this.color = WHITE_COLOR;
            points *= -1; //passed points is assumed to be positive
        }
        else{
            this.color = BLACK_COLOR;
        }
        this.points = points;
        this.setPosition(row, column);
    }
    public String getColor(){
        return this.color;
    }
    public PiecePane getPiecePane(){
        return this.piecePane;
    }
    public double getPoints(){
        return this.points;
    }
    //Set the value of piece, careful for color representation -, + signs
    public void setPoints(double points){
        this.points = points;
    }
    public abstract void move();
    public abstract Object[] showMoveables(); // return all moveable squares, as a string array once a piece object is clicked
    // will be used when an enemy piece takes a piece instance. We will have empty method stub in King subclass
    public void take(Piece takerPiece){
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        String currPosition = this.getPosition();
        if(takerPiece instanceof King){
            //check whether the enemy king can take this piece (Check whether this piece is guarded by any friendly piece)
            if(!EmptyPane.isSquareThreatened(currPosition, this.getColor())){
                //take current piece
                this.takeHelper(takerPiece);
            }
        }
        //calculate moveables of takerPiece and see whether enemy king is checked after having taken this piece
        else{
            String[] takerMoveables = (String[])(takerPiece.showMoveables());
            for(int i = 0; i < takerMoveables.length; i++){
                String currMoveable = takerMoveables[i];
                if(currPosition.equals(currMoveable)){
                    //check whether an takerPiece's friendly king is exposed after having taken this piece
                    double[][] nextState = App.retrieveGameState(takerPiece.getPosition(), currPosition);
                    //Retrieve taker's king's position
                    String enemyKingPosition = App.getKingPosition(nextState, takerPiece.getColor());
                    if(!App.isChecked(nextState, takerPiece.getColor(), enemyKingPosition)){
                        this.takeHelper(takerPiece);
                    }
                    break;
                }
            }
        }
    }
    
    private void takeHelper(Piece taker){
        String friendlyColor = this.getColor();
        String enemyColor = this.getEnemyColor();
        
        if(taker.getColor().equals(enemyColor)){
            String takerPosition = taker.currPosition;
            String takenPosition = this.currPosition;
            
            int takenRow = this.row;
            int takenColumn = this.column;
            int[] takerPos = Piece.getNumericPosition(takerPosition);
            int takerRow = takerPos[0];
            int takerColumn = takerPos[1];
            GridPane pieceHolder = App.getPieceHolder();
            taker.setPosition(takenRow, takenColumn);
            PiecePane takenPane = (PiecePane)(App.getGridNode(pieceHolder, takenRow, takenColumn));
            PiecePane takerPane = (PiecePane)(App.getGridNode(pieceHolder, taker.row, taker.column));
            
            Piece takenPiece = takenPane.getPiece();
            double takenPoints = takenPiece.getPoints();
            
            App.updateGamePoints(takenPoints);
            App.currentPieces.remove(takenPiece);
            App.takenPieces.add(takenPiece); // add the piece that has been killed
            
            //Replace takenPane with takerPane and place an empty pane instance to taker pane position
            pieceHolder.getChildren().remove(takerPane);
            pieceHolder.add(new EmptyPane(), taker.row, taker.column);
            pieceHolder.getChildren().remove(takenPane);
            pieceHolder.add(takerPane, takenRow, takenColumn);
            
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
        }
    }
    //Call this after the Piece instance is either successfuly has been moved or during initialization process
    //No bounds checking performed
    public void setPosition(int row, int column){
        this.row = row;
        this.column = column;
        this.currPosition = Piece.positions[row][column];
    }
    /*
    *@return the position as a whole string, specified in the static 2d matrix format of positions
    */
    public String getPosition(){
        return this.currPosition;
    }
    //Returns the column based on the position of the current piece instance
    //0 based indexing
    public int getColumn(){
        int result = -1;
        if(this.currPosition != null){
            char ch = this.currPosition.charAt(0);
            switch(ch){
                case 'a': result = 0; break;
                case 'b': result = 1; break;
                case 'c': result = 2; break;
                case 'd': result = 3; break;
                case 'e': result = 4; break;
                case 'f': result = 5; break;
                case 'g': result = 6; break;
                case 'h': result = 7; break;
            }
        }
        return result;
    }
    //Returns the row based on the position of the current piece instance
    //0 based indexing
    public int getRow(){
        int result = -1;
        if(this.currPosition != null){
            char ch = this.currPosition.charAt(1);
            switch(ch){
                case '8': result = 0; break;
                case '7': result = 1; break;
                case '6': result = 2; break;
                case '5': result = 3; break;
                case '4': result = 4; break;
                case '3': result = 5; break;
                case '2': result = 6; break;
                case '1': result = 7; break;
            }
        }
        return result;
    }
    public String getEnemyColor(){
        if(this.color.equals(WHITE_COLOR)){
            return BLACK_COLOR;
        }
        else{
            return WHITE_COLOR;
        }
    }
    //same implementations with getRow and getColumn combined in a static method
    //returns an array of length 2, first element is row second is column
    public static int[] getNumericPosition(String pos){
        int[] resultArr = new int[2];
        int result = -1;
        //Row
            char ch = pos.charAt(1);
            switch(ch){
                case '8': result = 0; break;
                case '7': result = 1; break;
                case '6': result = 2; break;
                case '5': result = 3; break;
                case '4': result = 4; break;
                case '3': result = 5; break;
                case '2': result = 6; break;
                case '1': result = 7; break;
            }
            resultArr[0] = result;
        result = -1;
        //Column
        ch = pos.charAt(0);
        switch(ch){
               case 'a': result = 0; break;
               case 'b': result = 1; break;
               case 'c': result = 2; break;
               case 'd': result = 3; break;
               case 'e': result = 4; break;
               case 'f': result = 5; break;
               case 'g': result = 6; break;
               case 'h': result = 7; break;
        }
        resultArr[1] = result; 
         
        return resultArr;
    }
}