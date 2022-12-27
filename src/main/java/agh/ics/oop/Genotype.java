package agh.ics.oop;


import java.util.ArrayList;


public class Genotype {
    private ArrayList<Integer> genotypeList = new ArrayList<>();

    private int length;

    Genotype(int length) {
        this.length = length;
        createGenotype(length);
    }

    Genotype(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy, boolean mutation) {
        mergeGenotype(leftGenotype, leftEnergy, rightGenotype, rightEnergy);
        if (mutation) mutateGenotype();
        else correctGenotype();
        this.length = leftGenotype.size();
    }

    private void createGenotype(int length) {
        for (int i = 0; i < length; i++) {
            genotypeList.add( (int)(Math.random() * 8) );
        }
    }

    private void mergeGenotype(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy) {
        boolean isSwapped = false;
        int breakpoint = (int) Math.floor( ( leftEnergy / ( leftEnergy + rightEnergy ) ) * leftGenotype.size() );
        if (Math.random() < 0.5) {
            isSwapped = true;
            breakpoint = leftGenotype.size() - breakpoint;
        }
        for (int i = 0; i < leftGenotype.size(); i++) {
            if ((i <= breakpoint && !isSwapped) || (i > breakpoint && isSwapped)) this.genotypeList.add(leftGenotype.get(i));
            else this.genotypeList.add(rightGenotype.get(i));
        }
    }

    private void mutateGenotype() {
        for (int i = 0; i < length; i++) {
            if (Math.random() < 0.2) genotypeList.set(i, (int) (Math.random() * 8));
        }
    }

    private void correctGenotype() {
        for (int i = 0; i < length; i++) {
            if (Math.random() < 0.2) genotypeList.set(i, genotypeList.get(i));
        }
    }

    public ArrayList<Integer> getGenotypeList() {
        return genotypeList;
    }

    public int getGene(int position){
        return genotypeList.get(position);
    }

    public int getLength(){
        return this.length;
    }


}
