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
//    private MapVisualizer visualizer;



    private List<App> observers = new ArrayList<>();


    public int getStartingEnergy(){
        return this.startingEnergy;
    }

    public void addObserver(App observer){
        this.observers.add(observer);
    }

    public void removeObserver(App observer){
        this.observers.remove(observer);
    }

    public void refreshObserver(){
        for(App observer: observers){
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

    @Override
    public void run() {
        for(int i=0; i<1000; i++){
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
            System.out.println(this.animals.size());

            map.nextDay();

        }
    }
}
