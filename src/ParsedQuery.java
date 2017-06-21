package WebScraper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fedor on 17.05.2017.
 */
/*
Класс-контейнер. Содержит в себе поля заполняемые в зависимости от запроса. Передается в качестве инструкции
объектам класса WebEntity для парсинга сайтов
 */
public class ParsedQuery implements Query {
    private int V = 0;
    private int W = 0;
    private int C = 0;
    private int E = 0;
    private List<String> keyWordsList = new ArrayList<>();


    public void setV(int v) {
        V = v;
    }

    public void setW(int w) {
        W = w;
    }

    public void setC(int c) {
        C = c;
    }

    public void setE(int e) {
        E = e;
    }

    public void setKeyWordsList(List<String> keyWordsList) {
        this.keyWordsList = keyWordsList;
    }

    public int getV() {
        return V;
    }

    public int getW() {
        return W;
    }

    public int getC() {
        return C;
    }

    public int getE() {
        return E;
    }

    public List<String> getKeyWordsList() {
        return keyWordsList;
    }
}
