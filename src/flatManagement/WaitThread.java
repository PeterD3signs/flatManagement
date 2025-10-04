package flatManagement;

public class WaitThread extends Thread{

    public void run () {

        try {
            sleep(1000);
        } catch (Exception ex){
            EventLog.AddEvent("WaitThread was interrupted");
        }

    }
}
