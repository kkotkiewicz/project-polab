package agh.ics.oop.gui;

import agh.ics.oop.attributes.Genotype;
import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.grassGenerators.EquatorGrassGenerator;
import agh.ics.oop.grassGenerators.IGrassGenerator;
import agh.ics.oop.grassGenerators.ToxicGrassGenerator;
import agh.ics.oop.mapElements.Animal;
import agh.ics.oop.mapElements.IMapElement;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Earth;
import agh.ics.oop.maps.Hell;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

public class SimulationParameters {
    private AbstractWorldMap map;
    private IGrassGenerator grassGenerator;
    private SimulationEngine simulationEngine;
    private int height = 700;
    private Vector2d upperRight;
    private boolean isRunning;
    private VBox vBox = new VBox();
    private VBox animalInformation;
    private VBox simulationInformation;
    private HBox bottomBox;
    private Animal observedAnimal;
    private Circle observedSource;
    private GridPane gridPane = new GridPane();
    private boolean isObserved = false;
    private int radius;
    private boolean isHighlighted = false;


    public SimulationParameters(HashMap<String, String> parameters) {
        int width = Integer.parseInt(parameters.get("width"));
        int height = Integer.parseInt(parameters.get("height"));
        if(parameters.get("grassGenerator").equals("Equator")){
            this.grassGenerator = new EquatorGrassGenerator(width, height);
        }
        else{
            this.grassGenerator = new ToxicGrassGenerator(width, height);
        }
        int plantsAtStart = Integer.parseInt(parameters.get("plantsAtStart"));
        int numOfPlants = Integer.parseInt(parameters.get("numOfPlants"));
        int plantEnergy = Integer.parseInt(parameters.get("plantEnergy"));
        int numOfAnimals = Integer.parseInt(parameters.get("numOfAnimals"));
        int startingEnergy = Integer.parseInt(parameters.get("startingEnergy"));
        int minEnergy = Integer.parseInt(parameters.get("minEnergy"));
        int copulationCost = Integer.parseInt(parameters.get("copulationCost"));
        int genotypeLength = Integer.parseInt(parameters.get("genotypeLength"));
        int minMutation = Integer.parseInt(parameters.get("minMutation"));
        int maxMutation = Integer.parseInt(parameters.get("maxMutation"));
        int moveDelay = Integer.parseInt(parameters.get("moveDelay"));
        boolean mutation = false;
        if(parameters.get("mutation").equals("true")){
            mutation = true;
        }
        boolean isShuffle = false;
        if(parameters.get("isShuffle").equals("true")){
            isShuffle = true;
        }
        if(parameters.get("map").equals("earth")){
            this.map = new Earth(width, height, copulationCost, mutation, grassGenerator, plantEnergy, minEnergy, numOfPlants, plantsAtStart, minMutation, maxMutation);
        }
        else{
            this.map = new Hell(width, height, copulationCost, mutation, grassGenerator, plantEnergy, minEnergy, numOfPlants, plantsAtStart, minMutation, maxMutation);
        }
        this.simulationEngine = new SimulationEngine(this.map, numOfAnimals, startingEnergy, genotypeLength, moveDelay, isShuffle);
        simulationEngine.addObserver(this);
        this.upperRight = this.map.getUpperRight();
        this.radius = ((this.height/(this.upperRight.getY()+1))/3);
        this.bottomBox = new HBox();
        this.animalInformation = new VBox();
        this.animalInformation.setMinWidth(200);
        this.simulationInformation = new VBox();
        this.bottomBox.getChildren().addAll(animalInformation, simulationInformation);
        this.bottomBox.setMinWidth(800);
        this.bottomBox.setSpacing(100);
        this.vBox.setSpacing(50);
    }


