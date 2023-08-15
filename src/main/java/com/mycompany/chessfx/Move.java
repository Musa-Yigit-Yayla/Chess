/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 * Class for representing chess moves
 */
public class Move {
    public static final boolean WHITE_TURN = true;
    public static final boolean BLACK_TURN = false;
    
    private static Move lastMove; // last valid move that has been made
    private static boolean  turn = true;// white's turn initially
    
    private Piece piece;
    private String prevPos;
    private String newPos;
    
    //No need to alter the turn data field, that is automatically reversed in here.
    public Move(Piece piece, String prevPos, String newPos){
        this.piece = piece;
        this.prevPos = prevPos;
        this.newPos = newPos;
        
        //alter the static variable turn since a new move has been made
        turn = !turn;
        //set the lastMove to this object
        lastMove = this;
    }
    public static Move getLastMove(){
        return lastMove;
    }
    //1 for white turn, -1 for black turn
    public static boolean getTurn(){
        return turn;
    }
    //Notice we are returning a direct reference to our piece data field
    public Piece getPiece(){
        return this.piece;
    }
    public String getPrevPos(){
        return this.prevPos;
    }
    public String getNewPos(){
        return this.newPos;
    }
}
