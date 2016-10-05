package com.google.cloud.runtimes.jetty.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.cloud.runtime.jetty.testing.AppDeployment;
import com.google.cloud.runtime.jetty.testing.HttpUrlUtil;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Test;

public class SpringPetclinicITCase
{
    @Test
    public void testGet() throws IOException
    {
        HttpURLConnection http = HttpUrlUtil.openTo(AppDeployment.SERVER_URI.resolve("/"));
        assertThat(http.getResponseCode(), is(200));
        String responseBody = HttpUrlUtil.getResponseBody(http);
        assertThat(responseBody, containsString("<title>PetClinic :: a Spring Framework demonstration</title>"));
    }
}
