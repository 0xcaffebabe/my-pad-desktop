package wang.ismy.desktop.service;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.http.HttpUtil;
import wang.ismy.desktop.dto.WeatherDTO;

/**
 * 天气操作类
 */
public class WeatherService {

    private Map<String, String> weatherMap = new HashMap<>();

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

        return weatherDTO;
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
