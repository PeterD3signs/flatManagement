package flatManagement;

public class Motorbike extends Vehicle{

    private boolean withBasket;

    public Motorbike (float volume, String name, float engineVolume, String engineType, float weight, boolean withBasket){

        super(volume, name, engineVolume, engineType, weight);

        this.withBasket = withBasket;

    }

    @Override
    public String GetThingType () {

        return ThingTypes.Motorbike.toString();

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        return ( previousParameters && (this.withBasket == withBasket || skipWithBasket) && ThingType.equals(GetThingType()) );

    }

    @Override
    public String toString () {

        String NewLine = "\n\t\t\t\t\t";
        String name = super.GetName();
        float volume = super.GetVolume();
        float engineVolume = super.GetEngineVolume();
        String engineType = super.GetEngineType();
        float weight = super.GetWeight();


        return ( ThingTypes.Motorbike + " -- " + name + ":" +
                NewLine + "Volume: " + volume + ";" +
                NewLine + "Engine volume: " + engineVolume + ";" +
                NewLine + "Engine type: " + engineType + ";" +
                NewLine + "Weight: " + weight + ";" +
                NewLine + "Does it come with a basket: " + (withBasket ? "yes" : "no" ) + ";"
        );

    }

}
