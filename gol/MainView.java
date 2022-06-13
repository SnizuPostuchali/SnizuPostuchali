
package com.mycompany.gameoflifesimulator.gol;

import com.mycompany.gameoflifesimulator.gol.model.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;


public class MainView extends VBox{
    
    public static final int EDITING = 0;
    public static final int SIMULATING = 1;
    
    private InfoBar infoBar;
    private Canvas canvas;
    
    private Affine affine;
    
    private Simulation simulation;
    private Board initialBoard;
    
    private CellState drawMode = CellState.ALIVE;
    
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
        
        this.initialBoard = new BoundedBoard(10, 10);
    }
    
    
    private void handleMoved(MouseEvent mouseEvent){
        Point2D simCoord = this.getSimulationCoordinates(mouseEvent);
        
        this.infoBar.setCursorPosition((int)simCoord.getX(), (int)simCoord.getY());
    }
    
    
    private void onKeyPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.D){
            this.drawMode = CellState.ALIVE;
            System.out.println("draw Mode");
        } else if(keyEvent.getCode() == KeyCode.E){
            this.drawMode = CellState.DEAD;
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
        this.initialBoard.setState(simX, simY,  drawMode);
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
           drawSimulation(this.initialBoard);
       } else {
           drawSimulation(this.simulation.getBoard());
       }
       
       g.setStroke(Color.BLUE);
       g.setLineWidth(0.05f);
       for(int x=0; x<= this.initialBoard.getWidth(); x++){
           g.strokeLine(x, 0, x, 10);
       }
       
       for(int y=0; y<= this.initialBoard.getHeight(); y++){
           g.strokeLine(0, y, 10, y);
       }
    }
    
    private void drawSimulation(Board simulationToDraw){
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        for(int x=0; x< simulationToDraw.getWidth(); x++){
           for(int y=0; y< simulationToDraw.getHeight(); y++){
               if(simulationToDraw.getState(x, y) == CellState.ALIVE){             
                   g.fillRect(x, y, 1, 1);
               }
           }
       }
    }
    
    public Simulation getSimulation() {
        return this.simulation;
    }

    void setDrawMode(CellState newDrawMode) {
        this.drawMode = newDrawMode;
        this.infoBar.setDrawMode(newDrawMode);
    }
    
    public void setApplicationState(int applicationState){
        if(applicationState == this.applicationState){
            return;
        }
        if(applicationState == SIMULATING){
            this.simulation = new Simulation(this.initialBoard, new StandartRule());
        }
        
        this.applicationState = applicationState;
        System.out.println("Application state: " + this.applicationState);
    }

    public int getApplicationState() {
        return applicationState;
    }
}
