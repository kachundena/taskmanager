# CRUD con Android y SQLite

Aplicación sencilla de lista de tareas. 


Pero la he ampliado con importación y exportación de datos.

# Explicación

Para cada tarea se guardan los siguientes campos:

* id: identificador único. Este campo está oculto y es una secuencia de la tabla
* deno: descripcion corta.
* detalle: descripción larga.

Muestra una lista de tareas. Se pueden crear nuevas tareas, editar las existentes o borrar. La edición y el borrado es individual. 

Se puede importar todos los datos. Ojo porque borra todo los existente. 
Tambien se pueden exportar los datos. 

La importación y exportación lo hace mediante el fichero tasks.json que se encuentra alojado en 
/storage/emulated/0/tasks/  (el directorio debe existir). 


# Base

Esta app es totalmente gratuita y open source,  [**su repositorio lo encuentras aquí**](https://github.com/parzibyte/CRUD-SQLite). Si quieres descargar la app visita la  [**página releases**](https://github.com/parzibyte/CRUD-SQLite/releases).