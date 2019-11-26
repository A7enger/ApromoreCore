package org.apromore.dao.jpa;

import org.apromore.xes.factory.XFactory;
import org.apromore.xes.factory.XFactoryRegistry;
import org.apromore.xes.info.XLogInfo;
import org.apromore.xes.info.XLogInfoFactory;
import org.apromore.xes.model.XTrace;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.Random;

import static org.junit.Assert.assertNotNull;

public class LogRepositoryCustomImplTest {

    LogRepositoryCustomImpl logRepo = new LogRepositoryCustomImpl();


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testImportFromFile() {

        System.out.println(ClassLoader.getSystemResource("XES_logs/SepsisCases.xes"));

        System.out.println(System.getProperty("user.dir"));

        try {
            String name = ClassLoader.getSystemResource("XES_logs/SepsisCases.xes").getPath();
            System.out.println("name: " + name);

//            FileInputStream fis = new FileInputStream(name);
//            System.out.println("fis: " + fis);

//            FilterInputStream filter = new BufferedInputStream(fis);// FilterInputStream is protected
//            while (filter.available() > 0) {
//                byte[] buf = new byte[1024];
//                int len = filter.read(buf);
//                String myStr = new String(buf, 0, len, "UTF-8");
//                System.out.print(myStr);
//            }
//            fis.close();

//            XFactoryExternalStore.InMemoryStoreImpl factory = new XFactoryExternalStore.InMemoryStoreImpl();

//            XFactory factory = new XFactoryLiteImpl();
            XFactory factory = XFactoryRegistry.instance().currentDefault();

            org.apromore.xes.model.XLog log = logRepo.importFromFile(factory, name);

//            XFactory factory = new XFactoryNaiveImpl();
//            XLog log = logRepo.importFromInputStream(fis, new XesXmlParser(factory));

            System.out.println("log: " + log);

            XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
            System.out.println("logInfo: " + logInfo);

            readSequentially(log);
//            readRandom(log);

//            XLog log = logRepo.importFromFile(new XFactoryNaiveImpl(), name);
//            System.out.println("xlog: " + log);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }


    @Test
    public void exportToFile() {
    }

    @Test
    public void importFromInputStream() {
    }

    @Test
    public void exportToInputStream() {
    }

    @Test
    public void getProcessLog() {

    }

    protected void readSequentially(org.apromore.xes.model.XLog log) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        long startTime = System.nanoTime();
        System.out.println("Reading all attributes via values(): ");
        long attributeCounter = 0;
        for (org.apromore.xes.model.XTrace trace : log) {
            for (org.apromore.xes.model.XEvent event : trace) {
                for (org.apromore.xes.model.XAttribute attr : event.getAttributes().values()) {
                    assertNotNull(attr.getKey());
                    if (attr instanceof org.apromore.xes.model.XAttributeBoolean) {
                        assertNotNull(((org.apromore.xes.model.XAttributeBoolean) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeLiteral) {
                        assertNotNull(((org.apromore.xes.model.XAttributeLiteral) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeContinuous) {
                        assertNotNull(((org.apromore.xes.model.XAttributeContinuous) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeDiscrete) {
                        assertNotNull(((org.apromore.xes.model.XAttributeDiscrete) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeTimestamp) {
                        assertNotNull(((org.apromore.xes.model.XAttributeTimestamp) attr).getValue());
                    }
                    attributeCounter++;
                }
            }
        }
        long elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Elapsed time: " + elapsedNanos / 1000000 + " ms");
        double elapsedSecond = (System.nanoTime() - startTime) / 1000000000.0;
        double attributesPerSecond = attributeCounter / elapsedSecond;
        System.out.println(numberFormat.format(attributesPerSecond) + " APS");
        System.gc();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        System.out.println("Memory Used: " + getMemoryUsage().getUsed() / 1024 / 1024 + " MB ");
    }

    protected void readRandom(org.apromore.xes.model.XLog log) {
        Random random = new Random();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        long startTime = System.nanoTime();
        System.out.println("Reading all attributes via values() of random traces & events: ");
        long attributeCounter = 0;
        int traceCounter = 0;
        while (traceCounter < log.size()) {
            XTrace trace = log.get(random.nextInt(log.size()));
            int eventCounter = 0;
            while (eventCounter < trace.size()) {
                org.apromore.xes.model.XEvent event = trace.get(random.nextInt(trace.size()));
                for (String key : event.getAttributes().keySet()) {
                    org.apromore.xes.model.XAttribute attr = event.getAttributes().get(key);
                    assertNotNull(attr);
                    if (attr instanceof org.apromore.xes.model.XAttributeBoolean) {
                        assertNotNull(((org.apromore.xes.model.XAttributeBoolean) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeLiteral) {
                        assertNotNull(((org.apromore.xes.model.XAttributeLiteral) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeContinuous) {
                        assertNotNull(((org.apromore.xes.model.XAttributeContinuous) attr).getValue());
                    } else if (attr instanceof org.apromore.xes.model.XAttributeDiscrete) {
                        assertNotNull(((org.apromore.xes.model.XAttributeDiscrete) attr).getValue());
                    }
                    attributeCounter++;
                }
                eventCounter++;
            }
            traceCounter++;
        }
        long elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Elapsed time: " + elapsedNanos / 1000000 + " ms");
        double elapsedSecond = (System.nanoTime() - startTime) / 1000000000.0;
        double attributesPerSecond = attributeCounter / elapsedSecond;
        System.out.println(numberFormat.format(attributesPerSecond) + " APS");
        System.gc();
        System.out.println("Memory Used: " + getMemoryUsage().getUsed() / 1024 / 1024 + " MB ");
    }

    protected MemoryUsage getMemoryUsage() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage();
    }
}