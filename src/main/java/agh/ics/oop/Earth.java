package agh.ics.oop;

public class Earth extends AbstractWorldMap{

    public Earth(int x, int y, int copulationCost, boolean mutation, IGrassGenerator grassGenerator, int grassEnergy){
        this.upperRight = new Vector2d(x, y);
        this.copulationCost = copulationCost;
        this.mutation = mutation;
        this.grassGenerator = grassGenerator;
        this.grassEnergy = grassEnergy;
    }

    public void canMoveTo(Animal animal) {
        Vector2d moveTo = animal.getLocation().add(animal.getDirection().toUnitVector());
        if (moveTo.getY() >= this.upperRight.getY() || moveTo.getY() < 0) {
            animal.turnBack();
            moveTo = new Vector2d(moveTo.getX(), animal.getLocation().getY());
        }
        if (moveTo.getX() > this.upperRight.getX() || moveTo.getX() < 0) {
            moveTo = new Vector2d(moveTo.getX() % this.upperRight.getX(),moveTo.getY());
        }
        this.removeAnimal(animal);
        animal.setLocation(moveTo);
        this.place(animal);
    }
}
