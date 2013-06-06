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
        try{
            driver = new FirefoxDriver();
            Scanner sc = new Scanner(new File("input.txt"));
            baseUrl = sc.nextLine();
            sc.close();
        }catch (Exception e){
            System.out.println("void Init(): " + e.getMessage());
        }
    }

    public void Start() throws FileNotFoundException {
        try{
            driver.get(baseUrl);
            WebElement body = driver.findElement(By.xpath("/html/body"));

            findAllLabels(body);
            findAllElements(body);

            printTests(baseUrl);
            driver.quit();
        }catch (Exception e){
            System.out.println("void Start(): " + e.getMessage());
        }
    }
}

