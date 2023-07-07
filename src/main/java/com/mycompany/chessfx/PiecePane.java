/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chessfx;

/**
 *
 * @author yigit
 */
import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

//Holds individual pieces
public class PiecePane extends StackPane{
    public final double PANE_WIDTH = 80.0;
    public final double PANE_HEIGHT = 80.0;
    
    private Piece piece;
    private Image image;
    private ImageView imgView = new ImageView();
    private GridPane pieceHolder; // retrieve from App.java
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
        String filePath = "";
        if(p == Piece.WHITE_PAWN_POINTS){
            //white pawn
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Pawn-white.png";
        }
        else if(p == Piece.BLACK_PAWN_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Pawn-black.png";
        }
        else if(p == Piece.WHITE_KNIGHT_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-white.png";
        }
        else if(p == Piece.BLACK_KNIGHT_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-black.png";
        }
        else if(p == Piece.WHITE_BISHOP_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-white.png";
        }
        else if(p == Piece.BLACK_BISHOP_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-black.png";
        }
        else if(p == Piece.WHITE_ROOK_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-white.png";
        }
        else if(p == Piece.BLACK_ROOK_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-black.png";
        }
        else if(p == Piece.WHITE_QUEEN_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-white.png";
        }
        else if(p == Piece.BLACK_QUEEN_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-black.png";
        }
        else if(p == Piece.WHITE_KING_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-white.png";
        }
        else if(p == Piece.BLACK_KING_POINTS){
            filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\King-black.png";
        }
        this.image = new Image(new File(filePath).toURI().toString());
        this.imgView.setImage(image);
        
        this.imgView.setFitHeight(this.PANE_HEIGHT);
        this.imgView.setFitHeight(this.PANE_WIDTH);
        this.getChildren().add(this.imgView);
    }
    
    //Invoke for once and only during the creation of the pieces
    public void setContainer(GridPane pieceHolder){
        this.pieceHolder = pieceHolder;
        String pos = this.piece.getPosition();
        int i, j = 0;
        for(i = 0; i < 8; i++){
            for(j = 0; j < 8; j++){
                if(Piece.positions[i][j].equals(pos)){
                    break;
                }
            }
        }
        this.pieceHolder.add(this.imgView, i, j);
    }
}

//This comment is for converting File objects into respective images or media
//Media media = new Media(new File(filePath).toURI().toString());