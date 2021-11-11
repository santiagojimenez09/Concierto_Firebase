package com.example.concierto_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView jtvasiento,jtvtotal;
    EditText jetcodigo,jetcantidad,jetlicor,jetvalor;
    Button jbtguardar,jbtconsultar,jbtmodificar,jbteliminar,jbtlimpiar;
    RadioButton jrbpalco,jrbplatea,jrbgeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        jtvasiento=findViewById(R.id.tvasiento);
        jtvtotal=findViewById(R.id.tvtotal);
        jetcodigo=findViewById(R.id.etcodigo);
        jetcantidad=findViewById(R.id.etcantidad);
        jetlicor=findViewById(R.id.etlicor);
        jetvalor=findViewById(R.id.etvalor);
        jbtguardar=findViewById(R.id.btguardar);
        jbtconsultar=findViewById(R.id.btconsultar);
        jbtmodificar=findViewById(R.id.btmodificar);
        jbteliminar=findViewById(R.id.bteliminar);
        jbtlimpiar=findViewById(R.id.btlimpiar);
        jrbpalco=findViewById(R.id.rbpalco);
        jrbplatea=findViewById(R.id.rbplatea);
        jrbgeneral=findViewById(R.id.rbgeneral);
    }

    public void calcular_total(){
        String codigo,cantidad,licor,valor;
        codigo=jetcodigo.getText().toString();
        cantidad=jetcantidad.getText().toString();
        licor=jetlicor.getText().toString();
        valor=jetvalor.getText().toString();
        if (codigo.isEmpty() || cantidad.isEmpty() || licor.isEmpty() || valor.isEmpty()){
            Toast.makeText(this,"Ingrese todos lo datos",Toast.LENGTH_LONG).show();
            jetcodigo.requestFocus();
        }else {

        }
    }
}