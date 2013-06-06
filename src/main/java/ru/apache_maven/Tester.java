package ru.apache_maven;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static ru.apache_maven.BlocksList.*;
import static ru.apache_maven.PrintTests.*;

/**
 * Created with IntelliJ IDEA.
 * User: ko6a
 * Email: ko6a93@bk.ru
 */

public class Tester {
    private String baseUrl;
    private WebDriver driver;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("START!");
        long t1 = System.nanoTime();
        Tester tester = new Tester();
        tester.Init();
        tester.Start();
        System.out.println("Time Main: " + ((System.nanoTime() - t1)/(1e+9)));
    }


    public void Init() throws FileNotFoundException {
        driver = new FirefoxDriver();
        Scanner sc = new Scanner(new File("input.txt"));
        baseUrl = sc.nextLine();
        sc.close();
    }

    public void Start() throws FileNotFoundException {
        long t1 = System.nanoTime();
        driver.get(baseUrl);
        System.out.println("Time driver.get(baseUrl): " + ((System.nanoTime() - t1)/(1e+9)));

        t1 = System.nanoTime();
        List<WebElement> webElements = driver.findElements(By.xpath("/html/body"));
        System.out.println("Time driver.findElements: " + ((System.nanoTime() - t1)/(1e+9)));

        t1 = System.nanoTime();
        findAllLabels(webElements.get(0));
        System.out.println("Time findAllLabels: " + ((System.nanoTime() - t1)/(1e+9)));

        t1 = System.nanoTime();
        findAllElementsNotRecurs(webElements.get(0));
        // findAllElementsRecurs(webElements.get(0), 0);
        System.out.println("Time findAllElementsNotRecurs: " + ((System.nanoTime() - t1)/(1e+9)));

        t1 = System.nanoTime();
        printTests(baseUrl);
        System.out.println("Time printTests: " + ((System.nanoTime() - t1)/(1e+9)));

        t1 = System.nanoTime();
        driver.quit();
        System.out.println("Time driver.quit(): " + ((System.nanoTime() - t1)/(1e+9)));
    }

    public void findAllLabels(WebElement element){
        try{
            List<WebElement> webElements = driver.findElements(By.xpath("//label"));
            for(WebElement s : webElements){
                String textId = s.getAttribute("for");
                if(textId!=null){
                    //System.out.println("label: " + s.getText());
                    labels.put(textId, s.getText());
                }
            }
        }catch (Exception e){
            System.out.println("void findAllLabels(): " + e);
        }
    }

    public void findAllElementsNotRecurs(WebElement element){
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
        //System.out.println("Blocks.size(): " + blocks.size() + " ~ " + blocksLen);
    }

    public void findAllElementsRecurs(WebElement element, int parentId){
        String elemName = element.getTagName();
        if(elemName.equals("script") || element.getCssValue("visibility").equals("hidden") ||
                element.getCssValue("display").equals("none")){
            return;
        }
        if (elemName.equals("a")){
            blocks.get(parentId).links.add(element);
        }
        else if (elemName.equals("button")){
            blocks.get(parentId).buttons.add(element);
        }
        else if (elemName.equals("img")){
            blocks.get(parentId).images.add(element);
            return;
        }
        else if(elemName.equals("input")){
            blocks.get(parentId).inputs.add(element);
        }
        else if(elemName.equals("select")) {
            blocks.get(parentId).selects.add(element);
        }
        else if(elemName.equals("textarea")) {
            blocks.get(parentId).textAreas.add(element);
            return;
        }
        else if(elemName.equals("label")) {
            blocks.get(parentId).textAreas.add(element);
        }
        else if(elemName.equals("div")) {
            if (element.getText().length() < 50) blocks.get(parentId).divs.add(element);
        }
        else if(elemName.equals("span")) {
            if (element.getText().length() < 50) blocks.get(parentId).spans.add(element);
        }
        else if(elemName.equals("ymaps") || elemName.equals("map")) {
            blocks.get(parentId).maps.add(element);
            return;
        }
        else if(elemName.equals("embed") || elemName.equals("object")) {
            blocks.get(parentId).flashMovies.add(element);
            return;
        }
        else if(elemName.equals("form")){
            blocks.add(new Block());
            blocks.get(blocks.size()-1).parentId = parentId;
            parentId = blocks.size()-1;
        }
        List<WebElement> webElems = element.findElements(By.xpath("*"));
        for(WebElement x : webElems){
            findAllElementsRecurs(x, parentId);
        }
    }
}

