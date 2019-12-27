package com.kachundena.taskmanager.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.kachundena.taskmanager.AyudanteBaseDeDatos;
import com.kachundena.taskmanager.modelos.Task;

public class TasksController {
    private AyudanteBaseDeDatos ayudanteBaseDeDatos;
    private String NOMBRE_TABLA = "tasks";

    public TasksController(Context contexto) {
        ayudanteBaseDeDatos = new AyudanteBaseDeDatos(contexto);
    }


    public int eliminarTask(Task task) {

        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        String[] argumentos = {String.valueOf(task.getTask_id())};
        return baseDeDatos.delete(NOMBRE_TABLA, "task_id = ?", argumentos);
    }

    public int eliminarAllTask() {

        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        return baseDeDatos.delete(NOMBRE_TABLA, null, null);
    }

    public long nuevaTask(Task task) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("deno", task.getDeno());
        valoresParaInsertar.put("detalle", task.getDetalle());
        return baseDeDatos.insert(NOMBRE_TABLA, null, valoresParaInsertar);
    }

    public int guardarCambios(Task taskEditada) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("deno", taskEditada.getDeno());
        valoresParaActualizar.put("detalle", taskEditada.getDetalle());
        // where id...
        String campoParaActualizar = "task_id = ?";
        // ... = idMascota
        String[] argumentosParaActualizar = {String.valueOf(taskEditada.getTask_id())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Task> obtenerTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();
        // SELECT deno, detalle, task_id
        String[] columnasAConsultar = {"deno", "detalle", "task_id"};
        Cursor cursor = baseDeDatos.query(
                NOMBRE_TABLA,//from tasks
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            /*
                Salimos aquí porque hubo un error, regresar
                lista vacía
             */
            return tasks;

        }
        // Si no hay datos, igualmente regresamos la lista vacía
        if (!cursor.moveToFirst()) return tasks;

        // En caso de que sí haya, iteramos y vamos agregando los
        // datos a la lista de tasks
        do {
            // El 0 es el número de la columna, como seleccionamos
            // deno, detalle,task_id entonces el deno es 0, detalle 1 e task_id es 2
            String denoObtenidoDeBD = cursor.getString(0);
            String detalleObtenidaDeBD = cursor.getString(1);
            int taskid = cursor.getInt(2);
            Task taskObtenidaDeBD = new Task(taskid, denoObtenidoDeBD, detalleObtenidaDeBD);
            tasks.add(taskObtenidaDeBD);
        } while (cursor.moveToNext());

        // Fin del ciclo. Cerramos cursor y regresamos la lista de tasks :)
        cursor.close();
        return tasks;
    }
}