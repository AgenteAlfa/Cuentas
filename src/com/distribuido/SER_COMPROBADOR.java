package com.distribuido;

import com.distribuido.Cliente.Cliente;
import com.distribuido.Cliente.Comprobador;

public class SER_COMPROBADOR {

    public static void main(String[] args)
    {

        //CAMBIAR LA LETRA
        Comprobador C  = new Comprobador(100, "Z");
        C.start();
    }

}
