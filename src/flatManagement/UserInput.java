package flatManagement;

import java.util.*;

public class UserInput {

    static Scanner scan = new Scanner(System.in);

    public static String GetPESEL (){

        System.out.println("\nPlease provide the PESEL number:");

        return GetStringInput();

    }

    public static void StartStopTime (boolean TimeRunning) {

        System.out.println("\nRight now the time simulation is " + (TimeRunning ? "on" : "stopped") + ". Do you wish to change that state? ('y'/'n' - yes/no)");

        char answer = GetCharInput(0);

        if (answer == 'y')

            if (TimeRunning){
                Main.StopTime();
                System.out.println("\nTime simulation stopped.");
            } else {
                Main.SimTime();
                System.out.println("\nTime simulation started.");
            }


        else if (answer != 'n'){

            System.out.println("\nIncorrect character. Please input again.");
            StartStopTime(TimeRunning);

        }

    }

    public static void EndProgramme() {

        System.out.println("\nThe programme is about to shut down. All unsaved data will be lost. Do you want to proceed? ('y'/'n' - yes/no)");

        char answer = GetCharInput(0);

        if (answer == 'y')
            Main.EndProgramme();
        else if (answer != 'n'){
            System.out.println("\nIncorrect character. Please input again.");
            EndProgramme();
        }

    }

    public static String GetCommand (){

        Print.PrintMenu();

        String answer = GetStringInput();

        char FrCh = answer.charAt(0);
        char ScCh = answer.charAt(1);

        switch (FrCh){      //Checking for correct input and gathering data about automatic creation of objects

            case 'a' -> {
                switch (ScCh){

                    case 's', 'b', 'a', 'p' -> answer = ShouldCreateFacilityAutomatically( ScCh );
                    case 'v' -> answer = WhichVehicleToAdd();

                }
            }
            case 'c' -> {

                if (ScCh == 'p')
                    answer = ShouldCreatePersonAutomatically();

            }
            case 'p' -> {
                switch (ScCh){

                    case 's' -> answer = PrintOneOrAll();
                    case 't' -> answer = HowManyTenantLetters();

                }
            }


        }

        return answer;

    }

    public static void Rent (int[] CurrentDate){

        System.out.println("\nPlease input the PESEL of the person who is about to rent a space:");

        String PESEL = GetStringInput();

        String SpaceToRentID = InquireForID('b', "Please input the ID of the space that is to be rented:",
                "Incorrect ID format.");

        System.out.println("\nPlease input for how many months should the property be rented:");

        int NoOfMonths = GetIntInput();

        int[] endDate = DateCalculator.GetEndDate(CurrentDate[0], CurrentDate[1], CurrentDate[2], NoOfMonths, 0);

        try {
            Main.Rent(PESEL, SpaceToRentID, CurrentDate[0], CurrentDate[1], CurrentDate[2], endDate[0], endDate[1], endDate[2], true);
        } catch (CustomException ex){

            if (ex.getMessage().equals(ExceptionMessages.NoPersonWithGivenPESEL.toString()))
                System.out.println("\nNo person with provided PESEL found. No space was rented.");
            else if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                System.out.println("\nNo subdivision with provided ID was found. No spaces were rented.");
            else if (ex.getMessage().equals(ExceptionMessages.NoBOFWithGivenID.toString()))
                System.out.println("\nNo block of flats with provided ID was found. No spaces were rented.");
            else if (ex.getMessage().equals(ExceptionMessages.NoParkingSpaceWithGivenID.toString()))
                System.out.println("\nNo parking space with provided ID was found. Nothing was rented.");
            else if (ex.getMessage().equals(ExceptionMessages.NoApartmentWithGivenID.toString()))
                System.out.println("\nNo apartment with provided ID was found. Nothing was rented.");
            else if (ex.getMessage().equals(ExceptionMessages.AlreadyOccupied.toString()))
                System.out.println("\nIndicated space already has a tenant and cannot be rented.");
            else if (ex.getMessage().equals(ExceptionMessages.NoFlatsRentedOnGivenSubdivision.toString()))
                System.out.println("\nA parking space cannot be rented if a given tenant does not have an apartment rented on the same subdivision.");
            else if (ex.getMessage().equals(ExceptionMessages.Already5SpacesRented.toString()))
                System.out.println("\nA given person already has 5 spaces rented.");
            else
                System.out.println("\nUnexpected error occurred. No space was rented.");

        } catch (ProblematicTenantException ptex){

            System.out.println( "\n" + ptex.getMessage() );

        }

    }

    public static void AddCoTenant (boolean WithMessage){

        System.out.println("\nPlease input the PESEL of the soon to be added co-tenant:");

        String CoTenantPESEL = GetStringInput();

        String ApartmentID = InquireForID('a', "Please input the ID of the apartment that the co-tenant will occupy:",
                "Incorrect apartment ID format.");

        boolean correct = true;

        try{
            Main.AddCoTenant(CoTenantPESEL, ApartmentID);
        } catch (CustomException ex){

            correct = false;

            if (ex.getMessage().equals(ExceptionMessages.NoPersonWithGivenPESEL.toString()))
                System.out.println("\nNo person with provided PESEL found. Co-tenant was not added.");
            else if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                System.out.println("\nNo subdivision with provided ID was found. Co-tenant was not added.");
            else if (ex.getMessage().equals(ExceptionMessages.NoBOFWithGivenID.toString()))
                System.out.println("\nNo block of flats with provided ID was found. Co-tenant was not added.");
            else if (ex.getMessage().equals(ExceptionMessages.NoApartmentWithGivenID.toString()))
                System.out.println("\nNo apartment with provided ID was found. Co-tenant was not added.");
            else if (ex.getMessage().equals(ExceptionMessages.NoResidentInGivenApartment.toString()))
                System.out.println("\nA co-tenant cannot be added to a place that is not yet rented.");
            else
                System.out.println("\nUnexpected error occurred. Co-tenant was not added.");

        }

        if (correct && WithMessage){
            System.out.println("\nA co-tenant with PESEL " + CoTenantPESEL + " was successfully added to an apartment with ID: " + ApartmentID + ".");
            EventLog.AddEvent("A co-tenant with PESEl " + CoTenantPESEL + " was added to an apartment with ID: " + ApartmentID + ";");
        }

    }

