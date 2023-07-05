/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Holds individual pieces
public class PiecePane extends StackPane{
    public final double PANE_WIDTH = 100.0;
    public final double PANE_HEIGHT = 100.0;
    
    private Piece piece;
    private Image image;
    private ImageView imgView = new ImageView();
    public PiecePane(Piece piece){
        this.piece = piece;
        this.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
        this.setImage();
        this.getChildren().add(imgView);
    }
    //Sets the image with respect to color and type of the piece
    //Must be invoked when a pawn is about to be promoted, notice that first you must update the pawn's point data field
    public void setImage(){
        double p = this.piece.getPoints();
        if(p == 1){
            //white pawn
        }
    }
    
}

