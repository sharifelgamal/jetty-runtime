package com.google.cloud.runtimes.jetty.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.cloud.runtime.jetty.testing.AppDeployment;
import com.google.cloud.runtime.jetty.testing.HttpUrlUtil;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

public class DeploymentITCase
{
    @Test
    public void testGet() throws IOException
    {
        HttpURLConnection http = HttpUrlUtil.openTo(AppDeployment.SERVER_URI.resolve("/hello/"));
        assertThat(http.getResponseCode(), is(200));
        String responseBody = HttpUrlUtil.getResponseBody(http);
        assertThat(responseBody, containsString("Hello from Servlet 3.1"));
    }
}