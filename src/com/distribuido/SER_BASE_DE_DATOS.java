package com.distribuido;

import com.distribuido.BaseDatos.BaseDatos;

import java.io.IOException;

public class SER_BASE_DE_DATOS {

    public static void main(String[] args)
    {
        try {
            BaseDatos BD = new BaseDatos();
            BD.Iniciar();
            BaseDatos BD2 = new BaseDatos();
            BD2.Iniciar();
        } catch (IOException e) {
            System.out.println("Error al iniciar la BD");
            e.printStackTrace();
        }

    }

}
