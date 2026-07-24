
package com.iluwatar.bridge;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FlyingMagicWeapon_RBL4_65b25ee1Test {

    private FlyingMagicWeaponImpl mockImp;
    private FlyingMagicWeapon flyingMagicWeapon;

    @Before
    public void setUp() {
        mockImp = mock(FlyingMagicWeaponImpl.class);
        flyingMagicWeapon = new FlyingMagicWeapon(mockImp);
    }

    @Test
    public void testWield() {
        flyingMagicWeapon.wield();
        verify(mockImp).wieldImp();
    }

    @Test
    public void testSwing() {
        flyingMagicWeapon.swing();
        verify(mockImp).swingImp();
    }

    @Test
    public void testUnwield() {
        flyingMagicWeapon.unwield();
        verify(mockImp).unwieldImp();
    }

    @Test
    public void testFly() {
        flyingMagicWeapon.fly();
        verify(mockImp).flyImp();
    }

    @Test
    public void testGetImp() {
        FlyingMagicWeaponImpl imp = flyingMagicWeapon.getImp();
        assertSame(mockImp, imp);
    }
}
