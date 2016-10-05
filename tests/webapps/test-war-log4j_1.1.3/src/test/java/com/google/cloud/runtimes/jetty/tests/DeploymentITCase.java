package com.google.cloud.runtimes.jetty.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.cloud.runtime.jetty.testing.AppDeployment;
import com.google.cloud.runtime.jetty.testing.HttpUrlUtil;
import com.google.cloud.runtime.jetty.testing.RemoteLog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DeploymentITCase
{
    @Test
    public void testGet() throws IOException
    {
        HttpURLConnection http = HttpUrlUtil.openTo(AppDeployment.SERVER_URI.resolve("/logging"));
        Assert.assertThat(http.getResponseCode(), is(200));
        
        // Fetch logging events on server
        List<RemoteLog.Entry> logs = RemoteLog.getLogs(AppDeployment.MODULE_ID, AppDeployment.VERSION_ID);
    
        List<String> expectedEntries = new ArrayList<>();
        expectedEntries.add("[DEBUG] - LoggingServlet(log4j-1.1.3) initialized");
        expectedEntries.add("[INFO] - LoggingServlet(log4j-1.1.3) GET requested");
        expectedEntries.add("[WARN] - LoggingServlet(log4j-1.1.3) Slightly warn, with a chance of log events");
        expectedEntries.add("[ERROR] - LoggingServlet(log4j-1.1.3) Nothing is (intentionally) being output by this Servlet");
    
        RemoteLog.assertHasEntries(logs, expectedEntries);
    
        RemoteLog.Entry entry = RemoteLog.findEntry(logs, "[FATAL] - LoggingServlet(log4j-1.1.3) Whoops (intentionally) causing a Throwable");
        assertThat("Multi-Line Log", entry.getTextPayload(), containsString("java.io.FileNotFoundException: A file cannot be found"));
        assertThat("Multi-Line Log", entry.getTextPayload(), containsString("at com.google.cloud.runtime.jetty.tests.webapp.LoggingServlet.doGet(LoggingServlet.java"));
    }
}