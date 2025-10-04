package flatManagement;

public class ProblematicTenantException extends Exception{
    public ProblematicTenantException (String PESEL, String listOfSpaces) {
        super( "\nPerson " + PESEL + "had already rented" + listOfSpaces + ".");
    }

}
