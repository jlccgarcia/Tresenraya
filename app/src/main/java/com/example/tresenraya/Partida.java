package com.example.tresenraya;

import java.util.Random;

public class Partida {

    public Partida (int dificultad) {
        //Almacenamos la dificultad que recibimos como parámetro en el campo clase "dificultad"
        this.dificultad=dificultad;

        jugador=1;  //Al jugador 1 le asignaremos el círculo

        //Inicializamos el array de casillas como "todas libres" (a 0)
        casillas=new int[9];
        for(int i=0;i<9;i++){
            casillas[i]=0;
        }
    }

    //Método para devolver la casilla "clave" para hacer 3 en raya.
    //Sería la casilla que hay que rellenar para completar el 3 en raya.
    public int dosEnRaya(int jugador_en_turno) {
        int casilla = -1;         //Almacenará la casilla "clave"
        int cuantas_lleva = 0;    //Cuando sea =2 --> El jugador que se está evaluando tiene 2 en raya

        for (int i = 0; i < COMBINACIONES.length; i++) {
            for (int pos : COMBINACIONES[i]) {
                //Se van comprobando las dimensiones
                //y se valora si se tiene 2 en raya
                if (casillas[pos]==jugador_en_turno) {
                    cuantas_lleva++;
                }
                //Almacena la casilla que aún está vacía, por si este jugador
                //lleva ya 2 con su marca en esa dimensión y luego tiene
                //que devolver esta casilla
                if (casillas[pos]==0) {
                    casilla=pos;
                }
            }
            //Si tras la valoración de una dimensión se encuentra
            //que lleva ya 2 juntas con su marca, se devuelve el valor
            //de la casilla "clave"
            if (cuantas_lleva==2 && casilla!=-1){
                return casilla;
            }

            //Reseteo de variables como estaban al principio
            //para que estén correctos para la siguiente vuelta del bucle
            casilla=-1;
            cuantas_lleva=0;
         }

        return -1;
    }

    //Método de "Inteligencia Artificial" para ver qué casilla seleccionará el ordenador
    public int ia() {
        int casilla;

        //Comprueba si la máquina (jugador 2) tiene la posibilidad
        //de hacer 3 en raya.
        casilla=dosEnRaya(2);
        //Si no se devuelve la casilla "clave", el método dosEnRaya devuelve -1;
        //si no, la casilla "clave"
        if(casilla!=-1) {
            return casilla;
        }

        //En nivel de dificultad normal, se valora si el jugador 1
        //tiene posibilidad de hacer 3 en raya; para que la máquina
        //sepa si tiene que poner su aspa en esa casilla "clave"
        if(dificultad>0) {
            casilla=dosEnRaya(1);   //Posibilidad de 3 en raya para el jugador 1
            if (casilla!=-1) {
                return casilla;
            }

        }

        //En nivel de dificutad imposible, si no hay posibilidad de hacer 3
        //en raya, le digo que la máquina me marque una esquina, lo que
        //hace imposible hacer 3 en raya (lógica del programa)
        if (dificultad==2) {
            //Esquinas --> Casillas 0, 2, 6 y 8
            if (casillas[0]==0) {
                return 0;
            }
            if (casillas[2]==0) {
                return 2;
            }
            if (casillas[6]==0) {
                return 6;
            }
            if (casillas[8]==0) {
                return 8;
            }
        }

        //Para el nivel de dificultad fácil --> Comportamiento aleatorio
        Random casilla_azar=new Random();
        casilla=casilla_azar.nextInt(9);

        return casilla;
    }

    //Método para indicar el turno de juego (cambia entre jugadores)
    //e indicar qué circunstancia va ocurriendo en el juego. Puede devolver:
    //1=gana jugador 1; 2=gana jugador 2; 3=empate; 0=ninguna de las anteriores (sigue jugando)
    public int turno(){

        boolean empate=true;
        boolean ult_movimiento=true;

        //Se comprueba cómo va el juego
        for (int i=0;i<COMBINACIONES.length;i++) {
            for (int pos:COMBINACIONES[i]) {
                System.out.println("Valor en posición " + pos + " " + casillas[pos]);

                //Comprobación para ver si hay 3 en raya
                //con 3 1's ó 3 2's seguidos en cada dimensión del array
                if (casillas[pos]!=jugador) {
                    ult_movimiento=false;
                }

                //Si aún hay alguna casilla con valor 0 es que aún se está jugando
                //Según se va jugando, se van rellenado esas casillas con
                //los valores 1 (jugador 1) ó 2 (jugardor 2)
                if (casillas[pos]==0) {
                    empate=false;
                }
            }

            System.out.println("----------------------------------------");

            if (ult_movimiento) {
                return jugador; //Indica el jugador que ha ganado
            }

            ult_movimiento=true;
        }

        if (empate) {
            return 3;   //Se ha empatado
        }

        //Se cambia el turno del jugador
        jugador++;
        if (jugador>2){
            jugador=1;
        }

        return 0;   //Se sigue jugando
    }


    //Método para controlar si la casilla está ocupada (1) o no (0)
    public boolean comprueba_casilla(int casilla){
        if (casillas[casilla]!=0){
            return false;
        } else {
            casillas[casilla]=jugador;
        }
        return true;
    }


    public final int dificultad;

    public int jugador;

    private int[] casillas; //Para controlar casillas que se van rellenando

    //Combinaciones de hacer 3 en raya
    private final int[][] COMBINACIONES={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
}
