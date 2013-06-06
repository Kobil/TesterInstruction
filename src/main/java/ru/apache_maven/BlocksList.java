package ru.apache_maven;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ko6a
 * Date: 29.05.13
 * Time: 14:44
 * Email: ko6a93@bk.ru
 */

public class BlocksList {
    public static List<Block> blocks = new ArrayList<Block>();
    public static HashMap<String, String> labels = new HashMap<String, String>();
    private static int r, x, y;

    public static String getLabelForElement(WebElement s, int id){
        String ans, text;
        Point sLocation;
        List<WebElement> webElements;

        if (labels.get(s.getAttribute("id"))!=null && !labels.get(s.getAttribute("id")).equals("")){
            return labels.get(s.getAttribute("id"));
        }

        sLocation = s.getLocation();
        ans = "-";
        r = 10000;
        x = sLocation.getX();
        y = sLocation.getY();

        webElements = s.findElements(By.xpath(".//preceding-sibling::* | .//ancestor::label | //h1 | //h2 | //h3"));

        ans = findTextInList(webElements, ans);

        if(ans.equals("-") || s.getTagName().equals("input")){
            text = s.getAttribute("value");
            if (text!=null && !text.equals("") && !text.matches("^[0-9\\s]+$")){
                return text;
            }
            else{
                return ans;
            }
        }
        return ans;
    }

    public static String findTextInList(List<WebElement> webElements, String ans){
        Point sLocation;
        int xx, yy, d;
        String text;
        for(WebElement ss : webElements){
            text = ss.getText();
            if(text!=null && !text.equals("")){
                sLocation = ss.getLocation();
                xx = sLocation.getX();
                yy = sLocation.getY();
                d =  Math.abs(yy-y) + Math.abs(xx-x);
                if (d < r && yy <= y){
                    r = d;
                    ans = text;
                }
            }
        }
        return ans;
    }

    public static void findAllLabels(WebElement element){
        try{
            List<WebElement> webElements = element.findElements(By.xpath("//label"));
            for(WebElement s : webElements){
                String textId = s.getAttribute("for");
                if(textId!=null){
                    labels.put(textId, s.getText());
                }
            }
        }catch (Exception e){
            System.out.println("void findAllLabels(): " + e.getMessage());
        }
    }

    public static void findAllElements(WebElement element){
        try{
            Block block = new Block();

            block.inputs = element.findElements(By.xpath("//input"));
            block.links = element.findElements(By.xpath("//a"));
            block.buttons = element.findElements(By.xpath("//button"));
            block.images = element.findElements(By.xpath("//img"));
            block.selects = element.findElements(By.xpath("//select"));
            block.textAreas = element.findElements(By.xpath("//textarea"));
            block.maps = element.findElements(By.xpath("//map | //ymaps"));
            block.flashMovies = element.findElements(By.xpath("//embed | //object"));
            block.divs = element.findElements(By.xpath("//div"));
            block.spans = element.findElements(By.xpath("//span"));
            blocks.add(block);

            List<WebElement> forms = element.findElements(By.xpath("//form"));
            for(WebElement webElement : forms){
                block = new Block();
                block.inputs = webElement.findElements(By.xpath(".//input"));
                block.links = webElement.findElements(By.xpath(".//a"));
                block.buttons = webElement.findElements(By.xpath(".//button"));
                block.images = webElement.findElements(By.xpath(".//img"));
                block.selects = webElement.findElements(By.xpath(".//select"));
                block.textAreas = webElement.findElements(By.xpath(".//textarea"));
                block.maps = webElement.findElements(By.xpath(".//map | .//ymaps"));
                block.flashMovies = webElement.findElements(By.xpath(".//embed | .//object"));
                block.divs = webElement.findElements(By.xpath(".//div"));
                block.spans = webElement.findElements(By.xpath(".//span"));
                blocks.add(block);
            }
        }catch (Exception e){
            System.out.println("void findAllLabels(): " + e.getMessage());
        }
    }
}
