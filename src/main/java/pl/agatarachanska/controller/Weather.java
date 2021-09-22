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

import javax.xml.transform.Source;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Weather implements Initializable {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
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
        onCityChange();
    }

    public Weather() {
        this.citySet = "Cracow".toUpperCase();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setMainData(resourceBundle);
        weatherManager = new WeatherManager(citySet, resourceBundle);
        weatherTool = new WeatherTool(citySet, resourceBundle);
        weatherTool.fetchLocalApi();
        if(!weatherTool.getUnexpectError()){
            if (weatherTool.getConnectionIsOpen()) {
                showWeatherAndForecastInMyRegion();
                if(weatherTool.getUnexpectError()){
                    showInfo(resourceBundle.getString("unexpectError"));
                    change.setDisable(true);
                }
            } else {
                showInfo(resourceBundle.getString("brakInternetu"));
            }
        } else {
            showInfo(resourceBundle.getString("unexpectError"));
        }

        if (!cityName.getText().equals("City Name") && !cityName.getText().equals("Nazwa miasta")) {
            updateCityNameFieldAndButtons();
            try {
                showWeather();
                showForecast();
            } catch (Exception e) {
                showInfo(resourceBundle.getString("unexpectError"));
            }
        } else {
            setVisibleButtonsAndDisableTextField();
        }
    }

    private void setMainData(ResourceBundle resourceBundle) {
        String name = new SimpleDateFormat("EEE", Locale.ENGLISH).format(new Date());
        String nameDay = setDayNames(name, resourceBundle);
        day.setText(nameDay);
    }

    private void updateButtonsAndTextField(boolean isInEditMode) {
        cityName.setDisable(!isInEditMode);
        set.setVisible(isInEditMode);
        change.setVisible(!isInEditMode);
        cancel.setVisible(isInEditMode);
    }

    private void onCityChange() {
        city.setStyle("-fx-text-fill: white");
        if (cityName.getText().equals("")) {
            showInfo(resourceBundle.getString("blankCityName"));
        } else {
            try {
                city.setStyle("-fx-text-fill: white");
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
        tomorrow.setText("");
        tomorrowDescription.setText("");
        img1.setImage(null);
        dayAfter.setText("");
        dayAfterDescription.setText("");
        img2.setImage(null);
        dayDayAfter.setText("");
        dayDayAfterDescription.setText("");
        img3.setImage(null);
        dayDayDayAfter.setText("");
        dayDayDayAfterDescription.setText("");
        img4.setImage(null);
    }

    private void showWeather() throws JSONException {
        weatherManager.fetchDataWeather();
        if(weatherManager.getUnexpectErrors()){
            showInfo(resourceBundle.getString("unexpectError"));
        } else {
            setCityTemperatureDescriptionPressureImageInSetCityField();
        }
    }

    private void setCityTemperatureDescriptionPressureImageInSetCityField() {
        city.setText(weatherManager.getCity().toUpperCase());
        temperature.setText(weatherManager.getTemperature().toString() + "°C");
        desc.setText(weatherManager.getDescription().toUpperCase());
        pressure.setText(weatherManager.getPressure() + " hPa");
        img.setImage(new Image(ImagesTool.getImage(weatherManager.getIcon())));
    }

    private void showForecast() {
        weatherTool.weatherInTheSelectedCity();
        if(weatherTool.getUnexpectError()){
            showInfo(resourceBundle.getString("unexpectError"));
        } else {
            showForecastDataAndDescription(tomorrow, tomorrowDescription, dayAfter, dayAfterDescription, dayDayAfter, dayDayAfterDescription, dayDayDayAfter, dayDayDayAfterDescription);
            setImageForecast();
        }

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
        switch (dzisiejszyDzien) {
            case "Mon":
                return resourceBundle.getString("poniedzialek") + " " + FORMATTER.format(datas);
            case "Tue":
                return resourceBundle.getString("wtorek") + " " + FORMATTER.format(datas);
            case "Wed":
                return resourceBundle.getString("sroda") + " " + FORMATTER.format(datas);
            case "Thu":
                return resourceBundle.getString("czwartek") + " " + FORMATTER.format(datas);
            case "Fri":
                return resourceBundle.getString("piatek") + " " + FORMATTER.format(datas);
            case "Sat":
                return resourceBundle.getString("sobota") + " " + FORMATTER.format(datas);
            case "Sun":
                return resourceBundle.getString("niedziela") + " " + FORMATTER.format(datas);
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
        if(!weatherTool.getUnexpectError()) {
            setTemperatureDescriptionPressureAndCityInMainMyRegion();

            showForecastDataAndDescription(tomorrow1, tomorrowDescription1, dayAfter1, dayAfterDescription1, dayDayAfter1, dayDayAfterDescription1, dayDayDayAfter1, dayDayDayAfterDescription1);
            setImageFromMyLocation();
        }
    }

    private void setTemperatureDescriptionPressureAndCityInMainMyRegion() {
        temperature1.setText(weatherTool.getTempToday() + "°C");
        desc1.setText(weatherTool.getDescriptionToday().toUpperCase());
        pressure1.setText(weatherTool.getPressureToday() + " hPa");
        city1.setText(weatherTool.getCity().toUpperCase());
    }

    private void showForecastDataAndDescription(Label tomorrow1, Label tomorrowDescription1, Label dayAfter1, Label dayAfterDescription1, Label dayDayAfter1, Label dayDayAfterDescription1, Label dayDayDayAfter1, Label dayDayDayAfterDescription1) {
        tomorrow1.setText(weatherTool.getTomorrow());
        tomorrowDescription1.setText(weatherTool.getTomorrowDescription());
        dayAfter1.setText(weatherTool.getDayAfter());
        dayAfterDescription1.setText(weatherTool.getDayAfterDescription());
        dayDayAfter1.setText(weatherTool.getDayDayAfter());
        dayDayAfterDescription1.setText(weatherTool.getDayDayAfterDescription());
        dayDayDayAfter1.setText(weatherTool.getDayDayDayAfter());
        dayDayDayAfterDescription1.setText(weatherTool.getDayDayDayAfterDescription());
    }

    private void setImageFromMyLocation() {
        img5.setImage(new Image(ImagesTool.getImage(weatherTool.getIcon0())));
        img6.setImage(new Image(ImagesTool.getImage(weatherTool.getIconA())));
        img7.setImage(new Image(ImagesTool.getImage(weatherTool.getIconB())));
        img8.setImage(new Image(ImagesTool.getImage(weatherTool.getIconC())));
        img9.setImage(new Image(ImagesTool.getImage(weatherTool.getIconD())));
    }
}
