
package com.iluwatar.bridge;

import org.junit.Test;

public class App_RBL4Test_58f65afc {

    @Test
    public void testBlindingMagicWeapon() {
        BlindingMagicWeapon blindingMagicWeapon = new BlindingMagicWeapon(new Excalibur());
        blindingMagicWeapon.wield();
        blindingMagicWeapon.blind();
        blindingMagicWeapon.swing();
        blindingMagicWeapon.unwield();
    }

    @Test
    public void testFlyingMagicWeapon() {
        FlyingMagicWeapon flyingMagicWeapon = new FlyingMagicWeapon(new Mjollnir());
        flyingMagicWeapon.wield();
        flyingMagicWeapon.fly();
        flyingMagicWeapon.swing();
        flyingMagicWeapon.unwield();
    }

    @Test
    public void testSoulEatingMagicWeapon() {
        SoulEatingMagicWeapon soulEatingMagicWeapon = new SoulEatingMagicWeapon(new Stormbringer());
        soulEatingMagicWeapon.wield();
        soulEatingMagicWeapon.swing();
        soulEatingMagicWeapon.eatSoul();
        soulEatingMagicWeapon.unwield();
    }
}
