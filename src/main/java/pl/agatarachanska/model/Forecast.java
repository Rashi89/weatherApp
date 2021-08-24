package pl.agatarachanska.model;

import java.util.Date;

public class Forecast {
    public String temperatura;
    public String symbol;
    public String description;
    public String pressure;
    public Date time;

    public Forecast(String temp, String sym, String descript, Date clock, String press){
        temperatura=temp;
        symbol = sym;
        description = descript;
        time= clock;
        pressure = press;
    }
}
