package agh.ics.oop.gui;

import agh.ics.oop.attributes.Genotype;
import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.grassGenerators.EquatorGrassGenerator;
import agh.ics.oop.grassGenerators.IGrassGenerator;
import agh.ics.oop.mapElements.Animal;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Earth;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


public class App extends Application {
    public GridPane gridPane = new GridPane();
    public Scene scene = new Scene(gridPane, 600, 600);

    private AbstractWorldMap map;
    private IGrassGenerator grassGenerator;
    private Thread thread;
    private SimulationEngine simulationEngine;

    private int height = 600;

    private Vector2d upperRight;

    private boolean isRunning;

    private VBox vBox;

    private VBox animalInformation;

    private Animal observedAnimal;

    private boolean isObserved = false;

    private File config = null;

    private HashMap<String, Object> parameters;

    @Override
    public void init() throws Exception {
        super.init();


        try {
            grassGenerator = new EquatorGrassGenerator(20, 20);
            this.map = new Earth(20, 20, 5, true, grassGenerator, 5, 5, 3);
            this.upperRight = this.map.getUpperRight();
            simulationEngine = new SimulationEngine(map, 20, 100, 7, 10, true);
            simulationEngine.addObserver(this);

            System.out.println(this.map);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void configureFileChooser(FileChooser fileChooser){
        fileChooser.setTitle("Przeglądaj plik");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage optionStage = new Stage();
        optionStage.setTitle("Wybierz plik konfiguracyjny");
        FileChooser fileChooser = new FileChooser();

        VBox controlPanel = new VBox();
        Button explorator = new Button("Wybierz plik");
        Button simulate = new Button("Rozpocznij symulacje");


        App app = this;
        explorator.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        configureFileChooser(fileChooser);
                        app.config = fileChooser.showOpenDialog(null); //tu bedzie primary stage
                    }
                }
        );

        simulate.setOnAction((event) -> {
            try{
                Scanner scanner = new Scanner(this.config);
                while(scanner.hasNext()){
                    String[] parameter = scanner.next().split(":");
                    this.parameters.put(parameter[0], parameter[1]);
                }
            }
            catch(InputMismatchException | FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        });
        

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        this.vBox = new VBox();

        this.thread = new Thread(this.simulationEngine);
        this.thread.start();

        isRunning = true;


        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> {
            if (this.isRunning) {
                stopButton.setText("Start");
                this.thread.suspend();
                this.isRunning = false;
            }
            else {
                stopButton.setText("Stop");
                this.thread.resume();
                this.isRunning = true;
            }
        });

        createGrid();

        vBox.getChildren().add(stopButton);
        vBox.getChildren().add(gridPane);
        scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createGrid() {
        int height = this.height/(this.upperRight.getY()+1);
        int width = this.height/(this.upperRight.getY()+1);

        int radius = ((this.height/(this.upperRight.getY()+1))/5);
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
                if (!map.isOccupied(new Vector2d(i,j))) {
                    continue;
                }
                Circle circle = map.objectAt(new Vector2d(i,j)).toShape(this.simulationEngine.getStartingEnergy(), radius);
                circle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> clickGrid(e));
                gridPane.add(circle, 1 + i - lowerLeft.getX(), 1 + upperRight.getY() - j,1,1);
                GridPane.setHalignment(circle, HPos.CENTER);
            }
        }
        gridPane.setGridLinesVisible(true);
    }


    public void refresh() {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            gridPane.getColumnConstraints().clear();
            gridPane.getRowConstraints().clear();
            gridPane.setGridLinesVisible(false);
            createGrid();
            if (isObserved) {
                refreshAnimal();
            }
        });
    }

    private void clickGrid(MouseEvent event) {
        Node source = (Node) event.getSource();
        GridPane p = (GridPane) source.getParent();
        int colIndex = p.getColumnIndex(source) - 1;
        int rowIndex = map.getUpperRight().getY() - p.getRowIndex(source) + 1;
        Vector2d position = new Vector2d(colIndex, rowIndex);
        System.out.println(position.toString());
        if (this.map.isOccupied(position)) {
            if (this.map.objectAt(position).getClass() == Animal.class) {
                this.observedAnimal = (Animal) this.map.objectAt(position);
                this.vBox.getChildren().remove(animalInformation);
                this.animalInformation = new VBox();
                if (!isObserved) {
                    this.isObserved = true;
                }
                vBox.getChildren().add(animalInformation);
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


}