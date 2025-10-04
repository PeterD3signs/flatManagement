package flatManagement;

public class TimeSimThread extends Thread{

    boolean SimulateTime = true;
    int[] PresentDate = new int[]{1,1,2000};

    private static long SleepForMS = 5000;

    public static final String monitor = new String();

    public void run() {

        while (SimulateTime){

            try {
                sleep(SleepForMS);
            } catch (Exception ex){
                System.out.println("InterruptedExceptionTIME");
            }

            long timeStart = System.currentTimeMillis();

            PresentDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 1);

            synchronized (monitor){

                Main.UpdateDate(PresentDate);

            }

            EventLog.AddEvent("Changed current date to: " + PresentDate[0] + "." + PresentDate[1] + "." + PresentDate[2] + ";");

            long timeEnd = System.currentTimeMillis();

            SleepForMS = 5000 - (timeEnd - timeStart);

        }

        EventLog.AddEvent("Time simulation ended;");

    }

    public void SetPresentDate (int[] PresentDate){

        this.PresentDate = PresentDate;

    }

    public void end(){

        SimulateTime = false;

    }

}
