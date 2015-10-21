package com.disney.wdpr.jenkins.manager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.disney.wdpr.jenkins.AbstractTestCase;
import com.disney.wdpr.jenkins.dto.GenericDto;
import com.disney.wdpr.jenkins.manager.LoadReport;
import com.disney.wdpr.jenkins.manager.ManagerUtils;

public class ManagerUtilsTest extends AbstractTestCase{

    private ManagerUtils managerUtils;
    private File archiveDirectory;
    private File archiveFile;
    private File archiveEntry1;
    private File archiveEntry2;
    private ZipOutputStream zout;
    private BufferedInputStream bis;
    private LoadReport loadReport;
    private BufferedReader reader;
    
    
    @Before
    public void initializeTest() {
        this.archiveDirectory = EasyMock.createMock(File.class);
        this.zout = EasyMock.createMock(ZipOutputStream.class);
        this.bis = EasyMock.createMock(BufferedInputStream.class);
        this.archiveFile = EasyMock.createMock(File.class);
        this.archiveEntry1 = EasyMock.createMock(File.class);
        this.archiveEntry2 = EasyMock.createMock(File.class);
        this.loadReport = EasyMock.createMock(LoadReport.class);
        this.reader = EasyMock.createMock(BufferedReader.class);
    }
    
//    @Test
    public void testCompressOldArchives2() {
        try {
            final File directory = new File("C:\\Data\\Matt\\LMS\\code\\LMSProctorAudit\\archive");
            managerUtils = new ManagerUtils();
            managerUtils.setArchiveFileAppenderDateFormat("yyyyMMdd'T'HHmmss");
            managerUtils.setZipFileExtensionDateFormat("yyyyMMdd");
            
            managerUtils.compressOldArchives(directory);
        } catch (final IOException e) {
            super.failure(e);
        }
        
    }

    
    @Test
    public void testCompressOldArchives() {
        try {
            
            final Calendar calendar = new GregorianCalendar(2014,1,1);
            final String archiveFilename = "archive";
            final String archiveFilenameWithDate = archiveFilename+"_20140131.zip";
            final File[] files = new File[] {this.archiveEntry1, this.archiveEntry2};
            
            final String archiveEntryName1 = "archiveEntryName1.csv";
            final String archiveEntryName2 = "archiveEntryName2.csv";
            
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .addMockedMethod("getCutOffDate")
                        .addMockedMethod("getFile")
                        .addMockedMethod("getZipStream")
                        .addMockedMethod("getInStream")
                        .addMockedMethod("writeData")
                        .createMock();

            managerUtils.setArchiveFileAppenderDateFormat("yyyyMMdd'T'HHmmss");
            managerUtils.setZipFileExtensionDateFormat("yyyyMMdd");

            EasyMock.expect(managerUtils.getCutOffDate()).andReturn(calendar);
            EasyMock.expect(archiveDirectory.listFiles(EasyMock.isA(FileFilter.class))).andReturn(files);            
            EasyMock.expect(managerUtils.getFile(archiveDirectory, archiveFilenameWithDate)).andReturn(archiveFile);
            EasyMock.expect(managerUtils.getZipStream(archiveFile)).andReturn(this.zout);
            this.zout.setLevel(9);
            EasyMock.expect(this.archiveEntry1.getName()).andReturn(archiveEntryName1);
            EasyMock.expect(this.managerUtils.getInStream(archiveEntry1)).andReturn(bis);
            zout.putNextEntry(EasyMock.isA(ZipEntry.class));

            this.managerUtils.writeData(bis, zout);
            
            zout.closeEntry();
            bis.close();
            zout.flush();
            
            EasyMock.expect(this.archiveEntry2.getName()).andReturn(archiveEntryName2);
            EasyMock.expect(this.managerUtils.getInStream(archiveEntry2)).andReturn(bis);
            zout.putNextEntry(EasyMock.isA(ZipEntry.class));

            this.managerUtils.writeData(bis, zout);
            
            zout.closeEntry();
            bis.close();
            zout.flush();
            
            EasyMock.expect(archiveFile.exists()).andReturn(true);
            EasyMock.expect(archiveEntry1.delete()).andReturn(true);
            EasyMock.expect(archiveEntry2.delete()).andReturn(true);
            
            bis.close();
            zout.flush();
            zout.close();
            
            
            this.replayAll();
            
            managerUtils.compressOldArchives(archiveDirectory);
            
            this.verifyAll();
            
        } catch (final Exception e) {
            super.failure(e);
        }
        
    }

