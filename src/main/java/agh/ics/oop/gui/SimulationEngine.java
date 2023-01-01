package agh.ics.oop.gui;

//import agh.ics.oop.gui.app;

import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.mapElements.Animal;
import agh.ics.oop.attributes.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements Runnable{

    private int moveDelay;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private AbstractWorldMap map;
    private final int startingEnergy;
    private final int genotypeLength;
    private final boolean isShuffle;
    private int numOfAnimals = 0;
    private int numOfPlants = 0;
    private int averageEnergy = 0;
    private double averageLifespan = 0;
    private String mostCommonGene="";
    private int freeSpace = 0;

//    private MapVisualizer visualizer;



    private List<SimulationParameters> observers = new ArrayList<>();

    public int getNumOfAnimals(){
        return this.numOfAnimals;
    }

    public void uptadeNumOfAnimals(){
        this.numOfAnimals = animals.size();
    }

    public int getNumOfPlants(){
        return this.numOfPlants;
    }

    public int getAverageEnergy(){
        return this.averageEnergy;
    }

    public void uptadeAverageEnergy(){
        int sum = 0;
        for(Animal animal: animals){
            sum+=animal.getEnergy();
        }
        this.averageEnergy = (int) (sum/this.animals.size());
    }

    public double getAverageLifespan() {
        return this.averageLifespan;
    }

    public String getMostCommonGene(){
        return this.mostCommonGene;
    }

    public int getFreeSpace(){
        return this.freeSpace;
    }




    public int getStartingEnergy(){
        return this.startingEnergy;
    }

    public void addObserver(SimulationParameters observer){
        this.observers.add(observer);
    }

    public void removeObserver(SimulationParameters observer){
        this.observers.remove(observer);
    }

    public void refreshObserver(){
        for(SimulationParameters observer: observers){
            observer.refresh();
        }
    }

    public SimulationEngine(AbstractWorldMap map, int numOfAnimals, int startingEnergy, int genotypeLength, int moveDelay, boolean isShuffle){
        this.map = map;
        this.startingEnergy = startingEnergy;
        this.genotypeLength = genotypeLength;
        this.moveDelay = moveDelay;
        this.isShuffle = isShuffle;
//        this.visualizer = visualizer;
        generateAnimals(numOfAnimals);
    }

    public void generateAnimals(int numOfAnimals) {
        for(int i=0; i<numOfAnimals; i++){
            int x = (int) (Math.random() * map.getUpperRight().getX());
            int y = (int) (Math.random() * map.getUpperRight().getY());
            Vector2d generatedPosition = new Vector2d(x, y);
            Animal animal = new Animal(this.startingEnergy, this.genotypeLength, generatedPosition, this.map, this.isShuffle);
            animals.add(animal);
            map.place(animal);
        }
        this.map.generateGrass();
    }

    public void writeToCSV(){
        
    }

    @Override
    public void run() {
        while(!animals.isEmpty()){
//            System.out.println(visualizer.draw(new Vector2d(0,0), new Vector2d(map.getUpperRight().getX() - 1, map.getUpperRight().getY() - 1)));
//            System.out.println(animals.size());
            ArrayList<Animal> toRemove = this.map.bigRemoval();
            for(Animal animal: toRemove){
                this.animals.remove(animal);
                map.removeAnimal(animal);
            }
            for(Animal animal: this.animals){
                animal.move();

            }
            this.map.bigFeast();
            this.animals.addAll(this.map.bigCopulation());
            for (Animal animal: animals) {
                animal.changeEnergy(-1);
            }
            this.map.generateGrass();


            try {
                Thread.sleep(50);
                refreshObserver();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            this.mostCommonGene = map.getMostCommonGene();
            this.uptadeNumOfAnimals();
            this.numOfPlants = map.getNumOfPlants();
            this.uptadeAverageEnergy();
            this.averageLifespan = map.getAverageLifespan();
            this.freeSpace = map.getFreeSpace();

            map.nextDay();



        }
    }
}
