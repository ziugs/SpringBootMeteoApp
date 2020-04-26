package ee.minutiAndmed.ziugs.meteo;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Downloader {

    private static final String FOLDER = "/10_min_andmed/";

    private static final int NUMBEROFCOLUMNS = 33;
    private static final int INDEXOFVISIBILITY = 20;
    private static final int INDEXOFWINDDIRECTION = 11;
    private static final int INDEXOFWINDSPEED = 7;
    private static final int INDEXOFWEATHERFENOMENON = 15;
    private static final int INDEXOFCLOUDBASE = 22;
    private static final int INDEXOFOKTA = 23;
    private static final int INDEXOFUPDATETIMEONSERVER = 0;

    private List<String> allValues = new ArrayList<>();
    private List<String> updateTimeOnServer = new ArrayList<>();
    private List<String> windDirection = new ArrayList<>();
    private List<String> windSpeed = new ArrayList<>();
    private List<String> visibility = new ArrayList<>();
    private List<String> weatherFenomenon = new ArrayList<>();
    private List<String> cloudBase = new ArrayList<>();
    private List<String> okta = new ArrayList<>();



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
                CSVFormat.DEFAULT.withDelimiter(';'));) {
            List<CSVRecord> lines = parser.getRecords();
            List<String> result = new ArrayList<String>();
            lines.get(lines.size() - 1).forEach(str -> result.add(str));
            for (int i = 0; i < result.size(); i++) {
                String b = result.get(i);
                allValues.add(b);
            }

            return result;

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

    private void putToList() {
        for (int i = INDEXOFVISIBILITY; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            visibility.add(allValues.get(i));
        }
        for (int i = INDEXOFWINDDIRECTION; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            windDirection.add(allValues.get(i));
        }
        for (int i = INDEXOFWINDSPEED; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            windSpeed.add(allValues.get(i));
        }
        for (int i = INDEXOFWEATHERFENOMENON; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            weatherFenomenon.add(allValues.get(i));
        }
        for (int i = INDEXOFCLOUDBASE; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            cloudBase.add(allValues.get(i));
        }
        for (int i = INDEXOFOKTA; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            okta.add(allValues.get(i));
        }
        for (int i = INDEXOFUPDATETIMEONSERVER; i < allValues.size(); i += NUMBEROFCOLUMNS) {
            allValues.get(i);
            updateTimeOnServer.add(allValues.get(i));
        }
    }

    private void clearLists(){
        cloudBase.clear();
        visibility.clear();
        okta.clear();
        updateTimeOnServer.clear();
        weatherFenomenon.clear();
        windDirection.clear();
        windSpeed.clear();

    }

}

