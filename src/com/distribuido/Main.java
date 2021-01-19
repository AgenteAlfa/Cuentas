package com.distribuido;

import com.distribuido.BaseDatos.BaseDatos;
import com.distribuido.Distribuidor.Distribuidor;
import com.distribuido.ManejoCSV.CSVcontrol;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        try {
            Distribuidor D = new Distribuidor();
            D.Esperar();
            System.out.println("Estoy esperando, presiona una tecla");

            BaseDatos BD = new BaseDatos();
            BD.Iniciar();

            D.Empezar();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
