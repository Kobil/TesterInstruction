package ru.apache_maven;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.apache_maven.Data.*;
import static ru.apache_maven.BlocksList.*;


/**
 * Created with IntelliJ IDEA.
 * User: ko6a
 * Date: 29.05.13
 * Time: 14:33
 * Email: ko6a93@bk.ru
 */
public class PrintTests {
    private static String dirName = "Intructions-HTML";
    private static int countOfTests = 0;
    private static PrintWriter pwMain;
    private static String textForPrint;
    private static int count;

    private static void printTestForLinks() throws FileNotFoundException {
        int col = 0;
        String nameOfFile = "test_links.html";
        PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

        pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href="+nameOfFile+" target=\"B\">" + nameOfFile
                + "</a><TH>"+ "проверка правильности ссылок" + "</TR>");

        pw.println("<HTML>"
                +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                +"<HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Ссылки</TITLE></HEAD>");
        pw.println("<BODY>");
        pw.println("<H2 ALIGN=\"center\">Кликнуть по ссылке:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Text<TH>Location</TR>");

        for(WebElement s : blocks.get(0).links){
            if(s.getText().length() > 0) {
                col++;
                pw.println("<TR><TD>" + col  + "<TD>" + s.getText() + "<TD>" + s.getLocation());
            }
        }

