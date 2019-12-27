package com.kachundena.taskmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.kachundena.taskmanager.modelos.Task;

public class AdaptadorTasks extends RecyclerView.Adapter<AdaptadorTasks.MyViewHolder> {

    private List<Task> listaDeTasks;

    public void setListaDeTasks(List<Task> listaDeTasks) {

        this.listaDeTasks = listaDeTasks;
    }

    public AdaptadorTasks(List<Task> tasks) {

        this.listaDeTasks = tasks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View filaTask = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_task, viewGroup, false);
        return new MyViewHolder(filaTask);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        // Obtener la tarea de nuestra lista gracias al índice i
        Task task = listaDeTasks.get(i);

        // Obtener los datos de la lista
        String deno = task.getDeno();
        String detalle = task.getDetalle();
        // Y poner a los TextView los datos con setText
        myViewHolder.deno.setText(deno);
        myViewHolder.detalle.setText(detalle);
    }

    @Override
    public int getItemCount() {
        return listaDeTasks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView deno, detalle;

        MyViewHolder(View itemView) {
            super(itemView);
            this.deno = itemView.findViewById(R.id.tvDeno);
            this.detalle = itemView.findViewById(R.id.tvDetalle);
        }
    }
}
