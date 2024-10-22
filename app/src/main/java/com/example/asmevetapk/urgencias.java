package com.example.asmevetapk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class urgencias extends AppCompatActivity {
    private Button btnCallEmergency;
    private Button btnSubmit;
    private EditText etAdditionalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgencias); // Asegúrate de que el nombre del layout sea correcto

        btnCallEmergency = findViewById(R.id.btnCallEmergency);
        btnSubmit = findViewById(R.id.btnSubmit);
        etAdditionalInfo = findViewById(R.id.etAdditionalInfo);

        // Configurar el botón para llamar a emergencias
        btnCallEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarEmergencias();
            }
        });

        // Configurar el botón para enviar información adicional
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarInformacion();
            }
        });
    }

    private void llamarEmergencias() {
        // Número de emergencia (ejemplo: 911)
        String numeroEmergencia = "911";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + numeroEmergencia));
        startActivity(intent);
    }

    private void enviarInformacion() {
        String infoAdicional = etAdditionalInfo.getText().toString().trim();
        if (!infoAdicional.isEmpty()) {
            // Aquí puedes implementar la lógica para enviar la información adicional
            // Por ejemplo, enviarla a un servidor o guardarla en una base de datos
            Toast.makeText(this, "Información enviada: " + infoAdicional, Toast.LENGTH_SHORT).show();
            etAdditionalInfo.setText(""); // Limpiar el campo de texto
        } else {
            Toast.makeText(this, "Por favor, introduce información adicional.", Toast.LENGTH_SHORT).show();
        }
    }
}