package agh.ics.oop.mapElements;

import agh.ics.oop.attributes.Genotype;
import agh.ics.oop.attributes.MapDirection;
import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.maps.AbstractWorldMap;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Animal implements IMapElement {

    private Genotype genotype;
    private int genotypePosition;
    private Vector2d location;
    private int energy;
    private boolean isShuffle;
    private MapDirection direction = MapDirection.NORTH;
    private AbstractWorldMap map;
    private int birthDate;
    private int childrenCount = 0;
    private int grassCount = 0;
    private boolean isAlive = true;
    private int deathDate;
    public Animal(int startingEnergy, int genotypeLength, Vector2d location, AbstractWorldMap map, boolean isShuffle) {
        this.genotype = new Genotype(genotypeLength);
        this.energy = startingEnergy;
        this.location = location;
        this.direction = this.direction.intToDirection((int) (Math.random() * 8));
        this.genotypePosition = (int) (Math.random() * genotype.getGenotypeList().size());
        this.map = map;
        this.isShuffle = isShuffle;
        this.birthDate = 0;
    }
    public Animal(Animal firstParent, Animal secondParent, int startingEnergy, boolean mutation, AbstractWorldMap map, boolean isShuffle, int birthDate) {
        this.genotype = new Genotype(firstParent.getGenotype().getGenotypeList(), firstParent.getEnergy(), secondParent.getGenotype().getGenotypeList(), secondParent.getEnergy(), mutation);
        this.energy = startingEnergy;
        this.location = new Vector2d(firstParent.getLocation().getX(), firstParent.getLocation().getY());
        this.direction = this.direction.intToDirection((int) (Math.random() * 8));
        this.genotypePosition = (int) (Math.random() * genotype.getGenotypeList().size());
        this.map = map;
        this.isShuffle = isShuffle;
        this.birthDate = birthDate;
    }
    public Genotype getGenotype() {
        return genotype;
    }
    public Vector2d getLocation() {
        return location;
    }
    public void setLocation(Vector2d location){
        this.location = location;
    }
    public void turnBack(){
        this.direction.intToDirection((this.direction.toInt()+4)%8);
    }
    public int getEnergy() {
        return energy;
    }
    public void changeEnergy(int x){
        this.energy += x;
    }
    public void addChild(){
        this.childrenCount+=1;
    }
    public void addGrass() {this.grassCount+=1;}
    public void die(int deathDate){this.deathDate = deathDate;}
    public int getGenotypePosition() {
        return genotypePosition;
    }
    public MapDirection getDirection() {
        return direction;
    }
    public int getBirthDate() {
        return birthDate;
    }
    public int getChildrenCount() {
        return childrenCount;
    }
    public void setStatus() {
        isAlive = false;
    }
    public boolean getIsAlive() {
        return isAlive;
    }
    public int getDeathDate() {
        return deathDate;
    }
    public int getGrassCount() {
        return grassCount;
    }
    public String toString() {
        switch (this.direction) {
            case EAST: return "→";
            case WEST: return "←";
            case NORTH: return "↑";
            case SOUTH: return "↓";
            case NORTHEAST: return "⬈";
            case NORTHWEST: return "⬉";
            case SOUTHEAST: return "⬊";
            case SOUTHWEST: return "⬋";
        }
        return "";
    }

    @Override
    public Circle toShape(int startingEnergy, int size) {
        Circle circle = new Circle();
        circle.setRadius(size);
        double colorValue =  (double) (this.getEnergy()) / (double) (startingEnergy);
        if (colorValue > 1) {
            circle.setFill(Color.rgb(255, 0, 0));
        }
        else {
            int color = (int) (255*colorValue);

            circle.setFill(Color.rgb(color,0,0));
        }
        return circle;
    }

    public void move(){
        if (isShuffle) {
            if (Math.random() < 0.2) {
                genotypePosition = (int) (Math.random() * genotype.getLength());
            }
        }
        int currentMove = this.genotype.getGene(this.genotypePosition);
        int currentDirection = this.direction.toInt();
        this.direction = this.direction.intToDirection((currentMove + currentDirection) % 8);
        map.canMoveTo(this);
        this.genotypePosition = (this.genotypePosition+1)%this.genotype.getLength();
    }

}
