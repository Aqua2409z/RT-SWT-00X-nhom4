package com.vaadin.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.shared.ui.ui.UIState.LocaleData;
import com.vaadin.shared.ui.ui.UIState.LocaleServiceState;
import com.vaadin.ui.UI;

public class LocaleService_RBL4_e764fd86Test {

    private UI mockUI;
    private LocaleServiceState mockState;
    private LocaleService localeService;

    @Before
    public void setUp() {
        mockUI = mock(UI.class);
        mockState = new LocaleServiceState();
        localeService = new LocaleService(mockUI, mockState);
    }

    @Test
    public void testGetUI() {
        assertEquals(mockUI, localeService.getUI());
    }

    @Test
    public void testAddLocale_NewLocale() {
        Locale locale = Locale.ENGLISH;
        localeService.addLocale(locale);
        assertEquals(1, mockState.localeData.size());
        assertEquals(locale.toString(), mockState.localeData.get(0).name);
    }

    @Test
    public void testAddLocale_ExistingLocale() {
        Locale locale = Locale.ENGLISH;
        localeService.addLocale(locale);
        localeService.addLocale(locale);
        assertEquals(1, mockState.localeData.size());
    }

    @Test
    public void testCreateLocaleData() {
        Locale locale = Locale.FRENCH;
        LocaleData localeData = localeService.createLocaleData(locale);
        
        assertEquals(locale.toString(), localeData.name);
        assertNotNull(localeData.shortMonthNames);
        assertNotNull(localeData.monthNames);
        assertNotNull(localeData.shortDayNames);
        assertNotNull(localeData.dayNames);
        assertNotNull(localeData.dateFormat);
        assertNotNull(localeData.hourMinuteDelimiter);
    }

    @Test
    public void testGetState_MarkAsDirty() {
        localeService.getState(true);
        verify(mockUI).markAsDirty();
    }

    @Test
    public void testGetState_NoMarkAsDirty() {
        localeService.getState(false);
        verify(mockUI, never()).markAsDirty();
    }
}
