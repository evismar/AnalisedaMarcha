package evismar.analisedamarcha;

import android.content.Context;
import android.content.Intent;
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


public class Medicao extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManagerAcc;
    private SensorManager senSensorManagerGyr;
    private SensorManager senSensorManagerQtn;
    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private Sensor senQuaternion;
    private StringBuilder texto;
    private String linha = new String();
    Button button;

    private Button startButton;
    private Button pauseButton;
    private Button enviarButton;
    private Button reiniciarButton;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int secs;
    int mins;
    String infoPessoal;
    long tempoInicial = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicao);

        infoPessoal = getIntent().getStringExtra("infoPessoal");

        senSensorManagerAcc = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManagerAcc.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senSensorManagerAcc.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        senSensorManagerGyr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyroscope = senSensorManagerGyr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senSensorManagerGyr.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        senSensorManagerQtn = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senQuaternion = senSensorManagerQtn.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        senSensorManagerQtn.registerListener(this, senQuaternion, SensorManager.SENSOR_DELAY_FASTEST);
        texto = new StringBuilder();

        timerValue = (TextView) findViewById(R.id.timerValue);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                pauseButton.setEnabled(true);
                startButton.setEnabled(false);


            }
        });

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                enviarButton.setEnabled(true);
                reiniciarButton.setEnabled(true);


            }
        });

        enviarButton = (Button) findViewById(R.id.enviarButton);
        enviarButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                enviaEmail();

  //              finish();
  //              startActivity(getIntent());
                //TODO
                //Voltar a página inicial

            }
        });

        reiniciarButton = (Button) findViewById(R.id.reiniciarButton);
        reiniciarButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                finish();
                startActivity(getIntent());

            }
        });

        reiniciarButton.setEnabled(false);
        pauseButton.setEnabled(false);
        enviarButton.setEnabled(false);

    }





    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(((mins*60)+secs)>10){

            if(tempoInicial == 0) {
                tempoInicial = System.currentTimeMillis();
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

                if(linha.equalsIgnoreCase("")){
                    linha = "Accelerometer:, " + (System.currentTimeMillis()-tempoInicial) + "," + sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2] + ",";
                }
                else{
                    if(!linha.contains("Gyr") && !linha.contains("Quat")){
                        linha = "Accelerometer:, " + (System.currentTimeMillis()-tempoInicial) + "," + sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2] + ",";
                    }
                }


            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

                if(linha.contains("Acc") && !linha.contains("Gyr") && !linha.contains("Quat")){
                    linha+="Gyrometer:," + (System.currentTimeMillis()-tempoInicial) + "," + sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2]+",";
                }

            }
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
                if(linha.contains("Gyr")){
                    linha+=" Quaternion:," + (System.currentTimeMillis()-tempoInicial) + "," + sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2]+ "," + sensorEvent.values[3]+"\n";
                    texto.append(linha);
                    linha = "";
                }

            }
        }




    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }



    public void enviaEmail(){
        Context context = getApplicationContext();

        Log.i("LogDoEvinho", Environment.getExternalStorageDirectory().getPath());

        senSensorManagerAcc.unregisterListener(this);
        senSensorManagerGyr.unregisterListener(this);
        try {
            Log.i("LogDoEvinho", "");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("dados_medicao.txt", Context.CONTEXT_IGNORE_SECURITY));
            outputStreamWriter.write(texto.toString());
            outputStreamWriter.close();

        }
        catch (IOException e) {

            Log.i("LogDoEvinho", "File write failed: " + e.toString());
        }

        writeToExternal(context, "dados_medicao.txt");

        File file = new File(context.getExternalFilesDir(null) + File.separator + "dados_medicao.txt");


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"evismar.almeida@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Coleta de dados");
        i.putExtra(Intent.EXTRA_TEXT, infoPessoal);

        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
      //  i.putExtra(Intent.EXTRA_STREAM, Uri.parse("texto.txt"));

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Medicao.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
    public void writeToExternal(Context context, String filename){
        try {
            File file = new File(context.getExternalFilesDir(null), filename); //Get file location from external source
            InputStream is = new FileInputStream(context.getFilesDir() + File.separator + filename); //get file location from internal
            OutputStream os = new FileOutputStream(file); //Open your OutputStream and pass in the file you want to write to
            byte[] toWrite = new byte[is.available()]; //Init a byte array for handing data transfer
            Log.i("Available ", is.available() + "");
            int result = is.read(toWrite); //Read the data from the byte array
            Log.i("Result", result + "");
            os.write(toWrite); //Write it to the output stream
            is.close(); //Close it
            os.close(); //Close it
            Log.i("Copying to", "" + context.getExternalFilesDir(null) + File.separator + filename);
            Log.i("Copying from", context.getFilesDir() + File.separator + filename + "");
        } catch (Exception e) {
            Toast.makeText(context, "File write failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show(); //if there's an error, make a piece of toast and serve it up
        }
    }

/*    public void onSensorChange(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
*/
}

