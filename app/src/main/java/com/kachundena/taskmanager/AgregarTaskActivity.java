package com.kachundena.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kachundena.taskmanager.controllers.TasksController;
import com.kachundena.taskmanager.modelos.Task;


public class AgregarTaskActivity extends AppCompatActivity {
    private Button btnAgregarTask, btnCancelarNuevaTask;
    private EditText etDeno, etDetalle;
    private TasksController tasksController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_task);

        // Instanciar vistas
        etDeno = findViewById(R.id.etDeno);
        etDetalle = findViewById(R.id.etDetalle);
        btnAgregarTask = findViewById(R.id.btnAgregarTask);
        btnCancelarNuevaTask = findViewById(R.id.btnCancelarNuevaTask);
        // Crear el controlador
        tasksController = new TasksController(AgregarTaskActivity.this);

        // Agregar listener del botón de guardar
        btnAgregarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resetear errores a ambos
                etDeno.setError(null);
                etDetalle.setError(null);
                String deno = etDeno.getText().toString(),
                        detalle = etDetalle.getText().toString();
                if ("".equals(deno)) {
                    etDeno.setError("Escribe la descripcion de la tarea");
                    etDeno.requestFocus();
                    return;
                }
                if ("".equals(detalle)) {
                    etDetalle.setError("Escribe el detalle de la tarea");
                    etDetalle.requestFocus();
                    return;
                }

                // Ya pasó la validación
                Task nuevaTask = new Task(deno, detalle);
                long id = tasksController.nuevaTask(nuevaTask);
                if (id == -1) {
                    // De alguna manera ocurrió un error
                    Toast.makeText(AgregarTaskActivity.this, "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
                } else {
                    // Terminar
                    finish();
                }
            }
        });

        // El de cancelar simplemente cierra la actividad
        btnCancelarNuevaTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
