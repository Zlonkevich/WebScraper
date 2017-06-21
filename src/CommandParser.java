package WebScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fedor on 17.05.2017.
 */

public class CommandParser implements Runnable {
    private String[]            commands    = null; // массив команд, который получается при парсинге из КС
    private String              line        = null;
    private BufferedReader      br          = new BufferedReader(new InputStreamReader(System.in));
    private List<String>        keyWords    = null; // список ключевых слов, которые нужны для парсинга сайтов
    private List<String>        urlList     = null;  // список URL сайтов, которые в последствии парсятся
    private WebEntityFactory    webEntityFactory = null;
    private ParsedQuery         parsedQuery = null;

    //читает введенную в КС команду и парсит ее методом parseCommand()
    void readFromCommandLine() {
        try {
            line = br.readLine();
            if (line != null) {
                parseCommand(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка ввода команды!");
        }
    }

    //парсит строку в массив String
    private void parseCommand(String command) {
        commands = command.split(" ");
//        showCommandList();
        parsedQuery = new ParsedQuery(); // создали объект запроса

        urlList = new ArrayList<>();
        keyWords = new ArrayList<>();

        //в цикле добаляются в соответствующие списки адреса сайтов и ключевые слова для поиска, а также передаются
        // флаги объекту запроса.
        //Также можно ввести в КС команду stop, которая выключит программу.
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].startsWith("http://")) {
                urlList.add(commands[i]);
            }
            if (commands[i].startsWith("https://")) {
                urlList.add(commands[i]);
            }
            if (commands[i].contains(":\\")) {
                FileParser fileParser = new FileParser(commands[i]);
                urlList = fileParser.getURLList();
            }
            //---------------------------------------------------------------------
            if (commands[i].endsWith(",")) {
                keyWords.add(commands[i].substring(0, (commands[i].length() - 1)));
            }
            if (i > 0 && !commands[i].startsWith("-") && !commands[i].endsWith(",")) {
                keyWords.add(commands[i]);
            }
            //---------------------------------------------------------------------
            if (commands[i].equals("-v")) {
                parsedQuery.setV(1);
            }
            if (commands[i].equals("-w")) {
                parsedQuery.setW(1);
            }
            if (commands[i].equals("-c")) {
                parsedQuery.setC(1);
            }
            if (commands[i].equals("-e")) {
                parsedQuery.setE(1);
            }
            //---------------------------------------------------------------------
            if (commands[i].equals("stop")) {
                Thread.currentThread().stop();
            }
//            if (commands[i].equals("stopEntities")) { //команда останавливает все потоки сущностей
//                if(webEntityFactory != null){
//                    webEntityFactory.stopAllEntities();
//                }
//            }
            else{}

        }
        // передаем в объект запроса на парсинг найденные ключевые слова
        parsedQuery.setKeyWordsList(keyWords);

        //создаем фабрику по созданию веб-сущностей (передаем ей как параметры список сайтов и объект запроса)
        createWebEntityFactory(urlList, parsedQuery);
    }

    //создает фабрику по созданию веб-сущностей
    private void createWebEntityFactory (List<String> urlList, ParsedQuery pq){
        if((urlList != null) && (parsedQuery != null))
            webEntityFactory = new WebEntityFactory(urlList, parsedQuery);
    }

    public String[] getCommands() {
        return commands;
    }

    @Override
    public void run() {
        while (true) {
            readFromCommandLine();
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("CommandParser thread was interrupted!");
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //выводит список парсенных команд покомандно
    public void showCommandList() {
        if (commands != null) {
            for (int i = 0; i < commands.length; i++) {
                System.out.println(commands[i]);
            }
        }
    }

    public void showKeyWordsList(){
        Iterator iter = keyWords.iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }
    }

    public void showURLList(){
        Iterator iter = urlList.iterator();
        while(iter.hasNext()){
            System.out.println(iter.next());
        }
    }

}
