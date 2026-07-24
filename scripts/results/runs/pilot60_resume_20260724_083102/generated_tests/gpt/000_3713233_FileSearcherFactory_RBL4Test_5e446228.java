package org.junithelper.core.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileSearcherFactory_RBL4Test_5e446228 {

    @Test
    public void testCreate() {
        FileSearcher fileSearcher = FileSearcherFactory.create();
        assertNotNull("FileSearcher should not be null", fileSearcher);
        assertTrue("FileSearcher should be an instance of FileSearcherCommonsIOImpl", 
                   fileSearcher instanceof FileSearcherCommonsIOImpl);
    }
}
