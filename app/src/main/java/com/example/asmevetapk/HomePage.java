package com.example.asmevetapk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    private Button buttonAgenda, buttonUrgencias, buttonMisMascotas, buttonConsejos;
    private ImageView imageViewLogo;
    private TextView textViewNombre;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        imageViewLogo = findViewById(R.id.imageView2);
        textViewNombre = findViewById(R.id.azmevet);
        buttonAgenda = findViewById(R.id.btn_agenda);
        buttonUrgencias = findViewById(R.id.btn_urgencias);
        buttonMisMascotas = findViewById(R.id.btn_mis_mascotas);
        buttonConsejos = findViewById(R.id.btn_consejos);

        buttonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, citas.class);
                startActivity(intent);
            }
        });

        buttonUrgencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, urgencias.class);
                startActivity(intent);
            }
        });

        buttonMisMascotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, mismascotas.class);
                startActivity(intent);
            }
        });

        buttonConsejos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, consejos.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            cerrarSesion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion() {
        // Lógica para cerrar sesión
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }
}