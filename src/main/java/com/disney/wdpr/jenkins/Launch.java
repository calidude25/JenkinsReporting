package com.disney.wdpr.jenkins;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.disney.wdpr.jenkins.manager.JobManager;
import com.disney.wdpr.jenkins.shutdown.DiagSignalHandler;
import com.disney.wdpr.jenkins.shutdown.ShutdownHook;

/**
 *
 * @author Matt Carson
 */
public class Launch
{
    private static final Logger LOG = Logger.getLogger(Launch.class);

    // private Map<String, String> arguments;
    private static AbstractApplicationContext appContext;

    public final static String SHARED_CONTEXT = "sharedContext.xml";
    public final static String REPORT_CONTEXT = "reportContext.xml";
    public final static String REPORT_BEAN = "reportManager";


    /**
     * To be deleted if we roll back.
     *
     * @return the appContext
     */
    public static AbstractApplicationContext getAppContext()
    {
        return Launch.appContext;
    }

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        new Launch().process(args);
    }

    protected static void setApplicationContext(final AbstractApplicationContext context)
    {
        Launch.appContext = context;
    }


    /**
     * @param args
     */
    public void process(final String[] args)
    {

        try
        {
//            if (args == null || args.length < 1)
//            {
//                throw new RuntimeException("Parameter list cannot be null: \n" + help());
//            }

            setAppContext(new String[] { SHARED_CONTEXT,REPORT_CONTEXT});
            String mainBean=REPORT_BEAN;

            // register shutdown hooks
            Launch.appContext.registerShutdownHook();
            final ShutdownHook shutdownHook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(shutdownHook);

            DiagSignalHandler.install("INT");// Interactive attention (CTRL-C). JVM will exit normally.
            DiagSignalHandler.install("TERM");// Termination request. JVM will exit normally.
            DiagSignalHandler.install("ABRT");// Abnormal termination. The JVM raises this signal whenever it detects a JVM fault.

            final JobManager job = (JobManager) Launch.appContext.getBean(mainBean);
            job.process("03. Appium Mobile Tests");
        }
        catch (final Exception e)
        {
            Launch.LOG.error("Failure launching Spring", e);
            throw new RuntimeException(e);
        }
    }

    protected void setAppContext(final String[] contextFiles)
    {
        if (Launch.appContext == null)
        {
            Launch.appContext = new ClassPathXmlApplicationContext(contextFiles);
        }
    }

    /**
     * Display parameter options to user
     *
     * @return Display parameter in a String
     */
    private String help()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nValid Parameters: audit, auditNow, extract or extractNow");
        stringBuilder.append("\n\taudit - Start regularly scheduled Proctor Audit Violation Report");
        stringBuilder.append("\n\tauditNow - Launch Proctor Audit Violation Report Now");
        stringBuilder.append("\n\textract - Start regularly scheduled Jam Extract Report");
        stringBuilder.append("\n\textractNow - Launch Jam Extract Report Now");
        return stringBuilder.toString();
    }

}
