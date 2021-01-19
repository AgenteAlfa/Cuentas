package com.distribuido.BaseDatos;

import com.distribuido.Comunicacion;
import com.distribuido.Configuracion;
import com.distribuido.ManejoCSV.CSVcontrol;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BaseDatos extends Comunicacion implements Runnable{
    private CSVcontrol Data;
    private Thread Hilo;
    private HiloConsultor mHiloConsultor;
    private BaseDatos ESTE = this;
    private Consultas mConsultas;

    private boolean Activo = true;

    public BaseDatos() throws IOException {
        super(
                new Socket(Configuracion.IP_DISTRIBUIDOR, Configuracion.PUERTO_ORDENES),
                new Socket(Configuracion.IP_DISTRIBUIDOR, Configuracion.PUERTO_RESPUESTAS)
        );
        mConsultas = new Consultas();
        Data = new CSVcontrol();
        Data.Cargar();
    }
    public void Iniciar() {
        //Cerrar();
        Hilo = new Thread(this);
        Hilo.start();
        mHiloConsultor = new HiloConsultor();
        mHiloConsultor.start();
    }

    @Override
    public void run() {

        int Orden;
        String Mensaje;

        while ( (Orden = LeerOrden()) != BDConfiguracion.ORDEN_APAGAR )
        {

            Mensaje = EscucharMensaje();
            //System.out.println("ORDEN " + Orden);
            switch (Orden)
            {
                case BDConfiguracion.ORDEN_DETENER:
                    Activo = false;
                    break;
                case BDConfiguracion.ORDEN_CONTINUAR:
                    Activo = true;
                    break;
                case BDConfiguracion.ORDEN_LEER:
                case BDConfiguracion.ORDEN_ESCRIBIR:
                    mConsultas.Encolar(Mensaje.split(","));
                    //System.out.println("Enviando confirmacion");
                    Confirmar("-10000");
                    break;
            }
        }

        Cerrar();
    }

    private class HiloConsultor extends Thread
    {
        private boolean Encendido = true;
        public void Apagar(){Encendido = false;}

        public boolean isEncendido() {
            return Encendido;
        }

        @Override
        public void run() {
            {


               while(Encendido)
                {
                    if (mConsultas.Cola.size() > 0) {
                        Consulta C = mConsultas.Extraer();
                        //System.out.println("Desencolando datos");
                        StringBuilder builder = new StringBuilder(C.Tipo);
                        builder.append(",");
                        builder.append(C.Codigo);
                        builder.append(",");
                        builder.append(C.De);
                        builder.append(",");
                        //Monto
                        if (C.Tipo.equals("L"))
                            builder.append(Data.BuscarCuenta(C.De));
                        else
                        {
                            Data.Transferencia(C.De,C.Para,C.Monto);

                            builder.append(Data.BuscarCuenta(C.De));
                            builder.append(",");

                            builder.append(C.Para);
                            builder.append(",");
                            builder.append(Data.BuscarCuenta(C.Para));
                            builder.append(",");
                        }

                        //System.out.println("BD : " +  builder.toString());
                        Responder(builder.toString());
                    }
                }
                //Responder("-1");
            }
        }
    }


    private class Consultas
    {

        public LinkedBlockingDeque<Consulta> Cola;

        public Consultas()
        {
            Cola = new LinkedBlockingDeque<>();
        }
        public void Encolar(String ... data)
        {

            Cola.add(new Consulta(data));
            //System.out.println("Encolando datos... " + Cola.size());
        }
        public synchronized Consulta Extraer(){
            //System.out.println(Cola.size());
            return Cola.poll();
        }


    }

    private class Consulta {
        public final String Tipo;
        public final String Codigo;
        public final String De;
        public final String Para;
        public final String Monto;
        public Consulta(String ... data) {
            Tipo = data[0];
            Codigo = data[1];
            De = data[2];
            Para = data[3];
            Monto = data[4];
        }
    }

}
