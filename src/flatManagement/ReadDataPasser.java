package flatManagement;

import java.util.LinkedList;

public class ReadDataPasser {

    private LinkedList<Subdivision> subdivisions;
    private LinkedList<Person> people;
    private int[] date;

    public ReadDataPasser (LinkedList<Subdivision> subdivisions, LinkedList<Person> people, int[] date){

        this.subdivisions = subdivisions;
        this.people = people;
        this.date = date;

    }

    public LinkedList<Subdivision> GetSubdivisions (){

        return subdivisions;

    }

    public LinkedList<Person> GetPeople (){

        return people;

    }

    public int[] GetDate (){

        return date;

    }

}
