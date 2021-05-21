package si.src.naloga.imenik;

import si.src.naloga.kontakt.Kontakt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void izpisiVseKontakte(Statement statement) throws SQLException {

        // Since there is no way for the user to control this string, I see no point in adding some SQL Injection
        // Prevention here...
        ResultSet results = statement.executeQuery("SELECT * FROM telefonski_imenik ORDER BY id");

        // Output results of query to screen
        System.out.println("ID, Ime, Priimek");
        while(results.next()){
            System.out.println(results.getString("ID")
                    + ", "
                    + results.getString("Ime")
                    + ", "
                    + results.getString("Priimek"));
        }
    }

    /**
     * Metaoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta
     */
    public void dodajKontakt(Statement statement) throws SQLException {
        // TODO: We can prevent duplicate entries by adding the unique constraint:
        //  alter table telefonski_imenik add unique unique_first_and_last_name(Ime, Priimek)
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vnesite podatke o kontaku:");

        System.out.println("Ime: ");
        String inputIme = scanner.nextLine();

        System.out.println("Priimek: ");
        String inputPriimek = scanner.nextLine();

        // TODO: Too lazy to prevent SQL injection right now... but this seems pretty safe? AFAIK... Will take a look at parametized queries WE GOT THIS IN THE BAG YO!
        try{
            statement.executeUpdate(String.format("INSERT INTO telefonski_imenik (Ime, Priimek) VALUES ('%s', '%s')",
                    inputIme, inputPriimek));
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     */
    public void urediKontakt() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId() {
        System.out.println("Metoda še ni implementirana");
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
}
