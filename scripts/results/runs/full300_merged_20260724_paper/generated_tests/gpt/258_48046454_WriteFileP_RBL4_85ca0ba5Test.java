package com.hazelcast.jet.impl.connector;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.jet.RestartableException;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.core.Inbox;
import com.hazelcast.jet.core.Outbox;
import com.hazelcast.jet.core.Processor;
import com.hazelcast.jet.core.ProcessorMetaSupplier;
import com.hazelcast.jet.core.Watermark;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.LongSupplier;

import static org.junit.Assert.*;

public class WriteFileP_RBL4_85ca0ba5Test {

    private WriteFileP<String> writeFileProcessor;
    private Path testDirectory;
    private static final String TEST_DIRECTORY = "testDir";
    private static final String CHARSET = "UTF-8";
    private static final long MAX_FILE_SIZE = 1024; // 1KB for testing
    private static final boolean EXACTLY_ONCE = true;

    @Before
    public void setUp() throws IOException {
        testDirectory = Paths.get(TEST_DIRECTORY);
        Files.createDirectories(testDirectory);
        writeFileProcessor = new WriteFileP<>(
                TEST_DIRECTORY,
                FunctionEx.identity(),
                CHARSET,
                null,
                MAX_FILE_SIZE,
                EXACTLY_ONCE,
                System::currentTimeMillis
        );
    }

    @After
    public void tearDown() throws IOException {
        Files.walk(testDirectory)
                .sorted((a, b) -> b.compareTo(a)) // reverse order to delete files first
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void testInit() throws IOException {
        Outbox outbox = new MockOutbox();
        Processor.Context context = new MockContext();
        writeFileProcessor.init(outbox, context);
        assertNotNull(writeFileProcessor);
    }

    @Test
    public void testProcess() throws IOException {
        Outbox outbox = new MockOutbox();
        Processor.Context context = new MockContext();
        writeFileProcessor.init(outbox, context);

        Inbox inbox = new MockInbox("test data 1", "test data 2");
        writeFileProcessor.process(0, inbox);
        writeFileProcessor.complete();

        Path filePath = testDirectory.resolve("0");
        assertTrue(Files.exists(filePath));
        assertEquals("test data 1\n" + "test data 2\n", new String(Files.readAllBytes(filePath), CHARSET));
    }

    @Test
    public void testSnapshotCommit() throws IOException {
        Outbox outbox = new MockOutbox();
        Processor.Context context = new MockContext();
        writeFileProcessor.init(outbox, context);

        Inbox inbox = new MockInbox("data for snapshot");
        writeFileProcessor.process(0, inbox);
        assertTrue(writeFileProcessor.snapshotCommitPrepare());
        assertTrue(writeFileProcessor.snapshotCommitFinish(true));
    }

    @Test(expected = RestartableException.class)
    public void testProcessIOException() throws IOException {
        Outbox outbox = new MockOutbox();
        Processor.Context context = new MockContext();
        writeFileProcessor.init(outbox, context);

        Inbox inbox = new MockInbox("data that causes IOException");
        writeFileProcessor.process(0, inbox);
    }

    @Test
    public void testClose() throws IOException {
        Outbox outbox = new MockOutbox();
        Processor.Context context = new MockContext();
        writeFileProcessor.init(outbox, context);
        writeFileProcessor.close();
        assertTrue(true); // Just checking if close() does not throw an exception
    }

    private static class WriteFileP_RBL4_85ca0ba5Test implements Outbox {
        @Override
        public void offer(Object item) {
        }

        @Override
        public void flush() {
        }
    }

    private static class WriteFileP_RBL4_85ca0ba5Test implements Processor.Context {
        @Override
        public int globalProcessorIndex() {
            return 0;
        }

        @Override
        public int totalParallelism() {
            return 1;
        }

        @Override
        public ProcessingGuarantee processingGuarantee() {
            return ProcessingGuarantee.EXACTLY_ONCE;
        }

        @Override
        public Logger logger() {
            return new MockLogger();
        }
    }

    private static class WriteFileP_RBL4_85ca0ba5Test implements Logger {
        @Override
        public void warning(String message) {
        }

        @Override
        public void warning(String message, Throwable t) {
        }
    }

    private static class WriteFileP_RBL4_85ca0ba5Test implements Inbox {
        private final String[] items;
        private int index = 0;

        MockInbox(String... items) {
            this.items = items;
        }

        @Override
        public Object poll() {
            if (index < items.length) {
                return items[index++];
            }
            return null;
        }
    }
}
