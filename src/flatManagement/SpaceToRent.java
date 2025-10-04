package flatManagement;

public interface SpaceToRent {

    float GetVolume ();

    void UpdateBegDate (int begDay, int begMonth, int begYear);

    void UpdateEndDate (int endDay, int endMonth, int endYear);

    boolean CurrentlyOccupied ();

    void Clear ();

    void Rent (int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear, String tenantPESEL) throws Exception;

    String GetStatus(boolean rentPaid, int[] LastPaymentDate);

    String GetTenantPESEL ();


}
