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
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class TakenPiecePane extends ScrollPane {
    public static final double TAKEN_PIECE_BOX_SPACING = 10.0;
    public static final double TAKEN_PIECE_IMAGE_LENGTH = 40.0;
    public static final double TAKEN_PIECE_PANE_WIDTH = 300.0;
    public static final double TAKEN_PIECE_PANE_HEIGHT = 50.0;
    
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<Double> imageValues = new ArrayList<>();
    private HBox pieceBox = new HBox();
    private boolean isWhite;
    
    public TakenPiecePane(boolean isWhite){
        //set the properties of the scroll pane
        this.pieceBox.setAlignment(Pos.CENTER_LEFT);
        this.pieceBox.setSpacing(TAKEN_PIECE_BOX_SPACING);
        
        this.setPrefWidth(TAKEN_PIECE_PANE_WIDTH);
        this.setPrefHeight(TAKEN_PIECE_PANE_HEIGHT);
        this.isWhite = isWhite;
        this.getChildren().add(this.pieceBox);
    }
    //This methods anticipates user to not to pass a King instance as a takenPiece parameter
    public void addPiece(Piece takenPiece){
        //add a newly taken piece if its color matches this object's isWhite property
        String takenPieceColor = takenPiece.getColor();
        if((takenPieceColor.equals(Piece.WHITE_COLOR) && this.isWhite) || (takenPieceColor.equals(Piece.BLACK_COLOR) && !this.isWhite)){
            //We will manually create a new image based on the given piece so as to avoid any prospective errors
            PiecePane takenPiecePane = takenPiece.getPiecePane();
            double pieceValue = takenPiece.getPoints();
            ImageView imgView;
            Image img;
            String filePath = "";
            switch((int)(pieceValue)){
                case 1: filePath = filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Pawn-white.png"; break;
                case -1: filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Pawn-black.png"; break;
                case 3: 
                    if(pieceValue == Piece.WHITE_BISHOP_POINTS){
                        filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-white.png";
                    }
                    else{
                        filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-white.png";
                    }break;
                case -3: 
                    if(pieceValue == Piece.BLACK_BISHOP_POINTS){
                        filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Bishop-black.png";
                    }
                    else{
                        filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Knight-black.png";
                    }break;
                case 5: filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-white.png"; break;
                case -5: filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Rook-black.png"; break;
                case 9: filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-white.png"; break;
                case -9: filePath = "C:\\Users\\yigit\\Documents\\NetBeansProjects\\ChessFX\\src\\main\\java\\Chess Piece Images\\Queen-black.png"; break;
            }
            img = new Image(new File(filePath).toURI().toString());
            imgView = new ImageView(img);
            //pieceBox.getChildren().add(imgView);
            this.insertImg(imgView, pieceValue);
            
        }
    }
    //Insert a recently added image so that we can preserve the property that our hbox dispalys the images in a descending manner
    //We will insert the given imgView into the arraylist with respect to where it should be in terms of descending sorted order
    //points resembles the point of the taken piece
    private void insertImg(ImageView imgView, double points){
        //since we have our arraylists in a descending sorted order, we can insert at the proper position
        //do not forget to add the absolute value of the points to the list
        double absPoints = Math.abs(points);
        int insertionIndex = -1;
        for(int i = 0; i < this.imageValues.size(); i++){
            double currValue = this.imageValues.get(i);
            if(absPoints >= currValue){
                insertionIndex = i;
                break;
            }
        }
        if(insertionIndex == -1){
            //add the given values to the very end of the corresponding arraylists
            this.imageViews.add(imgView);
            this.imageValues.add(absPoints);
        }
        else{
            this.imageViews.add(insertionIndex, imgView);
            this.imageValues.add(insertionIndex, absPoints);
        }
        this.pieceBox.getChildren().clear();
        //redraw the pieceBox by adding the nodes in the required order
        for(ImageView img: this.imageViews){
            this.pieceBox.getChildren().add(img);
        }
    }
}
