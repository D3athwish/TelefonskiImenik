package si.src.naloga.imenik;

import si.src.naloga.Main;
import si.src.naloga.kontakt.Kontakt;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        // Pridobimo vse vnose iz baze
        // Ne rabimo preparedStatement, ker nimamo uporabnikovega vnosa
        ResultSet results = Main
                .grabConnection()
                .createStatement()
                .executeQuery("SELECT * FROM telefonski_imenik ORDER BY id");

        // izpisi rezultat
        printResults(results);
    }

    /**
     * Metaoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta
     */
    public void dodajKontakt() throws SQLException {
        // Za preprečevanje duplikatov v bazi sem uporabil sledeči constraint:
        // alter table telefonski_imenik add unique unique_kontakt(Ime, Priimek, Naslov, Email, Telefon, Mobilni_telefon, Opomba);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Vnesite podatke o kontaku:");

        // Ko exportamo v .CSV je mozen bug: Če uporabimo vejico kot ločitelj med celicami, bomo pokvarili export.
        // Zato potrebujemo replace
        System.out.println("Ime: ");
        String inputIme = scanner.nextLine().replace(",", "");

        System.out.println("Priimek: ");
        String inputPriimek = scanner.nextLine().replace(",", "");

        System.out.println("Naslov: ");
        String inputNaslov = scanner.nextLine().replace(",", "");

        System.out.println("Email: ");
        String inputEmail = scanner.nextLine().replace(",", "");

        System.out.println("Telefon: ");
        String inputTelefon = scanner.nextLine().replace(",", "");

        System.out.println("Mobilni telefon: ");
        String inputMobilni_telefon = scanner.nextLine().replace(",", "");

        System.out.println("Opomba: ");
        String inputOpomba = scanner.nextLine().replace(",", "");


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
        // V navodilih naloga pravi, da ne smemo urediti IDja, torej pri urejanju vnosov ne omogočamo tega
        Scanner scanner = new Scanner(System.in);

        System.out.println("Kateri vnos bi radi posodbili?(ID)");
        int inputId = Integer.parseInt(scanner.nextLine());

        // Check if the ID we want to edit exists, if it does not cancel the operation and print message
        if(!checkIfIDexists(inputId)){
            System.out.println("Izbrani kontakt ne obstaja!");
            return;
        }

        System.out.println("Vnesite novo ime: ");
        String inputIme = scanner.nextLine().replace(",", "");

        System.out.println("Vnesite nov Priimek: ");
        String inputPriimek = scanner.nextLine().replace(",", "");

        System.out.println("Vnesite nov naslov: ");
        String inputNaslov = scanner.nextLine().replace(",", "");

        System.out.println("Vnesite nov Email: ");
        String inputEmail = scanner.nextLine().replace(",", "");

        System.out.println("Vnesite nov telefon: ");
        String inputTelefon = scanner.nextLine().replace(",", "");

        System.out.println("Vnesite nov mobilni telefon: ");
        String inputMobilni_telefon = scanner.nextLine().replace(",", "");

        System.out.println("Vnesite novo opombo: ");
        String inputOpomba = scanner.nextLine().replace(",", "");

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

        // Prikazi rezultat
        results.next();
        System.out.println("Stevilo kontakov: " + results.getString(1));
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() throws SQLException {
        // Najprej pridobimo vse vnose iz baze, sortirani so po IDju
        ResultSet results = Main
                .grabConnection()
                .createStatement()
                .executeQuery("SELECT * FROM telefonski_imenik ORDER BY id");

        // Loop skozi rezultate
        while(results.next()){
            // Vse parametre inicializramo na začasne spremenljivke
            int ID = results.getInt("ID");
            String ime = results.getString("Ime");
            String priimek = results.getString("Priimek");
            String naslov = results.getString("Naslov");
            String email = results.getString("Email");
            String telefon = results.getString("Telefon");
            String mobilniTelefon = results.getString("Mobilni_telefon");
            String opomba = results.getString("Opomba");

            // Kreiramo nov objekt in ga dodamo na ArrayList seznam kontaktov
            seznamKontaktov.add(new Kontakt(ID, ime, priimek, naslov, email, telefon, mobilniTelefon, opomba));
        }

        // Export seznamKontaktov v Kontakti.ser
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("Kontakti.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(seznamKontaktov);
            objectOutputStream.close();
            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() throws SQLException {
        // odstranimo vse obstoječe vnose iz seznamKontaktov, da je vedno List prazen pred dodajanjem novih
        seznamKontaktov.clear();
        // Serilizacija napolni seznamKontaktov nazaj
        serializirajSeznamKontaktov();

        // Branje iz datoteke Kontakti.ser
        try{
            FileInputStream fileInputStream = new FileInputStream("Kontakti.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            seznamKontaktov = (List<Kontakt>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }catch(IOException | ClassNotFoundException e){
            System.out.println("Datoteka Kontakti.ser verjetno ne obstaja...");
        }

        // Prikaz podatkov po deserializaciji
        System.out.println("ID, Ime, Priimek, Naslov, Email, Telefon, Mobilni telefon, Opomba");
        for (Kontakt kontakt : seznamKontaktov) {
            System.out.print(kontakt.getId() + ", ");
            System.out.print(kontakt.getIme() + ", ");
            System.out.print(kontakt.getPriimek() + ", ");
            System.out.print(kontakt.getNaslov() + ", ");
            System.out.print(kontakt.getEmail() + ", ");
            System.out.print(kontakt.getTelefon() + ", ");
            System.out.print(kontakt.getMobilniTelefon() + ", ");
            System.out.print(kontakt.getOpomba());

            // Nova vrstica za ločevanje vnosov
            System.out.println();
        }
    }

    /**
     * Izvozi seznam kontakov CSV datoteko.
     * Naj uporabnik sam izbere ime izhodne datoteke.
     */
    public void izvoziPodatkeVCsvDatoteko() throws IOException, SQLException {
        seznamKontaktov.clear();
        serializirajSeznamKontaktov();

        System.out.println("Vnesite ime datoteke: ");
        Scanner scanner = new Scanner(System.in);
        String imeDatoteke = scanner.nextLine();

        // V primeru če imamo znak, kateri ne sme biti v imenu datoteke to preprečimo in zahtevamo nov vnos
        while(imeDatoteke.matches(".*[\\\\/:*\"<>|].*")){
            System.out.println("Nedovoljeni znak, vnesite ime datoteke brez: \\, /, :, *, \", <, >, |");
            imeDatoteke = scanner.nextLine();
        }

        File file = new File(imeDatoteke);

        FileWriter fileWriter = new FileWriter(file + ".csv");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("ID, Ime, Priimek, Naslov, Email, Telefon, Mobilni Telefon, Opomba");
        bufferedWriter.newLine();

        for(Kontakt kontakt : seznamKontaktov){
            bufferedWriter.write(
                    kontakt.getId() + "," +
                    kontakt.getIme() + "," +
                    kontakt.getPriimek() + "," +
                    kontakt.getNaslov() + "," +
                    kontakt.getEmail() + "," +
                    kontakt.getTelefon() + "," +
                    kontakt.getMobilniTelefon() + "," +
                    kontakt.getOpomba()
            );
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();

        System.out.println("Operacija uspešno izvedena!");
    }

    public void iskanjePodatkovPoImenuAliPriimku() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Priimek ali ime(Vnesite P za priimek, I za ime): ");
        String ImeAliPriimek = scanner.nextLine();

        // Uporabnika pošljemo na določeno funkcijo glede na njegov vnos
        if(ImeAliPriimek.equals("I")){
            iskanjePodatkovPoImenu();
        }else if(ImeAliPriimek.equals("P")){
            iskanjePodatkovPoPriimku();
        }else{
            System.out.println("Nepravilni izbor!");
        }
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

    // Izpis rezultatov
    private void printResults(ResultSet results) throws SQLException {
        // Tak izpis sicer ni ok, zato ker ni pregleden saj so Stringi različnih velikosti
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

    // Ko posodabljamo ID, je moznost da ID ne obstaja
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

    private void iskanjePodatkovPoImenu() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Vnesite iskalni niz(Ime):");
        String iskalniNizIme = scanner.nextLine();

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("SELECT * FROM telefonski_imenik WHERE Ime LIKE ?");

        preparedStatement.setString(1, "%" + iskalniNizIme + "%");

        ResultSet results = preparedStatement.executeQuery();

        printResults(results);
    }

    private void iskanjePodatkovPoPriimku() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vnesite iskalni niz(Priimek):");
        String iskalniNizpriimek = scanner.nextLine();

        PreparedStatement preparedStatement = Main
                .grabConnection()
                .prepareStatement("SELECT * FROM telefonski_imenik WHERE Priimek LIKE ?");

        preparedStatement.setString(1, "%" + iskalniNizpriimek + "%");

        ResultSet results = preparedStatement.executeQuery();

        printResults(results);
    }
}
