package com.distribuido.ManejoCSV;

public class Casilla {

    private String Data;
    private Casilla Izq,Der,Arr,Aba;
    private int[] Posicion;

    public Casilla(String data,int ... puntos) {
        Data = data;
        Posicion = new int[2];
        Posicion[0] = puntos[0];
        Posicion[1] = puntos[0];
        Izq = null;
        Der = null;
        Arr = null;
        Aba = null;
    }

    public void setIzq(Casilla izq) {
        Izq = izq;
    }

    public void setDer(Casilla der) {
        Der = der;
    }

    public void setArr(Casilla arr) {
        Arr = arr;
    }

    public void setAba(Casilla aba) {
        Aba = aba;
    }

    public Casilla getIzq() {
        return Izq;
    }

    public Casilla getDer() {
        return Der;
    }

    public Casilla getArr() {
        return Arr;
    }

    public Casilla getAba() {
        return Aba;
    }

    public int[] getPosicion() {
        return Posicion;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public boolean Verificar(){
        return !Data.contains(",");
    }



}