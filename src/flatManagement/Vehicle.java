package flatManagement;

public abstract class Vehicle extends Thing{

    private float engineVolume;
    private String engineType;
    private float weight;

    public Vehicle (float volume, String name, float engineVolume, String engineType, float weight){

        super(volume, name);

        this.engineVolume = engineVolume;
        this.engineType = engineType;
        this.weight = weight;

    }
    public abstract String GetThingType ();

    public float GetEngineVolume (){

        return engineVolume;

    }

    public String GetEngineType (){

        return engineType;

    }

    public float GetWeight (){

        return weight;

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                               short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean previousParameters = super.IsItTheSame(ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

        boolean skipEngineVolume = engineVolume == 0;
        boolean skipEngineType = engineType == null;
        boolean skipWeight = weight == 0;

        return ( previousParameters &&
                ( this.engineVolume == engineVolume || skipEngineVolume) &&
                ( this.engineType.equals(engineType) || skipEngineType) &&
                (this.weight == weight || skipWeight) );

    }

}
