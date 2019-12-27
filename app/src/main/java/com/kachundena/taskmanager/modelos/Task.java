package com.kachundena.taskmanager.modelos;

import java.util.Date;


public class Task {
    private int task_id;
    private String deno;
    private String detalle;

    public Task(String deno, String detalle) {
        this.deno = deno;
        this.detalle = detalle;
    }

    public Task(int task_id, String deno, String detalle) {
        this.task_id = task_id;
        this.deno = deno;
        this.detalle = detalle;
    }

    public Task() {

    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getDeno() {
        return deno;
    }

    public void setDeno(String deno) {
        this.deno = deno;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "id='" + task_id + '\'' +
                ", deno=" + deno + '\'' +
                ", detalle=" + detalle +
                '}';
    }
}
