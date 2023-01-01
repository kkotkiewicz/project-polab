package agh.ics.oop.attributes;


import java.util.ArrayList;
import java.util.Arrays;


public class Genotype {
    private ArrayList<Integer> genotypeList = new ArrayList<>();

    private int length;
    private int minMutation;
    private int maxMutation;

    public Genotype(int length) {
        this.length = length;
        createGenotype(length);
    }

    public Genotype(ArrayList<Integer> leftGenotype, int leftEnergy, ArrayList<Integer> rightGenotype, int rightEnergy, boolean mutation, int minMutation, int maxMutation) {
        mergeGenotype(leftGenotype, leftEnergy, rightGenotype, rightEnergy);
        if (mutation) mutateGenotype();
        else correctGenotype();
        this.length = leftGenotype.size();
        this.minMutation = minMutation;
        this.maxMutation = maxMutation;
    }

    private void createGenotype(int length) {
        for (int i = 0; i < length; i++) {
            genotypeList.add( (int)(Math.random() * 8) );
        }
    }

    public String toString(){
        String gene = "";
        for(Integer number: genotypeList){
            gene+=number.toString();
        }
        return gene;
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
        int numOfMutation = (int) (Math.random()*(this.maxMutation-this.minMutation) + this.minMutation);
        for (int i = 0; i < numOfMutation; i++) {
            genotypeList.set((int) (Math.random()*length), (int) (Math.random()*8));
        }
    }

    private void correctGenotype() {
        int numOfMutation = (int) (Math.random()*(this.maxMutation-this.minMutation) + this.minMutation);
        for (int i = 0; i < numOfMutation; i++) {
            int genToMutate = (int) (Math.random()*length);
            if(Math.random()< 0.5){
                if(genotypeList.get(genToMutate)==7){
                    genotypeList.set(genToMutate, 0);
                }
                else{
                    genotypeList.set(genToMutate, genotypeList.get(genToMutate)+1);
                }
            }
            else{
                if(genotypeList.get(genToMutate)==0){
                    genotypeList.set(genToMutate, 7);
                }
                else{
                    genotypeList.set(genToMutate, genotypeList.get(genToMutate)-1);
                }
            }
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
