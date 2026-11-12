package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BonusTest {
    @Test
    @DisplayName("B1 allDefsMixedRun: present 35/40->40,40->50; streak 36/41/46->41,41->42/43; best 37/43->42,43->53; score 51->54->56")
    void allDefsMixedRun() {
        assertEquals(8, DataFlowTargets.bonus(new int[]{1, 0, 1, 1, 1, 1, 1}, 6));
    }

    @Test
    @DisplayName("B2 allDefsEmpty: present 35->50; best 37->53; score 49->56")
    void allDefsEmpty() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{}, 1));
    }
}
