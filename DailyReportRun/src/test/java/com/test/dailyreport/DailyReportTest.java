package com.test.dailyreport;

import static org.junit.Assert.assertTrue;

import com.test.dailyreport.service.ReportRunService;
import org.junit.Test;

import java.lang.reflect.Method;

public class DailyReportTest {

    @Test
    public void testGetClientInformation() {
        try {
            ReportRunService reportRunService = new ReportRunService();
            Method method = reportRunService.getClass().getDeclaredMethod("getClientInformation",  String.class);
            method.setAccessible(true);
            String clientInfo = (String) method.invoke(reportRunService, "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B");
            assertTrue("Client Information ", clientInfo.equalsIgnoreCase("CL  432100020001"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProductInformation() {
        try {
            ReportRunService reportRunService = new ReportRunService();
            Method method = reportRunService.getClass().getDeclaredMethod("getProductInformation",  String.class);
            method.setAccessible(true);
            String productInfo = (String) method.invoke(reportRunService, "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B");
            assertTrue("Product Information ", productInfo.equalsIgnoreCase("SGX FUNK    20100910"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTotalTransactionAmount() {
        try {
            ReportRunService reportRunService = new ReportRunService();
            Method method = reportRunService.getClass().getDeclaredMethod("getTotalTransactionAmount",  String.class);
            method.setAccessible(true);
            int total = (int) method.invoke(reportRunService, "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000");
            assertTrue("Total Transaction Amount ", total == 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFieldPositions() {
        try {
            ReportRunService reportRunService = new ReportRunService();
            Method method = reportRunService.getClass().getDeclaredMethod("getStaringPosition",  String.class);
            method.setAccessible(true);
            int startPosition = (int) method.invoke(reportRunService, "accountNumber");
            Method method1 = reportRunService.getClass().getDeclaredMethod("getEndingPosition",  String.class);
            method1.setAccessible(true);
            int endPosition = (int) method1.invoke(reportRunService, "accountNumber");
            assertTrue("Field Start Position ", startPosition == 12);
            assertTrue("Field End Position ", endPosition == 15);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

