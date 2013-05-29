import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ko6a
 */
public class Tester {
    private String baseUrl;
    private WebDriver driver;
    private HashMap<String, String> labels;
    private List<Form> forms;
    private String[] names, secondNames, phones, cities;
    public class Form{
        public int idOfParent = 0;
        public List<WebElement> buttons = new ArrayList<WebElement>();
        public List<WebElement> imageButtons = new ArrayList<WebElement>();
        public List<WebElement> links = new ArrayList<WebElement>();
        public List<WebElement> images = new ArrayList<WebElement>();
        public List<WebElement> inputs = new ArrayList<WebElement>();
        public List<WebElement> selects = new ArrayList<WebElement>();
        public List<WebElement> textAreas = new ArrayList<WebElement>();
        public List<WebElement> labelsList = new ArrayList<WebElement>();
        public List<WebElement> divs = new ArrayList<WebElement>();
    }

    @Before
    public void Init(){
        driver = new FirefoxDriver();
        baseUrl = "http://www.mail.ru" ;//"https://passport.yandex.ru/passport?mode=simplereg&retpath=https%3A%2F%2Fmail.yandex.ru%2F%3Forigin%3Dhome_ru_nohint&origin=home_ru_nohint";
        labels = new HashMap<String, String>();
        forms = new ArrayList<Form>();
        names = new String[]{"Андрей", "Иван", "Михаил", "Александр"};
        secondNames = new String[]{"Иванов", "Прохоров", "Соколов", "Сидоров"};
        phones = new String[]{"+7 916 123 11 22", "+7 917 351 49 91", "+7 926 413 11 22", "+7 916 836 29 82"};
        cities = new String[]{"Душанбе", "Москва", "Киев", "Ташкент"};
    }

    public void findAllLabels(WebElement element){
        try{
            if(element.equals("script") ||
                    element.getCssValue("visibility").equals("hidden") ||
                    element.getCssValue("display").equals("none"))
                return;
            if (element.getTagName().equals("label") && element.getAttribute("for")!=null) {
                labels.put(element.getAttribute("for"), element.getText());
              //  System.out.println(element.getAttribute("for")+" "+element.getText());
            }
            else{
                List<WebElement> webElems = element.findElements(By.xpath("*"));
                for(WebElement x : webElems) {
                  //  System.out.println("{\n" + x.getTagName());
                    findAllLabels(x);
                  //  System.out.println("\n}");
                }
            }
        }catch (Exception e){
            System.out.println("void findAllLabels : " + e);
        }
    }

    void findAllElements(WebElement element, int idOfParent){
        String elemName = element.getTagName();
        if(elemName.equals("script") ||
                element.getCssValue("visibility").equals("hidden") ||
                element.getCssValue("display").equals("none"))
            return;
        if (elemName.equals("a")){
           forms.get(idOfParent).links.add(element);

        }
        else
        if (elemName.equals("button")){
            forms.get(idOfParent).buttons.add(element);


        }
        else
        if (elemName.equals("img")){
            forms.get(idOfParent).images.add(element);

        }
        else
        if(elemName.equals("input")){
            forms.get(idOfParent).inputs.add(element);
        }
        else
        if(elemName.equals("select")) {
            forms.get(idOfParent).selects.add(element);
        }
        else
        if(elemName.equals("textarea")) {
            forms.get(idOfParent).textAreas.add(element);


        }
        else
        if(elemName.equals("label")) {
            forms.get(idOfParent).labelsList.add(element);

        }
        else
        if(elemName.equals("div")) {
            if (element.getText().length() < 50) forms.get(idOfParent).divs.add(element);
        }
        else
        if(elemName.equals("form")){
            forms.add(new Form());
            forms.get(forms.size()-1).idOfParent = idOfParent;
            idOfParent = forms.size()-1;
        }
        List<WebElement> webElems = element.findElements(By.xpath("*"));
        for(WebElement x : webElems)
            findAllElements(x, idOfParent);
    }

    @Test
    public void Start() throws FileNotFoundException {
        driver.get(baseUrl);
        List<WebElement> webElements = driver.findElements(By.xpath("/html/body"));

        findAllLabels(webElements.get(0));

        forms.add(new Form());
        findAllElements(webElements.get(0), 0);

        printTest1();
        printTest2();
        printTest3();

    }


