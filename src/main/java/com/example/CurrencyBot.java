package com.example;

import com.example.model.CurrencyModel;
import com.example.model.User;
import com.example.service.Convertor;
import com.example.service.CurrencyData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyBot extends TelegramLongPollingBot {
    public static Convertor convertor;
    public static Map<Long, User> userMap = new HashMap<>();
    public void onUpdateReceived(Update update) {
        Long chatId = null;
        User user = null;
        String userMessage = null;
        if (update.hasMessage()) {

            chatId = update.getMessage().getChatId();
            userMessage = update.getMessage().getText();

            if (userMessage.equals("/start")) {
                user = new User(update.getMessage().getFrom().getFirstName());
                userMap.put(chatId, user);

            } else {
                user = userMap.get(chatId);
            }
            if (userMessage.equals("Back to HOME")) {
                user.setStep(0);
            }
            if (userMessage.equals("\uD83D\uDD19 Back")) {
                user.setStep(1);
            }

        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            user = userMap.get(chatId);
            userMessage = update.getCallbackQuery().getData();

        }
        if (chatId != null && user != null && userMessage != null) {

            SendMessage sendMessage = sendMsg(update, user);
            sendMessage.setChatId(chatId);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

    }


    public String getBotToken() {
        return "key";
    }

    public String getBotUsername() {
        return "username";
    }

    public SendMessage sendMsg(Update update, User user) {
        SendMessage sendMessage = new SendMessage();
        switch (user.getStep()) {
            case 0:
                sendMessage.setReplyMarkup(
                        createReplyKeyboard());
                sendMessage.setText("Hello " + update.getMessage().getChat().getFirstName()
                        + "\nWelcome Currencies Bot");
                user.setStep(1);
                break;
            case 1:
                String msgText = update.getMessage().getText();
                if (msgText.equals("\uD83D\uDCB1 Convert") ||
                        msgText.equals("\uD83D\uDD19 Back")) {
                    sendMessage.setReplyMarkup(createInlineKeyboard());
                    sendMessage.setText("Choose convert method");
                    user.setStep(2);
                }else if (msgText.equals("\uD83D\uDCCA Dashboard")){

                    sendMessage.setText("1.\uD83C\uDDFA\uD83C\uDDF8 USD = "+ CurrencyData.getCurrenciesData().get(0).getRate()+" UZS  ( "+CurrencyData.getCurrenciesData().get(0).getDiff()+")\n" +
                            "2.\uD83C\uDDEA\uD83C\uDDFA EUR = "+CurrencyData.getCurrenciesData().get(1).getRate()+" UZS  ( "+CurrencyData.getCurrenciesData().get(1).getDiff()+")\n" +
                            "3.\uD83C\uDDF7\uD83C\uDDFA RUB = "+CurrencyData.getCurrenciesData().get(2).getRate()+" UZS  ( "+CurrencyData.getCurrenciesData().get(2).getDiff()+")\n" +
                            "4.\uD83C\uDDF9\uD83C\uDDF7 TRY = "+CurrencyData.getCurrenciesData().get(65).getRate()+" UZS  ( "+CurrencyData.getCurrenciesData().get(65).getDiff()+")\n" +
                            "5.\uD83C\uDDEE\uD83C\uDDF3 INR = "+CurrencyData.getCurrenciesData().get(26).getRate()+" UZS  ( "+CurrencyData.getCurrenciesData().get(26).getDiff()+")\n");
                }
                break;

            case 2:
                if (update.getCallbackQuery().getData().equals("convertFromUzs") ) {
                    sendMessage.setText("UZS  ➡️currency \n" +
                            "Choose currency");
                    sendMessage.setReplyMarkup(
                            createWorldCurrencyKeyboard("uzsToUsd", "uzsToEur", "uzsToRub"));
                    user.setStep(20);

                } else if (update.getCallbackQuery().getData().equals("convertToUzs")) {
                    sendMessage.setText("currency  ➡️UZS  \n" +
                            "Choose currency");

                    sendMessage.setReplyMarkup(
                            createWorldCurrencyKeyboard("usdToUzs", "eurToUzs", "rubToUzs"));
                    user.setStep(10);
                }

                break;
            case 10:
                switch (update.getCallbackQuery().getData()) {
                    case "usdToUzs":

                        sendMessage.setText("Enter amount of money:\n" +
                                update.getCallbackQuery().getData().substring(0, 3).toUpperCase() + " ➡ " + "UZS");
                        user.setStep(11);
                        break;
                    case "eurToUzs":

                        sendMessage.setText("Enter amount of money:\n" +
                                update.getCallbackQuery().getData().substring(0, 3).toUpperCase() + " ➡ " + "UZS");
                        user.setStep(12);
                        break;
                    case "rubToUzs":

                        sendMessage.setText("Enter amount of money:\n" +
                                update.getCallbackQuery().getData().substring(0, 3).toUpperCase() + " ➡ " + "UZS");
                        user.setStep(13);
                        break;
                }
                break;

            case 11:

                double amount = Double.parseDouble(update.getMessage().getText());
                double result = convert(CurrencyData.getCurrenciesData(), "USD", amount, false);
                sendMessage.setText(String.format("%.0f\n",result));
                break;
            case 12:

                amount = Double.parseDouble(update.getMessage().getText());
                result = convert(CurrencyData.getCurrenciesData(), "EUR", amount, false);
                sendMessage.setText(String.format("%.0f\n",result));

            case 13:

                amount = Double.parseDouble(update.getMessage().getText());
                result = convert(CurrencyData.getCurrenciesData(), "RUB", amount, false);
                sendMessage.setText(String.format("%.0f\n",result));
                break;


            case 20:

                switch (update.getCallbackQuery().getData()) {
                    case "uzsToUsd":

                        sendMessage.setText("Enter amount of money:\n" +
                                "UZS"+ " ➡ " + update.getCallbackQuery().getData().substring(5).toUpperCase());
                        user.setStep(21);
                        break;
                    case "uzsToEur":

                        sendMessage.setText("Enter amount of money:\n" +
                                "UZS"+ " ➡ " + update.getCallbackQuery().getData().substring(5).toUpperCase());
                        user.setStep(22);
                        break;

                    case "uzsToRub":

                        sendMessage.setText("Enter amount of money:\n" +
                                "UZS"+ " ➡ " + update.getCallbackQuery().getData().substring(5).toUpperCase());
                        user.setStep(23);
                        break;
                }
                break;
            case 21:

                amount = Double.parseDouble(update.getMessage().getText());
                result = convert(CurrencyData.getCurrenciesData(), "USD", amount, true);

                sendMessage.setText(String.format("%f\n",result));
                break;
            case 22:

                amount = Double.parseDouble(update.getMessage().getText());
                result = convert(CurrencyData.getCurrenciesData(), "EUR", amount, true);

                sendMessage.setText(String.format("%f\n",result));
                break;
            case 23:

                amount = Double.parseDouble(update.getMessage().getText());
                result = convert(CurrencyData.getCurrenciesData(), "RUB", amount, true);

                sendMessage.setText(String.format("%f\n",result));
                if (update.hasCallbackQuery()){
                    user.setStep(10);
                }
                break;

        }
        return sendMessage;
    }

    public static ReplyKeyboardMarkup createReplyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();

        KeyboardRow row1 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton("\uD83D\uDCB1 Convert");
        row1.add(button1);
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton("\uD83D\uDCCA Dashboard");
        row2.add(button2);
        keyboardRows.add(row2);


        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button4 = new KeyboardButton("\uD83D\uDD19 Back");
        KeyboardButton button5 = new KeyboardButton("Back to HOME");
        row3.add(button4);
        row3.add(button5);
        keyboardRows.add(row3);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup createInlineKeyboard() {
//       Inline keyboard markup
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//       Keyboard row list
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
//        row 1
        List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
        //        row 2
        List<InlineKeyboardButton> row2 = new ArrayList<InlineKeyboardButton>();
//        Button
        InlineKeyboardButton iButton1 = new InlineKeyboardButton("Convert to UZS");
        iButton1.setCallbackData("convertToUzs");

        InlineKeyboardButton iButton2 = new InlineKeyboardButton("Convert from UZS");
        iButton2.setCallbackData("convertFromUzs");

        row1.add(iButton1);
        rowList.add(row1);

        row2.add(iButton2);
        rowList.add(row2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;


    }


    public static InlineKeyboardMarkup createWorldCurrencyKeyboard(String cur1, String cur2, String cur3) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//       Keyboard row list
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
//        row 1
        List<InlineKeyboardButton> row1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> row2 = new ArrayList<InlineKeyboardButton>();
//        Button
        InlineKeyboardButton iButton1 = new InlineKeyboardButton("USD");
        iButton1.setCallbackData(cur1);
        InlineKeyboardButton iButton2 = new InlineKeyboardButton("EUR");
        iButton2.setCallbackData(cur2);
        InlineKeyboardButton iButton3 = new InlineKeyboardButton("RUB");
        iButton3.setCallbackData(cur3);
        row1.add(iButton1);
        row1.add(iButton2);
        row1.add(iButton3);
        rowList.add(row1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public  double convert(ArrayList<CurrencyModel> currencies, String cur, double amount, boolean uzs) {
        for (CurrencyModel currency : currencies) {
            if (currency.getCcy().equals(cur)) {
                return calc(currency,cur,amount,uzs);
            };
        }
        return 0;
    }

    public double calc(CurrencyModel currency,String cur, double amount,boolean uzs) {
        double result;
        double rate = Double.parseDouble(currency.getRate());
        if (uzs){
            result  = amount/rate;

        }else {
            result = amount*rate;

        }
        return result;
    }

}
