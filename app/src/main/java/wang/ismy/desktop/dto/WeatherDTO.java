package wang.ismy.desktop.dto;

import java.util.ArrayList;
import java.util.List;

public class WeatherDTO {
    private String currentWeather;
    /**
     * 风向
     */
    private String currentWindDirection;
    /**
     * 风速
     */
    private String currentWindSpeed;
    private String outdoorTemperature;
    private String feelLikeTemperature;
    private String outdoorHumidity;
    /**
     * 2小时降水概率
     */
    private String rainFallIn2HourProbability;
    /**
     * 降水方位、进行时文字描述
     */
    private String rainFallPrecipitation;

    private String lastUpdateTime;

    private List<ForecastItem> forecastDaily = new ArrayList<>();
    private List<ForecastItem> forecastHourly = new ArrayList<>();

    public List<ForecastItem> getForecastDaily() {
        return forecastDaily;
    }

    public void setForecastDaily(List<ForecastItem> forecastDaily) {
        this.forecastDaily = forecastDaily;
    }

    public List<ForecastItem> getForecastHourly() {
        return forecastHourly;
    }

    public void setForecastHourly(List<ForecastItem> forecastHourly) {
        this.forecastHourly = forecastHourly;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getOutdoorTemperature() {
        return outdoorTemperature;
    }

    public void setOutdoorTemperature(String outdoorTemperature) {
        this.outdoorTemperature = outdoorTemperature;
    }

    public String getFeelLikeTemperature() {
        return feelLikeTemperature;
    }

    public void setFeelLikeTemperature(String feelLikeTemperature) {
        this.feelLikeTemperature = feelLikeTemperature;
    }

    public String getOutdoorHumidity() {
        return outdoorHumidity;
    }

    public void setOutdoorHumidity(String outdoorHumidity) {
        this.outdoorHumidity = outdoorHumidity;
    }

    public String getRainFallIn2HourProbability() {
        return rainFallIn2HourProbability;
    }

    public void setRainFallIn2HourProbability(String rainFallIn2HourProbability) {
        this.rainFallIn2HourProbability = rainFallIn2HourProbability;
    }

    public String getRainFallPrecipitation() {
        return rainFallPrecipitation;
    }

    public void setRainFallPrecipitation(String rainFallPrecipitation) {
        this.rainFallPrecipitation = rainFallPrecipitation;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCurrentWindDirection() {
        return currentWindDirection;
    }

    public void setCurrentWindDirection(String currentWindDirection) {
        this.currentWindDirection = currentWindDirection;
    }

    public String getCurrentWindSpeed() {
        return currentWindSpeed;
    }

    public void setCurrentWindSpeed(String currentWindSpeed) {
        this.currentWindSpeed = currentWindSpeed;
    }

    @Override
    public String toString() {
        return "WeatherDTO{" +
                "currentWeather='" + currentWeather + '\'' +
                ", currentWindDirection='" + currentWindDirection + '\'' +
                ", currentWindSpeed='" + currentWindSpeed + '\'' +
                ", outdoorTemperature='" + outdoorTemperature + '\'' +
                ", feelLikeTemperature='" + feelLikeTemperature + '\'' +
                ", outdoorHumidity='" + outdoorHumidity + '\'' +
                ", rainFallIn2HourProbability='" + rainFallIn2HourProbability + '\'' +
                ", rainFallPrecipitation='" + rainFallPrecipitation + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", forecastDaily=" + forecastDaily +
                ", forecastHourly=" + forecastHourly +
                '}';
    }
}
