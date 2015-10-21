package com.disney.wdpr.jenkins;

import org.junit.Assert;
import org.junit.Test;

import com.disney.wdpr.jenkins.shutdown.DiagSignalHandler;

/**
 * Tests the DiagSignalHandler class.
 * 
 * @author ssf1
 */
@SuppressWarnings("restriction")
public class DiagSignalHandlerTest
{

    private final static String SIGNAL_NAME = "INT";

    /**
     * tests the install method.
     */
    @Test
    public void testInstall()
    {

        final DiagSignalHandler result = DiagSignalHandler.install(DiagSignalHandlerTest.SIGNAL_NAME);
        Assert.assertNotNull(result);

    }

}
