package org.junithelper.core.extractor;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.junithelper.core.config.Configuration;
import org.junithelper.core.meta.ClassMeta;
import org.junithelper.core.meta.ConstructorMeta;

import java.util.List;

public class ConstructorMetaExtractor_RBL4Test_aefe9473 {

    private ConstructorMetaExtractor extractor;
    private Configuration config;

    @Before
    public void setUp() {
        config = new Configuration();
        extractor = new ConstructorMetaExtractor(config);
    }

    @Test
    public void testExtractSingleConstructor() {
        String sourceCode = "public class ConstructorMetaExtractor_RBL4Test_aefe9473 { public TestClass(int a) {} }";
        extractor.initialize(sourceCode);
        List<ConstructorMeta> constructors = extractor.extract(sourceCode);
        
        Assert.assertEquals(1, constructors.size());
        ConstructorMeta constructor = constructors.get(0);
        Assert.assertEquals("TestClass", constructor.accessModifier.toString());
        Assert.assertEquals(1, constructor.argTypes.size());
        Assert.assertEquals("int", constructor.argTypes.get(0).name);
        Assert.assertEquals("a", constructor.argNames.get(0));
    }

    @Test
    public void testExtractMultipleConstructors() {
        String sourceCode = "public class ConstructorMetaExtractor_RBL4Test_aefe9473 { public TestClass() {} public TestClass(int a, String b) {} }";
        extractor.initialize(sourceCode);
        List<ConstructorMeta> constructors = extractor.extract(sourceCode);
        
        Assert.assertEquals(2, constructors.size());
        
        ConstructorMeta constructor1 = constructors.get(0);
        Assert.assertEquals(0, constructor1.argTypes.size());
        
        ConstructorMeta constructor2 = constructors.get(1);
        Assert.assertEquals(2, constructor2.argTypes.size());
        Assert.assertEquals("int", constructor2.argTypes.get(0).name);
        Assert.assertEquals("a", constructor2.argNames.get(0));
        Assert.assertEquals("String", constructor2.argTypes.get(1).name);
        Assert.assertEquals("b", constructor2.argNames.get(1));
    }

    @Test
    public void testExtractDefaultConstructor() {
        String sourceCode = "public class ConstructorMetaExtractor_RBL4Test_aefe9473 {}";
        extractor.initialize(sourceCode);
        List<ConstructorMeta> constructors = extractor.extract(sourceCode);
        
        Assert.assertEquals(1, constructors.size());
        ConstructorMeta constructor = constructors.get(0);
        Assert.assertEquals(0, constructor.argTypes.size());
    }

    @Test
    public void testExtractPrivateConstructor() {
        String sourceCode = "public class ConstructorMetaExtractor_RBL4Test_aefe9473 { private TestClass() {} }";
        extractor.initialize(sourceCode);
        List<ConstructorMeta> constructors = extractor.extract(sourceCode);
        
        Assert.assertEquals(1, constructors.size());
        ConstructorMeta constructor = constructors.get(0);
        Assert.assertEquals("TestClass", constructor.accessModifier.toString());
        Assert.assertEquals(0, constructor.argTypes.size());
    }

    @Test
    public void testExtractProtectedConstructor() {
        String sourceCode = "public class ConstructorMetaExtractor_RBL4Test_aefe9473 { protected TestClass(String name) {} }";
        extractor.initialize(sourceCode);
        List<ConstructorMeta> constructors = extractor.extract(sourceCode);
        
        Assert.assertEquals(1, constructors.size());
        ConstructorMeta constructor = constructors.get(0);
        Assert.assertEquals("TestClass", constructor.accessModifier.toString());
        Assert.assertEquals(1, constructor.argTypes.size());
        Assert.assertEquals("String", constructor.argTypes.get(0).name);
        Assert.assertEquals("name", constructor.argNames.get(0));
    }

    @Test
    public void testExtractConstructorWithGenerics() {
        String sourceCode = "public class ConstructorMetaExtractor_RBL4Test_aefe9473 { public TestClass(List<String> list) {} }";
        extractor.initialize(sourceCode);
        List<ConstructorMeta> constructors = extractor.extract(sourceCode);
        
        Assert.assertEquals(1, constructors.size());
        ConstructorMeta constructor = constructors.get(0);
        Assert.assertEquals(1, constructor.argTypes.size());
        Assert.assertEquals("List<String>", constructor.argTypes.get(0).name);
        Assert.assertEquals("list", constructor.argNames.get(0));
    }
}
