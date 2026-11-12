package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GcdTest {
    @Test
    @DisplayName("G1 zeroIterations: x 13->20; y 13->15 F")
    void zeroIterations() {
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    @Test
    @DisplayName("G2 twoIterations: x 13->16,17->16,17->20; y 13->15/16/17,18->15/16/17; tmp 16->18")
    void twoIterations() {
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }
}
