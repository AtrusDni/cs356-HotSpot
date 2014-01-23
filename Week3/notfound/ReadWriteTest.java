//package readwritetest;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.lang.management.*;

public class ReadWriteTest {
    
    static ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    public static void main(String[] args) {
        Reader reader = new Reader();
        Writer writer = new Writer();
        sleep(10);
        System.out.println("Finding deadlocked threads");
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] ids = bean.findDeadlockedThreads();
        if(ids != null) {
            ThreadInfo[] infos = bean.getThreadInfo(ids, true, true);
            System.out.println("The following threads are deadlocked: ");
            for (ThreadInfo threadInfo : infos) {
                System.out.println(threadInfo);
            }
        }
    }
    
    static void sleep (int secs) {
        try {
            Thread.currentThread().sleep(secs*1000);
        }
        catch (InterruptedException e) {
            
        }
    }
    
    static class Reader implements Runnable {
        Reader() {
            new Thread(this).start();
        }
        public void run() {
            sleep(2);
            System.out.println("Reader thread getting lock");
            rwlock.readLock().lock();
            System.out.println("Reader thread got lock");
            synchronized (rwlock) {
                System.out.println("Reader thread inside monitor");
                rwlock.readLock().unlock();
            }
        }
    }
    
    static class Writer implements Runnable {
        Writer() {
            new Thread(this).start();
        }
        public void run() {
            synchronized (rwlock) {
                sleep(4);
                System.out.println("Writer thread getting lock");
                rwlock.writeLock().lock();
                System.out.println("Writer thread got lock");
            }
        }
    }
}
