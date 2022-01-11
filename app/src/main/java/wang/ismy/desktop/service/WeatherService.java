package wang.ismy.desktop.service;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.http.HttpUtil;
import wang.ismy.desktop.dto.ForecastItem;
import wang.ismy.desktop.dto.WeatherDTO;

/**
 * 天气操作类
 */
public class WeatherService {

    private Map<String, String> weatherMap = new HashMap<>();

    static final String[] directArr = new String[] { "北", "东北偏北", "东北", "东北偏东", "东", "东南偏东", "东南", "东南偏南", "南",
            "西南偏南", "西南", "西南偏西", "西", "西北偏西", "西北", "西北偏北" };

    {buildWeatherMap();}

    public WeatherDTO get(){
        WeatherDTO weatherDTO = new WeatherDTO();
        String json = "";
        try {
            json = HttpUtil.get("https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?longitude=118.636286&latitude=24.874194&locale=zh_cn&isGlobal=false&appKey=weather20151024&sign=zUFJoAR2ZVrDy1vF3D07");
        }catch (Exception e) {
            Log.e("ISMY", "天气请求异常", e);
            return new WeatherDTO();
        }
        weatherDTO.setOutdoorTemperature((String) JSONPath.read(json, "$current.temperature.value"));
        weatherDTO.setOutdoorHumidity((String) JSONPath.read(json, "$current.humidity.value"));
        weatherDTO.setCurrentWeather(weatherMap.get((String) JSONPath.read(json, "$current.weather")));
        weatherDTO.setFeelLikeTemperature((String) JSONPath.read(json, "$current.feelsLike.value"));
        weatherDTO.setLastUpdateTime((String) JSONPath.read(json, "$current.pubTime"));
        weatherDTO.setRainFallIn2HourProbability((String) JSONPath.read(json, "$minutely.probability.maxProbability"));
        weatherDTO.setRainFallPrecipitation((String) JSONPath.read(json, "$minutely.precipitation.description"));
        weatherDTO.setCurrentWindDirection(readableDirection((String) JSONPath.read(json, "$current.wind.direction.value")));
        weatherDTO.setCurrentWindSpeed(calcWindSpeed((String) JSONPath.read(json, "$current.wind.speed.value")));
        weatherDTO.setForecastDaily(buildForecastDaily(json));
        weatherDTO.setForecastHourly(buildForecastHourly(json));
        return weatherDTO;
    }

    private String calcWindSpeed(String value) {
        Double speed = Double.parseDouble(value) * 1000 / 3600;
        return new DecimalFormat("#.00").format(speed);
    }

    private static String readableDirection(String value) {
        float degrees = Float.parseFloat(value);
        int index = 0;
        if (348.75 <= degrees && degrees <= 360) {
                index = 0;
        } else if (0 <= degrees && degrees <= 11.25) {
                index = 0;
        } else if (11.25 < degrees && degrees <= 33.75) {
                index = 1;
        } else if (33.75 < degrees && degrees <= 56.25) {
                index = 2;
        } else if (56.25 < degrees && degrees <= 78.75) {
                index = 3;
        } else if (78.75 < degrees && degrees <= 101.25) {
                index = 4;
        } else if (101.25 < degrees && degrees <= 123.75) {
                index = 5;
        } else if (123.75 < degrees && degrees <= 146.25) {
                index = 6;
        } else if (146.25 < degrees && degrees <= 168.75) {
                index = 7;
        } else if (168.75 < degrees && degrees <= 191.25) {
                index = 8;
        } else if (191.25 < degrees && degrees <= 213.75) {
                index = 9;
        } else if (213.75 < degrees && degrees <= 236.25) {
                index = 10;
        } else if (236.25 < degrees && degrees <= 258.75) {
                index = 11;
        } else if (258.75 < degrees && degrees <= 281.25) {
                index = 12;
        } else if (281.25 < degrees && degrees <= 303.75) {
                index = 13;
        } else if (303.75 < degrees && degrees <= 326.25) {
                index = 14;
        } else if (326.25 < degrees && degrees < 348.75) {
                index = 15;
        } else {
            return "错误";
        }
        return directArr[index];
    }


