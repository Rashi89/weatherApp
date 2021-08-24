package pl.agatarachanska.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Weather implements Initializable {

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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
