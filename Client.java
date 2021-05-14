import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Client extends JComponent {

    static JLabel name;
    static JTextArea abMe;
    static JTextArea interests;
    static JLabel contact;
    static JButton fList;
    static JButton change;
    static JButton search;
    static JButton checkFriendRequests;
    static JButton del;
    static JButton privacy;

    //these buttons will be used when an account is searched
    static JButton home;
    static JButton addFriend;
    //this one will be when you open someone else's friends list and not yours
    static JButton otherFriendsList;

    //will allow the searched profile buttons to exist before used
    static boolean homeConditional;
    static boolean addFriendConditional;
    static boolean privAdded;

    static Client client;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

        Socket socket = new Socket("localhost", 4242);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        Account a;
        Account b;

        String[] options = {"Login", "Create New Account"};
        int result = JOptionPane.showOptionDialog(null, "Would you like to login or create a new account?", "Ready?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        boolean gui = true;
        boolean ender = false;

        if (result == JOptionPane.YES_OPTION) {

            String username;
            String password;
            boolean x = false;

            do {

                do {
                    username = JOptionPane.showInputDialog(null, "Enter Username",
                            "Login", JOptionPane.QUESTION_MESSAGE);

                    if (username == null) {
                        username = "break";
                        gui = false;
                        ender = true;
                        break;
                    }

                } while ((username == null) || (username.isBlank()));

                do {

                    if (ender) { password = "break"; break; }

                    password = JOptionPane.showInputDialog(null, "Enter Password",
                            "Login", JOptionPane.QUESTION_MESSAGE);

                    if (password == null) {
                        username = "break";
                        password = "break";
                        gui = false;
                        ender = true;
                        break;
                    }

                } while ((password == null) || (password.isBlank()));

                writer.write("A:" + username + "," + password);
                writer.println();
                writer.flush();

                String account = reader.readLine();

                if (account.substring(0, 2).equals("V:")) {

                    String temp = account.substring(2);
                    temp = temp.substring(temp.indexOf(",") + 1);
                    temp = temp.substring(temp.indexOf(",") + 1);

                    String pics;
                    if (temp.startsWith(",")) {
                        pics = "";
                    } else {
                        pics = temp.substring(0, temp.indexOf("[") - 1);
                    }

                    String con = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
                    String[] C = con.split(",");
                    ArrayList<String> c = new ArrayList<>();

                    for (String t : C) {

                        if (t.indexOf(" ") == 0) {
                            c.add(t.substring(1));
                        } else {
                            c.add(t);
                        }
                    }

                    temp = temp.substring(temp.indexOf("]") + 3);
                    String fren = temp.substring(0, temp.indexOf("]"));
                    String[] F = fren.split(",");
                    ArrayList<String> f = new ArrayList<>();

                    for (String t : F) {

                        if (t.indexOf(" ") == 0) {
                            f.add(t.substring(1));
                        } else {
                            f.add(t);
                        }
                    }

                    if (f.get(0).equals("")) {
                        f.remove(0);
                    }

                    temp = temp.substring(temp.indexOf("]") + 2);
                    String lik = temp.substring(1, temp.indexOf("]"));
                    String[] L = lik.split(",");
                    ArrayList<String> l = new ArrayList<>();

                    for (String t : L) {
                        l.add(t);
                    }

                    temp = temp.substring(temp.indexOf("]") + 2);
                    String ab = temp.substring(0, temp.indexOf("[") - 1);

                    temp = temp.substring(temp.indexOf("[") + 1);
                    String fq = temp.substring(0, temp.indexOf("]"));
                    String[] Q = fq.split(",");
                    ArrayList<String> q = new ArrayList<>();

                    for (String t : Q) {
                        q.add(t);
                    }

                    if (ab.equals(",")) {
                        ab = "";
                    }

                    a = new Account(username, password, pics, c, f, l, ab, q);
                    break;

                } else {

                    JOptionPane.showMessageDialog(null, "Incorrect Username or Password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } while (true);

        } else if (result == JOptionPane.NO_OPTION) {

            //Create a new account here
            String newUser;
            String newPass;

            do {
                newUser = JOptionPane.showInputDialog(null, "Enter A Username",
                        "Login", JOptionPane.QUESTION_MESSAGE);

                writer.write("C:" + newUser);
                writer.println();
                writer.flush();

                String check = reader.readLine();

                if (check.equals("Y")) {
                    break;
                }

            } while (true);

            do {
                newPass = JOptionPane.showInputDialog(null, "Enter A Password",
                        "Login", JOptionPane.QUESTION_MESSAGE);

            } while ((newPass == null) || (newPass.isBlank()));

            a = new Account(newUser, newPass);

            System.out.println(a.toString());

            writer.write("N:" + a.toString());
            writer.println();
            writer.flush();

        } else {
            gui = false;
            a = null;
        }

        //Show profile in complex gui with loop, maybe a run method
        if (gui) {

            JFrame frame = new JFrame("Profile");
            frame.setLayout(new GridLayout(2,2));
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            name = new JLabel(a.getUsername());
            interests = new JTextArea(a.getLikes().toString());
            abMe = new JTextArea(a.getAboutMe());
            contact = new JLabel(a.getContacts().toString());
            fList = new JButton("Friends List");
            change = new JButton("Change information");
            del = new JButton("Delete Profile");
            checkFriendRequests = new JButton("Friend Requests");
            search = new JButton("Search Profiles");
            privacy = new JButton("Privacy Settings");
            JButton exit = new JButton("My Profile");
            JButton ie = new JButton("Import/Export");
            JLabel settings = new JLabel("Settings:");
            JLabel iTitle = new JLabel("My Interests:");
            JLabel aTitle = new JLabel("About Me:");
            JLabel pic = new JLabel("");

            if (a.getPicName().length() > 0) {
                pic.setIcon(createPicture(a.getPicName()));
            }

            abMe.setColumns(23);
            abMe.setRows(9);
            abMe.setWrapStyleWord(true);
            abMe.setLineWrap(true);
            abMe.setEditable(false);

            interests.setColumns(23);
            interests.setRows(9);
            interests.setWrapStyleWord(true);
            interests.setLineWrap(true);
            interests.setEditable(false);

            JPanel tpanel = new JPanel();
            tpanel.add(name);
            tpanel.add(contact);
            tpanel.add(pic);
            frame.add(tpanel);

            JPanel rpanel = new JPanel();
            rpanel.add(iTitle);
            rpanel.add(interests);
            frame.add(rpanel);

            JPanel cpanel = new JPanel();
            cpanel.add(aTitle);
            cpanel.add(abMe, BorderLayout.SOUTH);
            frame.add(cpanel);

            JPanel dpanel = new JPanel();
            dpanel.add(settings);
            dpanel.add(ie);
            dpanel.add(fList);
            dpanel.add(change);
            dpanel.add(del);
            dpanel.add(exit);
            dpanel.add(checkFriendRequests);
            dpanel.add(search);
            dpanel.add(privacy);
            exit.setVisible(false);
            frame.add(dpanel);

            frame.setVisible(true);

            frame.setVisible(true);

            privacy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    boolean priv = false;

                    do {

                        int privDec = JOptionPane.showConfirmDialog(null, "Go Private? (Only Friends can view your account.)", "Privacy Settigns", JOptionPane.YES_NO_OPTION);
                        if (privDec == JOptionPane.YES_OPTION) {
                            try {
                                a.makePrivate(a.getUsername());
                            } catch (FileNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        } else {
                            try {
                                a.makePublic(a.getUsername());
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                        break;

                    } while (true);




                }
            });

            change.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    String[] standingOptions = {"Password", "Contacts", "Interests", "About Me", "Profile Picture"};
                    String standing = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                            "Change Information", JOptionPane.QUESTION_MESSAGE, null, standingOptions, standingOptions[0]);

                    if (standing != null) {

                        switch (standing) {

                            case "Password":

                                do {
                                    String newPass = JOptionPane.showInputDialog(null, "Enter A New Password",
                                            "Password Change", JOptionPane.QUESTION_MESSAGE);

                                    if (newPass != null) {

                                        String b7 = a.toString();
                                        a.setPassword(newPass);
                                        String a7 = a.toString();

                                        writer.write("D:" + b7 + ":" + a7);
                                        writer.println();
                                        writer.flush();

                                        break;

                                    }

                                } while (true);
                                break;

                            case "Contacts":

                                do {

                                    String[] options = {"Add", "Delete"};
                                    int result = JOptionPane.showOptionDialog(null, "Would you like to delete or add a contact?", "Contact Change",
                                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                            null, options, options[1]);

                                    if (result == JOptionPane.YES_OPTION) {

                                        do {

                                            String newCont = JOptionPane.showInputDialog(null, "Enter A New Contact (email, phones, etc)",
                                                    "Add Contact", JOptionPane.QUESTION_MESSAGE);
                                            if (newCont != null) {

                                                String b3 = a.toString();
                                                if (a.contacts.get(0).equals("")) {
                                                    a.contacts.remove(0);
                                                }
                                                a.addContact(newCont);
                                                String a3 = a.toString();

                                                writer.write("D:" + b3 + ":" + a3);
                                                writer.println();
                                                writer.flush();

                                                contact.setText(a.getContacts().toString());

                                                break;
                                            }
                                        } while (true);


                                    } else if (result == JOptionPane.NO_OPTION) {

                                        do {

                                            String newCont = JOptionPane.showInputDialog(null, "Enter A Contact to remove (email, phones, etc)",
                                                    "Remove Contact", JOptionPane.QUESTION_MESSAGE);

                                            if (newCont != null) {

                                                String b2 = a.toString();
                                                a.removeContact(newCont);
                                                String a2 = a.toString();

                                                writer.write("D:" + b2 + ":" + a2);
                                                writer.println();
                                                writer.flush();

                                                contact.setText(a.getContacts().toString());

                                                break;
                                            }
                                        } while (true);
                                    }
                                    break;

                                } while (true);
                                break;

                            case "Interests":

                                do {

                                    String[] options = {"Add", "Delete"};
                                    int result = JOptionPane.showOptionDialog(null, "Would you like to delete or add an Interest?", "Interest Change",
                                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                            null, options, options[1]);

                                    if (result == JOptionPane.YES_OPTION) {

                                        do {

                                            String newCont = JOptionPane.showInputDialog(null, "Enter A New Interest",
                                                    "Add Interest", JOptionPane.QUESTION_MESSAGE);
                                            if (newCont != null) {

                                                String b5 = a.toString();
                                                if (a.likes.get(0).equals("")) {
                                                    a.likes.remove(0);
                                                }
                                                a.addLike(newCont);
                                                String a5 = a.toString();

                                                writer.write("D:" + b5 + ":" + a5);
                                                writer.println();
                                                writer.flush();

                                                interests.setText(a.getLikes().toString());

                                                break;
                                            }
                                        } while (true);


                                    } else if (result == JOptionPane.NO_OPTION) {

                                        do {

                                            String newCont = JOptionPane.showInputDialog(null, "Enter an interest to remove",
                                                    "Remove Interest", JOptionPane.QUESTION_MESSAGE);
                                            if (newCont != null) {

                                                String b6 = a.toString();
                                                a.removeLike(newCont);
                                                String a6 = a.toString();

                                                a.removeLike(newCont);

                                                writer.write("D:" + b6 + ":" + a6);
                                                writer.println();
                                                writer.flush();

                                                interests.setText(a.getLikes().toString());
                                                break;
                                            }
                                        } while (true);
                                    }
                                    break;

                                } while (true);
                                break;

                            case "About Me":

                                String am = JOptionPane.showInputDialog(null, "Enter your new about me",
                                        "About Me Change", JOptionPane.QUESTION_MESSAGE);


                                if (am != null) {

                                    String bef = a.toString();
                                    a.setAboutMe(am);
                                    String af = a.toString();

                                    writer.write("D:" + bef + ":" + af);
                                    writer.println();
                                    writer.flush();

                                    abMe.setText(am);

                                    break;

                                }
                                break;

                            case "Profile Picture":

                                String profPic = JOptionPane.showInputDialog(null, "Enter the name of the png",
                                        "Change Profile Picture", JOptionPane.QUESTION_MESSAGE);

                                if (profPic != null) {

                                    String bef1 = a.toString();
                                    a.setPicName(profPic);
                                    String af1 = a.toString();

                                    writer.write("D:" + bef1 + ":" + af1);
                                    writer.println();
                                    writer.flush();

                                    pic.setIcon(createPicture(a.getPicName()));

                                    break;

                                }
                                break;

                        }
                    }

                }

            });

            del.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    int delete = JOptionPane.showConfirmDialog(null,
                            "Are you sure you like to delete your account?", "Delete?", JOptionPane.YES_NO_OPTION);

                    if (delete == JOptionPane.YES_OPTION) {

                        writer.write("D:" + a.toString());
                        writer.println();
                        writer.flush();

                        writer.write("d:" + a.getUsername());
                        writer.println();
                        writer.flush();

                        frame.dispose();
                    }

                }
            });

            fList.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    frame.setVisible(false);

                    ArrayList<String> l = a.getFriendsList();
                    ArrayList<JButton> b = new ArrayList<>();

                    if (l.size() != 0) {

                        JFrame f = new JFrame("Profile");
                        Container con = f.getContentPane();

                        con.setLayout(new BorderLayout());
                        client = new Client();
                        con.add(client, BorderLayout.CENTER);

                        f.setSize(600, 400);
                        f.setLocationRelativeTo(null);
                        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        f.setVisible(true);

                        JPanel panel = new JPanel();

                        for (int i = 0; i < l.size(); i++) {

                            JButton j = new JButton(l.get(i));
                            b.add(j);
                        }

                        for (int i = 0; i < b.size(); i++) {

                            panel.add(b.get(i));

                        }
                        con.add(panel, BorderLayout.CENTER);

                        for (int i = 0; i < b.size(); i++) {

                            String s = l.get(i);

                            b.get(i).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    frame.setVisible(true);

                                    f.dispose();
                                    fList.setVisible(false);
                                    change.setVisible(false);
                                    search.setVisible(false);
                                    settings.setVisible(false);
                                    ie.setVisible(false);
                                    del.setVisible(false);
                                    privacy.setVisible(false);


                                    checkFriendRequests.setVisible(false);

                                    //create new jlabels


                                    //JPanel bottom = new JPanel();
                                    //dpanel.add(otherFriendsList);
                                    frame.add(dpanel);


                                    writer.write("G:" + s);
                                    writer.println();
                                    writer.flush();

                                    String account = "";

                                    try {
                                        account = reader.readLine();
                                    } catch (IOException ioException) {
                                    }

                                    String temp = account.substring(2);
                                    temp = temp.substring(temp.indexOf(",") + 1);

                                    String p = temp.substring(0, temp.indexOf(","));

                                    temp = temp.substring(temp.indexOf(",") + 1);

                                    String cont = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
                                    String[] C = cont.split(",");
                                    ArrayList<String> c = new ArrayList<>();

                                    for (String t : C) {
                                        c.add(t);
                                    }

                                    temp = temp.substring(temp.indexOf("]") + 2);
                                    String fren = temp.substring(1, temp.indexOf("]"));
                                    String[] F = fren.split(",");
                                    ArrayList<String> f = new ArrayList<>();

                                    for (String t : F) {
                                        f.add(t);
                                    }

                                    temp = temp.substring(temp.indexOf("]") + 2);
                                    String lik = temp.substring(1, temp.indexOf("]"));
                                    String[] L = lik.split(",");
                                    ArrayList<String> l = new ArrayList<>();

                                    for (String t : L) {
                                        l.add(t);
                                    }

                                    temp = temp.substring(temp.indexOf("]") + 1);
                                    String ab = temp.substring(0, temp.indexOf("[") - 1);

                                    temp = temp.substring(temp.indexOf("[") + 1);
                                    String fq = temp.substring(0, temp.indexOf("]"));
                                    String[] Q = fq.split(",");
                                    ArrayList<String> q = new ArrayList<>();

                                    for (String t : Q) {
                                        q.add(t);
                                    }

                                    if (ab.equals(",")) {
                                        ab = "";
                                    }

                                    name.setText(s);
                                    contact.setText(c.toString());
                                    interests.setText(l.toString());
                                    abMe.setText(ab);

                                    exit.setVisible(true);


                                    exit.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                            name.setText(a.getUsername());
                                            contact.setText(a.getContacts().toString());
                                            interests.setText(a.getLikes().toString());
                                            abMe.setText(a.getAboutMe());
                                            fList.setVisible(true);
                                            change.setVisible(true);
                                            search.setVisible(true);
                                            settings.setVisible(true);
                                            checkFriendRequests.setVisible(true);
                                            ie.setVisible(true);
                                            del.setVisible(true);
                                            exit.setVisible(false);
                                            privacy.setVisible(true);

                                        }
                                    });

                                }
                            });

                        }

                    } else {

                        JOptionPane.showMessageDialog(null, "You have no friends :(",
                                "Error", JOptionPane.ERROR_MESSAGE);

                        frame.setVisible(true);

                    }
                }

            });

            frame.setVisible(true);

            checkFriendRequests.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String[] potentialFriends = new String [a.getFriendReqs().size()];
                    for (int i = 0; i < a.getFriendReqs().size(); i++) {
                        potentialFriends[i] = a.getFriendReqs().get(i);
                    }

                    ArrayList<String> tempList = new ArrayList<String>();
                    for (int m = 0; m < potentialFriends.length; m++) {
                        if (potentialFriends[m].equals("")) {

                        } else {
                            tempList.add(potentialFriends[m]);
                        }
                    }

                    String[] potFriends = new String [tempList.size()];
                    for (int p = 0; p < potFriends.length; p++) {
                        potFriends[p] = tempList.get(p);
                    }

                    if (potFriends[0].equals("")) {
                        do {
                            JOptionPane.showMessageDialog(null, "No Friend Requests", "Friend Requests", JOptionPane.YES_OPTION);
                            break;
                        } while (true);
                    } else {

                        String pendingFriends = (String) JOptionPane.showInputDialog(null, "Which requests would you like to accept/decline?",
                                "Friend Requests", JOptionPane.QUESTION_MESSAGE, null, potFriends, potFriends[0]);

                        if (pendingFriends != null || pendingFriends !=  "") {
                            String newFriendInfo = "";
                            boolean go = false;
                            do {

                                int friendChoice = JOptionPane.showConfirmDialog(null, "Would you like to be friends with " + pendingFriends + "?",
                                        "Friend Requests", JOptionPane.YES_NO_OPTION);

                                if (friendChoice == JOptionPane.YES_OPTION) {
                                    //add friend by:
                                    //removing them from friend requests
                                    //adding them to friend requests list and vice versa
                                    //



                                    a.addFriend(pendingFriends);
                                    a.removeFriendReq(pendingFriends);
                                    //construct the account for pending friends
                                    writer.write("Z:" + pendingFriends);
                                    writer.println();
                                    writer.flush();
                                    newFriendInfo = "";
                                    go = true;
                                    break;

                                } else if (friendChoice == JOptionPane.NO_OPTION) {
                                    a.removeFriendReq(pendingFriends);
                                    //delete name from file friend request
                                    writer.write("s:" + a.getUsername() + ":" + pendingFriends);
                                    writer.println();
                                    writer.flush();
                                    break;
                                }

                            } while (true);

                            if (go) {

                                System.out.println("Getting here");
                                try {
                                    newFriendInfo = reader.readLine();

                                } catch (Exception f) {
                                    System.out.println(f.getStackTrace());
                                }

                                String searchedAccString = newFriendInfo.substring(2);

                                System.out.println("This works");

                                //create code to get the actual lists of interests and conts

                                String[] searchedAccSplit = searchedAccString.split("]");

                                //acc name
                                String searchedName = searchedAccSplit[0].substring(0, searchedAccSplit[0].indexOf(","));

                                //acc contacts
                                ArrayList<String> searchedContacts = new ArrayList<String>();
                                String sc = searchedAccSplit[0].substring(searchedAccSplit[0].indexOf("["));
                                if (sc.indexOf(",") != -1) {
                                    String[] scSplit = sc.split(",");
                                    for (int z = 0; z < scSplit.length; z++) {
                                        searchedContacts.add(scSplit[z]);
                                    }
                                } else if (sc.indexOf(",") == -1 && sc.equals("") == false) {
                                    searchedContacts.add(sc);
                                }

                                //friends list
                                ArrayList<String> searchedFriends = new ArrayList<String>();
                                String sf = searchedAccSplit[1].substring(searchedAccSplit[1].indexOf("[") + 1);
                                if (sf.indexOf(",") != -1) {
                                    String[] sfSplit = sf.split(",");
                                    for (int j = 0; j < sfSplit.length; j++) {
                                        searchedFriends.add(sfSplit[j]);
                                    }
                                } else if (sf.indexOf(",") == -1 && sf.equals("") == false) {
                                    searchedFriends.add(sf);
                                }

                                //acc interests and likes
                                ArrayList<String> searchedInterests = new ArrayList<String>();
                                String si = searchedAccSplit[2].substring(searchedAccSplit[2].indexOf("[") + 1);
                                if (si.indexOf(",") != -1) {
                                    String[] siSplit = si.split(",");
                                    for (int n = 0; n < siSplit.length; n++) {
                                        searchedInterests.add(siSplit[n]);
                                    }
                                } else if (si.indexOf(",") == -1 && si.equals("") == false) {
                                    searchedInterests.add(si);
                                }

                                //about me
                                String searchedAboutMe = "";
                                String sa = searchedAccSplit[3];
                                String saInterest = sa.substring(sa.indexOf(","), sa.indexOf("["));
                                String sam;
                                if (saInterest.equals(",,") == false) {
                                    sam = sa.substring(sa.indexOf(",") + 1, sa.lastIndexOf(","));
                                }
                                sam = searchedAboutMe;

                                //searched acc friend req list
                                ArrayList<String> searchedFriendRequests = new ArrayList<String>();
                                String fr = searchedAccSplit[3].substring(searchedAccSplit[3].indexOf("[")+1);
                                if (fr.indexOf(",") != -1) {
                                    String[] frSplit = fr.split(",");
                                    for (int b = 0; b < frSplit.length; b++) {
                                        searchedFriendRequests.add(frSplit[b]);
                                    }
                                } else if (fr.indexOf(",") == 1 && fr.equals("") == false) {
                                    searchedFriendRequests.add(fr);
                                }

                                String[] splitter = searchedAccString.split(",");
                                String pics = splitter[2];

                                Account addedFriend = new Account(searchedName, "password", pics, searchedContacts, searchedFriends, searchedInterests, searchedAboutMe, searchedFriendRequests);
                                addedFriend.getFriendsList().add(a.getUsername());

                                //have to write their new found friendship in the acc.txt file and delete their friend request
                                String user = a.getUsername();
                                String userFriend = addedFriend.getUsername();
                                writer.write("Q:" + user + ":" + userFriend);
                                writer.println();
                                writer.flush();
                            }


                        }

                    }
                }
            });

            search.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String checkType;
                    do {
                        String searchName =  JOptionPane.showInputDialog(null, "Search Profile",
                                "Search", JOptionPane.QUESTION_MESSAGE);
                        String sendServer = ("Z:" + searchName);
                        writer.write(sendServer);
                        writer.println();
                        writer.flush();
                        checkType = "";

                        break;

                    } while(true);


                    try {
                        checkType = reader.readLine();
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "An error occurred. Try again.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }


                    String checkCommand = checkType.substring(0,2);
                    String searchedAccString = checkType.substring(2);

                    //create code to get the actual lists of interests and conts

                    String[] searchedAccSplit = searchedAccString.split("]");

                    //acc name
                    String searchedName = searchedAccSplit[0].substring(0, searchedAccSplit[0].indexOf(","));

                    //acc contacts
                    ArrayList<String> searchedContacts = new ArrayList<String>();
                    String sc = searchedAccSplit[0].substring(searchedAccSplit[0].indexOf("["));
                    if (sc.indexOf(",") != -1) {
                        String[] scSplit = sc.split(",");
                        for (int z = 0; z < scSplit.length; z++) {
                            searchedContacts.add(scSplit[z]);
                        }
                    } else if (sc.indexOf(",") == -1 && sc.equals("") == false) {
                        searchedContacts.add(sc);
                    }

                    //friends list
                    ArrayList<String> searchedFriends = new ArrayList<String>();
                    String sf = searchedAccSplit[1].substring(searchedAccSplit[1].indexOf("[") + 1);
                    if (sf.indexOf(",") != -1) {
                        String[] sfSplit = sf.split(",");
                        for (int j = 0; j < sfSplit.length; j++) {
                            searchedFriends.add(sfSplit[j]);
                        }
                    } else if (sf.indexOf(",") == -1 && sf.equals("") == false) {
                        searchedFriends.add(sf);
                    }

                    //acc interests and likes
                    ArrayList<String> searchedInterests = new ArrayList<String>();
                    String si = searchedAccSplit[2].substring(searchedAccSplit[2].indexOf("[") + 1);
                    if (si.indexOf(",") != -1) {
                        String[] siSplit = si.split(",");
                        for (int n = 0; n < siSplit.length; n++) {
                            searchedInterests.add(siSplit[n]);
                        }
                    } else if (si.indexOf(",") == -1 && si.equals("") == false) {
                        searchedInterests.add(si);
                    }

                    //about me
                    String searchedAboutMe = "";
                    String sa = searchedAccSplit[3];
                    String saInterest = sa.substring(sa.indexOf(","), sa.indexOf("["));
                    String sam;
                    if (saInterest.equals(",,") == false) {
                        sam = sa.substring(sa.indexOf(",") + 1, sa.lastIndexOf(","));
                    }
                    sam = searchedAboutMe;

                    //searched acc friend req list
                    ArrayList<String> searchedFriendRequests = new ArrayList<String>();
                    String fr = searchedAccSplit[3].substring(searchedAccSplit[3].indexOf("[")+1);
                    if (fr.indexOf(",") != -1) {
                        String[] frSplit = fr.split(",");
                        for (int b = 0; b < frSplit.length; b++) {
                            searchedFriendRequests.add(frSplit[b]);
                        }
                    } else if (fr.indexOf(",") == 1 && fr.equals("") == false) {
                        searchedFriendRequests.add(fr);
                    }
                    String[] splitter = searchedAccString.split(",");
                    String pics = splitter[2];
                    Account searchedAcc = new Account(searchedName, "password", pics, searchedContacts, searchedFriends, searchedInterests, searchedAboutMe, searchedFriendRequests);
                    try {
                        System.out.println(searchedAcc.isPrivate(searchedAcc) + "isfriend");
                    } catch (IOException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                    //will check if the two accs are friends
                    //this isnt working
                    //logic? or friends list not updating?
                    //searchedAcc.getFriendsList().add("tax7");
                    boolean isFriend = a.isFriend(searchedAcc);
                    if (isFriend) {
                        //show the account without the add friend button
                        //add back to home button
                        System.out.println(isFriend + a.getUsername() + searchedAcc.getUsername());

                        //setting the text for all the jlabels
                        name.setText(searchedAcc.getUsername());
                        abMe.setText(searchedAcc.getAboutMe());
                        interests.setText(searchedAcc.getLikes().toString());
                        contact.setText(searchedAcc.getContacts().toString());

                        //make all login profile jbuttons invisible
                        fList.setVisible(false);
                        change.setVisible(false);
                        search.setVisible(false);
                        settings.setVisible(false);
                        ie.setVisible(false);
                        del.setVisible(false);
                        privacy.setVisible(false);


                        checkFriendRequests.setVisible(false);

                        //create new jlabels
                        home = new JButton("Home");

                        //JPanel bottom = new JPanel();
                        //dpanel.add(otherFriendsList);
                        dpanel.add(home);
                        frame.add(dpanel);

                        //otherFriendsList.setVisible(true);
                        home.setVisible(true);

                        homeConditional = true;



                    } else
                        try {
                            if (isFriend == false && searchedAcc.isPrivate(searchedAcc) == false) {
                                System.out.println("what the: " + searchedAcc.isPrivate(searchedAcc));
                                //show the account with the add friend button, replace search
                                //add back to home button
                                name.setText(searchedAcc.getUsername());
                                abMe.setText(searchedAcc.getAboutMe());
                                interests.setText(searchedAcc.getLikes().toString());
                                contact.setText(searchedAcc.getContacts().toString());

                                //make all login profile jbuttons invisible
                                fList.setVisible(false);
                                change.setVisible(false);
                                search.setVisible(false);
                                settings.setVisible(false);
                                ie.setVisible(false);
                                del.setVisible(false);
                                privacy.setVisible(false);


                                checkFriendRequests.setVisible(false);

                                //create new jlabels
                                home = new JButton("Home");
                                addFriend = new JButton("Add Friend");

                                //JPanel bottom = new JPanel();
                                //dpanel.add(otherFriendsList);
                                dpanel.add(home);
                                dpanel.add(addFriend);
                                frame.add(dpanel);

                                //otherFriendsList.setVisible(true);
                                home.setVisible(true);
                                addFriendConditional = true;
                                if (addFriendConditional) {
                                    addFriend.setVisible(true);
                                }


                                homeConditional = true;


                            } else
                                try {
                                    if (isFriend == false && searchedAcc.isPrivate(searchedAcc) == true) {
                                        int addOrNo;
                                        System.out.println("LEts fucking go");
                                        //ask if they want to add this private account
                                        do {
                                            addOrNo = JOptionPane.showConfirmDialog(null, "This account is private. Send Friend Request?", "Private Account", JOptionPane.YES_NO_OPTION);

                                            break;
                                        } while (true);

                                        if (addOrNo == JOptionPane.YES_OPTION) {
                                            privAdded = true;
                                            addFriendConditional = true;
                                        }

                                    }
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                        } catch (HeadlessException | IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


                    public class Client extends JComponent {

                        static JLabel name;
                        static JTextArea abMe;
                        static JTextArea interests;
                        static JLabel contact;
                        static JButton fList;
                        static JButton change;
                        static JButton search;
                        static JButton checkFriendRequests;
                        static JButton del;
                        static JButton privacy;

                        //these buttons will be used when an account is searched
                        static JButton home;
                        static JButton addFriend;
                        //this one will be when you open someone else's friends list and not yours
                        static JButton otherFriendsList;

                        //will allow the searched profile buttons to exist before used
                        static boolean homeConditional;
                        static boolean addFriendConditional;
                        static boolean privAdded;

                        static Client client;

                        public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

                            Socket socket = new Socket("localhost", 4242);

                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter writer = new PrintWriter(socket.getOutputStream());
                            Account a;
                            Account b;

                            String[] options = {"Login", "Create New Account"};
                            int result = JOptionPane.showOptionDialog(null, "Would you like to login or create a new account?", "Ready?",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                    null, options, options[1]);
                            boolean gui = true;
                            boolean ender = false;

                            if (result == JOptionPane.YES_OPTION) {

                                String username;
                                String password;
                                boolean x = false;

                                do {

                                    do {
                                        username = JOptionPane.showInputDialog(null, "Enter Username",
                                                "Login", JOptionPane.QUESTION_MESSAGE);

                                        if (username == null) {
                                            username = "break";
                                            gui = false;
                                            ender = true;
                                            break;
                                        }

                                    } while ((username == null) || (username.isBlank()));

                                    do {

                                        if (ender) { password = "break"; break; }

                                        password = JOptionPane.showInputDialog(null, "Enter Password",
                                                "Login", JOptionPane.QUESTION_MESSAGE);

                                        if (password == null) {
                                            username = "break";
                                            password = "break";
                                            gui = false;
                                            ender = true;
                                            break;
                                        }

                                    } while ((password == null) || (password.isBlank()));

                                    writer.write("A:" + username + "," + password);
                                    writer.println();
                                    writer.flush();

                                    String account = reader.readLine();

                                    if (account.substring(0, 2).equals("V:")) {

                                        String temp = account.substring(2);
                                        temp = temp.substring(temp.indexOf(",") + 1);
                                        temp = temp.substring(temp.indexOf(",") + 1);

                                        String pics;
                                        if (temp.startsWith(",")) {
                                            pics = "";
                                        } else {
                                            pics = temp.substring(0, temp.indexOf("[") - 1);
                                        }

                                        String con = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
                                        String[] C = con.split(",");
                                        ArrayList<String> c = new ArrayList<>();

                                        for (String t : C) {

                                            if (t.indexOf(" ") == 0) {
                                                c.add(t.substring(1));
                                            } else {
                                                c.add(t);
                                            }
                                        }

                                        temp = temp.substring(temp.indexOf("]") + 3);
                                        String fren = temp.substring(0, temp.indexOf("]"));
                                        String[] F = fren.split(",");
                                        ArrayList<String> f = new ArrayList<>();

                                        for (String t : F) {

                                            if (t.indexOf(" ") == 0) {
                                                f.add(t.substring(1));
                                            } else {
                                                f.add(t);
                                            }
                                        }

                                        if (f.get(0).equals("")) {
                                            f.remove(0);
                                        }

                                        temp = temp.substring(temp.indexOf("]") + 2);
                                        String lik = temp.substring(1, temp.indexOf("]"));
                                        String[] L = lik.split(",");
                                        ArrayList<String> l = new ArrayList<>();

                                        for (String t : L) {
                                            l.add(t);
                                        }

                                        temp = temp.substring(temp.indexOf("]") + 2);
                                        String ab = temp.substring(0, temp.indexOf("[") - 1);

                                        temp = temp.substring(temp.indexOf("[") + 1);
                                        String fq = temp.substring(0, temp.indexOf("]"));
                                        String[] Q = fq.split(",");
                                        ArrayList<String> q = new ArrayList<>();

                                        for (String t : Q) {
                                            q.add(t);
                                        }

                                        if (ab.equals(",")) {
                                            ab = "";
                                        }

                                        a = new Account(username, password, pics, c, f, l, ab, q);
                                        break;

                                    } else {

                                        JOptionPane.showMessageDialog(null, "Incorrect Username or Password", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                                    }

                                } while (true);

                            } else if (result == JOptionPane.NO_OPTION) {

                                //Create a new account here
                                String newUser;
                                String newPass;

                                do {
                                    newUser = JOptionPane.showInputDialog(null, "Enter A Username",
                                            "Login", JOptionPane.QUESTION_MESSAGE);

                                    writer.write("C:" + newUser);
                                    writer.println();
                                    writer.flush();

                                    String check = reader.readLine();

                                    if (check.equals("Y")) {
                                        break;
                                    }

                                } while (true);

                                do {
                                    newPass = JOptionPane.showInputDialog(null, "Enter A Password",
                                            "Login", JOptionPane.QUESTION_MESSAGE);

                                } while ((newPass == null) || (newPass.isBlank()));

                                a = new Account(newUser, newPass);

                                System.out.println(a.toString());

                                writer.write("N:" + a.toString());
                                writer.println();
                                writer.flush();

                            } else {
                                gui = false;
                                a = null;
                            }

                            //Show profile in complex gui with loop, maybe a run method
                            if (gui) {

                                JFrame frame = new JFrame("Profile");
                                frame.setLayout(new GridLayout(2,2));
                                frame.setSize(600, 400);
                                frame.setLocationRelativeTo(null);
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                name = new JLabel(a.getUsername());
                                interests = new JTextArea(a.getLikes().toString());
                                abMe = new JTextArea(a.getAboutMe());
                                contact = new JLabel(a.getContacts().toString());
                                fList = new JButton("Friends List");
                                change = new JButton("Change information");
                                del = new JButton("Delete Profile");
                                checkFriendRequests = new JButton("Friend Requests");
                                search = new JButton("Search Profiles");
                                privacy = new JButton("Privacy Settings");
                                JButton exit = new JButton("My Profile");
                                JButton ie = new JButton("Import/Export");
                                JLabel settings = new JLabel("Settings:");
                                JLabel iTitle = new JLabel("My Interests:");
                                JLabel aTitle = new JLabel("About Me:");
                                JLabel pic = new JLabel("");

                                if (a.getPicName().length() > 0) {
                                    pic.setIcon(createPicture(a.getPicName()));
                                }

                                abMe.setColumns(23);
                                abMe.setRows(9);
                                abMe.setWrapStyleWord(true);
                                abMe.setLineWrap(true);
                                abMe.setEditable(false);

                                interests.setColumns(23);
                                interests.setRows(9);
                                interests.setWrapStyleWord(true);
                                interests.setLineWrap(true);
                                interests.setEditable(false);

                                JPanel tpanel = new JPanel();
                                tpanel.add(name);
                                tpanel.add(contact);
                                tpanel.add(pic);
                                frame.add(tpanel);

                                JPanel rpanel = new JPanel();
                                rpanel.add(iTitle);
                                rpanel.add(interests);
                                frame.add(rpanel);

                                JPanel cpanel = new JPanel();
                                cpanel.add(aTitle);
                                cpanel.add(abMe, BorderLayout.SOUTH);
                                frame.add(cpanel);

                                JPanel dpanel = new JPanel();
                                dpanel.add(settings);
                                dpanel.add(ie);
                                dpanel.add(fList);
                                dpanel.add(change);
                                dpanel.add(del);
                                dpanel.add(exit);
                                dpanel.add(checkFriendRequests);
                                dpanel.add(search);
                                dpanel.add(privacy);
                                exit.setVisible(false);
                                frame.add(dpanel);

                                frame.setVisible(true);

                                frame.setVisible(true);

                                privacy.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {

                                        boolean priv = false;

                                        do {

                                            int privDec = JOptionPane.showConfirmDialog(null, "Go Private? (Only Friends can view your account.)", "Privacy Settigns", JOptionPane.YES_NO_OPTION);
                                            if (privDec == JOptionPane.YES_OPTION) {
                                                try {
                                                    a.makePrivate(a.getUsername());
                                                } catch (FileNotFoundException e1) {
                                                    // TODO Auto-generated catch block
                                                    e1.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    a.makePublic(a.getUsername());
                                                } catch (IOException e1) {
                                                    // TODO Auto-generated catch block
                                                    e1.printStackTrace();
                                                }
                                            }
                                            break;

                                        } while (true);




                                    }
                                });

                                change.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {

                                        String[] standingOptions = {"Password", "Contacts", "Interests", "About Me", "Profile Picture"};
                                        String standing = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                                                "Change Information", JOptionPane.QUESTION_MESSAGE, null, standingOptions, standingOptions[0]);

                                        if (standing != null) {

                                            switch (standing) {

                                                case "Password":

                                                    do {
                                                        String newPass = JOptionPane.showInputDialog(null, "Enter A New Password",
                                                                "Password Change", JOptionPane.QUESTION_MESSAGE);

                                                        if (newPass != null) {

                                                            String b7 = a.toString();
                                                            a.setPassword(newPass);
                                                            String a7 = a.toString();

                                                            writer.write("D:" + b7 + ":" + a7);
                                                            writer.println();
                                                            writer.flush();

                                                            break;

                                                        }

                                                    } while (true);
                                                    break;

                                                case "Contacts":

                                                    do {

                                                        String[] options = {"Add", "Delete"};
                                                        int result = JOptionPane.showOptionDialog(null, "Would you like to delete or add a contact?", "Contact Change",
                                                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                null, options, options[1]);

                                                        if (result == JOptionPane.YES_OPTION) {

                                                            do {

                                                                String newCont = JOptionPane.showInputDialog(null, "Enter A New Contact (email, phones, etc)",
                                                                        "Add Contact", JOptionPane.QUESTION_MESSAGE);
                                                                if (newCont != null) {

                                                                    String b3 = a.toString();
                                                                    if (a.contacts.get(0).equals("")) {
                                                                        a.contacts.remove(0);
                                                                    }
                                                                    a.addContact(newCont);
                                                                    String a3 = a.toString();

                                                                    writer.write("D:" + b3 + ":" + a3);
                                                                    writer.println();
                                                                    writer.flush();

                                                                    contact.setText(a.getContacts().toString());

                                                                    break;
                                                                }
                                                            } while (true);


                                                        } else if (result == JOptionPane.NO_OPTION) {

                                                            do {

                                                                String newCont = JOptionPane.showInputDialog(null, "Enter A Contact to remove (email, phones, etc)",
                                                                        "Remove Contact", JOptionPane.QUESTION_MESSAGE);

                                                                if (newCont != null) {

                                                                    String b2 = a.toString();
                                                                    a.removeContact(newCont);
                                                                    String a2 = a.toString();

                                                                    writer.write("D:" + b2 + ":" + a2);
                                                                    writer.println();
                                                                    writer.flush();

                                                                    contact.setText(a.getContacts().toString());

                                                                    break;
                                                                }
                                                            } while (true);
                                                        }
                                                        break;

                                                    } while (true);
                                                    break;

                                                case "Interests":

                                                    do {

                                                        String[] options = {"Add", "Delete"};
                                                        int result = JOptionPane.showOptionDialog(null, "Would you like to delete or add an Interest?", "Interest Change",
                                                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                null, options, options[1]);

                                                        if (result == JOptionPane.YES_OPTION) {

                                                            do {

                                                                String newCont = JOptionPane.showInputDialog(null, "Enter A New Interest",
                                                                        "Add Interest", JOptionPane.QUESTION_MESSAGE);
                                                                if (newCont != null) {

                                                                    String b5 = a.toString();
                                                                    if (a.likes.get(0).equals("")) {
                                                                        a.likes.remove(0);
                                                                    }
                                                                    a.addLike(newCont);
                                                                    String a5 = a.toString();

                                                                    writer.write("D:" + b5 + ":" + a5);
                                                                    writer.println();
                                                                    writer.flush();

                                                                    interests.setText(a.getLikes().toString());

                                                                    break;
                                                                }
                                                            } while (true);


                                                        } else if (result == JOptionPane.NO_OPTION) {

                                                            do {

                                                                String newCont = JOptionPane.showInputDialog(null, "Enter an interest to remove",
                                                                        "Remove Interest", JOptionPane.QUESTION_MESSAGE);
                                                                if (newCont != null) {

                                                                    String b6 = a.toString();
                                                                    a.removeLike(newCont);
                                                                    String a6 = a.toString();

                                                                    a.removeLike(newCont);

                                                                    writer.write("D:" + b6 + ":" + a6);
                                                                    writer.println();
                                                                    writer.flush();

                                                                    interests.setText(a.getLikes().toString());
                                                                    break;
                                                                }
                                                            } while (true);
                                                        }
                                                        break;

                                                    } while (true);
                                                    break;

                                                case "About Me":

                                                    String am = JOptionPane.showInputDialog(null, "Enter your new about me",
                                                            "About Me Change", JOptionPane.QUESTION_MESSAGE);


                                                    if (am != null) {

                                                        String bef = a.toString();
                                                        a.setAboutMe(am);
                                                        String af = a.toString();

                                                        writer.write("D:" + bef + ":" + af);
                                                        writer.println();
                                                        writer.flush();

                                                        abMe.setText(am);

                                                        break;

                                                    }
                                                    break;

                                                case "Profile Picture":

                                                    String profPic = JOptionPane.showInputDialog(null, "Enter the name of the png",
                                                            "Change Profile Picture", JOptionPane.QUESTION_MESSAGE);

                                                    if (profPic != null) {

                                                        String bef1 = a.toString();
                                                        a.setPicName(profPic);
                                                        String af1 = a.toString();

                                                        writer.write("D:" + bef1 + ":" + af1);
                                                        writer.println();
                                                        writer.flush();

                                                        pic.setIcon(createPicture(a.getPicName()));

                                                        break;

                                                    }
                                                    break;

                                            }
                                        }

                                    }

                                });

                                del.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {

                                        int delete = JOptionPane.showConfirmDialog(null,
                                                "Are you sure you like to delete your account?", "Delete?", JOptionPane.YES_NO_OPTION);

                                        if (delete == JOptionPane.YES_OPTION) {

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            writer.write("d:" + a.getUsername());
                                            writer.println();
                                            writer.flush();

                                            frame.dispose();
                                        }

                                    }
                                });

                                fList.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {

                                        frame.setVisible(false);

                                        ArrayList<String> l = a.getFriendsList();
                                        ArrayList<JButton> b = new ArrayList<>();

                                        if (l.size() != 0) {

                                            JFrame f = new JFrame("Profile");
                                            Container con = f.getContentPane();

                                            con.setLayout(new BorderLayout());
                                            client = new Client();
                                            con.add(client, BorderLayout.CENTER);

                                            f.setSize(600, 400);
                                            f.setLocationRelativeTo(null);
                                            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                            f.setVisible(true);

                                            JPanel panel = new JPanel();

                                            for (int i = 0; i < l.size(); i++) {

                                                JButton j = new JButton(l.get(i));
                                                b.add(j);
                                            }

                                            for (int i = 0; i < b.size(); i++) {

                                                panel.add(b.get(i));

                                            }
                                            con.add(panel, BorderLayout.CENTER);

                                            for (int i = 0; i < b.size(); i++) {

                                                String s = l.get(i);

                                                b.get(i).addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {

                                                        frame.setVisible(true);

                                                        f.dispose();
                                                        fList.setVisible(false);
                                                        change.setVisible(false);
                                                        search.setVisible(false);
                                                        settings.setVisible(false);
                                                        ie.setVisible(false);
                                                        del.setVisible(false);
                                                        privacy.setVisible(false);


                                                        checkFriendRequests.setVisible(false);

                                                        //create new jlabels


                                                        //JPanel bottom = new JPanel();
                                                        //dpanel.add(otherFriendsList);
                                                        frame.add(dpanel);


                                                        writer.write("G:" + s);
                                                        writer.println();
                                                        writer.flush();

                                                        String account = "";

                                                        try {
                                                            account = reader.readLine();
                                                        } catch (IOException ioException) {
                                                        }

                                                        String temp = account.substring(2);
                                                        temp = temp.substring(temp.indexOf(",") + 1);

                                                        String p = temp.substring(0, temp.indexOf(","));

                                                        temp = temp.substring(temp.indexOf(",") + 1);

                                                        String cont = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
                                                        String[] C = cont.split(",");
                                                        ArrayList<String> c = new ArrayList<>();

                                                        for (String t : C) {
                                                            c.add(t);
                                                        }

                                                        temp = temp.substring(temp.indexOf("]") + 2);
                                                        String fren = temp.substring(1, temp.indexOf("]"));
                                                        String[] F = fren.split(",");
                                                        ArrayList<String> f = new ArrayList<>();

                                                        for (String t : F) {
                                                            f.add(t);
                                                        }

                                                        temp = temp.substring(temp.indexOf("]") + 2);
                                                        String lik = temp.substring(1, temp.indexOf("]"));
                                                        String[] L = lik.split(",");
                                                        ArrayList<String> l = new ArrayList<>();

                                                        for (String t : L) {
                                                            l.add(t);
                                                        }

                                                        temp = temp.substring(temp.indexOf("]") + 1);
                                                        String ab = temp.substring(0, temp.indexOf("[") - 1);

                                                        temp = temp.substring(temp.indexOf("[") + 1);
                                                        String fq = temp.substring(0, temp.indexOf("]"));
                                                        String[] Q = fq.split(",");
                                                        ArrayList<String> q = new ArrayList<>();

                                                        for (String t : Q) {
                                                            q.add(t);
                                                        }

                                                        if (ab.equals(",")) {
                                                            ab = "";
                                                        }

                                                        name.setText(s);
                                                        contact.setText(c.toString());
                                                        interests.setText(l.toString());
                                                        abMe.setText(ab);

                                                        exit.setVisible(true);


                                                        exit.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {

                                                                name.setText(a.getUsername());
                                                                contact.setText(a.getContacts().toString());
                                                                interests.setText(a.getLikes().toString());
                                                                abMe.setText(a.getAboutMe());
                                                                fList.setVisible(true);
                                                                change.setVisible(true);
                                                                search.setVisible(true);
                                                                settings.setVisible(true);
                                                                checkFriendRequests.setVisible(true);
                                                                ie.setVisible(true);
                                                                del.setVisible(true);
                                                                exit.setVisible(false);
                                                                privacy.setVisible(true);

                                                            }
                                                        });

                                                    }
                                                });

                                            }

                                        } else {

                                            JOptionPane.showMessageDialog(null, "You have no friends :(",
                                                    "Error", JOptionPane.ERROR_MESSAGE);

                                            frame.setVisible(true);

                                        }
                                    }

                                });

                                frame.setVisible(true);

                                checkFriendRequests.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        String[] potentialFriends = new String [a.getFriendReqs().size()];
                                        for (int i = 0; i < a.getFriendReqs().size(); i++) {
                                            potentialFriends[i] = a.getFriendReqs().get(i);
                                        }

                                        ArrayList<String> tempList = new ArrayList<String>();
                                        for (int m = 0; m < potentialFriends.length; m++) {
                                            if (potentialFriends[m].equals("")) {

                                            } else {
                                                tempList.add(potentialFriends[m]);
                                            }
                                        }

                                        String[] potFriends = new String [tempList.size()];
                                        for (int p = 0; p < potFriends.length; p++) {
                                            potFriends[p] = tempList.get(p);
                                        }

                                        if (potFriends[0].equals("")) {
                                            do {
                                                JOptionPane.showMessageDialog(null, "No Friend Requests", "Friend Requests", JOptionPane.YES_OPTION);
                                                break;
                                            } while (true);
                                        } else {

                                            String pendingFriends = (String) JOptionPane.showInputDialog(null, "Which requests would you like to accept/decline?",
                                                    "Friend Requests", JOptionPane.QUESTION_MESSAGE, null, potFriends, potFriends[0]);

                                            if (pendingFriends != null || pendingFriends !=  "") {
                                                String newFriendInfo = "";
                                                boolean go = false;
                                                do {

                                                    int friendChoice = JOptionPane.showConfirmDialog(null, "Would you like to be friends with " + pendingFriends + "?",
                                                            "Friend Requests", JOptionPane.YES_NO_OPTION);

                                                    if (friendChoice == JOptionPane.YES_OPTION) {
                                                        //add friend by:
                                                        //removing them from friend requests
                                                        //adding them to friend requests list and vice versa
                                                        //



                                                        a.addFriend(pendingFriends);
                                                        a.removeFriendReq(pendingFriends);
                                                        //construct the account for pending friends
                                                        writer.write("Z:" + pendingFriends);
                                                        writer.println();
                                                        writer.flush();
                                                        newFriendInfo = "";
                                                        go = true;
                                                        break;

                                                    } else if (friendChoice == JOptionPane.NO_OPTION) {
                                                        a.removeFriendReq(pendingFriends);
                                                        //delete name from file friend request
                                                        writer.write("s:" + a.getUsername() + ":" + pendingFriends);
                                                        writer.println();
                                                        writer.flush();
                                                        break;
                                                    }

                                                } while (true);

                                                if (go) {

                                                    System.out.println("Getting here");
                                                    try {
                                                        newFriendInfo = reader.readLine();

                                                    } catch (Exception f) {
                                                        System.out.println(f.getStackTrace());
                                                    }

                                                    String searchedAccString = newFriendInfo.substring(2);

                                                    System.out.println("This works");

                                                    //create code to get the actual lists of interests and conts

                                                    String[] searchedAccSplit = searchedAccString.split("]");

                                                    //acc name
                                                    String searchedName = searchedAccSplit[0].substring(0, searchedAccSplit[0].indexOf(","));

                                                    //acc contacts
                                                    ArrayList<String> searchedContacts = new ArrayList<String>();
                                                    String sc = searchedAccSplit[0].substring(searchedAccSplit[0].indexOf("["));
                                                    if (sc.indexOf(",") != -1) {
                                                        String[] scSplit = sc.split(",");
                                                        for (int z = 0; z < scSplit.length; z++) {
                                                            searchedContacts.add(scSplit[z]);
                                                        }
                                                    } else if (sc.indexOf(",") == -1 && sc.equals("") == false) {
                                                        searchedContacts.add(sc);
                                                    }

                                                    //friends list
                                                    ArrayList<String> searchedFriends = new ArrayList<String>();
                                                    String sf = searchedAccSplit[1].substring(searchedAccSplit[1].indexOf("[") + 1);
                                                    if (sf.indexOf(",") != -1) {
                                                        String[] sfSplit = sf.split(",");
                                                        for (int j = 0; j < sfSplit.length; j++) {
                                                            searchedFriends.add(sfSplit[j]);
                                                        }
                                                    } else if (sf.indexOf(",") == -1 && sf.equals("") == false) {
                                                        searchedFriends.add(sf);
                                                    }

                                                    //acc interests and likes
                                                    ArrayList<String> searchedInterests = new ArrayList<String>();
                                                    String si = searchedAccSplit[2].substring(searchedAccSplit[2].indexOf("[") + 1);
                                                    if (si.indexOf(",") != -1) {
                                                        String[] siSplit = si.split(",");
                                                        for (int n = 0; n < siSplit.length; n++) {
                                                            searchedInterests.add(siSplit[n]);
                                                        }
                                                    } else if (si.indexOf(",") == -1 && si.equals("") == false) {
                                                        searchedInterests.add(si);
                                                    }

                                                    //about me
                                                    String searchedAboutMe = "";
                                                    String sa = searchedAccSplit[3];
                                                    String saInterest = sa.substring(sa.indexOf(","), sa.indexOf("["));
                                                    String sam;
                                                    if (saInterest.equals(",,") == false) {
                                                        sam = sa.substring(sa.indexOf(",") + 1, sa.lastIndexOf(","));
                                                    }
                                                    sam = searchedAboutMe;

                                                    //searched acc friend req list
                                                    ArrayList<String> searchedFriendRequests = new ArrayList<String>();
                                                    String fr = searchedAccSplit[3].substring(searchedAccSplit[3].indexOf("[")+1);
                                                    if (fr.indexOf(",") != -1) {
                                                        String[] frSplit = fr.split(",");
                                                        for (int b = 0; b < frSplit.length; b++) {
                                                            searchedFriendRequests.add(frSplit[b]);
                                                        }
                                                    } else if (fr.indexOf(",") == 1 && fr.equals("") == false) {
                                                        searchedFriendRequests.add(fr);
                                                    }

                                                    String[] splitter = searchedAccString.split(",");
                                                    String pics = splitter[2];

                                                    Account addedFriend = new Account(searchedName, "password", pics, searchedContacts, searchedFriends, searchedInterests, searchedAboutMe, searchedFriendRequests);
                                                    addedFriend.getFriendsList().add(a.getUsername());

                                                    //have to write their new found friendship in the acc.txt file and delete their friend request
                                                    String user = a.getUsername();
                                                    String userFriend = addedFriend.getUsername();
                                                    writer.write("Q:" + user + ":" + userFriend);
                                                    writer.println();
                                                    writer.flush();
                                                }


                                            }

                                        }
                                    }
                                });

                                search.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        String checkType;
                                        do {
                                            String searchName =  JOptionPane.showInputDialog(null, "Search Profile",
                                                    "Search", JOptionPane.QUESTION_MESSAGE);
                                            String sendServer = ("Z:" + searchName);
                                            writer.write(sendServer);
                                            writer.println();
                                            writer.flush();
                                            checkType = "";

                                            break;

                                        } while(true);


                                        try {
                                            checkType = reader.readLine();
                                        } catch (IOException ioException) {
                                            JOptionPane.showMessageDialog(null, "An error occurred. Try again.", "Error",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }


                                        String checkCommand = checkType.substring(0,2);
                                        String searchedAccString = checkType.substring(2);

                                        //create code to get the actual lists of interests and conts

                                        String[] searchedAccSplit = searchedAccString.split("]");

                                        //acc name
                                        String searchedName = searchedAccSplit[0].substring(0, searchedAccSplit[0].indexOf(","));

                                        //acc contacts
                                        ArrayList<String> searchedContacts = new ArrayList<String>();
                                        String sc = searchedAccSplit[0].substring(searchedAccSplit[0].indexOf("["));
                                        if (sc.indexOf(",") != -1) {
                                            String[] scSplit = sc.split(",");
                                            for (int z = 0; z < scSplit.length; z++) {
                                                searchedContacts.add(scSplit[z]);
                                            }
                                        } else if (sc.indexOf(",") == -1 && sc.equals("") == false) {
                                            searchedContacts.add(sc);
                                        }

                                        //friends list
                                        ArrayList<String> searchedFriends = new ArrayList<String>();
                                        String sf = searchedAccSplit[1].substring(searchedAccSplit[1].indexOf("[") + 1);
                                        if (sf.indexOf(",") != -1) {
                                            String[] sfSplit = sf.split(",");
                                            for (int j = 0; j < sfSplit.length; j++) {
                                                searchedFriends.add(sfSplit[j]);
                                            }
                                        } else if (sf.indexOf(",") == -1 && sf.equals("") == false) {
                                            searchedFriends.add(sf);
                                        }

                                        //acc interests and likes
                                        ArrayList<String> searchedInterests = new ArrayList<String>();
                                        String si = searchedAccSplit[2].substring(searchedAccSplit[2].indexOf("[") + 1);
                                        if (si.indexOf(",") != -1) {
                                            String[] siSplit = si.split(",");
                                            for (int n = 0; n < siSplit.length; n++) {
                                                searchedInterests.add(siSplit[n]);
                                            }
                                        } else if (si.indexOf(",") == -1 && si.equals("") == false) {
                                            searchedInterests.add(si);
                                        }

                                        //about me
                                        String searchedAboutMe = "";
                                        String sa = searchedAccSplit[3];
                                        String saInterest = sa.substring(sa.indexOf(","), sa.indexOf("["));
                                        String sam;
                                        if (saInterest.equals(",,") == false) {
                                            sam = sa.substring(sa.indexOf(",") + 1, sa.lastIndexOf(","));
                                        }
                                        sam = searchedAboutMe;

                                        //searched acc friend req list
                                        ArrayList<String> searchedFriendRequests = new ArrayList<String>();
                                        String fr = searchedAccSplit[3].substring(searchedAccSplit[3].indexOf("[")+1);
                                        if (fr.indexOf(",") != -1) {
                                            String[] frSplit = fr.split(",");
                                            for (int b = 0; b < frSplit.length; b++) {
                                                searchedFriendRequests.add(frSplit[b]);
                                            }
                                        } else if (fr.indexOf(",") == 1 && fr.equals("") == false) {
                                            searchedFriendRequests.add(fr);
                                        }
                                        String[] splitter = searchedAccString.split(",");
                                        String pics = splitter[2];
                                        Account searchedAcc = new Account(searchedName, "password", pics, searchedContacts, searchedFriends, searchedInterests, searchedAboutMe, searchedFriendRequests);
                                        try {
                                            System.out.println(searchedAcc.isPrivate(searchedAcc) + "isfriend");
                                        } catch (IOException e2) {
                                            // TODO Auto-generated catch block
                                            e2.printStackTrace();
                                        }
                                        //will check if the two accs are friends
                                        //this isnt working
                                        //logic? or friends list not updating?
                                        //searchedAcc.getFriendsList().add("tax7");
                                        boolean isFriend = a.isFriend(searchedAcc);
                                        if (isFriend) {
                                            //show the account without the add friend button
                                            //add back to home button
                                            System.out.println(isFriend + a.getUsername() + searchedAcc.getUsername());

                                            //setting the text for all the jlabels
                                            name.setText(searchedAcc.getUsername());
                                            abMe.setText(searchedAcc.getAboutMe());
                                            interests.setText(searchedAcc.getLikes().toString());
                                            contact.setText(searchedAcc.getContacts().toString());

                                            //make all login profile jbuttons invisible
                                            fList.setVisible(false);
                                            change.setVisible(false);
                                            search.setVisible(false);
                                            settings.setVisible(false);
                                            ie.setVisible(false);
                                            del.setVisible(false);
                                            privacy.setVisible(false);


                                            checkFriendRequests.setVisible(false);

                                            //create new jlabels
                                            home = new JButton("Home");

                                            //JPanel bottom = new JPanel();
                                            //dpanel.add(otherFriendsList);
                                            dpanel.add(home);
                                            frame.add(dpanel);

                                            //otherFriendsList.setVisible(true);
                                            home.setVisible(true);

                                            homeConditional = true;



                                        } else
                                            try {
                                                if (isFriend == false && searchedAcc.isPrivate(searchedAcc) == false) {
                                                    System.out.println("what the: " + searchedAcc.isPrivate(searchedAcc));
                                                    //show the account with the add friend button, replace search
                                                    //add back to home button
                                                    name.setText(searchedAcc.getUsername());
                                                    abMe.setText(searchedAcc.getAboutMe());
                                                    interests.setText(searchedAcc.getLikes().toString());
                                                    contact.setText(searchedAcc.getContacts().toString());

                                                    //make all login profile jbuttons invisible
                                                    fList.setVisible(false);
                                                    change.setVisible(false);
                                                    search.setVisible(false);
                                                    settings.setVisible(false);
                                                    ie.setVisible(false);
                                                    del.setVisible(false);
                                                    privacy.setVisible(false);


                                                    checkFriendRequests.setVisible(false);

                                                    //create new jlabels
                                                    home = new JButton("Home");
                                                    addFriend = new JButton("Add Friend");

                                                    //JPanel bottom = new JPanel();
                                                    //dpanel.add(otherFriendsList);
                                                    dpanel.add(home);
                                                    dpanel.add(addFriend);
                                                    frame.add(dpanel);

                                                    //otherFriendsList.setVisible(true);
                                                    home.setVisible(true);
                                                    addFriendConditional = true;
                                                    if (addFriendConditional) {
                                                        addFriend.setVisible(true);
                                                    }


                                                    homeConditional = true;


                                                } else
                                                    try {
                                                        if (isFriend == false && searchedAcc.isPrivate(searchedAcc) == true) {
                                                            int addOrNo;
                                                            System.out.println("LEts fucking go");
                                                            //ask if they want to add this private account
                                                            do {
                                                                addOrNo = JOptionPane.showConfirmDialog(null, "This account is private. Send Friend Request?", "Private Account", JOptionPane.YES_NO_OPTION);

                                                                break;
                                                            } while (true);

                                                            if (addOrNo == JOptionPane.YES_OPTION) {
                                                                privAdded = true;
                                                                addFriendConditional = true;
                                                            }

                                                        }
                                                    } catch (IOException e1) {
                                                        // TODO Auto-generated catch block
                                                        e1.printStackTrace();
                                                    }
                                            } catch (HeadlessException | IOException e1) {
                                                // TODO Auto-generated catch block
                                                e1.printStackTrace();
                                            }
                    if (homeConditional) {
                        home.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                //set all text back to profile information
                                name.setText(a.getUsername());
                                abMe.setText(a.getAboutMe());
                                interests.setText(a.getLikes().toString());
                                contact.setText(a.getContacts().toString());

                                home.setVisible(false);

                                if (addFriendConditional) {
                                    addFriend.setVisible(false);
                                }

                                //otherFriendsList.setVisible(false);

                                //profile buttons become visible again
                                fList.setVisible(true);
                                change.setVisible(true);
                                search.setVisible(true);
                                checkFriendRequests.setVisible(true);
                                settings.setVisible(true);
                                ie.setVisible(true);
                                del.setVisible(true);
                                privacy.setVisible(true);

                                homeConditional = false;

                            }
                        });
                    }

                    if (privAdded) {
                        boolean x = true;
                        if (x) {
                            searchedAcc.addFriendReq(a.getUsername());
                            JOptionPane.showConfirmDialog(null, "Friend Request Sent!", "Friend Request", JOptionPane.OK_CANCEL_OPTION);
                            writer.write("K:"+a.getUsername()+":"+searchedAcc.getUsername());
                            writer.println();
                            writer.flush();
                            addFriendConditional = false;
                        }
                    }

                    //friend request stuff here
                    if (addFriendConditional) {
                        addFriend.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                boolean x = true;
                                for (int i = 0; i < searchedAcc.getFriendReqs().size(); i++) {
                                    if (a.getUsername() == searchedAcc.getFriendReqs().get(i)) {
                                        x = false;
                                        JOptionPane.showConfirmDialog(null, "Friend Request Pending...", "Friend Request", JOptionPane.OK_CANCEL_OPTION);
                                    }
                                }
                                if (x) {
                                    searchedAcc.addFriendReq(a.getUsername());
                                    JOptionPane.showConfirmDialog(null, "Friend Request Sent!", "Friend Request", JOptionPane.OK_CANCEL_OPTION);
                                    writer.write("K:"+a.getUsername()+":"+searchedAcc.getUsername());
                                    writer.println();
                                    writer.flush();
                                }

                            }
                        });
                    }


                }
            });

            ie.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String[] options = {"Import", "Export"};
                    int result = JOptionPane.showOptionDialog(null, "Would you like to import or export a profile?", "Import or Export?",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, options, options[1]);

                    if (result == JOptionPane.YES_OPTION) {

                        String filename;
                        boolean breaker = false;

                        filename = JOptionPane.showInputDialog(null, "Enter the name of the file",
                                "Import", JOptionPane.QUESTION_MESSAGE);

                        if (filename == null) {
                            breaker = true;
                        }

                        if (!breaker) {

                            writer.write("I:" + filename);
                            writer.println();
                            writer.flush();

                            JOptionPane.showMessageDialog(null, "The File has been imported!",
                                    "Import Successful", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } else if (result == JOptionPane.NO_OPTION) {

                        String filename;
                        boolean breaker = false;

                        filename = JOptionPane.showInputDialog(null, "Enter the name of the file you would like to export to",
                                "Export", JOptionPane.QUESTION_MESSAGE);

                        if (filename == null) {
                            breaker = true;
                        }

                        if (!breaker) {

                            writer.write("E:" + filename + "," + a.toString());
                            writer.println();
                            writer.flush();

                            JOptionPane.showMessageDialog(null, "The File has been Exported!",
                                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                }
            });


        }
    }

    public static ImageIcon createPicture(String s) {

        ImageIcon i = new ImageIcon(s);
        return i;

    }
}