    private List<ForecastItem> buildForecastDaily(String json){
        String pubTime = (String) JSONPath.read(json, "$forecastDaily.pubTime");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime pubDatetime = LocalDateTime.parse(pubTime, formatter);

        JSONArray temperatureArray = (JSONArray) JSONPath.read(json, "$forecastDaily.temperature.value");
        JSONArray weatherArray = (JSONArray) JSONPath.read(json, "$forecastDaily.weather.value");

        List<ForecastItem> result = new ArrayList<>();
        for (int i = 0; i < temperatureArray.size(); i++) {
            ForecastItem item = new ForecastItem();
            item.setDesc(pubDatetime.plusDays(i).format(dayFormatter));
            JSONObject weatherJson = weatherArray.getJSONObject(i);
            JSONObject temperatureJson = temperatureArray.getJSONObject(i);
            item.setWeather(weatherMap.get(weatherJson.getString("from")) + "-" + weatherMap.get(weatherJson.getString("to")));
            item.setMaxTemperature(temperatureJson.getString("from"));
            item.setMinTemperature(temperatureJson.getString("to"));
            result.add(item);
        }
        return result;
    }

    private List<ForecastItem> buildForecastHourly(String json){
        String pubTime = (String) JSONPath.read(json, "$forecastHourly.temperature.pubTime");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime pubDatetime = LocalDateTime.parse(pubTime, formatter);

        JSONArray temperatureArray = (JSONArray) JSONPath.read(json, "$forecastHourly.temperature.value");
        JSONArray weatherArray = (JSONArray) JSONPath.read(json, "$forecastHourly.weather.value");

        List<ForecastItem> result = new ArrayList<>();
        for (int i = 0; i < temperatureArray.size(); i++) {
            ForecastItem item = new ForecastItem();
            item.setDesc(pubDatetime.plusHours(i).format(dayFormatter));
            String weatherJson = weatherArray.getString(i);
            String temperatureJson = temperatureArray.getString(i);
            item.setWeather(weatherMap.get(weatherJson));
            item.setMaxTemperature(temperatureJson);
            item.setMinTemperature(temperatureJson);
            result.add(item);
        }
        return result;
    }

    private void buildWeatherMap(){
        String json = "{\n" +
                "  \"weatherinfo\": [\n" +
                "    {\n" +
                "      \"code\": 0,\n" +
                "      \"wea\": \"晴\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 1,\n" +
                "      \"wea\": \"多云\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 2,\n" +
                "      \"wea\": \"阴\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 3,\n" +
                "      \"wea\": \"阵雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 4,\n" +
                "      \"wea\": \"雷阵雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 5,\n" +
                "      \"wea\": \"雷阵雨并伴有冰雹\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 6,\n" +
                "      \"wea\": \"雨夹雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 7,\n" +
                "      \"wea\": \"小雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 8,\n" +
                "      \"wea\": \"中雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 9,\n" +
                "      \"wea\": \"大雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 10,\n" +
                "      \"wea\": \"暴雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 11,\n" +
                "      \"wea\": \"大暴雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 12,\n" +
                "      \"wea\": \"特大暴雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 13,\n" +
                "      \"wea\": \"阵雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 14,\n" +
                "      \"wea\": \"小雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 15,\n" +
                "      \"wea\": \"中雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 16,\n" +
                "      \"wea\": \"大雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 17,\n" +
                "      \"wea\": \"暴雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 18,\n" +
                "      \"wea\": \"雾\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 19,\n" +
                "      \"wea\": \"冻雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 20,\n" +
                "      \"wea\": \"沙尘暴\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 21,\n" +
                "      \"wea\": \"小雨-中雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 22,\n" +
                "      \"wea\": \"中雨-大雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 23,\n" +
                "      \"wea\": \"大雨-暴雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 24,\n" +
                "      \"wea\": \"暴雨-大暴雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 25,\n" +
                "      \"wea\": \"大暴雨-特大暴雨\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 26,\n" +
                "      \"wea\": \"小雪-中雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 27,\n" +
                "      \"wea\": \"中雪-大雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 28,\n" +
                "      \"wea\": \"大雪-暴雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 29,\n" +
                "      \"wea\": \"浮沉\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 30,\n" +
                "      \"wea\": \"扬沙\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 31,\n" +
                "      \"wea\": \"强沙尘暴\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 32,\n" +
                "      \"wea\": \"飑\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 33,\n" +
                "      \"wea\": \"龙卷风\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 34,\n" +
                "      \"wea\": \"若高吹雪\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 35,\n" +
                "      \"wea\": \"轻雾\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 53,\n" +
                "      \"wea\": \"霾\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"code\": 99,\n" +
                "      \"wea\": \"未知\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONArray weatherinfo = JSONObject.parseObject(json).getJSONArray("weatherinfo");
        for (int i = 0; i < weatherinfo.size(); i++) {
            JSONObject jsonObject = weatherinfo.getJSONObject(i);
            weatherMap.put(jsonObject.getString("code"), jsonObject.getString("wea"));
        }
    }
}