    public static void RmCoTenant (boolean WithMessage){

        System.out.println("\nPlease input the PESEL of the soon to be deleted co-tenant:");

        String CoTenantPESEL = GetStringInput();

        String ApartmentID = InquireForID('a', "Please input the ID of the apartment that the co-tenant occupies:",
                "Incorrect apartment ID format.");

        boolean correct = true;

        try{
            Main.RemoveCoTenant(CoTenantPESEL, ApartmentID);
        } catch (CustomException ex){

            correct = false;

            if (ex.getMessage().equals(ExceptionMessages.NoPersonWithGivenPESEL.toString()))
                System.out.println("\nNo person with provided PESEL found. Co-tenant was not removed.");
            else if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                System.out.println("\nNo subdivision with provided ID was found. Co-tenant was not removed.");
            else if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                System.out.println("\nNo block of flats with provided ID was found. Co-tenant was not removed.");
            else if (ex.getMessage().equals(ExceptionMessages.NoApartmentWithGivenID.toString()))
                System.out.println("\nNo apartment with provided ID was found. Co-tenant was not removed.");
            else if (ex.getMessage().equals(ExceptionMessages.NoResidentInGivenApartment.toString()))
                System.out.println("\nA co-tenant cannot be removed from a place that is not yet rented.");
            else if (ex.getMessage().equals(ExceptionMessages.NoFacilityWithGivenIDRented.toString()))
                System.out.println("\nThe co-tenant does not appear to occupy the indicated apartment.");
            else
                System.out.println("\nUnexpected error occurred. Co-tenant was not removed.");


        }

        if (correct && WithMessage){
            System.out.println("\nA co-tenant with PESEL " + CoTenantPESEL + " was successfully removed from an apartment with ID: " + ApartmentID + ".");
            EventLog.AddEvent("A co-tenant with PESEL " + CoTenantPESEL + " was removed from an apartment with ID: " + ApartmentID + ";");
        }

    }

    public static void ExtendRent (boolean WithMessage){

        String SpaceToRentID = InquireForID('b', "Please input the ID of the space which rent is to be extended:",
                "Incorrect ID format.");

        System.out.println("\nPlease input for how many months should the rent be extended:");

        int NoOfMonths = GetIntInput();

        boolean correct = true;

        try {
            Main.ExtendRent(SpaceToRentID, NoOfMonths);
        } catch (CustomException ex){

            correct = false;

            if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                System.out.println("\nNo subdivision with provided ID was found. Rent was not extended.");
            else if (ex.getMessage().equals(ExceptionMessages.NoBOFWithGivenID.toString()))
                System.out.println("\nNo block of flats with provided ID was found. Rent was not extended.");
            else if (ex.getMessage().equals(ExceptionMessages.NoParkingSpaceWithGivenID.toString()))
                System.out.println("\nNo parking space with provided ID was found. Rent was not extended.");
            else if (ex.getMessage().equals(ExceptionMessages.NoApartmentWithGivenID.toString()))
                System.out.println("\nNo apartment with provided ID was found. Rent was not extended.");
            else if (ex.getMessage().equals(ExceptionMessages.NoOneRentsThisSpace.toString()))
                System.out.println("\nRent cannot be extended as no one rents this space at the moment.");
            else
                System.out.println("\nUnexpected error occurred. Rent was not extended");

        }

        if (correct && WithMessage){
            System.out.println("\nRent on a space with ID: " + SpaceToRentID + " was successfully extended.");
            EventLog.AddEvent("Rent on a space with ID: " + SpaceToRentID + " was extended;");
        }

    }

    public static void CancelRent (boolean WithMessage){

        String SpaceToRentID = InquireForID('b', "Please input the ID of the space which rent is to be terminated:",
                "Incorrect ID format.");

        boolean correct = true;

        try {
            Main.StopRent(SpaceToRentID);
        } catch (CustomException ex){

            correct = false;

            if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                System.out.println("\nNo subdivision with provided ID was found. Rent was not cancelled.");
            else if (ex.getMessage().equals(ExceptionMessages.NoBOFWithGivenID.toString()))
                System.out.println("\nNo block of flats with provided ID was found. Rent was not cancelled.");
            else if (ex.getMessage().equals(ExceptionMessages.NoParkingSpaceWithGivenID.toString()))
                System.out.println("\nNo parking space with provided ID was found. Rent was not cancelled.");
            else if (ex.getMessage().equals(ExceptionMessages.NoApartmentWithGivenID.toString()))
                System.out.println("\nNo apartment with provided ID was found. Rent was not cancelled.");
            else if (ex.getMessage().equals(ExceptionMessages.IndicatedSpaceIsNotRented.toString()))
                System.out.println("\nRent cannot be cancelled as no one rents this space at the moment.");
            else if (ex.getMessage().equals(ExceptionMessages.NoFacilityWithGivenIDRented.toString()))
                //this particular exception should not arise as long as the programme is working properly.
                System.out.println("\nRent cannot be cancelled as no one rents this space at the moment.");
            else
                System.out.println("\nUnexpected error occurred. Rent was not cancelled");

        }

        if (correct && WithMessage){
            System.out.println("\nRent on a space with ID: " + SpaceToRentID + " was successfully cancelled.");
            EventLog.AddEvent("Rent on a space with ID: " + SpaceToRentID + " was cancelled;");
        }

    }


    private static String InquireForID (char WhichID, String AskingMessage, String ErrorMessage){

        //if WhichID == 'p' -> checking for parking space id
        //if WhichID == 'a' -> checking for apartment id
        //if WhichID == any other char ('b' as default) -> checking for both

        boolean correct = false;

        String ID = "0-0-0";

        while (!correct){

            System.out.println("\n" + AskingMessage);

            ID = GetStringInput();

            switch (WhichID){
                case 'p' -> {
                    if (ID.replaceAll("-", "").length() == ID.length() - 1)
                        correct = true;
                }
                case 'a' -> {
                    if (ID.replaceAll("-", "").length() == ID.length() - 2)
                        correct = true;
                }
                default -> {
                    if (ID.replaceAll("-", "").length() == ID.length() - 1 || ID.replaceAll("-", "").length() == ID.length() - 2)
                        correct = true;
                }
            }

            if (!correct)
                System.out.println("\n" + ErrorMessage);

        }

        return ID;

    }

    private static String PrintOneOrAll(){

        System.out.println("\nWould you like to print the status of a given property, or all properties? ('o' / 'a' - one / all)");

        char answer = GetCharInput(0);

        if (answer == 'o')
            return "pso";
        else if (answer == 'a')
            return "psa";
        else{
            System.out.println("\nIncorrect character. Please input again.");
            return PrintOneOrAll();
        }

    }

