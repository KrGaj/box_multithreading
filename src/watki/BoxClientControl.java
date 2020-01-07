package watki;

/**
 * Klasa zapewnia syncronizowane metody konieczne do prawidłowej kontroli liczby kulek znajdującej się w pudełku.
 */

public class BoxClientControl {
    private final int maxAmountOfClientsInside =4; //maksymalna liczba kulek znajdujących się w pudełku w tym samym czasie
    private int clientsInside=0;  //obecna liczba kulek w pudełku

    /**
     * Uruchamiane, gdy kulka opuszcza pudełko. Zmniejsza liczbę kulek w pudełku o 1.
     */
    synchronized void clientExit() {
        --clientsInside;
    }

    /**
     * Zwraca maksymalną dopuszczalną liczbę kulek w pudełku.
     */
    synchronized int getMaxAmountOfClientsInside() {
        return maxAmountOfClientsInside;
    }

    /**
     * Zwraca obecną liczbę kulek w pudełku.
     */
    synchronized int getClientsInside() {
        return clientsInside;
    }

    /**
     * Uruchamiane, gdy kulka wchodzi do pudełka. Zwiększa liczbę kulek w pudełku o 1.
     */
    synchronized void clientEnter() {
        ++clientsInside;
    }
}
