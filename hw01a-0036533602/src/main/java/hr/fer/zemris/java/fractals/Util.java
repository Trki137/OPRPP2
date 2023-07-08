package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.math.Complex;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static List<Complex> getRoots(){
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                "Please enter at least two roots, one root per line. Enter 'done' when done");

        Scanner sc = new Scanner(System.in);
        String input;
        List<Complex> roots = new ArrayList<>();
        while(true){
            System.out.printf("Root %d>", roots.size() + 1);
            input = sc.nextLine().trim();

            if (input.isBlank()) {
                System.out.println("Input can't be blank");
                continue;
            }

            if(input.equalsIgnoreCase("done")){
                if(roots.size() >= 2)break;
                System.out.println("2 roots needed minimum");
                continue;
            }

            roots.add(Complex.parse(input));
        }
        return roots;
    }
}
