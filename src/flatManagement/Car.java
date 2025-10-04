package flatManagement;

public abstract class Car extends Vehicle{

    private short doorNumber;
    private String VIN;

    public Car (float volume, String name, float engineVolume, String engineType, float weight, short doorNumber, String VIN){

        super(volume, name, engineVolume, engineType, weight);

        this.doorNumber = doorNumber;
        this.VIN = VIN;

    }

    public short GetDoorNumber () {

        return doorNumber;

    }

    public String GetVIN () {

        return VIN;

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        boolean skipDoorNumber = doorNumber == 0;
        boolean skipVIN = VIN == null;

        return ( previousParameters &&
                ( this.doorNumber == doorNumber || skipDoorNumber) &&
                ( this.VIN.equals(VIN) || skipVIN) );

    }

}
