package annotations;

public class Main {
    public static void main(String[] args) {
        Worker worker = new Worker();

        System.out.println("Публичные методы:");
        worker.greet("Даниил");
        worker.printSum(5, 6);
        worker.printFirstLetter("Hello");

        System.out.println();
        System.out.println("Аннотированные protected и private методы:");
        AnnotationInvoker.invokeAnnotated(worker);
    }
}
