package agh.ics.oop.gui;

import agh.ics.oop.attributes.Genotype;
import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.grassGenerators.EquatorGrassGenerator;
import agh.ics.oop.grassGenerators.IGrassGenerator;
import agh.ics.oop.grassGenerators.ToxicGrassGenerator;
import agh.ics.oop.mapElements.Animal;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Earth;
import agh.ics.oop.maps.Hell;
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


    private boolean isObserved = false;

    private File config = null;

    private HashMap<String, String> parameters;

    @Override
    public void init() throws Exception {
        super.init();

        this.parameters = new HashMap<>();

    }



    private void configureFileChooser(FileChooser fileChooser){
        fileChooser.setTitle("Przeglądaj plik");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Wybierz plik konfiguracyjny");
        FileChooser fileChooser = new FileChooser();

        Stage stage = new Stage();

        VBox controlPanel = new VBox();
        Button explorator = new Button("Wybierz plik");
        Button simulate = new Button("Rozpocznij symulacje");

        controlPanel.getChildren().add(explorator);
        controlPanel.getChildren().add(simulate);

        Scene firstScene = new Scene(controlPanel);

        App app = this;
        explorator.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        configureFileChooser(fileChooser);
                        app.config = fileChooser.showOpenDialog(primaryStage); //tu bedzie primary stage
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
                SimulationParameters simulationParameters = new SimulationParameters(this.parameters);
                startSimulation(simulationParameters);
            }
            catch(InputMismatchException | FileNotFoundException e){
                System.out.println(e.getMessage());
            }
//            startSimulation();
        });


        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });


//        simulate.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
//            startSimulation();
//        });

        stage.setScene(firstScene);
        stage.show();
//        startSimulation();
    }






    private void startSimulation(SimulationParameters parameters) {
        Stage stage = new Stage();

        Thread thread = new Thread(parameters.getSimulationEngine());
        thread.start();

        stage.setOnCloseRequest(e -> {
            thread.stop();
        });





        parameters.setRunning(true);


        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> {
            if (parameters.getIsRunning()) {
                stopButton.setText("Start");
                thread.suspend();
                parameters.setRunning(false);
            }
            else {
                stopButton.setText("Stop");
                thread.resume();
                parameters.setRunning(true);
            }
        });

        parameters.createGrid();

        parameters.getvBox().getChildren().add(stopButton);
        parameters.getvBox().getChildren().add(gridPane);
        scene = new Scene(parameters.getvBox(), 700, 700);
        stage.setScene(scene);
        stage.show();
    }

}