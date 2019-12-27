package com.kachundena.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kachundena.taskmanager.controllers.TasksController;
import com.kachundena.taskmanager.modelos.Task;

public class EditarTaskActivity extends AppCompatActivity {
    private EditText etEditarDeno, etEditarDetalle;
    private Button btnGuardarCambios, btnCancelarEdicion;
    private Task task;//La mascota que vamos a estar editando
    private TasksController tasksController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_task);

        // Recuperar datos que enviaron
        Bundle extras = getIntent().getExtras();
        // Si no hay datos (cosa rara) salimos
        if (extras == null) {
            finish();
            return;
        }
        // Instanciar el controlador de las tasks
        tasksController = new TasksController(EditarTaskActivity.this);

        // Rearmar la task
        // Nota: igualmente solamente podríamos mandar el id y recuperar la task de la BD
        int idTask = extras.getInt("idTask");
        String denoTask = extras.getString("denoTask");
        String detalleTask = extras.getString("detalleTask");
        task = new Task(idTask, denoTask, detalleTask);


        // Ahora declaramos las vistas
        etEditarDeno = findViewById(R.id.etEditarDeno);
        etEditarDetalle = findViewById(R.id.etEditarDetalle);
        btnCancelarEdicion = findViewById(R.id.btnCancelarEdicionTask);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambiosTask);


        // Rellenar los EditText con los datos de la tarea
        etEditarDetalle.setText(String.valueOf(task.getDetalle()));
        etEditarDeno.setText(task.getDeno());

        // Listener del click del botón para salir, simplemente cierra la actividad
        btnCancelarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Listener del click del botón que guarda cambios
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remover previos errores si existen
                etEditarDeno.setError(null);
                etEditarDetalle.setError(null);
                // Crear la task con los nuevos cambios pero ponerle
                // el id de la anterior
                String nuevoDeno = etEditarDeno.getText().toString();
                String nuevoDetalle = etEditarDetalle.getText().toString();
                if (nuevoDeno.isEmpty()) {
                    etEditarDeno.setError("Escribe la descripcion");
                    etEditarDeno.requestFocus();
                    return;
                }
                if (nuevoDetalle.isEmpty()) {
                    etEditarDetalle.setError("Escribe el detalle");
                    etEditarDetalle.requestFocus();
                    return;
                }
                // Si llegamos hasta aquí es porque los datos ya están validados
                Task taskConNuevosCambios = new Task(task.getTask_id(), nuevoDeno, nuevoDetalle);
                int filasModificadas = tasksController.guardarCambios(taskConNuevosCambios);
                if (filasModificadas != 1) {
                    // De alguna forma ocurrió un error porque se debió modificar únicamente una fila
                    Toast.makeText(EditarTaskActivity.this, "Error guardando cambios. Intente de nuevo.", Toast.LENGTH_SHORT).show();
                } else {
                    // Si las cosas van bien, volvemos a la principal
                    // cerrando esta actividad
                    finish();
                }
            }
        });
    }
}
