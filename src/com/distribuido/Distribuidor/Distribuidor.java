package com.distribuido.Distribuidor;

import com.distribuido.BaseDatos.BDConfiguracion;
import com.distribuido.BaseDatos.BaseDatos;
import com.distribuido.Comunicacion;
import com.distribuido.Configuracion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Distribuidor {

    ArrayList<DBD> BDs ;
    private Hilo_Esperador HE;
    private Hilo_Escuchador mHilo_Escuchador;
    private ServerSocket SSOrdenador;
    private ServerSocket SSRespondedor;

    public Distribuidor() throws IOException {

        SSOrdenador = new ServerSocket(Configuracion.PUERTO_ORDENES);
        SSRespondedor = new ServerSocket(Configuracion.PUERTO_RESPUESTAS);
        SSOrdenador.setSoTimeout(50);
        SSRespondedor.setSoTimeout(50);

        BDs = new ArrayList<>();
    }
    public void Esperar()
    {
        HE = new Hilo_Esperador();
        HE.start();
    }

    public void Empezar()
    {
        HE.Apagar();
        mHilo_Escuchador = new Hilo_Escuchador();
        mHilo_Escuchador.start();


        if (BDs.size() > 0)
        {
            Random R = new Random();

            for (int i = 0; i <100; i++) {
                String id1 =  "0" + Integer.toHexString(R.nextInt(BDConfiguracion.N) + BDConfiguracion.MIN_INDEX);
                String id2 =  "0" + Integer.toHexString(R.nextInt(BDConfiguracion.N) + BDConfiguracion.MIN_INDEX);
                //System.out.println(id);
                //Tipo;
                //Codigo;
                //De;
                //Para;
                //Monto;

                //System.out.println("D : Enviando orden");
                String cadena = R.nextInt(100) > 30 ? "L,":"A,";
                cadena +=        "A" + i + ","
                        + id1 + ","
                        + id2 + ","
                        + R.nextInt(150);

                for (DBD datos : BDs) {

                    datos.Ordenar(BDConfiguracion.ORDEN_ESCRIBIR, cadena);

                }
                VerificarTodos();
            }

            for (DBD datos : BDs)
            {
                //datos.Ordenar(BDConfiguracion.ORDEN_APAGAR,"");
                //datos.Cerrar();
            }




        }
    }

    private void VerificarTodos()
    {
        //System.out.println("D: Esperando verificacion");
        for (DBD datos : BDs) {

            datos.EsperarConfirmacion();
            //System.out.println("Verificado");
        }
    }

    private class Hilo_Esperador extends Thread
    {

        private void Apagar(){
            //System.out.println("ESPERADOR : DEJO DE ESPERAR DISTRIBUIDORES");
            Encendido = false;  }
        private boolean Encendido = true;

        @Override
        public void run() {
            while(Encendido)
            {
                //System.out.println("Coordinador -> Estoy esperando distribuidores");
                try {;
                    BDs.add( new DBD( SSOrdenador.accept(),SSRespondedor.accept() ) );
                    System.out.println("A ingresado una BD");
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
            //System.out.println("Yo ya termine -> ESPERADOR");
        }
    }

    private class Hilo_Escuchador extends Thread
    {

        private void Apagar(){
            //System.out.println("ESPERADOR : DEJO DE ESPERAR DISTRIBUIDORES");
            Encendido = false;  }
        private boolean Encendido = true;

        @Override
        public void run() {
            while(Encendido)
            {
                for (DBD datos : BDs) {
                    String S = datos.EscucharRespuesta();
                    System.out.println("ESCUCHADO : " + S);
                    if (S.equals("-1"))
                        break;
                }
            }
        }
    }

}
