package com.distribuido;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Comunicacion {

    private final Socket SOrden;
    private final Socket SRespuesta;

    private ObjectInputStream OISo;
    private ObjectOutputStream OOSo;

    private ObjectInputStream OISr;
    private ObjectOutputStream OOSr;
    public Comunicacion(Socket orden, Socket respuesta)
    {
        SOrden = orden;
        SRespuesta = respuesta;
        try {
            OOSo = new ObjectOutputStream(SOrden.getOutputStream());
            OISo = new ObjectInputStream(SOrden.getInputStream());

            OOSr = new ObjectOutputStream(SRespuesta.getOutputStream());
            OISr = new ObjectInputStream(SRespuesta.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Ordenar(int orden,String cadena)
    {
        try {
            OOSo.writeObject(orden);
            OOSo.writeObject(cadena);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error desconocido");
        }

    }
    public void Confirmar(String res)
    {
        try {
            OOSo.writeObject(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Responder(String res)
    {
        try {
            OOSr.writeObject(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Cerrar()
    {

        try {
            OOSo.close();
            OISo.close();
            OOSr.close();
            OISr.close();
            SOrden.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int LeerOrden()
    {
        try {
            if (SOrden.isConnected())
                return (int) OISo.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public String EscucharMensaje()
    {
        try {
            if (SOrden.isConnected())
                return (String) OISo.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }
    public String EscucharRespuesta()
    {
        try {
            if (SRespuesta.isConnected())
                return (String) OISr.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }
    public String EsperarConfirmacion()
    {
        try {
            if (SOrden.isConnected())
                return (String) OISo.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

}
