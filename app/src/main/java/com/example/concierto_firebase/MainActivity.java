package com.example.concierto_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView jtvasiento,jtvtotal;
    EditText jetcodigo,jetcantidad,jetlicor,jetvalor;
    Button jbtguardar,jbtconsultar,jbtmodificar,jbteliminar,jbtlimpiar;
    RadioButton jrbpalco,jrbplatea,jrbgeneral;
    String codigo,cantidad,licor,valor,tipo,idconcierto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="MsgCon";
    String collection="concierto";

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
        codigo=jetcodigo.getText().toString();
        cantidad=jetcantidad.getText().toString();
        licor=jetlicor.getText().toString();
        valor=jetvalor.getText().toString();
        if(jrbpalco.isChecked()){
            tipo="palco";
            jtvasiento.setText("1700000");
        }else{
            if (jrbplatea.isChecked()){
                tipo="platea";
                jtvasiento.setText("120000");
            }else{
                tipo="general";
                jtvasiento.setText("80000");
            }
        }
        if (codigo.isEmpty() || cantidad.isEmpty() || licor.isEmpty() || valor.isEmpty()){
            Toast.makeText(this,"Ingrese todos lo datos",Toast.LENGTH_LONG).show();
            jetcodigo.requestFocus();
        }else {
            int cantidad_personas,valor_licor,valor_caja,total=0,valor_tipo;
            cantidad_personas=Integer.parseInt(cantidad);
            valor_licor=Integer.parseInt(licor);
            valor_caja=Integer.parseInt(valor);
            valor_tipo=Integer.parseInt(jtvasiento.getText().toString());
            if(cantidad_personas>10){
                Toast.makeText(this,"el palco no puede tener mas de 10 personas",Toast.LENGTH_LONG).show();
                jetcantidad.requestFocus();
            }else{
                if (tipo.equals("palco"))
                    total=1700000 + (valor_caja * valor_licor);
                else
                    total=(cantidad_personas * valor_tipo) + (valor_caja * valor_licor);
            }
            jtvtotal.setText(String.valueOf(total));
        }
    }

    public void Guardar(View view){
        calcular_total();
        Map<String, Object> user = new HashMap<>();
        user.put("nro_boleta",codigo);
        user.put("tipo",tipo);
        user.put("cantidad",cantidad);
        user.put("licor",licor);
        user.put("valor",valor);

        db.collection("concienrto")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"Documento adicionado"+documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "Error adicionando registro",e);
                    }
                });
        limpiar_campos();
    }

    public void Asiento(View view){
        calcular_total();
    }

    public void Limpiar(View view){
        limpiar_campos();
    }

    private void limpiar_campos(){
        jetcodigo.setText("");
        jrbpalco.setChecked(true);
        jtvasiento.setText("1700000");
        jetcantidad.setText("");
        jetlicor.setText("");
        jetvalor.setText("");
        jtvtotal.setText("0");
        jetcodigo.requestFocus();
    }

    public void Consultar(View view){
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this,"El codigo es requerido para la consulta",Toast.LENGTH_LONG).show();
            jetcodigo.requestFocus();
        }else{
            db.collection("concienrto")
                    .whereEqualTo("nro_boleta",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Log.i("INFO",document.toString());
                                    idconcierto=document.getId();
                                    if (document.getString("tipo").equals("palco")){
                                        jrbpalco.setChecked(true);
                                        jtvasiento.setText("1700000");
                                    }else{
                                        if (document.getString("tipo").equals("platea")){
                                            jrbplatea.setChecked(true);
                                            jtvasiento.setText("120000");
                                        }else{
                                            jrbgeneral.setChecked(true);
                                            jtvasiento.setText("80000");
                                        }
                                    }
                                    jetcantidad.setText(document.getString("cantidad"));
                                    jetlicor.setText(document.getString("licor"));
                                    jetvalor.setText(document.getString("valor"));
                                    calcular_total();
                                }
                            }else{
                                Toast.makeText(MainActivity.this,"Error consultando el registro",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void Modificar(View view){
        calcular_total();
        Map<String, Object> user = new HashMap<>();
        user.put("nro_boleta",codigo);
        user.put("tipo",tipo);
        user.put("cantidad",cantidad);
        user.put("licor",licor);
        user.put("valor",valor);

        db.collection(collection).document(idconcierto)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Concierto actualizado correctamente...",Toast.LENGTH_LONG).show();
                        limpiar_campos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error actualizado correctamente...",Toast.LENGTH_LONG).show();
                        limpiar_campos();
                    }
                });
    }

    public void Eliminar(View view){
        String codigo;
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this,"El codigo es requerido",Toast.LENGTH_LONG).show();
            jetcodigo.requestFocus();
        }else{
            db.collection(collection).document(idconcierto)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this,"Concierto eliminado correctamente",Toast.LENGTH_LONG).show();
                            limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error eliminado correctamente",Toast.LENGTH_LONG).show();
                            limpiar_campos();
                        }
                    });
        }
    }

}
