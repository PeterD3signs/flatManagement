package flatManagement;

import java.util.ArrayList;
import java.util.LinkedList;

public class Print {

    public static char PrintWelcomeMessage (){

        System.out.println();
        System.out.println("Welcome to ApartmentManager3000v10 Premium Ultimate!");

        return ImportOrCreate();

    }

    private static char ImportOrCreate (){

        System.out.println("\nWould you like to import people and subdivisions from file, create own new objects,");
        System.out.println("or read from the basic predetermined set (in accordance with the provided task)? ('i'/'c'/'r' - 'import'/'create'/'read')");

        char answer = UserInput.GetCharInput(0);

        if (answer != 'i' && answer != 'c' && answer != 'r'){
            System.out.println("Incorrect character.");
            answer = ImportOrCreate();
        }

        return answer;

    }

    public static void PrintTenantLetter (LinkedList<TenantLetter> tenantLetter) {

        int TLNumber = 0;

        System.out.println();

        for (TenantLetter letter : tenantLetter) {

            System.out.println("Tenant letter number #" + TLNumber + ":");
            System.out.println(letter.toString().replaceAll("!&", ","));

        }

    }

    public static void PrintMenu (){

        System.out.println();
        System.out.println("""
                        What would you like to do? Input a corresponding set of character to initiate a given action:
                        
                        - CREATING SPACES:
                        'ac' - Automatically create a set of ready Subdivisions
                        'as' - Add a subdivision
                        'ab' - Add a block of flats (to a subdivision)
                        'aa' - Add an apartment (to a block of flats)
                        'ap' - Add a parking space (to a subdivision)
                        - MANAGING CO-TENANTS:
                        'at' - Add a co-tenant
                        'rt' - Remove a co-tenant
                        - MANAGING OBJECTS:
                        'ai' - Add an item
                        'av' - Add a vehicle
                        'ro' - Remove an object
                        - MANAGING RENT:
                        're' - Rent (add the first tenant)
                        'ps' - Print the status of rented properties
                        'er' - Extend rent
                        'cr' - Cancel rent
                        - EVENT LOG:
                        'pe' - Print event log
                        'ce' - Clear event log
                        - FILE MANAGEMENT:
                        'pm' - Print memory
                        'cm' - Clear memory
                        'wf' - Write to file
                        'rf' - Read from file
                        - OTHER:
                        'cp' - Create a person
                        'rp' - Remove a person
                        'pd' - Print current date
                        'pt' - Print tenant letters
                        'st' - Start/Stop time simulation
                        'en' - End session
                        
                        ... and press the 'enter' key.
                        
                        Your answer here:""");

    }

    public static void PrintDate (int[] date){

        System.out.println("\nCURRENT DATE: " + date[0] + "." + date[1] + "." + date[2] + ";\n");

    }

    public static void PrintMessage(String Message){

        System.out.println("\n" + Message + "\n");

    }

    public static void PrintSubdivisionList (LinkedList<Subdivision> subdivisions, int[] CurrentDate, boolean withMessage){

        System.out.println("STATE OF SUBDIVISIONS AS OF " + CurrentDate[0] + "." + CurrentDate[1] + "." + CurrentDate[2] + ":");
        System.out.println();

        for (Subdivision s : subdivisions) {

            System.out.println("-> SUBDIVISION #" + s.GetID() + ", " + s.GetName() + ":");

            LinkedList<BlockOfFlats> blockOfFlats = s.GetBOF();
            LinkedList<ParkingSpace> parkingSpaces = s.GetPS();

            for (BlockOfFlats b : blockOfFlats) {

                System.out.println("\t\t* Block of flats #" + b.GetBlockNumber() + ", " + b.GetDesignation() + ":");

                LinkedList<Apartment> apartments = b.GetApartments();

                apartments.sort((a1, a2) -> Float.compare(a1.GetVolume(), a2.GetVolume()));       //sorting apartments in ascending order by volume

                for (Apartment a : apartments) System.out.println(a.toString());

            }

            if (parkingSpaces.isEmpty()){

                System.out.println("\n\tNO PARKING SPACES ON THIS SUBDIVISIONS.");

            } else {

                System.out.println("\n\tPARKING SPACES:");

                parkingSpaces.sort((p1, p2) -> Float.compare(p1.GetVolume(), p2.GetVolume()));         //sorting parking spaces in ascending order by volume

                for (ParkingSpace p : parkingSpaces) {

                    System.out.println(p.toString());

                    LinkedList<Thing> things = p.GetThings();

                    things.sort((t1, t2) -> ((Float.compare(t2.GetVolume(), t1.GetVolume()) == 0 ? t2.GetName().compareTo(t1.GetName()) : Float.compare(t2.GetVolume(), t1.GetVolume()))));            //sorting things in descending volume and name

                    for (int k = 0; k < things.size(); k++) {
                        System.out.print("\t\t\t\t" + k + ". ");
                        System.out.println(things.get(k).toString());
                    }


                }

            }

            System.out.println();
            System.out.println();

        }

        System.out.println();
        System.out.println();

        if (withMessage)
            EventLog.AddEvent("Printed the subdivisions list;");

    }

    public static void PrintPeopleList (LinkedList<Person> people, boolean withMessage){

        System.out.println("PEOPLE:");
        System.out.println();

        for (int i = 0; i < people.size(); i++){
            System.out.print(i + ". ");
            System.out.println( people.get(i).toString() );
        }


        System.out.println();
        System.out.println();

        if (withMessage)
            EventLog.AddEvent("Printed the people list;");

    }

    public static void PrintSubAndPpl (LinkedList<Subdivision> subdivisions, int[] CurrentDate, LinkedList<Person> people, boolean withMessage){

        PrintSubdivisionList(subdivisions, CurrentDate, withMessage);
        PrintPeopleList(people, withMessage);

    }

    public static void PrintOnePropertyStatus (SpaceToRent spaceToRent, boolean rentPayed, int[] LastPaymentDate){

        System.out.println( spaceToRent.GetStatus(rentPayed, LastPaymentDate) );

    }





}

