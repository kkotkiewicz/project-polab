package agh.ics.oop.gui;

import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.grassGenerators.EquatorGrassGenerator;
import agh.ics.oop.grassGenerators.IGrassGenerator;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.maps.Earth;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Label;


public class App extends Application {
    public GridPane gridPane = new GridPane();
    public Scene scene = new Scene(gridPane, 600, 600);

    private AbstractWorldMap map;
    private IGrassGenerator grassGenerator;
    private Thread thread;
    private SimulationEngine simulationEngine;

    private int height = 600;

    private Vector2d upperRight;

    @Override
    public void init() throws Exception {
        super.init();

//        Parameters parameters = getParameters();
//        List<String> elements = parameters.getRaw();
//        String[] args = new String[elements.size()];
//        for (int i = 0; i < elements.size(); i++) {
//            args[i] = elements.get(i);
//        }
        try {
            IGrassGenerator grassGenerator = new EquatorGrassGenerator(20, 20);
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        VBox vBox = new VBox();

        this.thread = new Thread(this.simulationEngine);
        this.thread.start();

        createGrid();

        vBox.getChildren().add(gridPane);
        scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createGrid() {
        int height = this.height/(this.upperRight.getY()+1);
        int width = this.height/(this.upperRight.getY()+1);

        int radius = (int) ((this.height/(this.upperRight.getY()+1))/5);
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
        });
    }

}