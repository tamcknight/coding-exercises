package com.tempconverter;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TempConversionTests {


    @Test
    public void convertFromCelsiusToFahrenheit() {
        try {
            assertThat(new ConversionBuilder().convertFromTemp("C").toTemp("F").withValue(100)).isEqualTo(212);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    @Test
    public void convertFromFahrenheitToCelsius() {
        try {
            assertThat(new ConversionBuilder().convertFromTemp("F").toTemp("C").withValue(32)).isEqualTo(0);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    @Test
    public void convertFromKelvinToCelsius() {
        try {
            assertThat(new ConversionBuilder().convertFromTemp("K").toTemp("C").withValue(300)).isEqualTo(26.85);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    @Test
    public void convertFromCelsiusToKelvin() {
        try {
            assertThat(new ConversionBuilder().convertFromTemp("C").toTemp("K").withValue(100)).isEqualTo(373.15);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    @Test
    public void convertFromFahrenheitToKelvin() {
        try {
            assertThat(new ConversionBuilder().convertFromTemp("F").toTemp("K").withValue(100)).isEqualTo(310.93);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    @Test
    public void convertFromKelvinToFahrenheit() {
        try {
            assertThat(new ConversionBuilder().convertFromTemp("K").toTemp("F").withValue(293)).isEqualTo(68);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }
}