    public static void RemoveObject (){

        String thingType = null;
        float volume = 0;
        String name = null;
        float engineVolume = 0;
        String engineType = null;
        float weight = 0;
        short DoorNumber = 0;
        String VIN = null;
        String BodyType = null;
        float wadingDepth = 0;
        boolean withBasket = false;
        boolean checkForBasket = false;
        float buoyancy = 0;
        float armourThickness = 0;
        boolean continueSearch = true;
        float LastParameter = 0;

        //checking by which characteristic the user wants to search for objects to remove:

        System.out.println("""

                        You can remove an object with any number of parameters. In each case, the first instance of an object will be deleted.
                        Please be weary of the fact, that by selecting a field that is exclusive to a certain vehicle, you limit your search to that class only.
                        
                        Please select by which characteristics you want to search for the (soon to be deleted) object, by answering in a given format:
                        'X,X,X,X,X,X,X,X,X,X,X,X,X;' where each X is either a 'y' or 'n', depending on whether you want to search by this characteristic.

                        Those are the characteristic you can search by:
                        1. Thing type
                        2. Volume
                        3. Name
                        4. Engine volume
                        5. Engine type
                        6. Weight
                        7. DoorNumber
                        8. VIN
                        9. BodyType
                        10. Wading depth
                        11. Motorbike with basket
                        12. Buoyancy
                        13. Armour thickness

                        Example: 'y,y,y,y,n,n,n,n,n,n,n,y,n;' will look for an object that has a specified type, volume, name, engine volume and buoyancy.
                        Only boats could be deleted with given search characteristics, because of the selected buoyancy parameter."""
                );

        System.out.print("\n\nNow input your string of characters: ");

        boolean correct = false;
        String answer = "";

        while (!correct){

            answer = GetStringInput();

            if (!answer.contains(";")) {      //if the answer does not contain ";"
                correct = true;
                System.out.print("\n\nAnswer must contain ';'. Please input your string of characters again: ");
            }

            if (answer.indexOf(";") != answer.length() - 1 && !correct) {     //if ";" is not the last character
                correct = true;
                System.out.print("\n\nAnswer must end with ';'. Please input your string of characters again: ");
            }

            if (answer.replaceAll(";","").length() != answer.length() - 1 && !correct) {      //if there are more than one instance of ";"
                correct = true;
                System.out.print("\n\nAnswer must contain only one ';' at the end of the sequence.\nPlease input your string of characters again: ");
            }

            if (answer.replaceAll(",","").length() != answer.length() - 12 && !correct){    //if the number of comas is wrong
                correct = true;
                System.out.print("\n\nThe number of comas seems to be incorrect. Please input your string of characters again: ");
            }

            if (answer.length() != 26 && !correct) {      //if the length of the answer is wrong
                correct = true;
                System.out.print("\n\nThe length of the answer must equal 26. Please input your string of characters again: ");
            }

            if (!correct){                              //checking for illegal characters
                for (int i = 0; i < 26; i++){

                    if (i % 2 == 0 && (answer.charAt(i) != 'y' && answer.charAt(i) != 'n')){

                        System.out.print("\n\nFound an illegal character at index " + i + ". You can only use 'y', 'n', ',' and ';' characters." +
                                "\nPlease input your string of characters again: ");
                        correct = true;
                        i = 26;

                    } else if (i % 2 == 1 && (answer.charAt(i) != ',' && answer.charAt(i) != ';')){

                        System.out.print("\n\nFound an illegal character at index " + i + ". You can only use 'y', 'n', ',' and ';' characters." +
                                "\nPlease input your string of characters again: ");
                        correct = true;
                        i = 26;

                    }

                }
            }

            correct = !correct;

        }

        System.out.println("\n");

        //inquiring about the characteristics

        boolean[] askAbout = new boolean[13];   //should the programme ask about this characteristic? in given order:
        //thingType, volume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, wadingDepth, withBasket, buoyancy, armourThickness

        for (int i = 0; i < 13; i++)
            askAbout[i] = (answer.charAt( 2*i ) == 'y');


        //searching for overlapping choices:
        if (askAbout[6] || askAbout[7])    //if car was indicated...
            if (askAbout[10] || askAbout[11] || askAbout[12])   //...and any other type was indicated at the same time
                continueSearch = false;

        if (askAbout[8])                   //if a passenger car was indicated...
            if (askAbout[9] || askAbout[10] || askAbout[11] || askAbout[12])    //...and any other type was indicated at the same time
                continueSearch = false;

        if (askAbout[9])                   //if an off-road car was indicated...
            if (askAbout[8] || askAbout[10] || askAbout[11] || askAbout[12])    //...and any other type was indicated at the same time
                continueSearch = false;

        if (askAbout[10])                   //if a motorcycle was indicated...
            if (askAbout[11] || askAbout[12])    //...and any other type was indicated at the same time
                continueSearch = false;

        if (askAbout[11] && askAbout[12])   //if a boat and an amphibious vehicle was indicated at the same time
            continueSearch = false;




        if (askAbout[0] && continueSearch){   //thingType

            System.out.print("Please provide the thing type. Input 'i', 'p', 'o', 'm', 'a', 'b' to search for" +
                        "\n an item, a passenger car, an off-road car, a motorcycle, an amphibious vehicle or a boat respectively: ");

            correct = false;

            while (!correct){

                char SelectedType = GetCharInput(0);

                if (SelectedType == 'i') {

                    thingType = ThingTypes.Thing.toString();

                    for (int i = 2; i <13; i++)
                        if (askAbout[i]) {
                            continueSearch = false;
                            i = 13;
                        }

                }else if (SelectedType == 'p') {

                    thingType = ThingTypes.PassengerCar.toString();
                    if (askAbout[9] || askAbout[10] || askAbout[11] || askAbout[12])
                        continueSearch = false;

                }else if (SelectedType == 'o') {

                    thingType = ThingTypes.OffRoadCar.toString();
                    if (askAbout[8] || askAbout[10] || askAbout[11] || askAbout[12])
                        continueSearch = false;

                }else if (SelectedType == 'm') {

                    thingType = ThingTypes.Motorbike.toString();
                    if (askAbout[6] || askAbout[7] || askAbout[8] || askAbout[9] || askAbout[11] || askAbout[12])
                        continueSearch = false;

                }else if (SelectedType == 'a') {

                    thingType = ThingTypes.AmphibiousVehicle.toString();
                    if (askAbout[6] || askAbout[7] || askAbout[8] || askAbout[9] || askAbout[10] || askAbout[11])
                        continueSearch = false;

                }else if (SelectedType == 'b') {

                    thingType = ThingTypes.Boat.toString();
                    if (askAbout[6] || askAbout[7] || askAbout[8] || askAbout[9] || askAbout[10] || askAbout[12])
                        continueSearch = false;

                }else{
                    correct = true;
                    System.out.println("\nIncorrect character was provided. Please input it again:");
                }

                correct = !correct;

                System.out.println();

            }

        }

        if (askAbout[1] && continueSearch){     //volume

            volume = InquireForVolume(true, false);

        }

        if (askAbout[2] && continueSearch){     //name

            System.out.println("\nPlease input the name of the object:");

            name = GetStringInput();

            System.out.println();

        }

        if (askAbout[3] && continueSearch){     //engineVolume

            correct = false;

            while (!correct){

                System.out.println("\nPlease input the engine volume using '.' as the dividing character:");

                try{
                    engineVolume = GetFloatInput();
                } catch (CustomException ex){

                    correct = true;
                    System.out.print("\nFormat of the provided data was incorrect.");

                }

                if (engineVolume < 0){

                    correct = true;
                    System.out.print("\nEngine volume cannot be negative.");

                } else if (engineVolume == 0){

                    correct = true;
                    System.out.print("\nEngine volume cannot be zero.");

                }

                correct = !correct;

            }

            System.out.println();

        }

        if (askAbout[4] && continueSearch){     //engineType

            System.out.println("\nPlease input the engine type:");

            engineType = GetStringInput();

            System.out.println();

        }

        if (askAbout[5] && continueSearch){     //weight

            correct = false;

            while (!correct){

                System.out.println("\nPlease input the weight using '.' as the dividing character:");

                try{
                    weight = GetFloatInput();
                } catch (CustomException ex){

                    correct = true;
                    System.out.print("\nFormat of the provided data was incorrect.");

                }

                if (weight < 0){

                    correct = true;
                    System.out.print("\nWeight cannot be negative.");

                } else if (weight == 0){

                    correct = true;
                    System.out.print("\nWeight cannot be zero.");

                }

                correct = !correct;

            }

            System.out.println();

        }

        if (askAbout[6] && continueSearch){     //doorNumber

            correct = false;

            while (!correct){

                int IntegerDoorNumber;

                System.out.println("\nPlease input the door number:");

                IntegerDoorNumber = GetIntInput();

                if (IntegerDoorNumber < 0)
                    System.out.print("\nNo vehicle can have a negative number of doors.");
                else if (IntegerDoorNumber > 10)
                    System.out.print("\nThe provided number of doors seems to be too high.");
                else {
                    correct = true;
                    DoorNumber = (short)IntegerDoorNumber;
                }

            }

            System.out.println();

        }

        if (askAbout[7] && continueSearch){     //VIN

            System.out.println("\nPlease input the VIN:");

            VIN = GetStringInput();

            System.out.println();

        }

        if (askAbout[8] && continueSearch){     //bodyTYpe

            System.out.println("\nPlease input the body type:");

            BodyType = GetStringInput();

            System.out.println();

        }

        if (askAbout[9] && continueSearch){     //wadingDepth

            correct = false;

            while (!correct){

                System.out.println("\nPlease input the wading depth using '.' as the dividing character:");

                try{
                    wadingDepth = GetFloatInput();
                } catch (CustomException ex){

                    correct = true;
                    System.out.print("\nFormat of the provided data was incorrect.");

                }

                if (wadingDepth < 0){

                    correct = true;
                    System.out.print("\nWading depth cannot be negative.");

                } else if (wadingDepth > 5){

                    correct = true;
                    System.out.print("\nWading depth cannot be that high");

                }

                correct = !correct;

            }

            System.out.println();

            LastParameter = wadingDepth;

        }

        if (askAbout[10] && continueSearch){        //motorbikeBasket

            checkForBasket = true;

            System.out.print("Does the motorbike that you look for come with a basket? ('y' / 'n' - yes / no)");

            correct = false;

            while (!correct){

                char SelectedType = GetCharInput(0);

                if (SelectedType == 'y')
                    withBasket = true;
                else if (SelectedType != 'n') {
                    correct = true;
                    System.out.println("\nIncorrect character provided. Please input whether the motorcycle comes with a basket: ('y' / 'n' - yes / no)");
                }

                correct = !correct;

                System.out.println();

            }

        }

        if (askAbout[11] && continueSearch){        //buoyancy

            correct = false;

            while (!correct){

                System.out.println("\nPlease input the buoyancy '.' as the dividing character:");

                try{
                    buoyancy = GetFloatInput();
                } catch (CustomException ex){

                    correct = true;
                    System.out.print("\nFormat of the provided data was incorrect.");

                }

                if (buoyancy < 0){

                    correct = true;
                    System.out.print("\nBuoyancy cannot be negative.");

                } else if (buoyancy == 0){

                    correct = true;
                    System.out.print("\nbuoyancy cannot be zero.");

                }

                correct = !correct;

            }

            System.out.println();

            LastParameter = buoyancy;

        }

        if (askAbout[12] && continueSearch){        //armourThickness

            correct = false;

            while (!correct){

                System.out.println("\nPlease input the armour thickness '.' as the dividing character:");

                try{
                    armourThickness = GetFloatInput();
                } catch (CustomException ex){

                    correct = true;
                    System.out.print("\nFormat of the provided data was incorrect.");

                }

                if (armourThickness < 0){

                    correct = true;
                    System.out.print("\nArmour thickness cannot be negative.");

                } else if (armourThickness == 0){

                    correct = true;
                    System.out.print("\nArmour thickness cannot be zero.");

                }

                correct = !correct;

            }

            System.out.println();

            LastParameter = armourThickness;

        }

        if (!continueSearch)
            System.out.println("Provided input is incoherent. No objects were deleted.");
        else{

            char option = 'l';
            //'l' - loop
            //'s' - stop
            //'a' - input again

            while (option == 'l'){

                String ID = InquireForID('p', "Please input the parking space ID number:", "Incorrect ID format");

                try{
                    Main.RemoveObject(ID, thingType, volume, name, engineVolume, engineType, weight, DoorNumber, VIN, BodyType, withBasket, !checkForBasket, LastParameter);
                } catch (CustomException ex){

                    if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                        System.out.println("\nNo subdivision with given ID was found.");
                    else if (ex.getMessage().equals(ExceptionMessages.NoParkingSpaceWithGivenID.toString()))
                        System.out.println("\nNo parking space with given ID was found.");
                    else if (ex.getMessage().equals(ExceptionMessages.NothingToDelete.toString())){
                        System.out.println("\nThere are no objects on a given parking space.");
                        option = 's';
                    } else if (ex.getMessage().equals(ExceptionMessages.NoItemsWithGivenCharacteristics.toString())) {
                        System.out.println("\nNo objects with given characteristics found. Nothing was deleted.");
                        option = 's';
                    }else
                        System.out.println("\nUnexpected error occurred.");

                    correct = (option == 's');

                    while (!correct){

                        System.out.println();
                        System.out.println("Do you want to input the parking space ID again, or not pursuit deleting any objects altogether?" +
                                "\nReply with 'i' to input the ID again, ot with 's' to stop adding the item:");

                        option = GetCharInput(0);

                        if (option == 'i'){

                            System.out.println("\nIncorrect ID.");
                            correct = true;
                            option = 'a';

                        } else if (option == 's')
                            correct = true;
                        else
                            System.out.println("\nIncorrect character.");

                    }

                }

                //here option is either equal to 's' - stop, 'l' - loop (in this case there was no Exception), or 'a' - again

                if (option == 'l'){
                    option = 's';
                    EventLog.AddEvent("Object was deleted from the parking space with ID number: " + ID + ";");
                    System.out.println("\nObject was successfully deleted from the parking space with ID number: " + ID + ".");
                }
                else if (option == 'a')
                    option = 'l';

            }

        }

    }

