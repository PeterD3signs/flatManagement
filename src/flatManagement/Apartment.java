package flatManagement;

public class Apartment implements SpaceToRent {

    private String ID;

    // ID generated in form of:
    // S-B-N
    // where:
    // S - subdivision ID
    // B - block of flats number
    // N - apartment number

    private float volume;

    private int begDay;
    private int begMonth;
    private int begYear;
    private int endDay;
    private int endMonth;
    private int endYear;
    private boolean currentlyOccupied;
    private String tenantPESEL;


    public Apartment(float volume, int subdivisionID, int BOFNumber , int aptNumber) {

        //data check

        ID = GenerateID(subdivisionID, BOFNumber , aptNumber);
        this.volume = volume;
        Clear();

    }

    public String GetTenantPESEL () {

        return tenantPESEL;

    }

    public String GetID () {

        return ID;

    }

    public Apartment(float volume, String ID, int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear, boolean currentlyOccupied, String tenantPESEL ){

        //A constructor for remaking the apartment after reading the file.

        this.ID = ID;
        this.volume = volume;
        UpdateBegDate(begDay, begMonth, begYear);
        UpdateEndDate(endDay, endMonth, endYear);
        this.currentlyOccupied = currentlyOccupied;
        this.tenantPESEL = tenantPESEL;

    }

    public void Clear() {

        UpdateBegDate(0, 0, 0);
        UpdateEndDate(0, 0, 0);
        currentlyOccupied = false;
        tenantPESEL = null;

    }

    @Override
    public void Rent(int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear, String tenantPESEL) throws CustomException{

        //data check for date

        if (currentlyOccupied)
            throw new CustomException(ExceptionMessages.AlreadyOccupied.toString());

        UpdateBegDate(begDay, begMonth, begYear);
        UpdateEndDate(endDay, endMonth, endYear);
        this.tenantPESEL = tenantPESEL;
        currentlyOccupied = true;

    }

    public String ExtendRent (int NoOfMonths){

        String tenantPESEL = null;

        int[]endDate = DateCalculator.GetEndDate(endDay, endMonth, endYear, NoOfMonths, 0);

        if (currentlyOccupied){
            tenantPESEL = this.tenantPESEL;
            UpdateEndDate(endDate[0], endDate[1], endDate[2]);
        }

        return tenantPESEL;

    }

    @Override
    public float GetVolume() {
        return volume;
    }

    @Override
    public void UpdateBegDate(int begDay, int begMonth, int begYear) {

        //data check for date

        this.begDay = begDay;
        this.begMonth = begMonth;
        this.begYear = begYear;

    }

    @Override
    public void UpdateEndDate(int endDay, int endMonth, int endYear) {

        //data check for date

        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;

    }

    private String GenerateID(int subdivisionID, int BOFNumber, int aptNumber) {

        return (subdivisionID + "-" + BOFNumber + "-" + aptNumber);

    }

    @Override
    public boolean CurrentlyOccupied() {

        return currentlyOccupied;

    }

    @Override
    public String toString() {

        int PersonIndex = Main.GetPersonIndex(tenantPESEL);
        String CoTenantPESELs = Main.GetCoTenantsPESELList(ID, "\n\t\t\t\t");

        return (

                "\t\t\t-Apartment " + ID + ":" +
                        "\n\t\t\t\tvolume: " + volume + ", " + (currentlyOccupied ? ("tenant PESEL: " + tenantPESEL + " (index on people list: " + PersonIndex + ");") : "unoccupied;") +
                        ( currentlyOccupied ? (
                        "\n\t\t\t\tbeginning of rent: " + begDay + "." + begMonth + "." + begYear + ", end of rent: " + endDay + "." + endMonth + "." + endYear + ";" +
                        "\n\t\t\t\t" + (CoTenantPESELs.equals("-1") ? "No co-tenants." : ( "Co-tenants:" + CoTenantPESELs ) ) )
                        : ""
                        )

        );

    }

    public String GetStatus(boolean rentPaid, int[] LastPaymentDate) {

        return ("Apartment " + ID + ", " + (currentlyOccupied ? ("tenant PESEL: " + tenantPESEL + ", rent " +
                (rentPaid ? "payed" :("not payed - last payment date: " + LastPaymentDate[0] + "." + LastPaymentDate[1] + "." + LastPaymentDate[2]))) : "unoccupied") + ";" );

    }

    public int[] GetEndDate (){

        return new int[]{endDay, endMonth, endYear};

    }



}
