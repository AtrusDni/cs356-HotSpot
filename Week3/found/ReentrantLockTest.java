import java.util.concurrent.TimeUnit;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    
    public ReentrantLockTest() {
        final ReentrantLock lock1 = new ReentrantLock();
    final ReentrantLock lock2 = new ReentrantLock();

    Thread thread1 = new Thread(new Runnable() {
        @Override public void run() {
            try {
                lock1.lock();
                System.out.println("Thread1 acquired lock1");
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException ignore) {}
                lock2.lock();
                System.out.println("Thread1 acquired lock2");
            }
            finally {
                lock2.unlock();
                lock1.unlock();
            }
        }
    });
    thread1.start();

    Thread thread2 = new Thread(new Runnable() {
            @Override public void run() {
                try {
                    lock2.lock();
                    System.out.println("Thread2 acquired lock2");
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException ignore) {}
                    lock1.lock();
                    System.out.println("Thread2 acquired lock1");
                }
                finally {
                    lock1.unlock();
                    lock2.unlock();
                }
            }
        });
    thread2.start();

    try {
        TimeUnit.MILLISECONDS.sleep(100);
    } catch (InterruptedException ignore) {}
        detectDeadlock();
    }
    public void detectDeadlock() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadBean.findMonitorDeadlockedThreads();
        int deadlockedThreads = threadIds != null? threadIds.length : 0;
        System.out.println("Number of deadlocked threads: " + deadlockedThreads);
    }
    public static void main(String[] args) {
        ReentrantLockTest relock = new ReentrantLockTest();
    }
}
