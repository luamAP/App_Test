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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    CheckBox playVideo;
    String TAG = "Script";
    TextView textviewStatus;
    TextView textviewHealth;
    TextView textviewVoltage;
    TextView textviewIniciar;

    // Variaveis em comum
    Button botaumPara;
    Button botaumInicia;
    IntentFilter intentfilter;
//    IntentFilter intentVideo;

    public String printInfo;
    public String printHealth;
    public String printVoltage;
    Timer timer = new Timer();

    boolean cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewStatus = (TextView) findViewById(R.id.textViewStatus);
        textviewHealth = (TextView) findViewById(R.id.textViewHealth);
        textviewVoltage = (TextView) findViewById(R.id.textViewVoltage);
<<<<<<< HEAD
        textIniciar = (TextView) findViewById(R.id.textviewIniciar);

<<<<<<< HEAD
        botaumInicia = (Button) findViewById(R.id.inicia);
        botaumPara = (Button) findViewById(R.id.para);
//        playVideo = (CheckBox) findViewById(R.id.playVideo);
=======
=======

>>>>>>> parent of c4ff5cf... Update MainActivity.java
        textviewIniciar = (TextView) findViewById(R.id.textviewIniciar);
        botaum = (Button) findViewById(R.id.inicia);
        botaum0 = (Button) findViewById(R.id.parar);
//        botaum.setChecked(false);
        cancel = false;
>>>>>>> parent of c4ff5cf... Update MainActivity.java

        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Writer("", true);
<<<<<<< HEAD
        botaumInicia.setVisibility(View.VISIBLE);
        botaumPara.setVisibility(View.GONE);

        botaumInicia.setOnClickListener(new View.OnClickListener() {
=======
        botaum.setVisibility(View.VISIBLE);
        botaum.setOnClickListener(new View.OnClickListener() {
>>>>>>> parent of c4ff5cf... Update MainActivity.java
            @Override
            public void onClick(View v) {
                textIniciar.setText("");

                if (timer != null){
                    timer.cancel();
                }

                if (playVideo.isChecked()){
//                     carrega um video do youtube
//                    openVideo();
                    IniciarRotina myRotina = new IniciarRotina();
                    timer.schedule(myRotina,0, 2_000);
//                    timer.scheduleAtFixedRate(myRotina,0,2_000);

                } else{
                    playVideo.setVisibility(View.GONE);

                    IniciarRotina myRotina = new IniciarRotina();
//                    iniciarRotina();
                    timer.schedule(myRotina, 0, 2_000);
//                    timer.scheduleAtFixedRate(myRotina,0,2_000);
                }
                botaumInicia.setVisibility(View.GONE);
                botaumPara.setVisibility(View.VISIBLE);

            }
        });

        botaumPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                Log.i(TAG, "Rotina Parada");

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    textviewStatus.setText("");
                    textviewHealth.setText("");
                    textviewVoltage.setText("");
                    textIniciar.setText("ENCERRADO");
                }
                botaumPara.setVisibility(View.GONE);
                botaumInicia.setVisibility(View.VISIBLE);
//                playVideo.setVisibility(View.VISIBLE);
            }
        }); }
=======
                pararRotina();
                botaum0.setVisibility(View.GONE);
                botaum.setVisibility(View.VISIBLE);
            }
        });

    }

    public void pararRotina(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
            textviewStatus.setText("");
            textviewHealth.setText("");
            textviewVoltage.setText("");
            textviewIniciar.setText("ENCERRADO");
        }
    }
>>>>>>> parent of c4ff5cf... Update MainActivity.java

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastreceiver != null)
        {
            unregisterReceiver(broadcastreceiver);
        }

    }

    private void openVideo(){
        WebView webView = (WebView)
                findViewById(R.id.webview);
        webView.loadUrl("https://youtu.be/tL1o60lHaho");
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
            printVoltage = "Voltagem do aparelho "+fullVoltage+" volts";

            // Instancias do GetBatteryInfoApp
//            int deviceStatus;
            int deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel=(int)(((float)level / (float)scale) * 100.0f);
//            Log.i("Script", "Lendo o deviceStatus: "+deviceStatus);
            Log.i("Script", "Lendo o batteryLevel: "+batteryLevel);

            if(deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING){
                printInfo = "Carregando em "+batteryLevel+" %"; }

            if(deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING){
                printInfo = "Descarregando em "+batteryLevel+" %"; }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL){
                printInfo = "Bateria cheia ("+batteryLevel+" %)"; }

            if(deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN){
                printInfo = "Desconhecido: "+batteryLevel+" %"; }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                printInfo = "Não está carregando: "+batteryLevel+" %"; }


            //Instancia do CheckBatteryHealthApp
            int deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            Log.i("Script", "Lendo o deviceHealth: "+deviceHealth);
            String textHealth = "Saúde do Dispositivo: ";

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_COLD){
                printHealth = textHealth +"Frio";}

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD){
                printHealth = textHealth+"Morto"; }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD){
                printHealth = textHealth+"Bem"; }

            if(deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                printHealth = textHealth+"Superaquecido"; }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                printHealth = textHealth+"Sobre tensão"; }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                printHealth = textHealth+"Desconhecido";}

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                printHealth = textHealth+"Falha desconhecida"; }
        }
    };

    class IniciarRotina extends TimerTask {
//        final long tempo = 2_000;
//        Timer timer = new Timer();

//        TimerTask rotina = new TimerTask() {
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

//        timer.schedule(rotina, 0,tempo);
//    }

    public void pararRotina() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
            textIniciar.setText("ENCERRADO");
        }
    }

    private void Writer(String text, boolean inicio) {

        //transformar data em string:
        String dateLog = new SimpleDateFormat("yyyy-MM-dd'.txt'", Locale.US).format(new Date());

        File logFile = new File(getFilesDir(), dateLog);
//        System.out.println(logFile.getAbsolutePath());

        LocalDateTime data = LocalDateTime.now();
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("HH:mm:ss");
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
