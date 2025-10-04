package flatManagement;

import java.util.LinkedList;

public class ParkingSpace implements SpaceToRent{

    private String ID;

    // ID generated in form of:
    // S-N
    // where:
    // S - subdivision ID
    // N - parking space number

    private float volume;

    private int begDay;
    private int begMonth;
    private int begYear;
    private int endDay;
    private int endMonth;
    private int endYear;
    private boolean currentlyOccupied;
    private String tenantPESEL;
    private LinkedList<Thing> things;
    private float volumeOfThings;

    public ParkingSpace (float volume, int subdivisionID, int psNumber){

        ID = GenerateID(subdivisionID, psNumber);
        this.volume = volume;
        Clear();

    }

    public ParkingSpace (float volume, String ID, int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear, boolean currentlyOccupied, String tenantPESEL, LinkedList<Thing> things, float volumeOfThings) {

        //A constructor for remaking the apartment after reading the file.

        this.ID = ID;
        this.volume = volume;
        UpdateBegDate(begDay, begMonth, begYear);
        UpdateEndDate(endDay, endMonth, endYear);
        this.currentlyOccupied = currentlyOccupied;
        this.tenantPESEL = tenantPESEL;
        this.things = things;
        this.volumeOfThings = volumeOfThings;

    }

    public String GetID (){

        return ID;

    }

    public String GetTenantPESEL (){

        return tenantPESEL;

    }

    private String GenerateID(int subdivisionID, int psNumber) {

        return (subdivisionID + "-" + psNumber);

    }

    public String ExtendRent(int NoOfMonths){

        String tenantPESEL = null;

        int[] endDate = DateCalculator.GetEndDate( endDay, endMonth, endYear, NoOfMonths, 0);

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

        this.begDay = begDay;
        this.begMonth = begMonth;
        this.begYear = begYear;

    }

    @Override
    public void UpdateEndDate(int endDay, int endMonth, int endYear) {

        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;

    }

    @Override
    public boolean CurrentlyOccupied() {
        return currentlyOccupied;
    }

    @Override
    public void Clear() {

        UpdateBegDate(0, 0, 0);
        UpdateEndDate(0, 0, 0);
        tenantPESEL = null;
        currentlyOccupied = false;
        things = new LinkedList<>();
        volumeOfThings = 0;

    }

    @Override
    public void Rent(int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear, String tenantPESEL) throws CustomException{

        if (currentlyOccupied)
            throw new CustomException(ExceptionMessages.AlreadyOccupied.toString());

        UpdateBegDate(begDay, begMonth, begYear);
        UpdateEndDate(endDay, endMonth, endYear);
        this.tenantPESEL =tenantPESEL;
        currentlyOccupied = true;

    }

    //THINGS MANAGEMENT:

    public LinkedList<Thing> GetThings () {

        return things;

    }

    public void AddThing (String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                          float weight, short doorNumber, String VIN, String bodyType , boolean withBasket, float LastParameter) throws CustomException, TooManyThingsException{


        if (!currentlyOccupied)
            throw new CustomException(ExceptionMessages.NoOneRentsThisSpace.toString());

        if (volumeOfThings + thingVolume > volume)
            throw new TooManyThingsException();


        if (ThingType.equals(ThingTypes.Thing.toString()))
            things.add(new Thing(thingVolume, name));

        else if (ThingType.equals(ThingTypes.PassengerCar.toString()))
            things.add(new PassengerCar(thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType));

        else if (ThingType.equals(ThingTypes.OffRoadCar.toString()))
            things.add(new OffRoadCar(thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, LastParameter));

        else if (ThingType.equals(ThingTypes.Motorbike.toString()))
            things.add(new Motorbike(thingVolume, name, engineVolume, engineType, weight, withBasket));

        else if (ThingType.equals(ThingTypes.Boat.toString()))
            things.add(new Boat(thingVolume, name, engineVolume, engineType, weight, LastParameter));

        else if (ThingType.equals(ThingTypes.AmphibiousVehicle.toString()))
            things.add(new AmphibiousVehicle(thingVolume, name, engineVolume, engineType, weight, LastParameter));

        else
            throw new CustomException(ExceptionMessages.WrongVehicleType.toString());

        volumeOfThings += thingVolume;

    }


    //remove the first instance of a thing with given characteristics:
    public void RemoveThing (String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                             float weight, short doorNumber, String VIN, String bodyType , boolean withBasket,
                             boolean skipWithBasket, float LastParameter ) throws CustomException {

        //check for skipping all data

        if (things.isEmpty())
            throw new CustomException(ExceptionMessages.NothingToDelete.toString());


        boolean found = false;

        for (int i = 0; i < things.size(); i++){

            if (
                    ThingType == null && things.get(i).IsItTheSame(thingVolume, name) ||
                            ThingType != null && ThingType.equals(ThingTypes.Thing.toString()) && things.get(i).GetThingType().equals(ThingType) && things.get(i).IsItTheSame(thingVolume, name) ||
                            ThingType != null && things.get(i).IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter)
            ) {

                volumeOfThings -= things.get(i).GetVolume();
                things.remove(i);
                found = true;
                i = things.size();

            }

        }


        /*
        The 'if' function from above means:

        Do instructions as long as:
        - case 1: Nothing is specified -> deletes the first thing from list
        - case 2: The type of the Thing is not specified and either volume or name is specified
        - case 3: The type of the Thing is not specified and both volume and name is specified
        - case 4: The type of the Thing is specified, but other arguments can be either specified or unspecified

        In each case, the first item that fits all the provided arguments is deleted.
        */

        if (!found)
            throw new CustomException(ExceptionMessages.NoItemsWithGivenCharacteristics.toString());

    }

    @Override
    public String toString(){

        int PersonIndex = Main.GetPersonIndex(tenantPESEL);

        return (

                "\t\t\t-PS " + ID + ":" +
                        "\n\t\t\t\tvolume: " + volume + ", " + (currentlyOccupied ? ("tenant PESEL: " + tenantPESEL + " (index on people list: " + PersonIndex + ");") : "not rented;") +
                        ( currentlyOccupied ? ("\n\t\t\t\tbeginning of rent: " + begDay + "." + begMonth + "." + begYear + ", end of rent: " + endDay + "." + endMonth + "." + endYear + ";" +
                                "\n\t\t\t\t" + (things.isEmpty() ? "No things stored on this parking space." : "Things stored on this parking space (total volume: " + volumeOfThings + "):")
                        ) : "" )

        );

    }

    public String GetStatus(boolean rentPaid, int[] LastPaymentDate) {

        return ("Parking space " + ID + ", " + (currentlyOccupied ? ("tenant PESEL: " + tenantPESEL + ", rent " +
                (rentPaid ? "payed" :("not payed - last payment date: " + LastPaymentDate[0] + "." + LastPaymentDate[1] + "." + LastPaymentDate[2]))) : "unoccupied") + ";" );

    }

    public int[] GetEndDate (){

        return new int[]{endDay, endMonth, endYear};

    }

}
