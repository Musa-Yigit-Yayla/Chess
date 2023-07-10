/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

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
    public abstract void take(); // will be used when an enemy piece takes a piece instance. We will have empty method stub in King subclass
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
}