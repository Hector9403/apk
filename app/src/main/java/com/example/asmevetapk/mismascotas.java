package com.example.asmevetapk;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class mismascotas extends AppCompatActivity {
    private ListView lvMascotas;
    private Button btnAddPet;
    private ArrayList<String> mascotasList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference mascotasDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismascotas);

        lvMascotas = findViewById(R.id.lvMascotas);
        btnAddPet = findViewById(R.id.btnAddPet);

        // Inicializar la lista de mascotas
        mascotasList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mascotasList);
        lvMascotas.setAdapter(adapter);

        // Inicializar Firebase Realtime Database
        mascotasDatabase = FirebaseDatabase.getInstance().getReference("mascotas");

        // Cargar mascotas existentes desde la base de datos
        cargarMascotas();

        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarMascota();
            }
        });
    }

    private void cargarMascotas() {
        // Aquí puedes implementar la lógica para cargar mascotas desde Firebase Realtime Database
        // Por simplicidad, este método está vacío. Puedes usar un Listener para obtener datos.
    }

    private void agregarMascota() {
        // Crear un layout para el diálogo
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Crear EditTexts para recibir la información de la mascota
        final EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nombre de la mascota");
        layout.addView(inputNombre);

        final EditText inputEdad = new EditText(this);
        inputEdad.setHint("Edad de la mascota (años)");
        layout.addView(inputEdad);

        final EditText inputRaza = new EditText(this);
        inputRaza.setHint("Raza de la mascota");
        layout.addView(inputRaza);

        final EditText inputTipo = new EditText(this);
        inputTipo.setHint("Tipo de animal (perro, gato, etc.)");
        layout.addView(inputTipo);

        // Crear un AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Nueva Mascota")
                .setMessage("Introduce la información de tu mascota:")
                .setView(layout)
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = inputNombre.getText().toString();
                        String edad = inputEdad.getText().toString();
                        String raza = inputRaza.getText().toString();
                        String tipo = inputTipo.getText().toString();

                        if (!nombre.isEmpty() && !edad.isEmpty() && !raza.isEmpty() && !tipo.isEmpty()) {
                            // Crear un objeto de mascota
                            Mascota mascota = new Mascota(nombre, edad, raza, tipo);

                            // Agregar la mascota a Firebase Realtime Database
                            mascotasDatabase.push().setValue(mascota)
                                    .addOnSuccessListener(aVoid -> {
                                        String mascotaInfo = "Nombre: " + nombre + ", Edad: " + edad + " años, Raza: " + raza + ", Tipo: " + tipo;
                                        mascotasList.add(mascotaInfo);
                                        adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                                        Toast.makeText(mismascotas.this, "Mascota agregada: " + nombre, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(mismascotas.this, "Error al agregar mascota: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(mismascotas.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // Cerrar el diálogo sin hacer nada
                    }
                });

        // Mostrar el cuadro de diálogo
        builder.show();
    }

    // Clase interna para representar una mascota
    public static class Mascota {
        public String nombre;
        public String edad;
        public String raza;
        public String tipo;

        public Mascota() {
            // Constructor vacío requerido para Firebase
        }

        public Mascota(String nombre, String edad, String raza, String tipo) {
            this.nombre = nombre;
            this.edad = edad;
            this.raza = raza;
            this.tipo = tipo;
        }
    }
}