package pl.agatarachanska.model;

import java.util.Date;

public class Forecast {
    public String temperatura;
    public String symbol;
    public String description;
    public String pressure;
    public Date time;

    public Forecast(String temp, String symbols, String descript, Date clock, String pressures){
        temperatura=temp;
        symbol = symbols;
        description = descript;
        time= clock;
        pressure = pressures;
    }
}
