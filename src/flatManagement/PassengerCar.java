package flatManagement;

public class PassengerCar extends Car{

    private String bodyType;

    public PassengerCar (float volume, String name, float engineVolume, String engineType, float weight, short doorNumber, String VIN, String bodyType){

        super(volume, name, engineVolume, engineType, weight, doorNumber, VIN);

        this.bodyType = bodyType;

    }

    @Override
    public String GetThingType() {

        return ThingTypes.PassengerCar.toString();

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        boolean skipBodyType = bodyType == null;

        return ( previousParameters && (this.bodyType.equals(bodyType) || skipBodyType) && ThingType.equals(GetThingType()) );

    }

    @Override
    public String toString () {

        String NewLine = "\n\t\t\t\t\t";
        String name = super.GetName();
        float volume = super.GetVolume();
        float engineVolume = super.GetEngineVolume();
        String engineType = super.GetEngineType();
        float weight = super.GetWeight();
        short doorNumber = super.GetDoorNumber();
        String VIN = super.GetVIN();

        return ( ThingTypes.PassengerCar + " -- " + name + ":" +
                NewLine + "Volume: " + volume + ";" +
                NewLine + "Engine volume: " + engineVolume + ";" +
                NewLine + "Engine type: " + engineType + ";" +
                NewLine + "Weight: " + weight + ";" +
                NewLine + "Door number: " + doorNumber + ";" +
                NewLine + "VIN: " + VIN + ";" +
                NewLine + "Body type: " + bodyType + ";"
        );

    }





}
