package com.TelegramBot.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ControlThread {
    MyTelegramBot bot;

    public ControlThread(final MyTelegramBot bot) {
        this.bot = bot;
    }

    public void run() {
        // final Properties pref = new Properties();

        // /*Get preference from file*/
        // try {
        // pref.load(new FileInputStream("/home/mikhailkhr/MyProjects/Java
        // projects/TelegramBot/pref.cfg"));
        // } catch (final FileNotFoundException e1) {
        // e1.printStackTrace();
        // } catch (final IOException e1){
        // e1.printStackTrace();
        // }
        SiteControl c = new ItnewsControl();
        c.init();
        MyArticle article = c.getMyArticle();
        article.printMyArticle();
    }
}

class MyArticle {

    Date date;
    String name;
    String url;
    int flag = 0;

    public void printMyArticle(){
        System.out.println(name);
        System.out.println(url);
        date.printDate();
    }
}

class Date{
    int month;
    int year;
    int day;
    int hours;
    int minuts;
    
    public Date(int year, int month, int day, int hours, int minuts){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minuts = minuts;
    }

    public void printDate(){
        System.out.println(year + "-" + month + "-" + day + " " + hours + ":" + minuts);
    }

    /*compare funktion*/
    public int compare(Date dateToCompare){
        
        if(year != dateToCompare.year){
            return year - dateToCompare.year;
        }
        if(month != dateToCompare.month){
            return month - dateToCompare.month;
        }
        if(day != dateToCompare.day){
            return day - dateToCompare.day;
        }
        if(hours != dateToCompare.hours){
            return hours - dateToCompare.hours;
        }
        return minuts - dateToCompare.minuts; 
    }
}

abstract class SiteControl{
    abstract public int init();

    abstract public MyArticle getMyArticle();

    abstract public void close();
}

class ItssfossControl extends SiteControl{
    final String URL = "https://itsfoss.com";
    WebClient webClient;
    HtmlPage page;
    int iterator;

    @Override
    public int init(){
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        
        /* web client settings */
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        iterator = 0;
        boolean endFlag;
        int atempts = 0;

        do{
            endFlag = true;
            try {
                page = webClient.getPage(URL + "/all-blog-posts/");
            } catch (final FailingHttpStatusCodeException e){
                e.printStackTrace();
            } catch (final MalformedURLException e){
                e.printStackTrace();
            } catch (final IOException e){
                e.printStackTrace();
                endFlag = false;
                atempts++;
                if(atempts == 4){
                    return 1;
                }
            }
        }while(!endFlag);
        return 0;
    }

    @Override
    public MyArticle getMyArticle(){
        MyArticle article = new MyArticle();
        if(page == null){
            System.out.println("page = null");
            return null;
        }
        final List<HtmlElement> elements = page.getByXPath("//header[@class='entry-header']");
        if(elements.size() == 0){
            return null;
        }
        HtmlElement he;


        he = elements.get(iterator).getFirstByXPath("h2/a");
        article.name = he.getTextContent();
        article.url = he.getAttribute("href");
        he = elements.get(iterator).getFirstByXPath("p/time");
        article.date =  getMyDate(he.getAttribute("datetime"));
        iterator++;
        return article;
    }

    @Override
    public void close(){
       
    }

    private Date getMyDate(String date){
        String dateDate = date.substring(0, 10);
        String[] datePaths = dateDate.split("-");
        String dateTime = date.substring(11, 16);
        String[] timePaths = dateTime.split(":");
        Date d = new Date(Integer.parseInt(datePaths[0]), Integer.parseInt(datePaths[1]), 
                            Integer.parseInt(datePaths[2]), Integer.parseInt(timePaths[0]), 
                            Integer.parseInt(timePaths[1]));
        return d;
    }

}

class ItnewsControl extends SiteControl{
    final String URL = "https://www.itnews.com";
    WebClient webClient;
    HtmlPage page;
    int iterator;

    @Override
    public int init() {
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        
        /* web client settings */
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        iterator = 0;
        boolean endFlag;
        int atempts = 0;

        do{
            endFlag = true;
            try {
                page = webClient.getPage(URL + "/news/");
            } catch (final FailingHttpStatusCodeException e){
                e.printStackTrace();
            } catch (final MalformedURLException e){
                e.printStackTrace();
            } catch (final IOException e){
                e.printStackTrace();
                endFlag = false;
                atempts++;
                if(atempts == 4){
                    return 1;
                }
            }
        }while(!endFlag);
        return 0;
    }

    @Override
    public MyArticle getMyArticle(){
        HtmlPage articlePage = null;
        String href = null;
        boolean endFlag = true;
        int atempts = 0;
        MyArticle article = new MyArticle();
        
        List<HtmlElement> elements = page.getByXPath("//div[@class='post-cont']/h3/a");
        article.name = elements.get(iterator).getTextContent();

        href = elements.get(iterator).getAttribute("href");
        article.url = URL + href;
        
        do{
            endFlag = true;
            try {
                articlePage = webClient.getPage(URL + href);
            } catch (FailingHttpStatusCodeException e){
                e.printStackTrace();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
                endFlag = false;
                System.err.println("Error while loading article " + URL + href);
                atempts++;
                if(atempts == 10){
                    return null;
                }
            }
        }while(!endFlag);

        HtmlElement headerElement = articlePage.getFirstByXPath("//span[@class='pub-date']");
        article.date = getMyDate(headerElement.getAttribute("content"));
        
        return article;
    }

    @Override
    public void close() {
        

    }

    private Date getMyDate(String date){
        String dateDate = date.substring(0, 10);
        String[] datePaths = dateDate.split("-");
        String dateTime = date.substring(11, 16);
        String[] timePaths = dateTime.split(":");
        Date d = new Date(Integer.parseInt(datePaths[0]), Integer.parseInt(datePaths[1]), 
                            Integer.parseInt(datePaths[2]), Integer.parseInt(timePaths[0]), 
                            Integer.parseInt(timePaths[1]));
        return d;
    }

}

