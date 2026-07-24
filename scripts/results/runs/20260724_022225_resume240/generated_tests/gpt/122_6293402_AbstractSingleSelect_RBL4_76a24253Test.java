package com.vaadin.ui;

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.shared.Registration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;

public class AbstractSingleSelect_RBL4_76a24253Test {

    private AbstractSingleSelect<String> select;

    @Before
    public void setUp() {
        select = new AbstractSingleSelect<String>() {
            @Override
            protected DataCommunicator<String> getDataCommunicator() {
                return new DataCommunicator<>(null);
            }
        };
    }

    @Test
    public void testSetSelectedItem() {
        select.setSelectedItem("Item1");
        assertEquals("Item1", select.getValue());
    }

    @Test
    public void testSetSelectedItemNull() {
        select.setSelectedItem("Item1");
        select.setSelectedItem(null);
        assertNull(select.getValue());
    }

    @Test
    public void testGetSelectedItem() {
        select.setSelectedItem("Item1");
        Optional<String> selectedItem = select.getSelectedItem();
        assertTrue(selectedItem.isPresent());
        assertEquals("Item1", selectedItem.get());
    }

    @Test
    public void testGetSelectedItemWhenNone() {
        Optional<String> selectedItem = select.getSelectedItem();
        assertFalse(selectedItem.isPresent());
    }

    @Test
    public void testIsSelected() {
        select.setSelectedItem("Item1");
        assertTrue(select.isSelected("Item1"));
        assertFalse(select.isSelected("Item2"));
    }

    @Test
    public void testAddSelectionListener() {
        final boolean[] eventFired = {false};
        select.addSelectionListener(event -> eventFired[0] = true);
        select.setSelectedItem("Item1");
        assertTrue(eventFired[0]);
    }

    @Test
    public void testSetValue() {
        select.setValue("Item1");
        assertEquals("Item1", select.getValue());
    }

    @Test
    public void testSetValueNull() {
        select.setValue("Item1");
        select.setValue(null);
        assertNull(select.getValue());
    }

    @Test
    public void testAddValueChangeListener() {
        final String[] oldValue = {null};
        final String[] newValue = {null};
        select.addValueChangeListener(event -> {
            oldValue[0] = event.getOldValue();
            newValue[0] = event.getValue();
        });
        select.setValue("Item1");
        assertEquals(null, oldValue[0]);
        assertEquals("Item1", newValue[0]);
    }

    @Test
    public void testSetRequiredIndicatorVisible() {
        select.setRequiredIndicatorVisible(true);
        assertTrue(select.isRequiredIndicatorVisible());
        select.setRequiredIndicatorVisible(false);
        assertFalse(select.isRequiredIndicatorVisible());
    }

    @Test
    public void testSetReadOnly() {
        select.setReadOnly(true);
        assertTrue(select.isReadOnly());
        select.setReadOnly(false);
        assertFalse(select.isReadOnly());
    }
}
