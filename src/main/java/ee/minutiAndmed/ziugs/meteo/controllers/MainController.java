package ee.minutiAndmed.ziugs.meteo.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Controller
public class MainController {


    private static final String[] STATIONSARRAY = {"jogeva", "johvi", "kihnu", "kunda", "kuusiku", "laanenigula", "narva", "parnu", "ristna", "ruhnu", "sorve", "harku", "toravere", "mustvee", "turi", "vaikemaarja", "viljandi", "vilsandi", "voru"};
    private static final String[] STATIONSARRAY2 = {"jogeva2", "johvi2", "kihnu2", "kunda2", "kuusiku2", "laanenigula2", "narva2", "parnu2", "ristna2", "ruhnu2", "sorve2", "harku2", "toravere2", "mustvee2", "turi2", "vaikemaarja2", "viljandi2", "vilsandi2", "voru2"};
    private static final String[] STATIONSARRAY3 = {"jogeva3", "johvi3", "kihnu3", "kunda3", "kuusiku3", "laanenigula3", "narva3", "parnu3", "ristna3", "ruhnu3", "sorve3", "harku3", "toravere3", "mustvee3", "turi3", "vaikemaarja3", "viljandi3", "vilsandi3", "voru3"};

    @RequestMapping(value = "/")
    public String home(Model model) {

        LocalDate date = LocalDate.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM");
        String formattedDate = date.format(dateFormat);

        LocalTime lt = LocalTime.parse(downloader.updateTimeOnServer.get(0));
        model.addAttribute("time", "Hetkel on kaardil " + formattedDate + " " + timeFormat.format(lt.plusMinutes(3)) + " UTC andmed");

        for (int i = 0; i < STATIONSARRAY.length; i++) {
            if (downloader.cloudBase().get(i).isBlank()) {
                model.addAttribute(STATIONSARRAY[i], "N/A " + downloader.visibility().get(i) + " km");
            } else {
                model.addAttribute(STATIONSARRAY[i], downloader.cloudBase().get(i) + " m " + downloader.okta().get(i) + " " + downloader.visibility().get(i) + " km");
            }
        }

        for (int i = 0; i < STATIONSARRAY2.length; i++) {
            if (downloader.windDirection().get(i).isBlank()) {
                model.addAttribute(STATIONSARRAY2[i], "N/A");
            } else {
                model.addAttribute(STATIONSARRAY2[i], downloader.windDirection().get(i) + "° " + downloader.windSpeed().get(i) + " m/s");
            }
        }

        for (int i = 0; i < STATIONSARRAY3.length; i++) {
            if (downloader.weatherFenomenon().get(i).isBlank()) {
                model.addAttribute(STATIONSARRAY3[i], "N/A");
            } else {
                model.addAttribute(STATIONSARRAY3[i], downloader.weatherFenomenon().get(i));
            }
        }

        return "home";
    }

}

