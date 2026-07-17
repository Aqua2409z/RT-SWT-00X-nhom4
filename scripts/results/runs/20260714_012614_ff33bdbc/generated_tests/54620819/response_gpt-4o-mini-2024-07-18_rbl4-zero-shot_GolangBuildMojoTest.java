
package com.igormaznitsa.mvngolang;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GolangBuildMojoTest {

    private GolangBuildMojo mojo;
    private MavenProjectHelper projectHelper;

    @Before
    public void setUp() {
        mojo = new GolangBuildMojo();
        projectHelper = Mockito.mock(MavenProjectHelper.class);
        mojo.projectHelper = projectHelper;
        mojo.setResultFolder("target");
        mojo.setResultName("test");
    }

    @Test
    public void testIsAttach() {
        assertFalse(mojo.isAttach());
        mojo.attach = true;
        assertTrue(mojo.isAttach());
    }

    @Test
    public void testGetAttachClassifier() {
        assertNull(mojo.getAttachClassifier());
        mojo.attachClassifier = "classifier";
        assertEquals("classifier", mojo.getAttachClassifier());
    }

    @Test
    public void testGetAttachType() {
        assertEquals("bin", mojo.getAttachType());
        mojo.attachType = "exe";
        assertEquals("exe", mojo.getAttachType());
    }

    @Test
    public void testGetLdflagsAsList() {
        assertTrue(mojo.getLdflagsAsList().isEmpty());
        mojo.ldFlags = new String[]{"-a", "main.prodVersion=1.2.3"};
        assertEquals(Arrays.asList("-a", "main.prodVersion=1.2.3"), mojo.getLdflagsAsList());
    }

    @Test
    public void testGetResultFolder() {
        assertEquals("target", mojo.getResultFolder());
    }

    @Test
    public void testGetResultName() {
        assertEquals("test", mojo.getResultName());
    }

    @Test
    public void testSetStrip() {
        mojo.setStrip(true);
        assertTrue(mojo.isStrip());
    }

    @Test
    public void testSetBuildMode() {
        mojo.setBuildMode("custom");
        assertEquals("custom", mojo.getBuildMode());
    }

    @Test(expected = MojoFailureException.class)
    public void testBeforeExecutionCreatesDirectory() throws MojoFailureException {
        mojo.beforeExecution(null);
        File folder = new File(mojo.getResultFolder());
        assertTrue(folder.isDirectory());
    }

    @Test
    public void testAfterExecutionFileNotFound() {
        try {
            mojo.afterExecution(null, false);
            fail("Expected MojoFailureException");
        } catch (MojoFailureException e) {
            assertTrue(e.getMessage().contains("Can't find generated target file"));
        }
    }

    @Test
    public void testProcessAttach() {
        mojo.attach = true;
        mojo.processAttach(new File("target/test"));
        verify(projectHelper).attachArtifact(any(), anyString(), anyString(), any(File.class));
    }

    @Test
    public void testGetAdditionalCommandFlags() {
        mojo.setBuildMode("custom");
        mojo.strip = true;
        mojo.ldFlags = new String[]{"-a"};
        String[] flags = mojo.getAdditionalCommandFlags();
        assertTrue(Arrays.asList(flags).contains("-buildmode=custom"));
        assertTrue(Arrays.asList(flags).contains("-s"));
        assertTrue(Arrays.asList(flags).contains("-w"));
    }
}
