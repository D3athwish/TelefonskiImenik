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
        //  We can prevent duplicate entries by adding the unique constraint:
        //  alter table telefonski_imenik add unique unique_first_and_last_name(Ime, Priimek)
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vnesite podatke o kontaku:");

        System.out.println("Ime: ");
        String inputIme = scanner.nextLine();

        System.out.println("Priimek: ");
        String inputPriimek = scanner.nextLine();

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("INSERT INTO telefonski_imenik (Ime, Priimek) VALUES (?, ?)");

        preparedStatement.setString(1, inputIme);
        preparedStatement.setString(2, inputPriimek);

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

        System.out.println("Vnesite novo ime:");
        String inputIme = scanner.nextLine();

        System.out.println("Vnesite nov priimek:");
        String inputPriimek = scanner.nextLine();

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("UPDATE telefonski_imenik SET Ime=?, Priimek=? WHERE ID=?");

        preparedStatement.setString(1, inputIme);
        preparedStatement.setString(2, inputPriimek);
        preparedStatement.setInt(3, inputId);

        executeUpdateStatement(preparedStatement);
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Kateri vnos bi radi izbrisali?(ID)");
        int inputId = Integer.parseInt(scanner.nextLine());

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
    public void izpisiSteviloKontaktov() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() {
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
            System.out.println("Statement execution successful!");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Statement execution failure!");
        }
    }

    private void printResults(ResultSet results) throws SQLException {
        System.out.println("ID\tIme\t\tPriimek");

        while(results.next()){
            System.out.println(results.getString("ID")
                    + "\t"
                    + results.getString("Ime")
                    + "\t"
                    + results.getString("Priimek"));
        }
    }
}
