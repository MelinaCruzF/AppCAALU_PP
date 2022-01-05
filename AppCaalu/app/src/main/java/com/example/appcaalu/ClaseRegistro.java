package com.example.appcaalu;

public class ClaseRegistro {
    private String  fecha, noregistro, nocontrol, nombre, apellidopaterno, apellidomaterno, grupo,
    horaingreso, temperingreso, sintomas, contacto, horasalida, tempersalida, observaciones;

    public ClaseRegistro() {
    }

    public ClaseRegistro(String fecha, String noregistro, String nocontrol, String nombre,
                         String apellidopaterno, String apellidomaterno, String grupo, String horaingreso,
                         String temperingreso, String sintomas, String contacto, String horasalida,
                         String tempersalida, String observaciones) {
        this.fecha = fecha;
        this.noregistro = noregistro;
        this.nocontrol = nocontrol;
        this.nombre = nombre;
        this.apellidopaterno = apellidopaterno;
        this.apellidomaterno = apellidomaterno;
        this.grupo = grupo;
        this.horaingreso = horaingreso;
        this.temperingreso = temperingreso;
        this.sintomas = sintomas;
        this.contacto = contacto;
        this.horasalida = horasalida;
        this.tempersalida = tempersalida;
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNoregistro() {
        return noregistro;
    }

    public boolean setNoregistro(String noregistro) {
        this.noregistro = noregistro;
        return true;
    }

    public String getNocontrol() {
        return nocontrol;
    }

    public void setNocontrol(String nocontrol) {
        this.nocontrol = nocontrol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getHoraingreso() {
        return horaingreso;
    }

    public void setHoraingreso(String horaingreso) {
        this.horaingreso = horaingreso;
    }

    public String getTemperingreso() {
        return temperingreso;
    }

    public void setTemperingreso(String temperingreso) {
        this.temperingreso = temperingreso;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getHorasalida() {
        return horasalida;
    }

    public void setHorasalida(String horasalida) {
        this.horasalida = horasalida;
    }

    public String getTempersalida() {
        return tempersalida;
    }

    public void setTempersalida(String tempersalida) {
        this.tempersalida = tempersalida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
