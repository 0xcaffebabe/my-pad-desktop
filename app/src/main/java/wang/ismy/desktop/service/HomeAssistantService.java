package wang.ismy.desktop.service;

import android.util.Pair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeAssistantService {
    private static OkHttpClient client = new OkHttpClient();
    public Pair<String, String> getRoomTemperatureAndHumidity() {

        Request get = new Request.Builder().url("http://192.168.31.160:8123/api/states")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiI0MjQ2YzgwOWQyNDM0MjllOGQwMDZlN2U3ZTVhNjU3NCIsImlhdCI6MTY3MTIwNDgwMSwiZXhwIjoxOTg2NTY0ODAxfQ.Q8Uag6vHNXevREsJKIANFhgGAAaB4o0nOhQ0C0JdKMo")
                .build();


        Response response = null;
        try {
            response = client.newCall(get).execute();
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            return Pair.create("", "");
        }
        String resp = null;
        try {
            resp = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray arr = JSONObject.parseArray(resp);
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = arr.getJSONObject(i);
            String id = json.getString("entity_id");
            if ("sensor.miaomiaoce_t2_3b50_temperature_humidity_sensor".equals(id)) {
                JSONObject attr = json.getJSONObject("attributes");
                return Pair.create(attr.getString("temperature-2-1"), attr.getString("relative_humidity-2-2"));
            }
        }
        return Pair.create("", "");
    }
}
