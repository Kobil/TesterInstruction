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
}
