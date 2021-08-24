package pl.agatarachanska.model;

import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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

    private JSONObject readJsonFromURL(String url) throws IOException{
        try(InputStream inputStream = new URL(url).openStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = read(bufferedReader);
            return new JSONObject(jsonText);
        }
    }
}
