package WebScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fedor on 19.05.2017.
 */
/*
класс Веб-сущность. Создается одноименной фабрикой отдельно для каждого сайта.
При создании получает на вход ссылку на сайт и объект класса-запроса, содержащего инфу для парсинга сайта.
Получает соединение к сайту.
Далее разбирает запрос и вызывает соответствующие методы по парсингу сайта.
 */

public class WebEntity implements Runnable {

    private String url = null;
    private ParsedQuery query = null;
    private String HTMLCode = null;
    private String pureText = null;
    private long timeCosts = 0;


    HttpURLConnection connection = null;

    //конструктор принимает на вход URL и объект типа query - контейнер, содержащий информацию по запросу.
    public WebEntity(String url, Query query) {
        this.url = url;
        this.query = (ParsedQuery) query;
    }


    //метод с помощью регулярных выражений вытаскивает из HTML-кода страницы только контент
//в методе еще не реализован переключатель паттерна для англоязычных и русскоязычных сайтов
    private void parseURL() {
        Pattern p = Pattern.compile(">[[a-zA-Zа-яА-Я_0-9]\\s]+<");
        Matcher m = p.matcher(HTMLCode);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }
        trashFilter(sb.toString());    //очищаем контент от скобок, пробелов и прочего
    }


    // метод очищает парсенный текст от скобок, пробелов и переводов каретки
    private void trashFilter(String contentWithTrash) {
        String[] splitted;

        splitted = contentWithTrash.split("<>");
        StringBuilder stringBuilder = new StringBuilder();
        for (String t : splitted)
            stringBuilder.append(" " + t); // на этом моменте собирается строка без <>, но с пробелами, чтобы слова не слипались
        String contentWithoutBraces = stringBuilder.toString();

        splitted = contentWithoutBraces.split(" |\\n"); // разбил строку по пробелу или переводу на новую строку
        stringBuilder = new StringBuilder(); // очистил StringBuilder
        for (String t : splitted) { // чистим от лишних пробелов
            t.trim();
            if (!t.isEmpty()) {
                stringBuilder.append(t + " "); // добавляем по одному пробелу между словами
            }
        }

//выводит на экран чистый контент сайта
//        System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////"
//                + "\n" + stringBuilder);

        pureText = stringBuilder.toString();
    }


    //определяет какие методы должны отработать и запускает их на выполнение

    private void parseDriver() {
        long start = System.nanoTime();

        //если в запросе есть команда -w, пробегаем по всему списку ключевых слов и с каждым из них вызываем метод
        // keywordAmount(), который печает в КС ответ, содержащий кол-во этого ключевого слова на странице.
        if (query.getW() != 0) {
            Iterator iter = query.getKeyWordsList().iterator();
            while (iter.hasNext())
                keywordAmount((String) iter.next());
        }
        //подсчитывает кол-во символов на странице
        if (query.getC() != 0)
            charAmount();

        //считает кол-во предложений с ключевым словом. Реализовать!!!
        if (query.getE() != 0)
            System.out.println("get chars amount");

        long end = System.nanoTime();
        this.timeCosts = end - start;   //подсчет затраченного на операции времени

        if (query.getV() != 0)
            System.out.println("Verbosity: " + timeCosts + " nanoSeconds spent.");
    }

    //осуществляет поиск ключевого слова в подготовленном тексте и печатает результат поиска в КС
    private void keywordAmount(String keyword) {
        int count = 0;
        for (String t : pureText.split(" ")) {
            if (t.equals(keyword))
                count++;
        }
        System.out.println(url + " contains " + count + " keywords " + "\"" + keyword + "\"");
    }

    //метод выводит на экран количество символов контента сайта без учета пробелов
    private void charAmount() {
        int charAmount = 0;
        for (char t : pureText.toCharArray())
            if (t != ' ')
                charAmount++;
        System.out.println(url + " contains " + charAmount + " symbols");
    }

    //метод не реализован =(
    private void sentenceContainsKeyword(String keyword) {
        int sentenceAmount = 0;

        System.out.println(url + " contains " + sentenceAmount + " sentences with keyword: " + keyword);

    }


    //подключается по указанному адресу
    private void getConnection() {
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //считывает весь HTML код страницы. здесь можно вывести в КС HTML-код страницы
    private void setHTMLCode() {
        try {
            StringBuilder sb = new StringBuilder();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                HTMLCode = sb.toString();
//здесь можно вывести в КС HTML-код страницы
//                System.out.println(HTMLCode);
            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }


    @Override
    public void run() {
        getConnection();
        setHTMLCode();
        parseURL();
        parseDriver();

//        System.out.println(Thread.currentThread().getName() + " отработал");

    }
}
