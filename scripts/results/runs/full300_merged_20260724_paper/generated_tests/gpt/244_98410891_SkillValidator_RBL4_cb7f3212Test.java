package com.softavail.commsrouter.api.service;

import com.softavail.commsrouter.api.dto.model.RouterObjectRef;
import com.softavail.commsrouter.api.dto.model.attribute.*;
import com.softavail.commsrouter.api.dto.model.skill.*;
import com.softavail.commsrouter.api.exception.BadValueException;
import com.softavail.commsrouter.api.exception.CommsRouterException;
import com.softavail.commsrouter.api.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class SkillValidator_RBL4_cb7f3212Test {

    private CoreSkillService coreSkillService;
    private SkillValidator skillValidator;

    @Before
    public void setUp() {
        coreSkillService = Mockito.mock(CoreSkillService.class);
        skillValidator = new SkillValidator(coreSkillService);
    }

    @Test(expected = BadValueException.class)
    public void testValidateSkillNotFound() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new StringAttributeValueDto("value"));

        when(coreSkillService.get(any(RouterObjectRef.class))).thenThrow(new NotFoundException());

        skillValidator.validate(capabilities, routerRef);
    }

    @Test(expected = BadValueException.class)
    public void testValidateSingleValueNotAllowed() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        SkillDto skillDto = new SkillDto();
        skillDto.setMultivalue(false);
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new ArrayOfStringsAttributeValueDto(new String[]{"value1", "value2"}));

        skillValidator.validate(capabilities, routerRef);
    }

    @Test(expected = BadValueException.class)
    public void testValidateInvalidType() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        SkillDto skillDto = new SkillDto();
        skillDto.setDomain(new StringAttributeDomainDto());
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new DoubleAttributeValueDto(1.0));

        skillValidator.validate(capabilities, routerRef);
    }

    @Test
    public void testValidateValidSingleValue() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        SkillDto skillDto = new SkillDto();
        skillDto.setMultivalue(false);
        skillDto.setDomain(new StringAttributeDomainDto());
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new StringAttributeValueDto("validValue"));

        skillValidator.validate(capabilities, routerRef);
    }

    @Test(expected = BadValueException.class)
    public void testValidateInvalidEnumerationValue() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        EnumerationAttributeDomainDto domainDto = new EnumerationAttributeDomainDto();
        domainDto.setValues(List.of("value1", "value2"));
        SkillDto skillDto = new SkillDto();
        skillDto.setDomain(domainDto);
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new StringAttributeValueDto("invalidValue"));

        skillValidator.validate(capabilities, routerRef);
    }

    @Test
    public void testValidateValidEnumerationValue() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        EnumerationAttributeDomainDto domainDto = new EnumerationAttributeDomainDto();
        domainDto.setValues(List.of("value1", "value2"));
        SkillDto skillDto = new SkillDto();
        skillDto.setDomain(domainDto);
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new StringAttributeValueDto("value1"));

        skillValidator.validate(capabilities, routerRef);
    }

    @Test(expected = BadValueException.class)
    public void testValidateInvalidDoubleValue() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        NumberAttributeDomainDto domainDto = new NumberAttributeDomainDto();
        domainDto.setIntervals(List.of(new NumberInterval(0.0, 10.0)));
        SkillDto skillDto = new SkillDto();
        skillDto.setDomain(domainDto);
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new DoubleAttributeValueDto(15.0));

        skillValidator.validate(capabilities, routerRef);
    }

    @Test
    public void testValidateValidDoubleValue() throws CommsRouterException {
        String skillName = "testSkill";
        String routerRef = "routerRef";
        NumberAttributeDomainDto domainDto = new NumberAttributeDomainDto();
        domainDto.setIntervals(List.of(new NumberInterval(0.0, 10.0)));
        SkillDto skillDto = new SkillDto();
        skillDto.setDomain(domainDto);
        when(coreSkillService.get(any(RouterObjectRef.class))).thenReturn(skillDto);

        AttributeGroupDto capabilities = new AttributeGroupDto();
        capabilities.put(skillName, new DoubleAttributeValueDto(5.0));

        skillValidator.validate(capabilities, routerRef);
    }
}
