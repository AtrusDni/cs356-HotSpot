public class DeadlockTest {

    static class DeadlockTestThread extends Thread {

        public String id;
	private DeadlockTestThread dltt;

        public DeadlockTestThread(String id) {
        	this.id = id;
        }

	public void setDeadlockTestThread(DeadlockTestThread dltt) {
		this.dltt = dltt;
	}

	public void run() {
		this.ping(dltt);
	}

        public synchronized void ping(DeadlockTestThread ponger) {
            System.out.format("%s: pinging %s\n", this.id, ponger.id);
            ponger.pong(this);
        }

        public synchronized void pong(DeadlockTestThread pinger) {
            System.out.format("%s: ponging %s\n", this.id, pinger.id);
        }

    }

    public static void main(String[] args) {
	System.out.println("Started!");
        DeadlockTestThread dltt1 = new DeadlockTestThread("dltt1");
        DeadlockTestThread dltt2 = new DeadlockTestThread("dltt2");
        DeadlockTestThread dltt3 = new DeadlockTestThread("dltt3");

	dltt1.setDeadlockTestThread(dltt2);
	dltt1.setDeadlockTestThread(dltt3);
	
	dltt2.setDeadlockTestThread(dltt1);
	dltt2.setDeadlockTestThread(dltt3);

	dltt3.setDeadlockTestThread(dltt1);
	dltt3.setDeadlockTestThread(dltt2);

        dltt1.start();
        dltt2.start();
        dltt3.start();

	//System.out.println("Finished!");
    }
}
