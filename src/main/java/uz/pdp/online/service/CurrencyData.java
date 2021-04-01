package uz.pdp.online.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.pdp.online.model.CurrencyModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyData {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static ArrayList<CurrencyModel> currencies;

    public static ArrayList<CurrencyModel> getCurrenciesData() {
        //        Get currency data
        try {
            URL url = new URL("http://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            URLConnection connection = url.openConnection();
            connection.connect();
//         Read data
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Type type = new TypeToken<ArrayList<CurrencyModel>>() {
            }.getType();
            currencies = gson.fromJson(reader, type);


        } catch (
                IOException e) {
            e.printStackTrace();
        }

//        Telegram bot initializer
        return currencies;
    }
}