    @Test
    public void testAddDateToFilename() {
        final String fileName = "discussion";
        final String extension = "csv";
        final String dateFormat = "yyyyMMdd";
        final Calendar calendar = new GregorianCalendar(2014,1,1);
        
        final ManagerUtils managerUtils = new ManagerUtils();
        final String output = managerUtils.addDateToFilename(fileName+"."+extension, calendar.getTime(), dateFormat);
        Assert.assertEquals(fileName+"_20140201."+extension, output);
        
    } 
    
    @Test
    public void testCleanupOldFiles() {
        try {
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .createMock();

            final File[] files = new File[]{this.archiveEntry1, this.archiveEntry2};
            EasyMock.expect(archiveDirectory.listFiles(EasyMock.isA(FileFilter.class))).andReturn(files);
            EasyMock.expect(this.archiveEntry1.delete()).andReturn(true);
            EasyMock.expect(this.archiveEntry2.delete()).andReturn(true);
            
            this.replayAll();
            
            managerUtils.cleanupOldfiles(archiveDirectory);
            
            this.verifyAll();
            
        } catch (final Exception e) {
            super.failure(e);
        }
    } 

    @Test
    public void testLoadReport() {
        try {
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .addMockedMethod("getReader")
                        .createMock();
            
            final String reportFilename = "comment.csv";
            final Map<String, GenericDto> resultMap = new HashMap<String, GenericDto>();
            final String line = "field1, field2, field3";

            EasyMock.expect(managerUtils.getReader(reportFilename)).andReturn(reader);
           
            EasyMock.expect(this.reader.readLine()).andReturn(line).times(2).andReturn(null);
            this.loadReport.loadLine(line, resultMap);
            reader.close();
            
            this.replayAll();
            
            final int results = managerUtils.loadReport(reportFilename, loadReport, resultMap);
            Assert.assertEquals(2, results);
            
            this.verifyAll();
            
        } catch (final Exception e) {
            super.failure(e);
        }
    } 

    @Test (expected=FileNotFoundException.class)
    public void testLoadReportException() throws IOException {
        try {
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .addMockedMethod("getReader")
                        .createMock();
            
            final String reportFilename = "comment.csv";
            final Map<String, GenericDto> resultMap = new HashMap<String, GenericDto>();
            final String line = "field1, field2, field3";

            EasyMock.expect(managerUtils.getReader(reportFilename)).andThrow(new FileNotFoundException());
                       
            this.replayAll();
            
            managerUtils.loadReport(reportFilename, loadReport, resultMap);
        }finally {           
            this.verifyAll();            
        }
    } 
    

    @Test
    public void testFileFilter() {
        try {
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .createMock();
            final Date cutoffDate = new GregorianCalendar(2014,2,20).getTime();
            
            final Date fileDate1 = new GregorianCalendar(2014,2,19).getTime();
            final Date fileDate2 = new GregorianCalendar(2014,2,20).getTime();

            final String csvExt = "csv";
            final String txtExt = "txt";
            final String filename1 = "somefile";
            final String filename2 = "anotherFile."+csvExt;
            final String filename3 = "anotherFile."+txtExt;
            
            
            EasyMock.expect(this.archiveFile.lastModified())
                .andReturn(fileDate1.getTime())
                .andReturn(fileDate2.getTime())
                .andReturn(fileDate1.getTime())
                .andReturn(fileDate1.getTime())
                .andReturn(fileDate1.getTime())
                .andReturn(fileDate2.getTime())
                ;
            EasyMock.expect(this.archiveFile.getName())
                .andReturn(filename1)
                .andReturn(filename2)
                .andReturn(filename2)
                .andReturn(filename3)
                .andReturn(filename1)
                .andReturn(filename2)
                ;
            
            
            this.replayAll();
            
            FileFilter fileFilter = ManagerUtils.getOldFileFilter(cutoffDate);
            
            // 1 - no extension filter - date before cutoff
            boolean result = fileFilter.accept(archiveFile);
            Assert.assertEquals(true, result);
            
            // 2 - no extension filter - date not before cutoff
            result = fileFilter.accept(archiveFile);
            Assert.assertEquals(false, result);

            // 3 - extension filter csv - file is csv and before cutoff
            fileFilter = ManagerUtils.getOldFileFilter(cutoffDate,csvExt);
            result = fileFilter.accept(archiveFile);
            Assert.assertEquals(true, result);
            
            // 4 - extension filter csv - file is txt and before cutoff
            result = fileFilter.accept(archiveFile);
            Assert.assertEquals(false, result);
            
            // 5 - extension filter csv - file has no ext and before cutoff
            result = fileFilter.accept(archiveFile);
            Assert.assertEquals(false, result);

            // 6 - extension filter csv - file is csv and not before cutoff
            result = fileFilter.accept(archiveFile);
            Assert.assertEquals(false, result);

            this.verifyAll();
            
        } catch (final Exception e) {
            super.failure(e);
        }
    } 

