package WebScraper;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Fedor on 18.05.2017.
 */
/*
Класс, задачей которого является получение адреса файла на диске, его чтение и возвращение списка сайтов.
В данной реализации можно было и не выносить его в отдельный класс. Но мне думается, что впоследствии можно расширить
его функционал, если файлы с сайтами содаржали еще и запросы, отдельные для каждого сайта.
 */

public class FileParser {

    private String              path    = null;
    private File                file    = null;
    private ArrayList <String>  urlList = new ArrayList<>();

    //конструктор. Получает на вход адрес файла на диске
    FileParser(String path){
        this.path = path;
        file = new File(path);
        parseFile();
    }

    //метод, который читает список сайтов из файла
    private void parseFile(){
        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            System.out.println(file.getAbsolutePath());
            try {
                //В цикле построчно считываем файл и записываем ссылки в массив
                String s;
                while ((s = in.readLine()) != null) {
                    urlList.add(s);
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }



    public ArrayList<String> getURLList(){
        return urlList;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getFilePath(){
        return path;
    }

    public void showURLList(){
        for(String s : urlList){
            System.out.println(s);
        }
    }
}
