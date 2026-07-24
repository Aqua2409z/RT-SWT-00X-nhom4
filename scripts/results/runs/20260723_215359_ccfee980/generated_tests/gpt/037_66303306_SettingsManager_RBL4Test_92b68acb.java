
package com.bazaarvoice.emodb.web.settings;

import com.bazaarvoice.emodb.cachemgr.api.CacheHandle;
import com.bazaarvoice.emodb.cachemgr.api.CacheRegistry;
import com.bazaarvoice.emodb.sor.api.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class SettingsManager_RBL4Test_92b68acb {

    private SettingsManager settingsManager;
    private DataStore dataStore;
    private CacheRegistry cacheRegistry;
    private CacheHandle cacheHandle;

    @BeforeMethod
    public void setUp() {
        dataStore = Mockito.mock(DataStore.class);
        cacheRegistry = Mockito.mock(CacheRegistry.class);
        cacheHandle = Mockito.mock(CacheHandle.class);
        Mockito.when(cacheRegistry.register(Mockito.anyString(), Mockito.any(), Mockito.anyBoolean())).thenReturn(cacheHandle);
        
        settingsManager = new SettingsManager(() -> dataStore, "settingsTable", "settingsTablePlacement", cacheRegistry);
    }

    @Test
    public void testRegisterAndGetSetting() {
        String settingName = "testSetting";
        String defaultValue = "defaultValue";

        Setting<String> setting = settingsManager.register(settingName, String.class, defaultValue);
        Assert.assertNotNull(setting);
        Assert.assertEquals(setting.get(), defaultValue);
    }

    @Test
    public void testSetAndGetSetting() {
        String settingName = "testSetting";
        String defaultValue = "defaultValue";
        Setting<String> setting = settingsManager.register(settingName, String.class, defaultValue);

        String newValue = "newValue";
        setting.set(newValue);

        Mockito.when(dataStore.get(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(Map.of("json", "{\"value\":\"" + newValue + "\"}", "settingVersion", 1));
        Assert.assertEquals(setting.get(), newValue);
    }

    @Test
    public void testGetAllSettings() {
        String settingName1 = "testSetting1";
        String defaultValue1 = "defaultValue1";
        String settingName2 = "testSetting2";
        String defaultValue2 = "defaultValue2";

        settingsManager.register(settingName1, String.class, defaultValue1);
        settingsManager.register(settingName2, String.class, defaultValue2);

        Map<String, Object> allSettings = settingsManager.getAll();
        Assert.assertEquals(allSettings.size(), 2);
        Assert.assertEquals(allSettings.get(settingName1), defaultValue1);
        Assert.assertEquals(allSettings.get(settingName2), defaultValue2);
    }

    @Test
    public void testGetSettingWithTypeReference() {
        String settingName = "testSetting";
        String defaultValue = "defaultValue";
        settingsManager.register(settingName, new TypeReference<String>() {}, defaultValue);

        Setting<String> setting = settingsManager.getSetting(settingName, new TypeReference<String>() {});
        Assert.assertNotNull(setting);
        Assert.assertEquals(setting.get(), defaultValue);
    }

    @Test
    public void testGetSettingNotRegistered() {
        Setting<String> setting = settingsManager.getSetting("nonExistentSetting", String.class);
        Assert.assertNull(setting);
    }
}
