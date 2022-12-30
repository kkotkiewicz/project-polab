package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Application;

import java.util.ArrayList;

public class World {
    public static void main(String[] args) {

//        IGrassGenerator grassGenerator = new EquatorGrassGenerator(5, 5);
//        AbstractWorldMap map = new Earth(5, 5, 5, true, grassGenerator, 5, 5, 7);
//        MapVisualizer mapVisualizer = new MapVisualizer(map);
//        SimulationEngine engine = new SimulationEngine(map, 1, 5, 7, 10, true);
//        engine.run();
        Application.launch(App.class, args);
    }
}
