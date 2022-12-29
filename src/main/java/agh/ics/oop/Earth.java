package agh.ics.oop;

import java.util.HashMap;

public class Earth extends AbstractWorldMap{

    public Earth(int x, int y, int copulationCost, boolean mutation, IGrassGenerator grassGenerator, int grassEnergy, int minEnergy){
        this.upperRight = new Vector2d(x, y);
        this.copulationCost = copulationCost;
        this.mutation = mutation;
        this.grassGenerator = grassGenerator;
        this.grassEnergy = grassEnergy;
        this.grassSpawnAmount = grassSpawnAmount;
        this.minEnergy = minEnergy;
        this.animalPositions = new HashMap<>();
        this.plantPositions = new HashMap<>();
    }

    public void canMoveTo(Animal animal) {
        Vector2d moveTo = animal.getLocation().add(animal.getDirection().toUnitVector());
        if (moveTo.getY() >= this.upperRight.getY() || moveTo.getY() < 0) {
            animal.turnBack();
            moveTo = new Vector2d(moveTo.getX(), animal.getLocation().getY());
        }
        if (moveTo.getX() >= this.upperRight.getX() || moveTo.getX() < 0) {
            if ( moveTo.getX() < 0 ) {
                moveTo = new Vector2d(moveTo.getX() + this.upperRight.getX(), moveTo.getY());
            }
            moveTo = new Vector2d(moveTo.getX() % this.upperRight.getX(),moveTo.getY());
        }
        this.removeAnimal(animal);
        System.out.println(animal.getLocation().toString());
        animal.setLocation(moveTo);
        this.place(animal);
        System.out.println(moveTo.toString());
    }
}
