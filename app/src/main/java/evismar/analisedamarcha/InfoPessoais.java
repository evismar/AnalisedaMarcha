package evismar.analisedamarcha;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InfoPessoais extends AppCompatActivity {

    private Button iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_info_pessoais);


        Spinner dropdown = (Spinner)findViewById(R.id.sexo);
        String[] items = new String[]{"Masculino", "Feminino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Spinner dropdown2 = (Spinner)findViewById(R.id.etnia);
        String[] items2 = new String[]{"Caucasiano", "Negro","Pardo"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);
//        //commit teste
        iniciar = (Button) findViewById(R.id.iniciar);
        iniciar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                EditText editText = (EditText)findViewById(R.id.nome);
                String nome = editText.getText().toString();

                EditText idadeText = (EditText)findViewById(R.id.idade);
                String idade = idadeText.getText().toString();

                Spinner sexoText = (Spinner)findViewById(R.id.sexo);
                String sexo = sexoText.getSelectedItem().toString();

                EditText pesoText = (EditText)findViewById(R.id.peso);
                String peso = pesoText.getText().toString();

                EditText alturaText = (EditText)findViewById(R.id.altura);
                String altura = alturaText.getText().toString();

                Spinner etniaText = (Spinner)findViewById(R.id.etnia);
                String etnia = etniaText.getSelectedItem().toString();

                EditText duracaoText = (EditText)findViewById(R.id.duracao);
                String duracao = duracaoText.getText().toString();

                EditText passosText = (EditText)findViewById(R.id.numpassos);
                String passos = passosText.getText().toString();


                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date data = new Date();

                String texto = "Dados do participante:\n \nNome: "+nome+"\nIdade: "+idade+"\n \nColeta de dados realizada no dia:"+df.format(data);

                String cabecalho = "Nome:,"+nome+"\n";
                cabecalho += "Idade:,"+idade+"\n";
                cabecalho +="Sexo:,"+sexo+"\n";
                cabecalho +="Peso:,"+peso+"\n";
                cabecalho +="Altura:,"+altura+"\n";
                cabecalho +="Etnia:,"+etnia+"\n";
                cabecalho +="Duração Máxima:,"+duracao+" minutos\n";
                cabecalho +="Qtd.Max. Passos:,"+passos+"\n";
                cabecalho +="Data:,"+df.format(data)+"\n";

                Intent myIntent = new Intent(view.getContext(),Preparacao.class);
                myIntent.putExtra("infoPessoal",texto);
                myIntent.putExtra("cabecalho",cabecalho);
                myIntent.putExtra("passos", passos);
                myIntent.putExtra("duracao",duracao);

                startActivity(myIntent);


            }
        });
    }

}
