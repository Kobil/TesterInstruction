package ru.apache_maven;

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

    private static void printTestForLinks() throws FileNotFoundException {
        String nameOfFile = "test_links.html";
        PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

        pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href="+nameOfFile+">"+nameOfFile+ "</a><TH>"+ "проверка правильности ссылок" + "</TR>");

        pw.println("<HTML>"
                +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                +"<HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Ссылки</TITLE></HEAD>");
        pw.println("<BODY><H1 ALIGN=\"center\">Тест1 - Ссылки</H1>");
        pw.println("<H2 ALIGN=\"center\">Кликнуть по ссылке:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Text<TH>Location</TR>");

        int col = 0;
        for(int i = 0; i < blocks.size(); i++){
            for(WebElement s : blocks.get(i).links){
                if(s.getText().length() > 0) {
                    col++;
                    pw.println("<TR><TD>" + col  + "<TD>" + s.getText() + "<TD>" + s.getLocation());
                }
            }
        }
        pw.println("</BODY></HTML>");
        pw.close();
    }

    private static void printTestForForms() throws FileNotFoundException {
        int col = 0;
        List<String> namesOfPrintedRadio = new ArrayList<String>();
        for(int i = 1; i < blocks.size(); i++){
            String pass = generateStringRandom(10);
            String nameOfFile = "test_forms-" + String.valueOf(i+2) + ".html";
            WebElement bSubmit = null;
            PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

            pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href="+nameOfFile+">" + nameOfFile + "</a><TH>" + "заполнение формы" + "</TR>");

            pw.println("<HTML>"
                    +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                    +"<HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Заполнение формы</TITLE></HEAD>");
            pw.println("<BODY><H1 ALIGN=\"center\">" + "Заполнение формы" +"</H1>");
            //pw.println("<H2 ALIGN=\"center\">:</H2>");
            pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Type<TH>Text<TH>Действие<TH>Location</TR>");

            for(WebElement s : blocks.get(i).selects){
                Random rnd = new Random();
                String[] values = s.getText().split("\n");
                int t = rnd.nextInt(values.length);
                pw.println("<TR><TD>" + (col++)
                        + "<TD>" + "select" + "<TD>"
                        + (s.getText().length() > 50 ?  s.getText().substring(0, 50)+"..." :  s.getText())
                        + "<TD>" + "Выбрать: "+ values[t]
                        + "<TD>" + s.getLocation());

            }

            for(WebElement s : blocks.get(i).textAreas){
                String text = getLabelForElement(s, i);
                pw.println("<TR><TD>" + (col++)
                        + "<TD>" + "textarea" + "<TD>"
                        + (text.length() > 50 ? text.substring(0, 50)+"..." : text)
                        + "<TD>" + generateStringToInputElement(s, text)
                        + "<TD>" + s.getLocation());
            }

            for(WebElement s : blocks.get(i).inputs){
                String text = labels.get(s.getAttribute("id"));
                if (s.getAttribute("type").equals("checkbox"))
                    pw.println("<TR><TD>" + (col++)
                            + "<TD>" + "checkbox" + "<TD>"
                            + (text.length() > 50 ? text.substring(0, 50)+"..." : text) + "<TD>"
                            + (col % 2 == 1 ? "выбрать" : "не выбирать")
                            + "<TD>" + s.getLocation());

                if (s.getAttribute("type").equals("radio")
                        && namesOfPrintedRadio.indexOf(s.getAttribute("name"))==-1){
                    Random rnd = new Random();
                    namesOfPrintedRadio.add(s.getAttribute("name"));
                    List<WebElement> radios = new ArrayList<WebElement>();
                    for(int j = 0; j < blocks.get(i).inputs.size(); j++)
                        if (s.getAttribute("name").equals(blocks.get(i).inputs.get(j).getAttribute("name")))
                            radios.add(blocks.get(i).inputs.get(j));
                    int t = rnd.nextInt(radios.size());
                    for(int j = 0; j < radios.size(); j++){
                        text = radios.get(j).getAttribute("value");
                        pw.println("<TR><TD>" + (col++)
                                + "<TD>" + "radio" + "<TD>" + labels.get(radios.get(j).getAttribute("id"))+" = "
                                + (text.length() > 50 ? text.substring(0, 50) +"..." : text) + "<TD>" + (t==j ? "Выбрать" : "-")
                                + "<TD>" + radios.get(j).getLocation());
                    }
                }

                if(s.getAttribute("type").equals("text")){
                    String str = getLabelForElement(s, i);
                    pw.println("<TR><TD>" + (col++)
                            + "<TD>" + "text" + "<TD>" + str
                            + "<TD>" + generateStringToInputElement(s, str)
                            + "<TD>" + s.getLocation());
                }

                if(s.getAttribute("type").equals("password")){
                    pw.println("<TR><TD>" + (col++)
                            + "<TD>" + "password" + "<TD>" + getLabelForElement(s, i)
                            + "<TD>" +  "Ввести: " + pass
                            + "<TD>" + s.getLocation());
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
                String text = bSubmit.getAttribute("value");
                pw.println("<TR><TD>" + (col++)
                        + "<TD>" + "SubmitButton" + "<TD>" + (text!=null ? text : bSubmit.getText())
                        + "<TD>" +  "Кликнуть"
                        + "<TD>" + bSubmit.getLocation());
            }
            pw.println("</BODY></HTML>");
            pw.close();
        }
    }

    private static void printTestForButtons() throws FileNotFoundException {
        String nameOfFile = "test_buttons.html";
        PrintWriter pw = new PrintWriter(new File(dirName + "//" + nameOfFile));

        pwMain.println("<TR><TH>"+ (++countOfTests) +"<TH><a href="+nameOfFile+">"+nameOfFile+ "</a><TH>"+ "проверка работоспособности кнопок" + "</TR>");

        pw.println("<HTML><HEAD>"
                +"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"
                +"<META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Кнопки</TITLE><HEAD>");
        pw.println("<BODY><H1 ALIGN=\"center\">Test0 - Кнопки</H1>");
        pw.println("<H2 ALIGN=\"center\">Кликнуть по кнопке:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Тип<TH>Text<TH>Location</TR>");
        int col = 0;
        for(int i = 0; i < blocks.size(); i++){
            for(WebElement s : blocks.get(i).buttons){
                if(s.getText().length() > 0) {
                    col++;
                    pw.println("<TR><TD>" + col+ "<TD>" + "button"  + "<TD>" + s.getText() + "<TD>" + s.getLocation());
                }
            }
            for(WebElement s : blocks.get(i).images){
                if(s.getText().length() > 0 && s.getAttribute("onclick") != null) {
                    col++;
                    pw.println("<TR><TD>" + col + "<TD>" + "image"+ "<TD>" + s.getAttribute("src") + "<TD>" + s.getLocation());
                }
            }

            for(WebElement s : blocks.get(i).inputs){
                String text = s.getAttribute("value");
                if(s.getAttribute("value")!=null && s.getAttribute("type").equals("submit")) {
                    col++;
                    pw.println("<TR><TD>" + col + "<TD>" + "button"+ "<TD>"
                            + (text != null ? text : getLabelForElement(s, i))
                            + "<TD>" + s.getLocation());
                }
            }
        }
        pw.println("</BODY></HTML>");
        pw.close();
    }

    public static void printTests() throws FileNotFoundException {
        (new File(dirName)).mkdirs();

        pwMain = new PrintWriter(new File(dirName + "//" + "ind.html"));
        pwMain.println("<HTML><HEAD><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"/>"+
                "<META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/>"+
                "<TITLE>Инструкция</TITLE></HEAD>"+
                "<frameset cols=\"50%,50%\">"+
                "<frame src=\""+"index.html" +"\" name=frame1>" +
                "<frame src=\"index.html\" name=freame2>"+
                "</frameset>"+
                "</HTML>");
        pwMain.close();


        pwMain = new PrintWriter(new File(dirName + "//" + "style.css"));
        pwMain.println("\n" +
                " H2 {font-size: 30pt; color: green;}" +
                " A {color: blue}" +
                " TABLE {background : lightgrey}"
                +"\n");
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

        pwMain.println("</BODY></HTML>");
        pwMain.close();
    }
}
