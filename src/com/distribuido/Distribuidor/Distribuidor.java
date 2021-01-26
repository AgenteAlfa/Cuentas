package com.distribuido.Distribuidor;

import com.distribuido.BaseDatos.BDConfiguracion;
import com.distribuido.BaseDatos.Respuesta;
import com.distribuido.BaseDatos.Transaccion;
import com.distribuido.Configuracion;
import com.distribuido.Ventana.Ventana;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class Distribuidor {

    ArrayList<DBD> BDs;
    List<DCliente> Clientes;
    public LinkedBlockingDeque<Transaccion> ColaClientes;
    private boolean Trabajando;


    private Hilo_Esperador HE;
    private Hilo_Escuchador mHilo_Escuchador;
    private Hilo_TrabajadorCola HCola;
    
    private Hilo_Cliente HC;
    private Hilo_Distribuidir HD;

    private final ServerSocket SSOrdenador;
    private final ServerSocket SSRespondedor;
    private final ServerSocket SSEscuchador;

    private Ventana mVentana;

    public Distribuidor(Ventana ventana) throws IOException {

        SSOrdenador = new ServerSocket(Configuracion.PUERTO_ORDENES);
        SSRespondedor = new ServerSocket(Configuracion.PUERTO_RESPUESTAS);
        SSEscuchador = new ServerSocket(Configuracion.PUERTO_ENVIAR);
        SSOrdenador.setSoTimeout(1500);
        SSRespondedor.setSoTimeout(1550);

        //SSEscuchador.setSoTimeout(10000);

        BDs = new ArrayList<>();
        Clientes = Collections.synchronizedList(new ArrayList<DCliente>());
        ColaClientes = new LinkedBlockingDeque<>();

        mVentana = ventana;
    }

    public void Esperar() {
        HE = new Hilo_Esperador();
        HE.start();
    }

    public void Empezar() {
        HE.Apagar();
        System.out.println("Estoy empezando con ..." + BDs.size() + " BDs");
        mHilo_Escuchador = new Hilo_Escuchador();
        mHilo_Escuchador.start();

        HC = new Hilo_Cliente();
        HD = new Hilo_Distribuidir();

        HCola = new Hilo_TrabajadorCola();
        
        HC.start();
        HD.start();
        HCola.start();


    }


    private class Hilo_Esperador extends Thread //Espera a las BD
    {

        private void Apagar() {
            System.out.println("ESPERADOR : DEJO DE ESPERAR DISTRIBUIDORES");
            Encendido = false;
        }

        private boolean Encendido = true;

        @Override
        public void run() {
            while (Encendido) {
                System.out.println("Coordinador -> Estoy esperando distribuidores");
                try {
                    ;
                    BDs.add(new DBD(SSOrdenador.accept(), SSRespondedor.accept()));
                    System.out.println("A ingresado una BD");
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
            System.out.println("Yo ya termine -> ESPERADOR");
        }
    }

    private class Hilo_Cliente extends Thread // Espera a los Clientes
    {

        private void Apagar() {
            Encendido = false;
        }

        private boolean Encendido = true;

        @Override
        public void run() {
            while (Encendido) {
                try {
                    while (Encendido) {
                        DCliente DC = new DCliente(SSEscuchador.accept());
                        Clientes.add(DC);
                        System.out.println("Un cliente a entrado, ahora hay ..." + Clientes.size());
                    }

                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
            System.out.println("Yo ya termine -> ESPERADOR");
        }
    }


    private class Hilo_Distribuidir extends Thread //Distribuir clientes
    {
        private void Apagar() {
            Encendido = false;
        }
        boolean letrero = true;
        private boolean Encendido = true;

        @Override
        public void run() {
            System.out.println("Empezo el hilo distribuidor");
            while (Encendido) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (DCliente cliente : Clientes) {
                    if (cliente.Pendiente)
                    {//Hay que revisar si hay una respuesta lista

                        if (cliente.Respuesta != null) {
                            cliente.Enviar(cliente.Respuesta);
                            cliente.Pendiente = false;
                            cliente.Respuesta = null;
                            cliente.CodigoSolicitud = null;
                            letrero = true;
                        }
                        else //if (letrero)
                        {

                            System.out.println("Hay un cliente " + cliente.CodigoSolicitud +  " esperando y no hay respuesta");
                            System.out.println("Tareas pendientes : " + ColaClientes.size());
                            letrero = false;
                        }
                    }
                    else
                    {
                        if (cliente.Disponible())
                        {
                            String cadena = cliente.Leer();
                            System.out.println("Entrando..." + cadena);
                            Transaccion t = new Transaccion(cadena);
                            ColaClientes.add(t);
                            cliente.CodigoSolicitud = t.Codigo;
                            cliente.Pendiente = true;
                            cliente.Lectura = t.Tipo.equals("L");
                            cliente.Respuesta = null;
                            System.out.println("Un cliente fue agregado a la cola");
                        }
                    }
                }

            }
        }
    }

    private class Hilo_Escuchador extends Thread    //Escuchar BD
    {

        private void Apagar() {
            System.out.println("ESPERADOR : DEJO DE ESPERAR DISTRIBUIDORES");
            Encendido = false;
        }

        private boolean Encendido = true;

        @Override
        public void run() {
            while (Encendido) {
                Respuesta[] mResp = new Respuesta[BDs.size()];
                for (int i = 0; i < BDs.size(); i++) {
                    DBD datos = BDs.get(i);
                    mResp[i] = new Respuesta(datos.EscucharRespuesta());
                    System.out.println(mResp[i].toString());

                }
                //Revisar igualdad de datos
                boolean igual = true;
                for (int i = 1; i < BDs.size(); i++) {
                    igual = mResp[i - 1].toString().equals(mResp[i].toString());
                }
                if (!igual){
                    System.out.println("CORRUPCION DE DATOS");
                    return;}

                for (DCliente c : Clientes) {
                    if (mResp[0].Codigo.equals(c.CodigoSolicitud))
                    {
                        c.Respuesta = mResp[0].toString();
                        mVentana.AumentarProgreso();
                    }
                    System.out.println(c.Lectura);
                }

            }
        }
    }

    private class Hilo_TrabajadorCola extends Thread
    {
    
        private void Apagar() {
            Encendido = false;
        }
    
        private boolean Encendido = true;
    
        @Override
        public void run() {

            System.out.println("Empezo el hilo que trabaja con la cola de pedidos");
            while (Encendido) {
                if(ColaClientes.size() > 0)
                {
                    System.out.println("Hay un cliente pendiente de ser atendido");
                    Transaccion transaccion = ColaClientes.getFirst();
                    for (DBD bd: BDs) {
                        assert transaccion != null;
                        if (transaccion.Tipo.equals("L"))
                        {
                            System.out.println("Enviado a BD " + transaccion.Codigo);
                            bd.Ordenar(BDConfiguracion.ORDEN_ESCRIBIR,transaccion.toString());
                            ColaClientes.poll();
                        }
                        else
                        {
                            Trabajando = false;
                            //Revisar que el trabajo este completo
                            for (DCliente c : Clientes) {
                                Trabajando |= c.Lectura;
                            }

                            //Realizar Actualizacion
                            if (!Trabajando) // Solo se ordena actualizar si no se trabaja
                            {
                                bd.Ordenar(BDConfiguracion.ORDEN_ESCRIBIR,transaccion.toString());
                                ColaClientes.poll();
                            }
                            else
                            {
                                System.out.println("No puedo dar ordenes de actualizar si aun se lee");
                            }
                        }

                    }
                }
            }
        }
    }
    
}
