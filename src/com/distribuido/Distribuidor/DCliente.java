package com.distribuido.Distribuidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DCliente {
    private Socket mSocket;

    private ObjectInputStream OIS;
    private ObjectOutputStream OOS;

    public boolean Lectura = true;
    public boolean Pendiente = false;
    public String CodigoSolicitud = null;
    public String Respuesta = null;

    public DCliente(Socket socket) {
        this.mSocket = socket;
        try {
            OOS = new ObjectOutputStream(socket.getOutputStream());
            OIS = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Enviar(String S)
    {
        try {
            OOS.writeObject(S);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean Disponible()
    {
        try {
            return mSocket.getInputStream().available() > 0;
            //return OIS.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String Leer()
    {
        try {
            return (String) OIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
