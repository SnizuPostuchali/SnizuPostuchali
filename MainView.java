/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gameoflifesimulator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 *
 * @author Master
 */
public class MainView extends VBox{
    
    private Button stepButton;
    private Canvas canvas;
    
    private Affine affine;
    
    private Simulation simulation;
    
    public MainView() {
        this.stepButton = new Button("step");
        this.stepButton.setOnAction(actionEvent -> {
            simulation.step();
            draw();
        });
        
        this.canvas = new Canvas(400, 400);
        
        this.getChildren().addAll(this.stepButton, this.canvas);
        
        this.affine = new Affine();
        this.affine.appendScale(40, 40);
        
        this.simulation = new Simulation(10, 10);
        
        simulation.setAlive(3, 0);
        simulation.setAlive(3, 1);
        simulation.setAlive(3, 4);
        
        simulation.setAlive(3, 5);
        simulation.setAlive(5, 6);
        simulation.setAlive(6, 5);
        simulation.setAlive(6, 6);
    }
    
    public void draw(){
       GraphicsContext g = this.canvas.getGraphicsContext2D();
       g.setTransform(this.affine);
       
       g.setFill(Color.LIGHTGRAY);
       g.fillRect(0, 0, 400, 400);
       
       g.setFill(Color.BLACK);
       for(int x=0; x< this.simulation.width; x++){
           for(int y=0; y< this.simulation.height; y++){
               if(this.simulation.board [x][y] == 1){             //лучше использовать board [x][y] вместо getState(x, y)
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
