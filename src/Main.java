package WebScraper;

/**
 * Created by Fedor on 17.05.2017.
 */




public class Main {

    public static void main(String[] args) {
        Thread commandParser = new Thread(new CommandParser());
        commandParser.start();


    }
}
