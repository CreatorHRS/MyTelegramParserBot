package com.TelegramBot.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class App {

    public static void main(String[] args) {

        /*Redirect stream of errors*/
        try {
            System.setErr(new PrintStream("/home/mikhailkhr/MyProjects/Java projects/TelegramBot/log.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*Init a bot*/
        ApiContextInitializer.init();
        MyTelegramBot aa = new MyTelegramBot();

        TelegramBotsApi telegramBotApi = new TelegramBotsApi();
        try {
            telegramBotApi.registerBot(aa);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        ControlThread h = new ControlThread(aa);
        h.run();
    }
}

/**The bot class */
class MyTelegramBot extends TelegramLongPollingBot {
    SendMessage message = new SendMessage();
    SendPhoto photo = new SendPhoto();
    SendSticker sticker = new SendSticker();
    String testUserID;
    String botToken;
    String botName;

    public MyTelegramBot() {
        Properties info = new Properties();
        try {
            info.load(new FileInputStream("/home/mikhailkhr/MyProjects/Java projects/TelegramBot/info.cfg"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        botName = info.getProperty("botName");
        botToken = info.getProperty("botToken");
        testUserID = info.getProperty("userid");
        System.out.println(botName + botToken + testUserID);
    }

    public String getBotUsername() {
        return botName;
    }

    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            reply(update.getMessage());
        }
    }

    @Override
    public String getBotToken() {
      // TODO Auto-generated method stub
        return botToken;
    }

    private void reply(Message m) {
        System.out.println(m.getText());
        
        if(m.getText().equals("/start")){
            message.setChatId(m.getChatId());
            message.setText("Приветствую😀\n Вот список команд:\n/about - об боте");
            try{
                System.out.println("made");
                execute(message); // Call method to send the message
                System.out.println("sended");
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }else if(m.getText().equals("/about")){
            message.setChatId(m.getChatId());
            message.setText("Версия: 0.0.1 beta\nРазработчик: Mikhail Kharisov\n" + message.getChatId());
            try{
                execute(message); // Call method to send the message
            }catch(TelegramApiException e) {
                e.printStackTrace();
            }
        } 
    }
}