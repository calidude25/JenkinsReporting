package com.disney.wdpr.jenkins.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.disney.wdpr.jenkins.dto.GenericDto;

public class ManagerUtils {
    private static Logger log = Logger.getLogger(ManagerUtils.class);

    private String archiveFileAppenderDateFormat;
    private String zipFileExtensionDateFormat;
    private String localArchiveDirectory;
    private String localWorkingDirectory;
    private int daysToKeepArchives;

    protected Calendar getCutOffDate(){
        final Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        return cal;
    }


    public void compressOldArchives(final File directory) throws IOException {

        final Calendar cal = getCutOffDate();

        final File[] files = directory.listFiles(getOldFileFilter(cal.getTime(), "csv"));
        cal.add(Calendar.DATE, -1);
        log.info("zip date extension: " + cal.getTime());

        if (files.length > 0) {
            BufferedInputStream bis = null;
            ZipOutputStream zout = null;
            try {

                final SimpleDateFormat archiveFileDateExtension = new SimpleDateFormat(zipFileExtensionDateFormat);
                final String archiveName = "archive_" + archiveFileDateExtension.format(cal.getTime()) + ".zip";
                final File archiveFile = getFile(directory, archiveName);

                zout = getZipStream(archiveFile);
                zout.setLevel(9);
                final SimpleDateFormat singleElementDateFormat = new SimpleDateFormat(archiveFileAppenderDateFormat);

                for (final File inputFile : files) {

                    final String newFilename = inputFile.getName() + "_" + singleElementDateFormat.format(cal.getTime());

                    bis = getInStream(inputFile);

                    final ZipEntry entry = new ZipEntry(newFilename);
                    zout.putNextEntry(entry);
                    writeData(bis, zout);

                    zout.closeEntry();
                    bis.close();
                    zout.flush();
                }
                if (archiveFile.exists()) {
                    for (final File inputFile : files) {
                        inputFile.delete();
                    }
                }

            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (zout != null) {
                    zout.flush();
                    zout.close();
                }
            }

        }

    }

    protected void writeData(final BufferedInputStream bis, final ZipOutputStream zout) throws IOException {
        int count;
        final int buffer = 2048;
        final byte data[] = new byte[buffer];

        while ((count = bis.read(data, 0, buffer)) != -1) {
            zout.write(data, 0, count);
        }
    }

    protected File getFile(final File parent, final String name) {
        return new File(parent, name);
    }

    protected ZipOutputStream getZipStream(final File file) throws FileNotFoundException{
        return new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
    }

    protected BufferedInputStream getInStream(final File inputFile) throws FileNotFoundException{
        return new BufferedInputStream(new FileInputStream(inputFile));
    }

    public static FileFilter getOldFileFilter(final Date cutOffDate) {
        return getOldFileFilter(cutOffDate, null);
    }

    public static FileFilter getOldFileFilter(final Date cutOffDate, final String ext) {
        return new FileFilter() {

            @Override
            public boolean accept(final File file) {
                final Date dateMod = new Date(file.lastModified());
                String fileExt = StringUtils.getFilenameExtension(file.getName());
                if(fileExt==null) {
                    fileExt="";
                }

                if (dateMod.before(cutOffDate) && (ext == null || fileExt.equals(ext))) {
                    return true;
                }

                return false;
            }
        };
    }

    public void moveFileToArchive(final String baseFilename, final Date date) throws IOException {
        final String newFilename = localArchiveDirectory + File.separator +
                addDateToFilename(baseFilename, date,archiveFileAppenderDateFormat);
        final File baseFile = getFileInstance(localWorkingDirectory + File.separator + baseFilename);
        final File toFile = getFileInstance(newFilename);
        moveFile(baseFile, toFile);
    }

    protected File getFileInstance(final String filename) {
        return new File(filename);
    }
    public String addDateToFilename(final String filename, final Date date, final String dateFormat) {
        final String extension = StringUtils.getFilenameExtension(filename);
        final String noExtensionBaseFile = StringUtils.stripFilenameExtension(filename);
        final String newFilename = noExtensionBaseFile + "_" + formatDate(date.getTime(),dateFormat)+"."+extension;

        return newFilename;
    }

    public void cleanupOldfiles(final File directory) {

        final Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, daysToKeepArchives);

        log.info("old file cutoff date: " + cal.getTime());

        final File[] files = directory.listFiles(getOldFileFilter(cal.getTime()));

        for (final File file : files) {
            file.delete();
        }

    }

    public int loadReport(final String reportFilename, final LoadReport loadReportLine, final Map<String, GenericDto> resultMap)
            throws IOException {
        BufferedReader reader = null;
        int lineCounter = 0;
        try {
            reader = getReader(reportFilename);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCounter != 0) {
                    loadReportLine.loadLine(line, resultMap);
                }
                lineCounter++;
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return lineCounter;
    }

    public static String formatDate(final long millis, final String format){
        final Date date = new Date(millis);
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static long parseUnixEpochDate(final String unixDateString) {
        final String strippedDate = unixDateString.substring(unixDateString.indexOf("(")+1, unixDateString.indexOf(")"));
        return Long.valueOf(strippedDate);
    }

    public BufferedWriter getWriter(final String fileName) throws IOException {
        return new BufferedWriter(new FileWriter(localWorkingDirectory+File.separator+fileName));
    }

    public BufferedReader getReader(final String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(localWorkingDirectory + File.separator + fileName));
    }

    public void moveFile(final File from, final File to) throws IOException {
        FileUtils.moveFile(from, to);
    }

    public void setArchiveFileAppenderDateFormat(final String archiveFileAppenderDateFormat) {
        this.archiveFileAppenderDateFormat = archiveFileAppenderDateFormat;
    }

    public void setZipFileExtensionDateFormat(final String zipFileExtensionDateFormat) {
        this.zipFileExtensionDateFormat = zipFileExtensionDateFormat;
    }

    public void setLocalArchiveDirectory(final String localArchiveDirectory) {
        this.localArchiveDirectory = localArchiveDirectory;
    }

    public void setLocalWorkingDirectory(final String localWorkingDirectory) {
        this.localWorkingDirectory = localWorkingDirectory;
    }

    public void setDaysToKeepArchives(final int monthsToKeepArchives) {
        daysToKeepArchives = monthsToKeepArchives;
    }

}
