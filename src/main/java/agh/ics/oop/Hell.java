package agh.ics.oop;

public class Hell extends AbstractWorldMap{

    public Hell(int x, int y, int copulationCost, boolean mutation, IGrassGenerator grassGenerator, int grassEnergy, int grassSpawnAmount, int minEnergy){
        this.upperRight = new Vector2d(x, y);
        this.copulationCost = copulationCost;
        this.mutation = mutation;
        this.grassGenerator = grassGenerator;
        this.grassEnergy = grassEnergy;
        this.grassSpawnAmount = grassSpawnAmount;
        this.minEnergy = minEnergy;
    }

    public void canMoveTo(Animal animal){
        Vector2d location = animal.getLocation().add(animal.getDirection().toUnitVector());
        if(!(location.precedes(this.upperRight) || location.follows(this.bottomLeft))){
            int x = (int)(Math.random()*this.upperRight.getX());
            int y = (int)(Math.random()*this.upperRight.getY());
            location = new Vector2d(x, y);
            animal.changeEnergy((-1)*this.copulationCost);
        }
        this.removeAnimal(animal);
        animal.setLocation(location);
        this.place(animal);
    }


}
