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
    private static String[] thirdNames = new String[]{"Иванович", "Игоревич", "Романович", "Сергеевич"};
    private static String[] secondNames = new String[]{"Иванов", "Прохоров", "Соколов", "Сидоров"};
    private static String[] phones = new String[]{"+7 916 123 11 22", "+7 917 351 49 91", "+7 926 413 11 22", "+7 916 836 29 82"};
    private static String[] cities = new String[]{"Душанбе", "Москва", "Киев", "Ташкент"};
    private static String[] countries = new String[]{"Таджикистан", "Россия", "Украина", "Узбекистан", "США", "Турция"};

    public static String getName() {
        return names[rnd.nextInt(names.length)];
    }

    public static String getSecondName() {
        return secondNames[rnd.nextInt(secondNames.length)];
    }

    public static String getThirdName() {
        return thirdNames[rnd.nextInt(thirdNames.length)];
    }

    public static String getPhone() {
        return phones[rnd.nextInt(phones.length)];
    }

    public static String getCity() {
        return cities[rnd.nextInt(cities.length)];
    }

    public static String getCountry() {
        return countries[rnd.nextInt(countries.length)];
    }

    public static String getDay() {
        return Integer.toString(rnd.nextInt(27) + 1);
    }

    public static String getYesOrNo() {
        return (rnd.nextInt(10) % 2 == 0 ? "Да" : "Нет");
    }

    public static String getYear() {
        return Integer.toString(rnd.nextInt(40) + 1980);
    }

    public static String getMonth() {
        return Integer.toString(rnd.nextInt(11) + 1);
    }

    public static String getDate() {
        return getDay() + "." + getMonth() + "." + getYear();
    }

    public static String getNumber(int l, int r) {
        return Integer.toString(rnd.nextInt(r - l) + l);
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

    static String generateStringForInputElement(WebElement s, String str){
        Random rnd = new Random();
        if(s.getTagName().equals("input")){
            String tagType = s.getAttribute("type");
            if(tagType.equals("text")){
                if(str.matches(".*(?iu)(логин).*") || str.matches(".*(?iu)(пароль).*")
                        || str.matches(".*(?iu)(ящик).*")) return "Ввести: " + generateStringRandom(7);
                if(str.matches(".*(?iu)(имя).*")) return "Ввести: " + getName();
                if(str.matches(".*(?iu)(фамили).*")) return "Ввести: " + getSecondName();
                if(str.matches(".*(?iu)(отчеств).*")) return "Ввести: " + getThirdName();
                if(str.matches(".*(?iu)(телефон).*") || str.matches(".*(?iu)(мобильн).*"))  return "Ввести: " + getPhone();
                if(str.matches(".*(?iu)(город).*") || str.matches(".*(?iu)(прожива).*")) return "Ввести: " + getCity();
                if(str.matches(".*(?iu)(день).*")) return "Ввести: " + getDay();
                if(str.matches(".*(?iu)(год).*")) return "Ввести: " + getYear();
                if(str.matches(".*(?iu)(дата).*")) return "Ввести: " + getDate();
                if(str.matches(".*(?iu)(кредит).*") || str.matches(".*(?iu)(взнос).*") || str.matches(".*(?iu)(цена).*") ||
                   str.matches(".*(?iu)(стоимост).*") || str.matches("^[0-9\\s]+$")) return "Ввести : " + getNumber(1000, 300000);
                if(str.matches(".*(?iu)(гражданс).*") || str.matches(".*(?iu)(страна).*"))  return "Ввести : " + getCountry();
                if(str.matches(".*(?iu)(Наличие).*") || str.matches(".*(?iu)(наличие).*")){
                    return "Ввести: " + getYesOrNo();
                }
                return "Ввести: " + getText();
            }
        }
        else if(s.getTagName().equals("textarea")){
                return "Ввести: "+ getText();
        }
        return "";
    }

}
