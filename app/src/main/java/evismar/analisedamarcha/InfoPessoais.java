package evismar.analisedamarcha;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InfoPessoais extends AppCompatActivity {

    private Button iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pessoais);

        iniciar = (Button) findViewById(R.id.iniciar);
        iniciar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                EditText editText = (EditText)findViewById(R.id.nome);
                String nome = editText.getText().toString();

                EditText idadeText = (EditText)findViewById(R.id.idade);
                String idade = idadeText.getText().toString();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date data = new Date();

                String texto = "Dados do participante:\n \nNome: "+nome+"\nIdade: "+idade+"\n \nColeta de dados realizada no dia:"+df.format(data);

                Intent myIntent = new Intent(view.getContext(),Medicao.class);
                myIntent.putExtra("infoPessoal",texto);
                startActivity(myIntent);


            }
        });
    }

}
