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
import java.util.regex.Pattern;

public class WeatherTool {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String TIME_WEATHER = "06:00:00";
    private static final Integer NEXT_DAY = 8;
    private final String language;
    private final ResourceBundle resourceBundle;
    public String temp;
    public String sym;
    public String descript;
    public String press;
    public Date clock;
    public List<String> forecast = new ArrayList<>();
    public List<Forecast> forecasts = new ArrayList<>();
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
    private String tomorrowDescription;
    private String dayAfterDescription;
    private String dayDayAfterDescription;
    private String dayDayDayAfterDescription;
    private boolean connectionIsOpen;
    private boolean unexpectError = false;

    public WeatherTool(String city, ResourceBundle resourceBundle) {
        this.city = city;
        this.resourceBundle = resourceBundle;
        Locale currentLocale = Locale.getDefault();
        this.language = currentLocale.getLanguage();
    }

    public void fetchLocalApi() {
        try {
            local = new URL("http://ip-api.com/xml").openConnection().getInputStream();
            this.connectionIsOpen = true;
        } catch (UnknownHostException | MalformedURLException e) {
            this.connectionIsOpen = false;
        } catch (IOException e) {
            this.unexpectError = true;
        }
    }

    public void weatherInYourRegion() {
        downloadDataFromApi();
        String api = "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude +
                "&units=metric&mode=xml&lang=" + language + "&appid=a539a1d5b32e2518dfe9ca8abf12434c";
        downloadDataWeatherFromAPI(api);
    }

    public void weatherInTheSelectedCity() {
        downloadDataFromApi();
        String api = "http://api.openweathermap.org/data/2.5/forecast?q=" + URLEncoder.encode(city, StandardCharsets.UTF_8) +
                "&units=metric&mode=xml&lang=" + language + "&appid=a539a1d5b32e2518dfe9ca8abf12434c";

        downloadDataWeatherFromAPI(api);
    }

    private void downloadDataFromApi() {
        XMLInputFactory inputFactory =
                XMLInputFactory.newInstance();
        fetchLocalApi();
        try {
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
            this.unexpectError = true;
        } catch (Exception e) {
            this.unexpectError = true;
        }
    }

    private void downloadDataWeatherFromAPI(String api) {
        forecast.clear();
        forecasts.clear();
        try {
            InputStream weatherApi = new URL(api).openConnection().getInputStream();
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(weatherApi);
            reader.next();
            readDataFromXML(reader);
        } catch (MalformedURLException e) {
            this.unexpectError = true;
        } catch (IOException e) {
            this.unexpectError = true;
        } catch (XMLStreamException e) {
            this.unexpectError = true;
        } catch (NullPointerException e){
            this.unexpectError = true;
        }
        setTheWeatherAndForecastFor4Days();
    }

