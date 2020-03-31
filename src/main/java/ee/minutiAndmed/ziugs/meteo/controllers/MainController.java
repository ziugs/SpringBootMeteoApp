package ee.minutiAndmed.ziugs.meteo.controllers;

import ee.minutiAndmed.ziugs.meteo.Downloader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.DataOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Controller
public class MainController {


    @RequestMapping(value = "/")
    public String home(Model model) {

        Downloader downloader = new Downloader();
        Downloader.allValues.clear();
        downloader.doDownload();

        Downloader.cloudBase.clear();
        Downloader.visibility.clear();
        Downloader.okta.clear();
        Downloader.updateTimeOnServer.clear();
        Downloader.weatherFenomenon.clear();
        Downloader.windDirection.clear();
        Downloader.windSpeed.clear();

        Downloader.putCloudBase();
        Downloader.putOkta();
        Downloader.putVisibility();
        Downloader.putWindDirection();
        Downloader.putWindSpeed();
        Downloader.putWeatherFenomenon();
        Downloader.putUpdateTimeOnServer();

        String[] stationsArray = {"jogeva", "johvi", "kihnu", "kunda", "kuusiku", "laanenigula", "narva", "parnu", "ristna", "ruhnu", "sorve", "harku", "toravere", "mustvee", "turi", "vaikemaarja", "viljandi", "vilsandi", "voru"};
        String[] stationsArray2 = {"jogeva2", "johvi2", "kihnu2", "kunda2", "kuusiku2", "laanenigula2", "narva2", "parnu2", "ristna2", "ruhnu2", "sorve2", "harku2", "toravere2", "mustvee2", "turi2", "vaikemaarja2", "viljandi2", "vilsandi2", "voru2"};
        String[] stationsArray3 = {"jogeva3", "johvi3", "kihnu3", "kunda3", "kuusiku3", "laanenigula3", "narva3", "parnu3", "ristna3", "ruhnu3", "sorve3", "harku3", "toravere3", "mustvee3", "turi3", "vaikemaarja3", "viljandi3", "vilsandi3", "voru3"};

        LocalDate date = LocalDate.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFornmat = DateTimeFormatter.ofPattern("dd.MM");
        String formattedDate = date.format(dateFornmat);

        LocalTime lt = LocalTime.parse(Downloader.updateTimeOnServer.get(0));
        model.addAttribute("time", "Hetkel on kaardil " + formattedDate + " " + timeFormat.format(lt.plusMinutes(3)) + " UTC andmed");

        for (int i = 0; i < stationsArray.length; i++) {
            if (Downloader.cloudBase.get(i).isBlank()) {
                model.addAttribute(stationsArray[i], "N/A " + Downloader.visibility.get(i) + " km");
            } else {
                model.addAttribute(stationsArray[i], Downloader.cloudBase.get(i) + " m " + Downloader.okta.get(i) + " " + Downloader.visibility.get(i) + " km");
            }
        }

        for (int i = 0; i < stationsArray2.length; i++) {
            if (Downloader.windDirection.get(i).isBlank()) {
                model.addAttribute(stationsArray2[i], "N/A");
            } else {
                model.addAttribute(stationsArray2[i], Downloader.windDirection.get(i) + "° " + Downloader.windSpeed.get(i) + " m/s");
            }
        }

        for (int i = 0; i < stationsArray3.length; i++) {
            if (Downloader.weatherFenomenon.get(i).isBlank()) {
                model.addAttribute(stationsArray3[i], "N/A");
            } else {
                model.addAttribute(stationsArray3[i], Downloader.weatherFenomenon.get(i));
            }
        }

        return "home";
    }

    public String display(Model model) {
        model.addAttribute("harku ", "WOW!");
        return "home";

    }
}

