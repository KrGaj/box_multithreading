package watki;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

/**
 * Klasa reprezentuje wątek kulki poruszającej się po ekranie.
 */

public class Ball extends Thread {

    private Timeline timeline;
    private final int duration=10;

    private Box box;
    private BallCoordinates coords;

    private Synchronizator synchro;

    private Circle ball;    //kulka rysowana na ekranie

    private final int ballRadius=15;

    //ograniczenia "świata", po którym porusza się kulka
    private final int zeroLimit=0;
    private double worldLimit_x;
    private double worldLimit_y;

    //ograniczenia pudełka
    private double minBoxLimit_x;
    private double minBoxLimit_y;
    private double maxBoxLimit_x;
    private double maxBoxLimit_y;

    //obecnie używane limity
    private double usingMinLimit_x=zeroLimit;
    private double usingMinLimit_y=zeroLimit;
    private double usingMaxLimit_x;
    private double usingMaxLimit_y;

    private BoxClientControl bcc;

    /**
     * Konstruktor.
     * @param canvas panel, po którym porusza się kulka
     * @param box pudełko, z którym ma synchronizować się kulka
     * @param synchro monitor odpowiadający za synchronizację wątków
     * @param bcc monitor odpowiadający za prawidłowe wyliczanie kulek wewnątrz pudełka
     */
    Ball(Pane canvas, Box box, Synchronizator synchro, BoxClientControl bcc) {
        //paleta, z której są losowane kolory kulek
        Color[] colors={Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color. MAGENTA, Color.CADETBLUE, Color.CYAN,
                Color.GRAY, Color.LIME, Color.ORANGE, Color.ROYALBLUE, Color.SILVER, Color.NAVY, Color.DARKGREEN,
                Color.DARKGRAY};

        Bounds bounds;  //ograniczenia panelu, po którym poruszają się kulki

        coords=new BallCoordinates();

        this.bcc=bcc;
        this.box=box;

        bounds=canvas.getBoundsInLocal();

        this.synchro=synchro;

        worldLimit_x=bounds.getMaxX();
        worldLimit_y=bounds.getMaxY();

        usingMaxLimit_x=worldLimit_x;
        usingMaxLimit_y=worldLimit_y;

        Random generator=new Random();

        ball=new Circle(coords.getPosition_x(), coords.getPosition_y(), ballRadius, colors[generator.nextInt(colors.length)]);
        canvas.getChildren().add(ball);

        minBoxLimit_x=box.getInnerSquareMinX();
        minBoxLimit_y=box.getInnerSquareMinY();
        maxBoxLimit_x=box.getInnerSquareMaxX();
        maxBoxLimit_y=box.getInnerSquareMaxY();
    }

    /**
     * Metoda zmienia położenie kulki w pewnej przestrzeni. W przypadku zetknięcia się ze ścianą, kulka odbija się idealnie sprężyście.
     */
    void move()
    {
        if(coords.getPosition_x()-ballRadius<=usingMinLimit_x || coords.getPosition_x()+ballRadius>=usingMaxLimit_x)
        {
            coords.setMinusDx();
        }

        if(coords.getPosition_y()-ballRadius<=usingMinLimit_y || coords.getPosition_y()+ballRadius>=usingMaxLimit_y)
        {
            coords.setMinusDy();
        }

        if(coords.getPosition_x()+ballRadius>=box.getOuterSquareMinX() && coords.getPosition_x()-ballRadius<=box.getOuterSquareMaxX() &&
                coords.getPosition_y()+ballRadius>=box.getOuterSquareMinY() && coords.getPosition_y()-ballRadius<=box.getOuterSquareMaxY() &&
        !coords.isInTheBox()) {

            coords.setMoving(false);
            coords.setTicketNeed(true);
        }

        if((!(coords.getPosition_x()+ballRadius>=box.getOuterSquareMinX() && coords.getPosition_x()-ballRadius<=box.getOuterSquareMaxX() &&
                coords.getPosition_y()+ballRadius>=box.getOuterSquareMinY() && coords.getPosition_y()-ballRadius<=box.getOuterSquareMaxY())) &&
                coords.isInTheBox())
        {
            coords.setIsInTheBox(false);
        }

        coords.setPosition_x(coords.getPosition_x()+coords.getDx()*coords.getSpeed());
        coords.setPosition_y(coords.getPosition_y()+coords.getDy()*coords.getSpeed());
    }

