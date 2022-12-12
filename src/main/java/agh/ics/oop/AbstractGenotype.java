package agh.ics.oop;


import java.util.ArrayList;

abstract public class AbstractGenotype {
    private ArrayList<Integer> genotypeList = new ArrayList<Integer>();

    AbstractGenotype(int length) {
        createGenotype(length);
    }

    AbstractGenotype(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy) {

    }

    private void createGenotype(int length) {
        for (int i = 0; i < length; i++) {
            genotypeList.add( (int)(Math.random() * 8) );
        }
    }

    abstract void mergeGenotypeNoMutation(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy);

    public ArrayList<Integer> getGenotypeList() {
        return genotypeList;
    }
}
