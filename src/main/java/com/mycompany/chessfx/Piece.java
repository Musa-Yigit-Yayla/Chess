/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
public class Piece {
    private String color;
    private PiecePane piecePane;
    private double points; //value of the piece
    
    public static final String BLACK_COLOR = "black";
    public static final String WHITE_COLOR = "white";
    
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
}