
package com.mycompany.gameoflifesimulator;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.util.Duration;



public class Simulator {
    private Timeline timeline;
    private MainView mainView;
    private Simulation simulation;

    public Simulator(MainView mainView, Simulation simulation) {
        this.mainView = mainView;
        this.simulation = simulation;
        this.timeline = new Timeline(new KeyFrame(Duration.millis(500), this::doStep));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }
    
    private void doStep(ActionEvent actionEvent){
        this.simulation.step();
        this.mainView.draw();
    }
    
    public void start(){
        this.timeline.play();
    }
    
    public void stop(){
        this.timeline.stop();
    }
}