package si.src.naloga;

import si.src.naloga.imenik.TelefonskiImenik;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        // TODO: Okay so we have to make a CRUD Database app, well the first thing we should do is check if a
        //  connection to the database is even possible.
        //  If it is possible, then connect and proceed with the program,
        //  but if the connection isn't possible read the contents of the SQL base from a file

        try {
            // Checking if connection to database is possible
           DriverManager.getConnection("jdbc:mysql://localhost:3306/telefonski_imenik",
                    "root", "wearenumberone");
            System.out.println("Connection to database was successful!");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection to database was not successful!");
            System.out.println("Reading contents of database from file...");
        }

        TelefonskiImenik telefonskiImenik = new TelefonskiImenik();

        izpisiMenu();

        Scanner in = new Scanner(System.in);
        String akcija = "";

        // zanka za izris menija
        while (!"0".equals(akcija)) {
            akcija = in.next();

            switch (akcija) {
                case "1":
                    telefonskiImenik.izpisiVseKontakte();
                    break;
                case "2":
                    telefonskiImenik.dodajKontakt();
                    break;
                case "3":
                    telefonskiImenik.urediKontakt();
                    break;
                case "4":
                    telefonskiImenik.izbrisiKontaktPoId();
                    break;
                case "5":
                    telefonskiImenik.izpisiKontaktZaId();
                    break;
                case "6":
                    telefonskiImenik.izpisiSteviloKontaktov();
                    break;
                case "7":
                    telefonskiImenik.serializirajSeznamKontaktov();
                    break;
                case "8":
                    telefonskiImenik.naloziSerializiranSeznamKontakotv();
                    break;
                case "9":
                    telefonskiImenik.izvoziPodatkeVCsvDatoteko();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Napačna izbira!");
                    break;
            }

            izpisiMenu();
        }
    }

    public static Connection grabConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/telefonski_imenik",
                "root", "wearenumberone");
    }

    /**
     * Uporabniku izpišemo menu
     */
    public static void izpisiMenu() {

        System.out.println("");
        System.out.println("");
        System.out.println("Aplikacija telefonski imenik:");
        System.out.println("-----------------------------------");
        System.out.println("Akcije:");
        System.out.println("1 - izpiši vse kontakte v imeniku");
        System.out.println("2 - dodaj kontakt v imenik");
        System.out.println("3 - uredi obstoječi kontakt");
        System.out.println("4 - briši kontakt po ID-ju");
        System.out.println("5 - izpiši kontakt po ID-ju");
        System.out.println("6 - izpiši število vseh kontaktov");
        System.out.println("7 - Shrani kontakte na disk (serializacija)");
        System.out.println("8 - Preberi kontake iz serializirano datoteke");
        System.out.println("9 - Izvozi kontakte v csv");
        System.out.println("");
        System.out.println("0 - Izhod iz aplikacije");
        System.out.println("-----------------------------------");
        System.out.println("Akcija: ");


    }
}

/*
try{
        // Attempting to connect to database
        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/telefonski_imenik","root","wearenumberone");

        // Creating SQL statement
        Statement testStatement=connection.createStatement();

        // Executing the SQL statement
        ResultSet response=testStatement.executeQuery(selectStatement);

        // Showing results of SQL statement
        while(response.next()){
        System.out.println(response.getString("Ime")+", "+response.getString("Priimek"));
        }
        }
        catch(SQLException throwables){
        throwables.printStackTrace();
        System.out.println("Neuspšena povezava do baze podatkov!... Branje podatkov iz datoteke...");
        }*/
