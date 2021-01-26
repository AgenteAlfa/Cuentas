package com.distribuido.BaseDatos;

public class Transaccion {
    public String Tipo;
    public String Codigo;
    public String De;
    public String Para;
    public int Monto;

    public Transaccion(String cadena) {
        String[] data = cadena.split(",");
        Tipo = data[0];
        Codigo = data[1];
        De = data[2];
        Para = data[3];
        Monto = Integer.parseInt(data[4]);
    }
    public Transaccion(String ... data) {
        Tipo = data[0];
        Codigo = data[1];
        De = data[2];
        Para = data[3];
        Monto = Integer.parseInt(data[4]);
    }

    public Transaccion() {
    }

    public Transaccion(String tipo, String codigo, String de, String para, int monto) {
        Tipo = tipo;
        Codigo = codigo;
        De = de;
        Para = para;
        Monto = monto;
    }

    public String toString()
    {
        return  Tipo +
                ',' +
                Codigo +
                ',' +
                De +
                ',' +
                Para +
                ',' +
                Monto;
    }
}
