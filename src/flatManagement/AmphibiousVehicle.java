package flatManagement;

public class AmphibiousVehicle extends Vehicle{

    private float armourThickness;

    public AmphibiousVehicle (float volume, String name, float engineVolume, String engineType, float weight, float armourThickness){

        super(volume, name, engineVolume, engineType, weight);

        this.armourThickness = armourThickness;

    }

    @Override
    public String GetThingType () {

        return ThingTypes.AmphibiousVehicle.toString();

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        boolean skipLastParameter = LastParameter == 0;

        return ( previousParameters && (armourThickness == LastParameter || skipLastParameter) && ThingType.equals(GetThingType()) );

    }

    @Override
    public String toString () {

        String NewLine = "\n\t\t\t\t\t";
        String name = super.GetName();
        float volume = super.GetVolume();
        float engineVolume = super.GetEngineVolume();
        String engineType = super.GetEngineType();
        float weight = super.GetWeight();


        return ( ThingTypes.AmphibiousVehicle + " -- " + name + ":" +
                NewLine + "Volume: " + volume + ";" +
                NewLine + "Engine volume: " + engineVolume + ";" +
                NewLine + "Engine type: " + engineType + ";" +
                NewLine + "Weight: " + weight + ";" +
                NewLine + "Armour thickness: " + armourThickness + ";"
                );

    }

}
