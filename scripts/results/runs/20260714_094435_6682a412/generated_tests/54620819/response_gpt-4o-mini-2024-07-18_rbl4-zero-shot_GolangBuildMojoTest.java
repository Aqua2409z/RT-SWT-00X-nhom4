
package com.igormaznitsa.mvngolang;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

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
        mojo.attach = true;
        assertTrue(mojo.isAttach());
    }

    @Test
    public void testGetAttachClassifier() {
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
        mojo.ldFlags = new String[]{"-a", "-b"};
        List<String> ldFlagsList = mojo.getLdflagsAsList();
        assertEquals(2, ldFlagsList.size());
        assertTrue(ldFlagsList.contains("-a"));
        assertTrue(ldFlagsList.contains("-b"));
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
    public void testGetBuildMode() {
        assertEquals("default", mojo.getBuildMode());
        mojo.setBuildMode("custom");
        assertEquals("custom", mojo.getBuildMode());
    }

    @Test
    public void testSetStrip() {
        mojo.setStrip(true);
        assertTrue(mojo.isStrip());
    }

    @Test
    public void testGetAdditionalCommandFlags() {
        mojo.setBuildMode("custom");
        mojo.setStrip(true);
        mojo.ldFlags = new String[]{"-a"};
        String[] flags = mojo.getAdditionalCommandFlags();
        assertTrue(flags.length > 0);
        assertTrue(flags[0].contains("-buildmode=custom"));
        assertTrue(flags[1].contains("-ldflags"));
    }

    @Test(expected = MojoFailureException.class)
    public void testBeforeExecutionCreatesDirectory() throws MojoFailureException {
        mojo.beforeExecution(null);
        File resultFile = new File(mojo.getResultFolder(), mojo.getResultName());
        assertTrue(resultFile.getParentFile().exists());
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
        File resultFile = new File(mojo.getResultFolder(), mojo.getResultName());
        mojo.processAttach(resultFile);
        verify(projectHelper).attachArtifact(any(), anyString(), anyString(), eq(resultFile));
    }

    @Test
    public void testIsCommandSupportVerbose() {
        assertTrue(mojo.isCommandSupportVerbose());
    }
}
