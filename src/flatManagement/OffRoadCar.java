package flatManagement;

public class OffRoadCar extends Car{

    private float wadingDepth;

    public OffRoadCar (float volume, String name, float engineVolume, String engineType, float weight, short doorNumber, String VIN, float wadingDepth){

        super(volume, name, engineVolume, engineType, weight, doorNumber, VIN);

        this.wadingDepth = wadingDepth;

    }

    @Override
    public String GetThingType () {

        return ThingTypes.OffRoadCar.toString();

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        boolean skipLastParameter = LastParameter == 0;

        return ( previousParameters && (wadingDepth == LastParameter || skipLastParameter) && ThingType.equals(GetThingType()) );

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

        return ( ThingTypes.OffRoadCar + " -- " + name + ":" +
                NewLine + "Volume: " + volume + ";" +
                NewLine + "Engine volume: " + engineVolume + ";" +
                NewLine + "Engine type: " + engineType + ";" +
                NewLine + "Weight: " + weight + ";" +
                NewLine + "Door number: " + doorNumber + ";" +
                NewLine + "VIN: " + VIN + ";" +
                NewLine + "Wading depth: " + wadingDepth + ";"
        );

    }


}
