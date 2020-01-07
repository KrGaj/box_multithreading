package watki;

import java.util.Random;
import java.lang.Math;

/**
 * Klasa przechowująca współrzędne kulki oraz zapewniająca synchronizowany dostęp do nich.
 */

public class BallCoordinates {
    private double position_x=20;   //pozycja w osi X
    private double position_y=20;   //pozycja w osi Y
    private double dx=5;    //przesunięcie w X
    private double dy;      //przesunięcie w Y
    private final double speed=0.3; //szybkość
    private boolean isMoving=true;  //"blokada" na poruszanie się kulki
    private boolean needTicket=false;   //czy kulka potrze
    private boolean isInTheBox=false;   //czy kulka znajduje się w pudełku

    /**
     * Konstruktor. Ustala kąt poruszania się kulki oraz przesunięcie w Y.
     */
    BallCoordinates() {
        Random generator=new Random();
        double angle=generator.nextInt(15)+60;
        dy=dx*Math.tan(angle);
    }

    /**
     * @return współrzędna X kulki
     */
    synchronized double getPosition_x() {
        return position_x;
    }

    /**
     * @return współrzędna Y kulki
     */
    synchronized double getPosition_y() {
        return position_y;
    }

    /**
     * @return wartość przesunięcia w płaszczyźnie X
     */
    synchronized double getDx() {
        return dx;
    }

    /**
     * @return wartość przesunięcia w płaszczyźnie Y
     */
    synchronized double getDy() {
        return dy;
    }

    /**
     * @return wartość prędkości przesunięcia
     */
    synchronized double getSpeed() {
        return speed;
    }

    /**
     * Ustawia nową wartość współrzędnej X.
     * @param pos_x nowa wartość X
     */
    synchronized void setPosition_x(double pos_x) {
        position_x=pos_x;
    }

    /**
     * Ustawia nową wartość współrzędnej Y.
     * @param pos_y nowa wartość Y
     */
    synchronized void setPosition_y(double pos_y) {
        position_y=pos_y;
    }

    /**
     * Zmienia znak wartości przesunięcia w płaszczyźnie X na ujemny.
     */
    synchronized void setMinusDx() {
        dx*=-1;
    }

    /**
     * Zmienia znak wartości przesunięcia w płaszczyźnie Y na ujemny.
     */
    synchronized void setMinusDy() {
        dy*=-1;
    }

    /**
     * Zwraca wartość informującą, czy kulka jest w ruchu.
     * @return true gdy kulka jest w ruchu, false w przeciwnym wypadku
     */
    synchronized boolean isMoving() {
        return isMoving;
    }

    /**
     * Zwraca wartość informującą, czy kulka oczekuje na wpuszczenie.
     * @return true, gdy kulka oczekuje na wpuszczenie, false w przeciwnym wypadku
     */
    synchronized boolean needTicket() {
        return needTicket;
    }

    /**
     * Zwraca wartość informującą, czy kulka porusza się wewnątrz pudełka.
     * @return true, gdy kulka znajduje się w pudełku, false w przeciwnym wypadku
     */
    synchronized boolean isInTheBox() {
        return isInTheBox;
    }

    /**
     * Zmienia informacje dotyczące oczekiwania na wpuszczenie.
     * @param needTicket true, gdy kulka oczekuje na wpuszczenie, false w przeciwnym wypadku
     */
    synchronized void setTicketNeed(boolean needTicket) {
        this.needTicket=needTicket;
    }

    /**
     * Zmienia informacje dotyczące poruszania się wewnątrz pudełka.
     * @param isInTheBox true, gdy kulka znajduje się w pudełku, false w przeciwnym wypadku
     */
    synchronized void setIsInTheBox(boolean isInTheBox) {
        this.isInTheBox=isInTheBox;
    }

    /**
     * Zmienia informację mówiącą, czy kulka ma się poruszać.
     * @param isMoving true gdy kulka ma być w ruchu, false w przeciwnym wypadku
     */
    synchronized void setMoving(boolean isMoving) {
        this.isMoving=isMoving;
    }
}
