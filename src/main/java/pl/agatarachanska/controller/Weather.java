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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.JSONException;
import pl.agatarachanska.model.WeatherMenager;
import pl.agatarachanska.model.WeatherTool;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
