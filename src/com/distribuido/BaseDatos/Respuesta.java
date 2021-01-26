package com.distribuido.BaseDatos;

public class Respuesta {
    public String Tipo;
    public String Codigo;
    public String De;
    public int MontoDe;
    public String Para;
    public int MontoPara;

    public Respuesta(String cadena) {
        String[] data = cadena.split(",");
        Tipo = data[0];
        Codigo = data[1];
        De = data[2];
        MontoDe = Integer.parseInt(data[3]);
        Para = data[4];
        MontoPara = Integer.parseInt(data[5]);
    }
    public Respuesta(String ... data) {
        Tipo = data[0];
        Codigo = data[1];
        De = data[2];
        MontoDe = Integer.parseInt(data[3]);
        Para = data[4];
        MontoPara = Integer.parseInt(data[5]);
    }

    public Respuesta() {
    }

    public Respuesta(String tipo, String codigo, String de, int montoDe, String para, int montoPara) {
        Tipo = tipo;
        Codigo = codigo;
        De = de;
        MontoDe = montoDe;
        Para = para;
        MontoPara = montoPara;
    }

    public String toString()
    {
        return  Tipo +
                ',' +
                Codigo +
                ',' +
                De +
                ',' +
                MontoDe +
                ',' +
                Para +
                ',' +
                MontoPara;
    }
}
