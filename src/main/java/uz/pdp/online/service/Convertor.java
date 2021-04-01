package uz.pdp.online.service;

import uz.pdp.online.model.CurrencyModel;

import java.util.ArrayList;

public class Convertor {
    public  double convert(ArrayList<CurrencyModel> currencies, String cur,double amount, boolean uzs) {
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
        System.out.println("rate"+rate);
        System.out.println("Enter amount of money");

        if (uzs){
            result  = amount/rate;

        }else {
            result = amount*rate;

        }
        return result;
    }
}
