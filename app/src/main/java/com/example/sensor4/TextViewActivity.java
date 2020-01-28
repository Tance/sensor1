/*package com.example.sensor4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TextViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);

    }
}*/
package com.example.sensor4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class TextViewActivity extends AppCompatActivity {
    private TextView accelerometerView;
    private TextView orientationView;
    private SensorManager sensorManager;
    private MySensorEventListener sensorEventListener;

    private Button mBtnTextView;
    private Button btnGetSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);

        sensorEventListener = new MySensorEventListener();
        accelerometerView = (TextView) this.findViewById(R.id.accelerometerView);
        orientationView = (TextView) this.findViewById(R.id.orientationView);
        //获取感应器管理器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        String filePath = "/sdcard/AsensorData/";
        String fileName = "data.txt";
        String firstString = "firstString";
        writeTxtToFile(firstString, filePath, fileName);
    }

    @Override
    protected void onResume() {
        //获取方向传感器
        Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //获取加速度传感器
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    private final class MySensorEventListener implements SensorEventListener {
        //可以得到传感器实时测量出来的变化值
        @Override
        public void onSensorChanged(SensorEvent event) {
            //得到方向的值
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];
                orientationView.setText("Orientation: " + x + ", " + y + ", " + z);
            }
            //得到加速度的值
            else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];
                accelerometerView.setText("Accelerometer&TIme: " +System.currentTimeMillis()+","+ x + ", " + y + ", " + z);
                String inputString = ("Accelerometer&TIme: " +System.currentTimeMillis()+","+ x + ", " + y + ", " + z).toString();
                String filePath = "/sdcard/AsensorData/";
                String fileName = "data.txt";
                writeTxtToFile(inputString, filePath, fileName);
                //save(inputString);
                //writeData();
            }


        }




        //重写变化
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    //暂停传感器的捕获
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }
    // 将字符串写入到文本文件中
    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\t";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

//生成文件

    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//生成文件夹

    private void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
    private void save(String input) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = openFileOutput("sensordata.txt", Context.MODE_APPEND);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

}
    /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWedget();
    }
    private void initWedget() {
        btnGetSensor = (Button) findViewById(R.id.btn1);
        btnGetSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    //得到手机上所有的传感器

                    List<Sensor> listSensor = manager.getSensorList(Sensor.TYPE_ALL);
                    int i = 1;
                    for (Sensor sensor : listSensor) {
                        Log.d("sensor " + i, sensor.getName());
                        i++;
                    }
                    //通过调用getDefaultSensor方法获取某一个类型的默认传感器
                    //Sensor s = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
                }
            });
    }







    public void showToast(View view){
        Toast.makeText(this,"测试",Toast.LENGTH_SHORT).show();
    }

}
*/