    public void refresh() {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            gridPane.getColumnConstraints().clear();
            gridPane.getRowConstraints().clear();
            gridPane.setGridLinesVisible(false);
            if (isHighlighted) {
                createGridWithGenes();
            }
            else {
                createGrid();
            }
            if (isObserved) {
                refreshAnimal();
            }
            refreshSimulationParameters();
        });
    }

    private void clickGrid(MouseEvent event) {
        if (isObserved) {
            double colorValue =  (double) (observedAnimal.getEnergy()) / (double) (this.simulationEngine.getStartingEnergy());
            if (colorValue > 1) {
                this.observedSource.setFill((Color.rgb(255, 0, 0)));
            }
            else {
                this.observedSource.setFill(Color.rgb((int) (255 * colorValue), 0, 0));
            }
            this.observedSource.setRadius(radius);
        }
        Circle source = (Circle) event.getSource();
        this.observedSource = source;
        source.setFill(Color.BLUE);
        source.setRadius(1.5 * radius);
        GridPane p = (GridPane) source.getParent();
        int colIndex = p.getColumnIndex(source) - 1;
        int rowIndex = map.getUpperRight().getY() - p.getRowIndex(source) + 1;
        Vector2d position = new Vector2d(colIndex, rowIndex);
        System.out.println(position.toString());
        if (this.map.isOccupied(position)) {
            if (this.map.objectAt(position).getClass() == Animal.class) {
                this.observedAnimal = (Animal) this.map.objectAt(position);
                this.vBox.getChildren().remove(animalInformation);

                if (!isObserved) {
                    this.isObserved = true;
                }
                refreshAnimal();
            }
        }
    }

    private void refreshAnimal() {
        this.animalInformation.getChildren().clear();
        StringBuilder stringBuilder = new StringBuilder();
        HBox genotypeHBox = new HBox();
        Genotype genotype = this.observedAnimal.getGenotype();
        for (int i = 0; i < genotype.getLength(); i++) {
            if (i == observedAnimal.getGenotypePosition()) {
                stringBuilder.append(genotype.getGene(i));
                Label label = new Label(stringBuilder.toString());
                label.setTextFill(Color.RED);
                genotypeHBox.getChildren().add(label);
            }
            else {
                stringBuilder.append(genotype.getGene(i));
                Label label = new Label(stringBuilder.toString());
                genotypeHBox.getChildren().add(label);
            }

            stringBuilder.delete(0, 1);
        }
        animalInformation.getChildren().add(genotypeHBox);

        stringBuilder.append(observedAnimal.getEnergy());
        Label energy = new Label("Energy: " + stringBuilder);
        animalInformation.getChildren().add(energy);
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(observedAnimal.getGrassCount());
        Label grassCount = new Label("Grass eaten: " + stringBuilder);
        animalInformation.getChildren().add(grassCount);
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(observedAnimal.getChildrenCount());
        Label childrenCount = new Label("Number of children: " + stringBuilder);
        animalInformation.getChildren().add(childrenCount);
        stringBuilder.delete(0, stringBuilder.length());

        if (observedAnimal.getIsAlive()) {
            stringBuilder.append(this.map.getDaysPassed() - observedAnimal.getBirthDate());
        }
        else {
            stringBuilder.append(observedAnimal.getDeathDate() - observedAnimal.getBirthDate());
        }
        Label daysAlive = new Label("Days alive: " + stringBuilder);
        animalInformation.getChildren().add(daysAlive);
        stringBuilder.delete(0, stringBuilder.length());

        if (!observedAnimal.getIsAlive()) {
            stringBuilder.append(observedAnimal.getDeathDate());
            Label deathDate = new Label("Animal died on day: " + stringBuilder);
            animalInformation.getChildren().add(deathDate);
        }
    }
    public void refreshSimulationParameters() {
        this.simulationInformation.getChildren().clear();


        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.simulationEngine.getNumOfAnimals());
        Label numOfAnimals = new Label("Number of animals: " + stringBuilder.toString());
        this.simulationInformation.getChildren().add(numOfAnimals);
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(this.simulationEngine.getNumOfPlants());
        Label numOfPlants = new Label("Number of plants: " + stringBuilder.toString());
        this.simulationInformation.getChildren().add(numOfPlants);
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(this.simulationEngine.getAverageEnergy());
        Label averageEnergy = new Label("Average Energy: " + stringBuilder.toString());
        this.simulationInformation.getChildren().add(averageEnergy);
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(this.simulationEngine.getAverageLifespan());
        Label averageLifespan = new Label("Average lifespan: " + stringBuilder.toString());
        this.simulationInformation.getChildren().add(averageLifespan);
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(this.simulationEngine.getFreeSpace());
        Label freeSpace = new Label("Number of free spaces: " + stringBuilder.toString());
        this.simulationInformation.getChildren().add(freeSpace);
        stringBuilder.delete(0, stringBuilder.length());

        Label mostCommon = new Label("Most common genotype: " + this.simulationEngine.getMostCommonGene().toString());
        this.simulationInformation.getChildren().add(mostCommon);
        stringBuilder.delete(0, stringBuilder.length());


    }

    public void createGridWithGenes() {
        int height = this.height/(this.upperRight.getY()+1);
        int width = this.height/(this.upperRight.getY()+1);

        ArrayList<IMapElement> dominantAnimals = this.simulationEngine.getAnimalsDominant();


        Label firstLabel = new Label("x/y");

        gridPane.getColumnConstraints().add(new ColumnConstraints(width));

        GridPane.setHalignment(firstLabel, HPos.CENTER);

        gridPane.getRowConstraints().add(new RowConstraints(height));

        gridPane.add(firstLabel, 0,0,1,1);


        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = map.getUpperRight();


        for (int i = lowerLeft.getX(); i <= upperRight.getX(); i++) {
            Label label = new Label("" + i);
            gridPane.add(label, 1 + i - lowerLeft.getX(), 0, 1, 1);
            gridPane.getColumnConstraints().add(new ColumnConstraints(width));
            GridPane.setHalignment(label, HPos.CENTER);

        }

        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            Label label = new Label("" + i);
            gridPane.add(label, 0, 1 + upperRight.getY() - i, 1 , 1);
            gridPane.getRowConstraints().add(new RowConstraints(height));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (int i = lowerLeft.getX(); i <= upperRight.getX(); i++) {
            for (int j = upperRight.getY(); j>= lowerLeft.getY(); j--) {
                if (map.isOccupied(new Vector2d(i,j))) {
                    IMapElement object = map.objectAt(new Vector2d(i,j));
                    Circle circle = map.objectAt(new Vector2d(i,j)).toShape(this.simulationEngine.getStartingEnergy(), radius);
                    circle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clickGrid(e));
                    if (map.objectAt(new Vector2d(i, j)).equals(observedAnimal)) {
                        circle.setFill(Color.BLUE);
                        circle.setRadius(1.5 * radius);
                    }
                    if (map.objectAt(new Vector2d(i,j)).getClass() == Animal.class) {
                        if (dominantAnimals.contains(object)) {
                            circle.setFill(Color.PINK);
                            circle.setRadius(1.5 * radius);
                        }
                    }
                    gridPane.add(circle, 1 + i - lowerLeft.getX(), 1 + upperRight.getY() - j,1,1);
                    GridPane.setHalignment(circle, HPos.CENTER);
                }

            }
        }
        gridPane.setGridLinesVisible(true);
    }

    public void createGrid() {
        int height = this.height/(this.upperRight.getY()+1);
        int width = this.height/(this.upperRight.getY()+1);


        Label firstLabel = new Label("x/y");

        gridPane.getColumnConstraints().add(new ColumnConstraints(width));

        GridPane.setHalignment(firstLabel, HPos.CENTER);

        gridPane.getRowConstraints().add(new RowConstraints(height));

        gridPane.add(firstLabel, 0,0,1,1);


        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = map.getUpperRight();


        for (int i = lowerLeft.getX(); i <= upperRight.getX(); i++) {
            Label label = new Label("" + i);
            gridPane.add(label, 1 + i - lowerLeft.getX(), 0, 1, 1);
            gridPane.getColumnConstraints().add(new ColumnConstraints(width));
            GridPane.setHalignment(label, HPos.CENTER);

        }

        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            Label label = new Label("" + i);
            gridPane.add(label, 0, 1 + upperRight.getY() - i, 1 , 1);
            gridPane.getRowConstraints().add(new RowConstraints(height));
            GridPane.setHalignment(label, HPos.CENTER);
        }

        for (int i = lowerLeft.getX(); i <= upperRight.getX(); i++) {
            for (int j = upperRight.getY(); j>= lowerLeft.getY(); j--) {
                if (map.isOccupied(new Vector2d(i,j))) {
                    Circle circle = map.objectAt(new Vector2d(i,j)).toShape(this.simulationEngine.getStartingEnergy(), radius);
                    circle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clickGrid(e));
                    if (map.objectAt(new Vector2d(i, j)).equals(observedAnimal)) {
                        circle.setFill(Color.BLUE);
                        circle.setRadius(1.5 * radius);
                    }
                    gridPane.add(circle, 1 + i - lowerLeft.getX(), 1 + upperRight.getY() - j,1,1);
                    GridPane.setHalignment(circle, HPos.CENTER);
                }

            }
        }
        gridPane.setGridLinesVisible(true);
    }

    public GridPane getGridPane() {
        return this.gridPane;
    }

    public AbstractWorldMap getMap() {
        return map;
    }

    public SimulationEngine getSimulationEngine() {
        return simulationEngine;
    }

    public VBox getvBox() {
        return vBox;
    }

    public VBox getAnimalInformation() {
        return animalInformation;
    }
    public void setRunning(boolean newValue) {
        isRunning = newValue;
    }

    public boolean getIsRunning() {
        return isRunning;
    }
    public HBox getBottomBox() {
        return bottomBox;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }
    public void setHighlighted(boolean flag) {
        isHighlighted = flag;
    }
}
