package agh.ics.oop.maps;

import agh.ics.oop.mapElements.Animal;
import agh.ics.oop.grassGenerators.IGrassGenerator;
import agh.ics.oop.attributes.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;

public class Earth extends AbstractWorldMap {

    public Earth(int x, int y, int copulationCost, boolean mutation, IGrassGenerator grassGenerator, int grassEnergy, int minEnergy, int grassSpawnAmount, int plantsAtStart, int minMutation, int maxMutation){
        this.upperRight = new Vector2d(x-1, y-1);
        this.copulationCost = copulationCost;
        this.mutation = mutation;
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
        this.grassGenerator = grassGenerator;
        this.grassEnergy = grassEnergy;
        this.grassSpawnAmount = grassSpawnAmount;
        this.plantsAtStart = plantsAtStart;
        this.minEnergy = minEnergy;
        this.animalPositions = new HashMap<>();
        this.plantPositions = new HashMap<>();
        this.grassSpawnAmount = grassSpawnAmount;
        this.deadAnimals = new int[x][y];
        this.availblePositions = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                this.deadAnimals[i][j] = 0;
                this.availblePositions.add(new Vector2d(i, j));
            }
        }
        this.notPrePositions = new ArrayList<>();
    }

    public void canMoveTo(Animal animal) {
        Vector2d moveTo = animal.getLocation().add(animal.getDirection().toUnitVector());
        if (moveTo.getY() > this.upperRight.getY() || moveTo.getY() < 0) {
            animal.turnBack();
            moveTo = new Vector2d(moveTo.getX(), animal.getLocation().getY());
        }
        if (moveTo.getX() >= this.upperRight.getX() || moveTo.getX() < 0) {
            if ( moveTo.getX() < 0 ) {
                moveTo = new Vector2d(moveTo.getX() + this.upperRight.getX() + 1, moveTo.getY());
            }
            moveTo = new Vector2d(moveTo.getX() % (this.upperRight.getX() + 1),moveTo.getY());
        }
        this.removeAnimal(animal);
        animal.setLocation(moveTo);
        this.place(animal);
    }
}
