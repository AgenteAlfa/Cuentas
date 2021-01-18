package com.distribuido.ManejoCSV;


import javax.naming.Context;
import java.io.*;
import java.util.Random;

public class CSVcontrol {

    private Casilla CInicial,CApuntada;
    private int[] Puntero;

    public CSVcontrol()
    {
        Configuracion.Inicializar();
        Puntero = new int[] {0,0};
        CInicial = null;
    }
    public void SetPuntero(int ... puntos)
    {
        Puntero = puntos;
    }

    public void Imprimir()
    {
        Casilla C = CInicial;
        while (C != null)
        {
            Casilla temp = C;
            while (temp != null)
            {
                System.out.print(temp.getData() + " \t");
                temp = temp.getDer();
            }
            System.out.println();
            C = C.getAba();
        }

    }

    public void Generar(int T1,int T2,int N)
    {
        Casilla temp = null;
        int i = 0;
        while (i++ < N)
        {
            temp = AgregarLinea(temp,GenerarPalabra(T1),GenerarNumero(T2));
        }
    }
    private String GenerarPalabra(int N)
    {
        StringBuilder builder = new StringBuilder();
        Random R = new Random();
        for (int i = 0; i < N; i++)
        {
            char c = (char) (R.nextInt('Z' - 'A') + 'A');
            if (R.nextInt(5) == 0)
                builder.append(c);
            else
                builder.append(R.nextInt(10));
        }
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


    public Casilla getCasilla(int ... puntos)
    {
        Casilla C = CInicial;
        for (int i = 0; i < puntos[0]; i++) {
            C = C.getAba();
        }
        for (int j = 0; j < puntos[1]; j++) {
            C = C.getDer();
        }
        return C;
    }

    public boolean Guardar() throws IOException {

        FileOutputStream FOS = new FileOutputStream(Configuracion.getmFile());
        OutputStreamWriter OSW = new OutputStreamWriter(FOS);
        BufferedWriter BW = new BufferedWriter(OSW);

        Casilla C = CInicial;
        while (C != null)
        {
            Casilla temp = C;
            while (temp != null)
            {
                BW.write(temp.getData());
                if (temp.getDer() != null)
                    BW.write(",");
                temp = temp.getDer();
            }
            if (C.getAba() != null)
            BW.newLine();
            C = C.getAba();
        }
        BW.close();
        OSW.close();
        FOS.close();
        return true;
    }
    private Casilla AgregarLinea(Casilla CAnterior , String ... Lineas)
    {
        Casilla CIni = null;
        Casilla FAnterior = null;

        for (String S: Lineas) {
            Casilla C = new Casilla(S,Puntero);
            if (CIni == null) CIni = C;
            Puntero[1] ++;

            C.setIzq(FAnterior);
            if (FAnterior != null) FAnterior.setDer(C);

            FAnterior = C;
        }
        Puntero[0]++;
        Puntero[1] = 0;
        if (CInicial == null) CInicial = CIni;

        if (CAnterior != null) CAnterior.setAba(CIni);
        if (CIni != null) CIni.setArr(CAnterior);

        return CIni;
    }

    public boolean Cargar() {

        CApuntada = CInicial;
        Puntero = new int[] {0,0};

        try{
            FileInputStream FIS = new FileInputStream(Configuracion.getmFile());
            InputStreamReader inputStreamReader = new InputStreamReader(FIS);
            BufferedReader BR = new BufferedReader(inputStreamReader);

            Casilla temp = null;
            while (BR.ready())
            {
                temp = AgregarLinea(temp, BR.readLine().split(Configuracion.Separador));
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