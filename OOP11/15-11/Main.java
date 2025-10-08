public class Main {
    public static void main(String[] args) {
        Helpers sm = new Helpers();

        System.out.println("Reading from console:");
        sm.readFromConsole();
        sm.printToConsole();
        sm.printToFile("output.txt");
        sm.readFromFile("output.txt");
        sm.printToObject("teachers.dat");
        sm.readFromObject("teachers.dat");
    }
}
