package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Collections;

public abstract class AbstractWorldMap {
    protected CompareAnimals comparator = new CompareAnimals();
    protected Vector2d bottomLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;
    protected int copulationCost;
    protected int minEnergy;
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
        Collections.sort(this.animalPositions.get(animal.getLocation()), comparator);

    }

    public void copulation(Animal animal1, Animal animal2){
        Animal child = new Animal(animal1, animal2, 2*this.copulationCost, mutation, this, isShuffle, this.daysPassed);
        this.place(child);
        animal1.changeEnergy((-1)*this.copulationCost);
        animal2.changeEnergy((-1)*this.copulationCost);
        animal1.addChild();
        animal2.addChild();
        Collections.sort(this.animalPositions.get(animal1.getLocation()), comparator);
    }

    public void bigCopulation(){
        for(Vector2d key: this.animalPositions.keySet()){
            if(this.animalPositions.get(key).size()>1){
                if(this.animalPositions.get(key).get(1).getEnergy()>this.minEnergy){
                    this.copulation(this.animalPositions.get(key).get(0), this.animalPositions.get(key).get(1));
                }
            }
        }

    }

    public void removeAnimal(Animal animal){
        this.animalPositions.get(animal.getLocation()).remove(animal);
        if(this.animalPositions.get(animal.getLocation()).size()==0){
            this.animalPositions.remove(animal.getLocation());
        }
    }

    public void bigRemoval(){
        for(ArrayList<Animal> animals: this.animalPositions.values()){
            for(Animal animal: animals){
                if(animal.getEnergy()<=0){
                    this.removeAnimal(animal);
                }
            }
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
        Collections.sort(this.animalPositions.get(animal.getLocation()), comparator);
    }

    public void bigFeast(){
        for(Vector2d key : this.animalPositions.keySet()){
            if(this.plantPositions.containsKey(key)){
                this.feast(key, this.animalPositions.get(key).get(0));
            }
        }
    }

    public void generateGrass(int quantity) {
        for (int i = 0; i < quantity; i++) {
            Vector2d newPosition = grassGenerator.generateGrass(this.plantPositions);
            if (newPosition != null) {
                Plant newPlant = new Plant(newPosition);
                plantPositions.put(newPosition, newPlant);
            }
        }
    }

}
