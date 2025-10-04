package flatManagement;

import java.util.LinkedList;

public class Person {

    private String name;
    private String surname;
    private String PESEL;
    private String adress;
    private int DOB;    //day of birth
    private int MOB;    //month of birth
    private int YOB;    //year of birth
    private LinkedList<TenantLetter> TenantLetters;
    private LinkedList<String> IDsOfRentedSpaces;
    private LinkedList<String> IDsOfOccupiedSpaces;      //Spaces that a given person occupies, but are not rented by this person (only apartments)

    public Person (String name, String surname, String PESEL, String adress, int DOB, int MOB, int YOB,
                   LinkedList<TenantLetter> TennantLetters, LinkedList<String> IDsOfRentedSpaces, LinkedList<String> IDsOfOccupiedSpaces){

        this.name = name;
        this.surname = surname;
        this.PESEL = PESEL;
        this.adress = adress;
        this.DOB = DOB;
        this.MOB = MOB;
        this.YOB = YOB;
        this.TenantLetters = TennantLetters;
        this.IDsOfRentedSpaces = IDsOfRentedSpaces;
        this.IDsOfOccupiedSpaces = IDsOfOccupiedSpaces;

    }

    public LinkedList<String> GetIDsOfRentedSpaces (){

        return IDsOfRentedSpaces;

    }

    public String GetIDsOfRentedSpacesInString (){

        return IDsOfRentedSpaces.toString().substring(1, IDsOfRentedSpaces.toString().lastIndexOf(']'));

    }

    public String GetPESEL (){

        return PESEL;

    }

    public String GetSubdivisionID(String ID) {

        return ID.substring(0, ID.indexOf("-"));

    }

    public void ClearTenantLetter (String SpaceToRentID ){

        for (int i = 0; i < TenantLetters.size(); i++)
            if ( SpaceToRentID.equals(TenantLetters.get(i).GetID() ) ){

                TenantLetters.remove(i);
                i = TenantLetters.size();

            }


    }

    private boolean MoreThan3TenantLetters(String SubdivisionID){

        int occurances = 0;

        for (TenantLetter tenantLetter : TenantLetters)
            if (GetSubdivisionID(tenantLetter.GetID()).equals(SubdivisionID))
                occurances++;

        return (occurances >= 3);

    }

    private boolean IsAFlat (String ID){

        return (ID.length() - ID.replace("-","").length() == 2);

    }

    public boolean IsCoTenant (String ID){

        boolean contains = false;

        for (int i = 0; i < IDsOfOccupiedSpaces.size(); i++)
            if (IDsOfOccupiedSpaces.get(i).equals( ID )){

                contains = true;
                i = IDsOfOccupiedSpaces.size();

            }

        return contains;

    }

    public void RentSpace (String IDofRentedSpace) throws CustomException, ProblematicTenantException {

        String SubdivisionID = GetSubdivisionID(IDofRentedSpace);

        if (IDsOfRentedSpaces.size() == 5 )
            throw new CustomException(ExceptionMessages.Already5SpacesRented.toString());

        if(MoreThan3TenantLetters(SubdivisionID))       //if >= 3 TenantLetters for a given Subdivision
            throw new ProblematicTenantException( GetPESEL(), GetIDsOfRentedSpacesInString() );

        //checking for already rented facilities on a given subdivision, if there is a request for renting a parking space
        if ( !IsAFlat(IDofRentedSpace) ){

            boolean found = false;

            for (int i = 0; i < IDsOfRentedSpaces.size(); i++)
                if (SubdivisionID.equals(GetSubdivisionID(IDsOfRentedSpaces.get(i)))){  //if any other facility with the same subdivision ID is found, a parking space can be rented
                    found = true;
                    i = IDsOfRentedSpaces.size();
                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoFlatsRentedOnGivenSubdivision.toString());

        }

        //adding to the list of rented spaces:
        IDsOfRentedSpaces.add(IDofRentedSpace);

    }

    public void RentUnsuccessful (String IDofRentedSpace) {     //Used if the process of renting a place was unsuccessful

        IDsOfRentedSpaces.remove(IDofRentedSpace);

    }

    public void StopRent (String IDofRentedSpace) throws CustomException {

        boolean success = false;

        for (int i = 0; i < IDsOfRentedSpaces.size(); i++)
            if (IDofRentedSpace.equals(IDsOfRentedSpaces.get(i))){

                success = true;

                //removing active tenant letters:
                for (int j = 0; j < TenantLetters.size(); j++)
                    if (TenantLetters.get(j).GetID().equals( IDofRentedSpace )){

                        if (TenantLetters.get(j).IsActive())
                            TenantLetters.remove(j);

                        j = TenantLetters.size();

                    }

                IDsOfRentedSpaces.remove(i);
                i = IDsOfRentedSpaces.size();

            }

        if (!success)
            throw new CustomException(ExceptionMessages.NoFacilityWithGivenIDRented.toString());
    }

    public void AddOccupiedSpace (String IDofOccupiedSpace){

        IDsOfOccupiedSpaces.add( IDofOccupiedSpace );

    }

