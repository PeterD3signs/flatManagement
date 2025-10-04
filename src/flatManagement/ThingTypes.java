package flatManagement;

public enum ThingTypes {

    Thing { @Override public  String toString () { return "Thing"; } },
    PassengerCar { @Override public String toString () { return "Passenger Car"; } },
    OffRoadCar { @Override public String toString () { return "Off-Road Car"; } },
    Motorbike { @Override public String toString () { return "Motorbike"; } },
    AmphibiousVehicle { @Override public String toString () { return "Amphibious Vehicle"; } },
    Boat { @Override public String toString () { return "Boat"; } }

}
