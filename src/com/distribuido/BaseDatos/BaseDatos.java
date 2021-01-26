package com.distribuido.BaseDatos;

import com.distribuido.Comunicacion;
import com.distribuido.Configuracion;
import com.distribuido.ManejoCSV.CSVcontrol;

import java.io.IOException;
import java.net.Socket;
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

        while (Activo && (Orden = LeerOrden()) != BDConfiguracion.ORDEN_APAGAR )
        {
            Mensaje = EscucharMensaje();
            System.out.println("ORDEN " + Orden);
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
                    System.out.println("Enviando confirmacion");
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
                        Transaccion C = mConsultas.Extraer();
                        System.out.println("Desencolando datos");
                        if (C.Tipo.equals("A")) {
                            Data.Transferencia(C.De, C.Para, C.Monto);
                        }

                        Respuesta r = new Respuesta(
                                C.Tipo,
                                C.Codigo,
                                C.De,
                                Data.BuscarCuenta(C.De),
                                C.Tipo.equals("A")? C.Para : "0",
                                C.Tipo.equals("A")? Data.BuscarCuenta(C.Para) : "0"
                        );


                        System.out.println("BD : " +  r.toString());
                        Responder(r.toString());
                    }
                }
                Responder("-1");
            }
        }
    }


    private class Consultas
    {

        public LinkedBlockingDeque<Transaccion> Cola;

        public Consultas()
        {
            Cola = new LinkedBlockingDeque<>();
        }
        public void Encolar(String ... data)
        {

            Cola.add(new Transaccion(data));
            System.out.println("Encolando datos... " + Cola.size());
        }
        public synchronized Transaccion Extraer(){
            System.out.println(Cola.size());
            return Cola.poll();
        }


    }


}
