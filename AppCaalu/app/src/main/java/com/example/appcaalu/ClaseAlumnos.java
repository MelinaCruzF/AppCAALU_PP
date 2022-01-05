package com.example.appcaalu;

public class ClaseAlumnos {

    private String nocontrol, nombres, apellidopaterno, apellidomaterno, grupo, especialidad;

    public ClaseAlumnos() {
    }

    public ClaseAlumnos(String nocontrol, String nombres, String apellidopaterno, String apellidomaterno,
                        String grupo, String especialidad) {
        this.nocontrol = nocontrol;
        this.nombres = nombres;
        this.apellidopaterno = apellidopaterno;
        this.apellidomaterno = apellidomaterno;
        this.grupo = grupo;
        this.especialidad = especialidad;
    }

    public <T> ClaseAlumnos(T value) {
    }

    public String getNocontrol() {
       return nocontrol;
    }

    public void setNocontrol(String nocontrol) {
      this.nocontrol = nocontrol;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidopaterno() {
        return apellidopaterno;
    }

    public void setApellidopaterno(String apellidopaterno) {
        this.apellidopaterno = apellidopaterno;
    }

    public String getApellidomaterno() {
        return apellidomaterno;
    }

    public void setApellidomaterno(String apellidomaterno) {
        this.apellidomaterno = apellidomaterno;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return especialidad;
    }
}