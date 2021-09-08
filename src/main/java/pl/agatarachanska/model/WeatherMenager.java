package pl.agatarachanska.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeatherMenager {
    private final String city;

    private String day;
    private Integer temperatura;
    private String predkoscWiatru;
    private String icon;
    private String opis;
    private String zachmurzenie;
    private String cisnienie;
    private String wilgotnosc;
    private String language;

    private static final String KEY_API="a539a1d5b32e2518dfe9ca8abf12434c";

    public WeatherMenager(String city) {
        this.city = city;
        Locale currentLocale=Locale.getDefault();
        if(currentLocale.getDisplayLanguage().equals("polski")){
            this.language ="pl";
        } else this.language = "en";
    }

    private String read(Reader reader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        int ilosc;
        while((ilosc = reader.read())!=-1){
            stringBuffer.append((char) ilosc);
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
        this.temperatura = jsonDetails.getInt("temp");
        this.cisnienie = jsonDetails.get("pressure").toString();
        this.wilgotnosc = jsonDetails.get("humidity").toString();
        jsonDetails = json.getJSONObject("wind");
        this.predkoscWiatru =jsonDetails.get("speed").toString();
        jsonDetails =json.getJSONObject("clouds");
        this.zachmurzenie = jsonDetails.get("all").toString();

        calendar.add(Calendar.DATE,date);
        nameDay = dateFormat.format(calendar.getTime());
        Locale currentLocale=Locale.getDefault();

        if(currentLocale.getDisplayLanguage().equals("polski")) {
            this.day = changeDate(nameDay, currentLocale.getDisplayLanguage());
        }else
        {
            this.day = nameDay;
        }

        jsonDetails =json.getJSONArray("weather").getJSONObject(0);
        this.opis = jsonDetails.get("description").toString();
        this.icon =jsonDetails.get("icon").toString();
    }
    public String changeDate(String day,String jezyk){
        if(jezyk.equals("polski")) {
            if (day.equals("Monday")) {
                return "Poniedziałek";
            } else if (day.equals("Tuesday")) {
                return "Wtorek";
            } else if (day.equals("Wednesday")) {
                return "Środa";
            } else if (day.equals("Thursday")) {
                return "Czwartek";
            } else if (day.equals("Friday")) {
                return "Piątek";
            } else if (day.equals("Saturday")) {
                return "Sobota";
            } else if (day.equals("Sunday")) {
                return "Niedziela";
            }
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
        return temperatura;
    }

    public String getIcon(){
        return icon;
    }

    public String getDescription(){
        return opis;
    }

    public String getWindSpeed(){
        return predkoscWiatru;
    }

    public String getCloud(){
        return zachmurzenie;
    }
    public String getPressure(){
        return cisnienie;
    }
    public String getHumidity(){
        return wilgotnosc;
    }
}
