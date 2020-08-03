package ee.minutiandmed.ziugs.controllers;

import ee.minutiandmed.ziugs.Downloader;
import ee.minutiandmed.ziugs.DownloaderData;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Controller
public class MainController {

    private static final String[] STATIONS_ARRAY = {"jogeva", "johvi", "kihnu", "kunda", "kuusiku", "laanenigula",
            "narva", "parnu", "ristna", "ruhnu", "sorve", "harku", "toravere", "mustvee", "turi", "vaikemaarja",
            "viljandi", "vilsandi", "voru"};

    @Autowired
    private Downloader downloader;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM");
        String formattedDate = date.format(dateFormat);
        DownloaderData data = downloader.getData();
        String na = "N/A";

        LocalTime lt = LocalTime.parse(data.getUpdateTimeOnServer().get(0));
        model.addAttribute("time", String.format("Hetkel on kaardil %s %s UTC andmed", formattedDate, timeFormat.format(lt.plusMinutes(4))));


        for (int i = 0; i < STATIONS_ARRAY.length; i++) {
            long convertedToFeet = Math.round(NumberUtils.toInt(data.getCloudBase().get(i)) * 3.3);
            float convertedToKnots = 0;
            if (data.getWindSpeed().get(i).isEmpty()) {
                data.getWindSpeed().set(i, "0");
            } else {
                String str = data.getWindSpeed().get(i).replace(",", ".");
                convertedToKnots = (float) Math.round(Float.parseFloat(str) * 1.94);
            }
            DecimalFormat df = new DecimalFormat("###");
            if (data.getCloudBase().get(i).isEmpty()) {
                model.addAttribute(STATIONS_ARRAY[i], String.format("N/A %s km <br/> %s° %s kt <br/> %s",
                        data.getVisibility().get(i), data.getWindDirection().get(i), df.format(convertedToKnots),
                        data.getWeatherFenomenon().get(i)));
            } else {
                model.addAttribute(STATIONS_ARRAY[i], String.format("%d ft %s %s km <br/> %s° %s kt <br/> %s",
                        convertedToFeet, data.getOkta().get(i), data.getVisibility().get(i),
                        data.getWindDirection().get(i), df.format(convertedToKnots),
                        data.getWeatherFenomenon().get(i)));
            }
        }

        return "home";
    }
}

