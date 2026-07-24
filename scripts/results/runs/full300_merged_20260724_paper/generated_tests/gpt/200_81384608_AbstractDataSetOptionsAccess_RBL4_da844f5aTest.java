
package com.thinkbiganalytics.kylo.catalog.spark;

import com.thinkbiganalytics.kylo.catalog.api.KyloCatalogConstants;
import com.thinkbiganalytics.kylo.catalog.api.KyloCatalogDataSetAccess;
import com.thinkbiganalytics.kylo.catalog.rest.model.DataSetTemplate;
import com.thinkbiganalytics.kylo.catalog.spi.DataSetOptions;
import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class AbstractDataSetOptionsAccess_RBL4_da844f5aTest {

    private TestDataSetOptionsAccess access;
    private DataSetOptions options;
    private Configuration hadoopConfiguration;
    private DataSourceResourceLoader resourceLoader;

    @Before
    public void setUp() {
        options = new DataSetOptions();
        hadoopConfiguration = new Configuration();
        resourceLoader = new DataSourceResourceLoader();
        access = new TestDataSetOptionsAccess(options, hadoopConfiguration, resourceLoader);
    }

    @Test
    public void testAddFile() {
        String path = "testFile.txt";
        access.addFile(path);
        assertTrue(resourceLoader.getFiles().contains(path));
    }

    @Test
    public void testAddJar() {
        String jarPath = "testJar.jar";
        access.addJar(jarPath);
        assertTrue(options.getJars().contains(jarPath));
    }

    @Test
    public void testAddJars() {
        List<String> jarPaths = Arrays.asList("jar1.jar", "jar2.jar");
        access.addJars(jarPaths);
        assertTrue(options.getJars().containsAll(jarPaths));
    }

    @Test
    public void testDataSet() {
        DataSetTemplate dataSetTemplate = new DataSetTemplate();
        dataSetTemplate.setFiles(Collections.singletonList("file1.txt"));
        dataSetTemplate.setFormat("csv");
        dataSetTemplate.setJars(Collections.singletonList("jar1.jar"));
        dataSetTemplate.setOptions(Collections.singletonMap("key", "value"));
        dataSetTemplate.setPaths(Collections.singletonList("path1"));

        access.dataSet(dataSetTemplate);

        assertTrue(resourceLoader.getFiles().contains("file1.txt"));
        assertEquals("csv", options.getFormat());
        assertTrue(options.getJars().contains("jar1.jar"));
        assertEquals("value", options.getOption("key"));
        assertTrue(options.getPaths().contains("path1"));
    }

    @Test
    public void testFormat() {
        String format = "json";
        access.format(format);
        assertEquals(format, options.getFormat());
    }

    @Test
    public void testOption() {
        String key = KyloCatalogConstants.HADOOP_CONF_PREFIX + "testKey";
        String value = "testValue";
        access.option(key, value);
        assertEquals(value, hadoopConfiguration.get("testKey"));
        assertEquals(value, options.getOption(key));
    }

    private static class AbstractDataSetOptionsAccess_RBL4_da844f5aTest extends AbstractDataSetOptionsAccess<TestDataSetOptionsAccess> {
        public TestDataSetOptionsAccess(DataSetOptions options, Configuration hadoopConfiguration, DataSourceResourceLoader resourceLoader) {
            super(options, hadoopConfiguration, resourceLoader);
        }
    }

    private static class AbstractDataSetOptionsAccess_RBL4_da844f5aTest {
        private final List<String> files = new ArrayList<>();
        private final List<String> jars = new ArrayList<>();

        public void addFile(String path) {
            files.add(path);
        }

        public void addJar(String path) {
            jars.add(path);
        }

        public List<String> getFiles() {
            return files;
        }

        public List<String> getJars() {
            return jars;
        }
    }
}