    public void StopOccupying (String IDofOccupiedSpace) throws CustomException {

        boolean success = false;

        for (int i = 0; i < IDsOfOccupiedSpaces.size(); i++)
            if (IDofOccupiedSpace.equals(IDsOfOccupiedSpaces.get(i))){

                success = true;
                IDsOfOccupiedSpaces.remove(i);
                i = IDsOfOccupiedSpaces.size();

            }

        if (!success)
            throw new CustomException(ExceptionMessages.NoFacilityWithGivenID.toString());
    }

    public void StopOccupyingWithoutException (String IDofOccupiedSpace) {

        IDsOfOccupiedSpaces.remove(IDofOccupiedSpace);

    }

    public void AddTenantLetter (int DOI, int MOI, int YOI, String SpaceID){

        TenantLetters.add( new TenantLetter(DOI, MOI, YOI, SpaceID));

    }

    public LinkedList<TenantLetter> GetTenantLetters () {

        return TenantLetters;

    }

    public void SetTenantLettersToExpired ( LinkedList<TenantLetter> tenantLetters ) {

        //tc - tenant letter to be changed
        //th - tenant letter here (stored in this Person object)

        for (TenantLetter tc : tenantLetters)
            for (TenantLetter th : TenantLetters)
                if (tc.EqualsThisTenantLetter(th)){
                    th.MoveToArchive();
                    EventLog.AddEvent("Tenant letter for property " + th.GetID() + " expired without payment;");
                    EventLog.AddEvent("Property " + th.GetID() + " was cleared;");
                    Main.StopRentWithoutException( th.GetID() );
                }


    }

    @Override
    public String toString () {

        String TenantLetters;

        if (this.TenantLetters.isEmpty())
            TenantLetters = "-1";
        else{

            TenantLetters = this.TenantLetters.toString();
            TenantLetters = TenantLetters.substring(1);
            TenantLetters = TenantLetters.replace("]",";");
            TenantLetters = TenantLetters.replaceAll(", ", ";\n\t\t\t");
            TenantLetters = TenantLetters.replaceAll("!&", ",");     //replacing the temporarily placed '!'

        }

        String IDsOfRentedPlaces;

        if (this.IDsOfRentedSpaces.isEmpty())
            IDsOfRentedPlaces = "-1";
        else {

            IDsOfRentedPlaces = this.IDsOfRentedSpaces.toString();
            IDsOfRentedPlaces = IDsOfRentedPlaces.replace("[", "* ");
            IDsOfRentedPlaces = IDsOfRentedPlaces.replace("]", ";");
            IDsOfRentedPlaces = IDsOfRentedPlaces.replaceAll(", ", ";\n\t\t\t* ");

        }

        String IDsOfOccupiedPlaces;

        if (this.IDsOfOccupiedSpaces.isEmpty())
            IDsOfOccupiedPlaces = "-1";
        else {

            IDsOfOccupiedPlaces = this.IDsOfOccupiedSpaces.toString();
            IDsOfOccupiedPlaces = IDsOfOccupiedPlaces.replace("[", "* ");
            IDsOfOccupiedPlaces = IDsOfOccupiedPlaces.replace("]", ";");
            IDsOfOccupiedPlaces = IDsOfOccupiedPlaces.replaceAll(", ", ";\n\t\t\t* ");


        }

        return (" " + name + " " + surname + ":" +
                "\n\t\tPESEL: " + PESEL + ";" +
                "\n\t\tDate of birth: " + DOB + "." + MOB + "." + YOB + ";" +
                "\n\t\tAdress: " + adress + ";" +
                "\n\t\t" + (TenantLetters.equals("-1") ? "No tenant letters;" : ( "Tenant letters:" + "\n\t\t\t" + TenantLetters ) ) +
                "\n\t\t" + (IDsOfRentedPlaces.equals("-1") ? "No rented spaces;" : ("IDs of rented spaces:" + "\n\t\t\t" + IDsOfRentedPlaces ) ) +
                "\n\t\t" + (IDsOfOccupiedPlaces.equals("-1") ? "No occupied spaces;" : ("IDs of occupied spaces:" + "\n\t\t\t" + IDsOfOccupiedPlaces ) ) );

    }

    public int[] GetLastPaymentDate (String SpaceID){

        int[] lastPaymentDate = new int[]{-1, 0, 0};

        if (!TenantLetters.isEmpty())
            for (int i = 0; i < TenantLetters.size(); i++)
                if (TenantLetters.get(i).GetID().equals(SpaceID)){
                    lastPaymentDate = TenantLetters.get(i).GetLastPaymentDate();
                    i = TenantLetters.size();
                }

        return lastPaymentDate;

    }

    public void RemoveCoTenantStatus( String ID ){

        for (int i = 0; i < IDsOfOccupiedSpaces.size(); i++)
            if (IDsOfOccupiedSpaces.get(i).equals(ID)){

                IDsOfOccupiedSpaces.remove(i);
                i = IDsOfOccupiedSpaces.size();

            }

    }

}
