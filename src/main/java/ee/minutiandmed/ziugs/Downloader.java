package ee.minutiandmed.ziugs;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;




@Component
public class Downloader {



    private static final String FOLDER = "/10_min_andmed/";

    private static final int NUMBER_OF_COLUMNS = 33;
    private static final int INDEX_OF_VISIBILITY = 20;
    private static final int INDEX_OF_WIND_DIRECTION = 11;
    private static final int INDEX_OF_WIND_SPEED = 7;
    private static final int INDEX_OF_WEATHER_FENOMENON = 15;
    private static final int INDEX_OF_CLOUDBASE = 22;
    private static final int INDEX_OF_OKTA = 23;
    private static final int INDEX_OF_UPDATE_TIME_ON_SERVER = 0;

    private List<String> updateTimeOnServer = new ArrayList<>();
    private List<String> windDirection = new ArrayList<>();
    private List<String> windSpeed = new ArrayList<>();
    private List<String> visibility = new ArrayList<>();
    private List<String> weatherFenomenon = new ArrayList<>();
    private List<String> cloudBase = new ArrayList<>();
    private List<String> okta = new ArrayList<>();

    public List<String> getUpdateTimeOnServer() {
        return updateTimeOnServer;
    }

    public List<String> getWindDirection() {
        return windDirection;
    }

    public List<String> getWindSpeed() {
        return windSpeed;
    }

    public List<String> getVisibility() {
        return visibility;
    }

    public List<String> getWeatherFenomenon() {
        return weatherFenomenon;
    }

    public List<String> getCloudBase() {
        return cloudBase;
    }

    public List<String> getOkta() {
        return okta;
    }

    @PostConstruct
    void doDownload() {

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
            e.printStackTrace();
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<String> parse(ByteArrayOutputStream bos) {

        try (CSVParser parser = new CSVParser(new StringReader(new String(bos.toByteArray(), "UTF-8")),
                CSVFormat.DEFAULT.withDelimiter(';'))) {
            List<CSVRecord> lines = parser.getRecords();
            List<String> allValues = new ArrayList<String>();
            allValues.clear();
            lines.get(lines.size() - 1).forEach(str -> allValues.add(str));

            for (int i = INDEX_OF_VISIBILITY; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                visibility.add(allValues.get(i));
            }
            for (int i = INDEX_OF_WIND_DIRECTION; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                windDirection.add(allValues.get(i));
            }
            for (int i = INDEX_OF_WIND_SPEED; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                windSpeed.add(allValues.get(i));
            }
            for (int i = INDEX_OF_WEATHER_FENOMENON; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                weatherFenomenon.add(allValues.get(i));
            }
            for (int i = INDEX_OF_CLOUDBASE; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                cloudBase.add(allValues.get(i));
            }
            for (int i = INDEX_OF_OKTA; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                okta.add(allValues.get(i));
            }
            for (int i = INDEX_OF_UPDATE_TIME_ON_SERVER; i < allValues.size(); i += NUMBER_OF_COLUMNS) {
                updateTimeOnServer.add(allValues.get(i));
            }

            return allValues;

        } catch (IOException e) {
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
            throw new RuntimeException(e);
        }

        return bos;
    }

    void clearLists() {
        cloudBase.clear();
        visibility.clear();
        okta.clear();
        updateTimeOnServer.clear();
        weatherFenomenon.clear();
        windDirection.clear();
        windSpeed.clear();

    }

}

