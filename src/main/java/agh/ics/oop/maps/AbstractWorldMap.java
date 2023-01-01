package agh.ics.oop.maps;

import agh.ics.oop.*;
import agh.ics.oop.attributes.Genotype;
import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.grassGenerators.IGrassGenerator;
import agh.ics.oop.mapElements.Animal;
import agh.ics.oop.mapElements.IMapElement;
import agh.ics.oop.mapElements.Plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public abstract class AbstractWorldMap {
    protected CompareAnimals comparator = new CompareAnimals();
    protected Vector2d bottomLeft = new Vector2d(0, 0);
    protected Vector2d upperRight;
    protected int copulationCost;
    protected int minEnergy;
    protected boolean mutation;
    protected int minMutation;
    protected int maxMutation;
    protected boolean isShuffle;
    protected int daysPassed = 0;
    protected int[][] deadAnimals;
    protected ArrayList<Vector2d> availblePositions;
    protected ArrayList<Vector2d> notPrePositions;
    private double averageLifespan = 0;
    private int numOfDeadAnimals = 0;
    protected IGrassGenerator grassGenerator;
    protected int grassEnergy;
    protected int grassSpawnAmount;
    protected int plantsAtStart;

    public abstract void canMoveTo(Animal animal);

    public void addGene(Genotype genotype){
        if(this.genotypes.containsKey(genotype.toString())){
            this.genotypes.put(genotype.toString(), this.genotypes.get(genotype.toString())+1);
        }
        else{
            this.genotypes.put(genotype.toString(), 1);
        }
    }

    public void removeGene(Genotype genotype){
        if(this.genotypes.get(genotype.toString()).equals(1)){
            this.genotypes.remove(genotype.toString());
        }
        else{
            this.genotypes.put(genotype.toString(), this.genotypes.get(genotype.toString())-1);
        }
    }

    public String getMostCommonGene(){
        String mostCommon = "";
        int quantity = 0;
        for(String key: this.genotypes.keySet()){
            if(this.genotypes.get(key)>quantity){
                quantity=this.genotypes.get(key);
                mostCommon = key;
            }
        }
        return mostCommon;
    }

    public int getFreeSpace(){
        int freeSpace = this.upperRight.getX()*this.upperRight.getY() - (this.animalPositions.size() + this.plantPositions.size());
        for(Vector2d key: animalPositions.keySet()){
            if(plantPositions.containsKey(key)){
                freeSpace += 1;
            }
        }
        return freeSpace;
    }

    protected HashMap<String, Integer> genotypes = new HashMap<>();

    protected HashMap<Vector2d, ArrayList<Animal>> animalPositions;

    protected HashMap<Vector2d, Plant> plantPositions;

    public int getDaysPassed() {
        return daysPassed;
    }

    public double getAverageLifespan(){
        return this.averageLifespan;
    }

    public void updateAverageLifespan(int days){
        this.averageLifespan =((double)((this.numOfDeadAnimals*this.averageLifespan)+days)/(double)(this.numOfDeadAnimals+1));
        this.numOfDeadAnimals+=1;
    }

    public void nextDay(){
        this.daysPassed+=1;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }

    public int getNumOfPlants(){
        return plantPositions.size();
    }

    public void place(Animal animal) {
        if(!this.animalPositions.containsKey(animal.getLocation())){
            this.animalPositions.put(animal.getLocation(), new ArrayList<Animal>());
        }
        this.animalPositions.get(animal.getLocation()).add(animal);
//        Collections.sort(this.animalPositions.get(animal.getLocation()), comparator);
        this.animalPositions.get(animal.getLocation()).sort(comparator);
        this.addGene(animal.getGenotype());
    }

    public Animal copulation(Animal animal1, Animal animal2){
        Animal child = new Animal(animal1, animal2, 2*this.copulationCost, mutation, this, isShuffle, this.daysPassed, this.minMutation, this.maxMutation);
        this.place(child);
        animal1.changeEnergy((-1)*this.copulationCost);
        animal2.changeEnergy((-1)*this.copulationCost);
        animal1.addChild();
        animal2.addChild();
        this.animalPositions.get(animal1.getLocation()).sort(comparator);
        return child;
    }

    public ArrayList<Animal> bigCopulation(){
        ArrayList<Animal> children = new ArrayList<>();
        for(Vector2d key: this.animalPositions.keySet()){
            if(this.animalPositions.get(key).size()>1){
                if(this.animalPositions.get(key).get(1).getEnergy()>this.minEnergy && this.animalPositions.get(key).get(0).getEnergy()>this.minEnergy){
                    children.add(this.copulation(this.animalPositions.get(key).get(0), this.animalPositions.get(key).get(1)));
                }
            }
        }
        return children;
    }

    public void replaceAnimal(Animal animal) {
        this.animalPositions.get(animal.getLocation()).remove(animal);
        if(this.animalPositions.get(animal.getLocation()).size()==0){
            this.animalPositions.remove(animal.getLocation());
        }
    }

    public void removeAnimal(Animal animal){
        animal.die(this.daysPassed);
        this.removeGene(animal.getGenotype());
        this.updateAverageLifespan(animal.getLifespan());
        this.animalPositions.get(animal.getLocation()).remove(animal);
        if(this.animalPositions.get(animal.getLocation()).size()==0){
            this.animalPositions.remove(animal.getLocation());
        }
        this.deadAnimals[animal.getLocation().getX()][animal.getLocation().getY()] += 1;
        this.availblePositions.remove(animal.getLocation());
        if (!notPrePositions.contains(animal.getLocation())) {
            notPrePositions.add(animal.getLocation());
        }
    }

    public ArrayList<Animal> bigRemoval(){
        ArrayList<Animal> toRemove = new ArrayList<>();
        for(ArrayList<Animal> animals: this.animalPositions.values()){
            for(Animal animal: animals){
                if(animal.getEnergy()<=0){
                    toRemove.add(animal);
                    animal.setStatus();
                }
            }
        }
        return toRemove;
    }

    public boolean isOccupied(Vector2d location){
        if((this.animalPositions.containsKey(location))){
            if (!this.animalPositions.get(location).isEmpty()) {
                return true;
            }
            animalPositions.remove(location);
        }
        if (this.plantPositions.containsKey(location)) {
            return true;
        }
        return false;
    }

    public IMapElement objectAt(Vector2d location){
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
        animal.addGrass();
        this.plantPositions.remove(location);
        this.animalPositions.get(animal.getLocation()).sort(comparator);
    }

    public void bigFeast(){
        for(Vector2d key : this.animalPositions.keySet()){
            if(this.plantPositions.containsKey(key)){
                this.feast(key, this.animalPositions.get(key).get(0));
            }
        }
    }

    public void generateGrass() {
        int grassToSpawn = this.grassSpawnAmount;
        if(this.daysPassed == 0){
            grassToSpawn = this.plantsAtStart;
        }
        for (int i = 0; i < grassToSpawn; i++) {
            Vector2d newPosition = grassGenerator.generateGrass(this.plantPositions, this.deadAnimals, this.availblePositions, this.notPrePositions);
            if (newPosition != null) {
                Plant newPlant = new Plant(newPosition);
                plantPositions.put(newPosition, newPlant);
            }
        }
    }

}