    public static void RemovePerson (){

        System.out.println( "\nPleas input the PESEL of the person that you want to remove:");

        String PESEL = GetStringInput();
        boolean success = true;

        try{
            Main.RemovePerson(PESEL);
        } catch (CustomException ex){
            System.out.println("\nNo person with given PESEL found");
            success = false;
        }

        if (success){
            System.out.println("\nPerson with PESEL number " + PESEL + " was removed.");
            EventLog.AddEvent("Person with PESEL number " + PESEL + " was removed;");
        }


    }

    private static String HowManyTenantLetters() {

        System.out.println( "Would you like to print the tenant letters of a single person, or all tenant letters?" + "('s'/'a' - single/all)");

        char answer = GetCharInput(0);

        if (answer == 's')
            return "pto";  //tenant letters of one person
        else if (answer == 'a')
            return "pta";  //all tenant letters
        else{
            System.out.println("\nIncorrect character. Please input again.");
            return HowManyTenantLetters();
        }

    }

    public static void AddPersonManually() {

        System.out.println( "Input the name:");
        String name = GetStringInput();
        System.out.println( "\nInput the surname:");
        String surname = GetStringInput();
        System.out.println( "\nInput PESEL:");
        String PESEL = GetStringInput();
        System.out.println( "\nInput the address:");
        String address = GetStringInput();

        int DOB = 1, MOB = 1, YOB = 2000;
        boolean correct = false;

        while (!correct){

            System.out.println( "\ninput the day of birth:");
            DOB = GetIntInput();
            System.out.println( "\ninput the month of birth:");
            MOB = GetIntInput();
            System.out.println( "\ninput the year of birth:");
            YOB = GetIntInput();

            correct = DateCalculator.CheckDate( DOB, MOB, YOB, 1900, 2023 );

            if (!correct)
                System.out.println("\nDate of birth is in an incorrect format. Please input the date again.");

        }

        correct = false;

        while (!correct){

            try {
                Main.AddPersonManually(name, surname, PESEL, address, DOB, MOB, YOB);
            } catch (CustomException ex){
                Print.PrintMessage("\nA person with provided PESEL already exists. Please input the PESEL again:");
                PESEL = GetStringInput();
                correct = true;
            }

            correct = !correct;

        }

    }

