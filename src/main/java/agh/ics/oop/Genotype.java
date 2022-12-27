package agh.ics.oop;


import java.util.ArrayList;

abstract public class AbstractGenotype {
    private ArrayList<Integer> genotypeList = new ArrayList<Integer>();

    AbstractGenotype(int length) {
        createGenotype(length);
    }

    AbstractGenotype(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy, boolean mutation) {
        mergeGenotype(leftGenotype, leftEnergy, rightGenotype, rightEnergy);
        if (mutation) mutateGenotype();
        else correctGenotype();
    }

    private void createGenotype(int length) {
        for (int i = 0; i < length; i++) {
            genotypeList.add( (int)(Math.random() * 8) );
        }
    }

    private void mergeGenotype(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy) {
        
    }

    private void mutateGenotype() {

    }

    private void correctGenotype() {

    }

    public ArrayList<Integer> getGenotypeList() {
        return genotypeList;
    }
}
