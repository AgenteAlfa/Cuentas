package com.distribuido.Cliente;

import com.distribuido.BaseDatos.BDConfiguracion;
import com.distribuido.BaseDatos.Transaccion;
import com.distribuido.Configuracion;
import com.distribuido.Distribuidor.DBD;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;

public class Cliente extends Thread{

    Socket mCliente;
    private static int index = 0;
    private final String Letra;
    public final int NTransacciones;

    private ObjectInputStream OIS;
    private ObjectOutputStream OOS;

    public Cliente(int NTransacciones , String letra) {
        this.NTransacciones = NTransacciones;
        Letra = letra;
    }


    @Override
    public void run() {
        Socket socket = new Socket();

        int timeout = 5000;
        SocketAddress sockAdr = new InetSocketAddress(Configuracion.IP_DISTRIBUIDOR,Configuracion.PUERTO_ENVIAR);
        try {
            System.out.println("Intentando conectarse...");
            socket.connect(sockAdr, timeout);
            System.out.println("Conectado");
            OOS = new ObjectOutputStream(socket.getOutputStream());
            OIS = new ObjectInputStream(socket.getInputStream());

            Random R = new Random();
            for (int i = 0; i < NTransacciones; i++) {
                String id1 =  "0" + Integer.toHexString(R.nextInt(BDConfiguracion.N) + BDConfiguracion.MIN_INDEX);
                String id2 =  "0" + Integer.toHexString(R.nextInt(BDConfiguracion.N) + BDConfiguracion.MIN_INDEX);
                Transaccion T = new Transaccion(R.nextInt(
                        100) > 30 ? "L":"A",
                        Letra + i,
                        id1,
                        id2,
                        R.nextInt(250));

                System.out.println("Enviando consulta " + T.toString());
                OOS.writeObject(T.toString());
                System.out.println("Recibiendo consulta : ");
                String S = (String) OIS.readObject();
                System.out.println("x" + S);
            }
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