    private static String ShouldCreateFacilityAutomatically ( char ScCh ){

        System.out.println( "Would you like to create a whole " +
                (ScCh == 's' ? "Subdivision" : (ScCh == 'b' ? "Block Of Flats" : (ScCh == 'a' ? "Apartment" : "Parking Space")))
                        + " automatically? ('y'/'n' - yes/no)");

        char answer = GetCharInput(0);

        if (answer == 'y')
            return ("a" + ScCh + "a");  //create automatically
        else if (answer == 'n')
            return ("a" + ScCh + "m");  //create manually
        else{
            System.out.println("\nIncorrect character. Please input again.");
            return ShouldCreateFacilityAutomatically( ScCh );
        }

    }

    public static void AddItem (){

        float volume = InquireForVolume(true, true);

        System.out.println("\nPlease input the name of the item:");
        String name = GetStringInput();

        PassObjectToParkingSpace(ThingTypes.Thing.toString(), volume, name, (float)0, "", (float)0, (short)0, "", "", false, (float)0);

    }

    private static void PassObjectToParkingSpace (String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                                        float weight, short doorNumber, String VIN, String bodyType , boolean withBasket, float LastParameter){

        char option = 'l';
        //'l' - loop
        //'s' - stop
        //'a' - input again

        while (option == 'l'){

            String ID = InquireForID('p', "Please input the parking space ID number:", "Incorrect ID format");

            try{
                Main.AddObject(ID, ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, LastParameter);
            } catch (CustomException | TooManyThingsException ex){

                if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                    System.out.println("\nNo subdivision with given ID was found.");
                else if (ex.getMessage().equals(ExceptionMessages.NoParkingSpaceWithGivenID.toString()))
                    System.out.println("\nNo parking space with given ID was found.");
                else if (ex.getMessage().equals(ExceptionMessages.NoOneRentsThisSpace.toString())) {
                    System.out.println("\nCannot insert objects into parking spaces that are not rented");
                    option = 's';
                }else if (ex.getMessage().equals(ExceptionMessages.TooManyThingsException.toString())){
                    System.out.println("\nRemove some old items to insert a new item.");
                    option = 's';
                } else
                    System.out.println("\nUnexpected error occurred.");

                boolean correct = (option == 's');

                while (!correct){

                    System.out.println();
                    System.out.println("Do you want to input the parking space ID again, or not pursuit adding the new object altogether?" +
                            "\nReply with 'i' to input the ID again, ot with 's' to stop adding the item:");

                    option = GetCharInput(0);

                    if (option == 'i'){

                        System.out.println("\nIncorrect ID.");
                        correct = true;
                        option = 'a';

                    } else if (option == 's')
                        correct = true;
                    else
                        System.out.println("\nIncorrect character.");

                }

            }

            //here option is either equal to 's' - stop, 'l' - loop (in this case there was not Exception), or 'a' - again

            if (option == 'l'){
                option = 's';
                EventLog.AddEvent("Object \"" + name + "\" was added to the parking space with ID number: " + ID + ";");
                System.out.println("\nObject \"" + name + "\" was successfully added to the parking space with ID number: " + ID + ".");
            }
            else if (option == 'a')
                option = 'l';

        }

    }

