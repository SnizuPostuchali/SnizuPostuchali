
package com.mycompany.gameoflifesimulator;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

/**
 *
 * @author Master
 */
public class Toolbar extends ToolBar{
    
    private MainView mainView;
    
    public Toolbar(MainView mainView){
        this.mainView = mainView;
        Button draw = new Button("Draw");
        draw.setOnAction(this::handleDraw);
        Button erase = new Button("Erase");
        erase.setOnAction(this::handleErase);
        Button step = new Button("Step");
        step.setOnAction(this::handleStep);
        Button reset = new Button("Reset");
        reset.setOnAction(this::handleReset);
        Button start = new Button("Start");
        start.setOnAction(this::handleStart);
        Button stop = new Button("Stop");
        stop.setOnAction(this::handleStop);
    
        this.getItems().addAll(draw, erase, reset, start, stop, step);  
    }

    private void handleDraw(ActionEvent actionEvent) {
        System.out.println("draw");
        this.mainView.setDrawMode(Simulation.ALIVE);
    }

    private void handleErase(ActionEvent actionEvent) {
        System.out.println("erase");
        this.mainView.setDrawMode(Simulation.DEAD);
    }

    private void handleStep(ActionEvent actionEvent) {
        System.out.println("step");
        this.mainView.setApplicationState(mainView.SIMULATING);
        this.mainView.getSimulation().step();
        this.mainView.draw();
    }

    private void handleReset(ActionEvent actionEvent) {
        this.mainView.setApplicationState(mainView.EDITING);
        this.mainView.draw();
    }

    private void handleStart(ActionEvent actionEvent) {
        this.mainView.setApplicationState(MainView.SIMULATING);
        this.mainView.getSimulator().start();
    }

    private void handleStop(ActionEvent actionEvent) {
        this.mainView.getSimulator().stop();
    }
}
