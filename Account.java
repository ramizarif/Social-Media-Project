import java.util.ArrayList;

public class Account {

    public String username;
    public String password;
    public ArrayList<String> friendsList;
    public ArrayList<String> likes;
    public String aboutMe;

    public Account(String username, String password) {

        this.username = username;
        this.password = password;
        this.friendsList = null;
        this.likes = null;
        this.aboutMe = null;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriendsList(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
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

    public String toString() {

        return this.username + "," + this.password + "," + this.friendsList + "," + this.likes + "," + this.aboutMe;

    }
}
