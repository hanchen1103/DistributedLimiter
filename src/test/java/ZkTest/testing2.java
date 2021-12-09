package ZkTest;

import java.util.Scanner;


public class testing2 {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

        System.out.print("Enter a year: ");

        int year = reader.nextInt();

        boolean leap = false;

        if (year % 4 == 0) {
            if (year % 100 == 0) {
                leap = year % 400 == 0;
            }
            else
                leap = true;
        }
        else
            leap = false;

        if (leap)
            System.out.println(year + " is a leap year.");
        else
            System.out.println(year + " is not a leap year.");
    }
}