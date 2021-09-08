package pl.agatarachanska.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.JSONException;
import pl.agatarachanska.model.ImagesTool;
import pl.agatarachanska.model.WeatherMenager;
import pl.agatarachanska.model.WeatherTool;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Weather implements Initializable {

    private WeatherMenager weatherMenager;
    private WeatherTool weatherTool;
    public String citySet;
    private Date datas;

    @FXML
    private Label city,city1,day,temperature,temperature1,pressure,pressure1,tomorrow,tomorrow1,dayAfter,
            dayAfter1,dayDayAfter,dayDayAfter1,dayDayDayAfter,dayDayDayAfter1,desc,desc1,errors,tomorrowDescription,
            dayAfterDescription,dayDayAfterDescription,dayDayDayAfterDescription,tomorrowDescription1,
            dayAfterDescription1,dayDayAfterDescription1,dayDayDayAfterDescription1;

    @FXML
    private ImageView img,img1,img2,img3,img4,img5,img6,img7,img8,img9;

    @FXML
    private TextField cityName;

    @FXML
    private Button change,set,cancel;
    private String language;

    public Weather(){this.citySet="Cracow".toUpperCase();}

    @FXML
    void handleButtonClicks(javafx.event.ActionEvent event) {
        String initialCity = city.getText();

        if(event.getSource()==change){
            cityName.setText("");
            bottomSet(true);
            cityName.requestFocus();
        }else if(event.getSource()==set){
            setPressed();
        }else if(event.getSource()==cancel){
            cityName.setText(initialCity);
            bottomSet(false);
        }
    }
    private void bottomSet(boolean statement) {
        cityName.setDisable(!statement);
        set.setVisible(statement);
        change.setVisible(!statement);
        cancel.setVisible(statement);
    }
    private void setPressed(){
        System.out.println(cityName.getText());
        city.setStyle("-fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #53f5cf, #0987db)");
        if (cityName.getText().equals("")) {
            if(language.equals("polski")){
                showToast("Nazwa miasta nie może być pusta!");
            }else showToast("City Name cannot be blank");
        } else {
            try {
                city.setStyle("-fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #53f5cf, #0987db)");
                this.citySet = cityName.getText().trim();
                cityName.setText((citySet.trim()).toUpperCase());
                weatherTool = new WeatherTool(citySet);
                weatherMenager = new WeatherMenager(citySet);
                weatherTool.fetchLocalApi();
                if(!weatherTool.getConnectionIsOpen()){
                    city.setText("Error!");
                    city.setStyle("-fx-text-fill:red;");
                    if(language.equals("polski")){
                        showToast("Brak Internetu!");
                    }else { showToast("Internet Down. Please Connect "); }
                    reset();
                }else {
                    showWeather();
                    showForecast();
                    bottomSet(false);
                }
            } catch (Exception e) {
                city.setText("Error!");
                city.setStyle("-fx-text-fill:red;");
                if(language.equals("polski")){
                    showToast("Nie znaleziono prognozy dla wybranego miasta");
                }else {
                    showToast("City with the given name was not found.");
                }
                reset();
            }
        }
    }
    public void showWeather() throws JSONException {
        weatherMenager.fetchDataWeather();
        city.setText(weatherMenager.getCity().toUpperCase());
        temperature.setText(weatherMenager.getTemperature().toString() + "°C");
//        day.setText(weatherMenager.getDay().toUpperCase());
        desc.setText(weatherMenager.getDescription().toUpperCase());
        img.setImage(new Image(ImagesTool.getImage(weatherMenager.getIcon())));
        pressure.setText(weatherMenager.getPressure() + " hPa");

    }
    private void showForecast() {

        String weatherApi = weatherTool.cityWeather();
        weatherTool.downloadDataWeather(weatherApi);
        tomorrow.setText(weatherTool.getTomorrow());
        tomorrowDescription.setText(weatherTool.getTomorrowDescription());
        dayAfter.setText(weatherTool.getDayAfter());
        dayAfterDescription.setText(weatherTool.getDayAfterDescription());
        dayDayAfter.setText(weatherTool.getDayDayAfter());
        dayDayAfterDescription.setText(weatherTool.getDayDayAfterDescription());
        dayDayDayAfter.setText((weatherTool.getDayDayDayAfter()));
        dayDayDayAfterDescription.setText(weatherTool.getDayDayDayAfterDescription());

        img1.setImage(new Image("/images/" + weatherTool.getIconA() + ".png"));
        img2.setImage(new Image("/images/" + weatherTool.getIconB() + ".png"));
        img3.setImage(new Image("/images/" + weatherTool.getIconC() + ".png"));
        img4.setImage(new Image("/images/" + weatherTool.getIconD() + ".png"));
    }
    public void reset() {
        bottomSet(false);
        temperature.setText("");
        desc.setText("");
        pressure.setText("");
        img.setImage(null);
    }
    private void showToast(String message) {
        errors.setText(message);
        errors.setTextFill(Color.RED);
        errors.setStyle("-fx-font-weight:bold;");

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), errors);
        fadeIn.setToValue(1);
        fadeIn.setFromValue(0);
        fadeIn.play();

        fadeIn.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.play();
            pause.setOnFinished(event2 -> {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), errors);
                fadeOut.setToValue(0);
                fadeOut.setFromValue(1);
                fadeOut.play();
            });
        });
    }

    public String setDayNames(String dzisiejszyDzien, String jezyk) {
        datas = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        if (jezyk.equals("polski")) {
            switch (dzisiejszyDzien) {
                case "Mon":
                    return "PONIEDZIAŁEK " + formatter.format(datas);
                case "Tue":
                    return "WTOREK " + formatter.format(datas);
                case "Wed":
                    return "ŚRODA " + formatter.format(datas);
                case "Thu":
                    return "CZWARTEK " + formatter.format(datas);
                case "Fri":
                    return "PIĄTEK " + formatter.format(datas);
                case "Sat":
                    return "SOBOTA " + formatter.format(datas);
                case "Sun":
                    return "NIEDZIELA " + formatter.format(datas);
            }
        } else {
            switch (dzisiejszyDzien) {
                case "Mon":
                    return "MONDAY " + formatter.format(datas);
                case "Tue":
                    return "TUESDAY " + formatter.format(datas);
                case "Wed":
                    return "WEDNESDAY " + formatter.format(datas);
                case "Thu":
                    return "THURSDAY " + formatter.format(datas);
                case "Fri":
                    return "FRIDAY " + formatter.format(datas);
                case "Sat":
                    return "SATURDAY " + formatter.format(datas);
                case "Sun":
                    return "SUNDAY " + formatter.format(datas);
            }
        }
        return "brak";
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale currentLocale=Locale.getDefault();
        String name = new SimpleDateFormat("EEE",Locale.ENGLISH).format(new Date());
        String nameDay = setDayNames(name, currentLocale.getDisplayLanguage());
        day.setText(nameDay);
        this.language=currentLocale.getDisplayLanguage();
        weatherMenager = new WeatherMenager(citySet);
        weatherTool = new WeatherTool(citySet);
        weatherTool.fetchLocalApi();
        if(weatherTool.getConnectionIsOpen()) {
            String dateFromApi = weatherTool.coordinateWeather();
            weatherTool.downloadDataWeather(dateFromApi);

            temperature1.setText(weatherTool.getTempToday() + "°C");
            desc1.setText(weatherTool.getDescriptionToday().toUpperCase());
            pressure1.setText(weatherTool.getPressureToday() + " hPa");
            city1.setText(weatherTool.getCity().toUpperCase());

            tomorrow1.setText(weatherTool.getTomorrow());
            tomorrowDescription1.setText(weatherTool.getTomorrowDescription());
            dayAfter1.setText(weatherTool.getDayAfter());
            dayAfterDescription1.setText(weatherTool.getDayAfterDescription());
            dayDayAfter1.setText(weatherTool.getDayDayAfter());
            dayDayAfterDescription1.setText(weatherTool.getDayDayAfterDescription());
            dayDayDayAfter1.setText(weatherTool.getDayDayDayAfter());
            dayDayDayAfterDescription1.setText(weatherTool.getDayDayDayAfterDescription());

            img5.setImage(new Image("/images/" + weatherTool.getIcon0() + ".png"));
            img6.setImage(new Image("/images/" + weatherTool.getIconA() + ".png"));
            img7.setImage(new Image("/images/" + weatherTool.getIconB() + ".png"));
            img8.setImage(new Image("/images/" + weatherTool.getIconC() + ".png"));
            img9.setImage(new Image("/images/" + weatherTool.getIconD() + ".png"));
        }else{
            if(language.equals("polski")) {
                showToast("Brak internetu!!!!");
            }else showToast("Internet Down. Please Connect");
        }
        if (!cityName.getText().equals("City Name")&&!cityName.getText().equals("Nazwa miasta")) {
            citySet=cityName.getText();
            cityName.setText(citySet);
            cityName.setDisable(true);
            set.setVisible(false);
            cancel.setVisible(false);
            errors.setText("");

            try {
                showWeather();
                showForecast();
            } catch (Exception e) {
                city.setStyle("-fx-text-fill:red;");
                if(currentLocale.getDisplayLanguage().equals("polski")){
                    city.setText("Error! - Brak Internetu");
                    showToast("Brak Internetu. Połącz sie ponownie!");
                }else {
                    city.setText("Error! - No Internet");
                    city.setTextFill(Color.RED);
                    showToast("Internet Down. Please Connect");
                }
                reset();
                change.setDisable(true);
                cityName.setText("");
            }

            cityName.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    setPressed();
                }
            });

        }else{
            weatherTool = new WeatherTool(citySet);
            set.setVisible(false);
            cancel.setVisible(false);
            cityName.setDisable(true);
        }
    }
}
