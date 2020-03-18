package com.example.debugmodecontroler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.SystemProperties;
import android.hardware.usb.UsbManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    Button btnEnable, btnDisable;
    private UsbManager mUsbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        btnEnable = findViewById(R.id.button_enable);
        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemProperties.set("persist.debug_mode_enabled", "1");
                setCurrentFunction2(MainActivity.this,"mtp",true);
                Log.d("hf_test", "enable");
            }
        });
        btnDisable = findViewById(R.id.button_disable);
        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemProperties.set("persist.debug_mode_enabled", "0");
                setCurrentFunction2(MainActivity.this,"mtp",false);
                Log.d("hf_test", "disable");
            }
        });
    }




    private void setCurrentFunction2(Context context, String function, boolean usbDataUnlocked) {
        try {
            // ①获得类对象
            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

            // ②根据对象获得类名
            String usbManagerclassName = usbManager.getClass().getName();
            Log.d("hf_test", "usbManagerclassName: " + usbManagerclassName);

            // ③根据类名获得具体的类
            Class<?> usbManagerClass = Class.forName(usbManagerclassName);

            // ④获得指定的成员方法
            Method setCurrentFunctionMethod  = usbManagerClass.getDeclaredMethod("setCurrentFunction", String.class, boolean.class);

            // ⑤设置成员方法可以被访问
            setCurrentFunctionMethod.setAccessible(true);

            String funName = setCurrentFunctionMethod.getName();
            Log.d("hf_test", "funName: " + funName);
            // ⑥通过反射调用成员方法
            setCurrentFunctionMethod.invoke(usbManager, function, usbDataUnlocked);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
