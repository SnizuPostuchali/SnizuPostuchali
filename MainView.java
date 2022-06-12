/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameoflifesimulator;

import javafx.geometry.Point2D;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;


/**
 *
 * @author Master
 */
public class MainView extends VBox{
    
    private Button stepButton;
    private Canvas canvas;
    
    private Affine affine;
    
    private Simulation simulation;
    
    private int drawMode = 1;
    
    public MainView() {
        this.stepButton = new Button("step");
        this.stepButton.setOnAction(actionEvent -> {
            simulation.step();
            draw();
        });
        
        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        
        this.setOnKeyPressed(this::onKeyPressed);
        
        this.getChildren().addAll(this.stepButton, this.canvas);
        
        this.affine = new Affine();
        this.affine.appendScale(40, 40);
        
        this.simulation = new Simulation(10, 10);
    }
    
    
    private void handleDraw(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        
        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            int simX = (int)simCoord.getX();
            int simY = (int)simCoord.getY();
            
            System.out.println(simX + ", " + simY);
            this.simulation.setState(simX, simY, drawMode);
            draw();
            
        } catch (NonInvertibleTransformException ex) {
            System.out.println("Could not invert transform");
        }
    }
    
    
    private void onKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.D){
            this.drawMode = 1;
            System.out.println("draw Mode");
        } else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = 0;
            System.out.println("erase Mode");
        }
    }
    
    
    public void draw(){
       GraphicsContext g = this.canvas.getGraphicsContext2D();
       g.setTransform(this.affine);
       
       g.setFill(Color.LIGHTGRAY);
       g.fillRect(0, 0, 400, 400);
       
       g.setFill(Color.BLACK);
       for(int x=0; x< this.simulation.width; x++){
           for(int y=0; y< this.simulation.height; y++){
               if(this.simulation.getState(x, y) == 1){             
                   g.fillRect(x, y, 1, 1);
               }
           }
       }
       
       g.setStroke(Color.BLUE);
       g.setLineWidth(0.05f);
       for(int x=0; x<= this.simulation.width; x++){
           g.strokeLine(x, 0, x, 10);
       }
       
       for(int y=0; y<= this.simulation.height; y++){
           g.strokeLine(0, y, 10, y);
       }
    }

    
}
