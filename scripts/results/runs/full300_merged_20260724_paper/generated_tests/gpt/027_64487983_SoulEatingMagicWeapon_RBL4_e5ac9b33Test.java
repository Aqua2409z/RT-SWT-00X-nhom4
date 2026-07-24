
package com.iluwatar.bridge;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SoulEatingMagicWeapon_RBL4_e5ac9b33Test {

    private SoulEatingMagicWeaponImpl mockImp;
    private SoulEatingMagicWeapon weapon;

    @Before
    public void setUp() {
        mockImp = mock(SoulEatingMagicWeaponImpl.class);
        weapon = new SoulEatingMagicWeapon(mockImp);
    }

    @Test
    public void testWield() {
        weapon.wield();
        verify(mockImp).wieldImp();
    }

    @Test
    public void testSwing() {
        weapon.swing();
        verify(mockImp).swingImp();
    }

    @Test
    public void testUnwield() {
        weapon.unwield();
        verify(mockImp).unwieldImp();
    }

    @Test
    public void testEatSoul() {
        weapon.eatSoul();
        verify(mockImp).eatSoulImp();
    }

    @Test
    public void testGetImp() {
        SoulEatingMagicWeaponImpl imp = weapon.getImp();
        assertSame(mockImp, imp);
    }
}
