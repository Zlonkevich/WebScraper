package WebScraper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Fedor on 19.05.2017.
 */

/*
Фабрика веб-сущностей - классов, которые создаются отдельно для каждого сайта и каждая в своем потоке.
Класс создается вызовом метода createWebEntityFactory у CommandParser. В качестве параметров получает на вход
список сайтов, которые нужно парсить, и объект класса ParsedQuery - контейнер, содержащий информацию для парсинга сайта.
(список ключевых слов и набор специальных команд для вывода информации по сайту.)
 */

public class WebEntityFactory {

    private List<String>    urlList     = null;    //список URL сайтов для парсинга
    private List<WebEntity> webEntityList;  //список веб-сущностей, содержащий все созданные сущности
    private ParsedQuery     parsedQuery = null;   // ссылка на объект класса, содержащего инфу для парсинга
    private List<Thread>    entityThreadList;  // список созданных поток. Нужен для управления созданными потоками.

    //конструктор класса. Принимает на вход список сайтов и объект-запрос, содержащий инфу для парсинга сайта
    public WebEntityFactory(List<String> urlList, Query parsedQuery){
        this.urlList = urlList;
        this.parsedQuery = (ParsedQuery) parsedQuery;
        createWebEntity();
    }

    //метод создает веб-сущность по одной штуке на каждый сайт, добавляет их в список веб-сущностей.
    //также инициализирует списки веб-сущностей и их потоков
    public void createWebEntity (){
        webEntityList = new ArrayList<>();
        entityThreadList = new ArrayList<>();
        Iterator iter = urlList.iterator();
        while (iter.hasNext()){
            WebEntity webEntity = new WebEntity((String)iter.next(), parsedQuery);
            webEntityList.add(webEntity);
            startInNewThread(webEntity);
        }
    }

    //метод создает отдельный поток для каждой веб-сущности и стартует его, а также добавляет потоки в единый список потоков.
    private void startInNewThread(WebEntity we){
        Thread thread = new Thread(we);
        entityThreadList.add(thread);
        thread.start();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* метод останавливает потоки, пробегая по списку потоков.
    выдает ошибку - Exception in thread "Thread-0" java.util.NoSuchElementException
    Скорее всего это связано с явным преобразованием типов */
    public void stopAllEntities(){
        if (!entityThreadList.isEmpty()){
            Iterator iter = entityThreadList.iterator();
            while (iter.hasNext()){
                System.out.println(((Thread) iter.next()).getName() + " stopped");
                ((Thread) iter.next()).stop();
            }
            System.out.println("Entities stopped!");
        }
    }

    public List getWebEntityList(){
        return webEntityList;
    }


}
