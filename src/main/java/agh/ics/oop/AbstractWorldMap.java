package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractWorldMap {
    protected Vector2d bottomLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;
    protected int copulationCost;
    protected boolean mutation;
    protected boolean isShuffle;
    protected int daysPassed = 0;
    protected IGrassGenerator grassGenerator;
    protected int grassEnergy;
    protected int grassSpawnAmount;

    public abstract void canMoveTo(Animal animal);

    protected HashMap<Vector2d, ArrayList<Animal>> animalPositions;

    protected HashMap<Vector2d, Plant> plantPositions;

    public void nextDay(){
        this.daysPassed+=1;
    }

    public void place(Animal animal) {
        if(!this.animalPositions.containsKey(animal.getLocation())){
            this.animalPositions.put(animal.getLocation(), new ArrayList<Animal>());
        }
        this.animalPositions.get(animal.getLocation()).add(animal);
    }

    public void copulation(Animal animal1, Animal animal2){
        Animal child = new Animal(animal1, animal2, 2*this.copulationCost, mutation, this, isShuffle, this.daysPassed);
        this.place(child);
        animal1.changeEnergy((-1)*this.copulationCost);
        animal2.changeEnergy((-1)*this.copulationCost);
    }

    public void removeAnimal(Animal animal){
        this.animalPositions.get(animal.getLocation()).remove(animal);
        if(this.animalPositions.get(animal.getLocation()).size()==0){
            this.animalPositions.remove(animal.getLocation());
        }
    }

    public boolean isOccupied(Vector2d location){
        if(this.animalPositions.containsKey(location) || this.plantPositions.containsKey(location)){
            return true;
        }
        return false;
    }

    public IMapElement ObjectAt(Vector2d location){
        if(this.animalPositions.containsKey(location)){
            return this.animalPositions.get(location).get(0);
        }
        if(this.plantPositions.containsKey(location)){
            return this.plantPositions.get(location);
        }
        return null;
    }

    public void feast(Vector2d location, Animal animal){
        animal.changeEnergy(this.grassEnergy);
        this.plantPositions.remove(location);
    }

    
}
