package ee.minutiAndmed.ziugs.meteo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class MeteoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeteoApplication.class, args);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Downloader downloader = context.getBean("downloader", Downloader.class);
        downloader.doDownload();

        context.close();

    }


}


