package flatManagement;

public class Boat extends Vehicle{

    private float buoyancy;

    public Boat (float volume, String name, float engineVolume, String engineType, float weight, float buoyancy){

        super(volume, name, engineVolume, engineType, weight);

        this.buoyancy = buoyancy;

    }

    @Override
    public String GetThingType () {

        return ThingTypes.Boat.toString();

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        boolean skipLastParameter = LastParameter == 0;

        return ( previousParameters && (buoyancy == LastParameter || skipLastParameter) && ThingType.equals(GetThingType()) );

    }
    @Override
    public String toString () {

        String NewLine = "\n\t\t\t\t\t";
        String name = super.GetName();
        float volume = super.GetVolume();
        float engineVolume = super.GetEngineVolume();
        String engineType = super.GetEngineType();
        float weight = super.GetWeight();


        return ( ThingTypes.Boat + " -- " + name + ":" +
                NewLine + "Volume: " + volume + ";" +
                NewLine + "Engine volume: " + engineVolume + ";" +
                NewLine + "Engine type: " + engineType + ";" +
                NewLine + "Weight: " + weight + ";" +
                NewLine + "Buoyancy: " + buoyancy + ";"
        );

    }


}
