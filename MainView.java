
package com.mycompany.gameoflifesimulator;

import javafx.geometry.Point2D;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;


/**
 *
 * @author Master
 */
public class MainView extends VBox{
    
    public static final int EDITING = 0;
    public static final int SIMULATING = 1;
    
    private InfoBar infoBar;
    private Canvas canvas;
    
    private Affine affine;
    
    private Simulation simulation;
    private Simulation initialSimulation;
    
    private Simulator simulator;
    
    private int drawMode = Simulation.ALIVE;
    
    private int applicationState = EDITING;
    
    public MainView() {
        
        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleMoved);
        
        this.setOnKeyPressed(this::onKeyPressed);
        
        Toolbar toolbar = new Toolbar(this);
        
        this.infoBar = new InfoBar();
        this.infoBar.setDrawMode(this.drawMode);
        this.infoBar.setCursorPosition(0, 0);
        
        Pane spacer = new Pane();
        spacer.setMinSize(0, 0);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        this.getChildren().addAll(toolbar, this.canvas, spacer, infoBar);
        
        this.affine = new Affine();
        this.affine.appendScale(40, 40);
        
        this.initialSimulation = new Simulation(10, 10);
        this.simulation = Simulation.copy(this.initialSimulation);
    }
    
    
    private void handleMoved(MouseEvent mouseEvent){
        Point2D simCoord = this.getSimulationCoordinates(mouseEvent);
        
        this.infoBar.setCursorPosition((int)simCoord.getX(), (int)simCoord.getY());
    }
    
    
    private void onKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.D){
            this.drawMode = Simulation.ALIVE;
            System.out.println("draw Mode");
        } else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = Simulation.DEAD;
            System.out.println("erase Mode");
        }
    }
    
    
    private void handleDraw(MouseEvent event) {
        
        if(this.applicationState == SIMULATING){
            return;
        }
        
        Point2D simCoord = this.getSimulationCoordinates(event);

        int simX = (int)simCoord.getX();
        int simY = (int)simCoord.getY();

        System.out.println(simX + ", " + simY);
        this.initialSimulation.setState(simX, simY,  drawMode);
        draw();
    }
    
    
    private Point2D getSimulationCoordinates(MouseEvent mouseEvent){
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        
        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            return simCoord;
        } catch (NonInvertibleTransformException ex) {
            throw new RuntimeException("Non invertable transform");
        }
    }

    
    public void draw(){
       GraphicsContext g = this.canvas.getGraphicsContext2D();
       g.setTransform(this.affine);
       
       g.setFill(Color.LIGHTGRAY);
       g.fillRect(0, 0, 400, 400);
       
       if(this.applicationState == EDITING){
           drawSimulation(this.initialSimulation);
       } else {
           drawSimulation(this.simulation);
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
    
    private void drawSimulation(Simulation simulationToDraw){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        for(int x=0; x< simulationToDraw.width; x++){
           for(int y=0; y< simulationToDraw.height; y++){
               if(simulationToDraw.getState(x, y) == Simulation.ALIVE){             
                   g.fillRect(x, y, 1, 1);
               }
           }
       }
    }
    
    public Simulation getSimulation() {
        return this.simulation;
    }

    void setDrawMode(int newDrawMode) {
        this.drawMode = newDrawMode;
        this.infoBar.setDrawMode(newDrawMode);
    }
    
    public void setApplicationState(int applicationState){
        if(applicationState == this.applicationState){
            return;
        }
        if(applicationState == SIMULATING){
            this.simulation = Simulation.copy(this.initialSimulation);
            this.simulator = new Simulator(this, this.simulation);
        }
        
        this.applicationState = applicationState;
        System.out.println("Application state: " + this.applicationState);
    }
    
    public Simulator getSimulator() {
        return this.simulator;
    }
}
