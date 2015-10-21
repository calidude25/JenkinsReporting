package com.disney.wdpr.jenkins.shutdown;

import org.apache.log4j.Logger;

/**
 * @author Matt Carson
 */
public class ShutdownHook extends Thread
{
    private static final Logger LOG = Logger.getLogger(ShutdownHook.class);

    @Override
    public void run()
    {
        ShutdownHook.LOG.warn("Shutting down...");
    }

}
