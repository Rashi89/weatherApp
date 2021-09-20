package pl.agatarachanska.model;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class WeatherMenager {
    private final String city;

    private String day;
    private Integer temperature;
    private String windSpeed;
    private String icon;
    private String description;
    private String cloudy;
    private String pressure;
    private String moisture;
    private ResourceBundle resourceBundle;
    private String language;

    private static final String KEY_API="a539a1d5b32e2518dfe9ca8abf12434c";

    public WeatherMenager(String city, ResourceBundle resourceBundle) {
        this.city = city;
        this.resourceBundle= resourceBundle;
        Locale currentLocale = Locale.getDefault();
        this.language=currentLocale.getLanguage();
    }

    private String read(Reader reader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        int quantity;
        while((quantity = reader.read())!=-1){
            stringBuffer.append((char) quantity);
        }
        return stringBuffer.toString();
    }

    public JSONObject readJsonFromURL(String url) throws IOException{
        try(InputStream inputStream = new URL(url).openStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = read(bufferedReader);
            return new JSONObject(jsonText);
        }
    }

    public void fetchDataWeather(){
        int date =0;
        String nameDay;

        JSONObject json;
        JSONObject jsonDetails;

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE",Locale.ENGLISH);
        Calendar calendar  = Calendar.getInstance();

        try{
            String town = city.toString();
            json = readJsonFromURL("http://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(town, StandardCharsets.UTF_8) + "&appid=" + KEY_API +"&lang="+language+"&units=metric");

        }catch (IOException | JSONException e) {
            return;
        }

        jsonDetails = json.getJSONObject("main");
        this.temperature = jsonDetails.getInt("temp");
        this.pressure = jsonDetails.get("pressure").toString();
        this.moisture = jsonDetails.get("humidity").toString();
        jsonDetails = json.getJSONObject("wind");
        this.windSpeed =jsonDetails.get("speed").toString();
        jsonDetails =json.getJSONObject("clouds");
        this.cloudy = jsonDetails.get("all").toString();

        calendar.add(Calendar.DATE,date);
        nameDay = dateFormat.format(calendar.getTime());
        this.day = changeDate(nameDay, resourceBundle);
        jsonDetails =json.getJSONArray("weather").getJSONObject(0);
        this.description = jsonDetails.get("description").toString();
        this.icon =jsonDetails.get("icon").toString();
    }
    public String changeDate(String day,ResourceBundle resourceBundle){
            if (day.equals("Monday")) {
                return resourceBundle.getString("poniedzialek");
            } else if (day.equals("Tuesday")) {
                return resourceBundle.getString("wtorek");
            } else if (day.equals("Wednesday")) {
                return resourceBundle.getString("sroda");
            } else if (day.equals("Thursday")) {
                return resourceBundle.getString("czwartek");
            } else if (day.equals("Friday")) {
                return resourceBundle.getString("piatek");
            } else if (day.equals("Saturday")) {
                return resourceBundle.getString("sobota");
            } else if (day.equals("Sunday")) {
                return resourceBundle.getString("niedziela");
            }
        return "";
    }
    public String getCity() {
        return city;
    }
    public String getDay(){
        return day;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public String getIcon(){
        return icon;
    }

    public String getDescription(){
        return description;
    }

    public String getWindSpeed(){
        return windSpeed;
    }

    public String getCloud(){
        return cloudy;
    }
    public String getPressure(){
        return pressure;
    }
    public String getMoisture(){
        return moisture;
    }
}