        pw.println("</table><p align=\"center\"><a href=\"index.html\">Перейти на главную страницу</a>");
        pw.println("</BODY></HTML>");
        pw.close();
    }

    private static void printTestForForms() throws FileNotFoundException {
        Random rnd = new Random();
        List<String> namesOfPrintedRadio = new ArrayList<String>();
        int countOfForms = 0;
        String text;
        String pass;
        WebElement bSubmit;
        List<WebElement> radios = new ArrayList<WebElement>();
        for(int i = 1; i < blocks.size(); i++){
            count = 1;
            pass = generateStringRandom(10);
            textForPrint = "";
            bSubmit = null;

            textForPrint += ("<HTML>"
                    +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                    +"<HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/>"
                    +"<TITLE>Заполнение формы</TITLE></HEAD>");
            textForPrint += "\n" + ("<BODY><H1 ALIGN=\"center\">" + "Заполнение формы" +"</H1>");
            textForPrint += "\n" + ("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Type<TH>Text<TH>Действие<TH>Location</TR>");

            for(WebElement s : blocks.get(i).selects){
                text = s.getText();
                if(text != null){
                    String[] values = text.split("\n");
                    int t = rnd.nextInt(values.length);
                    addWebElementData("\n" + ("<TR><TD>" + count
                            + "<TD>" + "select" + "<TD>"
                            + (text.length() > 50 ?  text.substring(0, 50) + "..." : text)
                            + "<TD>" + "Выбрать: "+ values[t]
                            + "<TD>"), s);
                }
            }

            for(WebElement s : blocks.get(i).textAreas){
                text = getLabelForElement(s, i);
                if(text != null){
                    addWebElementData("\n" + ("<TR><TD>" + count
                        + "<TD>" + "textarea" + "<TD>"
                        + (text.length() > 50 ? text.substring(0, 50)+"..." : text)
                        + "<TD>" + generateStringForInputElement(s, text)
                        + "<TD>"), s);
                }
            }

            for(WebElement s : blocks.get(i).inputs){
                text = getLabelForElement(s, i);
                if (text!=null && s.getAttribute("type").equals("checkbox")){
                    addWebElementData("\n" + ("<TR><TD>" + count
                            + "<TD>" + "checkbox" + "<TD>"
                            + (text.length() > 50 ? text.substring(0, 50)+"..." : text) + "<TD>"
                            + (count % 2 == 1 ? "Выбрать" : "Не выбирать")
                            + "<TD>") , s);
                }

                if (s.getAttribute("type").equals("radio") && namesOfPrintedRadio.indexOf(s.getAttribute("name"))==-1){
                    namesOfPrintedRadio.add(s.getAttribute("name"));
                    radios.clear();
                    for(int j = 0; j < blocks.get(i).inputs.size(); j++){
                        if (s.getAttribute("name").equals(blocks.get(i).inputs.get(j).getAttribute("name"))){
                            radios.add(blocks.get(i).inputs.get(j));
                        }
                    }
                    int t = rnd.nextInt(radios.size());
                    for(int j = 0; j < radios.size(); j++){
                        text = getLabelForElement(radios.get(j), j);
                        if(text != null){
                            addWebElementData("\n" + ("<TR><TD>" + count
                                + "<TD>" + "radio" + "<TD>"
                                +(text.length() > 50 ? text.substring(0, 50) +"..." : text)
                                + "<TD>" + (t==j ? "Выбрать" : "-")
                                + "<TD>"), radios.get(j));
                        }
                    }
                }

                if(s.getAttribute("type").equals("text")){
                    String str = getLabelForElement(s, i);
                    addWebElementData("\n" + ("<TR><TD>" + count
                            + "<TD>" + "text" + "<TD>" + str
                            + "<TD>" + generateStringForInputElement(s, str)
                            + "<TD>"), s);
                }

                if(s.getAttribute("type").equals("password")){
                    addWebElementData("\n" + ("<TR><TD>" + count
                            + "<TD>" + "password" + "<TD>" + getLabelForElement(s, i)
                            + "<TD>" +  "Ввести: " + pass
                            + "<TD>"), s);
                }

                if (s.getAttribute("type").equals("submit")){
                    bSubmit = s;
                }

            }

            for(WebElement s : blocks.get(i).buttons){
                if (s.getAttribute("type").equals("submit")){
                    bSubmit = s;
                    break;
                }
            }

            if(bSubmit != null){
                addWebElementData("\n" + ("<TR><TD>" + count
                        + "<TD>" + "SubmitButton" + "<TD>"
                        + (bSubmit.getTagName().equals("button") ? bSubmit.getText()
                                                                 : getLabelForElement(bSubmit, i))
                        + "<TD>" +  "Кликнуть"
                        + "<TD>"), bSubmit);

                textForPrint += "\n" + ("</table><p align=\"center\"><a href=\"index.html\">Перейти на главную страницу</a>");
                textForPrint += "\n" + ("</BODY></HTML>");

                String nameOfFile = "test_forms-" + String.valueOf(++countOfForms) + ".html";
                PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

                pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href=" + nameOfFile + " target=\"B\">"
                        + nameOfFile + "</a><TH>" + "заполнение формы" + "</TR>");

                pw.println(textForPrint);
                pw.close();
            }


        }
    }

    private static void printTestForButtons() throws FileNotFoundException {
        String nameOfFile = "test_buttons.html";
        PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

        pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href="+nameOfFile+" target=\"B\">" + nameOfFile
                + "</a><TH>"+ "проверка работоспособности кнопок" + "</TR>");

        pw.println("<HTML><HEAD>"
                +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                +"<META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Кнопки</TITLE><HEAD>");
        pw.println("<BODY>");
        pw.println("<H2 ALIGN=\"center\">Кликнуть по кнопке:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Тип<TH>Text<TH>Location</TR>");

        int col = 0;
        String text;
        for(WebElement s : blocks.get(0).buttons){
            text = s.getText();
            if((text != null) && (text.length() > 0)) {
                col++;
                pw.println("<TR><TD>" + col+ "<TD>" + "button"  + "<TD>" + text + "<TD>" + s.getLocation());
            }
        }

        for(WebElement s : blocks.get(0).images){
            text = s.getText();
            if((text != null) && (text.length() > 0) && (s.getAttribute("onclick") != null)) {
                col++;
                pw.println("<TR><TD>" + col + "<TD>" + "image"+ "<TD>" + s.getAttribute("src") + "<TD>" + s.getLocation());
            }
        }

        for(WebElement s : blocks.get(0).inputs){
            if((s.getAttribute("value")!=null) && (s.getAttribute("type").equals("submit"))) {
                text = getLabelForElement(s, 0);
                col++;
                pw.println("<TR><TD>" + col + "<TD>" + "button"+ "<TD>"
                        + text
                        + "<TD>" + s.getLocation());
            }
        }

        pw.println("</table><p align=\"center\"><a href=\"index.html\">Перейти на главную страницу</a>");
        pw.println("</BODY></HTML>");
        pw.close();
    }

    private static void printTestForOtherTags() throws FileNotFoundException {
        String nameOfFile = "test_other_tags.html";
        PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

        pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href="+nameOfFile+" target=\"B\">"+nameOfFile+ "</a><TH>"
                + "проверка правильности функционирования элементов" + "</TR>");

        pw.println("<HTML>"
                +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                +"<HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Ссылки</TITLE></HEAD>");
        pw.println("<BODY>");
        pw.println("<H2 ALIGN=\"center\">Проверить правильность функционирования элементов:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Type<TH>Text<TH>Location</TR>");

        int col = 0;

        for(WebElement s : blocks.get(0).maps){
            col++;
            pw.println("<TR><TD>" + col + "<TD>" + getLabelForElement(s, 0)
                    + "<TD>"+ s.getTagName()
                    + "<TD>" + s.getLocation());
        }

        for(WebElement s : blocks.get(0).flashMovies){
            col++;
            pw.println("<TR><TD>" + col + "<TD>" + getLabelForElement(s, 0)
                    + "<TD>" + "flash movie"
                    + "<TD>" + s.getLocation());
        }

        pw.println("</table><p align=\"center\"><a href=\"index.html\">Перейти на главную страницу</a>");
        pw.println("</BODY></HTML>");
        pw.close();
    }

    public static void printTests(String baseUrl) throws FileNotFoundException {
        (new File(dirName)).mkdirs();

        pwMain = new PrintWriter(new File(dirName + "//" + "ind.html"));
        pwMain.println("<HTML><HEAD><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"+
                "<META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/>"+
                "<TITLE>Instruction</TITLE></HEAD>"+
                "<frameset cols=\"35%,65%\">"+
                "<frame src=\""+"index.html" +"\" name=\"A\">" +
                "<frame src=\"index.html\" name=\"B\">"+
                "</frameset>"+
                "</HTML>");
        pwMain.close();

        pwMain = new PrintWriter(new File(dirName + "//" + "style.css"));
        pwMain.println("\n" +
                 "body {"
            +    "background: #e4e4e4;"
            +    "padding: 0;"
            +    "text-align: justify;"
            +    "font: 15px Arial, Helvetica, sans-serif;"
            +    "color: #626262;"
            +    "}"
            +    "\n"
            +    "TABLE {"
            +    "box-shadow: 0 0 5px black; /* Параметры тени */"
            +    "box-shadow: 0 0 10px rgba(0,0,0,0.5); /* Параметры тени */"
            +    "-moz-box-shadow: 0 0 10px rgba(0,0,0,0.5); /* Для Firefox */"
            +    "-webkit-box-shadow: 0 0 10px rgba(0,0,0,0.5); /* Для Safari и Chrome */"
            +    "background: #f2f2f2;"
            +    "padding: 30;"
            +    "text-align: center;"
            +    "border-radius: 6px;"
            +    "}"
            +    "\n");
        pwMain.close();

        pwMain = new PrintWriter(new File(dirName + "//" + "index.html"));
        pwMain.println("<HTML><HEAD>"
                +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                +"<META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Инструкция</TITLE></HEAD>");
        pwMain.println("<BODY alink=*green><H2 ALIGN=\"center\">Тесты:</H2>");
        pwMain.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Имя файла<TH>Тип теста</TR>");

        printTestForLinks();
        printTestForForms();
        printTestForButtons();
        printTestForOtherTags();

        pwMain.println("</BODY></HTML>");
        pwMain.close();
    }

    private static void addWebElementData(String textToAdd, WebElement webElement){
        Point pointElement = webElement.getLocation();
        if(pointElement.getX() == 0 && pointElement.getY() == 0) return;
        count++;
        textForPrint += "\n" + textToAdd +  pointElement;
    }
}
