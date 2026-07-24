
package com.iluwatar.dependency.injection;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class GuiceWizard_RBL4_f36a1968Test {

    private Tobacco tobacco;
    private GuiceWizard guiceWizard;

    @Before
    public void setUp() {
        tobacco = mock(Tobacco.class);
        guiceWizard = new GuiceWizard(tobacco);
    }

    @Test
    public void testSmoke() {
        guiceWizard.smoke();
        verify(tobacco, times(1)).smoke(guiceWizard);
    }
}
