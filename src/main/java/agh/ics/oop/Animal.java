package agh.ics.oop;

public class Animal {

    private Genotype genotype;
    private int genotypePosition;
    private Vector2d location;
    private int energy;
    private MapDirection direction = MapDirection.NORTH;
    Animal (int startingEnergy, int genotypeLength, Vector2d location) {
        this.genotype = new Genotype(genotypeLength);
        this.energy = startingEnergy;
        this.location = location;
        this.direction.intToDirection((int) (Math.random() * 8));
        this.genotypePosition = (int) (Math.random() * genotype.getGenotypeList().size());
    }

    Animal (Animal firstParent, Animal secondParent, double fractionOfEnergy, boolean mutation) {
        this.genotype = new Genotype(firstParent.getGenotype().getGenotypeList(), firstParent.getEnergy(), secondParent.getGenotype().getGenotypeList(), secondParent.getEnergy(), mutation);
        this.energy = (int) (Math.floor(firstParent.getEnergy() * fractionOfEnergy) + Math.floor(secondParent.getEnergy() * fractionOfEnergy));
        this.location = new Vector2d(firstParent.getLocation().getX(), firstParent.getLocation().getY());
        this.direction.intToDirection((int) (Math.random() * 8));
        this.genotypePosition = (int) (Math.random() * genotype.getGenotypeList().size());
    }


    public Genotype getGenotype() {
        return genotype;
    }

    public Vector2d getLocation() {
        return location;
    }

    public int getEnergy() {
        return energy;
    }

    public int getGenotypePosition() {
        return genotypePosition;
    }
    public String toString() {
        return location.toString() + " " + energy;
    }
}
