package sample;

import javax.xml.soap.Node;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Spencer on 9/16/2015.
 *
 * Stores CD's records in alphabetical order and by year in a Linked List.
 *
 * Allows user to either create a new file to save entries or start from a currnetly stored list
 *
 * Once record entry is done, option is given to save entries on text file
 */


public class CDOrganizer{

    private CDNode head = null;

    /**
     * adds a record to the correct position in the list
     * @param artist
     * @param title
     * @param year
     * @param genre
     */
   public void addRecord(String artist, String title, int year, int genre){
        CDNode ref = head;

       if(head == null || artist.compareToIgnoreCase(head.artist)<0){//if first record in the list, then add at beginning

           head = new CDNode(artist, title, year, genre, head);

       }else {
           //while statement orders records by artist and year
           while (ref.next != null  && (artist.compareToIgnoreCase(ref.next.artist)>0 || (artist.compareToIgnoreCase(ref.next.artist)>0 && year> ref.next.year))){
               ref = ref.next;
           }

           ref.next = new CDNode(artist, title, year, genre, ref.next);//add entry at end of list
       }
   }

    /**
     * Retrieves all the data that is being kept in the list, basically a toString
     * @return
     */
   public String retrieveAll(){
       StringBuilder creator = new StringBuilder();

       creator.append("Artist Title Genre Year");//header
       creator.append("\n");

       CDNode ref = head;
       while(ref != null){

           creator.append(ref.artist +" "+ ref.title+ " "+ ref.genre + " " + ref.year);
           creator.append("\n");

           ref = ref.next;
       }
       return creator.toString();
   }

    /**
     * searches list for genre or artist to find the entries that match
     * @param artistSearch
     * @param genreSearch
     * @return
     */
   public String search(String artistSearch, int genreSearch){

       StringBuilder creator = new StringBuilder();

       CDNode ref = head;
       int count=0;

       if(genreSearch<0) {//search for entries by artist

           while(ref != null){

                   if (ref.artist.startsWith(artistSearch)){

                       creator.append(ref.artist +" "+ ref.title+ " "+ ref.genre + " " + ref.year);
                       creator.append("\n");
                       count++;
                   }

               ref = ref.next;
           }


       }else if(artistSearch.equals(" ")){//search for artist by genre

           while(ref != null){
                if(genreSearch == ref.genre){
                    creator.append(ref.artist +" "+ ref.title+ " "+ ref.genre + " " + ref.year);
                    creator.append("\n");
                    count++;
                }
               ref = ref.next;
           }

       }

       creator.append(" Total amount of entries found " + count);
       return creator.toString();


   }

    /**
     * deletes an entry matching the correct title and artist
     * @param artistSearch
     * @param titleSearch
     */
    public void deleteEntry(String artistSearch, String titleSearch){
        CDNode ref = head;

        if(head.title.equals(titleSearch) && head.artist.equals(artistSearch)){//if first entry then cut off head

                head=head.next;

        }else {

            try {
                while (!ref.next.artist.equals(artistSearch) || !ref.next.title.equals(titleSearch)) {//needs to match both to delete
                    ref = ref.next;
                }

                ref.next = ref.next.next;
            } catch (NullPointerException e) {//prevents null pointer at end of list if criteria not found
                System.out.println(artistSearch + "/" + titleSearch + " was not found in the list**");
            }
        }
            System.out.println(retrieveAll());// shows all entries at end to show that correct one was deleted
    }

    /**
     * creates the string that needs to be saved to the file
     * holds string in a string builder
     * @return
     */

    public String saveStringBuilder(){
        StringBuilder creator = new StringBuilder();

        CDNode ref = head;
        while(ref != null){

            creator.append(ref.artist +"_"+ ref.title+ "_"+ ref.genre + "_" + ref.year);
            creator.append("\n");

            ref = ref.next;
        }
        return creator.toString();
    }

    /**
     * Saves the correct string to the file
     * @param saveFile - file to save too
     * @throws FileNotFoundException
     */
    public void saveTo(File saveFile) throws FileNotFoundException {
        System.out.println("**Save Successful**");

        PrintWriter saver = new PrintWriter(saveFile);

        saver.println(saveStringBuilder());

        saver.close();

    }

    /**
     * Inner class which creates each individual node for the linked list
     */
    private class CDNode{
        String artist;
        String title;
        int genre;
        int year;
        CDNode next;

        /**
         * constructor to populate all necessary data fields of each node of list
         * @param artist
         * @param title
         * @param year
         * @param genre
         * @param next
         */

        public CDNode(String artist, String title, int year, int genre, CDNode next){
            this.artist = artist;
            this.year = year;
            this.title = title;
            this.genre = genre;
            this.next = next;
        }

    }

}
