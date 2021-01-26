package com.distribuido.Ventana;

import com.distribuido.BaseDatos.BaseDatos;
import com.distribuido.Cliente.Cliente;
import com.distribuido.Distribuidor.Distribuidor;
import com.distribuido.ManejoCSV.CSVcontrol;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Ventana extends JFrame{
    int ancho=600;
    int alto=400;
    int iesimo = 0;
    FondoPanel fondo = new FondoPanel();
    //Campos para llenar
    JLabel A = new JLabel();
    JTextField JTextFieldA = new JTextField();

    JButton BCalcular = new JButton();
    JProgressBar BProgreso = new JProgressBar();
//    JLabel R = new JLabel();
    Distribuidor mDistribuidor;

    public Ventana() throws IOException {

        setSize(ancho, alto);
        this.setContentPane(fondo);
        fondo.setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        init();

        setVisible(true);

        mDistribuidor = new Distribuidor(this);
        mDistribuidor.Esperar();

        //--ACCIÓN DEL BOTÓN

        ActionListener calcula=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //--CAMPO DE ACCIÓN DEL BOTÓN CALCULAR
                //-----------------------------

                mDistribuidor.Empezar();
                //Obtiene los datos de la ventana
                // a es el numero de transacciones que se desean generar



                //Inicia el calculo

                // aqui se coloca la función.

                //------------------------------------
            }
        };

        BCalcular.addActionListener(calcula);

    }
    public void AumentarProgreso()
    {
        JTextFieldA.setText(Integer.toString(++iesimo));
    }


    public static void main (String args[]){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Ventana().setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init(){
        int PY1=200;
        int PY2=280;
        int tamanoLetraJL=25;
        //Label A
        A.setText("Número de Transacciones:");
        A.setBounds(70,PY1,320,30);
        A.setFont(new Font("Arial",Font.BOLD,tamanoLetraJL));
        add(A);
        //JTextFieldA
        JTextFieldA.setText("0");
        JTextFieldA.setBounds(438,PY1,90,30);
        JTextFieldA.setFont(new Font("Arial",Font.BOLD,20));
        add(JTextFieldA);

        //Boton calcular
        BCalcular.setText("Empezar a trabajar");
        BCalcular.setBounds(210,PY2,150,30);
        BCalcular.setFont(new Font("Arial",Font.BOLD,20));
        BCalcular.setMnemonic('c');//tecla para hacerlo funcionar ALT+c
        add(BCalcular);
        //ProgressBar
        BProgreso.setBounds(150,420,350,5);
        add(BProgreso);
        //Label R
        // R.setText("Resultado:");
        // R.setBounds(200,440,300,30);
        // R.setFont(new Font("Arial",Font.BOLD,20));
        // add(R);
    }

    class FondoPanel extends JPanel{
        private Image imagen;

        @Override
        public void paint(Graphics g){
            //imagen = new ImageIcon(getClass().getResource("src\\com\\distribuido\\Ventana\\imagenes\\fondoIntegral.jpg")).getImage();
            imagen = new ImageIcon(getClass().getResource("imagenes/Fondo.jpg")).getImage();
            g.drawImage(imagen,0,0,this);

            setOpaque(false);
            super.paint(g);

        }
    }
}
