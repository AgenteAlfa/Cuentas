package com.distribuido;

import com.distribuido.BaseDatos.BaseDatos;

import java.io.IOException;

public class SER_5_BASE_DE_DATOS {

    public static void main(String[] args)
    {
        try {
            BaseDatos BD1 = new BaseDatos();
            BD1.Iniciar();

            BaseDatos BD2 = new BaseDatos();
            BD2.Iniciar();

            BaseDatos BD3 = new BaseDatos();
            BD3.Iniciar();

            BaseDatos BD4 = new BaseDatos();
            BD4.Iniciar();

            BaseDatos BD5 = new BaseDatos();
            BD5.Iniciar();

        } catch (IOException e) {
            System.out.println("Error al iniciar la BD");
            e.printStackTrace();
        }

    }

}