    public static void AddVehicle (String thingType){

        float volume = InquireForVolume(true, true);
        String name;
        float engineVolume = 1;
        String engineType;
        float weight = 1;
        short doorNumber = 1;
        String VIN = "";
        String bodyType = "";
        float lastParameter = 1;
        boolean withBasket = false;


        System.out.println("\nPlease input the name of the vehicle:");
        name = GetStringInput();

        System.out.println("\nPlease input the engine type:");
        engineType = GetStringInput();

        System.out.println("\nPlease input the engine volume, using '.' as the dividing character:");

        boolean correct = false;

        while (!correct){

            try{
                engineVolume = GetFloatInput();
            } catch (CustomException ex) {

                System.out.println("\nEngine volume was passed in an incorrect format.\nPlease input it again, using '.' as the dividing character:");
                correct = true;

            }

            if (engineVolume < 0){

                System.out.println("\nEngine volume cannot have a negative value.\nPlease input it again, using '.' as the dividing character:");
                correct = true;

            } else if (engineVolume == 0) {

                System.out.println("\nEngine volume cannot be a zero.\nPlease input it again, using '.' as the dividing character:");
                correct = true;

            }

            correct = !correct;

        }

        System.out.println("\nPlease input the weight of the vehicle, using '.' as the dividing character:");

        correct = false;

        while (!correct){

            try{
                weight = GetFloatInput();
            } catch (CustomException ex) {

                System.out.println("\nWeight was passed in an incorrect format.\nPlease input it again, using '.' as the dividing character:");
                correct = true;

            }

            if (weight < 0){

                System.out.println("\nWeight cannot have a negative value.\nPlease input it again, using '.' as the dividing character:");
                correct = true;

            } else if (weight == 0) {

                System.out.println("\nWeight cannot be a zero.\nPlease input it again, using '.' as the dividing character:");
                correct = true;

            }

            correct = !correct;

        }

        //completing the data:
        if (thingType.equals(ThingTypes.PassengerCar.toString()) || thingType.equals(ThingTypes.OffRoadCar.toString())){

            System.out.println("\nPlease input the number of doors:");
            correct = false;

            while (!correct){

                int IntegerDoorNumber = GetIntInput();

                if (IntegerDoorNumber < 0 || IntegerDoorNumber > 10)
                    System.out.println("\nProvided number of doors seems to be incorrect. Please input the number of doors again:");
                else{
                    correct = true;
                    doorNumber = (short)IntegerDoorNumber;
                }

            }

            System.out.println("\nPlease input the VIN number:");
            VIN = GetStringInput();

            if (thingType.equals(ThingTypes.PassengerCar.toString())){      //passenger car

                System.out.println("\nPlease input the body type:");
                bodyType = GetStringInput();

            } else {                                                        //off-road car

                System.out.println("\nPlease input the wading depth:");

                correct = false;

                while (!correct){

                    try{
                        lastParameter = GetFloatInput();
                    } catch (CustomException ex) {

                        System.out.println("\nWading depth was passed in an incorrect format.\nPlease input it again, using '.' as the dividing character:");
                        correct = true;

                    }

                    if (lastParameter < 0){

                        System.out.println("\nWading depth cannot be negative.\nPlease input it again, using '.' as the dividing character:");
                        correct = true;

                    } else if (lastParameter > 5) {

                        System.out.println("\nWading depth cannot be this big.\nPlease input it again, using '.' as the dividing character:");
                        correct = true;

                    }

                    correct = !correct;

                }

            }

        } else if (thingType.equals(ThingTypes.Motorbike.toString())) {

            char answerChar;
            correct = false;

            System.out.println("\nDoes this motorbike come with a basket? ('y'/'n' - yes/no)");

            while (!correct){

                answerChar = GetCharInput(0);

                if (answerChar == 'y')
                    withBasket = true;
                else if (answerChar != 'n'){
                    System.out.println("\nIncorrect character. Please input again: ('y' - comes with the basket/ 'n' - comes without it)");
                    correct = true;
                }

                correct = !correct;
            }

        } else if (thingType.equals(ThingTypes.AmphibiousVehicle.toString())) {

            System.out.println("\nPlease input the armour thickness:");

            correct = false;

            while (!correct){

                try{
                    lastParameter = GetFloatInput();
                } catch (CustomException ex) {

                    System.out.println("\nArmour thickness was passed in an incorrect format.\nPlease input it again, using '.' as the dividing character:");
                    correct = true;

                }

                if (lastParameter < 0){

                    System.out.println("\nArmour thickness cannot be negative.\nPlease input it again, using '.' as the dividing character:");
                    correct = true;

                } else if (lastParameter == 0) {

                    System.out.println("\nArmour thickness cannot be zero.\nPlease input it again, using '.' as the dividing character:");
                    correct = true;

                }

                correct = !correct;

            }

        } else if (thingType.equals(ThingTypes.Boat.toString())) {

            System.out.println("\nPlease input the buoyancy:");

            correct = false;

            while (!correct){

                try{
                    lastParameter = GetFloatInput();
                } catch (CustomException ex) {

                    System.out.println("\nBuoyancy was passed in an incorrect format.\nPlease input it again, using '.' as the dividing character:");
                    correct = true;

                }

                if (lastParameter < 0){

                    System.out.println("\nBuoyancy cannot be negative.\nPlease input it again, using '.' as the dividing character:");
                    correct = true;

                } else if (lastParameter == 0) {

                    System.out.println("\nBuoyancy cannot be zero.\nPlease input it again, using '.' as the dividing character:");
                    correct = true;

                }

                correct = !correct;

            }


        }

        PassObjectToParkingSpace(thingType, volume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, lastParameter);

    }

    private static float InquireForVolume (boolean object, boolean flat){

        System.out.println("\nWould you like to input the volume straight away" +
                "\nor by passing the length, width and height of the " +
                (object ? "object" : (flat ? "apartment" : "parking space")) + "?" +
                "\n('s' - straight away / 'b' - by parameters)");

        char answer = GetCharInput(0);
        float volume = 1;

        if (answer == 's'){

            System.out.println("\nPlease input the volume, using '.' as the dividing character:");

            try{
                volume = GetFloatInput();
            } catch (CustomException ex){
                System.out.print("\nSomething went wrong. You will need to pass the data again.");
                return InquireForVolume(object, flat);
            }

            return volume;

        } else if (answer == 'b'){

            boolean correct = false;
            System.out.println("\nPlease input the width:");

            while (!correct){

                try{
                    volume = GetFloatInput();
                } catch (CustomException ex){
                    System.out.print("\nSomething went wrong. Please input the value again:");
                    correct = true;
                }

                if (volume < 0){
                    System.out.println("\nNegative width is not allowed. Please input the value again:");
                    correct = true;
                } else if (volume == 0) {
                    System.out.println("\nZero is not allowed as the width parameter. Please input the value again:");
                    correct = true;
                }

                correct = !correct;

            }

            correct = false;
            System.out.println("\nPlease input the length:");

            while (!correct){

                try{
                    volume *= GetFloatInput();
                } catch (CustomException ex){
                    System.out.print("\nSomething went wrong. Please input the value again:");
                    correct = true;
                }

                if (volume < 0){
                    System.out.println("\nNegative length is not allowed. Please input the value again:");
                    correct = true;
                } else if (volume == 0) {
                    System.out.println("\nZero is not allowed as the length parameter. Please input the value again:");
                    correct = true;
                }

                correct = !correct;
            }

            correct = false;
            System.out.println("\nPlease input the height:");

            while (!correct){

                try{
                    volume *= GetFloatInput();
                } catch (CustomException ex){
                    System.out.print("\nSomething went wrong. Please input the value again:");
                    correct = true;
                }

                if (volume < 0){
                    System.out.println("\nNegative height is not allowed. Please input the value again:");
                    correct = true;
                } else if (volume == 0) {
                    System.out.println("\nZero is not allowed as the width parameter. Please input the height again:");
                    correct = true;
                }

                correct = !correct;

            }

            return volume;

        } else {

            System.out.println("\nIncorrect character. Please input again.");
            return InquireForVolume(object, flat);

        }

    }

