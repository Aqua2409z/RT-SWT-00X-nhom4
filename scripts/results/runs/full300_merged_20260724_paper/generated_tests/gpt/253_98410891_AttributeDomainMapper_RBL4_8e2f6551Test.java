package com.softavail.commsrouter.domain.dto.mappers;

import com.softavail.commsrouter.api.dto.model.skill.AttributeDomainDto;
import com.softavail.commsrouter.api.dto.model.skill.AttributeDomainDtoVisitor;
import com.softavail.commsrouter.api.dto.model.skill.AttributeType;
import com.softavail.commsrouter.api.dto.model.skill.BoolAttributeDomainDto;
import com.softavail.commsrouter.api.dto.model.skill.EnumerationAttributeDomainDto;
import com.softavail.commsrouter.api.dto.model.skill.NumberAttributeDomainDto;
import com.softavail.commsrouter.api.dto.model.skill.NumberInterval;
import com.softavail.commsrouter.api.dto.model.skill.NumberIntervalBoundary;
import com.softavail.commsrouter.api.dto.model.skill.StringAttributeDomainDto;
import com.softavail.commsrouter.domain.AttributeDomain;
import com.softavail.commsrouter.domain.AttributeDomainDefinition;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class AttributeDomainMapper_RBL4_8e2f6551Test {

    @Test
    public void testGetAttributeTypeEnumeration() {
        AttributeDomainDefinition definition = new AttributeDomainDefinition();
        definition.setEnumValue("value");
        assertEquals(AttributeType.enumeration, AttributeDomainMapper.getAttributeType(definition));
    }

    @Test
    public void testGetAttributeTypeNumber() {
        AttributeDomainDefinition definition = new AttributeDomainDefinition();
        definition.setBoundary(new NumberIntervalBoundary(1, true));
        assertEquals(AttributeType.number, AttributeDomainMapper.getAttributeType(definition));
    }

    @Test
    public void testGetAttributeTypeString() {
        AttributeDomainDefinition definition = new AttributeDomainDefinition();
        definition.setRegex(".*");
        assertEquals(AttributeType.string, AttributeDomainMapper.getAttributeType(definition));
    }

    @Test(expected = RuntimeException.class)
    public void testGetAttributeTypeInvalid() {
        AttributeDomainDefinition definition = new AttributeDomainDefinition();
        AttributeDomainMapper.getAttributeType(definition);
    }

    @Test
    public void testToDtoEnumeration() {
        AttributeDomain domain = new AttributeDomain();
        domain.setType(AttributeType.enumeration);
        domain.setDefinitions(Arrays.asList(new AttributeDomainDefinition("value1"), new AttributeDomainDefinition("value2")));
        
        EnumerationAttributeDomainDto dto = (EnumerationAttributeDomainDto) new AttributeDomainMapper().toDto(domain);
        assertEquals(new HashSet<>(Arrays.asList("value1", "value2")), dto.getValues());
    }

    @Test
    public void testToDtoNumber() {
        AttributeDomain domain = new AttributeDomain();
        domain.setType(AttributeType.number);
        domain.setDefinitions(Arrays.asList(new AttributeDomainDefinition(new NumberIntervalBoundary(1, true)), new AttributeDomainDefinition(new NumberIntervalBoundary(5, false))));
        
        NumberAttributeDomainDto dto = (NumberAttributeDomainDto) new AttributeDomainMapper().toDto(domain);
        assertEquals(1, dto.getIntervals().size());
        assertEquals(new NumberIntervalBoundary(1, true), dto.getIntervals().get(0).getLow());
        assertEquals(new NumberIntervalBoundary(5, false), dto.getIntervals().get(0).getHigh());
    }

    @Test
    public void testToDtoString() {
        AttributeDomain domain = new AttributeDomain();
        domain.setType(AttributeType.string);
        domain.setDefinitions(Arrays.asList(new AttributeDomainDefinition(".*")));
        
        StringAttributeDomainDto dto = (StringAttributeDomainDto) new AttributeDomainMapper().toDto(domain);
        assertEquals(".*", dto.getRegex());
    }

    @Test
    public void testToDtoBool() {
        AttributeDomain domain = new AttributeDomain();
        domain.setType(AttributeType.bool);
        domain.setDefinitions(Arrays.asList());
        
        BoolAttributeDomainDto dto = (BoolAttributeDomainDto) new AttributeDomainMapper().toDto(domain);
        assertNotNull(dto);
    }

    @Test
    public void testFromDtoEnumeration() {
        EnumerationAttributeDomainDto dto = new EnumerationAttributeDomainDto(new HashSet<>(Arrays.asList("value1", "value2")));
        AttributeDomain domain = new AttributeDomainMapper().fromDto(dto);
        
        assertEquals(AttributeType.enumeration, domain.getType());
        assertEquals(2, domain.getDefinitions().size());
    }

    @Test
    public void testFromDtoNumber() {
        NumberAttributeDomainDto dto = new NumberAttributeDomainDto(Arrays.asList(new NumberInterval(new NumberIntervalBoundary(1, true), null)));
        AttributeDomain domain = new AttributeDomainMapper().fromDto(dto);
        
        assertEquals(AttributeType.number, domain.getType());
        assertEquals(1, domain.getDefinitions().size());
    }

    @Test
    public void testFromDtoString() {
        StringAttributeDomainDto dto = new StringAttributeDomainDto(".*");
        AttributeDomain domain = new AttributeDomainMapper().fromDto(dto);
        
        assertEquals(AttributeType.string, domain.getType());
        assertEquals(".*", domain.getDefinitions().get(0).getRegex());
    }

    @Test
    public void testFromDtoBool() {
        BoolAttributeDomainDto dto = new BoolAttributeDomainDto();
        AttributeDomain domain = new AttributeDomainMapper().fromDto(dto);
        
        assertEquals(AttributeType.bool, domain.getType());
        assertTrue(domain.getDefinitions().isEmpty());
    }
}
