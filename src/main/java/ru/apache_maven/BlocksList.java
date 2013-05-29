package ru.apache_maven;

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

    public static String getLabelForElement(WebElement s, int id){
        if (labels.get(s.getAttribute("id"))!=null)
            return labels.get(s.getAttribute("id"));

        String ans = "text";
        int r = 10000;
        int x = s.getLocation().getX();
        int y = s.getLocation().getY();
        for(id = 0; id < blocks.size(); id++){
            for(WebElement ss : blocks.get(id).labelsList){
                if(!ss.getText().equals("") && ss.getText()!=null){
                    int xx = ss.getLocation().getX();
                    int yy = ss.getLocation().getY();
                    int d = Math.abs(xx-x) + Math.abs(yy-y);
                    if (d < r && yy <= y && xx <= x ){
                        r = d;
                        ans = ss.getText();
                    }
                }
            }
            for(WebElement ss : blocks.get(id).divs){
                if(!ss.getText().equals("") && ss.getText()!=null){
                    int xx = ss.getLocation().getX();
                    int yy = ss.getLocation().getY();
                    int d = Math.abs(xx-x) + Math.abs(yy-y);
                    if (d < r && yy <= y && xx <= x){
                        r = d;
                        ans = ss.getText();
                    }
                }
            }
        }
        return ans;
    }
}
