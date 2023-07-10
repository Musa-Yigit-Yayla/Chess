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
    public static final int WHITE_TURN = 1;
    public static final int BLACK_TURN = -1;
    
    private static Move lastMove; // last valid move that has been made
    private static int turn = WHITE_TURN;// white's turn initially
    
    private Piece piece;
    private String prevPos;
    private String newPos;
    
    public Move(Piece piece, String prevPos, String newPos){
        this.piece = piece;
        this.prevPos = prevPos;
        this.newPos = newPos;
        
        //alter the static variable turn since a new move has been made
        turn *= -1;
        //set the lastMove to this object
        lastMove = this;
    }
    public Move getLastMove(){
        return lastMove;
    }
    //1 for white turn, -1 for black turn
    public int getTurn(){
        return turn;
    }
}
