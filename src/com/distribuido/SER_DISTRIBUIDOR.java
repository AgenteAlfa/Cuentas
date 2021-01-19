package com.distribuido;

import com.distribuido.Distribuidor.Distribuidor;

import java.io.IOException;
import java.util.Scanner;

public class SER_DISTRIBUIDOR {

    public static void main(String[] args)
    {
        try {
            Distribuidor D = new Distribuidor();
            D.Esperar();
            System.out.println("Estoy esperando, presiona una tecla");
            Scanner SC = new Scanner(System.in);
            SC.next();
            D.Empezar();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
