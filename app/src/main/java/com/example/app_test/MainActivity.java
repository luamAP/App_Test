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

    // Variaveis em comum
    Button botaum;
    IntentFilter intentfilter;

    public String printInfo;
    public String printHealth;
    public String printVoltage;
    public boolean cancelar = false;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewStatus = (TextView) findViewById(R.id.textViewStatus);
        textviewHealth = (TextView) findViewById(R.id.textViewHealth);
        textviewVoltage = (TextView) findViewById(R.id.textViewVoltage);
        botaum  = (Button) findViewById(R.id.inicia);

        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        

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
                        Writer(printVoltage, false);
                        textviewStatus.setText(printInfo);
                        Writer(printVoltage, false);
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
