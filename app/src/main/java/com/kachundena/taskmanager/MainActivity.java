package com.kachundena.taskmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Context;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


import com.google.gson.Gson;
import com.kachundena.taskmanager.controllers.TasksController;
import com.kachundena.taskmanager.modelos.Task;

import static android.os.Environment.getExternalStorageDirectory;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private List<Task> listaDeTasks;
    private RecyclerView recyclerView;
    private AdaptadorTasks adaptadorTasks;
    private TasksController tasksController;
    private FloatingActionButton fabAgregarTask;
    final Context context = this;


    public static final int PICKFILE_RESULT_CODE = 1;

    private Uri fileUri;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Ojo: este código es generado automáticamente, pone la vista y ya, pero
        // no tiene nada que ver con el código que vamos a escribir
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Lo siguiente sí es nuestro ;)
        // Definir nuestro controlador
        tasksController = new TasksController(MainActivity.this);

        // Instanciar vistas
        recyclerView = findViewById(R.id.recyclerViewTasks);
        fabAgregarTask = findViewById(R.id.fabAgregarTask);


        // Por defecto es una lista vacía,
        // se la ponemos al adaptador y configuramos el recyclerView
        listaDeTasks = new ArrayList<>();
        adaptadorTasks = new AdaptadorTasks(listaDeTasks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaptadorTasks);

        // Una vez que ya configuramos el RecyclerView le ponemos los datos de la BD
        refrescarListaDeTasks();

        // Listener de los clicks en la lista, o sea el RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                // Pasar a la actividad EditarMascotaActivity.java
                Task taskSeleccionada = listaDeTasks.get(position);
                Intent intent = new Intent(MainActivity.this, EditarTaskActivity.class);
                intent.putExtra("idTask", taskSeleccionada.getTask_id());
                intent.putExtra("denoTask", taskSeleccionada.getDeno());
                intent.putExtra("detalleTask", taskSeleccionada.getDetalle());
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {
                final Task taskParaEliminar = listaDeTasks.get(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(MainActivity.this)
                        .setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tasksController.eliminarTask(taskParaEliminar);
                                refrescarListaDeTasks();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar la tarea " + taskParaEliminar.getDeno() + "?")
                        .create();
                dialog.show();

            }
        }));

        // Listener del FAB
        fabAgregarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente cambiamos de actividad
                Intent intent = new Intent(MainActivity.this, AgregarTaskActivity.class);
                startActivity(intent);
            }
        });

        // Créditos
        fabAgregarTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Acerca de")
                        .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogo, int which) {
                                dialogo.dismiss();
                            }
                        })
                        .setPositiveButton("Sitio web", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentNavegador = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kachundena.com"));
                                startActivity(intentNavegador);
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.acercade:
                Context contexta = getApplicationContext();
                CharSequence texta = "Acerca de ...";
                int durationa = Toast.LENGTH_SHORT;

                Toast toasta = Toast.makeText(contexta, texta, durationa);
                toasta.show();
                return true;
            case R.id.importdata:
                Context contexti = getApplicationContext();
                //Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath());
                //chooseFile.setDataAndType(uri, "*/*");
                //chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                //startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                importarJson();
                CharSequence texti = "Importar datos";
                int durationi = Toast.LENGTH_SHORT;

                Toast toasti = Toast.makeText(contexti, texti, durationi);
                toasti.show();
                return true;
            case R.id.exportdata:
                Context contexte = getApplicationContext();
                exportarJson();
                CharSequence texte = "Exportar datos";
                int duratione = Toast.LENGTH_SHORT;
                Toast toaste = Toast.makeText(contexte, texte, duratione);
                toaste.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refrescarListaDeTasks();
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case PICKFILE_RESULT_CODE:
//                if (resultCode == -1) {
//                    fileUri = data.getData();
//                    filePath = fileUri.getPath();
//                    if (!filePath.isEmpty()) {
//                        if (filePath.startsWith("/document/raw:")) {
//                            filePath.replaceFirst("/document/raw:", "");
//                        }
//                    }
//
//                    //importarJson(filePath);
//                }
//
//                break;
//        }
//    }

    public void refrescarListaDeTasks() {
        /*
         * ==========
         * Justo aquí obtenemos la lista de la BD
         * y se la ponemos al RecyclerView
         * ============
         *
         * */
        if (adaptadorTasks == null) return;
        listaDeTasks = tasksController.obtenerTasks();
        adaptadorTasks.setListaDeTasks(listaDeTasks);
        adaptadorTasks.notifyDataSetChanged();
    }

    protected void StringToJSON (File fileJSON, String valor) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // start runtime permission
                Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission){
                    //Log.e(TAG, "get permision   ");
                    ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }else {
                    //Log.e(TAG, "get permision-- already granted ");
                }
            }else {
                //readfile();
            }
            fileJSON.createNewFile();
            FileWriter fileWriter = new FileWriter(fileJSON);
            valor = "{\n" +
                    "    \"tasks\": " + valor + "\n" +
                    "}";
            fileWriter.write(valor);
            //System.out.println(valor);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String JSONToString(File fileJSON) {
        try {
            String retorno = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // start runtime permission
                Boolean hasPermission =( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission){
                    //Log.e(TAG, "get permision   ");
                    ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }else {
                    //Log.e(TAG, "get permision-- already granted ");
                }
            }else {
                //readfile();
            }
            FileInputStream fis = new FileInputStream(fileJSON);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                retorno = retorno + strLine;
            }
            in.close();
            return retorno;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }


    protected void importarJson() {
        try {
            // get JSONObject from JSON file
            File fileData = null;
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/tasks/");
            if(dir.exists()) {
                fileData = new File(dir, "tasks.json");
            }
            JSONObject obj = new JSONObject(JSONToString(fileData));
            // fetch JSONArray named users
            JSONArray Jtasks = obj.getJSONArray("tasks");
            // implement for loop for getting users list data
            String deno, detalle;
            tasksController.eliminarAllTask();
            for (int i = 0; i < Jtasks.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject Jtask = Jtasks.getJSONObject(i);
                // fetch email and name and store it in arraylist
                deno = Jtask.getString("deno");
                detalle = Jtask.getString("detalle");
                Task nuevatask = new Task(deno, detalle);
                tasksController.nuevaTask(nuevatask);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void exportarJson() {
        try {
            // Get All Tasks for export
            ArrayList<Task> tasks = tasksController.obtenerTasks();

            // Convert Class to String JSON
            Gson gson = new Gson();
            String json = gson.toJson(tasks);



            // get JSONObject from JSON file
            File fileData = null;
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/tasks/");
            if (dir.exists()) {
                fileData = new File(dir, "tasks.json");
            }
            // Write to file
            StringToJSON(fileData, json);


//            JSONObject obj = new JSONObject(JSONToString(fileData));
//            // fetch JSONArray named users
//            JSONArray Jtasks = obj.getJSONArray("tasks");
//            // implement for loop for getting users list data
//            String deno, detalle;
//            tasksController.eliminarAllTask();
//            for (int i = 0; i < Jtasks.length(); i++) {
//                // create a JSONObject for fetching single user data
//                JSONObject Jtask = Jtasks.getJSONObject(i);
//                // fetch email and name and store it in arraylist
//                deno = Jtask.getString("deno");
//                detalle = Jtask.getString("detalle");
//                Task nuevatask = new Task(deno, detalle);
//                tasksController.nuevaTask(nuevatask);
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected File getFileJSON(File myExternalFile, String szFilePath) {
        String filename = "tasks.json";
        String filepath = "Download";
        String retorno = "";
        String myData = "";
        if (!szFilePath.isEmpty()) {
            if (szFilePath.startsWith("/document/raw:")) {
                retorno = szFilePath.replace("/document/raw:", "");
                //szFilePath.replaceFirst("/document/raw:", "");
            }
            else {
                retorno = szFilePath;
            }
        }
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            myExternalFile = null;
        } else {
            //myExternalFile = new File(getExternalFilesDir(filepath), filename);
            myExternalFile = new File(retorno);
        }
        return myExternalFile;
    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
        return true;
        }
        return false;
        }

        private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
        return true;
        }
        return false;
        }

}
