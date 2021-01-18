package com.distribuido.ManejoCSV;

import java.io.File;

public class Configuracion {
    public static final String Separador = ",";
    public static final char Salto = '\n';
    private static final String NombreArchivo = "DatosCSV";
    private static  File mFile;
    public static void Inicializar()
    {
        mFile = new File("src/data/" + NombreArchivo);
    }

    public static File getmFile() {
        return mFile;
    }
}