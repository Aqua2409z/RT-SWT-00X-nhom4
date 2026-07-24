package org.sonar.plugins.jproperties;

import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.Plugin.Context;

import static org.mockito.Mockito.verify;

public class JavaPropertiesPlugin_RBL4_f9bc17aaTest {

    @Test
    public void testDefine() {
        // Arrange
        Context context = Mockito.mock(Context.class);
        JavaPropertiesPlugin plugin = new JavaPropertiesPlugin();

        // Act
        plugin.define(context);

        // Assert
        verify(context).addExtensions(
            JavaPropertiesLanguage.class,
            JavaPropertiesSquidSensor.class,
            JavaPropertiesProfile.class,
            GenericJavaPropertiesRulesDefinition.class,
            SonarScannerJavaPropertiesRulesDefinition.class
        );
    }
}
