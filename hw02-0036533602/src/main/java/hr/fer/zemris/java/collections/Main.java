package hr.fer.zemris.java.collections;

public class Main {
    public static void main(String[] args) {
        List col1 = new ArrayIndexedCollection();
        List col2 = new LinkedListIndexedCollection();
        col1.add("Ivana");
        col2.add("Jasna");
        Collection col3 = col1;
        Collection col4 = col2;
        col1.get(0);
        col2.get(0);
        col1.forEach(System.out::println); // Ivana
        col2.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna
    }
}
