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

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Weather implements Initializable {

    private WeatherMenager weatherMenager;
    private WeatherTool weatherTool;
    public String citySet;

    @FXML
    private Label city,city1,day,day1,temperature,temperature1,pressure,pressure1,tomorrow,tomorrow1,dayAfter,
            dayAfter1,dayDayAfter,dayDayAfter1,dayDayDayAfter,dayDayDayAfter1,desc,desc1,errors;

    @FXML
    private ImageView img,img1,img2,img3,img4,img5,img6,img7,img8,img9;

    @FXML
    private TextField cityName;

    @FXML
    private Button change,set,cancel;

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
    private void setPressed() {
        System.out.println(cityName.getText());
        if (cityName.getText().equals("")) {
            showToast("City Name cannot be blank");
        } else {
            try {
                this.citySet = cityName.getText().trim();
                cityName.setText((citySet.trim()).toUpperCase());
                weatherTool = new WeatherTool(citySet);
                weatherMenager = new WeatherMenager(citySet);
                city.setTextFill(Color.web("black"));
                showWeather();
                showForecast();
                bottomSet(false);

            } catch (Exception e) {
                city.setText("Error!");
                city.setTextFill(Color.RED);
                showToast("City with the given name was not found.");
                reset();

            }
        }
    }
    public void showWeather() throws JSONException {
        weatherMenager.fetchDataWeather();
        city.setText(weatherMenager.getCity().toUpperCase());
        temperature.setText(weatherMenager.getTemperature().toString() + "°C");
        day.setText(weatherMenager.getDay().toUpperCase());
        desc.setText(weatherMenager.getDescription().toUpperCase());
        img.setImage(new Image(ImagesTool.getImage(weatherMenager.getIcon())));
        pressure.setText(weatherMenager.getPressure() + " hPa");

    }
    private void showForecast() {

        String weatherApi = weatherTool.cityWeather();
        weatherTool.downloadDataWeather(weatherApi);
        tomorrow.setText(weatherTool.getTomorrow());
        dayAfter.setText(weatherTool.getDayAfter());
        dayDayAfter.setText(weatherTool.getDayDayAfter());
        dayDayDayAfter.setText((weatherTool.getDayDayDayAfter()));

        img1.setImage(new Image("/images/" + weatherTool.getIconA() + ".png"));
        img2.setImage(new Image("/images/" + weatherTool.getIconB() + ".png"));
        img3.setImage(new Image("/images/" + weatherTool.getIconC() + ".png"));
        img4.setImage(new Image("/images/" + weatherTool.getIconD() + ".png"));
    }
    public void reset() {
        bottomSet(false);
        day.setText("");
        temperature.setText("");
        desc.setText("");
        pressure.setText("");
        img.setImage(null);
    }
    private void showToast(String message) {
        errors.setText(message);
        errors.setTextFill(Color.RED);
        errors.setStyle("-fx-background-color: #fff; -fx-background-radius: 50px;");

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

    @FXML
    void yourLocationButton() {
        String dateFromApi = weatherTool.coordinateWeather();
        weatherTool.downloadDataWeather(dateFromApi);

        String dzisiejszyDzien = weatherTool.getTodayDay();

        Locale currentLocale=Locale.getDefault();
        setDayName(dzisiejszyDzien,currentLocale.getDisplayLanguage());


        temperature1.setText(weatherTool.getTempToday()+ "°C");
        desc1.setText(weatherTool.getDescriptionToday().toUpperCase());
        pressure1.setText(weatherTool.getPressureToday()+" hPa");
        city1.setText(weatherTool.getCity().toUpperCase());

        tomorrow1.setText(weatherTool.getTomorrow());
        dayAfter1.setText(weatherTool.getDayAfter());
        dayDayAfter1.setText(weatherTool.getDayDayAfter());
        dayDayDayAfter1.setText(weatherTool.getDayDayDayAfter());

        img5.setImage(new Image("/images/"+weatherTool.getIcon0()+".png"));
        img6.setImage(new Image("/images/"+weatherTool.getIconA()+".png"));
        img7.setImage(new Image("/images/"+weatherTool.getIconB()+".png"));
        img8.setImage(new Image("/images/"+weatherTool.getIconC()+".png"));
        img9.setImage(new Image("/images/"+weatherTool.getIconD()+".png"));


    }

    public void setDayName(String dzisiejszyDzien, String jezyk){
        if(jezyk.equals("polski")){
            switch (dzisiejszyDzien){
                case "Mon":
                    day1.setText("PONIEDZIAŁEK");
                    break;
                case "Tue":
                    day1.setText("WTOREK");
                    break;
                case "Wed":
                    day1.setText("ŚRODA");
                    break;
                case "Thu":
                    day1.setText("CZWARTEK");
                    break;
                case "Fri":
                    day1.setText("PIĄTEK");
                    break;
                case "Sat":
                    day1.setText("SOBOTA");
                    break;
                case "Sun":
                    day1.setText("NIEDZIELE");
                    break;
            }
        }else{
            switch (dzisiejszyDzien){
                case "Mon":
                    day1.setText("MONDAY");
                    break;
                case "Tue":
                    day1.setText("TUESDAY");
                    break;
                case "Wed":
                    day1.setText("WEDNESDAY");
                    break;
                case "Thu":
                    day1.setText("THURSDAY");
                    break;
                case "Fri":
                    day1.setText("FRIDAY");
                    break;
                case "Sat":
                    day1.setText("SATURDAY");
                    break;
                case "Sun":
                    day1.setText("SUNDAY");
                    break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale currentLocale=Locale.getDefault();
        if (!cityName.getText().equals("City Name")) {
            citySet=cityName.getText();
            cityName.setText(citySet);
            cityName.setDisable(true);
            set.setVisible(false);
            cancel.setVisible(false);
            errors.setText("");
            weatherMenager = new WeatherMenager(citySet);
            weatherTool = new WeatherTool(citySet);

            try {
                showWeather();
                showForecast();
            } catch (Exception e) {
                city.setText("Error! - No Internet");
                city.setTextFill(Color.RED);
                showToast("Internet Down. Please Connect");
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