    /**
     * Metoda aktualizuje pozycję obiektu klasy Ball.
     */
    void animate()
    {
        if(coords.isMoving())
        {
            move();
        }

        ball.setCenterX(coords.getPosition_x());
        ball.setCenterY(coords.getPosition_y());
    }

    /**
     * Metoda zmienia zakres, w jakim porusza się kulka.
     * @param hasTicket określa, czy kulka ma prawo wejść do pudełka
     */
    void changeLimits(boolean hasTicket)
    {
        if(hasTicket)
        {
            usingMinLimit_x=minBoxLimit_x;
            usingMaxLimit_x=maxBoxLimit_x;

            usingMinLimit_y=minBoxLimit_y;
            usingMaxLimit_y=maxBoxLimit_y;
        }
        else
        {
            usingMinLimit_x=usingMinLimit_y=zeroLimit;
            usingMaxLimit_x=worldLimit_x;
            usingMaxLimit_y=worldLimit_y;
        }
    }

    /**
     * Metoda uruchamia timeline oraz wątek kulki. Wątek kulki pracuje niezależnie i to na nim odbywa się synchronizacja z wątkiem pudełka.
     */
    @Override
    public void run() {

        timeline=new Timeline(new KeyFrame(Duration.millis(duration), actionEvent -> animate()));
        timeline.setCycleCount(Animation.INDEFINITE);   //animacja w nieskończoność
        timeline.play();

        int time=0;

        while(true) {
            try {
                sleep(10);
                if(coords.needTicket()) {
                    coords.setTicketNeed(false);
                    coords.setIsInTheBox(true);

                    synchro.syncro("Ball");

                    //takeTicket();
                    enterToBox();

                    System.out.println("Idę");
                    coords.setMoving(true);
                }

                else if(coords.isInTheBox())
                {
                    time+=10;
                }

                if(time==5000)  //w przybliżeniu 5 sekund
                {
                    time=0;
                    leaveTheBox();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metoda wprowadzająca kulkę do pudełka.
     * @throws InterruptedException w przypadku, gdy wątek został przerwany
     */
    private void enterToBox() throws InterruptedException {
        double enterDX=5;
        double enterDY=5;

        if(coords.getPosition_x()>box.getInnerSquareMaxX()) //prawa strona pudełka
        {
            enterDX*=-1;
        }
        if(coords.getPosition_y()>box.getInnerSquareMaxY()) //dół pudełka
        {
            enterDY*=-1;
        }

        //któreś współrzędne zgadzające się ze współrzędnymi wnętrza pudełka
        if(coords.getPosition_x()-ballRadius>box.getInnerSquareMinX() && coords.getPosition_x()+ballRadius<box.getInnerSquareMaxX())
        {
            enterDX=0;
        }
        if(coords.getPosition_y()-ballRadius>box.getInnerSquareMinY() && coords.getPosition_y()+ballRadius<box.getInnerSquareMaxY())
        {
            enterDY=0;
        }

        //wprowadzenie kulki do pudełka
        for(int i=0; i<35; ++i)
        {
            sleep(10);
            coords.setPosition_x(coords.getPosition_x()+enterDX*coords.getSpeed());
            coords.setPosition_y(coords.getPosition_y()+enterDY*coords.getSpeed());
        }

        bcc.clientEnter();
        changeLimits(true);
    }

    /**
     * Metoda "wypuszczająca" kulkę z pudełka
     */
    private void leaveTheBox() {
        coords.setMoving(false);
        changeLimits(false);
        bcc.clientExit();
        coords.setMoving(true);
    }
}