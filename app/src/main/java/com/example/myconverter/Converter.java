package com.example.myconverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Converter {
    private static final Map<String, Double> dict;
    static {
        dict = new HashMap<String, Double>();
        dict.put("Meters", 1.0);
        dict.put("Grams", 1.0);
        dict.put("Rubles", 1.0);
        dict.put("Kilometers", 1000.0);
        dict.put("Kilograms", 1000.0);
        dict.put("Miles", 1609.64);
        dict.put("Pounds", 453.592);
        dict.put("Dollars", 2.62);
        dict.put("Euro", 3.09);
    }

    public static String convert(String inputSpinner, String outputSpinner, String inputEdit)
    {
        double inputCoefficient = dict.get(inputSpinner);
        double outputCoefficient  = dict.get(outputSpinner);
        if(Objects.requireNonNull(inputEdit).length() > 0)
        {
            return String.valueOf(String.format("%.2f", (inputCoefficient / outputCoefficient) * Double.parseDouble(Objects.requireNonNull(inputEdit)))).replace(",", ".");
        }
        else
        {
            return "ERROR";
        }
    }
}
