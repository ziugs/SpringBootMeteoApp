package ee.minutiandmed.ziugs.controllers;

import ee.minutiandmed.ziugs.Downloader;
import ee.minutiandmed.ziugs.DownloaderData;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class MainController {


    private static final String[] STATION_SARRAY = {"jogeva", "johvi", "kihnu", "kunda", "kuusiku", "laanenigula",
            "narva", "parnu", "ristna", "ruhnu", "sorve", "harku", "toravere", "mustvee", "turi", "vaikemaarja",
            "viljandi", "vilsandi", "voru"};


    @Autowired
    private Downloader downloader;

    @RequestMapping(value = "/")
    public String home(Model model) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM");
        String formattedDate = date.format(dateFormat);
        DownloaderData data = downloader.getData();
        System.out.println(data.getUpdateTimeOnServer().get(0));

        LocalTime lt = LocalTime.parse(data.getUpdateTimeOnServer().get(0));
        model.addAttribute("time", String.format("Hetkel on kaardil %s %s UTC andmed", formattedDate, timeFormat.format(lt.plusMinutes(3))));

        for (int i = 0; i < STATION_SARRAY.length; i++) {
            if (data.getCloudBase().get(i).isBlank()) {
                model.addAttribute(STATION_SARRAY[i], String.format("N/A %s km", data.getVisibility().get(i)));
            } else {
                model.addAttribute(STATION_SARRAY[i], String.format("%s m %s %s km <br/> %s° %s m/s <br/> %s",
                        data.getCloudBase().get(i), data.getOkta().get(i), data.getVisibility().get(i),
                        data.getWindDirection().get(i), data.getWindSpeed().get(i),
                        data.getWeatherFenomenon().get(i)));
            }
        }

        return "home";
    }


}

