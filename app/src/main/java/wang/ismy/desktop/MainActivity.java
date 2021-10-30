package wang.ismy.desktop;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import wang.ismy.desktop.dto.WeatherDTO;
import wang.ismy.desktop.service.WeatherService;
import wang.ismy.desktop.util.NumberUtils;

public class MainActivity extends AppCompatActivity {

    // 当前时间
    private TextView currentDatetime;

    // 室内温度文本展示
    private TextView indoorTemperatureTextView;

    // 室内湿度
    private TextView indoorHumidityTextView;

    private TextView currentWeatherTextView, outdoorTemperatureTextView, outdoorHumidityTextView, feelLikeTemperatureTextView, rainFallIn2HourProbabilityTextView, rainFallPrecipitationTextView, lastUpdateTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        setContentView(R.layout.activity_main);
        initViewRef();
        getSupportActionBar().hide();
        createUpdateCurrentTimeThread();
        createMonthFutureWeather();
        createDayFutureWeather();
        createWeatherUpdateThread();
        test();
    }



    private void initViewRef(){
        indoorTemperatureTextView = findViewById(R.id.indoorTemperature);
        indoorHumidityTextView = findViewById(R.id.indoorHumidity);
        currentDatetime = findViewById(R.id.currentDatetime);
        currentWeatherTextView = findViewById(R.id.currentWeather);
        outdoorTemperatureTextView = findViewById(R.id.outdoorTemperature);
        outdoorHumidityTextView = findViewById(R.id.outdoorHumidity);
        feelLikeTemperatureTextView = findViewById(R.id.feelLikeTemperature);
        rainFallIn2HourProbabilityTextView = findViewById(R.id.rainFallIn2HourProbability);
        rainFallPrecipitationTextView = findViewById(R.id.rainFallPrecipitation);
        lastUpdateTimeTextView = findViewById(R.id.lastUpdateTime);
    }

    private void test(){
        BluetoothClient mClient = new BluetoothClient(MainActivity.this);
        connectMiJiaSensor(mClient);
    }

    private void connectMiJiaSensor(BluetoothClient mClient){
        String mac = "A4:C1:38:14:3B:50";
        String serviceUUID = "ebe0ccb0-7a0a-4b0c-8a1a-6ff2997da3a6";
        String characterUUID = "ebe0ccc1-7a0a-4b0c-8a1a-6ff2997da3a6";
        mClient.connect(mac,new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                Log.i("ISMY", "connect status: " + code);
                if (code == REQUEST_SUCCESS) {;
                    mClient.notify(mac, UUID.fromString(serviceUUID), UUID.fromString(characterUUID), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID service, UUID character, byte[] value) {
                            int tmp = NumberUtils.byteArrayToInt(new byte[]{value[1],value[0]});
                            int humdity = value[2];
                            Log.i("ISMY", "tmp:" + tmp + ", hum:" + humdity);
                            // 更新室内温度
                            indoorTemperatureTextView.setText("室内:" + (tmp/100d) + "℃");
                            // 更新室内湿度
                            indoorHumidityTextView.setText("室内:" + humdity + "%");
                        }

                        @Override
                        public void onResponse(int code) {
                            if (code == REQUEST_SUCCESS) {

                            }
                        }
                    });
                }else {
                    Log.i("ISMY", "reconnect to mijia sensor");
                    connectMiJiaSensor(mClient);
                }
            }


        });
    }

    private void createUpdateCurrentTimeThread(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 更新当前时间
                currentDatetime.post(() -> {
                    currentDatetime.setText(LocalDateTime.now().format(formatter));
                });
            }
        }).start();
    }

    private void createMonthFutureWeather(){
        LinearLayout list = findViewById(R.id.monthWeatherList);
        list.removeAllViews();
        for (int i = 0; i < 15; i++) {
            LinearLayout item = new LinearLayout(MainActivity.this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.addView(createFutureText("10-31"));
            item.addView(createFutureText("多云"));
            item.addView(createFutureText("25℃~35℃"));
            list.addView(item);
        }
    }

    private void createDayFutureWeather(){
        LinearLayout list = findViewById(R.id.dayWeatherList);
        list.removeAllViews();
        for (int i = 0; i < 15; i++) {
            LinearLayout item = new LinearLayout(MainActivity.this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.addView(createFutureText("19:00"));
            item.addView(createFutureText("多云"));
            item.addView(createFutureText("35℃"));
            list.addView(item);
        }
    }

    private void createWeatherUpdateThread(){
        new Thread(() ->{
            WeatherService weatherService = new WeatherService();
            while(true) {
                WeatherDTO weatherDTO = weatherService.get();
                currentWeatherTextView.post(() -> {
                    currentWeatherTextView.setText(weatherDTO.getCurrentWeather());
                    outdoorTemperatureTextView.setText("室外:" + weatherDTO.getOutdoorTemperature() + "℃");
                    outdoorHumidityTextView.setText("室外:" + weatherDTO.getOutdoorHumidity() + "%");
                    feelLikeTemperatureTextView.setText("体感:" + weatherDTO.getFeelLikeTemperature() + "℃");
                    rainFallIn2HourProbabilityTextView.setText(weatherDTO.getRainFallIn2HourProbability());
                    rainFallPrecipitationTextView.setText(weatherDTO.getRainFallPrecipitation());
                    lastUpdateTimeTextView.setText("最后更新于:" + weatherDTO.getLastUpdateTime());
                });
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private TextView createFutureText(String text) {
        TextView textView = new TextView(MainActivity.this);textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(25);
        return textView;
    }
}