    public static void ClearEventLog (){

        System.out.println( "Are you sure that you want to clear the event log? ('y'/'n' - yes/no)");

        char answer = GetCharInput(0);

        if (answer == 'y')
            EventLog.Clear();
        else if (answer != 'n'){
            System.out.println("\nIncorrect character. Please input again.");
            ClearEventLog();
        }

    }

    private static String ShouldCreatePersonAutomatically (){

        System.out.println( "Would you like to create a person automatically? ('y'/'n' - yes/no)");

        char answer = GetCharInput(0);

        if (answer == 'y')
            return "cpa";  //create automatically
        else if (answer == 'n')
            return "cpm";  //create manually
        else{
            System.out.println("\nIncorrect character. Please input again.");
            return ShouldCreatePersonAutomatically();
        }

    }

    private static String WhichVehicleToAdd (){

        System.out.println( "Which vehicle would you like to add?");
        System.out.println("('p' - " + ThingTypes.PassengerCar + ", 'o' - " + ThingTypes.OffRoadCar + ", 'm' - " +
                ThingTypes.Motorbike + ", 'a' - " + ThingTypes.AmphibiousVehicle + ", 'b' - " + ThingTypes.Boat + ")");

        char answer = GetCharInput(0);

        if (answer != 'p' && answer != 'o' && answer != 'm' && answer != 'a' && answer !='b') {
            System.out.println("\nIncorrect character. Please input again.");
            return WhichVehicleToAdd();
        } else
            return ("av" + answer);

    }

    public static void AutoCreatePS (boolean showCompletionMessage, float volume ){

        System.out.println("\nPlease input the subdivision ID (number) where the parking place should be created:");

        int SubdivisionID = GetIntInput();
        boolean correct = true;

        if (SubdivisionID < 0){

            System.out.println("\nSubdivision ID cannot be negative. Operation was unsuccessful.");

        } else {

            if (volume == 0)
                volume = 8 + (int)(Math.random() * (50)) + (float)Math.random();

            System.out.println();

            try {
                Main.AddParkingSpace(SubdivisionID, volume, null, 0, 0, 0, 0, 0, 0, false, null, new LinkedList<>(), 0);
            } catch (CustomException ex){

                System.out.println("\nNo subdivisions with given ID were found. Operation was unsuccessful");
                correct = false;

            }

            if (correct && showCompletionMessage){
                System.out.println("\nNew parking space was added to the subdivision with ID number: " + SubdivisionID + ".");
                EventLog.AddEvent("New parking space was generated at a subdivision with ID: " + SubdivisionID + ";");
            }

        }


    }

    public static void ManCreatePS (){

        float volume = InquireForVolume(false, false);

        AutoCreatePS(true, volume);

    }

