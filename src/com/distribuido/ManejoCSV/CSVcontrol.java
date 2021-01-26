package com.distribuido.ManejoCSV;


import com.distribuido.BaseDatos.BDConfiguracion;

import javax.naming.Context;
import java.io.*;
import java.util.Random;

public class CSVcontrol {

    int index;
    int iesimo = 0;
    private String[][] TablaDatos;


    public CSVcontrol()
    {
        index = BDConfiguracion.MIN_INDEX;
        Configuracion.Inicializar();
    }


    public void Imprimir()
    {
        for (String[] linea :TablaDatos) {
            System.out.println(linea[0] + " " + linea[1]);
        }

    }

    public synchronized boolean Transferencia(String de , String para , int Monto)
    {
        int DE = (int) Long.parseLong(de,16) - BDConfiguracion.N;
        int PARA = (int) Long.parseLong(para,16)- BDConfiguracion.N;
        if (Integer.parseInt(TablaDatos[DE][1])  > Monto)
        {
            TablaDatos[DE][1] = Integer.toString(Integer.parseInt(TablaDatos[DE][1])  - Monto);
            TablaDatos[PARA][1] = Integer.toString(Integer.parseInt(TablaDatos[PARA][1])  + Monto);
        }

        return false;

    }

    public void Generar(int T1,int T2,int N)
    {
        Casilla temp = null;
        int i = 0;
        while (i++ < N)
        {
            AgregarLinea(temp,GenerarPalabra(T1),GenerarNumero(T2));
        }
    }

    public String BuscarMontoCuenta(String S)
    {
        int n = (int) Long.parseLong(S,16) - BDConfiguracion.N;
        return TablaDatos[n][1];
    }

    private String GenerarPalabra(int N)
    {
        StringBuilder builder = new StringBuilder();
        String S = Integer.toHexString(index++);
        builder.append("0".repeat(Math.max(0, N - S.length())));
        builder.append(S);
        return builder.toString();
    }
    private String GenerarNumero(int N)
    {
        StringBuilder builder = new StringBuilder();
        Random R = new Random();
        for (int i = 0; i < N; i++)
        {
                builder.append(R.nextInt(10));
        }
        return builder.toString();
    }



    public boolean Guardar() throws IOException {

        FileOutputStream FOS = new FileOutputStream(Configuracion.getmFile());
        OutputStreamWriter OSW = new OutputStreamWriter(FOS);
        BufferedWriter BW = new BufferedWriter(OSW);

        for (int i = 0; i < BDConfiguracion.N; i++) {
            BW.write(TablaDatos[i][0]);
            BW.write(",");
            BW.write(TablaDatos[i][0]);
            BW.newLine();
        }
        BW.close();
        OSW.close();
        FOS.close();
        return true;
    }
    private void AgregarLinea(Casilla CAnterior , String ... Lineas)
    {

        TablaDatos[iesimo][0] = Lineas[0];
        TablaDatos[iesimo][1] = Lineas[1];

        iesimo++;

    }

    public boolean Cargar() {

        TablaDatos = new String[BDConfiguracion.N][2];

        try{
            FileInputStream FIS = new FileInputStream(Configuracion.getmFile());
            InputStreamReader inputStreamReader = new InputStreamReader(FIS);
            BufferedReader BR = new BufferedReader(inputStreamReader);

            Casilla temp = null;
            while (BR.ready())
            {
                AgregarLinea(temp, BR.readLine().split(Configuracion.Separador));
            }

            BR.close();
            inputStreamReader.close();
            FIS.close();

        }catch (Exception E)
        {
            System.out.println("Error: " + E.getMessage());
        }
        return true;
    }

}