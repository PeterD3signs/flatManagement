package flatManagement;

import java.util.LinkedList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static LinkedList<Subdivision> subdivisions = new LinkedList<>();
    private static LinkedList<Person> people = new LinkedList<>();
    private static int[] PresentDate = new int[]{1,1,2000};
    static TimeSimThread timeSim = new TimeSimThread();
    static CheckRentThread rentCheck = new CheckRentThread();
    private static boolean Continue = true;
    private static boolean TimeRunning = false;

    public static void main(String[] args) {

        ReadFileOnStart();     //This method calls for the creation of a set of people and subdivisions in accordance with the task;

        while (Continue) {

            String answer = UserInput.GetCommand();
            LaunchAppropriateMethod( answer );

        }

    }

    private static void ReadFileOnStart (){

        char ChAnswer;

        ChAnswer = Print.PrintWelcomeMessage();

        if (ChAnswer == 'i'){   //import from file

            SetDataTo();

        } else if (ChAnswer == 'r' ){       //Here is where a set of people and subdivisions is created in accordance with the task

            /*
            Generating 15 people, including:
            - 5 only renting spaces
            - 2 renting and occupying at the same time
            - 2 only renting but with tenant letters
            - 1 renting, occupying and with tenant letters
            - 4 only occupying
            - 1 that only rents, but already has 5 properties

            + 11 apartments
            + 6 parking spaces
            */

            int[] endDate;
            LinkedList<BlockOfFlats> BOF = new LinkedList<>();
            LinkedList<ParkingSpace> PS = new LinkedList<>();
            LinkedList<Apartment> apartments0 = new LinkedList<>();
            LinkedList<Apartment> apartments1 = new LinkedList<>();
            LinkedList<String> IDsOfRentedSpaces;
            LinkedList<String> IDsOfOccupiedSpaces;
            LinkedList<TenantLetter> tenantLetters;
            LinkedList<Thing> things;


            //Adding people:
            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-0-0");
            IDsOfRentedSpaces.add("0-0");
            people.add( new Person("Klara", "Rybner", "00000000000", "Przyjemna 9, Zieleniec", 22, 10, 1999, new LinkedList<>(), IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-0-1");
            IDsOfRentedSpaces.add("0-1");
            people.add( new Person("Adam", "Nowak", "11111111111", "Rakowiecka 13, Warszawa", 11, 11, 1991, new LinkedList<>(), IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-0-2");
            people.add( new Person("Bartosz", "Kowalski", "22222222222", "Marszalkowska 8, Warszawa", 21, 10, 1993, new LinkedList<>(), IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-0-3");
            people.add( new Person("Maciej", "Orlowski", "33333333333", "Koszykowa 3, Warszawa", 1, 9, 1987, new LinkedList<>(), IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-0-4");
            people.add( new Person("Piotr", "Szczypak", "44444444444", "Kasztanowa 7, Krakow", 3, 8, 2000, new LinkedList<>(), IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-1-0");
            IDsOfOccupiedSpaces = new LinkedList<>();
            IDsOfOccupiedSpaces.add("0-1-4");
            IDsOfOccupiedSpaces.add("0-1-3");
            people.add( new Person("Tymon", "Oska", "55555555555", "Terlecka 311, Krakow", 17, 7, 1988, new LinkedList<>(), IDsOfRentedSpaces, IDsOfOccupiedSpaces ));

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-1-1");
            IDsOfOccupiedSpaces = new LinkedList<>();
            IDsOfOccupiedSpaces.add("0-1-2");
            IDsOfOccupiedSpaces.add("0-0-1");
            people.add( new Person("Szymon", "Raczkiewicz", "66666666666", "Mala 13, Poznan", 28, 6, 1975, new LinkedList<>(), IDsOfRentedSpaces, IDsOfOccupiedSpaces ));

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-1-2");
            tenantLetters = new LinkedList<>();
            tenantLetters.add( new TenantLetter(30, 12, 1999, "0-1-2"));
            people.add( new Person("Filip", "Kaczynski", "77777777777", "Znajoma 5, Poznan", 7, 5, 1949, tenantLetters, IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-1-3");
            tenantLetters = new LinkedList<>();
            tenantLetters.add( new TenantLetter(7, 12, 1999, "0-1-3"));
            people.add( new Person("Edyta", "Maklowicz", "88888888888", "Taka 1, Radom", 12, 4, 1965, tenantLetters, IDsOfRentedSpaces, new LinkedList<>()) );

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-1-4");
            IDsOfOccupiedSpaces = new LinkedList<>();
            IDsOfOccupiedSpaces.add("0-0-2");
            tenantLetters = new LinkedList<>();
            tenantLetters.add( new TenantLetter(20, 12, 1999, "0-1-4"));
            people.add( new Person("Wanda", "Kubica", "99999999999", "Owaka 10, Radom", 13, 3, 1980, tenantLetters, IDsOfRentedSpaces, IDsOfOccupiedSpaces ));

            IDsOfOccupiedSpaces = new LinkedList<>();
            IDsOfOccupiedSpaces.add("0-1-5");
            people.add( new Person("Anna", "Weslowoska", "10987654321", "Lotnikow 176, Lodz", 29, 2, 1996, new LinkedList<>(), new LinkedList<>(), IDsOfOccupiedSpaces ));
            people.add( new Person("Maria", "Pazdzioch", "11109876543", "Jedenasta 11, Lodz", 5, 1, 1981, new LinkedList<>(), new LinkedList<>(), IDsOfOccupiedSpaces ));
            people.add( new Person("Natalia", "Kosa", "12111098765", "Ukryta 28, Sosonowiec", 4, 12, 1966, new LinkedList<>(), new LinkedList<>(), IDsOfOccupiedSpaces ));
            people.add( new Person("Gabriela", "Kwasniewska", "13121110987", "Widoczna Golym Okiem 44, Sosnowiec", 10, 11, 2002, new LinkedList<>(), new LinkedList<>(), IDsOfOccupiedSpaces ));

            IDsOfRentedSpaces = new LinkedList<>();
            IDsOfRentedSpaces.add("0-1-5");
            IDsOfRentedSpaces.add("0-2");
            IDsOfRentedSpaces.add("0-3");
            IDsOfRentedSpaces.add("0-4");
            IDsOfRentedSpaces.add("0-5");
            people.add( new Person("Sara", "Sledz", "14131211109", "Tysiaca polskich znakow 1000, Zieleniec", 13, 11, 1989, new LinkedList<>(), IDsOfRentedSpaces, new LinkedList<>()) );



            //Adding apartments in block 0:
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 2 );
            apartments0.add( new Apartment(430, "0-0-0", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "00000000000" ) );

            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 1, 0 );
            apartments0.add( new Apartment(245, "0-0-1", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "11111111111" ) );

            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 10, 0 );
            apartments0.add( new Apartment(315, "0-0-2", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "22222222222" ) );

            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 3, 1 );
            apartments0.add( new Apartment(200, "0-0-3", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "33333333333" ) );

            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 7, 2 );
            apartments0.add( new Apartment(280, "0-0-4", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "44444444444" ) );



            //Adding apartments in block 1:
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 10, 3 );
            apartments1.add( new Apartment(580, "0-1-0", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "55555555555" ) );

            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 0, 3 );
            apartments1.add( new Apartment(716, "0-1-1", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "66666666666" ) );

            apartments1.add( new Apartment(881, "0-1-2", 1, 1, 1999, 29, 12, 1999, true, "77777777777" ) );

            apartments1.add( new Apartment(576, "0-1-3", 1, 1, 1999, 6, 12, 1999, true, "88888888888" ) );

            apartments1.add( new Apartment(222, "0-1-4", 1, 1, 1999, 19, 12, 1999, true, "99999999999" ) );

            apartments1.add( new Apartment(1256, "0-1-5", 1, 1, 1999, 19, 12, 2017, true, "14131211109" ) );




            //Adding blocks of flats:
            BOF.add( new BlockOfFlats(0, "Kazimierz", apartments0) );
            BOF.add( new BlockOfFlats(1, "Tobiasz", apartments1) );



            //Adding Parking Spaces:
            things = new LinkedList<>();
            things.add( new Thing(1, "Stary telewizor"));
            things.add( new Thing(2.5f, "Nowa lodowka"));
            things.add( new Thing(2.5f, "Stara lodowka"));
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 3, 3 );
            PS.add( new ParkingSpace(50, "0-0", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "00000000000", things, 6 ) );

            things = new LinkedList<>();
            things.add( new Motorbike(4, "Superszybkismigaczladowy 8911.7" , 0.125f, "Silnik dwusuwowy", 25, false));
            things.add( new Thing(24, "Kartonowe pudlo o wymiarach 4x3x2"));
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 2, 0 );
            PS.add( new ParkingSpace(28.1f, "0-1", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "11111111111", things, 28) );



            things = new LinkedList<>();
            things.add( new Boat(40, "Superszybkismigaczwodny 8911.6", 6, "V8", 4000, 3000));
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 1, 2 );
            PS.add( new ParkingSpace(45, "0-2", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "14131211109", things, 40) );

            things = new LinkedList<>();
            things.add( new AmphibiousVehicle(36, "Superszybkismigaczwodnoladowy 8911.5", 10, "V12", 6000, 30));
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 1, 2 );
            PS.add( new ParkingSpace(40, "0-3", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "14131211109", things, 36) );

            things = new LinkedList<>();
            things.add( new PassengerCar(20, "Superautko Gold", 2, "i4", 1300, (short)3, "0000", "hatch"));
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 1, 2 );
            PS.add( new ParkingSpace(35, "0-4", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "14131211109", things, 20) );

            things = new LinkedList<>();
            things.add( new OffRoadCar(23, "Tayato Land Surfer", 4, "v6", 1500, (short)4, "0001", 1.1f));
            endDate = DateCalculator.GetEndDate(PresentDate[0], PresentDate[1], PresentDate[2], 1, 2 );
            PS.add( new ParkingSpace(30, "0-5", PresentDate[0], PresentDate[1], PresentDate[2], endDate[0], endDate[1], endDate[2], true, "14131211109", things, 23) );


            //Adding the subdivision:
            subdivisions.add( new Subdivision(0, "Grazyna", BOF, PS) );

            Print.PrintMessage("Data successfully read.");
            EventLog.AddEvent("Data read from the prefilled set;");

        }

    }

    private static void LaunchAppropriateMethod (String answer){

        switch (answer){

            //create a set of subdivisions
            case "ac" -> UserInput.GenSetOfSub();

            //create subdivisions
            case "asa" -> AddSubdivision(-1 , GenerateName(false) , new LinkedList<>(), new LinkedList<>(), true);
            case "asm" -> UserInput.ManCreateSub();

            //create blocks of flats
            case "aba" -> UserInput.AutoCreateBOF(true, GenerateName(true));
            case "abm" -> UserInput.ManCreateBOF();

            //create apartments
            case "aaa" -> UserInput.AutoCreateApt(true, 0);
            case "aam" -> UserInput.ManCreateApt();

            //create parking spaces
            case "apa" -> UserInput.AutoCreatePS(true, 0);
            case "apm" -> UserInput.ManCreatePS();

            //items and vehicles
            case "ai" -> UserInput.AddItem();
            case "avp" -> UserInput.AddVehicle(ThingTypes.PassengerCar.toString());
            case "avo" -> UserInput.AddVehicle(ThingTypes.OffRoadCar.toString());
            case "avm" -> UserInput.AddVehicle(ThingTypes.Motorbike.toString());
            case "ava" -> UserInput.AddVehicle(ThingTypes.AmphibiousVehicle.toString());
            case "avb" -> UserInput.AddVehicle(ThingTypes.Boat.toString());
            case "ro" -> UserInput.RemoveObject();

            // rent
            case "re" -> UserInput.Rent(PresentDate);
            case "at" -> UserInput.AddCoTenant(true);
            case "rt" -> UserInput.RmCoTenant(true);
            case "pso" -> UserInput.PrintStatusOfOneSpace(true, true, null);
            case "psa" -> UserInput.PrintStatusOfAllSpaces(false, subdivisions);
            case "er" -> UserInput.ExtendRent(true);
            case "cr" -> UserInput.CancelRent(true);

            //event log
            case "pe" -> EventLog.Print();
            case "ce" -> UserInput.ClearEventLog();

            //file management
            case "pm" -> Print.PrintSubAndPpl(subdivisions, PresentDate, people, true);
            case "cm" -> UserInput.ClearMemory();
            case "wf" -> UserInput.WriteToFile();
            case "rf" -> SetDataTo();

            //person
            case "cpa" -> AddPersonAutomatically();
            case "cpm" -> UserInput.AddPersonManually();
            case "rp" -> UserInput.RemovePerson();

            //other
            case "pd" -> PrintDate();
            case "pto" -> PrintTenantLetter();
            case "pta" -> PrintAllTenantLetters();
            case "st" -> UserInput.StartStopTime(TimeRunning);      //check rent in CheckRentThread;
            case "en" -> UserInput.EndProgramme();

        }

    }

    public static void GenSetOfSub (int minNumberOfSub, int maxNumberOfSub, int minNumOfBOFPerSub, int maxNumOfBOFPerSub,
                                    int minNumOfPSPerSub, int maxNumOfPSPerSub, int minNumOfAptPerBOF, int maxNUmOfAptPerBOF) {

        int NoOfSub = minNumberOfSub + (int)(Math.random() * (maxNumberOfSub - minNumberOfSub + 1));

        int IndexOfSubdivision = subdivisions.size();

        for (int i = 0; i < NoOfSub; i++){

            LinkedList<BlockOfFlats> BlocksOfFlats = new LinkedList<>();
            LinkedList<ParkingSpace> ParkingSpaces = new LinkedList<>();

            int NoOfBOF = minNumOfBOFPerSub + (int)(Math.random() * (maxNumOfBOFPerSub - minNumOfBOFPerSub + 1));
            int NoOfPS = minNumOfPSPerSub + (int)(Math.random() * (maxNumOfPSPerSub - minNumOfPSPerSub + 1));

            for (int j = 0; j < NoOfBOF; j++){

                LinkedList<Apartment> Apartments = new LinkedList<>();

                int NoOfApt = minNumOfAptPerBOF + (int)(Math.random() * (maxNUmOfAptPerBOF - minNumOfAptPerBOF + 1));

                for (int k = 0; k < NoOfApt; k++){

                    Apartments.add( new Apartment( (50 + (int)(Math.random() * (5000)) + (float)Math.random()), IndexOfSubdivision, j, k ) );

                }

                BlocksOfFlats.add( new BlockOfFlats(j, GenerateName(true), Apartments));

            }

            for (int j = 0; j < NoOfPS; j++)
                ParkingSpaces.add( new ParkingSpace((8 + (int)(Math.random() * (50)) + (float)Math.random()), IndexOfSubdivision, j) );

            AddSubdivision(IndexOfSubdivision, GenerateName(false), BlocksOfFlats, ParkingSpaces, false);

            IndexOfSubdivision++;

        }

        Print.PrintMessage("Successfully generated and filled " + NoOfSub + " subdivisions.");
        EventLog.AddEvent("Generated and filled " + NoOfSub + " new subdivisions;");

    }

    private static void PrintTenantLetter() {

        String PESEL = UserInput.GetPESEL();

        int Index = GetPersonIndex( PESEL );

        if (Index == -1)
            Print.PrintMessage("No tenant with given PESEL");
        else {
            EventLog.AddEvent("Printed tenant letters of a person with a given PESEL: " + PESEL + ";");
            Print.PrintTenantLetter(people.get(Index).GetTenantLetters());
        }

    }

    private static void PrintAllTenantLetters(){

        LinkedList<TenantLetter> tenantLetters = new LinkedList<>();
        LinkedList<TenantLetter> tempLetters;

        for (Person person : people) {

            tempLetters = person.GetTenantLetters();
            tenantLetters.addAll(tempLetters);

        }

        EventLog.AddEvent("Printed every tenant letter;");
        Print.PrintTenantLetter( tenantLetters );

    }

    private static void PrintDate(){

        EventLog.AddEvent("Present date printed;");
        Print.PrintDate(PresentDate);

    }

    public static void EndProgramme(){

        StopTime();
        Continue = false;

    }

    public static void SimTime () {

        TimeRunning = true;
        EventLog.AddEvent("Started simulating time;");
        EventLog.AddEvent("Started checking rent;");

        timeSim.SetPresentDate(PresentDate);
        timeSim.start();
        rentCheck.start();

    }

    public static void StopTime() {

        TimeRunning = false;

        timeSim.end();
        rentCheck.end();

    }

    public static void AddSubdivision (int SubdivisionID ,String name, LinkedList<BlockOfFlats> BOF, LinkedList<ParkingSpace> parking, boolean WithMessage) {

        //use -1 in SubdivisionID to create a new Subdivision

        if (SubdivisionID == -1)
            SubdivisionID = subdivisions.size();

        subdivisions.add( new Subdivision(SubdivisionID, name, BOF, parking) );

        if (WithMessage){
            Print.PrintMessage("Subdivision added.");
            EventLog.AddEvent("Subdivision with ID " + SubdivisionID + " was added;");
        }

    }

    public static void AddBlockOfFlats (int SubdivisionID, int blockNumber, String designation, LinkedList<Apartment> apartments) throws CustomException{

        //use -1 in blockNumber to create a new BOF

        int Index = GetSubdivisionIndex( SubdivisionID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else
            subdivisions.get(Index).AddBOF( new BlockOfFlats( (blockNumber == -1 ? subdivisions.get(Index).GetNoOfBOF() : blockNumber), designation, apartments) );

    }

    public static void AddApartment (int SubdivisionID, int blockNumber, float volume, String ID, int begDay, int begMonth, int begYear,
                              int endDay, int endMonth, int endYear, boolean currentlyOccupied, String tenantPESEL ) throws CustomException{

        //if SubdivisionID == -1 this is treated as an already existing apartment and the details will be read from 'ID'.

        if (SubdivisionID != -1)
            ID = (SubdivisionID) + "-" + (blockNumber) + "-";

        int Index = GetSubdivisionIndex( ID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else
            subdivisions.get(Index).AddApartment(volume, ID, begDay, begMonth, begYear, endDay, endMonth, endYear, currentlyOccupied, tenantPESEL);


    }

    public static void AddParkingSpace (int SubdivisionID, float volume, String ID, int begDay, int begMonth, int begYear, int endDay, int endMonth,
                                 int endYear, boolean currentlyOccupied, String tenantPESEL, LinkedList<Thing> things, float volumeOfThings) throws CustomException {

        //if SubdivisionID == -1 this is treated as an old parking space and the details will be read from 'ID'.

        if (SubdivisionID != -1)
            ID = (SubdivisionID) + "-";

        int Index = GetSubdivisionIndex( ID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else
            subdivisions.get(Index).AddParkingSpace(volume, ID, begDay, begMonth, begYear, endDay, endMonth, endYear, currentlyOccupied, tenantPESEL, things, volumeOfThings);


    }

    public static void AddPersonManually (String name, String surname, String PESEL, String adress, int DOB, int MOB, int YOB) throws CustomException{

        int Index = GetPersonIndex( PESEL );

        if (Index == -1){

            AddPerson(name, surname, PESEL, adress, DOB, MOB, YOB, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
            Print.PrintMessage("Added a person with a given PESEL: " + PESEL + ";");

        } else
            throw new CustomException(ExceptionMessages.PersonWithThisPESELAlreadyOnList.toString());

    }

    private static void AddPerson(String name, String surname, String PESEL, String adress, int DOB, int MOB, int YOB,
                          LinkedList<TenantLetter> TennantLetters, LinkedList<String> IDsOfRentedSpaces, LinkedList<String> IDsOfOccupiedSpaces){

        people.add( new Person(name, surname, PESEL, adress, DOB, MOB, YOB, TennantLetters, IDsOfRentedSpaces, IDsOfOccupiedSpaces));
        EventLog.AddEvent("Added a person with a given PESEL: " + PESEL + ";");

    }

    private static void AddPersonAutomatically (){

        boolean male = Math.random() < 0.5;
        String name = GenerateName(male);
        String surname = GenerateSurname(male);
        final int[] DateOfBirth = DateCalculator.GenRandomDate(1950, 2023);
        int DOB = DateOfBirth[0];
        int MOB = DateOfBirth[1];
        int YOB = DateOfBirth[2];
        String PESEL = GeneratePesel(male, DateOfBirth);
        String adress = GenerateAdress();

        Print.PrintMessage("Added a person with a given PESEL: " + PESEL + ";");
        AddPerson(name, surname, PESEL, adress, DOB, MOB, YOB, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());

    }

    private static String GenerateAdress(){

        String[] Cities = new String[]{"Warszawa", "Krakow", "Wroclaw", "Lodz", "Poznan", "Gdansk", "Szczecin",
                "Lublin", "Bydgoszcz", "Bialystok", "Zielona Gora", "Swinoujscie", "Malbork", "Sosnowiec"};

        String[] Streets = new String[]{"Marszalkowska", "Zielona", "Czerwona", "Duza", "Mala", "Ksiazeca", "Warszawska",
                "Koszykowa", "Dobrego Projektu", "Zdania Egzaminow", "Mickiewicza" };

        int HouseNumber = 1 + (int)(Math.random() * (100));
        int FlatNumber = 1 + (int)(Math.random() * (100));
        int WhichCity = (int)(Math.random() * (Cities.length ));
        int WhichStreet = (int)(Math.random() * (Streets.length ));

        return Cities[WhichCity] + ", " + Streets[WhichStreet] + " " + HouseNumber + "/" + FlatNumber;

    }

    private static String GenerateSurname (boolean male) {

        String[] MaleSurnames = new String[]{
                "Nowak", "Kowalski", "Wisniewski", "Wojcik", "Kowalczyk",
                "Kaminski", "Lewandowski", "Zielinski", "Szymanski", "Wozniak",
                "Dabrowski", "Kozlowski", "Mazur", "Jankowski", "Kwiatkowski"
        };

        String[] FemaleSurnames = new String[]{
                "Nowak", "Kowalska", "Wisniewska", "Wojcik", "Kowalska",
                "Kaminska", "Lewandowska", "Zielinska", "Szymanska", "Wozniak",
                "Dabrowska", "Kozlowska", "Mazur", "Jankowska", "Kwiatkowska"
        };

        int WhichName;

        if (male){
            WhichName = (int)(Math.random() * (MaleSurnames.length ));
            return MaleSurnames[WhichName];
        } else {
            WhichName = (int)(Math.random() * (FemaleSurnames.length ));
            return FemaleSurnames[WhichName];
        }

    }

    private static String GenerateName (boolean male) {

        String[] MaleNames = new String[]{
                "Adam","Adrian","Aleks","Albert",
                "Bartosz","Borys","Bruno","Benjamin",
                "Cezary","Czeslaw","Cyprian","Colin",
                "Dionizy","Damian","Denis","Dariusz",
                "Edward","Emil","Ernest","Eustachy",
                "Fabian","Filip","Florian","Franciszek",
                "Gabriel","Gracjan","Gustaw","Gwidon",
                "Hugo","Hektor","Henry","Hipolit",
                "Igor","Ignacy","Ivo","Ireneusz",
                "Jacek","Jaroslaw","Jedrzej","Julian",
                "Kacper","Kamil","Konrad","Kornel",
                "Lech","Leon","Leszek","Ludwik",
                "Maciej","Maks","Michal","Marek",
                "Nataliusz","Norbert","Nikita","Natan",
                "Olaf","Oskar","Oliwier","Orion",
                "Patryk","Pawel","Piotr","Przemyslaw",
                "Rafal","Roman","Ryszard","Remigiusz",
                "Sebastian","Stefan","Sylwester","Szymon",
                "Tadeusz","Teodor","Tymoteusz","Tytus",
                "Waclaw","Wieslaw","Wiktor","Witold",
                "Vincent","Xavier","Yoda","Zelislaw",
                "Zbigniew","Zenon","Zdzislaw","Zygmunt"
        };
        String[] FemaleNames = new String[]{
                "Ada","Anna","Antonina","Anastazja",
                "Beata","Blanka","Bozena","Bronislawa",
                "Cecylia","Carmen","Chiara","Czeslawa",
                "Dagmara","Danuta","Daria","Dominika",
                "Edyta","Ewelina","Elzbieta","Emilia",
                "Fatima","Felicja","Franciszka","Faustyna",
                "Greta","Gloria","Grace","Gabriela",
                "Halina","Hanna","Hermenegilda","Honorata",
                "Ida","Iga","Ina","Iza",
                "Jadwiga","Julia","Judyta","Joanna",
                "Kaja","Kinga","Krystyna","Kornelia",
                "Laura","Lidia","Lena","Lea",
                "Magda","Maja","Malgorzata","Maria",
                "Nadia","Nikola","Natalia","Naomi",
                "Ola","Olga","Oliwia","Oksana",
                "Pamela","Paulina","Patrycja","Pola",
                "Roma","Roberta","Renata","Roza",
                "Sylwia","Sara","Sabrina","Scarlett",
                "Tamara","Tomila","Tola","Taylor",
                "Wanda","Weronika","Wiktoria","Wladyslawa",
                "Violetta","Urszula","Yasmine","Zaneta",
                "Zoe","Zuzanna","Zoja","Zofia"
        };

        int WhichName;

        if (male){
            WhichName = (int)(Math.random() * (MaleNames.length ));
            return MaleNames[WhichName];
        } else {
            WhichName = (int)(Math.random() * (FemaleNames.length ));
            return FemaleNames[WhichName];
        }

    }

    private static String GeneratePesel(boolean male, int[] DateOfBirth){

        String PESEL = "";      //Generated according to data from the Polish government website.
        boolean OriginalPESEL = false;

        if (DateOfBirth[2] >= 2000){
            DateOfBirth[2] -= 2000;
            DateOfBirth[1] += 20;
        } else
            DateOfBirth[2] -= 1900;

        String RR = Integer.toString(DateOfBirth[2]);
        if (RR.length() == 1)
            RR = "0" + RR;

        String MM = Integer.toString(DateOfBirth[1]);
        if (MM.length() == 1)
            MM = "0" + MM;

        String DD = Integer.toString(DateOfBirth[0]);
        if (DD.length() == 1)
            DD = "0" + DD;

        PESEL += RR + MM + DD;

        while (!OriginalPESEL){

            int SerialNumber = 1000 + (int)(Math.random() * (9999 - 1000 + 1));

            if (male && SerialNumber % 2 == 0){

                SerialNumber--;
                if (SerialNumber < 1000)
                    SerialNumber += 2;

            } else if (!male && SerialNumber % 2 == 1){

                SerialNumber--;

            }

            String TempPESEL = PESEL + SerialNumber;
            TempPESEL += GenerateLastDigit(TempPESEL);

            int Index = GetPersonIndex(TempPESEL);  //checking whether the PESEL is new;

            if (Index == -1) {
                OriginalPESEL = true;
                PESEL = TempPESEL;
            }

        }

        return PESEL;

    }

    private static int GenerateLastDigit (String TempPESEL){

        int ControlSum = 0;

        for (int i = 0; i < TempPESEL.length(); i++){

            ControlSum +=  ( (TempPESEL.charAt(i) - '0' ) % 10 );

        }

        ControlSum = ControlSum % 10;
        ControlSum = ControlSum - 10;

        return -ControlSum;

    }

    public static void AddObject (String ParkingSpaceID, String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                           float weight, short doorNumber, String VIN, String bodyType , boolean withBasket, float LastParameter) throws CustomException, TooManyThingsException{

        int Index = GetSubdivisionIndex(ParkingSpaceID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else
            subdivisions.get(Index).AddObject(ParkingSpaceID, ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, LastParameter);

    }

    public static void AddTenantLetter ( String SpaceToRentID, int DOI, int MOI, int YOI ) throws CustomException{

        int Index = GetSubdivisionIndex( SpaceToRentID );
        String tenantPESEL;

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else{

            tenantPESEL = subdivisions.get(Index).GetTenantPESEL(SpaceToRentID);

            people.get( GetPersonIndex(tenantPESEL) ).AddTenantLetter(DOI, MOI, YOI, SpaceToRentID);

        }

    }

    public static LinkedList<Subdivision> GetSub() {

        return subdivisions;

    }

    public static void RemoveObject ( String ParkingSpaceID, String ThingType, float thingVolume, String name, float engineVolume, String engineType,
                               float weight, short doorNumber, String VIN, String bodyType , boolean withBasket,
                               boolean skipWithBasket, float LastParameter ) throws CustomException {

        int Index = GetSubdivisionIndex( ParkingSpaceID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else
            subdivisions.get(Index).RemoveObject(ParkingSpaceID, ThingType, thingVolume, name, engineVolume, engineType, weight, doorNumber, VIN, bodyType, withBasket, skipWithBasket, LastParameter);

    }

    public static void RemovePerson (String PESEL) throws CustomException{

        int PersonIndex = GetPersonIndex( PESEL );

        if (PersonIndex == -1)
            throw new CustomException(ExceptionMessages.NoPersonWithGivenPESEL.toString());

        LinkedList<String> IDsOfRentedPlaces = people.get(PersonIndex).GetIDsOfRentedSpaces();

        for (String ID : IDsOfRentedPlaces) {

            subdivisions.get(GetSubdivisionIndex(ID)).Clear(ID);

            for (Person p : people){
                p.RemoveCoTenantStatus( ID );
            }

        }

        people.remove(PersonIndex);
        EventLog.AddEvent("Removed a person with given PESEL: " + PESEL + ";");

    }

    public static void Rent (String PESEL, String SpaceToRentID, int begDay, int begMonth, int begYear, int endDay, int endMonth, int endYear, boolean WithMessage) throws CustomException, ProblematicTenantException{


        int PersonIndex = GetPersonIndex( PESEL );

        if (PersonIndex == -1)
            throw new CustomException(ExceptionMessages.NoPersonWithGivenPESEL.toString());

        int SubdivIndex = GetSubdivisionIndex( SpaceToRentID );

        if (SubdivIndex == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());

        people.get( PersonIndex ).RentSpace( SpaceToRentID );

        try {
            subdivisions.get( SubdivIndex ).Rent(PESEL, SpaceToRentID, begDay, begMonth, begYear, endDay, endMonth, endYear);
        } catch (Exception ex){

            people.get( PersonIndex ).RentUnsuccessful( SpaceToRentID );
            throw ex;

        }

        if (WithMessage){
            Print.PrintMessage("Successfully rented space with ID: " + SpaceToRentID + ".");
            EventLog.AddEvent("Person with PESEL " + PESEL + "rented a property with ID " + SpaceToRentID + ";");
        }

    }

    public static void StopRent (String SpaceToRentID) throws CustomException{

        String PESEL;

        PESEL = GetPESELOfTenant(SpaceToRentID);

        int Index = GetPersonIndex( PESEL );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoPersonWithGivenPESEL.toString());

        people.get(Index).StopRent(SpaceToRentID);

        subdivisions.get( GetSubdivisionIndex( SpaceToRentID ) ).Clear( SpaceToRentID );

        //checking for any co-tenants:
        if (SpaceToRentID.indexOf("-") != SpaceToRentID.lastIndexOf("-"))   //if a given space is an Apartment
            for (Person person : people)
                if (person.IsCoTenant(SpaceToRentID))
                    person.StopOccupyingWithoutException(SpaceToRentID);

    }

    public static void StopRentWithoutException (String SpaceToRentID) {

        try {
            StopRent( SpaceToRentID );
        } catch (Exception ex) {
            EventLog.AddEvent("Caught unexpected exception when stopping rent on space with ID: " + SpaceToRentID + ";");
        }

    }


    private static String GetPESELOfTenant ( String SpaceID ) throws CustomException {

        String PESEL;

        int Index = GetSubdivisionIndex( SpaceID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());
        else
            PESEL = subdivisions.get(Index).GetPESELOfTenant(SpaceID);

        if (PESEL == null)
            throw new CustomException(ExceptionMessages.IndicatedSpaceIsNotRented.toString());

        return PESEL;

    }

    public static void AddCoTenant (String CoTenantPESEL, String ApartmentID) throws CustomException{

        CheckApartmentID( ApartmentID );

        int Index = GetPersonIndex( CoTenantPESEL );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoPersonWithGivenPESEL.toString());

        people.get(Index).AddOccupiedSpace(ApartmentID);


    }

    public static void RemoveCoTenant (String CoTenantPESEL, String ApartmentID) throws CustomException {

        CheckApartmentID( ApartmentID );

        int Index = GetPersonIndex( CoTenantPESEL );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoPersonWithGivenPESEL.toString());

        people.get(Index).StopOccupying( ApartmentID );

    }


    public static void ExtendRent (String SpaceToRentID, int NoOfMonths) throws CustomException{

        int SubdivIndex = GetSubdivisionIndex( SpaceToRentID );
        String TenantPESEL;

        if (SubdivIndex == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());

        TenantPESEL = subdivisions.get(SubdivIndex).ExtendRent( SpaceToRentID, NoOfMonths);

        if (TenantPESEL == null)
            throw new CustomException(ExceptionMessages.NoOneRentsThisSpace.toString());

        people.get( GetPersonIndex( TenantPESEL ) ).ClearTenantLetter( SpaceToRentID );

    }

    public static SpaceToRent GetSpaceToRent (String SpaceID) throws CustomException{

        int Index = GetSubdivisionIndex( SpaceID );
        SpaceToRent spaceToRent;

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());

        spaceToRent = subdivisions.get(Index).GetSpaceToRent( SpaceID );

        return spaceToRent;

    }

    public static int[] GetLastPaymentDate (String TenantPESEL, String SpaceID){

        int Index = GetPersonIndex(TenantPESEL);

        return people.get(Index).GetLastPaymentDate(SpaceID);

    }



    //Misc:

    public static void CheckApartmentID ( String ApartmentID) throws CustomException {       //check whether an apartment exist and has a tenant

        int Index = GetSubdivisionIndex( ApartmentID );

        if (Index == -1)
            throw new CustomException(ExceptionMessages.NoSubdivisionWithGivenID.toString());

        subdivisions.get(Index).CheckApartmentID(ApartmentID);

    }


    public static String WithdrawSubdivisionID (String ID) {       //withdraw subdivision ID from ParkingSpaceID / ApartmentID

        return ID.substring(0, ID.indexOf("-"));

    }

    public static int GetSubdivisionIndex (String ID) {

        boolean found = false;
        int Index = 0;

        while (!found && Index < subdivisions.size())
            if ( subdivisions.get(Index).GetID().equals( WithdrawSubdivisionID( ID ) ) )
                found = true;
            else
                Index++;

        return (found ? Index : -1);

    }

    public static int GetSubdivisionIndex (int ID) {

        boolean found = false;
        int Index = 0;

        while (!found && Index < subdivisions.size())
            if ( subdivisions.get(Index).GetID().equals( Integer.toString(ID) ) )
                found = true;
            else
                Index++;

        return (found ? Index : -1);

    }

    public static int GetPersonIndex (String PESEL) {

        boolean found = false;
        int Index = 0;

        while (!found && Index < people.size())
            if ( people.get(Index).GetPESEL().equals( PESEL ) )
                found = true;
            else
                Index++;

        return (found ? Index : -1);

    }

    public static void SetDataTo (){

        boolean correct = true;
        boolean rentCheckOn = TimeRunning;
        ReadDataPasser readDataPasser = new ReadDataPasser(new LinkedList<>(), new LinkedList<>(), new int[]{1,1,2000});



        try {
            readDataPasser = ReadAndWrite.Read();
        } catch (CustomException ce) {

            correct = false;

            Print.PrintMessage("Data passed by file were grouped in an incorrect order. Old data remains in memory.");

        } catch (NoReadableDataInFileException nrex) {

            correct = false;

            Print.PrintMessage(nrex.getMessage() + " Old data remains in memory.");

        } catch (Exception ex) {

            correct = false;

            Print.PrintMessage("An exception with message: " + ex.getMessage() + " was thrown." +
                    "\nNothing was read and programme memory remains intact.");

        }

        if (correct) {

            if (rentCheckOn)
                StopTime();

            subdivisions = readDataPasser.GetSubdivisions();
            people = readDataPasser.GetPeople();
            UpdateDate(readDataPasser.GetDate());

            if (rentCheckOn)
                SimTime();

            EventLog.AddEvent("New data read from file;");
            Print.PrintMessage("Data successfully read from file.");

        }


    }

    public static LinkedList<TenantLetter> GetTenantLetters ( String SpaceID ) throws CustomException{

        int Index = GetSubdivisionIndex( SpaceID );
        String tenantPESEL;

        tenantPESEL = subdivisions.get(Index).GetTenantPESEL(SpaceID);

        return people.get( GetPersonIndex(tenantPESEL) ).GetTenantLetters();

    }

    public static void SetTenantLettersToExpired ( LinkedList<TenantLetter> tenantLetters ) {

        for (Person person : people) person.SetTenantLettersToExpired(tenantLetters);

    }

    public static void UpdateDate (int[] newDate){

        PresentDate = newDate;

    }

    public static LinkedList<Person> GetPeople () {

        return people;

    }

    public static int[] GetDate (){

        return PresentDate;

    }

    public static String GetCoTenantsPESELList(String SpaceID, String AddBeforeEachPESEL){

        LinkedList<String> PESELs = new LinkedList<>();

        for (int i = 0; i < people.size(); i++)
            if (people.get(i).IsCoTenant( SpaceID ))
                PESELs.add( AddBeforeEachPESEL + people.get(i).GetPESEL() + " (index on people list: " + i + ")" );

        String toReturn;

        if (PESELs.isEmpty())
            toReturn = "-1";
        else
            toReturn = PESELs.toString().substring(1, PESELs.toString().lastIndexOf(']')).replaceAll(", ", "");

        return  toReturn;

    }

    public static void ClearMemory() {

        if (TimeRunning)
            StopTime();

        EventLog.Clear();
        EventLog.AddEvent("Memory cleared;");
        people = new LinkedList<>();
        subdivisions = new LinkedList<>();


    }

}