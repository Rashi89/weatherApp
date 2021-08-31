package pl.agatarachanska.model;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private String tomorrowDescription;
    private String dayAfterDescription;
    private String dayDayAfterDescription;
    private String dayDayDayAfterDescription;
    private boolean connectionIsOpen;

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
            this.connectionIsOpen=true;
        }catch(UnknownHostException | MalformedURLException e){
            this.connectionIsOpen=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadDataFromApi(){
        XMLInputFactory inputFactory =
                XMLInputFactory.newInstance();
        fetchLocalApi();
        try{
            XMLStreamReader reader =
                    inputFactory.createXMLStreamReader(local);
            reader.next();

            while (reader.hasNext()) {
                int eventType = reader.getEventType();
                if (eventType == XMLStreamReader.START_ELEMENT) {
                    String el = reader.getLocalName();

                    if (el.equals("city")) {
                        city = reader.getElementText();
                    }

                    if (el.equals("lat")) {
                        latitude = reader.getElementText();
                    }

                    if (el.equals("lon")) {
                        longitude = reader.getElementText();
                    }
                }
                reader.next();
            }
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String coordinateWeather(){
        downloadDataFromApi();
        return "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude +
                "&units=metric&mode=xml&lang="+language+"&appid=a539a1d5b32e2518dfe9ca8abf12434c";
    }

    public String cityWeather(){
        String town = city.toString();
        String api = "http://api.openweathermap.org/data/2.5/forecast?q=" + URLEncoder.encode(town, StandardCharsets.UTF_8) +
                "&units=metric&mode=xml&lang="+language+"&appid=a539a1d5b32e2518dfe9ca8abf12434c";
        downloadDataFromApi();
        return api;
    }

    public void downloadDataWeather(String api){
        forecast.clear();
        forecasts.clear();

        try{
            InputStream weatherApi = new URL(api).openConnection().getInputStream();
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            XMLStreamReader reader = inputFactory.createXMLStreamReader(weatherApi);

            reader.next();

            while(reader.hasNext()){
                int eventType = reader.getEventType();
                if(eventType == XMLStreamReader.START_ELEMENT){
                    String element = reader.getLocalName();

                    if(element.equals("temperature")){
                        temperature = reader.getAttributeValue(1);
                        forecast.add(temperature);
                    }
                    if(element.equals("name")){
                        city = reader.getElementText();
                    }
                    if(element.equals("time")){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                        String time = reader.getAttributeValue(1);
                        try{
                            date = simpleDateFormat.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if(element.equals("pressure")){
                        pressure = reader.getAttributeValue(1);
                        forecast.add(pressure);
                    }
                    if(element.equals("symbol")){
                        String symbol = reader.getAttributeValue(2);
                        String description = reader.getAttributeValue(1);
                        Forecast temp = new Forecast(temperature,symbol,description,date,pressure);
                        forecasts.add(temp);
                    }
                }
                reader.next();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        if(forecast.isEmpty()){
            forecast.add("0");
            forecasts.add(new Forecast(temp,sym,descript,clock,press));
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            List<Integer> array = new ArrayList();

            for(int i=0 ; i<forecasts.size();i++){
                String now = simpleDateFormat.format(forecasts.get(i).time);
                //na ktorej pozycji jest 6 rano
                if(now.equals(TIME_WEATHER)){
                    array.add(i);
                }
            }
            int fetchDataFromArray = array.get(0);//5
            int fetchDataFromArray1 = array.get(1);//13
            int fetchDataFromArray2 = array.get(2);
            int fetchDataFromArray3 = array.get(3);

            List<Double> day = new ArrayList<>();
            List<Double> day1 = new ArrayList<>();
            List<Double> day2 = new ArrayList<>();
            List<Double> day3 = new ArrayList<>();

            //przyporzadkowanie temperatur do nastepnych dni
            for(int i = fetchDataFromArray; i<fetchDataFromArray1; i++){
                Double temp = Double.parseDouble(forecasts.get(i).temperatura);
                day.add(temp);
            }

            for(int i = fetchDataFromArray1;i<fetchDataFromArray2;i++){
                Double temp = Double.parseDouble(forecasts.get(i).temperatura);
                day1.add(temp);
            }
            for(int i = fetchDataFromArray2;i<fetchDataFromArray3;i++){
                Double temp = Double.parseDouble(forecasts.get(i).temperatura);
                day2.add(temp);
            }
            for(int i = fetchDataFromArray3;i<fetchDataFromArray3+NEXT_DAY;i++){
                Double temp = Double.parseDouble(forecasts.get(i).temperatura);
                day3.add(temp);
            }

            //sortuje temperatury w kazdym dniu czyli od 6 do 6

            Collections.sort(day);
            Collections.sort(day1);
            Collections.sort(day2);
            Collections.sort(day3);

            //wybiera najwieksza w ciagu dnia
            int first = day.get(day.size()-1).intValue();
            int second =day1.get(day.size()-1).intValue();
            int third =day2.get(day.size()-1).intValue();
            int fourth = day3.get(day.size()-1).intValue();

            String tempTomorrow = Integer.toString(first);
            String tempDay = Integer.toString(second);
            String tempDayDay = Integer.toString(third);
            String tempDayDayDay = Integer.toString(fourth);

            this.tempToday = forecast.get(0).substring(0, 2);
            this.pressureToday = forecasts.get(fetchDataFromArray).pressure;
            this.todayDay = forecasts.get(0).time.toString().substring(0,3);
            this.descriptionToday = forecasts.get(0).description;

            Locale currentLocale=Locale.getDefault();
            String descriptionTomorrow= forecasts.get(fetchDataFromArray).description;
            String descriptionDay= forecasts.get(fetchDataFromArray1).description;
            String descriptionDayDay= forecasts.get(fetchDataFromArray2).description;
            String descriptionDayDayDay =forecasts.get(fetchDataFromArray3).description;

            this.tomorrow = " "+dayName(forecasts.get(fetchDataFromArray).time.toString().substring(0,4))+
                    "  "+tempTomorrow+"ºC  "+forecasts.get(fetchDataFromArray).pressure +" hPa";

            this.tomorrowDescription =" "+descriptionTomorrow;

            this.dayAfter = " "+dayName(forecasts.get(fetchDataFromArray1).time.toString().substring(0,4))+
                    "  "+tempDay+"ºC   "+forecasts.get(fetchDataFromArray1).pressure +" hPa";

            this.dayAfterDescription = " "+descriptionDay;

            this.dayDayAfter =" "+dayName(forecasts.get(fetchDataFromArray2).time.toString().substring(0,4))+
                    "  "+tempDayDay+"ºC  "+forecasts.get(fetchDataFromArray2).pressure +" hPa";

            this.dayDayAfterDescription =" "+descriptionDayDay;

            this.dayDayDayAfter =" "+dayName(forecasts.get(fetchDataFromArray3).time.toString().substring(0,4))+
                    "  "+tempDayDayDay+"ºC  "+forecasts.get(fetchDataFromArray3).pressure +" hPa";

            this.dayDayDayAfterDescription = " "+descriptionDayDayDay;

            this.icon0 = forecasts.get(0).symbol;
            this.iconA = forecasts.get(fetchDataFromArray).symbol;
            this.iconB = forecasts.get(fetchDataFromArray1).symbol;
            this.iconC = forecasts.get(fetchDataFromArray2).symbol;
            this.iconD = forecasts.get(fetchDataFromArray3).symbol;

        }
    }
    private String dayName(String name) {
        Locale currentLocale = Locale.getDefault();
        if (currentLocale.getDisplayLanguage().equals("polski")) {
            switch (name) {
                case "Mon ":
                    return "Pon.";
                case "Tue ":
                    return "Wt.";
                case "Wed ":
                    return "Śr.";
                case "Thu ":
                    return "Czw.";
                case "Fri ":
                    return "Pt.";
                case "Sat ":
                    return "Sob.";
                case "Sun ":
                    return "Niedz.";
                default:
                    return "";
            }

        } else return name;
    }
    public String getCity(){
        return city;
    }
    public String getIcon0() {
        return icon0;
    }

    public String getIconA() { return iconA; }

    public String getIconB() {
        return iconB;
    }

    public String getIconC() {
        return iconC;
    }

    public String getIconD() {
        return iconD;
    }
    public String getTodayDay(){
        return todayDay;
    }
    public String getDescriptionToday(){
        return descriptionToday;
    }
    public String getTempToday(){
        return tempToday;
    }
    public String getPressureToday(){
        return pressureToday;
    }
    public String getTomorrow(){
        return tomorrow;
    }
    public String getDayAfter(){
        return dayAfter;
    }
    public String getDayDayAfter(){
        return dayDayAfter;
    }
    public String getDayDayDayAfter(){
        return dayDayDayAfter;
    }
    public String getTomorrowDescription(){ return tomorrowDescription;}
    public String getDayAfterDescription() {return dayAfterDescription; }
    public String getDayDayAfterDescription() { return dayDayAfterDescription; }
    public String getDayDayDayAfterDescription() { return dayDayDayAfterDescription; }
    public boolean getConnectionIsOpen(){return connectionIsOpen;}
}