    private void readDataFromXML(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            if (eventType == XMLStreamReader.START_ELEMENT) {
                String element = reader.getLocalName();
                if (element.equals("temperature")) {
                    temperature = reader.getAttributeValue(1);
                    forecast.add(temperature);
                }
                if (element.equals("name")) {
                    city = reader.getElementText();
                }
                if (element.equals("time")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                    String time = reader.getAttributeValue(1);
                    try {
                        date = simpleDateFormat.parse(time);
                    } catch (ParseException e) {
                        this.unexpectError = true;
                    }
                }
                if (element.equals("pressure")) {
                    pressure = reader.getAttributeValue(1);
                    forecast.add(pressure);
                }
                if (element.equals("symbol")) {
                    String symbol = reader.getAttributeValue(2);
                    String description = reader.getAttributeValue(1);
                    Forecast temp = new Forecast(temperature, symbol, description, date, pressure);
                    forecasts.add(temp);
                }
            }
            reader.next();
        }
    }

    private void setTheWeatherAndForecastFor4Days() {
        if (forecast.isEmpty()) {
            forecast.add("0");
            forecasts.add(new Forecast(temp, sym, descript, clock, press));
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
            List<Integer> array = new ArrayList();

            divisionOfDailyData(simpleDateFormat, array);

            int fetchDataFromArray = array.get(0);
            int fetchDataFromArray1 = array.get(1);
            int fetchDataFromArray2 = array.get(2);
            int fetchDataFromArray3 = array.get(3);

            List<Double> day = new ArrayList<>();
            List<Double> day1 = new ArrayList<>();
            List<Double> day2 = new ArrayList<>();
            List<Double> day3 = new ArrayList<>();

            assingnmentTheTemperature(fetchDataFromArray, fetchDataFromArray1, fetchDataFromArray2,
                    fetchDataFromArray3, day, day1, day2, day3);

            sortTemperatureInDay(day, day1, day2, day3);

            int first = day.get(day.size() - 1).intValue();
            int second = day1.get(day.size() - 1).intValue();
            int third = day2.get(day.size() - 1).intValue();
            int fourth = day3.get(day.size() - 1).intValue();

            String tempTomorrow = Integer.toString(first);
            String tempDay = Integer.toString(second);
            String tempDayDay = Integer.toString(third);
            String tempDayDayDay = Integer.toString(fourth);

            String descriptionTomorrow = setDescriptionInDay(forecasts, fetchDataFromArray);
            String descriptionDay = setDescriptionInDay(forecasts, fetchDataFromArray1);
            String descriptionDayDay = setDescriptionInDay(forecasts, fetchDataFromArray2);
            String descriptionDayDayDay = setDescriptionInDay(forecasts, fetchDataFromArray3);

            setForecastDescriptionFor4Days(fetchDataFromArray, fetchDataFromArray1, fetchDataFromArray2, fetchDataFromArray3,
                    tempTomorrow, tempDay, tempDayDay, tempDayDayDay, descriptionTomorrow, descriptionDay,
                    descriptionDayDay, descriptionDayDayDay);

            todayWeather(forecast, forecasts, fetchDataFromArray);

        }
    }

    private void divisionOfDailyData(SimpleDateFormat simpleDateFormat, List<Integer> array) {
        for (int i = 0; i < forecasts.size(); i++) {
            String now = simpleDateFormat.format(forecasts.get(i).time);
            if (now.equals(TIME_WEATHER)) {
                array.add(i);
            }
        }
    }

    private void assingnmentTheTemperature(int fetchDataFromArray, int fetchDataFromArray1, int fetchDataFromArray2, int fetchDataFromArray3, List<Double> day, List<Double> day1, List<Double> day2, List<Double> day3) {
        assignmentTheTemperatureToTheDay(fetchDataFromArray, fetchDataFromArray1, day);
        assignmentTheTemperatureToTheDay(fetchDataFromArray1, fetchDataFromArray2, day1);
        assignmentTheTemperatureToTheDay(fetchDataFromArray2, fetchDataFromArray3, day2);
        assignmentTheTemperatureToTheDay(fetchDataFromArray3, fetchDataFromArray3 + NEXT_DAY, day3);
    }

    private void assignmentTheTemperatureToTheDay(int fetchDataFromArray, int fetchDataFromArray1, List<Double> day) {
        for (int i = fetchDataFromArray; i < fetchDataFromArray1; i++) {
            Double temp = Double.parseDouble(forecasts.get(i).temperature);
            day.add(temp);
        }
    }

    private void sortTemperatureInDay(List<Double> day, List<Double> day1, List<Double> day2, List<Double> day3) {
        Collections.sort(day);
        Collections.sort(day1);
        Collections.sort(day2);
        Collections.sort(day3);
    }

    private String setDescriptionInDay(List<Forecast> forecasts, int fetchDataFromArray) {
        return forecasts.get(fetchDataFromArray).description;
    }

    private void setForecastDescriptionFor4Days(int fetchDataFromArray, int fetchDataFromArray1, int fetchDataFromArray2,
                                                int fetchDataFromArray3, String tempTomorrow, String tempDay,
                                                String tempDayDay, String tempDayDayDay, String descriptionTomorrow,
                                                String descriptionDay, String descriptionDayDay,
                                                String descriptionDayDayDay) {
        this.tomorrow = " " + dayName(forecasts.get(fetchDataFromArray).time.toString().substring(0, 4)) +
                "  " + tempTomorrow + "ºC  " + forecasts.get(fetchDataFromArray).pressure + " hPa";
        this.tomorrowDescription = " " + descriptionTomorrow;
        this.dayAfter = " " + dayName(forecasts.get(fetchDataFromArray1).time.toString().substring(0, 4)) +
                "  " + tempDay + "ºC   " + forecasts.get(fetchDataFromArray1).pressure + " hPa";
        this.dayAfterDescription = " " + descriptionDay;
        this.dayDayAfter = " " + dayName(forecasts.get(fetchDataFromArray2).time.toString().substring(0, 4)) +
                "  " + tempDayDay + "ºC  " + forecasts.get(fetchDataFromArray2).pressure + " hPa";
        this.dayDayAfterDescription = " " + descriptionDayDay;
        this.dayDayDayAfter = " " + dayName(forecasts.get(fetchDataFromArray3).time.toString().substring(0, 4)) +
                "  " + tempDayDayDay + "ºC  " + forecasts.get(fetchDataFromArray3).pressure + " hPa";
        this.dayDayDayAfterDescription = " " + descriptionDayDayDay;
        this.iconA = forecasts.get(fetchDataFromArray).symbol;
        this.iconB = forecasts.get(fetchDataFromArray1).symbol;
        this.iconC = forecasts.get(fetchDataFromArray2).symbol;
        this.iconD = forecasts.get(fetchDataFromArray3).symbol;
    }

    private void todayWeather(List<String> forecast, List<Forecast> forecasts, int fetchDataFromArray) {
        String temperature =forecast.get(0);
        String[] parts = temperature.split(Pattern.quote("."));
        this.tempToday = parts[0];
        this.pressureToday = forecasts.get(fetchDataFromArray).pressure;
        this.todayDay = forecasts.get(0).time.toString().substring(0, 3);
        this.descriptionToday = forecasts.get(0).description;
        this.icon0 = forecasts.get(0).symbol;
    }

    private String dayName(String name) {
        switch (name) {
            case "Mon ":
                return resourceBundle.getString("poniedzialek").substring(0, 2) + ".";
            case "Tue ":
                return resourceBundle.getString("wtorek").substring(0, 2) + ".";
            case "Wed ":
                return resourceBundle.getString("sroda").substring(0, 2) + ".";
            case "Thu ":
                return resourceBundle.getString("czwartek").substring(0, 2) + ".";
            case "Fri ":
                return resourceBundle.getString("piatek").substring(0, 2) + ".";
            case "Sat ":
                return resourceBundle.getString("sobota").substring(0, 2) + ".";
            case "Sun ":
                return resourceBundle.getString("niedziela").substring(0, 2) + ".";
            default:
                return "";
        }
    }

    public String getCity() {
        return city;
    }

    public String getIcon0() {
        return icon0;
    }

    public String getIconA() {
        return iconA;
    }

    public String getIconB() {
        return iconB;
    }

    public String getIconC() {
        return iconC;
    }

    public String getIconD() {
        return iconD;
    }

    public String getDescriptionToday() {
        return descriptionToday;
    }

    public String getTempToday() {
        return tempToday;
    }

    public String getPressureToday() {
        return pressureToday;
    }

    public String getTomorrow() {
        return tomorrow;
    }

    public String getDayAfter() {
        return dayAfter;
    }

    public String getDayDayAfter() {
        return dayDayAfter;
    }

    public String getDayDayDayAfter() {
        return dayDayDayAfter;
    }

    public String getTomorrowDescription() {
        return tomorrowDescription;
    }

    public String getDayAfterDescription() {
        return dayAfterDescription;
    }

    public String getDayDayAfterDescription() {
        return dayDayAfterDescription;
    }

    public String getDayDayDayAfterDescription() {
        return dayDayDayAfterDescription;
    }

    public boolean getConnectionIsOpen() {
        return connectionIsOpen;
    }
    public  boolean getUnexpectError(){
        return unexpectError;
    }
}
