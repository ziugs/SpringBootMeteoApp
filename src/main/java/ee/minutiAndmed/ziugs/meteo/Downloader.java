package ee.minutiAndmed.ziugs.meteo;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.ftp.FTPClient;

public class Downloader {

    private static final String FOLDER = "/10_min_andmed/";
    public static List<String> allValues = new ArrayList<>();
    public static List<String> updateTimeOnServer = new ArrayList<>();
    public static List<String> windDirection = new ArrayList<>();
    public static List<String> windSpeed = new ArrayList<>();
    public static List<String> visibility = new ArrayList<>();
    public static List<String> weatherFenomenon = new ArrayList<>();
    public static List<String> cloudBase = new ArrayList<>();
    public static List<String> okta = new ArrayList<>();

    public void doDownload() {
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

    public static void putVisibility() {
        for (int i = 20; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.visibility.add(Downloader.allValues.get(i));

        }
    }

    public static void putWindDirection() {
        for (int i = 11; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.windDirection.add(Downloader.allValues.get(i));

        }
    }
    public static void putWindSpeed() {
        for (int i = 7; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.windSpeed.add(Downloader.allValues.get(i));

        }
    }

    public static void putWeatherFenomenon() {
        for (int i = 15; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.weatherFenomenon.add(Downloader.allValues.get(i));

        }
    }

    public static void putCloudBase() {
        for (int i = 22; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.cloudBase.add(Downloader.allValues.get(i));

        }
    }

    public static void putOkta() {
        for (int i = 23; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.okta.add(Downloader.allValues.get(i));
        }
    }

    public static void putUpdateTimeOnServer() {
        for (int i = 0; i < Downloader.allValues.size(); i += 33) {
            Downloader.allValues.get(i);
            Downloader.updateTimeOnServer.add(Downloader.allValues.get(i));
        }
    }

}