    public static void AutoCreateApt (boolean showCompletionMessage, float volume ){

        System.out.println("\nPlease input the subdivision ID (number) where the apartment should be created:");

        int SubdivisionID = GetIntInput();

        System.out.println("\n\nPlease input the block of flats number where the apartment should be created:");

        int BOFnumber = GetIntInput();

        boolean correct = true;

        if (SubdivisionID < 0)
            System.out.println("\nSubdivision ID cannot be negative. Operation was unsuccessful.");
        else if (BOFnumber < 0)
            System.out.println("\nBlock of flats number cannot be negative. Operation was unsuccessful.");
        else {

            if (volume == 0)
                volume = 50 + (int)(Math.random() * (5000)) + (float)Math.random();

            System.out.println();

            try {
                Main.AddApartment(SubdivisionID, BOFnumber, volume, null, 0, 0, 0, 0, 0, 0, false, null);
            } catch (CustomException ex){

                if(ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                    System.out.println("\nNo subdivisions with given ID were found. Operation was unsuccessful");
                else
                    System.out.println("\nNo block of flats with given number was found. Operation was unsuccessful");

                correct = false;

            }

            if (correct && showCompletionMessage){
                System.out.println("\nNew apartment was added to the block of flats with number: " + BOFnumber + " on a subdivision with ID number: " + SubdivisionID + ".");
                EventLog.AddEvent("New apartment was generated in a block of flats with number: " + BOFnumber + " on a subdivision with ID: " + SubdivisionID + ";");
            }

        }

    }

    public static void ManCreateApt (){

        float volume = InquireForVolume(false, true);

        AutoCreateApt(true, volume);

    }

    public static void AutoCreateBOF (boolean showCompletionMessage, String designation){

        System.out.println("\nPlease input the subdivision ID (number) where the block of flats should be created:");

        int SubdivisionID = GetIntInput();
        boolean correct = true;

        if (SubdivisionID < 0){

            System.out.println("\nSubdivision ID cannot be negative. Operation was unsuccessful.");

        } else {

            LinkedList<Apartment> apartments = new LinkedList<>();

            System.out.println();

            try {
                Main.AddBlockOfFlats(SubdivisionID, -1, designation, apartments);
            } catch (CustomException ex){

                System.out.println("\nNo subdivisions with given ID were found. Operation was unsuccessful");
                correct = false;

            }

            if (correct && showCompletionMessage){
                System.out.println("\nNew block of flats was added to the subdivision with ID number: " + SubdivisionID + ".");
                EventLog.AddEvent("New block of flats was generated at a subdivision with ID: " + SubdivisionID + ";");
            }

        }

    }

    public static void ManCreateBOF (){

        System.out.println("\nPlease input the desired block of flats designation:");

        String designation = GetStringInput();

        System.out.println();

        AutoCreateBOF(true, designation);

    }

    public static void GenSetOfSub (){

        boolean correct = false;

        System.out.println("""

                Do you want to generate all of the subdivisions with preset configuration,
                or do you prefer to choose how many block of flats, apartments and parking spaces
                there will be per a subdivisions ? ('p' / 'o' - preset configuration / own choosing)""");

        while (!correct){

            char SelectedType = GetCharInput(0);

            System.out.println();

            if (SelectedType == 'p'){

                correct = true;
                Main.GenSetOfSub(3, 5, 4, 10, 50, 200, 10, 30);

            } else if (SelectedType == 'o') {

                correct = true;
                int MaxSize = 5000;

                int[] MaxAndMinNumbers = new int[8];

                String[] Messages = new String[]{
                        "MIN-imum number of subdivisions",
                        "MAX-imum number of subdivisions",
                        "MIN-imum number of blocks of flats per subdivision",
                        "MAX-imum number of blocks of flats per subdivision",
                        "MIN-imum number of parking spaces per subdivision",
                        "MAX-imum number of parking spaces per subdivision",
                        "MIN-imum number of apartments per each block of flats",
                        "MAX-imum number of apartments per each block of flats"
                };

                for (int i = 0; i < 8; i++){

                    System.out.println("Please input the " + Messages[i] + " that are to be generated:");

                    MaxAndMinNumbers[i] = GetIntInput();

                    System.out.println("\n");

                }

                for (int i = 0; i < 8; i++){

                    if (MaxAndMinNumbers[i] < 0){
                        System.out.println("Provided numbers cannot be negative.");
                        i = 8;
                        correct = false;
                    } else if (MaxAndMinNumbers[i] == 0){
                        System.out.println("Provided numbers cannot be equal to zero.");
                        i = 8;
                        correct = false;
                    } else if (MaxAndMinNumbers[i] > MaxSize){
                        System.out.println("Provided numbers cannot be that big. The limit is set at " + MaxSize + ".");
                        i = 8;
                        correct = false;
                    } else if (i % 2 == 1){
                        if (MaxAndMinNumbers[i - 1] > MaxAndMinNumbers[i]){
                            System.out.println("Minimum cannot be bigger than maximum.");
                            i = 8;
                            correct = false;
                        }
                    }

                }

                if (correct){
                    Main.GenSetOfSub(MaxAndMinNumbers[0], MaxAndMinNumbers[1], MaxAndMinNumbers[2], MaxAndMinNumbers[3], MaxAndMinNumbers[4],
                            MaxAndMinNumbers[5], MaxAndMinNumbers[6], MaxAndMinNumbers[7]);
                }

            } else
                System.out.println("Incorrect character provided. Please input your answer again:");

        }

    }

    public static void ManCreateSub (){

        System.out.println("\nPlease input the desired subdivision name:");

        String name = GetStringInput();

        System.out.println();

        Main.AddSubdivision(-1, name, new LinkedList<>(), new LinkedList<>(), true);

    }

    public static void PrintStatusOfOneSpace (boolean withMessage, boolean FromMain, String SpaceID){

        if (FromMain)
            SpaceID = InquireForID('b', "Please input the ID of the space which status you want to check:", "Incorrect ID format.");

        boolean correct = true;
        SpaceToRent spaceToRent = null;

        try{

            spaceToRent = Main.GetSpaceToRent(SpaceID);

        } catch (CustomException ex){

            correct = false;

            if (FromMain){

                if (ex.getMessage().equals(ExceptionMessages.NoSubdivisionWithGivenID.toString()))
                    System.out.println("\nNo subdivision with provided ID was found.");
                else if (ex.getMessage().equals(ExceptionMessages.NoBOFWithGivenID.toString()))
                    System.out.println("\nNo block of flats with provided ID was found.");
                else if (ex.getMessage().equals(ExceptionMessages.NoApartmentWithGivenID.toString()))
                    System.out.println("\nNo apartment with provided ID was found.");
                else if (ex.getMessage().equals(ExceptionMessages.NoParkingSpaceWithGivenID.toString()))
                    System.out.println("\nNo parking space with provided ID was found.");
                else if (ex.getMessage().equals(ExceptionMessages.NoOneRentsThisSpace.toString())) {

                    if (withMessage){
                        System.out.println("\nThis space is not rented.");
                    }

                } else
                    System.out.println("\nUnexpected error occurred. Data will not be printed");

            }

        }

        if (spaceToRent == null)
            correct = false;

        if (correct){

            int[] LastPaymentDate = Main.GetLastPaymentDate( spaceToRent.GetTenantPESEL(), SpaceID );
            boolean rentPayed = (LastPaymentDate[0] == -1);

            Print.PrintOnePropertyStatus(spaceToRent, rentPayed, LastPaymentDate);

            if (withMessage)
                EventLog.AddEvent("Printed the status of space with ID: " + SpaceID + ";");

        }

    }

    public static void PrintStatusOfAllSpaces (boolean withMessage, LinkedList<Subdivision> subdivisions){

        for (Subdivision s : subdivisions) {

            LinkedList<BlockOfFlats> blockOfFlats = s.GetBOF();
            LinkedList<ParkingSpace> parkingSpaces = s.GetPS();

            for (BlockOfFlats b : blockOfFlats) {

                LinkedList<Apartment> apartments = b.GetApartments();

                for (Apartment a : apartments) PrintStatusOfOneSpace(false, false, a.GetID());

            }

            System.out.println();
            System.out.println();

            for (ParkingSpace p : parkingSpaces) PrintStatusOfOneSpace(false, false, p.GetID());

        }

        if (withMessage)
            EventLog.AddEvent("Printed the status of all spaces");

    }

    public static void WriteToFile (){

        boolean writeSuccessful = true;

        try{
            ReadAndWrite.writeToFile();
        } catch (Exception ex){

            boolean correct = true;
            writeSuccessful = false;

            try {
                ReadAndWrite.ClearFile();
            } catch (Exception exc) {

                correct = false;
                System.out.println("\nAn exception with message: " + exc.getMessage() + " was encountered.");
                System.out.println("Writing to file was unsuccessful.");
                System.out.println("Full exception:");
                exc.printStackTrace(System.out);

            }

            if (correct){
                System.out.println("\nAn exception with message: " + ex.getMessage() + " was encountered.");
                System.out.println("Writing to file was unsuccessful, resulting in an empty file.");
                System.out.println("Full exception:");
                ex.printStackTrace(System.out);
            }


        }

        if (writeSuccessful){
            System.out.println("\nSuccessfully written to file.");
            EventLog.AddEvent("Saved memory content in file;");
        }

    }

    public static String GetStringInput (){

        return scan.nextLine();

    }

    public static char GetCharInput (int Index){

        char ReadData = scan.next().charAt(Index);
        scan.nextLine();    //consume trailing characters

        return ReadData;

    }

    public static int GetIntInput (){

        int ReadData = scan.nextInt();
        scan.nextLine();    //consume trailing characters

        return ReadData;

    }

    public static float GetFloatInput () throws CustomException{

        float toRet;

        String floatString = GetStringInput();

        try{
            toRet = Float.parseFloat(floatString);
        } catch (Exception ex){
            throw new CustomException(ExceptionMessages.WrongFloatFormat.toString());
        }

        return toRet;

    }

    public static void ClearMemory (){

        System.out.println("\nYou are about to clear the programme memory. All unsaved data will be lost. Do you want to proceed? ('y'/'n' - yes/no)");

        char answer = GetCharInput(0);

        if (answer == 'y')
            Main.ClearMemory();
        else if (answer != 'n'){
            System.out.println("\nIncorrect character. Please input again.");
            ClearMemory();
        }


    }

}
