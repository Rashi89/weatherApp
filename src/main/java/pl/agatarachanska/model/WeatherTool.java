package pl.agatarachanska.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherTool {
    private InputStream local;
    private Date date;
    private String city;
    private String latitude;
    private String longitude;

    private String temperature;
    private String pressure;

    private String tempToday;
    private String pressureToday;
    private String descriptionToday;
    private String todayDay;

    private String tomorrow;
    private String dayAfter;
    private String dayDayAfter;
    private String dayDayDayAfter;

    private String icon0;
    private String iconA;
    private String iconB;
    private String iconC;
    private String iconD;

    public String temp;
    public String sym;
    public String descript;
    public String press;
    public Date clock;

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String TIME_WEATHER = "06:00:00";
    private static final Integer NEXT_DAY = 8;

    public List<String> forecast = new ArrayList<>();
    public List<Forecast> forecasts = new ArrayList<>();

    public String language;

    public WeatherTool(String city){
        this.city=city;
        Locale currentLocale=Locale.getDefault();
        if(currentLocale.getDisplayLanguage().equals("polski")){
            this.language ="pl";
        } else this.language = "en";
    }

    public void fetchLocalApi(){
        try{
            local = new URL("http://ip-api.com/xml").openConnection().getInputStream();
            System.out.println("local1 "+local);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
