package evismar.analisedamarcha;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Preparacao extends AppCompatActivity {

    String infoPessoal;
    String tempoMax;
    String qtdPassos;
    String cabecalho;

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparacao);

        infoPessoal = getIntent().getStringExtra("infoPessoal");
        tempoMax = getIntent().getStringExtra("duracao");
        cabecalho = getIntent().getStringExtra("cabecalho");
        qtdPassos = getIntent().getStringExtra("passos");

        startButton = (Button) findViewById(R.id.iniciar);
        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Medicao.class);
                myIntent.putExtra("infoPessoal", infoPessoal);
                myIntent.putExtra("cabecalho", cabecalho);
                myIntent.putExtra("passos", qtdPassos);
                myIntent.putExtra("duracao", tempoMax);
                startActivity(myIntent);


            }
        });

    }

}
