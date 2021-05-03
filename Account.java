import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.Buffer;; 

public class Account {

    public String username;
    public String password;
    public ArrayList<String> contacts;
    public ArrayList<String> friendsList;
    public ArrayList<String> likes;
    public String aboutMe;
    public ArrayList<String> friendReqs;
    public String picName;


    public Account(String username, String password) {

        this.username = username;
        this.password = password;
        this.picName = "";
        this.contacts = new ArrayList<String>();
        this.friendsList = new ArrayList<String>();
        this.likes = new ArrayList<String>();
        this.aboutMe = "";
        this.friendReqs = new ArrayList<String>();

    }

    public Account(String username, String password, String picName, ArrayList<String> contacts, ArrayList<String> friendsList, ArrayList<String> likes, String aboutMe, ArrayList<String> friendReqs) {

        this.username = username;
        this.password = password;
        this.picName = picName;
        this.contacts = contacts;
        this.friendsList = friendsList;
        this.likes = likes;
        this.aboutMe = aboutMe;
        this.friendReqs = friendReqs;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public ArrayList<String> getFriendReqs() {
        return friendReqs;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    
    public String getPicName() {
        return picName;
    }
    
    public void setPicName(String picName) {
        this.picName = picName; 
    }

    public void addFriend(String name) {
        this.friendsList.add(name);
    }

    public void addLike(String like) {
        this.likes.add(like);
    }

    public void removeFriend(String name) {
        this.friendsList.remove(this.friendsList.indexOf(name));
    }

    public void removeLike(String like) {
        this.likes.remove(this.likes.indexOf(like));
    }

    public void addFriendReq(String name) {
        this.friendReqs.add(name);
    }

    public void removeFriendReq(String name) {
        this.friendReqs.remove(name);
    }

    public void addContact(String contact) {
        this.contacts.add(contact);
    }

    public void removeContact(String contact) {
        this.contacts.remove(contact);
    }

    public String toString() {
        return this.username + "," + this.password + "," + this.contacts + "," + this.friendsList + "," + this.likes + "," + this.aboutMe + "," + this.friendReqs;

    }

    public boolean isFriend(Account o) {
        for (int i = 0; i < friendsList.size(); i++) {
            if (o.getUsername().equals(friendsList.get(i))) {
                System.out.println(o.getUsername() + "and" + friendsList.get(i));
                return true; 
            } 
        }
        return false; 
    }
    public void makePrivate(String name) throws FileNotFoundException {
        File f = new File("privateaccs.txt");
        FileOutputStream fos = new FileOutputStream(f,true); 
        PrintWriter pw = new PrintWriter(fos);  

        pw.println(name);
        pw.close();
        
    }

    public void makePublic(String name) throws FileNotFoundException, IOException {
        File f = new File("privateaccs.txt");
        File f2 = new File("tempPriv.txt");
        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);
        FileOutputStream fos = new FileOutputStream(f2,true); 
        PrintWriter pw = new PrintWriter(fos);

        String readLine = bfr.readLine();
        while(readLine != null) {
            if (readLine.equals(name)) {

            } else {
                pw.println(readLine);
            }
            
            readLine = bfr.readLine();
        }
        
        bfr.close();
        pw.close();
        boolean y = f.delete();
        boolean x = f2.renameTo(new File("privateaccs.txt"));
    }

    public boolean isPrivate(Account a) throws IOException {
        File f = new File("privateaccs.txt");
        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);
        String readLine = bfr.readLine();
        while(readLine != null) {
            if (readLine.equals(a.getUsername())) {
                return true; 
            }
            readLine = bfr.readLine();
        }

        return false; 
        

    }

}
