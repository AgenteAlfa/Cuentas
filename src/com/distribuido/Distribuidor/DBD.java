package com.distribuido.Distribuidor;

import com.distribuido.Comunicacion;

import java.net.Socket;

public class DBD extends Comunicacion {
    public DBD(Socket orden, Socket responder) {
        super(orden,responder);
    }
}
