package com.disney.wdpr.jenkins;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Job;
import org.springframework.context.support.AbstractApplicationContext;

import com.disney.wdpr.jenkins.Launch;
import com.disney.wdpr.jenkins.manager.JobManager;

/**
 * Tests the lunch class
 */
public class LaunchTest extends AbstractTestCase
{

    private Launch launch;

    // Mocks
    private AbstractApplicationContext applicationContext;
    private Job springJob;
//    private JobLauncher jobLauncher;
    private JobManager jobManager;
//
//    private static final String MODE_PARAM_NODE_VALUE = BatchModeType.parameterName() + "=" + BatchModeType.Node.toString();
//    private static final String MODE_PARAM_HYBRID_VALUE = BatchModeType.parameterName() + "=" + BatchModeType.Hybrid.toString();
//    private static final String MODE_PARAM_HYBRID_AMQ_VALUE = BatchModeType.parameterName() + "="
//        + BatchModeType.Hybrid_Activemq.toString();
//    private static final String TYPE_PARAM_SCHEDULED_VALUE = BatchContextType.parameterName() + "=" + BatchContextType.Scheduled.toString();
//    private static final String SCENARIO_PARAM_VALUE = BatchScenarioType.parameterName() + "=" + BatchScenarioType.AOP.toString();
//
//    final JobRun jobRun = new JobRun();
//    private final int jobId = 1;
//    com.wdpr.ldp.controller.model.Job job = new com.wdpr.ldp.controller.model.Job();

    /**
     *
     */
    @Before
    public void initializeTest()
    {
        applicationContext = EasyMock.createMock(AbstractApplicationContext.class);
//        this.springJob = EasyMock.createMock(JobListing.class);
//        this.jobLauncher = EasyMock.createMock(JobLauncher.class);
        jobManager = EasyMock.createMock(JobManager.class);
//        this.launch.setJobManager(this.jobManager);
//
//        Launch.setApplicationContext(this.applicationContext);
//
//        this.jobRun.setJobId(this.jobId);
//        this.job.setJobId(this.jobId);

    }



    @Test
    public void testProcessControllerExtract()
    {
        try
        {
            launch = EasyMock.createMockBuilder(Launch.class)
                    .addMockedMethod("setAppContext")
                    .createMock();

            Launch.setApplicationContext(applicationContext);

            launch.setAppContext(EasyMock.aryEq(new String[] { Launch.SHARED_CONTEXT,Launch.EXTRACT_CONTEXT}));
            applicationContext.registerShutdownHook();
            EasyMock.expect(applicationContext.getBean(Launch.EXTRACT_BEAN)).andReturn(jobManager);
            jobManager.process("03. Appium Mobile Tests");

            replayAll();

            final String[] args = new String[] { Launch.MODE_PARAM_EXTRACT_NOW };

            launch.process(args);

            verifyAll();
        }
        catch (final Exception e)
        {
            super.failure(e);
        }

    }




    @Test(expected=RuntimeException.class)
    public void testProcessControllerException()
    {
            launch = EasyMock.createMockBuilder(Launch.class)
                    .addMockedMethod("setAppContext")
                    .createMock();

            Launch.setApplicationContext(applicationContext);

            replayAll();

            final String[] args = new String[] { "junk" };

            launch.process(args);

            verifyAll();

    }


    @Override
    protected void replayAll()
    {
        EasyMock.replay(launch);
        EasyMock.replay(applicationContext);
        EasyMock.replay(jobManager);
    }

    @Override
    protected void verifyAll()
    {
        EasyMock.verify(launch);
        EasyMock.verify(applicationContext);
        EasyMock.verify(jobManager);
    }

}
