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
import pl.agatarachanska.model.WeatherManager;
import pl.agatarachanska.model.WeatherTool;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Weather implements Initializable {

    private String citySet;
    private WeatherManager weatherManager;
    private WeatherTool weatherTool;
    private Date datas;

    @FXML
    private Label city, city1, day, temperature, temperature1, pressure, pressure1, tomorrow, tomorrow1, dayAfter,
            dayAfter1, dayDayAfter, dayDayAfter1, dayDayDayAfter, dayDayDayAfter1, desc, desc1, errors, tomorrowDescription,
            dayAfterDescription, dayDayAfterDescription, dayDayDayAfterDescription, tomorrowDescription1,
            dayAfterDescription1, dayDayAfterDescription1, dayDayDayAfterDescription1;

    @FXML
    private ImageView img, img1, img2, img3, img4, img5, img6, img7, img8, img9;

    @FXML
    private TextField cityName;

    @FXML
    private Button change, set, cancel;
    private ResourceBundle resourceBundle;

    @FXML
    void handleButtonCancel(javafx.event.ActionEvent event) {
        String initialCity = city.getText();
        cityName.setText(initialCity);
        updateButtonsAndTextField(false);
    }

    @FXML
    void handleButtonChange(javafx.event.ActionEvent event) {
        cityName.setText("");
        updateButtonsAndTextField(true);
        cityName.requestFocus();
    }

    @FXML
    void handleButtonSet(javafx.event.ActionEvent event) {
        setPressed();
    }

    public Weather() {
        this.citySet = "Cracow".toUpperCase();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String name = new SimpleDateFormat("EEE", Locale.ENGLISH).format(new Date());
        String nameDay = setDayNames(name, resourceBundle);
        day.setText(nameDay);
        this.resourceBundle = resourceBundle;
        weatherManager = new WeatherManager(citySet, resourceBundle);
        weatherTool = new WeatherTool(citySet, resourceBundle);
        weatherTool.fetchLocalApi();
        if (weatherTool.getConnectionIsOpen()) {
            showWeatherAndForecastInMyRegion();
        } else {
            showInfo(resourceBundle.getString("brakInternetu"));
        }
        if (!cityName.getText().equals("City Name") && !cityName.getText().equals("Nazwa miasta")) {
            updateCityNameFieldAndButtons();
            try {
                showWeather();
                showForecast();
            } catch (Exception e) {
                showErrorInfo();
            }
            cityName.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    setPressed();
                }
            });
        } else {
            setVisibleButtonsAndDisableTextField();
        }
    }

    private void updateButtonsAndTextField(boolean isInEditMode) {
        cityName.setDisable(!isInEditMode);
        set.setVisible(isInEditMode);
        change.setVisible(!isInEditMode);
        cancel.setVisible(isInEditMode);
    }

    private void setPressed() {
        city.setStyle("-fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #53f5cf, #0987db)");
        if (cityName.getText().equals("")) {
            showInfo(resourceBundle.getString("blankCityName"));
        } else {
            try {
                city.setStyle("-fx-text-fill: linear-gradient(from 25% 25% to 100% 100%, #53f5cf, #0987db)");
                this.citySet = cityName.getText().trim();
                cityName.setText((citySet.trim()).toUpperCase());
                weatherTool = new WeatherTool(citySet, resourceBundle);
                weatherManager = new WeatherManager(citySet, resourceBundle);
                weatherTool.fetchLocalApi();
                if (!weatherTool.getConnectionIsOpen()) {
                    city.setText("Error!");
                    city.setStyle("-fx-text-fill:red;");
                    showInfo(resourceBundle.getString("brakInternetu"));
                    reset();
                } else {
                    showWeather();
                    showForecast();
                    updateButtonsAndTextField(false);
                }
            } catch (Exception e) {
                city.setText("Error!");
                city.setStyle("-fx-text-fill:red;");
                showInfo(resourceBundle.getString("brakPrognozy"));
                reset();
            }
        }
    }

    private void reset() {
        updateButtonsAndTextField(false);
        temperature.setText("");
        desc.setText("");
        pressure.setText("");
        img.setImage(null);
    }

    private void showWeather() throws JSONException {
        weatherManager.fetchDataWeather();
        city.setText(weatherManager.getCity().toUpperCase());
        temperature.setText(weatherManager.getTemperature().toString() + "°C");
        desc.setText(weatherManager.getDescription().toUpperCase());
        img.setImage(new Image(ImagesTool.getImage(weatherManager.getIcon())));
        pressure.setText(weatherManager.getPressure() + " hPa");
    }

    private void showForecast() {
        weatherTool.weatherInTheSelectedCity();
        tomorrow.setText(weatherTool.getTomorrow());
        tomorrowDescription.setText(weatherTool.getTomorrowDescription());
        dayAfter.setText(weatherTool.getDayAfter());
        dayAfterDescription.setText(weatherTool.getDayAfterDescription());
        dayDayAfter.setText(weatherTool.getDayDayAfter());
        dayDayAfterDescription.setText(weatherTool.getDayDayAfterDescription());
        dayDayDayAfter.setText((weatherTool.getDayDayDayAfter()));
        dayDayDayAfterDescription.setText(weatherTool.getDayDayDayAfterDescription());

        setImageForecast();
    }

    private void setImageForecast() {
        img1.setImage(new Image(ImagesTool.getImage(weatherTool.getIconA())));
        img2.setImage(new Image(ImagesTool.getImage(weatherTool.getIconB())));
        img3.setImage(new Image(ImagesTool.getImage(weatherTool.getIconC())));
        img4.setImage(new Image(ImagesTool.getImage(weatherTool.getIconD())));
    }


    private void showInfo(String message) {
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

    private String setDayNames(String dzisiejszyDzien, ResourceBundle resourceBundle) {
        datas = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        switch (dzisiejszyDzien) {
            case "Mon":
                return resourceBundle.getString("poniedzialek") + " " + formatter.format(datas);
            case "Tue":
                return resourceBundle.getString("wtorek") + " " + formatter.format(datas);
            case "Wed":
                return resourceBundle.getString("sroda") + " " + formatter.format(datas);
            case "Thu":
                return resourceBundle.getString("czwartek") + " " + formatter.format(datas);
            case "Fri":
                return resourceBundle.getString("piatek") + " " + formatter.format(datas);
            case "Sat":
                return resourceBundle.getString("sobota") + " " + formatter.format(datas);
            case "Sun":
                return resourceBundle.getString("niedziela") + " " + formatter.format(datas);
        }
        return "brak";
    }

    private void setVisibleButtonsAndDisableTextField() {
        weatherTool = new WeatherTool(citySet, resourceBundle);
        set.setVisible(false);
        cancel.setVisible(false);
        cityName.setDisable(true);
    }

    private void showErrorInfo() {
        city.setStyle("-fx-text-fill:red;");
        city.setTextFill(Color.RED);
        city.setText(resourceBundle.getString("error"));
        showInfo(resourceBundle.getString("brakInternetu"));
        reset();
        change.setDisable(true);
        cityName.setText("");
    }

    private void updateCityNameFieldAndButtons() {
        citySet = cityName.getText();
        cityName.setText(citySet);
        cityName.setDisable(true);
        set.setVisible(false);
        cancel.setVisible(false);
        errors.setText("");
    }

    private void showWeatherAndForecastInMyRegion() {
        weatherTool.weatherInYourRegion();
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
        setImageFromMyLocation();
    }

    private void setImageFromMyLocation() {
        img5.setImage(new Image(ImagesTool.getImage(weatherTool.getIcon0())));
        img6.setImage(new Image(ImagesTool.getImage(weatherTool.getIconA())));
        img7.setImage(new Image(ImagesTool.getImage(weatherTool.getIconB())));
        img8.setImage(new Image(ImagesTool.getImage(weatherTool.getIconC())));
        img9.setImage(new Image(ImagesTool.getImage(weatherTool.getIconD())));
    }
}
