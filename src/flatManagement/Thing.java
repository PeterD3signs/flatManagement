package flatManagement;

public class Thing implements SameThing {

    private String name;
    private float volume;

    public Thing (float volume, String name){

        this.name = name;
        this.volume = volume;

    }

    public String GetName () {

        return name;

    }

    public float GetVolume () {

        return volume;

    }

    public String GetThingType () {

        return ThingTypes.Thing.toString();

    }

    @Override
    public boolean IsItTheSame(String ThingType, float thingVolume, String name, float engineVolume, String engineType, float weight,
                            short doorNumber, String VIN, String bodyType, boolean withBasket, boolean skipWithBasket, float LastParameter) {

        boolean skipVolume = thingVolume == 0;
        boolean skipName = name == null;

        return ( (this.name.equals(name) || skipName ) && (volume == thingVolume || skipVolume ) && !(ThingType.equals(ThingTypes.Thing.toString())) );

    }

    public boolean IsItTheSame(float thingVolume, String name){

        boolean skipVolume = thingVolume == 0;
        boolean skipName = name == null;

        return ( (this.name.equals(name) || skipName ) && (volume == thingVolume || skipVolume ) );

    }

    @Override
    public String toString () {

        String NewLine = "\n\t\t\t\t\t";

        return ( "Item -- " + name + ":" + NewLine + "Volume: " + volume + ";" );

    }
}
