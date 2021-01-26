package com.distribuido.Ventana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ventana extends JFrame{
    int ancho=600;
    int alto=400;
    FondoPanel fondo = new FondoPanel();
    //Campos para llenar
    JLabel A = new JLabel();
    JTextField JTextFieldA = new JTextField();

    JButton BCalcular = new JButton();
    JProgressBar BProgreso = new JProgressBar();
    //a es el número de transacciones que
    int numt =1000;
//    JLabel R = new JLabel();

    public Ventana(){

        setSize(ancho, alto);
        this.setContentPane(fondo);
        fondo.setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //x es el número de transacciones hechas
        int x=0;
        init();

        setVisible(true);

        //--ACCIÓN DEL BOTÓN

        ActionListener calcula=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //--CAMPO DE ACCIÓN DEL BOTÓN CALCULAR
                //-----------------------------

                // numt se obtiene de la ventana, es el numero de transacciones que se desean generar
                numt = Integer.parseInt(JTextFieldA.getText());
                //-----------------------------

                //Inicia el calculo

                // aqui se coloca la función.
                //x es el número de transacciones hechas, la funcion aumenta el progreso con respecto a numt
                AumentarProgreso(x);


                //------------------------------------
            }
        };

        BCalcular.addActionListener(calcula);

    }

/*    public void SetRespuesta(String S)
    {
        R.setText("Resultado: " + S);
    }

 */
    public void AumentarProgreso(double x)
    {
        BProgreso.setValue((int) ((x/numt)*100));
    }


    public static void main (String args[]){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Ventana().setVisible(true);
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
        BCalcular.setText("Generar");
        BCalcular.setBounds(210,PY2,150,30);
        BCalcular.setFont(new Font("Arial",Font.BOLD,20));
        BCalcular.setMnemonic('c');//tecla para hacerlo funcionar ALT+c
        add(BCalcular);
        //ProgressBar
        BProgreso.setBounds(110,330,350,4);
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
