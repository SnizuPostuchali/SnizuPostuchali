
package com.mycompany.gameoflifesimulator.gol.model;


public interface SimulationRule {
    CellState getNextState(int x, int y, Board board);
}
