package watki;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Kontroler. Odpowiada za wczytanie elementów programu oraz uruchamia kolejne wątki.
 */

public class Playground {

    @FXML
    Pane canvas;    //panel, na którym rysowane są pozostałe elementy

    private BoxClientControl bcc;   //monitor dla kontroli liczby kulek wewnątrz pudełka

    private Synchronizator synchro; //obiekt monitora

    private Box box;    //obiekt pudełka
    private Thread boxThread;   //wątek pudełka

    private int i=0;    //indeks ostatnio uruchomionego wątku kulki
    private final int maxBallsAmount=50;    //maksymalna liczba kulek

    private Thread[] ballThreads = new Thread[maxBallsAmount];  //tablica wątków

    /**
     * Metoda reprezentowana graficznie przez przycisk. Tworzy wątki kolejnych kulek.
     */
    public void draw_ball_button()
    {
        if(i<maxBallsAmount) {
            ballThreads[i] = new Ball(canvas, box, synchro, bcc);
            ballThreads[i].setDaemon(true); //wątek uruchomiony jako demon niezwłocznie zamknie się wraz z Main
            ballThreads[i].start();

            ++i;
        }
    }

    /**
     * Inicjalizacja. Wczytuje elementy niezbędne do późniejszego prawidłowego działania programu.
     */
    void init()
    {
        bcc=new BoxClientControl();
        synchro=new Synchronizator();
        box=new Box(canvas, synchro, bcc);
        boxThread=new Thread(box);
        boxThread.setDaemon(true);
        boxThread.start();
    }
}
