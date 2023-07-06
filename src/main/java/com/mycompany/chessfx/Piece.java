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
    public static final double BLACKKING_POINTS = -90.0;
    
    //W or B, or w or b for color, respective points for pieces, bishop is 3.15
    public Piece(char color, double points){
        color = Character.toUpperCase(color);
        if(color == 'W'){
            this.color = WHITE_COLOR;
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
    
}