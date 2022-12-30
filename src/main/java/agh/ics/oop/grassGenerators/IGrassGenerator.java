package agh.ics.oop.grassGenerators;

import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.mapElements.Plant;

import java.util.HashMap;

public interface IGrassGenerator {

    public Vector2d generateGrass(HashMap<Vector2d, Plant> plantPositions);
}
