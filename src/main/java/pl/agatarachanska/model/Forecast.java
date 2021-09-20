package pl.agatarachanska.model;

import java.util.Date;

public class Forecast {
    public String temperature;
    public String symbol;
    public String description;
    public String pressure;
    public Date time;

    public Forecast(String temp, String symbols, String description, Date clock, String pressures){
        temperature=temp;
        symbol = symbols;
        this.description = description;
        time= clock;
        pressure = pressures;
    }
}
