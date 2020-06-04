package ee.minutiandmed.ziugs;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DownloaderData {
    private List<String> updateTimeOnServer;
    private List<String> windDirection;
    private List<String> windSpeed;
    private List<String> visibility;
    private List<String> weatherFenomenon;
    private List<String> cloudBase;
    private List<String> okta;

    public DownloaderData(List<String> updateTimeOnServer, List<String> windDirection, List<String> windSpeed,
                          List<String> visibility, List<String> weatherFenomenon, List<String> cloudBase, List<String> okta) {
        this.updateTimeOnServer = updateTimeOnServer;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.weatherFenomenon = weatherFenomenon;
        this.cloudBase = cloudBase;
        this.okta = okta;
    }

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
    public void clear() {
        getUpdateTimeOnServer().clear();
        getOkta().clear();
        getCloudBase().clear();
        getWeatherFenomenon().clear();
        getWindDirection().clear();
        getVisibility().clear();
        getWindSpeed().clear();

    }
}