package agh.ics.oop;

import agh.ics.oop.mapElements.Animal;

import java.util.Comparator;

public class CompareAnimals implements Comparator<Animal> {

    public int compare(Animal animal1, Animal animal2){
        if(animal1.getEnergy() == animal2.getEnergy()){
            if(animal1.getBirthDate() == animal2.getBirthDate()){
                if(animal1.getChildrenCount() == animal2.getChildrenCount()){
                    return 1;
                }
                else{
                    return animal2.getChildrenCount()-animal1.getChildrenCount();
                }
            }
            else{
                return animal2.getBirthDate() - animal1.getBirthDate();
            }
        }
        else{
            return animal2.getEnergy() - animal1.getEnergy();
        }
    }
}
