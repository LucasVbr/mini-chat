package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolutionDeNomTest {

    @Test
    void getIPAddress() {

        final int EXPECTED = 1;
        final int ACTUAL = 0;
        final String[][] FIXTURE = {
                {"localhost", "127.0.0.1"},
                {"www.univ-jfc.fr", "194.57.185.14"},
        };

        for (String[] test : FIXTURE) {
            assertEquals(test[EXPECTED], ResolutionDeNom.getIPAddress(test[ACTUAL]));
            System.out.printf("%s -> %s : OK\n", test[ACTUAL], test[EXPECTED]);
        }

    }
}