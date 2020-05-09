package ee.minutiandmed.ziugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//@Component
public class RWRL {
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    Lock readLock = rwlock.readLock();
    Lock writeLock = rwlock.writeLock();

    @Autowired
    Downloader downloader;

    @Scheduled(cron = "0 3/10 0-23 * * ?")
    void updateLists() {
        readLock.lock();
        System.out.println("Lock");
        try {
            downloader.clearLists();
            System.out.println(downloader.getUpdateTimeOnServer().get(0));
            downloader.doDownload();
            System.out.println(downloader.getUpdateTimeOnServer().get(0));
        } finally {
            readLock.unlock();
            System.out.println("unlock");
        }
    }

}
