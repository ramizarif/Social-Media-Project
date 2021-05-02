import java.util.ArrayList;

public class Account {

    public String username;
    public String password;
    public ArrayList<String> contacts;
    public ArrayList<String> friendsList;
    public ArrayList<String> likes;
    public String aboutMe;
    public ArrayList<String> friendReqs;


    public Account(String username, String password) {

        this.username = username;
        this.password = password;
        this.contacts = new ArrayList<String>();
        this.friendsList = new ArrayList<String>();
        this.likes = new ArrayList<String>();
        this.aboutMe = "";
        this.friendReqs = new ArrayList<String>();

    }

    public Account(String username, String password, ArrayList<String> contacts, ArrayList<String> friendsList, ArrayList<String> likes, String aboutMe, ArrayList<String> friendReqs) {

        this.username = username;
        this.password = password;
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
            if (o.getUsername() == friendsList.get(i)) {
                return true; 
            } 
        }
        for (int k = 0; k < o.getFriendsList().size(); k++) {
            if (username == o.getFriendsList().get(k)); 
            return true; 
        }
        return false; 
    }
}