    void printTest1() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("test1.html"));
        pw.println("<HTML><HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Test 1</TITLE><HEAD>");
        pw.println("<BODY><H1 ALIGN=\"center\">Тест1 - Ссылки</H1>");
        pw.println("<H2 ALIGN=\"center\">Кликнуть по элементу:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Text<TH>Location</TR>");
        int col = 0;
        for(int i = 0; i < forms.size(); i++){
            for(WebElement s : forms.get(i).links){
                if(s.getText().length() > 0) {
                    col++;
                    pw.println("<TR><TD>" + col  + "<TD>" + s.getText() + "<TD>" + s.getLocation());
                }
            }
        }
        pw.println("</BODY></HTML>");
        pw.close();
    }

    void printTest2() throws FileNotFoundException {
        int col = 0;
        List<String> namesOfPrintedRadio = new ArrayList<String>();
        for(int i = 1; i < forms.size(); i++){
            String pass = generateStringRandom(10);
            WebElement bSubmit = null;
            PrintWriter pw = new PrintWriter(new File("test" + String.valueOf(i+2) + ".html"));
            pw.println("<HTML><HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Test 1</TITLE><HEAD>");
            pw.println("<BODY><H1 ALIGN=\"center\">Test" + String.valueOf(i+2) +"</H1>");
            //pw.println("<H2 ALIGN=\"center\">:</H2>");
            pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Type<TH>Text<TH>Действие<TH>Location</TR>");

            for(WebElement s : forms.get(i).selects){
                Random rnd = new Random();
                String[] values = s.getText().split("\n");
                int t = rnd.nextInt(values.length);
                pw.println("<TR><TD>" + (col++)
                        + "<TD>" + "select" + "<TD>"
                        + (s.getText().length() > 50 ?  s.getText().substring(0, 50)+"..." :  s.getText())
                        + "<TD>" + "Выбрать:"+ values[t]
                        + "<TD>" + s.getLocation());

            }

            for(WebElement s : forms.get(i).textAreas){
                String text = getLabelForElement(s, i);
                pw.println("<TR><TD>" + (col++)
                        + "<TD>" + "textarea" + "<TD>"
                        + (text.length() > 50 ? text.substring(0, 50)+"..." : text)
                        + "<TD>" + generateString(s, text)
                        + "<TD>" + s.getLocation());
            }

            for(WebElement s : forms.get(i).inputs){
                String text = labels.get(s.getAttribute("id"));
                if (s.getAttribute("type").equals("checkbox"))
                    pw.println("<TR><TD>" + (col++)
                            + "<TD>" + "checkbox" + "<TD>"
                            + (text.length() > 50 ? text.substring(0, 50)+"..." : text) + "<TD>" + (col % 2 == 1 ? "true" : "false")
                            + "<TD>" + s.getLocation());

                if (s.getAttribute("type").equals("radio")
                        && namesOfPrintedRadio.indexOf(s.getAttribute("name"))==-1){
                    Random rnd = new Random();
                    namesOfPrintedRadio.add(s.getAttribute("name"));
                    List<WebElement> radios = new ArrayList<WebElement>();
                    for(int j = 0; j < forms.get(i).inputs.size(); j++)
                        if (s.getAttribute("name").equals(forms.get(i).inputs.get(j).getAttribute("name")))
                            radios.add(forms.get(i).inputs.get(j));
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
                    // s.sendKeys(getLabelForElement(s, i));
                    String str = getLabelForElement(s, i);
                    pw.println("<TR><TD>" + (col++)
                            + "<TD>" + "text" + "<TD>" + str
                            + "<TD>" + generateString(s, str)
                            + "<TD>" + s.getLocation());
                }

                if(s.getAttribute("type").equals("password")){
                    pw.println("<TR><TD>" + (col++)
                            + "<TD>" + "password" + "<TD>" + getLabelForElement(s, i)
                            + "<TD>" +  "Ввести:" + pass
                            + "<TD>" + s.getLocation());
                }

                if (s.getAttribute("type").equals("submit")){
                    bSubmit = s;
                }

            }

            for(WebElement s : forms.get(i).buttons){
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
//        public List<WebElement> buttons = new ArrayList<WebElement>();
//        public List<WebElement> imageButtons = new ArrayList<WebElement>();
//        public List<WebElement> links = new ArrayList<WebElement>();
//        public List<WebElement> images = new ArrayList<WebElement>();
//        public List<WebElement> inputs = new ArrayList<WebElement>();
//        public List<WebElement> selects = new ArrayList<WebElement>();
//        public List<WebElement> textAreas = new ArrayList<WebElement>();
//        public List<WebElement> labelsList = new ArrayList<WebElement>();
//        public List<WebElement> divs = new ArrayList<WebElement>();
    }

    void printTest3() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("test0.html"));
        pw.println("<HTML><HEAD><META http-equiv=\"content-type\" CONTENT=\"text/html; charset=UTF-8\"/><TITLE>Test 1</TITLE><HEAD>");
        pw.println("<BODY><H1 ALIGN=\"center\">Test0 - Кнопки</H1>");
        pw.println("<H2 ALIGN=\"center\">Кликнуть по элементу:</H2>");
        pw.println("<TABLE BORDER=\"1\" ALIGN=\"center\" CELLPADDING=\"4\"><TR><TH>№<TH>Тип<TH>Text<TH>Location</TR>");
        int col = 0;
        for(int i = 0; i < forms.size(); i++){
            for(WebElement s : forms.get(i).buttons){
                if(s.getText().length() > 0) {
                    col++;
                    pw.println("<TR><TD>" + col+ "<TD>" + "button"  + "<TD>" + s.getText() + "<TD>" + s.getLocation());
                }
            }
            for(WebElement s : forms.get(i).images){
                if(s.getText().length() > 0 && s.getAttribute("onclick") != null) {
                    col++;
                    pw.println("<TR><TD>" + col + "<TD>" + "image"+ "<TD>" + s.getAttribute("src") + "<TD>" + s.getLocation());
                }
            }
        }
        pw.println("</BODY></HTML>");
        pw.close();
    }

    String getLabelForElement(WebElement s, int id){
        if (labels.get(s.getAttribute("id"))!=null)
            return labels.get(s.getAttribute("id"));

        String ans = "text";
        int r = 10000;
        int x = s.getLocation().getX();
        int y = s.getLocation().getY();
        for(id = 0; id < forms.size(); id++){
            for(WebElement ss : forms.get(id).labelsList){
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
            for(WebElement ss : forms.get(id).divs){
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

    String generateString(WebElement s, String str){
        Random rnd = new Random();
        if(s.getTagName().equals("input")){
            String tagType = s.getAttribute("type");
            if(tagType.equals("text")){
                if(str.matches(".*(?iu)(логин).*") || str.matches(".*(?iu)(пароль).*")
                        || str.matches(".*(?iu)(ящик).*")) return "Ввести: " + generateStringRandom(7);
                if(str.matches(".*(?iu)(имя).*")) return "Ввести: " + names[rnd.nextInt(names.length)];
                if(str.matches(".*(?iu)(фамили).*")) return "Ввести: " + secondNames[rnd.nextInt(secondNames.length)];
                if(str.matches(".*(?iu)(телефон).*") || str.matches(".*(?iu)(мобильн).*"))
                    return "Ввести: " + phones[rnd.nextInt(phones.length)];
                if(str.matches(".*(?iu)(город).*") || str.matches(".*(?iu)(прожива).*"))
                    return "Ввести: " + cities[rnd.nextInt(cities.length)];
                else if(str.matches(".*(?iu)(день).*"))
                    return "Ввести: " + Integer.toString(rnd.nextInt(30) + 1);
                else if(str.matches(".*(?iu)(год).*"))
                    return "Ввести: " + Integer.toString(rnd.nextInt(40) + 1980);
                String text[] = new String[]{"Погода в " + cities[rnd.nextInt(cities.length)],
                        secondNames[rnd.nextInt(secondNames.length)] + names[rnd.nextInt(names.length)]};
                return "Ввести: " + text[rnd.nextInt(10) % 2];
            }
        }
        else
        if(s.getTagName().equals("textarea")){
            return "Ввести: "+ secondNames[rnd.nextInt(secondNames.length)] + names[rnd.nextInt(names.length)]
                    + "проживает в городе" + cities[rnd.nextInt(cities.length)];
        }
        return "";
    }

    String generateStringRandom(int len){
        Random rnd = new Random();
        String str = "";
        char[] ch = new char[2];
        for(int i = 0; i < len; i++){
            int val = rnd.nextInt(10);
            ch[0] = (char)(rnd.nextInt(10) + 48);
            ch[1] = (char)(rnd.nextInt(26) + 97);
            str += ch[val % 2];
        }
        return str;
    }

    void console_output(){
        System.out.println("\nLabels:");
        for(String s: labels.values())
            System.out.print(" " + s);


        for(int i=0; i<forms.size(); i++){
            System.out.println("\nForms " + i + "idOfParent= " + forms.get(i).idOfParent);

            System.out.print("\nLinks:");
            for(WebElement s: forms.get(i).links)
                System.out.print(" " + s.getText());

            System.out.print("\nButtons:");
            for(WebElement s: forms.get(i).buttons)
                System.out.print(" = " + s.getText());

            System.out.print("\nInputs:");
            for(WebElement s: forms.get(i).inputs){
                System.out.print(" = " + s.getAttribute("type"));

            }

            System.out.print("\nImages:");
            for(WebElement s: forms.get(i).images)
                System.out.print(" = " + s.getText());

            System.out.print("\nImageButtons:");
            for(WebElement s: forms.get(i).imageButtons)
                System.out.print(" = " + s.getText());

            System.out.print("\nSelects:");
            for(WebElement s: forms.get(i).selects)
                System.out.print(" = " + s.getText());
            System.out.println();
        }
    }
}











//    public static void main(String args[ ]) throws Exception {
//        WebDriver driver = new FirefoxDriver();
//        HttpGet request = new HttpGet("http://ya.ru/");
//
//        HttpResponse response = client.execute(request);
//
//        System.out.println(getContentToString(response.getEntity().getContent()));
//    }
//
//    public static String getContentToString(InputStream inputStream) throws Exception {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//        StringBuilder builder = new StringBuilder("");
//        for (String line = null; (line = reader.readLine()) != null;) {
//            builder.append(line).append("\n");
//        }
//
//        return builder.toString();
//    }
//}
