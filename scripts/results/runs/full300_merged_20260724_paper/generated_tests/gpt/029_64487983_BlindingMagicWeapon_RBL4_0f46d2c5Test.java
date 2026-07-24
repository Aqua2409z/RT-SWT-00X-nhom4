
package com.iluwatar.bridge;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class BlindingMagicWeapon_RBL4_0f46d2c5Test {

    private BlindingMagicWeaponImpl mockImp;
    private BlindingMagicWeapon blindingMagicWeapon;

    @Before
    public void setUp() {
        mockImp = mock(BlindingMagicWeaponImpl.class);
        blindingMagicWeapon = new BlindingMagicWeapon(mockImp);
    }

    @Test
    public void testWield() {
        blindingMagicWeapon.wield();
        verify(mockImp).wieldImp();
    }

    @Test
    public void testSwing() {
        blindingMagicWeapon.swing();
        verify(mockImp).swingImp();
    }

    @Test
    public void testUnwield() {
        blindingMagicWeapon.unwield();
        verify(mockImp).unwieldImp();
    }

    @Test
    public void testBlind() {
        blindingMagicWeapon.blind();
        verify(mockImp).blindImp();
    }

    @Test
    public void testGetImp() {
        BlindingMagicWeaponImpl imp = blindingMagicWeapon.getImp();
        assertSame(mockImp, imp);
    }
}
