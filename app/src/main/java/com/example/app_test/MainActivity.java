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
    TextView textIniciar;
    WebView webview;

    // Variaveis em comum
    Button botaumPara;
    Button botaumInicia;
    IntentFilter intentfilter;

    public String printInfo;
    public String printHealth;
    public String printVoltage;
//    Timer timer = new Timer();
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewStatus = (TextView) findViewById(R.id.textViewStatus);
        textviewHealth = (TextView) findViewById(R.id.textViewHealth);
        textviewVoltage = (TextView) findViewById(R.id.textViewVoltage);
        textIniciar = (TextView) findViewById(R.id.textviewIniciar);

        webview = (WebView) findViewById(R.id.webview);
        botaumInicia = (Button) findViewById(R.id.inicia);
        botaumPara = (Button) findViewById(R.id.para);
        playVideo = (CheckBox) findViewById(R.id.playVideo);

        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        botaumInicia.setVisibility(View.VISIBLE);
        botaumPara.setVisibility(View.GONE);

        botaumInicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                Writer("", true);

                textIniciar.setText("");
//                if (timer != null){
//                    timer.cancel();
//                }

//                IniciarRotina myRotina = new IniciarRotina();
//                timer.schedule(myRotina, 0, 2_000);
//                botaumInicia.setVisibility(View.GONE);
//                botaumPara.setVisibility(View.VISIBLE);

                if (playVideo.isChecked()){
                    openVideo();
                    IniciarRotina myRotina = new IniciarRotina();
                    timer.schedule(myRotina, 0, 300_000);
                    botaumInicia.setVisibility(View.GONE);
                    botaumPara.setVisibility(View.VISIBLE);
                } else {
                    IniciarRotina myRotina = new IniciarRotina();
                    timer.schedule(myRotina, 0, 300_000);
                    botaumInicia.setVisibility(View.GONE);
                    botaumPara.setVisibility(View.VISIBLE);
                    playVideo.setVisibility(View.GONE);
                }
            }
        });

        botaumPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timer != null) {
                    timer.cancel();
                    timer = null;

                    Log.i(TAG, "Log parado!");
                    textviewStatus.setText("");
                    textviewHealth.setText("");
                    textviewVoltage.setText("");
                    textIniciar.setText("ENCERRADO");
                }
                botaumPara.setVisibility(View.GONE);
                botaumInicia.setVisibility(View.VISIBLE);
                playVideo.setVisibility(View.VISIBLE);

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(broadcastreceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openVideo(){
        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("https://www.youtube.com/watch?time_continue=15&v=NYgmFncXxqE");
    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // Instancia do GetBatteryVoltageApp
            int batteryVol = (int)(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0));
            float fullVoltage = (float) (batteryVol * 0.001);

            Log.i(TAG, "Lendo o fullVoltage: "+fullVoltage);
            printVoltage = "Voltagem do aparelho "+fullVoltage+" volts";

            // Instancias do GetBatteryInfoApp
            int deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel=(int)(((float)level / (float)scale) * 100.0f);

            Log.i(TAG, "Lendo o batteryLevel: "+batteryLevel);

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
            Log.i(TAG, "Lendo o deviceHealth: "+deviceHealth);
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

        }};

    private void Writer(String text, boolean inicio) {
        String dtaString = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        File logFile = new File(getFilesDir(), dtaString+".txt");

        LocalDateTime data = LocalDateTime.now();
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("HH:mm:ss");
        String dataFormatada = data.format(dataFormato);

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