package flatManagement;

import java.util.LinkedList;

public class CheckRentThread extends Thread{

    boolean SimulateTime = true;
    public static final String monitor = TimeSimThread.monitor;

    private static long SleepForMS = 10000;

    public void run() {

        int[] CurrentDate;

        synchronized (monitor){

            CurrentDate = Main.GetDate();

        }

        checkRent( CurrentDate );

        try {

            //Sleeping for a 1000 milliseconds so that later the programme does not write events in incorrect order in event log.
            //Not important in the sense of correct execution.
            sleep(1000);

        } catch (Exception ex){
            System.out.println("InterruptedExceptionRENT");
        }

        while (SimulateTime){

            try {
                sleep(SleepForMS);
            } catch (Exception ex){
                System.out.println("InterruptedExceptionRENT");
            }

            long timeStart = System.currentTimeMillis();

            synchronized (monitor){

                CurrentDate = Main.GetDate();

            }

            checkRent(CurrentDate);
            CheckTenantLetters(CurrentDate);

            long timeEnd = System.currentTimeMillis();

            SleepForMS = 10000 - (timeEnd - timeStart);

        }

        EventLog.AddEvent("Rent status check ended;");

    }

    public void end(){

        SimulateTime = false;

    }

    private static void checkRent(int[] CurrentDate){

        LinkedList<Subdivision> subdivisions = Main.GetSub();

        for (Subdivision s : subdivisions) {

            LinkedList<BlockOfFlats> BOF = s.GetBOF();
            LinkedList<ParkingSpace> PS = s.GetPS();

            for (BlockOfFlats b : BOF) {

                LinkedList<Apartment> apartments = b.GetApartments();

                for (Apartment a : apartments) {

                    int[] endDate = a.GetEndDate();
                    String aptID = a.GetID();

                    if ( a.CurrentlyOccupied() && DateCalculator.IsDate2Greater(endDate, CurrentDate) && NoTenantLetterYet( GetTenantLetters( aptID ) , aptID )) {      //checking for expired rent

                        boolean correct = true;

                        try {
                            Main.AddTenantLetter(aptID, CurrentDate[0], CurrentDate[1], CurrentDate[2]);
                        } catch (CustomException ex) {

                            correct = false;
                            EventLog.AddEvent("Unexpected exception raised by CheckRentThread with message " + ex.getMessage() + " when checking an apartment with ID: " + aptID + ";");

                        }

                        if (correct)
                            EventLog.AddEvent("Added a TenantLetter to an apartment with ID: " + aptID + ";");

                    }

                }

            }

            for (ParkingSpace p : PS) {

                int[] endDate = p.GetEndDate();
                String psID = p.GetID();

                if ( p.CurrentlyOccupied() && DateCalculator.IsDate2Greater(endDate, CurrentDate) && NoTenantLetterYet( GetTenantLetters( psID ) , psID )) {

                    boolean correct = true;

                    try {
                        Main.AddTenantLetter( psID , CurrentDate[0], CurrentDate[1], CurrentDate[2]);
                    } catch (Exception ex) {

                        correct = false;
                        EventLog.AddEvent("Unexpected exception raised by CheckRentThread with message " + ex.getMessage() + " when checking a parking space with ID: " + psID + ";");

                    }

                    if (correct)
                        EventLog.AddEvent("Added a TenantLetter to a parking space with ID: " + psID + ";");

                }

            }

        }

        EventLog.AddEvent("Rent checked;");

    }

    private static void CheckTenantLetters(int[] CurrentDate){

        LinkedList<Person> people = Main.GetPeople();
        LinkedList<TenantLetter> tenantLettersToUpdate = new LinkedList<>();

        for (Person person : people) {

            LinkedList<TenantLetter> tenantLettersToCheck = person.GetTenantLetters();

            for (TenantLetter t : tenantLettersToCheck)
                if ( t.IsActive() && DateCalculator.IsDate2Greater(t.GetLastPaymentDate(), CurrentDate ) )
                    tenantLettersToUpdate.add(t);
        }

        Main.SetTenantLettersToExpired( tenantLettersToUpdate );

    }

    private static boolean NoTenantLetterYet ( LinkedList<TenantLetter> tenantLetters, String SpaceID ){

        boolean NoTLYet = true;

        for (int i = 0; i < tenantLetters.size(); i++)
            if (tenantLetters.get(i).GetID().equals(SpaceID)){
                NoTLYet = false;
                i = tenantLetters.size();
            }

        return NoTLYet;

    }

    private static LinkedList<TenantLetter> GetTenantLetters ( String SpaceID ){

        LinkedList<TenantLetter> tenantLetters = new LinkedList<>();

        try{
            tenantLetters = Main.GetTenantLetters( SpaceID );
        } catch (Exception ex){
            EventLog.AddEvent("Unexpected exception raised by GetTenantLetters method in CheckRentThread with message " + ex.getMessage() + ";");
        }

        return tenantLetters;

    }
}
