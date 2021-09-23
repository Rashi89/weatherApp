package pl.agatarachanska.model;

public class ImagesTool {
    public static String getImage(String icon) {

        switch (icon) {
            case "01d":
                return "/images/01d.png";
            case "01n":
                return "/images/01n.png";
            case "02d":
                return "/images/02d.png";
            case "02n":
                return "/images/02n.png";
            case "03d":
                return "/images/03d.png";
            case "03n":
                return "/images/03n.png";
            case "04d":
                return "/images/04d.png";
            case "04n":
                return "/images/04n.png";
            case "09d":
                return "/images/09d.png";
            case "09n":
                return "/images/09n.png";
            case "10d":
                return "/images/10d.png";
            case "10n":
                return "/images/10n.png";
            case "11d":
                return "/images/11d.png";
            case "11n":
                return "/images/11n.png";
            case "13d":
                return "/images/13d.png";
            case "13n":
                return "/images/13n.png";
            case "50d":
                return "/images/50d.png";
            case "50n":
                return "/images/50n.png";
            default:
                return "/images/icon.png";
        }
    }
    public static String getBackground(String icon) {

        switch (icon) {
            case "01d":
                return "/images/bg_slonce_1.jpg";
            case "01n":
                return "/images/bg_slonce_2.jpg";
            case "02d":
                return "/images/bg_zachmurzenie_1.jpg";
            case "02n":
                return "/images/bg_zachmurzenie_2.jpg";
            case "03d":
                return "/images/bg_zachmurzenie_1.jpg";
            case "03n":
                return "/images/bg_zachmurzenie_2.jpg";
            case "04d":
                return "/images/bg_zachmurzenie_1.jpg";
            case "04n":
                return "/images/bg_zachmurzenie_2.jpg";
            case "09d":
                return "/images/bg_deszcz_1.jpg";
            case "09n":
                return "/images/bg_deszcz_2.jpg";
            case "10d":
                return "/images/bg_deszcz_1.jpg";
            case "10n":
                return "/images/bg_deszcz_2.jpg";
            case "11d":
                return "/images/bg_burza_1.jpg";
            case "11n":
                return "/images/bg_burza_2.jpg";
            case "13d":
                return "/images/bg_snieg_1.jpg";
            case "13n":
                return "/images/bg_snieg_2.jpg";
            case "50d":
                return "/images/bg_mgla_1.jpg";
            case "50n":
                return "/images/bg_mgla_2.jpg";
            default:
                return "/images/bg_slonce_1.jpg";
        }
    }
}
