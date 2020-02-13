package com.example.app_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    String TAG = "Script";
    TextView textviewStatus;
    TextView textviewHealth;
    TextView textviewVoltage;
    TextView textIniciar;

    // Variaveis em comum
    Button botaum0;
    Button botaum;
    IntentFilter intentfilter;

    public String printInfo;
    public String printHealth;
    public String printVoltage;
    Timer timer;

    boolean cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewStatus = (TextView) findViewById(R.id.textViewStatus);
        textviewHealth = (TextView) findViewById(R.id.textViewHealth);
        textviewVoltage = (TextView) findViewById(R.id.textViewVoltage);

//        textIniciar = (TextView) findViewById(R.id.textIniciar);
        botaum = (Button) findViewById(R.id.inicia);
        botaum0 = (Button) findViewById(R.id.parar);
//        botaum.setChecked(false);
        cancel = false;

        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Writer("", true);
        botaum.setVisibility(View.VISIBLE);
        botaum0.setVisibility(View.GONE);
        botaum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarRotina();
                botaum.setVisibility(View.GONE);
                botaum0.setVisibility(View.VISIBLE);
            }
        });

        botaum0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pararRotina();
                botaum0.setVisibility(View.GONE);
                botaum0.setVisibility(View.VISIBLE);
            }
        });

    }

    public void pararRotina(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
            textIniciar.setText("ENCERRADO");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastreceiver != null)
        {
            unregisterReceiver(broadcastreceiver);
        }

    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Instancia do GetBatteryVoltageApp
//            int batteryVol;
//            float fullVoltage;
            int batteryVol = (int)(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0));
            float fullVoltage = (float) (batteryVol * 0.001);
//            Log.i("Script", "Lendo o batteryVol: "+batteryVol);
            Log.i("Script", "Lendo o fullVoltage: "+fullVoltage);
            printVoltage = " "+fullVoltage+" volt";

            // Instancias do GetBatteryInfoApp
//            int deviceStatus;
            int deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel=(int)(((float)level / (float)scale) * 100.0f);
//            Log.i("Script", "Lendo o deviceStatus: "+deviceStatus);
            Log.i("Script", "Lendo o batteryLevel: "+batteryLevel);

            if(deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING){
                printInfo = " = Charging at "+batteryLevel+" %"; }

            if(deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING){
                printInfo = " = Discharging at "+batteryLevel+" %"; }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL){
                printInfo = " = Battery Full at "+batteryLevel+" %"; }

            if(deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN){
                printInfo = " = Unknown at "+batteryLevel+" %"; }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                printInfo = " = Not Charging at "+batteryLevel+" %"; }


            //Instancia do CheckBatteryHealthApp
            int deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            Log.i("Script", "Lendo o deviceHealth: "+deviceHealth);

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_COLD){
                printHealth = " = Cold";}

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD){
                printHealth = " = Dead"; }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD){
                printHealth = " = Good"; }

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                printHealth = " = OverHeat"; }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                printHealth = " = Over voltage"; }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                printHealth = " = Unknown";}

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                printHealth = " = Unspecified Failure"; }
        }
    };

    public void iniciarRotina(){
        final long tempo = 2_000;
        Timer timer = new Timer();

        TimerTask rotina = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.registerReceiver(broadcastreceiver, intentfilter);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        textviewVoltage.setText(printVoltage);
                        Writer(printVoltage, false);
                        textviewHealth.setText(printHealth);
                        Writer(printHealth, false);
                        textviewStatus.setText(printInfo);
                        Writer(printInfo, false);
                    }
                });

            }
        };

        timer.scheduleAtFixedRate(rotina, 0,tempo);
    }

    public void pararRotina() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void Writer(String text, boolean inicio) {
        File logFile = new File(getFilesDir(), "log.txt");
//        System.out.println(logFile.getAbsolutePath());

        LocalDateTime data = LocalDateTime.now();
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dataFormatada = data.format(dataFormato);

        String TAG = "Script";

        try (FileOutputStream fos = new FileOutputStream(logFile, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            if (inicio) {
                bw.write(" \n" + "====================\n" + dataFormatada + "  Aplicação iniciada.\n");
                Log.i(TAG, "Log iniciado");

            } else {
                bw.write(dataFormatada + "  " + text + "\n");
                Log.i(TAG, "Texto gravado");
            }

            // Toast.makeText(this, "Salvo em "+getFilesDir()+"/"+"log.txt", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.w(TAG, "error while writing log", e);
        }
    }
}
