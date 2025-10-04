package flatManagement;

import java.util.LinkedList;
import java.util.ArrayList;

public class Subdivision {

    private int ID;
    private String name;
    private LinkedList<BlockOfFlats> BOF;
    private LinkedList<ParkingSpace> parking;

    public Subdivision(int ID, String name, LinkedList<BlockOfFlats> BOF, LinkedList<ParkingSpace> parking){

        this.ID = ID;
        this.name = name;
        this.BOF = BOF;
        this.parking = parking;

    }

    public String GetID (){

        return Integer.toString(ID);

    }

    public String GetName (){

        return name;

    }

    public LinkedList<BlockOfFlats> GetBOF () {

        return BOF;

    }

    public LinkedList<ParkingSpace> GetPS () {

        return parking;

    }

    public int GetNoOfBOF(){

        return BOF.size();

    }

    public void AddParkingSpace (float volume, String ID, int begDay, int begMonth, int begYear, int endDay, int endMonth,
                                 int endYear, boolean currentlyOccupied, String tenantPESEL, LinkedList<Thing> things, float volumeOfThings) {

        //if the parking space number is to be generated automatically:
        if (ID.lastIndexOf('-') == ID.length() - 1)
            ID += parking.size();

        parking.add( new ParkingSpace( volume, ID, begDay, begMonth, begYear, endDay, endMonth, endYear, currentlyOccupied, tenantPESEL, things, volumeOfThings ) );

    }

    public void AddBOF (BlockOfFlats blockOfFlats) {

        BOF.add(blockOfFlats);

    }

    public void AddApartment (float volume, String ID, int begDay, int begMonth, int begYear,
                              int endDay, int endMonth, int endYear, boolean currentlyOccupied, String tenantPESEL ) throws CustomException{

        boolean added = false;

        for (int i = 0; i < BOF.size(); i++)
            if (BOF.get(i).GetBlockNumber().equals(GetBlockNumber(ID))){

                //if the flat number is to be generated automatically:
                if (ID.lastIndexOf('-') == ID.length() - 1)
                    ID += BOF.get(i).GetNoOfApartments();

                added = true;
                BOF.get(i).AddApartment( new Apartment(volume, ID, begDay, begMonth, begYear, endDay, endMonth, endYear, currentlyOccupied, tenantPESEL) );
                i = BOF.size();

            }

        if (!added)
            throw new CustomException(ExceptionMessages.NoBOFWithGivenID.toString());

    }

    public String GetBlockNumber (String ID){

        return ID.substring(ID.indexOf('-') + 1, ID.lastIndexOf('-'));

    }

    public void AddObject (String ParkingSpaceID, String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                              float weight, short doorNumber, String VIN, String bodyType , boolean withBasket, float LastParameter) throws CustomException, TooManyThingsException {

        boolean found = false;

        for (int i = 0; i < parking.size(); i++)
            if ( parking.get(i).GetID().equals(ParkingSpaceID)){

                parking.get(i).AddThing(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, LastParameter);

                found = true;
                i = parking.size();

            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoParkingSpaceWithGivenID.toString());


    }

    public String GetTenantPESEL (String SpaceToRentID) throws CustomException{

        boolean found = false;
        String tenatPESEL = null;

        if (SpaceToRentID.indexOf('-') == SpaceToRentID.lastIndexOf('-')){  //if the rented place is a parking space:

            for (int i = 0 ; i < parking.size(); i++)
                if (parking.get(i).GetID().equals(SpaceToRentID)){

                    found = true;
                    tenatPESEL = parking.get(i).GetTenantPESEL();
                    i = parking.size();

                }

        } else {                                                                //if the rented place is an apartment:

            for (int i = 0; i < BOF.size(); i++)
                if ( BOF.get(i).GetBlockNumber().equals( GetBlockNumber(SpaceToRentID)) ){

                    tenatPESEL = BOF.get(i).GetTenantPESEL(SpaceToRentID);

                    found = true;
                    i = BOF.size();

                }
        }

        if (!found)
            throw new CustomException(ExceptionMessages.NoSpaceToRentWithGivenIDFound.toString());

        return tenatPESEL;

    }

