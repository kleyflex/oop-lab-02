package annotations;

import java.util.Arrays;

public class Worker {
    public void greet(String name) {
        System.out.println("Привет, " + name);
    }

    public void printSum(int a, int b) {
        System.out.println("Сумма: " + (a + b));
    }

    public void printFirstLetter(String text) {
        printSymbol(text.charAt(0));
    }

    @Repeat(3)
    protected void printProduct(double a, double b) {
        System.out.println("Произведение: " + (a * b));
    }

    @Repeat(1)
    protected void describe(String name, int age, boolean isStudent) {
        System.out.println(name + ", возраст - " + age + ", студент - " + isStudent);
    }

    protected void printUpperCase(String text) {
        System.out.println(text.toUpperCase());
    }

    @Repeat(2)
    private void printSquare(Integer number) {
        System.out.println("Квадрат: " + number * number);
    }

    @Repeat(4)
    private void printArray(int[] numbers) {
        System.out.println("Массив: " + Arrays.toString(numbers));
    }

    private void printSymbol(char symbol) {
        System.out.println("Символ: " + symbol);
    }
}
