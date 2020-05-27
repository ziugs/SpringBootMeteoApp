package ee.minutiandmed.ziugs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ee.minutiandmed.ziugs.persistance.CsvIndex;
import ee.minutiandmed.ziugs.persistance.Stations;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class Downloader {

    private static final String FOLDER = "/10_min_andmed/";
    private static final int NUMBER_OF_COLUMNS = 33;
    private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private DownloaderData data = new DownloaderData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    @PostConstruct
    @Scheduled(cron = "0 4/10 0-23 * * ?")
    void doDownloadFromFtp() {
        try {
            rwlock.writeLock().lock();
            FTPClient ftpClient = new FTPClient();
            ftpClient.setControlEncoding(UTF_8.name());
            try {
                ftpClient.connect("ftp.emhi.ee", 21);
                ftpClient.login("ppalennusalk", "3ecugEcr");
                ftpClient.enterLocalPassiveMode();
                data.clear();
                Arrays.stream(Stations.values()).forEach(station ->
                        parse(retrieveFile(FOLDER.concat(station.getCsvFileName()), ftpClient)));

            } catch (IOException e) {
                LOGGER.error("Error happend  during reading the file", e);
                e.printStackTrace();
            } finally {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();

                } catch (IOException ex) {
                    LOGGER.error("Error happend durind logout and disconnect", ex);
                    ex.printStackTrace();
                }
            }
        } finally {
            rwlock.writeLock().unlock();
        }
    }

    private List<String> parse(ByteArrayOutputStream bos) {

        try (CSVParser parser = new CSVParser(new StringReader(new String(bos.toByteArray(), UTF_8.name())),
                CSVFormat.DEFAULT.withDelimiter(';'))) {
            List<CSVRecord> lines = parser.getRecords();
            List<String> allValues = new ArrayList<>();
            lines.get(lines.size() - 1).forEach(str -> allValues.add(str));
            Arrays.stream(CsvIndex.values()).forEach(csvIndex -> extractedData(csvIndex, allValues));
            return allValues;

        } catch (IOException e) {
            LOGGER.error("Runtime exeption occured in parse method", e);
            throw new RuntimeException(e);
        }
    }

    private ByteArrayOutputStream retrieveFile(String fileName, FTPClient ftpClient) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            if (!ftpClient.retrieveFile(fileName, bos)) {
                throw new RuntimeException(String.format("Unable to read %s", fileName));
            }
        } catch (IOException e) {
            LOGGER.error("Runtime exeption occured in retrieveFile method", e);
            throw new RuntimeException(e);
        }
        return bos;
    }

    public DownloaderData getData() {

        try {
            rwlock.readLock().lock();
            return data;

        } finally {
            rwlock.readLock().unlock();
        }
    }

    private void extractedData(CsvIndex index, List<String> allValues) {
        for (int i = index.getIndex(); i < allValues.size(); i += NUMBER_OF_COLUMNS) {
            index.getList(data).add(allValues.get(i));
        }
    }
}