    public void RemoveObject ( String ParkingSpaceID, String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                               float weight, short doorNumber, String VIN, String bodyType , boolean withBasket,
                               boolean skipWithBasket, float LastParameter ) throws CustomException {

        boolean found = false;

        for ( int i = 0; i < parking.size(); i++)
            if ( parking.get(i).GetID().equals( ParkingSpaceID ) ){

                parking.get(i).RemoveThing(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

                found = true;
                i = parking.size();


            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoParkingSpaceWithGivenID.toString());

    }

    public void Rent (String PESEL, String SpaceToRentID, int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear) throws CustomException{

        boolean found = false;

        if (SpaceToRentID.indexOf('-') == SpaceToRentID.lastIndexOf('-')){  //if the rented place is a parking space:

            for (int i = 0 ; i < parking.size(); i++)
                if (parking.get(i).GetID().equals(SpaceToRentID)){

                    parking.get(i).Rent(begDay, begMonth, begYear, endDay, endMonth, endYear, PESEL);

                    found = true;
                    i = parking.size();

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoParkingSpaceWithGivenID.toString());

        } else {                                                                //if the rented place is an apartment:

            for (int i = 0; i < BOF.size(); i++)
                if ( BOF.get(i).GetBlockNumber().equals( GetBlockNumber(SpaceToRentID)) ){

                    BOF.get(i).Rent(PESEL, SpaceToRentID, begDay, begMonth, begYear, endDay, endMonth, endYear);

                    found = true;
                    i = BOF.size();

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoBOFWithGivenID.toString());

        }

    }

    public void Clear ( String SpaceToRentID ) {

        if (SpaceToRentID.indexOf('-') == SpaceToRentID.lastIndexOf('-')){  //if the rented place is a parking space:

            for (int i = 0 ; i < parking.size(); i++)
                if (parking.get(i).GetID().equals(SpaceToRentID)){
                    parking.get(i).Clear();
                    i = parking.size();
                }

        } else {                                                                //if the rented place is an apartment:

            for (int i = 0; i < BOF.size(); i++)
                if ( BOF.get(i).GetBlockNumber().equals( GetBlockNumber(SpaceToRentID)) ){
                    BOF.get(i).Clear( SpaceToRentID );
                    i = BOF.size();
                }

        }

    }

    public void CheckApartmentID (String ID) throws CustomException {

        boolean found = false;

        for (int i = 0; i < BOF.size(); i++)
            if (BOF.get(i).GetBlockNumber().equals( GetBlockNumber(ID) )){

                BOF.get(i).CheckApartmentID(ID);

                found = true;
                i = BOF.size();
            }

        if (!found)
            throw new CustomException(ExceptionMessages.NoBOFWithGivenID.toString());

    }

    public String ExtendRent( String SpaceToRentID, int NoOfMonths) throws CustomException{

        String TenantPESEL = null;
        boolean found = false;

        if (SpaceToRentID.indexOf('-') == SpaceToRentID.lastIndexOf('-')){  //if the rented place is a parking space:

            for (int i = 0 ; i < parking.size(); i++)
                if (parking.get(i).GetID().equals(SpaceToRentID)){

                    TenantPESEL = parking.get(i).ExtendRent(NoOfMonths);
                    i = parking.size();
                    found = true;

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoParkingSpaceWithGivenID.toString());

        } else {                                                                //if the rented place is an apartment:

            for (int i = 0; i < BOF.size(); i++)
                if ( BOF.get(i).GetBlockNumber().equals( GetBlockNumber(SpaceToRentID)) ){

                    TenantPESEL = BOF.get(i).ExtendRent(SpaceToRentID, NoOfMonths);
                    i = BOF.size();
                    found = true;

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoBOFWithGivenID.toString());

        }

        return TenantPESEL;

    }

    public String GetPESELOfTenant ( String SpaceID ) throws CustomException{

        String TenantPESEL = null;
        boolean found = false;

        if (SpaceID.indexOf('-') == SpaceID.lastIndexOf('-')){  //if the rented place is a parking space:

            for (int i = 0 ; i < parking.size(); i++)
                if (parking.get(i).GetID().equals(SpaceID)){

                    TenantPESEL = parking.get(i).GetTenantPESEL();
                    i = parking.size();
                    found = true;

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoParkingSpaceWithGivenID.toString());

        } else {                                                                //if the rented place is an apartment:

            for (int i = 0; i < BOF.size(); i++)
                if ( BOF.get(i).GetBlockNumber().equals( GetBlockNumber(SpaceID)) ){

                    TenantPESEL = BOF.get(i).GetPESELOfTenant( SpaceID);
                    i = BOF.size();
                    found = true;

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoBOFWithGivenID.toString());

        }

        return TenantPESEL;

    }

    public SpaceToRent GetSpaceToRent ( String SpaceID  ) throws CustomException{

        boolean found = false;

        SpaceToRent spaceToRent = null;

        if (SpaceID.indexOf('-') == SpaceID.lastIndexOf('-')){  //if the rented place is a parking space:

            for (int i = 0 ; i < parking.size(); i++)
                if (parking.get(i).GetID().equals(SpaceID)){

                    if (!parking.get(i).CurrentlyOccupied())
                        throw new CustomException(ExceptionMessages.NoOneRentsThisSpace.toString());

                    spaceToRent = parking.get(i);
                    i = parking.size();
                    found = true;

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoParkingSpaceWithGivenID.toString());

        } else {                                                                //if the rented place is an apartment:

            for (int i = 0; i < BOF.size(); i++)
                if ( BOF.get(i).GetBlockNumber().equals( GetBlockNumber(SpaceID)) ){

                    spaceToRent = BOF.get(i).GetSpaceToRent( SpaceID);
                    i = BOF.size();
                    found = true;

                }

            if (!found)
                throw new CustomException(ExceptionMessages.NoBOFWithGivenID.toString());

        }

        return spaceToRent;

    }


}
