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
        if (labels.get(s.getAttribute("id"))!=null && !labels.get(s.getAttribute("id")).equals(""))
            return labels.get(s.getAttribute("id"));
        String ans;

        Point sLocation = s.getLocation();
        ans = "-";
        r = 10000;
        x = sLocation.getX();
        y = sLocation.getY();

        List<WebElement> webElements = s.findElements(By.xpath(".//preceding-sibling::* " +
                "| .//ancestor::label | //h1 | //h2 | //h3"
        ));

        ans = findTextInList(webElements, ans);

        if(ans.equals("-") || s.getTagName().equals("input")){
            String text = s.getAttribute("value");
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
        for(WebElement ss : webElements){
            String text = ss.getText();
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
            System.out.println("void findAllLabels(): " + e);
        }
    }

    public static void findAllElements(WebElement element){
        try{
            blocks.add(new Block());
            blocks.get(0).inputs = element.findElements(By.xpath("//input"));
            blocks.get(0).links = element.findElements(By.xpath("//a"));
            blocks.get(0).buttons = element.findElements(By.xpath("//button"));
            blocks.get(0).images = element.findElements(By.xpath("//img"));
            blocks.get(0).selects = element.findElements(By.xpath("//select"));
            blocks.get(0).textAreas = element.findElements(By.xpath("//textarea"));
            blocks.get(0).maps = element.findElements(By.xpath("//map | //ymaps"));
            blocks.get(0).flashMovies = element.findElements(By.xpath("//embed | //object"));
            blocks.get(0).divs = element.findElements(By.xpath("//div"));
            blocks.get(0).spans = element.findElements(By.xpath("//span"));

            List<WebElement> forms = element.findElements(By.xpath("//form"));
            int blocksLen = 0;
            for(WebElement webElement : forms){
                blocks.add(new Block());
                blocksLen++;
                blocks.get(blocksLen).inputs = webElement.findElements(By.xpath(".//input"));
                blocks.get(blocksLen).links = webElement.findElements(By.xpath(".//a"));
                blocks.get(blocksLen).buttons = webElement.findElements(By.xpath(".//button"));
                blocks.get(blocksLen).images = webElement.findElements(By.xpath(".//img"));
                blocks.get(blocksLen).selects = webElement.findElements(By.xpath(".//select"));
                blocks.get(blocksLen).textAreas = webElement.findElements(By.xpath(".//textarea"));
                blocks.get(blocksLen).maps = webElement.findElements(By.xpath(".//map | .//ymaps"));
                blocks.get(blocksLen).flashMovies = webElement.findElements(By.xpath(".//embed | .//object"));
                blocks.get(blocksLen).divs = webElement.findElements(By.xpath(".//div"));
                blocks.get(blocksLen).spans = webElement.findElements(By.xpath(".//span"));
            }
        }catch (Exception e){
            System.out.println("void findAllLabels(): " + e);
        }
    }
}
