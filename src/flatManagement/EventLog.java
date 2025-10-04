package flatManagement;

import java.util.LinkedList;

public class EventLog {     //Stores last X events

    private static int X = 10000;       //limit of entries in Event Log

    private static LinkedList<String> Events = new LinkedList<>();

    public static void AddEvent (String Event){

        if (Events.size() > X)
            Events.remove(0);

        Events.add(Event + "\n");

    }

    public static void Print (){

        AddEvent("Event log was printed;");
        System.out.println();
        System.out.println(  Events.toString().substring(1, Events.toString().lastIndexOf(']')).replaceAll(", ", "") );
        System.out.println();

    }

    public static void Clear (){

        Events = new LinkedList<>();
        AddEvent("Event log was cleared;");

    }

}
