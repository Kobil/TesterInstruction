package ru.apache_maven;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ko6a
 * Date: 29.05.13
 * Time: 13:39
 * Email: ko6a93@bk.ru
 */
public class Data {

    private static Random rnd = new Random();
    private static String[] names = new String[]{"Андрей", "Иван", "Михаил", "Александр"};
    private static String[] secondNames = new String[]{"Иванов", "Прохоров", "Соколов", "Сидоров"};
    private static String[] phones = new String[]{"+7 916 123 11 22", "+7 917 351 49 91", "+7 926 413 11 22", "+7 916 836 29 82"};
    private static String[] cities = new String[]{"Душанбе", "Москва", "Киев", "Ташкент"};

    public static String getName() {
        return names[rnd.nextInt(names.length)];
    }

    public static String getSecondName() {
        return secondNames[rnd.nextInt(secondNames.length)];
    }

    public static String getPhone() {
        return phones[rnd.nextInt(phones.length)];
    }

    public static String getCity() {
        return cities[rnd.nextInt(cities.length)];
    }

    public static String getDay() {
        return Integer.toString(rnd.nextInt(30) + 1);
    }

    public static String getYear() {
        return Integer.toString(rnd.nextInt(40) + 1980);
    }

    public static String getText(){
        String text[] = new String[]{"Погода в " + getCity(), getSecondName()+ " " + getName(),
                                        getSecondName()+ " " + getName() + " проживает в городе " + getCity()};
        return text[rnd.nextInt(10) % 3];
    }

    static String generateStringRandom(int len){
        String str = "";
        char[] ch = new char[2];
        for(int i = 0; i < len; i++){
            int val = rnd.nextInt(10);
            ch[0] = (char)(rnd.nextInt(10) + 48);
            ch[1] = (char)(rnd.nextInt(26) + 97);
            str += ch[val % 2];
        }
        return str;
    }

    static String generateStringToInputElement(WebElement s, String str){
        Random rnd = new Random();
        if(s.getTagName().equals("input")){
            String tagType = s.getAttribute("type");
            if(tagType.equals("text")){
                if(str.matches(".*(?iu)(логин).*") || str.matches(".*(?iu)(пароль).*")
                        || str.matches(".*(?iu)(ящик).*")) return "Ввести: " + generateStringRandom(7);
                if(str.matches(".*(?iu)(имя).*")) return "Ввести: " + getName();
                if(str.matches(".*(?iu)(фамили).*")) return "Ввести: " + getSecondName();
                if(str.matches(".*(?iu)(телефон).*") || str.matches(".*(?iu)(мобильн).*"))  return "Ввести: " + getPhone();
                if(str.matches(".*(?iu)(город).*") || str.matches(".*(?iu)(прожива).*")) return "Ввести: " + getCity();
                if(str.matches(".*(?iu)(день).*")) return "Ввести: " + getDay();
                if(str.matches(".*(?iu)(год).*")) return "Ввести: " + getYear();
                return "Ввести: " + getText();
            }
        }
        else if(s.getTagName().equals("textarea")){
                return "Ввести: "+ getText();
        }
        return "";
    }

}
