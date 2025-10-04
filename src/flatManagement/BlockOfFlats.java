package flatManagement;

import java.util.LinkedList;

public class BlockOfFlats {

    private int blockNumber;
    private String designation;
    private LinkedList<Apartment> apartments;

    public BlockOfFlats(int blockNumber, String designation, LinkedList<Apartment> apartments){

        this.blockNumber = blockNumber;
        this.designation = designation;
        this.apartments = apartments;

    }

    public String GetBlockNumber (){

        return Integer.toString(blockNumber);

    }

    public int GetNoOfApartments(){

        return apartments.size();

    }

    public String GetDesignation () {

        return designation;

    }

    public LinkedList<Apartment> GetApartments (){

        return apartments;

    }

    public void AddApartment (Apartment apartment) {

        apartments.add(apartment);

    }

    public String GetTenantPESEL (String SpaceToRentID) throws CustomException{

        for (Apartment a : apartments)
            if (a.GetID().equals(SpaceToRentID)) {

                return a.GetTenantPESEL();

            }

        throw new CustomException(ExceptionMessages.NoItemsWithGivenCharacteristics.toString());

    }

    public void Clear (String SpaceToRentID) {

        for (int i = 0; i < apartments.size(); i++)
            if (apartments.get(i).GetID().equals( SpaceToRentID )){
                apartments.get(i).Clear();
                i = apartments.size();
            }

    }

    public void Rent (String PESEL, String SpaceToRentID, int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear) throws CustomException {

        boolean found = false;

        for (int i = 0; i < apartments.size(); i++)
            if (apartments.get(i).GetID().equals(SpaceToRentID)){

                apartments.get(i).Rent(begDay, begMonth, begYear, endDay, endMonth, endYear, PESEL);

                found = true;
                i = apartments.size();

            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoApartmentWithGivenID.toString());

    }

    public void CheckApartmentID (String ID) throws CustomException {

        boolean found = false;
        boolean CurrentlyOccupied;

        for (int i = 0; i < apartments.size(); i++)
            if (apartments.get(i).GetID().equals( ID )){

                CurrentlyOccupied = apartments.get(i).CurrentlyOccupied();

                if (!CurrentlyOccupied)
                    throw new CustomException(ExceptionMessages.NoResidentInGivenApartment.toString());

                found = true;
                i = apartments.size();
            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoApartmentWithGivenID.toString());


    }

    public String ExtendRent( String SpaceToRentID, int NoOfMonths) throws CustomException{

        String TenantPESEL = null;
        boolean found = false;

        for (int i = 0; i < apartments.size(); i++)
            if (apartments.get(i).GetID().equals( SpaceToRentID )){

                TenantPESEL = apartments.get(i).ExtendRent(NoOfMonths);
                found = true;
                i = apartments.size();

            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoApartmentWithGivenID.toString());

        return TenantPESEL;

    }

    public String GetPESELOfTenant( String SpaceID ) throws CustomException{

        String TenantPESEL = null;
        boolean found = false;

        for (int i = 0; i < apartments.size(); i++)
            if (apartments.get(i).GetID().equals( SpaceID )){

                TenantPESEL = apartments.get(i).GetTenantPESEL();
                found = true;
                i = apartments.size();

            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoApartmentWithGivenID.toString());

        return TenantPESEL;

    }

    public SpaceToRent GetSpaceToRent( String SpaceID ) throws CustomException{

        SpaceToRent spaceToRent = null;
        boolean found = false;

        for (int i = 0; i < apartments.size(); i++)
            if (apartments.get(i).GetID().equals( SpaceID )){

                if (!apartments.get(i).CurrentlyOccupied())
                    throw new CustomException(ExceptionMessages.NoOneRentsThisSpace.toString());

                spaceToRent = apartments.get(i);
                found = true;
                i = apartments.size();

            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoApartmentWithGivenID.toString());

        return spaceToRent;

    }


}
