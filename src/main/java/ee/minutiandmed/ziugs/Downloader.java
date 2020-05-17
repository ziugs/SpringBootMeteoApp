package ee.minutiandmed.ziugs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private ReadWriteLock rwlock = new ReentrantReadWriteLock();

    private static final String FOLDER = "/10_min_andmed/";

    private static final int NUMBER_OF_COLUMNS = 33;
    private static final int INDEX_OF_VISIBILITY = 20;
    private static final int INDEX_OF_WIND_DIRECTION = 11;
    private static final int INDEX_OF_WIND_SPEED = 7;
    private static final int INDEX_OF_WEATHER_FENOMENON = 15;
    private static final int INDEX_OF_CLOUDBASE = 22;
    private static final int INDEX_OF_OKTA = 23;
    private static final int INDEX_OF_UPDATE_TIME_ON_SERVER = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class);

    private DownloaderData data = new DownloaderData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    @PostConstruct
    @Scheduled(cron = "0 4/10 0-23 * * ?")
    void doDownloadFromFtp() {
        try {

            rwlock.writeLock().lock();
            FTPClient ftpClient = new FTPClient();
            ftpClient.setControlEncoding("UTF-8");
            Map<Stations, List<String>> meteoData = new HashMap<>();
            try {
                ftpClient.connect("ftp.emhi.ee", 21);
                ftpClient.login("ppalennusalk", "3ecugEcr");
                ftpClient.enterLocalPassiveMode();

                Arrays.stream(Stations.values()).forEach(station -> meteoData.put(station,
                        parse(retrieveFile(FOLDER.concat(station.getCsvFileName()), ftpClient))));

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

        try (CSVParser parser = new CSVParser(new StringReader(new String(bos.toByteArray(), "UTF-8")),
                CSVFormat.DEFAULT.withDelimiter(';'))) {
            List<CSVRecord> lines = parser.getRecords();
            List<String> allValues = new ArrayList<String>();
            lines.get(lines.size() - 1).forEach(str -> allValues.add(str));

            for (int i = INDEX_OF_VISIBILITY; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getVisibility().add(allValues.get(i));
            }
            for (int i = INDEX_OF_WIND_DIRECTION; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getWindDirection().add(allValues.get(i));
            }
            for (int i = INDEX_OF_WIND_SPEED; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getWindSpeed().add(allValues.get(i));
            }
            for (int i = INDEX_OF_WEATHER_FENOMENON; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getWeatherFenomenon().add(allValues.get(i));
            }
            for (int i = INDEX_OF_CLOUDBASE; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getCloudBase().add(allValues.get(i));
            }
            for (int i = INDEX_OF_OKTA; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getOkta().add(allValues.get(i));
            }
            for (int i = INDEX_OF_UPDATE_TIME_ON_SERVER; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                data.getUpdateTimeOnServer().add(allValues.get(i));
            }

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

}
