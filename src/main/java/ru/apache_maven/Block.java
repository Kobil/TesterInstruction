package ru.apache_maven;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ko6a
 * Date: 29.05.13
 * Time: 13:40
 * Email: ko6a93@bk.ru
 */
public class Block {
        public int parentId = 0;
        public List<WebElement> buttons = new ArrayList<WebElement>();
        public List<WebElement> links = new ArrayList<WebElement>();
        public List<WebElement> images = new ArrayList<WebElement>();
        public List<WebElement> inputs = new ArrayList<WebElement>();
        public List<WebElement> selects = new ArrayList<WebElement>();
        public List<WebElement> textAreas = new ArrayList<WebElement>();
        public List<WebElement> labelsList = new ArrayList<WebElement>();
        public List<WebElement> divs = new ArrayList<WebElement>();
        public List<WebElement> spans = new ArrayList<WebElement>();
}
