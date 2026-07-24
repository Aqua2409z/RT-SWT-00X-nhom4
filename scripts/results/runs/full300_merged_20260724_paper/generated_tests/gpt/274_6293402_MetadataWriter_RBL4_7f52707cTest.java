package com.vaadin.server.communication;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.server.SystemMessages;
import com.vaadin.ui.UI;
import com.vaadin.server.VaadinSession;

public class MetadataWriter_RBL4_7f52707cTest {

    private MetadataWriter metadataWriter;
    private UI ui;
    private Writer writer;
    private SystemMessages messages;

    @Before
    public void setUp() {
        metadataWriter = new MetadataWriter();
        ui = mock(UI.class);
        writer = new StringWriter();
        messages = mock(SystemMessages.class);
    }

    @Test
    public void testWriteWithRepaintAll() throws IOException {
        when(ui.getSession()).thenReturn(mock(VaadinSession.class));
        when(ui.getSession().getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(ui.getSession().getSession().getMaxInactiveInterval()).thenReturn(30);
        when(messages.isSessionExpiredNotificationEnabled()).thenReturn(true);
        when(messages.getSessionExpiredURL()).thenReturn("http://example.com");

        metadataWriter.write(ui, writer, true, false, messages);
        
        String expected = "{\"repaintAll\":true,\"timedRedirect\":{\"interval\":45,\"url\":\"http:\\/\\/example.com\"}}";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteWithAsync() throws IOException {
        when(ui.getSession()).thenReturn(mock(VaadinSession.class));
        when(ui.getSession().getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(ui.getSession().getSession().getMaxInactiveInterval()).thenReturn(30);
        when(messages.isSessionExpiredNotificationEnabled()).thenReturn(true);
        when(messages.getSessionExpiredURL()).thenReturn("http://example.com");

        metadataWriter.write(ui, writer, false, true, messages);
        
        String expected = "{\"async\":true,\"timedRedirect\":{\"interval\":45,\"url\":\"http:\\/\\/example.com\"}}";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteWithNoSessionExpiredMessage() throws IOException {
        when(ui.getSession()).thenReturn(mock(VaadinSession.class));
        when(ui.getSession().getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(ui.getSession().getSession().getMaxInactiveInterval()).thenReturn(30);
        when(messages.isSessionExpiredNotificationEnabled()).thenReturn(true);
        when(messages.getSessionExpiredURL()).thenReturn(null);

        metadataWriter.write(ui, writer, false, false, messages);
        
        String expected = "{\"timedRedirect\":{\"interval\":45,\"url\":\"\"}}";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testWriteWithNoMessages() throws IOException {
        when(ui.getSession()).thenReturn(mock(VaadinSession.class));
        when(ui.getSession().getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(ui.getSession().getSession().getMaxInactiveInterval()).thenReturn(30);

        metadataWriter.write(ui, writer, false, false, null);
        
        String expected = "{}";
        assertEquals(expected, writer.toString());
    }

    @Test(expected = IOException.class)
    public void testWriteThrowsIOException() throws IOException {
        when(ui.getSession()).thenReturn(mock(VaadinSession.class));
        when(ui.getSession().getSession()).thenReturn(mock(javax.servlet.http.HttpSession.class));
        when(ui.getSession().getSession().getMaxInactiveInterval()).thenReturn(30);
        when(messages.isSessionExpiredNotificationEnabled()).thenReturn(true);
        when(messages.getSessionExpiredURL()).thenReturn("http://example.com");

        // Simulate an IOException by using a writer that throws an exception
        Writer faultyWriter = mock(Writer.class);
        doThrow(new IOException()).when(faultyWriter).write(anyString());

        metadataWriter.write(ui, faultyWriter, true, false, messages);
    }
}
