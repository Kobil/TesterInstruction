package ru.apache_maven;

import org.junit.Before;
import org.junit.Test;
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
 */
public class Tester {
    private String baseUrl;
    private WebDriver driver;

    public static void main() throws FileNotFoundException {
        Tester test = new Tester();
        test.Init();
        test.Start();
    }

    @Before
    public void Init() throws FileNotFoundException {
        driver = new FirefoxDriver();
        Scanner sc = new Scanner(new File("input.txt"));
        baseUrl = sc.nextLine();
        sc.close();
    }

    @Test
    public void Start() throws FileNotFoundException {
        driver.get(baseUrl);
        List<WebElement> webElements = driver.findElements(By.xpath("/html/body"));

        findAllLabels(webElements.get(0));

        blocks.add(new Block());
        findAllElements(webElements.get(0), 0);

        printTests();

    }

    public void findAllLabels(WebElement element){
        try{
            if(element.equals("script") ||
                    element.getCssValue("visibility").equals("hidden") ||
                    element.getCssValue("display").equals("none"))
                return;
            if (element.getTagName().equals("label") && element.getAttribute("for")!=null) {
                labels.put(element.getAttribute("for"), element.getText());
            }
            else{
                List<WebElement> webElems = element.findElements(By.xpath("*"));
                for(WebElement x : webElems) {
                    findAllLabels(x);
                }
            }
        }catch (Exception e){
            System.out.println("void findAllLabels : " + e);
        }
    }

    public void findAllElements(WebElement element, int idOfParent){
        String elemName = element.getTagName();
        if(elemName.equals("script") ||
                element.getCssValue("visibility").equals("hidden") ||
                element.getCssValue("display").equals("none"))
            return;
        if (elemName.equals("a")){
            blocks.get(idOfParent).links.add(element);

        }
        else
        if (elemName.equals("button")){
            blocks.get(idOfParent).buttons.add(element);


        }
        else
        if (elemName.equals("img")){
            blocks.get(idOfParent).images.add(element);

        }
        else
        if(elemName.equals("input")){
            blocks.get(idOfParent).inputs.add(element);
        }
        else
        if(elemName.equals("select")) {
            blocks.get(idOfParent).selects.add(element);
        }
        else
        if(elemName.equals("textarea")) {
            blocks.get(idOfParent).textAreas.add(element);
        }
        else
        if(elemName.equals("label")) {
            blocks.get(idOfParent).labelsList.add(element);

        }
        else
        if(elemName.equals("div")) {
            if (element.getText().length() < 50) blocks.get(idOfParent).divs.add(element);
        }
        else
        if(elemName.equals("form")){
            blocks.add(new Block());
            blocks.get(blocks.size()-1).idOfParent = idOfParent;
            idOfParent = blocks.size()-1;
        }
        List<WebElement> webElems = element.findElements(By.xpath("*"));
        for(WebElement x : webElems)
            findAllElements(x, idOfParent);
    }
}

