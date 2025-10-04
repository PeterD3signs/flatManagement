package flatManagement;

public enum ExceptionMessages {

    // quick alternative to creating 20 separate exceptions
    // (messages that the user should see vary even within one exception)

    AlreadyOccupied,
    NoSpaceToRentWithGivenIDFound,
    NoApartmentWithGivenID,
    NoResidentInGivenApartment,
    WrongVehicleType,
    NothingToDelete,
    NoItemsWithGivenCharacteristics,
    NoBOFWithGivenID,
    NoParkingSpaceWithGivenID,
    NoSubdivisionWithGivenID,
    PersonWithThisPESELAlreadyOnList,
    NoPersonWithGivenPESEL,
    IndicatedSpaceIsNotRented,
    NoOneRentsThisSpace,
    Already5SpacesRented,
    NoFlatsRentedOnGivenSubdivision,
    NoFacilityWithGivenIDRented,
    NoFacilityWithGivenID,
    WrongFloatFormat,
    IncorrectDataOrder,
    NoReadableDataInFIle { @Override public String toString(){ return "Data encountered in file could not be read."; } },
    TooManyThingsException { @Override public String toString(){ return "Remove some old items to insert a new item"; } }

}
