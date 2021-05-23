package si.src.naloga.imenik;

import si.src.naloga.Main;
import si.src.naloga.kontakt.Kontakt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TelefonskiImenik {

    private List<Kontakt> seznamKontaktov;

    public TelefonskiImenik() {
        seznamKontaktov = new ArrayList<>();
    }

    /**
     * Metaoda izpiše vse kontakte
     */
    public void izpisiVseKontakte() throws SQLException {
        // Getting all values from table
        // No need for a PreparedStatement since we have no user input
        ResultSet results = Main
                .grabConnection()
                .createStatement()
                .executeQuery("SELECT * FROM telefonski_imenik ORDER BY id");

        // Output results of query to screen
        printResults(results);
    }

    /**
     * Metaoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta
     */
    public void dodajKontakt() throws SQLException {
        // I used the following constraint to prevent duplicates
        //  alter table telefonski_imenik add unique unique_kontakt(Ime, Priimek, Naslov, Email, Telefon, Mobilni_telefon, Opomba);
        // Initially I only had the constraint on Ime and Priimek, because I thought that of course we can't have two of the same people in our database
        // But in real life, it is possible to have two identically named people, like Gašper Galič :)
        // But 100% There won't be people with the same NAME, LASTNAME, ADDRESS etc etc.
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vnesite podatke o kontaku:");

        System.out.println("Ime: ");
        String inputIme = scanner.nextLine();

        System.out.println("Priimek: ");
        String inputPriimek = scanner.nextLine();

        System.out.println("Naslov: ");
        String inputNaslov = scanner.nextLine();

        System.out.println("Email: ");
        String inputEmail = scanner.nextLine();

        System.out.println("Telefon: ");
        String inputTelefon = scanner.nextLine();

        System.out.println("Mobilni telefon: ");
        String inputMobilni_telefon = scanner.nextLine();

        System.out.println("Opomba: ");
        String inputOpomba = scanner.nextLine();

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("INSERT INTO telefonski_imenik" +
                        " (Ime, Priimek, Naslov, Email, Telefon, Mobilni_telefon, Opomba) VALUES (?, ?, ?, ?, ?, ?, ?)");

        preparedStatement.setString(1, inputIme);
        preparedStatement.setString(2, inputPriimek);
        preparedStatement.setString(3, inputNaslov);
        preparedStatement.setString(4, inputEmail);
        preparedStatement.setString(5, inputTelefon);
        preparedStatement.setString(6, inputMobilni_telefon);
        preparedStatement.setString(7, inputOpomba);

        executeUpdateStatement(preparedStatement);
    }

    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     */
    public void urediKontakt() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Kateri vnos bi radi posodbili?(ID)");
        int inputId = Integer.parseInt(scanner.nextLine());

        // Check if the ID we want to edit exists, if it does not cancel the operation and print message
        if(!checkIfIDexists(inputId)){
            System.out.println("Izbrani kontakt ne obstaja!");
            return;
        }

        System.out.println("Vnesite novo ime: ");
        String inputIme = scanner.nextLine();

        System.out.println("Vnesite nov Priimek: ");
        String inputPriimek = scanner.nextLine();

        System.out.println("Vnesite nov naslov:: ");
        String inputNaslov = scanner.nextLine();

        System.out.println("Vnesite nov Email: ");
        String inputEmail = scanner.nextLine();

        System.out.println("Vnesite nov telefon: ");
        String inputTelefon = scanner.nextLine();

        System.out.println("Vnesite nov mobilni telefon: ");
        String inputMobilni_telefon = scanner.nextLine();

        System.out.println("Vnesite novo opombo: ");
        String inputOpomba = scanner.nextLine();

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("UPDATE telefonski_imenik" +
                        " SET Ime=?, Priimek=?, Naslov=?, Email=?, Telefon=?, Mobilni_telefon=?, Opomba=? WHERE ID=?");

        preparedStatement.setString(1, inputIme);
        preparedStatement.setString(2, inputPriimek);
        preparedStatement.setString(3, inputNaslov);
        preparedStatement.setString(4, inputEmail);
        preparedStatement.setString(5, inputTelefon);
        preparedStatement.setString(6, inputMobilni_telefon);
        preparedStatement.setString(7, inputOpomba);
        preparedStatement.setInt(8, inputId);

        executeUpdateStatement(preparedStatement);
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Kateri vnos bi radi izbrisali?(ID)");
        int inputId = Integer.parseInt(scanner.nextLine());

        if(!checkIfIDexists(inputId)){
            System.out.println("Izbrani kontakt ne obstaja!");
            return;
        }

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("DELETE FROM telefonski_imenik WHERE ID=?");

        preparedStatement.setInt(1, inputId);

        executeUpdateStatement(preparedStatement);
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Kateri vnos bi radi prikazali?(ID)");
        int inputId = Integer.parseInt(scanner.nextLine());

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("SELECT * FROM telefonski_imenik WHERE ID = ?");

        preparedStatement.setInt(1, inputId);

        ResultSet results = preparedStatement.executeQuery();
        printResults(results);
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiSteviloKontaktov() throws SQLException {
        ResultSet results = Main
                .grabConnection()
                .createStatement()
                .executeQuery("SELECT COUNT(ID) FROM telefonski_imenik");

        // Output results of query to screen
        results.next();
        System.out.println("Stevilo kontakov: " + results.getString(1));
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() {
        // TODO: I'm not sure what this is suppose to do...
        //  Is this suppose to take the input from a SQL query and save it to disk with the extension .ser? Ok, let's try that :)

        // TODO: 1. Query: SELECT *  FROM telefonski_imenik;
        // TODO: 2. Serialize the query into the Kontakt object and then to the List<Kontakt> seznamKontaktov


    }

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() {
        // TODO: So this is related to serializirajSeznamKontaktov(), so we have to accept the .ser file and deserialize it to the arraylist?


        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izvozi seznam kontakov CSV datoteko.
     * Naj uporabnik sam izbere ime izhodne datoteke.
     */
    public void izvoziPodatkeVCsvDatoteko() {
        System.out.println("Metoda še ni implementirana");
    }

    private void executeUpdateStatement(PreparedStatement preparedStatement) {
        try{
            preparedStatement.executeUpdate();
            System.out.println("Operacija je bila uspešno izvedena!");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Operacija ni bila izvedena!");
        }
    }

    private void printResults(ResultSet results) throws SQLException {
        // This isn't exactly ok,... output will look messy if a certain row is large,
        // can be a bit hacky here and do some tabulators but It's best to stick to a format
        System.out.println("ID, Ime, Priimek, Naslov, Email, Telefon, Mobilni telefon, Opomba");

        while(results.next()){
            System.out.println(results.getString("ID")
                    + ", "
                    + results.getString("Ime")
                    + ", "
                    + results.getString("Priimek")
                    + ", "
                    + results.getString("Naslov")
                    + ", "
                    + results.getString("Email")
                    + ", "
                    + results.getString("Telefon")
                    + ", "
                    + results.getString("Mobilni_telefon")
                    + ", "
                    + results.getString("Opomba")
            );
        }
    }

    // When updating a row by ID it's possible that the row doesn't exist
    private Boolean checkIfIDexists(int inputId) throws SQLException {
        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("SELECT COUNT(?) FROM telefonski_imenik WHERE ID=?");

        preparedStatement.setInt(1, inputId);
        preparedStatement.setInt(2, inputId);

        ResultSet results = preparedStatement.executeQuery();

        results.next();

        int count = Integer.parseInt(results.getString(1));
        return count > 0;

    }
}
