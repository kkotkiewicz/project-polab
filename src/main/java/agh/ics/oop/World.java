package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        IGrassGenerator grassGenerator = new EquatorGrassGenerator(11, 11);
        AbstractWorldMap map = new Earth(10, 10, 5, true, grassGenerator, 5, 5);
        MapVisualizer mapVisualizer = new MapVisualizer(map);
//        for (int i = 0; i < 5; i++) {
//            Animal animal = new Animal(50, 5, new Vector2d(2, i), map, true);
//            map.place(animal);
//            animal.move();
//            System.out.println(mapVisualizer.draw(new Vector2d(0,0), new Vector2d(9, 9)));
//            animal.move();
//            System.out.println(mapVisualizer.draw(new Vector2d(0,0), new Vector2d(9, 9)));
//        }
        Animal animal = new Animal(50, 5, new Vector2d(0,0), map, false);
        map.place(animal);
        map.generateGrass(10);
        for (int i = 0; i < 30; i++) {
            animal.move();
            System.out.println(mapVisualizer.draw(new Vector2d(0,0), new Vector2d(10, 10)));
        }
        System.out.println(mapVisualizer.draw(new Vector2d(0,0), new Vector2d(10, 10)));
    }
}
