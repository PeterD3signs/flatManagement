package flatManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class ReadAndWrite {


    public static void ClearFile () throws Exception {

        PrintWriter pw = new PrintWriter( "FacilityDATA.txt" );
        pw.close();

    }

    public static void writeToFile() throws Exception {

        PrintWriter pw = null;

        try{

            pw = new PrintWriter( "FacilityDATA.txt" );

            LinkedList<Subdivision> subdivisions = Main.GetSub();
            LinkedList<Person> people = Main.GetPeople();
            int[] CurrentDate = Main.GetDate();

            pw.println("STATE OF SUBDIVISIONS AS OF " + CurrentDate[0] + "." + CurrentDate[1] + "." + CurrentDate[2] + ":");
            pw.println();

            for (Subdivision s : subdivisions) {

                pw.println("-> SUBDIVISION #" + s.GetID() + ", " + s.GetName() + ":");

                LinkedList<BlockOfFlats> blockOfFlats = s.GetBOF();
                LinkedList<ParkingSpace> parkingSpaces = s.GetPS();

                for (BlockOfFlats b : blockOfFlats) {

                    pw.println("\t\t* Block of flats #" + b.GetBlockNumber() + ", " + b.GetDesignation() + ":");

                    LinkedList<Apartment> apartments = b.GetApartments();

                    apartments.sort((a1, a2) -> Float.compare(a1.GetVolume(), a2.GetVolume()));       //sorting apartments in ascending order by volume

                    for (Apartment a : apartments) pw.println(a.toString());

                }

                if(parkingSpaces.isEmpty()){

                    pw.println("\n\tNO PARKING SPACES ON THIS SUBDIVISIONS.");

                } else {

                    pw.println("\n\tPARKING SPACES:");

                    parkingSpaces.sort((p1, p2) -> Float.compare(p1.GetVolume(), p2.GetVolume()));         //sorting parking spaces in ascending order by volume

                    for (ParkingSpace p : parkingSpaces) {

                        pw.println(p.toString());

                        LinkedList<Thing> things = p.GetThings();

                        things.sort((t1, t2) -> ((Float.compare(t2.GetVolume(), t1.GetVolume()) == 0 ? t2.GetName().compareTo(t1.GetName()) : Float.compare(t2.GetVolume(), t1.GetVolume()))));            //sorting things in descending volume and name

                        for (int k = 0; k < things.size(); k++) {
                            pw.print("\t\t\t\t" + k + ". ");
                            pw.println(things.get(k).toString());
                        }

                    }

                }

            }

            pw.println();
            pw.println();

            pw.println("PEOPLE:");
            pw.println();

            for (int i = 0; i < people.size(); i++){
                pw.print("Person " + i + ".");
                pw.println( people.get(i).toString() );
            }

            pw.println();
            pw.println();

            EventLog.AddEvent("Data printed to file;");

        } finally {

            if (pw != null)
                pw.close();

        }

    }

    public static ReadDataPasser Read () throws Exception{

        LinkedList<Person> people = new LinkedList<>();

        LinkedList<Subdivision> subdivisions = new LinkedList<>();
        int SubID = 0;
        String SubName = "";

        LinkedList<BlockOfFlats> BOF = new LinkedList<>();
        int BlockNumber = 0;
        String Designation = "";

        LinkedList<ParkingSpace> PS = new LinkedList<>();
        String PSID;
        float PSVolume;
        int PSBegDay;
        int PSBegMonth;
        int PSBegYear;
        int PSEndDay;
        int PSEndMonth;
        int PSEndYear;
        boolean PSCurOcp;
        String PSTenPESEL;
        LinkedList<Thing> Things;
        float VolumeOfThings;

        LinkedList<Apartment> Aparts = new LinkedList<>();
        String AptID;
        float AptVolume;
        int AptBegDay;
        int AptBegMonth;
        int AptBegYear;
        int AptEndDay;
        int AptEndMonth;
        int AptEndYear;
        boolean AptCurOcp;
        String AptTenPESEL;


        int[] PresentDate = new int[3];

        BufferedReader br = null;
        String s = null;
        boolean cont = true;
        boolean readPpl = false;
        boolean skipFirstReadLine = false;

        boolean SubdivisionRead = false;
        boolean BOFRead = false;
        boolean PSRead = false;
        boolean DateRead = false;

        int LineNumber = 0;

        try{

            br = new BufferedReader( new FileReader("FacilityDATA.txt") );

            //reading subdivisions and all its inner parts:
            while (cont){

                if (!skipFirstReadLine){
                    s = br.readLine();
                    LineNumber++;
                } else
                    skipFirstReadLine = false;

                if (s == null){                                             //if readPpl == false -> eof, else -> end of reading subdivisions

                    cont = false;

                    if (BOFRead)
                        BOF.add( new BlockOfFlats( BlockNumber, Designation, Aparts ));     //adding the previous BOF to the list of BOFs

                    if (SubdivisionRead)
                        subdivisions.add( new Subdivision(SubID, SubName, BOF, PS) );    //adding the previous subdivision to the list of subdivisions

                } else if (s.contains("STATE OF SUBDIVISIONS AS OF")) {      //reading the date

                    //("STATE OF SUBDIVISIONS AS OF " + CurrentDate[0] + "." + CurrentDate[1] + "." + CurrentDate[2] + ":")

                    s = s.substring(("STATE OF SUBDIVISIONS AS OF").length() + 1, s.lastIndexOf(":"));
                    PresentDate[0] = Integer.parseInt(s.substring(0, s.indexOf('.')));
                    PresentDate[1] = Integer.parseInt(s.substring(s.indexOf('.') + 1, s.lastIndexOf('.')));
                    PresentDate[2] = Integer.parseInt(s.substring(s.lastIndexOf('.') + 1));

                    DateRead = true;

                } else if (s.contains("-> SUBDIVISION #")){                 //adding a subdivision

                    if (!DateRead || PSRead)
                        throw new CustomException(ExceptionMessages.IncorrectDataOrder.toString());

                    if (BOFRead)
                        BOF.add( new BlockOfFlats( BlockNumber, Designation, Aparts ));     //adding the previous BOF to the list of BOFs

                    if (SubdivisionRead)
                        subdivisions.add( new Subdivision(SubID, SubName, BOF, PS) );    //adding the previous subdivision to the list of subdivisions

                    SubdivisionRead = true;

                    BOFRead = false;
                    PSRead = false;

                    BOF = new LinkedList<>();
                    PS = new LinkedList<>();

                    //getting the name and index of the new subdivision:

                    //("-> SUBDIVISION #" + s.GetID() + ", " + s.GetName() + ":")
                    SubID = Integer.parseInt( s.substring( ("-> SUBDIVISION #").length(), s.indexOf(',') ));
                    SubName = s.substring( s.indexOf(',') + 2, s.lastIndexOf(':'));

                } else if ( s.contains("* Block of flats #") ){             //adding a BOF

                    if (!DateRead || !SubdivisionRead || PSRead)
                        throw new CustomException(ExceptionMessages.IncorrectDataOrder.toString());

                    if (BOFRead)
                        BOF.add( new BlockOfFlats( BlockNumber, Designation, Aparts ));     //adding the previous BOF to the list of BOFs

                    BOFRead = true;
                    Aparts = new LinkedList<>();

                    //getting the name and index of bof:

                    //("\t\t* Block of flats #" + b.GetBlockNumber() + ", " + b.GetDesignation() + ":")
                    BlockNumber = Integer.parseInt( s.substring(s.indexOf('#') + 1, s.indexOf(",") ));
                    Designation = s.substring(s.indexOf(',') + 2, s.lastIndexOf(':'));

                } else if (s.contains("-Apartment")) {                      //adding an apartment

                    if (!DateRead || !SubdivisionRead || !BOFRead || PSRead)
                        throw new CustomException(ExceptionMessages.IncorrectDataOrder.toString());

                    /*
                    ("\t\t\t-Apartment " + ID + ":" +
                        "\n\t\t\t\tvolume: " + volume + ", " + (currentlyOccupied ? ("tenant PESEL: " + tenantPESEL + " (index on people list: " + PersonIndex + ");") : "unoccupied;") +
                        ( currentlyOccupied ? (
                        "\n\t\t\t\tbeginning of rent: " + begDay + "." + begMonth + "." + begYear + ", end of rent: " + endDay + "." + endMonth + "." + endYear + ";" +
                        "\n\t\t\t\t" + (CoTenantPESELs.equals("-1") ? "No co-tenants." : ( "Co-tenants:" + CoTenantPESELs ) ) )
                        : ""
                        )
                     */


                    AptID = s.substring( s.indexOf(' ') + 1, s.lastIndexOf(':'));

                    s = br.readLine();
                    if (s == null || !s.contains("volume: "))
                        throw new NoReadableDataInFileException( LineNumber );
                    LineNumber++;

                    AptVolume = Float.parseFloat( s.substring( s.indexOf(' ') + 1 , s.indexOf(',') ));

                    if (s.contains("unoccupied;")){

                        AptTenPESEL = null;
                        AptBegDay = 0;
                        AptBegMonth = 0;
                        AptBegYear = 0;
                        AptEndDay = 0;
                        AptEndMonth = 0;
                        AptEndYear = 0;
                        AptCurOcp = false;

                    } else {

                        AptTenPESEL = s.substring( s.indexOf(',') + (" tenant PESEL: ").length() + 1, s.indexOf('(') - 1 );
                        AptCurOcp = true;

                        s = br.readLine();
                        if (s == null || !s.contains("beginning of rent"))
                            throw new NoReadableDataInFileException( LineNumber );
                        LineNumber++;

                        s = s.substring( s.indexOf(':') + 2);
                        AptBegDay = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                        s = s.substring( s.indexOf('.') + 1);
                        AptBegMonth = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                        s = s.substring( s.indexOf('.') + 1);
                        AptBegYear = Integer.parseInt( s.substring( 0, s.indexOf(',') ));

                        s = s.substring( s.indexOf(':') + 2);
                        AptEndDay = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                        s = s.substring( s.indexOf('.') + 1);
                        AptEndMonth = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                        s = s.substring( s.indexOf('.') + 1);
                        AptEndYear = Integer.parseInt( s.substring( 0, s.indexOf(';') ));

                        //reading the co-tenants:
                        s = br.readLine();
                        if (s == null || !s.contains("tenants"))
                            throw new NoReadableDataInFileException( LineNumber );
                        LineNumber++;

                        if ( s.contains("Co-tenants:") ){

                            //(AddBeforeEachPESEL + people.get(i).GetPESEL() + " (index on people list: " + i + ")")
                            //AddBeforeEachPESEL = "\n\t\t\t\t"

                            s = br.readLine();
                            if (s == null)
                                throw new NoReadableDataInFileException( LineNumber );
                            LineNumber++;

                            while (s.contains("(index on people list:")){

                                s = br.readLine();
                                if (s == null)
                                    throw new NoReadableDataInFileException( LineNumber );
                                LineNumber++;

                            }

                            skipFirstReadLine = true;


                        }


                    }

                    Aparts.add(new Apartment(AptVolume, AptID, AptBegDay, AptBegMonth, AptBegYear, AptEndDay, AptEndMonth, AptEndYear, AptCurOcp, AptTenPESEL));

                } else if (s.contains("NO PARKING SPACES ON THIS SUBDIVISIONS.")) {         //skipping adding parking spaces

                    if (!DateRead || !SubdivisionRead)
                        throw new CustomException(ExceptionMessages.IncorrectDataOrder.toString());

                } else if (s.contains("PARKING SPACES:")) {                                 //adding parking spaces

                    if (!DateRead || !SubdivisionRead)
                        throw new CustomException(ExceptionMessages.IncorrectDataOrder.toString());

                    /*
                    ("\t\t\t-PS " + ID + ":" +
                        "\n\t\t\t\tvolume: " + volume + ", " + (currentlyOccupied ? ("tenant PESEL: " + tenantPESEL + " (index on people list: " + PersonIndex + ");") : "not rented;") +
                        ( currentlyOccupied ? ("\n\t\t\t\tbeginning of rent: " + begDay + "." + begMonth + "." + begYear + ", end of rent: " + endDay + "." + endMonth + "." + endYear + ";" +
                                "\n\t\t\t\t" + (things.isEmpty() ? "No things stored on this parking space." : "Things stored on this parking space (total volume: " + volumeOfThings + "):")
                        ) : "" ));
                    */

                    s = br.readLine();
                    if (s == null || !s.contains("-PS "))
                        throw new NoReadableDataInFileException( LineNumber );
                    LineNumber++;

                    while (s.contains("-PS")){

                        PSID = s.substring(s.indexOf(' ') + 1, s.indexOf(':'));

                        s = br.readLine();
                        if (s == null || !s.contains("volume: "))
                            throw new NoReadableDataInFileException( LineNumber );
                        LineNumber++;

                        PSVolume = Float.parseFloat( s.substring( s.indexOf(' ') + 1 , s.indexOf(',') ));

                        Things = new LinkedList<>();
                        VolumeOfThings = 0;

                        if (s.contains("not rented;")){

                            PSTenPESEL = null;
                            PSBegDay = 0;
                            PSBegMonth = 0;
                            PSBegYear = 0;
                            PSEndDay = 0;
                            PSEndMonth = 0;
                            PSEndYear = 0;
                            PSCurOcp = false;

                            s = br.readLine();
                            LineNumber++;

                        } else {

                            PSTenPESEL = s.substring( s.indexOf(',') + (" tenant PESEL: ").length() + 1, s.indexOf('(') - 1 );
                            PSCurOcp = true;

                            s = br.readLine();
                            if (s == null || !s.contains("beginning of rent"))
                                throw new NoReadableDataInFileException( LineNumber );
                            LineNumber++;

                            s = s.substring( s.indexOf(':') + 2);
                            PSBegDay = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                            s = s.substring( s.indexOf('.') + 1);
                            PSBegMonth = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                            s = s.substring( s.indexOf('.') + 1);
                            PSBegYear = Integer.parseInt( s.substring( 0, s.indexOf(',') ));

                            s = s.substring( s.indexOf(':') + 2);
                            PSEndDay = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                            s = s.substring( s.indexOf('.') + 1);
                            PSEndMonth = Integer.parseInt( s.substring( 0, s.indexOf('.') ));

                            s = s.substring( s.indexOf('.') + 1);
                            PSEndYear = Integer.parseInt( s.substring( 0, s.indexOf(';') ));

                            //reading the stored things:
                            s = br.readLine();
                            if (s == null)
                                throw new NoReadableDataInFileException( LineNumber );
                            LineNumber++;

                            if (s.contains("No things stored on this parking space")){     //there are no things stored on this PS

                                s = br.readLine();      //reading the next line in advance to unify the further code with else if branch
                                LineNumber++;

                            } else if (s.contains("Things stored")){

                                VolumeOfThings = Float.parseFloat( s.substring( s.indexOf(":") + 2, s.indexOf(")")) );

                                boolean contReadingItems = true;

                                s = br.readLine();      //reading the first object
                                LineNumber++;

                                while (contReadingItems){

                                    String object = "";

                                    if (s.contains("--") && s.contains("."))
                                        object = s.substring(s.indexOf('.') + 2, s.indexOf("--") - 1);

                                    String itemName = null;
                                    float itemVolume = 0;
                                    float engineVolume = 0;
                                    String engineType = null;
                                    float weight = 0;
                                    float thirdParameter = 0;
                                    boolean withBasket = false;
                                    short doorNumber = 0;
                                    String VIN = null;
                                    String bodyType = null;

                                    String readObject = "none";      //which object turned out to be the one that was read

                                    if ( CompareToObject( object , true, true, true, true, true, true) ){       //reading name and volume

                                        if (s == null)
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        itemName = s.substring( s.indexOf("--") + 3, s.indexOf(':'));

                                        s = br.readLine();
                                        if (s == null || !s.contains("Volume:"))
                                            throw new NoReadableDataInFileException( LineNumber );
                                        LineNumber++;

                                        itemVolume = Float.parseFloat( s.substring( s.indexOf(':') + 2, s.indexOf(';')));

                                        s = br.readLine();
                                        LineNumber++;

                                        readObject = ThingTypes.Thing.toString();

                                    }

                                    if ( CompareToObject( object , false, true, true, true, true, true ) ){             //reading vehicle data

                                        if (s == null || !s.contains("Engine volume:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        engineVolume = Float.parseFloat( s.substring( s.indexOf(':') + 2, s.indexOf(';')));

                                        s = br.readLine();
                                        if (s == null || !s.contains("Engine type:"))
                                            throw new NoReadableDataInFileException( LineNumber );
                                        LineNumber++;

                                        engineType = s.substring( s.indexOf(':') + 2, s.indexOf(';'));

                                        s = br.readLine();
                                        if (s == null || !s.contains("Weight:"))
                                            throw new NoReadableDataInFileException( LineNumber );
                                        LineNumber++;

                                        weight = Float.parseFloat( s.substring( s.indexOf(':') + 2, s.indexOf(';')));

                                        s = br.readLine();
                                        LineNumber++;

                                    }

                                    if ( CompareToObject( object , false, false, false, false, false, true) ){        //reading a boat

                                        if (s == null || !s.contains("Buoyancy:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        thirdParameter = Float.parseFloat( s.substring( s.indexOf(':') + 2, s.indexOf(';')));

                                        s = br.readLine();
                                        LineNumber++;

                                        readObject = ThingTypes.Boat.toString();

                                    }

                                    if ( CompareToObject( object , false, false, false, false, true, false) ){        //reading an amphibious vehicle

                                        if (s == null || !s.contains("Armour thickness:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        thirdParameter = Float.parseFloat( s.substring( s.indexOf(':') + 2, s.indexOf(';')));

                                        s = br.readLine();
                                        LineNumber++;

                                        readObject = ThingTypes.AmphibiousVehicle.toString();

                                    }

                                    if ( CompareToObject( object , false, false, false, true, false, false) ){        //reading a motorbike

                                        if (s == null || !s.contains("Does it come with a basket:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        withBasket = ( s.substring( s.indexOf(':') + 2, s.indexOf(';')).equals("yes") );

                                        s = br.readLine();
                                        LineNumber++;

                                        readObject = ThingTypes.Motorbike.toString();

                                    }

                                    if ( CompareToObject( object , false, true, true, false, false, false) ){        //reading a car

                                        if (s == null || !s.contains("Door number:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        doorNumber = (short)( Integer.parseInt( s.substring( s.indexOf(':') + 2, s.indexOf(';'))) );

                                        s = br.readLine();
                                        if (s == null || !s.contains("VIN:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);
                                        LineNumber++;

                                        VIN = s.substring( s.indexOf(':') + 2, s.indexOf(';'));

                                        s = br.readLine();
                                        LineNumber++;

                                    }

                                    if ( CompareToObject( object, false, false, true, false, false, false)){        //reading an off-road car

                                        if (s == null || !s.contains("Wading depth:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        thirdParameter = Float.parseFloat( s.substring( s.indexOf(':') + 2, s.indexOf(';')));

                                        s = br.readLine();
                                        LineNumber++;

                                        readObject = ThingTypes.OffRoadCar.toString();

                                    }

                                    if ( CompareToObject( object, false, true, false, false, false, false)){        //reading a passenger car

                                        if (s == null || !s.contains("Body type:"))
                                            throw new NoReadableDataInFileException( LineNumber - 1);

                                        bodyType = s.substring( s.indexOf(':') + 2, s.indexOf(';'));

                                        s = br.readLine();
                                        LineNumber++;

                                        readObject = ThingTypes.PassengerCar.toString();

                                    }


                                    //Adding item:

                                    if (readObject.equals( ThingTypes.Thing.toString() ))

                                        Things.add( new Thing( itemVolume, itemName) );

                                    else if (readObject.equals( ThingTypes.Boat.toString() ))

                                        Things.add( new Boat( itemVolume, itemName, engineVolume, engineType, weight, thirdParameter) );

                                    else if (readObject.equals( ThingTypes.AmphibiousVehicle.toString() ))

                                        Things.add( new AmphibiousVehicle( itemVolume, itemName, engineVolume, engineType, weight, thirdParameter) );

                                    else if (readObject.equals( ThingTypes.Motorbike.toString() ))

                                        Things.add( new Motorbike( itemVolume, itemName, engineVolume, engineType, weight, withBasket) );

                                    else if (readObject.equals( ThingTypes.OffRoadCar.toString() ))

                                        Things.add( new OffRoadCar( itemVolume, itemName, engineVolume, engineType, weight, doorNumber, VIN, thirdParameter) );

                                    else if (readObject.equals( ThingTypes.PassengerCar.toString() ))

                                        Things.add( new PassengerCar( itemVolume, itemName, engineVolume, engineType, weight, doorNumber, VIN, bodyType) );

                                    else

                                        contReadingItems = false;


                                    if (s == null)                  //Next line is the eof
                                        contReadingItems = false;

                                }

                            } else {
                                throw new NoReadableDataInFileException( LineNumber - 1 );
                            }

                        }

                        PS.add( new ParkingSpace( PSVolume , PSID, PSBegDay, PSBegMonth, PSBegYear, PSEndDay, PSEndMonth, PSEndYear, PSCurOcp, PSTenPESEL, Things, VolumeOfThings ));

                        if (s == null)
                            s = "end";

                    }

                    skipFirstReadLine = true;

                } else if (s.contains("PEOPLE:")){

                    s = null;
                    readPpl = true;
                    skipFirstReadLine = true;

                }


            }

            //reading people:
            if (readPpl){

                cont = true;

                s = br.readLine();
                LineNumber++;

                while (cont){

                    if (s == null){         //eof

                        cont = false;

                    } else if ( s.contains("Person ") ){    //person

                        /*
                        " " + name + " " + surname + ":" +
                        "\n\t\tPESEL: " + PESEL + ";" +
                        "\n\t\tDate of birth: " + DOB + "." + MOB + "." + YOB + ";" +
                        "\n\t\tAdress: " + adress + ";" +
                        "\n\t\t" + (TenantLetters.equals("-1") ? "No tenant letters;" : ( "Tenant letters:" + "\n\t\t\t" + TenantLetters ) ) +
                        "\n\t\t" + (IDsOfRentedPlaces.equals("-1") ? "No rented spaces;" : ("IDs of rented spaces:" + "\n\t\t\t" + IDsOfRentedPlaces ) ) +
                        "\n\t\t" + (IDsOfOccupiedPlaces.equals("-1") ? "No occupied spaces;" : ("IDs of occupied spaces:" + "\n\t\t\t" + IDsOfOccupiedPlaces ) ) );
                         */


                        //Reading personal data:
                        String name;
                        String surname;
                        String PESEL;
                        int[] DOB;
                        String Adress;
                        LinkedList<TenantLetter> tenantLetters = new LinkedList<>();
                        LinkedList<String> IDsOfRentedPlaces = new LinkedList<>();
                        LinkedList<String> IDsOfOccupiedPlaces = new LinkedList<>();

                        s = s.substring( s.indexOf('.') + 2 );

                        name = s.substring( 0, s.indexOf(' ') );
                        surname = s.substring( s.indexOf(' ') + 1, s.indexOf(':'));

                        s = br.readLine();
                        if (s == null || !s.contains("PESEL:"))
                            throw new NoReadableDataInFileException( LineNumber - 1);
                        LineNumber++;

                        PESEL = s.substring( s.indexOf(':') + 2, s.indexOf(';'));

                        s = br.readLine();
                        if (s == null || !s.contains("Date of birth:"))
                            throw new NoReadableDataInFileException( LineNumber - 1);
                        LineNumber++;

                        DOB = new int[]{
                                Integer.parseInt( s.substring( s.indexOf(':') + 2, s.indexOf('.') ) ),
                                Integer.parseInt( s.substring( s.indexOf('.') + 1, s.lastIndexOf('.') ) ),
                                Integer.parseInt( s.substring( s.lastIndexOf('.') + 1, s.indexOf(';') ) )
                        };

                        s = br.readLine();
                        if (s == null || !s.contains("Adress:"))
                            throw new NoReadableDataInFileException( LineNumber - 1);
                        LineNumber++;

                        Adress = s.substring( s.indexOf(':') + 2, s.indexOf(';'));



                        //Reading Tenant Letters:
                        s = br.readLine();
                        if (s == null || (!s.contains("Tenant letters:") && !s.contains("No tenant letters;")))
                            throw new NoReadableDataInFileException( LineNumber - 1);
                        LineNumber++;

                        if (s.contains("Tenant letters:")){

                            s = br.readLine();
                            if (s == null || !s.contains("-> SpaceID: "))
                                throw new NoReadableDataInFileException( LineNumber - 1);
                            LineNumber++;

                            while ( s.contains("-> SpaceID:")){

                                boolean IsActive = !s.contains("inactive");

                                s = s.substring( s.indexOf(':') + 2 );
                                String SpaceID = s.substring(0, s.indexOf(','));

                                s = s.substring( s.indexOf(':') + 2 );
                                int DayOfIssue = Integer.parseInt(s.substring(0, s.indexOf('.')));

                                s = s.substring( s.indexOf('.') + 1, s.indexOf(','));
                                int MonthOfIssue = Integer.parseInt( s.substring(0, s.indexOf('.')) );
                                int YearOfIssue = Integer.parseInt( s.substring(s.indexOf('.') + 1) );

                                tenantLetters.add( new TenantLetter( DayOfIssue, MonthOfIssue, YearOfIssue, SpaceID, IsActive) );

                                s = br.readLine();
                                if (s == null)
                                    throw new NoReadableDataInFileException( LineNumber - 1);
                                LineNumber++;

                            }

                        } else {

                            s = br.readLine();
                            if (s == null)
                                throw new NoReadableDataInFileException( LineNumber - 1);
                            LineNumber++;

                        }

                        //Reading IDs of rented spaces:
                        if (s.contains("IDs of")){

                            s = br.readLine();
                            if (s == null)
                                throw new NoReadableDataInFileException( LineNumber - 1);
                            LineNumber++;

                            while (s.contains("*")){

                                IDsOfRentedPlaces.add( s.substring( s.indexOf('*') + 2, s.indexOf(';') ));

                                s = br.readLine();
                                if (s == null)
                                    throw new NoReadableDataInFileException( LineNumber - 1);
                                LineNumber++;

                            }

                        } else {

                            s = br.readLine();
                            if (s == null)
                                throw new NoReadableDataInFileException( LineNumber - 1);
                            LineNumber++;

                        }


                        //Reading IDs of occupied spaces:
                        if (s.contains("IDs of")){

                            s = br.readLine();
                            if (s == null)
                                throw new NoReadableDataInFileException( LineNumber - 1);
                            LineNumber++;

                            while (s.contains("*")){

                                IDsOfOccupiedPlaces.add( s.substring( s.indexOf('*') + 2, s.indexOf(';') ));

                                s = br.readLine();
                                if (s == null)
                                    s = "eof!";
                                LineNumber++;

                            }

                            if ( s.equals("eof!") )
                                s = null;

                        } else {

                            s = br.readLine();
                            LineNumber++;

                        }

                        people.add( new Person(name, surname, PESEL, Adress, DOB[0], DOB[1], DOB[2], tenantLetters, IDsOfRentedPlaces, IDsOfOccupiedPlaces) );

                    } else {        //reading a line with unnecessaryy data

                        s = br.readLine();
                        LineNumber++;

                    }

                }

            }

        } finally {

            if (br != null)
                br.close();

        }

        return new ReadDataPasser(subdivisions, people, PresentDate);

    }

    private static boolean CompareToObject ( String object, boolean toItem, boolean toPassengerCar, boolean toOffRoadCar, boolean toMotorbike, boolean toAmpVeh, boolean toBoat){

        boolean fitsCategory = false;

        if ( object.equals("Item") && toItem){
            fitsCategory = true;
        }

        if ( object.equals( ThingTypes.PassengerCar.toString() ) && toPassengerCar){
            fitsCategory = true;
        }

        if ( object.equals( ThingTypes.OffRoadCar.toString() ) && toOffRoadCar){
            fitsCategory = true;
        }

        if ( object.equals( ThingTypes.Motorbike.toString() ) && toMotorbike){
            fitsCategory = true;
        }

        if ( object.equals( ThingTypes.AmphibiousVehicle.toString() ) && toAmpVeh){
            fitsCategory = true;
        }

        if ( object.equals( ThingTypes.Boat.toString() ) && toBoat){
            fitsCategory = true;
        }

        return fitsCategory;

    }

}
