package com.tempconverter;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ConversionBuilder implements Converter {


    private String fromTemp;
    private String toTemp;
    private double convertFromValue;

    Map<String, Converter> tempConverters = new HashMap<>();

    //initialize the map with the possible values
    {
        tempConverters.put("CF", new CelsiusToFahrenheitConverter());
        tempConverters.put("FC", new FahrenheitToCelsiusConverter());
        tempConverters.put("CK", new CelsiusToKelvinConverter());
        tempConverters.put("KC", new KelvinToCelsiusConverter());
        tempConverters.put("FK", new FahrenheitToKelvinConverter());
        tempConverters.put("KF", new KelvinToFahrenheitConverter());
    }

    /*method named in such a way so users know it is a temp*/
    public ConversionBuilder convertFromTemp(String aFromTemp) {
        fromTemp = aFromTemp;

        return this;
    }

    /*method named in such a way so users know it is a temp to convert to*/
    public ConversionBuilder toTemp(String aToTemp){
        toTemp = aToTemp;
        return this;
    }

    @Override
    public double withValue(double aValue) throws Exception{
        convertFromValue = aValue;
        String key = fromTemp + toTemp;
            if (!tempConverters.containsKey(key))
                 throw new Exception("Unsupported Unit Conversion Combination");
        return tempConverters.get(fromTemp + toTemp).withValue(convertFromValue);
    }

    //Print out the value when it is run on the command line.
    public static void main(String[] args) {

        try {
            System.out.println(new ConversionBuilder().convertFromTemp("C").toTemp("F").withValue(100));
            System.out.println(new ConversionBuilder().convertFromTemp("K").toTemp("C").withValue(300));
            System.out.println(new ConversionBuilder().convertFromTemp("F").toTemp("C").withValue(32));
            System.out.println(new ConversionBuilder().convertFromTemp("C").toTemp("K").withValue(100));
            System.out.println(new ConversionBuilder().convertFromTemp("F").toTemp("K").withValue(100));
            System.out.println(new ConversionBuilder().convertFromTemp("K").toTemp("F").withValue(293));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    Below are the 6 converter classes to convert the different combinations
     */

    public class KelvinToCelsiusConverter implements Converter {

        @Override
        public double withValue(double convertFrom) throws Exception {
            double raw = convertFrom - 273.15 ;
            return  Double.valueOf(new DecimalFormat("#.##").format(raw));
        }
    }

    public class CelsiusToKelvinConverter implements Converter {

        @Override
        public double withValue(double convertFrom) throws Exception {
            double raw = convertFrom + 273.15;
            return  Double.valueOf(new DecimalFormat("#.##").format(raw));
        }
    }

    public class FahrenheitToKelvinConverter implements Converter {

        @Override
        public double withValue(double convertFrom) throws Exception{
            double raw =  (((convertFrom - 32) * 5) / 9) + 273.15;
            return Double.valueOf(new DecimalFormat("#.##").format(raw));
        }
    }

    public class KelvinToFahrenheitConverter implements Converter {

        @Override
        public double withValue(double convertFrom) throws Exception {
            double raw = (1.8 * (convertFrom - 273)) + 32;
            return Double.valueOf(new DecimalFormat("#.##").format(raw));
        }
    }

    public class CelsiusToFahrenheitConverter implements Converter {

        @Override
        public double withValue(double convertFrom) {
            double raw = (convertFrom * 1.8) + 32;
            return Double.valueOf(new DecimalFormat("#.##").format(raw));
        }
    }

    public class FahrenheitToCelsiusConverter implements Converter {

        @Override
        public double withValue(double convertFrom) {
            double raw = (convertFrom - 32) * 1.8 ;
            return Double.valueOf(new DecimalFormat("#.##").format(raw));
        }
    }


}
