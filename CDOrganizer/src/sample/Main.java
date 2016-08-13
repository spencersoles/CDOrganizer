package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.soap.Node;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * User has chose to choose between existing file or create a new one
 * Stores each current and new data entry in a linked list, ordering alphabetically and by year
 * Gives user option to save changes to chosen file
 */

public class Main {

    public static void main(String[] args)  throws FileNotFoundException{

        CDOrganizer organizer = new CDOrganizer();// initialize linked list and record keeping logic
        Scanner scan = new Scanner(System.in);
        StringBuilder genreFile = new StringBuilder();//holds all lines of pre chosen file

        Scanner genreReader = new Scanner(new File("genre.txt"));//file that holds all genre options

        boolean hasChanged = true;// keeps track if txt file has been altered

        while(genreReader.hasNext()){//populates string of genre options
            genreFile.append(genreReader.nextLine());
            genreFile.append("\n");
        }

        int choice = 1;

        File saveFile= fileExist(scan, organizer);//checks file existence and if not then creates new one under name

        while(choice !=9) {//prompt which gives user options to select from
            System.out.println("CD Organizer -- Enter your choice");
            System.out.println("1. Enter a New CD");
            System.out.println("2. View all CDs");
            System.out.println("3. Search for a CD");
            System.out.println("4. Delete a CD");
            System.out.println("5. Save CD collection to disk");
            System.out.println("9. Exit the program");
            choice = scan.nextInt();
            scan.nextLine();// so the next scanner does not skip
            if(choice == 1){//create a CD
                newCD(scan, organizer, genreFile.toString());
                hasChanged=true;
            }if(choice == 2){//view all CD's
                viewAll(organizer);
            }if(choice == 3){//search for a CD
                search(organizer, scan);
            }if(choice == 4){
                delete(organizer, scan);//delete
                hasChanged=true;
            }if(choice == 5){
                save(organizer, saveFile);//save
                hasChanged=false;
            }
        }


        if(hasChanged){//option to save changes to file
            System.out.println("**WARNING**  Your CD collection has changed since you last saved to disk., Do you want to save? (y/n)");
            String lastSave = scan.nextLine().toUpperCase();
            if(lastSave.equals("Y")){
                save(organizer, saveFile);
            }
        }


        scan.close();//close main scanner
        genreReader.close();
    }

    /**
     *Checks to see if the file exists, if it does not then it creates a new file to save to
     * @param scan
     * @param organizer
     */
    public static File fileExist(Scanner scan, CDOrganizer organizer){//called at beggining when filename is asked for
        System.out.println("Please enter the filename for your CD storage collection. If you do not yet have a collection, enter a filename where you wish your CDs to be stored.");
        System.out.println("Filename: " );
        String fileName=scan.nextLine();
        File cdfile = new File(fileName);

        try{
            Scanner fileContents= new Scanner(cdfile);
            //fileContents.nextLine();

            while(fileContents.hasNext()){
                String [] data = fileContents.nextLine().split("_");//splitter
                organizer.addRecord(data[0].toUpperCase(), data[1].toUpperCase(), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            }
            organizer.retrieveAll();
        }catch(FileNotFoundException e){

            System.out.println("There are no CD's in this record");

        }
        return cdfile;
    }
    /**
     *Calls add record and allows user to input data for record
     * @param scan
     * @param organizer
     * @param genreFile
     */
    public static void newCD(Scanner scan, CDOrganizer organizer, String genreFile) {
        //asks user for all data needed for each record

        System.out.println("Enter the artist: ");
        String artist = scan.nextLine();

        System.out.println("Enter the CD title: ");
        String title = scan.nextLine();

        System.out.println("Enter the year of the CD: ");
        int year = scan.nextInt();

        System.out.println(genreFile);

        System.out.println("Enter the genre number: ");
        int genre = scan.nextInt();

        organizer.addRecord(artist.toUpperCase(), title.toUpperCase(), year, genre);//calls add record in CDOrganizer

        scan.nextLine();// so the next scanner does not skip
    }

/**
 * allows user to view all CD's saved in the organizer, calls retrieveAll from CDOrganizer
 */
    public static void viewAll(CDOrganizer organizer) {
        System.out.println(organizer.retrieveAll());
    }

    /**
     * Searches through list to find the entries that match either a genre or artist
     * @param organizer
     * @param scan
     */
    public static void search(CDOrganizer organizer, Scanner scan) {
        int artistOrGenre;
        String artistSearch=" ";
        int genreSearch=-1;

        System.out.println("Search by (1) Artist or (2) Genre? ");
        artistOrGenre = scan.nextInt();
        scan.nextLine();
        if(artistOrGenre == 1){
            System.out.println("Enter Artist (all or partial name): ");
            artistSearch = scan.nextLine();
        }else if(artistOrGenre ==2){
            System.out.println("Enter Genre number for which to search: ");
            genreSearch = scan.nextInt();
        }

        System.out.println("Artist Title Genre Year");
        System.out.println(organizer.search(artistSearch.toUpperCase(), genreSearch));


    }

    /**
     * Deletes an entry, calls the deleteEntry function of CDOrganizer, allowing the user to input title and artist
     * @param organizer
     * @param scan
     */
    public static void delete(CDOrganizer organizer, Scanner scan){
        String artist;
        String title;
        System.out.println("Enter the title and artist of the CD to delete");
        System.out.println("Artist: ");
        artist=scan.nextLine();
        System.out.println("Title: ");
        title=scan.nextLine();
        organizer.deleteEntry(artist.toUpperCase(), title.toUpperCase());
    }

    /**
     * Saves the list to a file, calls saveTo
     * @param organizer
     * @param saveFile
     * @throws FileNotFoundException
     */
    public static void save(CDOrganizer organizer, File saveFile)throws FileNotFoundException{
        organizer.saveTo(saveFile);
    }


}
