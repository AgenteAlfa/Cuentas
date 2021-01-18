package com.distribuido;

import com.distribuido.ManejoCSV.CSVcontrol;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        CSVcontrol mcsv = new CSVcontrol();

        mcsv.Generar(6,4,100000);
        try {
            mcsv.Guardar();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
