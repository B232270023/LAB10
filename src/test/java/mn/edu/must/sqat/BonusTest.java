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

    @Test
    @DisplayName("B3 singleDayAtThreshold: best 37->42 T,43->53 F; score 51->56")
    void singleDayAtThreshold() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{1}, 1));
    }

    @Test
    @DisplayName("B4 fiveDaysBelowThreshold: present 40->50 F; score 49->54->56; best 43->53 T")
    void fiveDaysBelowThreshold() {
        assertEquals(3, DataFlowTargets.bonus(new int[]{1, 1, 1, 1, 1}, 6));
    }

    @Test
    @DisplayName("B5 nullAttendance: attendance 31->32 T; threshold use is short-circuited")
    void nullAttendance() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> DataFlowTargets.bonus(null, 0));
    }

    @Test
    @DisplayName("B6 negativeThreshold: attendance 31->32 F; threshold 31->32 T")
    void negativeThreshold() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> DataFlowTargets.bonus(new int[]{}, -1));
    }

    @Test
    @DisplayName("B7 allAbsentZeroThreshold: present 35->50 T; best 37->53 F; score 51->56")
    void allAbsentZeroThreshold() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{0, -2}, 0));
    }
}
