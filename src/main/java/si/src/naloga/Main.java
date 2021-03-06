package si.src.naloga;

import si.src.naloga.imenik.TelefonskiImenik;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static boolean customDatabase = false;

    public final static String defaultUrlBaze = "jdbc:mysql://localhost:3306/telefonski_imenik";
    public final static String defaultUporabniskoImeBaze = "root";
    public final static String defaultGesloBaze = "wearenumberone";

    public static String customUrlBaze = "";
    public static String customUporabniskoImeBaze = "";
    public static String customGesloBaze = "";

    public static void main(String[] args) throws SQLException, IOException {
        Scanner in = new Scanner(System.in);
        TelefonskiImenik telefonskiImenik = new TelefonskiImenik();

        izborPodatkovneBaze();
        preverjanjePovezaveDoBaze(telefonskiImenik);
        izpisiMenu();

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
                case "A":
                    telefonskiImenik.iskanjePodatkovPoImenuAliPriimku();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Napa??na izbira!");
                    break;
            }

            izpisiMenu();
        }
    }

    // ??e je customDatabase == true, potem uporabnik vnese svoje podatke za dostop do baze, v nasprotnem primeru
    // se uporabljajo default vrednosti (Moje lokalno okolje)
    public static Connection grabConnection() throws SQLException {
        if(customDatabase){
            return DriverManager.getConnection(customUrlBaze, customUporabniskoImeBaze, customGesloBaze);
        }else{
            return DriverManager.getConnection(defaultUrlBaze, defaultUporabniskoImeBaze, defaultGesloBaze);
        }
    }

        /**
         * Uporabniku izpi??emo menu
         */
    public static void izpisiMenu() {
        System.out.println("");
        System.out.println("Aplikacija telefonski imenik:");
        System.out.println("-----------------------------------");
        System.out.println("Akcije:");
        System.out.println("1 - izpi??i vse kontakte v imeniku");
        System.out.println("2 - dodaj kontakt v imenik");
        System.out.println("3 - uredi obstoje??i kontakt");
        System.out.println("4 - bri??i kontakt po ID-ju");
        System.out.println("5 - izpi??i kontakt po ID-ju");
        System.out.println("6 - izpi??i ??tevilo vseh kontaktov");
        System.out.println("7 - Shrani kontakte na disk (serializacija)");
        System.out.println("8 - Preberi kontake iz serializirano datoteke");
        System.out.println("9 - Izvozi kontakte v csv");
        System.out.println("A - Iskanje kontaktov po imenu ali priimku");
        System.out.println("");
        System.out.println("0 - Izhod iz aplikacije");
        System.out.println("-----------------------------------");
        System.out.println("Akcija: ");
    }

    private static void izborPodatkovneBaze() {
        Scanner in = new Scanner(System.in);
        System.out.println("0 - Pove??i na default podatkvno bazo");
        System.out.println("1 - Pove??i na custom podatkovno bazo");
        String kateraBaza = in.nextLine();

        if(kateraBaza.equals("0")){
            customDatabase = false;
        }else{
            customDatabase = true;
            System.out.println("Vnesite url va??e baze: ");
            customUrlBaze = in.nextLine();

            System.out.println("Vnesite uporabni??ko ime va??e baze: ");
            customUporabniskoImeBaze = in.nextLine();

            System.out.println("Vnesite geslo va??e baze: ");
            customGesloBaze = in.nextLine();
        }
    }

    private static void preverjanjePovezaveDoBaze(TelefonskiImenik telefonskiImenik) throws SQLException {
        try {
            // Preverjanje ??e je povezava do baze mozna
            grabConnection();
            System.out.println("Povezava do baze je bila uspe??na!");
        }
        catch (SQLException e) {
            // ??e povezava do baze ni mozna, potem pokazi sporo??ilo da ni mozno in preberi podatke iz Kontakt.ser
            System.out.println("Povezava do baze je bila neuspe??na!");
            System.out.println("Branje podatkov iz datoteke Kontakt.ser");
            telefonskiImenik.naloziSerializiranSeznamKontakotv();
        }
    }
}