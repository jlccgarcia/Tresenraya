package com.example.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Iniciamos el array casillas que identifica cada casilla y la almacena en el array
        CASILLAS=new int[9];
        CASILLAS[0]=R.id.a1;
        CASILLAS[1]=R.id.a2;
        CASILLAS[2]=R.id.a3;
        CASILLAS[3]=R.id.b1;
        CASILLAS[4]=R.id.b2;
        CASILLAS[5]=R.id.b3;
        CASILLAS[6]=R.id.c1;
        CASILLAS[7]=R.id.c2;
        CASILLAS[8]=R.id.c3;
    }

    public void aJugar(View vista) {
        ImageView imagen;

        //Reseteo del tablero, poniéndolas todas en blanco
        for(int cadaCasilla:CASILLAS){
            imagen=(ImageView)findViewById((cadaCasilla));
            imagen.setImageResource(R.drawable.casilla);
        }

        //Se establece los jugadores que van a jugar la partida: 1 ó 2
        //A este método se le pasa el botón pulsado...
        jugadores=1;
        if (vista.getId()==R.id.dosjug) {
            jugadores=2;
        }

        //Se establece el nivel de dificultad con la que se jugará
        RadioGroup configDificultad=(RadioGroup)findViewById(R.id.configID);
        int id=configDificultad.getCheckedRadioButtonId();
        int dificultad=0;
        if (id==R.id.normal) {
            dificultad=1;
        } else if (id==R.id.imposible) {
            dificultad=2;
        }

        //Se empieza a jugar y se inhabilitan los botones
        partida=new Partida(dificultad);
        ((Button)findViewById(R.id.unjug)).setEnabled(false);
        //((RadioGroup)findViewById(R.id.configID)).setEnabled(false);
        //Probar si funciona con el setEnabled de la línea anterior, si no este setAlpha
        ((RadioGroup)findViewById(R.id.configID)).setAlpha(0);
        ((Button)findViewById(R.id.dosjug)).setEnabled(false);
    }

    public void toque (View mivista) {
        //Se evita que si no se ha comenzado la partida (no se han
        //seleccionado jugadores), se pueda detectar pulsaciones sobre casillas
        if (partida==null) {
            return;
        }
        int  casilla=0;

        //Capturamos el valor de la casilla pulsada (que se pasa como parámetro a este método)
        for (int i=0;i<9;i++) {
                if (CASILLAS[i]==mivista.getId()){
                    casilla=i;
                    break;
                }
        }

        //Mensaje emergente de prueba para saber qué casilla se ha pulsado (para pruebas intermedias)
        /*Toast toast= Toast.makeText(this, "Has pulsado la casila " + casilla, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
        */

        if (partida.comprueba_casilla(casilla)==false){
            return;
        }

        //Respuesta a nuestra pulsación y cambiamos de turno
        //Comprobamos si ya ha terminado la partida (gana uno, el otro, o empate)
        marca(casilla);
        int resultado=partida.turno();
        if (resultado>0) {
            termina(resultado);
            return; //Hace falta este return para que cuando vuelva del
                    //método termina "ganando el jugador 1" no siga la
                    //ejecución de este método (con partida=null)
        }

        //El ordenador escoge la casilla donde quiere dibujar
        casilla=partida.ia();
        while(partida.comprueba_casilla(casilla)!=true){
            casilla=partida.ia();
        }

        //Respuesta a pulsación del ordenador y comprobamos si ha terminado la partida
        marca(casilla);
        resultado=partida.turno();
        if (resultado>0) {
            termina(resultado);
        }
    }

    private void termina (int resultado) {
        String mensaje;

        if (resultado==1) { //Ha ganado el jugador 1
            mensaje="Ganan los círculos";
        } else if (resultado==2) {  //Ha ganado el jugador 2
            mensaje="Ganan las aspas";
        } else {
            mensaje="Empate";
        }

        //Mostramos el resultado
        Toast toast= Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();

        //Una vez que muestra el mensaje de quién ha ganado, reinicializamos
        //los botones para que se pueda comenzar una partida nueva
        partida=null;
        ((Button)findViewById(R.id.unjug)).setEnabled(true);
        ((RadioGroup)findViewById(R.id.configID)).setAlpha(1);
        ((Button)findViewById(R.id.dosjug)).setEnabled(true);
    }

    //Dibujar círculo o aspa según sea el jugador 1 o el 2
    private void marca (int casilla) {

        //Variable de tipo ImageView para manejar casilla-aspa-círculo
        ImageView imagen;

        imagen=(ImageView)findViewById(CASILLAS[casilla]);

        if (partida.jugador==1) {
            imagen.setImageResource(R.drawable.circulo);
        } else {
            imagen.setImageResource(R.drawable.aspa);
        }
    }


    private int jugadores;      //Nº de jugadores
    private int[] CASILLAS;     //Casillas del tablero
    private Partida partida;

}
