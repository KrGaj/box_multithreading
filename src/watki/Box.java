package watki;

/**
 * Klasa implementuje wątek pudełka, wewnątrz którego poruszają się kulki.
 */

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Box extends Thread {

    private final int frameThickness=10;    //grubość ramki

    //box składa się z dwóch kwadratów - zewnętrznego i wewnętrznego
    private Rectangle outerSquare;
    private Rectangle innerSquare;

    private Synchronizator synchro; //monitor

    //współrzędne ścian wnętrza boxa
    private final int innerSquareMinX;
    private final int innerSquareMinY;
    private final int innerSquareMaxX;
    private final int innerSquareMaxY;

    //współrzędne zewnętrznych ścian pudełka
    private final int outerSquareMinX;
    private final int outerSquareMinY;
    private final int outerSquareMaxX;
    private final int outerSquareMaxY;

    private BoxClientControl bcc;

    /**
     * Konstruktor.
     * @param canvas panel, na którym ma znajdować się pudełko
     * @param synchro monitor kontrolujący synchronizację między wątkami piłek oraz pudełka
     * @param bcc monitor kontrolujący liczbę kulek wewnątrz pudełka
     */
    Box(Pane canvas, Synchronizator synchro, BoxClientControl bcc){
        this.bcc=bcc;

        Bounds bounds=canvas.getLayoutBounds(); //krawędzie

        final int outerSquareSize=250;

        final int outerSquareX=(int)(0.5*(bounds.getMaxX()-outerSquareSize));
        final int outerSquareY=(int)(0.5*(bounds.getMaxY()-outerSquareSize));

        outerSquare =new Rectangle(outerSquareX, outerSquareY, outerSquareSize, outerSquareSize);
        outerSquare.setFill(Color.CYAN);

        final int innerSquareX=outerSquareX+frameThickness;
        final int innerSquareY=outerSquareY+frameThickness;

        final int innerSquareSize=outerSquareSize-2*frameThickness;

        innerSquare =new Rectangle(innerSquareX, innerSquareY, innerSquareSize, innerSquareSize);
        innerSquare.setFill(Color.WHITE);

        canvas.getChildren().addAll(outerSquare, innerSquare);

        outerSquareMinX =outerSquareX;
        outerSquareMinY =outerSquareY;
        outerSquareMaxX = outerSquareMinX +outerSquareSize;
        outerSquareMaxY = outerSquareMinY +outerSquareSize;

        innerSquareMinX=innerSquareX;
        innerSquareMinY=innerSquareY;
        innerSquareMaxX=innerSquareMinX+innerSquareSize;
        innerSquareMaxY=innerSquareMinY+innerSquareSize;

        this.synchro=synchro;
    }

    /**
     * Uruchamia wątek. Wywołuje metodę letBallGo()
     * @see #letBallGo()
     */
    @Override
    public void run() {
        letBallGo();
    }

    /**
     *
     * @return współrzędna X lewej zewnętrznej ściany.
     */
    int getOuterSquareMinX()
    {
        return outerSquareMinX;
    }

    /**
     *
     * @return współrzędna Y górnej zewnętrznej ściany.
     */
    int getOuterSquareMinY()
    {
        return outerSquareMinY;
    }

    /**
     *
     * @return współrzędna X prawej zewnętrznej ściany.
     */
    int getOuterSquareMaxX()
    {
        return outerSquareMaxX;
    }

    /**
     *
     * @return współrzędna Y dolnej zewnętrznej ściany.
     */
    int getOuterSquareMaxY()
    {
        return outerSquareMaxY;
    }

    /**
     *
     * @return współrzędna X lewej wewnętrznej ściany.
     */
    int getInnerSquareMinX()
    {
        return innerSquareMinX;
    }

    /**
     *
     * @return współrzędna Y górnej wewnętrznej ściany.
     */
    int getInnerSquareMinY()
    {
        return innerSquareMinY;
    }

    /**
     *
     * @return współrzędna X prawej wewnętrznej ściany.
     */
    int getInnerSquareMaxX()
    {
        return innerSquareMaxX;
    }

    /**
     *
     * @return współrzędna Y dolnej wewnętrznej ściany.
     */
    int getInnerSquareMaxY()
    {
        return innerSquareMaxY;
    }

    /**
     * Metoda co trzy sekundy wpuszcza jedną kulkę, pod warunkiem, że wewnątrz jest mniej niż 5 kulek.
     */
    synchronized private void letBallGo()
    {
        while (true)
        {
            try {
                sleep(3000);

                if(bcc.getClientsInside()<bcc.getMaxAmountOfClientsInside()) {
                    System.out.println("Wybudzam po raz: "+ (bcc.getClientsInside()+1));

                    synchro.syncro("Box");
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}