    @Test
    public void testDateFormat() {
        final Date date = new GregorianCalendar(2014,2,5).getTime();
        
        
        final String result = ManagerUtils.formatDate(date.getTime(), "yyyyMMdd");
        Assert.assertEquals("20140305", result);
        
    }    
    

    @Test
    public void testParseUnixEpochDate() {
        final Date date = new GregorianCalendar(2014,2,5).getTime();
        final String unixEpoch = "/Date("+date.getTime()+")";
        
        
        final long result = ManagerUtils.parseUnixEpochDate(unixEpoch);
        Assert.assertEquals(date.getTime(), result);
        
    }    

    
    @Test
    public void testCompressOldArchivesEmpty() {
        try {
            final File[] files = new File[0];
            
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .createMock();

            EasyMock.expect(archiveDirectory.listFiles(EasyMock.isA(FileFilter.class))).andReturn(files);
            
            this.replayAll();
            
            managerUtils.compressOldArchives(archiveDirectory);
            
            this.verifyAll();
            
        } catch (final Exception e) {
            super.failure(e);
        }
    }

    @Test
    public void testMoveFileToArchive() {
        try {
            
            final String ext = "csv";
            final String baseFilename = "comment";
            final String filenameWithExtension = baseFilename+"."+ext;
            final String filenameWithDate = baseFilename+"_20140305."+ext;
            final String localArchiveDir = "archive";
            final String localWorkingDir = "staging";
            
            managerUtils =
                    EasyMock.createMockBuilder(ManagerUtils.class)
                        .addMockedMethod("getFileInstance")
                        .addMockedMethod("moveFile")
                        .createMock();
            managerUtils.setArchiveFileAppenderDateFormat("yyyyMMdd");
            managerUtils.setLocalArchiveDirectory(localArchiveDir);
            managerUtils.setLocalWorkingDirectory(localWorkingDir);
            
            
            
            final Date date = new GregorianCalendar(2014,2,5).getTime();
            
            EasyMock.expect(managerUtils.getFileInstance(localWorkingDir+File.separator+filenameWithExtension)).andReturn(this.archiveEntry1);
            EasyMock.expect(managerUtils.getFileInstance(localArchiveDir + File.separator + filenameWithDate)).andReturn(this.archiveEntry2);
            managerUtils.moveFile(archiveEntry1, archiveEntry2);
            
            this.replayAll();
            
            managerUtils.moveFileToArchive(filenameWithExtension, date);
            
            this.verifyAll();
            
        } catch (final Exception e) {
            super.failure(e);
        }
    } 
    
    
    @Override
    protected void replayAll() {
        EasyMock.replay(this.managerUtils);
        EasyMock.replay(this.archiveDirectory);
        EasyMock.replay(this.bis);
        EasyMock.replay(this.zout);
        EasyMock.replay(this.archiveFile);
        EasyMock.replay(this.archiveEntry1);
        EasyMock.replay(this.archiveEntry2);
        EasyMock.replay(this.loadReport);
        EasyMock.replay(this.reader);
    }

    @Override
    protected void verifyAll() {
        EasyMock.verify(this.managerUtils);
        EasyMock.verify(this.archiveDirectory);
        EasyMock.verify(this.bis);
        EasyMock.verify(this.zout);
        EasyMock.verify(this.archiveFile);
        EasyMock.verify(this.archiveEntry1);
        EasyMock.verify(this.archiveEntry2);
        EasyMock.verify(this.loadReport);
        EasyMock.verify(this.reader);
    }

}
