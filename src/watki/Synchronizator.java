package watki;

/**
 * Klasa reprezentująca monitor pomiędzy kulkami a pudełkiem.
 */
public class Synchronizator {

    /**
     * Monitor. Wywołany na kulce zatrzymuje ją, natomiast wywołany w pudełku budzi kulkę.
     * @param myNameIs String będący nazwą klasy, której obiekt wywołuje metodę.
     */
    synchronized void syncro(String myNameIs) {
        if(myNameIs.equals("Ball")) {
            try {
                wait();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        else if(myNameIs.equals("Box")) {
            try {
                notify();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            System.out.println("Wrong argument");
            System.exit(1);
        }
    }
}
