package flatManagement;

public class TenantLetter {

    private int DOI;    //day of issue
    private int MOI;    //month of issue
    private int YOI;    //year of issue
    private int LPD;    //last payment day
    private int LPM;    //last payment month
    private int LPY;    //last payment year
    private boolean IsActive;   //is this TenantLetter still active, or is it already in archive
    private String SpaceID;

    public TenantLetter (int DOI, int MOI, int YOI, String SpaceID){

        int[] DateOfLastPayment = DateCalculator.GetEndDate(DOI, MOI, YOI, 30);

        this.DOI = DOI;
        this.MOI = MOI;
        this.YOI = YOI;
        this.LPD = DateOfLastPayment[0];
        this.LPM = DateOfLastPayment[1];
        this.LPY = DateOfLastPayment[2];
        IsActive = true;
        this.SpaceID = SpaceID;

    }

    public TenantLetter (int DOI, int MOI, int YOI, String SpaceID, boolean IsActive){

        int[] DateOfLastPayment = DateCalculator.GetEndDate(DOI, MOI, YOI, 30);

        this.DOI = DOI;
        this.MOI = MOI;
        this.YOI = YOI;
        this.LPD = DateOfLastPayment[0];
        this.LPM = DateOfLastPayment[1];
        this.LPY = DateOfLastPayment[2];
        this.IsActive = IsActive;
        this.SpaceID = SpaceID;

    }

    public void MoveToArchive (){

        IsActive = false;

    }

    public int[] GetDateOfIssue (){

        return new int[]{DOI, MOI, YOI};

    }

    public boolean EqualsThisTenantLetter (TenantLetter checkTenantLetter) {

        boolean isEqual = false;

        String ID = checkTenantLetter.GetID();
        int[] lpd = checkTenantLetter.GetLastPaymentDate();
        int[] doi = checkTenantLetter.GetDateOfIssue();
        boolean IsActive = checkTenantLetter.IsActive();

        if (ID.equals(SpaceID) && lpd[0] == LPD && lpd[1] == LPM && lpd[2] == LPY && doi[0] == DOI && doi[1] == MOI && doi[2] == YOI && IsActive == this.IsActive)
            isEqual = true;

        return isEqual;

    }

    public boolean IsActive (){

        return IsActive;

    }

    public int[] GetLastPaymentDate (){

        return new int[]{LPD, LPM, LPY};

    }

    public String GetID (){

        return SpaceID;

    }

    @Override
    public String toString() {

        return ("-> SpaceID: " + SpaceID + "!& Date of issue: " + DOI + "." + MOI + "." + YOI +
                "!& Last payment date: " + LPD + "." + LPM + "." + LPY + "!& Currently " + (IsActive ? "" : "in") + "active");

        //note: REPLACE "!&" WITH ","

    }



}
