package com.example.asmevetapk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class citas extends AppCompatActivity {

    private EditText etUserName, etDateTime;
    private Button btnScheduleAppointment;
    private FloatingActionButton floatingActionButton2;
    private Spinner spinnerTime;

    // Autenticación de Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Base de datos Realtime Database
    private DatabaseReference databaseReference;

    // Almacenar el ID de la cita seleccionada para actualizar
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Verificar si el usuario está autenticado
        if (currentUser == null) {
            Intent loginIntent = new Intent(citas.this, MainActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Inicializar base de datos Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("citas");

        // Inicializar elementos de la UI
        etUserName = findViewById(R.id.etUserName);
        etDateTime = findViewById(R.id.etDateTime);
        spinnerTime = findViewById(R.id.spinnerTime); // Inicializar el Spinner
        btnScheduleAppointment = findViewById(R.id.btnScheduleAppointment);
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);

        // Crear la lista de horas
        String[] timeSlots = createTimeSlots();

        // Configurar el adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);

        // Configurar el DatePickerDialog para seleccionar la fecha
        etDateTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(citas.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        etDateTime.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

// Configurar el listener para el botón "Agendar Cita"
        btnScheduleAppointment.setOnClickListener(v -> {
            String userName = etUserName.getText().toString();
            String dateTime = etDateTime.getText().toString();
            String selectedTime = spinnerTime.getSelectedItem().toString(); // Obtener la hora seleccionada

            // Validar campos
            if (userName.isEmpty() || dateTime.isEmpty()) {
                Toast.makeText(citas.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear un mapa para guardar los datos en Realtime Database
            Map<String, Object> appointment = new HashMap<>();
            appointment.put("userName", userName);
            appointment.put("dateTime", dateTime + " " + selectedTime); // Guardar fecha y hora
            appointment.put("userId", currentUser.getUid());  // Guardar el ID del usuario autenticado

            // Comprobar si appointmentId existe para actualizar o crear
            if (appointmentId == null) {
                // Generar una clave única para la cita
                appointmentId = databaseReference.push().getKey();
            }

            // Guardar o actualizar la cita en la base de datos
            if (appointmentId != null) {
                databaseReference.child(appointmentId).setValue(appointment)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(citas.this, "Cita agendada con éxito", Toast.LENGTH_SHORT).show();
                            // Limpiar los campos después de agendar la cita
                            etUserName.setText("");
                            etDateTime.setText("");
                            spinnerTime.setSelection(0); // Restablecer el Spinner a la primera opción
                            // Deshabilitar el Spinner después de agendar la cita
                            spinnerTime.setEnabled(false);
                        })
                        .addOnFailureListener(e -> Toast.makeText(citas.this, "Error al agendar cita: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // Configurar el listener para el FloatingActionButton
        floatingActionButton2.setOnClickListener(v -> {
            Intent intent = new Intent(citas.this, HomePage.class);
            startActivity(intent);
        });
    }

    // Método para crear la lista de horas
    private String[] createTimeSlots() {
        String[] timeSlots = new String[20]; // 20 intervalos de media hora
        int index = 0;

        // Horas de 8 AM a 12 PM
        for (int hour = 8; hour <= 12; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                timeSlots[index++] = String.format("%02d:%02d %s", hour, minute, (hour < 12 ? "AM" : "PM"));
            }
        }

        // Horas de 2 PM a 6 PM
        for (int hour = 14; hour <= 18; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                timeSlots[index++] = String.format("%02d:%02d PM", hour, minute);
            }
        }

        return timeSlots;
    }
}