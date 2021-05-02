import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Test extends JComponent {

    static JLabel name;
    static JLabel abMe;
    static JLabel interests;
    static JLabel contact;
    static JButton fList;
    static JButton change;
    static JButton search; 
    static JButton checkFriendRequests; 

    //these labels will be used when an account is searched
    static JLabel searchedName; 
    static JLabel searchedAbMe;
    static JLabel searchedInterests;
    static JLabel searchedContacts;


    //these buttons will be used when an account is searched
    static JButton home; 
    static JButton addFriend; 
    //this one will be when you open someone else's friends list and not yours
    static JButton otherFriendsList; 

    //will allow the searched profile buttons to exist before used
    static boolean homeConditional;
    static boolean addFriendConditional;  

    static Client client;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

        Socket socket = new Socket("localhost", 4242);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        Account a;

        String[] options = {"Login", "Create New Account"};
        int result = JOptionPane.showOptionDialog(null, "Would you like to login or create a new account?", "Ready?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);

        if (result == JOptionPane.YES_OPTION) {

            String username;
            String password;
            boolean x = false;

            do {

                do {
                    username = JOptionPane.showInputDialog(null, "Enter Username",
                            "Login", JOptionPane.QUESTION_MESSAGE);

                } while ((username == null) || (username.isBlank()));

                do {
                    password = JOptionPane.showInputDialog(null, "Enter Password",
                            "Login", JOptionPane.QUESTION_MESSAGE);

                } while ((password == null) || (password.isBlank()));

                writer.write("A:" + username + "," + password);
                writer.println();
                writer.flush();

                String account = reader.readLine();

                if (account.substring(0, 2).equals("V:")) {

                    String temp = account.substring(2);
                    temp = temp.substring(temp.indexOf(",") + 1);
                    temp = temp.substring(temp.indexOf(",") + 1);

                    String con = temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
                    String[] C = con.split(",");
                    ArrayList<String> c = new ArrayList<>();

                    for (String t: C) {
                        c.add(t);
                    }

                    temp = temp.substring(temp.indexOf("]") + 2);
                    String fren = temp.substring(1, temp.indexOf("]"));
                    String[] F = fren.split(",");
                    ArrayList<String> f = new ArrayList<>();

                    for (String t: F) {
                        f.add(t);
                    }

                    temp = temp.substring(temp.indexOf("]") + 2);
                    String lik = temp.substring(1, temp.indexOf("]"));
                    String[] L = lik.split(",");
                    ArrayList<String> l = new ArrayList<>();

                    for (String t: L) {
                        l.add(t);
                    }

                    temp = temp.substring(temp.indexOf("]") + 1);
                    String ab = temp.substring(0, temp.indexOf("[") - 1);

                    temp = temp.substring(temp.indexOf("[") + 1);
                    String fq = temp.substring(0, temp.indexOf("]"));
                    String[] Q = fq.split(",");
                    ArrayList<String> q = new ArrayList<>();

                    for (String t: Q) {
                        q.add(t);
                    }

                    if (ab.equals(",")) {
                        ab = "";
                    }

                    a = new Account(username, password, c, f, l, ab, q);
                    break;

                } else {

                    JOptionPane.showMessageDialog(null, "Incorrect Username or Password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } while (true);

        } else {

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

            writer.write("N:" + a.toString());
            writer.println();
            writer.flush();

        }

        //Show profile in complex gui with loop, maybe a run method

        JFrame frame = new JFrame("Profile");
        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());
        client = new Client();
        content.add(client, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        name = new JLabel(a.getUsername());
        interests = new JLabel(a.getLikes().toString());
        abMe = new JLabel(a.getAboutMe());
        contact = new JLabel(a.getContacts().toString());
        fList = new JButton("Friends List");
        change = new JButton("Change information");
        search = new JButton("Search Profiles");
        checkFriendRequests = new JButton("Friend Requests");

        

        JPanel tpanel = new JPanel();
        tpanel.add(name);
        tpanel.add(contact);
        content.add(tpanel, BorderLayout.NORTH);

        JPanel rpanel = new JPanel();
        rpanel.add(interests);
        content.add(rpanel, BorderLayout.EAST);

        JPanel cpanel = new JPanel();
        cpanel.add(abMe);
        content.add(cpanel, BorderLayout.WEST);

        JPanel dpanel = new JPanel();
        dpanel.add(fList);
        dpanel.add(change);
        dpanel.add(search); 
        dpanel.add(checkFriendRequests);
        content.add(dpanel, BorderLayout.SOUTH);


        boolean pat = false;

        frame.setVisible(true);

        change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String[] standingOptions = {"Username", "Password", "Contacts", "Interests"};
                String standing = (String) JOptionPane.showInputDialog(null, "What would you like to change?",
                        "Change Information", JOptionPane.QUESTION_MESSAGE, null, standingOptions, standingOptions[0]);

                if (standing != null) {

                    switch (standing) {

                        case "Username":

                            do {
                                String newUser = JOptionPane.showInputDialog(null, "Enter A New Username",
                                        "Name Change", JOptionPane.QUESTION_MESSAGE);

                                writer.write("C:" + newUser);
                                writer.println();
                                writer.flush();

                                String check = "";

                                try {
                                    check = reader.readLine();
                                } catch (IOException ioException) {
                                    JOptionPane.showMessageDialog(null, "An error occurred. Try again.", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                                if (check.equals("Y")) {

                                    writer.write("D:" + a.toString());
                                    writer.println();
                                    writer.flush();

                                    writer.write("d:" + a.getUsername());
                                    writer.println();
                                    writer.flush();

                                    a.setUsername(newUser);

                                    writer.write("R:" + a.toString());
                                    writer.println();
                                    writer.flush();

                                    writer.write("r:" + a.getUsername());
                                    writer.println();
                                    writer.flush();

                                    name.setText(a.getUsername());
                                    break;
                                }

                            } while (true);
                            break;

                        case "Password":

                            do {
                                String newPass = JOptionPane.showInputDialog(null, "Enter A New Password",
                                        "Password Change", JOptionPane.QUESTION_MESSAGE);

                                if (newPass != null) {

                                    writer.write("D:" + a.toString());
                                    writer.println();
                                    writer.flush();

                                    a.setPassword(newPass);

                                    writer.write("R:" + a.toString());
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

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            a.addContact(newCont);

                                            writer.write("R:" + a.toString());
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

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            a.removeContact(newCont);

                                            writer.write("R:" + a.toString());
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

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            a.addLike(newCont);

                                            writer.write("R:" + a.toString());
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

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            a.removeLike(newCont);

                                            writer.write("R:" + a.toString());
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
                    }
                }

                }

            });

        /*
        
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

                ArrayList<String> l = a.getFriendsList();
                ArrayList<JButton> b = new ArrayList<>();

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

                            f.dispose();
                            change.setVisible(false);
                            del.setVisible(false);

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
                                    change.setVisible(true);
                                    del.setVisible(true);
                                    exit.setVisible(false);

                                }
                            });

                        }
                    });

                }

            }

        });

        */
        

        frame.setVisible(true);

        checkFriendRequests.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] potentialFriends = new String [a.getFriendReqs().size()]; 
                for (int i = 0; i < a.getFriendReqs().size(); i++) {
                    potentialFriends[i] = a.getFriendReqs().get(i);
                }

                if (potentialFriends.length == 0) {
                    do { 
                        JOptionPane.showMessageDialog(null, "No Friend Requests", "Friend Requests", JOptionPane.YES_OPTION);
                        break; 
                    } while (true);
                } else {
                    String pendingFriends = (String) JOptionPane.showInputDialog(null, "Which requests would you like to accept/decline?",
                "Friend Requests", JOptionPane.QUESTION_MESSAGE, null, potentialFriends, potentialFriends[0]);

                if (pendingFriends != null) {
                    String newFriendInfo; 
                    do {

                        int friendChoice = JOptionPane.showConfirmDialog(null, "Would you like to be friends with " + pendingFriends + "?", 
                        "Friend Requests", JOptionPane.YES_NO_OPTION);

                        if (friendChoice == JOptionPane.YES_OPTION) {
                            //get the user 
                            a.addFriend(pendingFriends);
                            writer.write("Z:" + pendingFriends);
                            writer.println();
                            writer.flush();
                            newFriendInfo = "";
                            break; 

                        } else {
                            a.removeFriendReq(pendingFriends);
                        }

                    } while (true);

                    try {
                        newFriendInfo = reader.readLine();

                    } catch (Exception f) {
                        System.out.println(f.getStackTrace());
                    }

                    String checkCommand = newFriendInfo.substring(0,2);
                    String addedFriendString = newFriendInfo.substring(2);

                    
                    String[] addedFriendSplit = addedFriendString.split(",");
                    ArrayList<String> con = new ArrayList<String>(); 
                    ArrayList<String> fl = new ArrayList<String>();
                    ArrayList<String> like = new ArrayList<String>();
                    String aboutMe = addedFriendSplit[4];
                    ArrayList<String> fr = new ArrayList<String>();
                    Account addedFriend = new Account(addedFriendSplit[0], addedFriendSplit[1], con, fl, like, aboutMe, fr);
                    addedFriend.getFriendsList().add(a.getUsername());
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

                    
                    String[] searchedAccSplit = searchedAccString.split(",");
                    ArrayList<String> con = new ArrayList<String>(); 
                    ArrayList<String> fl = new ArrayList<String>();
                    ArrayList<String> like = new ArrayList<String>();
                    String aboutMe = searchedAccSplit[4];
                    ArrayList<String> fr = new ArrayList<String>();
                    Account searchedAcc = new Account(searchedAccSplit[0], searchedAccSplit[1], con, fl, like, aboutMe, fr);
                    
                    //will check if the two accs are friends
                    //this isnt working 
                        //logic? or friends list not updating?
                    //searchedAcc.getFriendsList().add("tax7");
                    searchedAcc.setAboutMe("I am a friend of the test account");
                    boolean isFriend = a.isFriend(searchedAcc); 
                    System.out.println(isFriend);

                    if (isFriend) {
                        //show the account without the add friend button
                            //add back to home button

                            //setting the text for all the jlabels
                            name.setText(searchedAcc.getUsername());
                            abMe.setText(searchedAcc.getAboutMe());
                            interests.setText(searchedAcc.getLikes().toString());
                            contact.setText(searchedAcc.getContacts().toString());
                            
                            //make all login profile jbuttons invisible
                            fList.setVisible(false);
                            change.setVisible(false);
                            search.setVisible(false);
                            checkFriendRequests.setVisible(false);

                            //create new jlabels
                            searchedName = new JLabel(a.getUsername());
                            searchedInterests = new JLabel(a.getLikes().toString());
                            searchedAbMe = new JLabel(a.getAboutMe());
                            searchedContacts = new JLabel(a.getContacts().toString());
                            otherFriendsList = new JButton(searchedAcc.getUsername() + "'s Friends List");
                            // dont need this bc they friends addFriend = new JButton("Add Friend");
                            home = new JButton("Home");

                            //JPanel bottom = new JPanel();
                            dpanel.add(otherFriendsList);
                            dpanel.add(home);
                            content.add(dpanel, BorderLayout.SOUTH); 
                            
                            otherFriendsList.setVisible(true);
                            home.setVisible(true); 

                            homeConditional = true; 
                            
                            

                            /*
                            Container parentName = name.getParent(); 
                            parentName.remove(name); 
                            Container parentInterests = interests.getParent();
                            parentInterests.remove(interests);
                            Container parentAbMe = abMe.getParent();
                            parentAbMe.remove(abMe);
                            Container parentContact = contact.getParent();
                            parentContact.remove(contact);
                            Container parentFList = fList.getParent();
                            parentFList.remove(fList);
                            Container parentSearch = search.getParent();
                            parentSearch.remove(search); 
                            Container parentChange = change.getParent();
                            parentChange.remove(change); 

                            parentName.revalidate();
                            parentName.repaint();
                            parentInterests.revalidate();
                            parentInterests.repaint();
                            parentAbMe.revalidate();
                            parentAbMe.repaint();
                            parentContact.revalidate();
                            parentContact.repaint();
                            parentFList.revalidate();
                            parentFList.repaint();
                            parentSearch.revalidate();
                            parentSearch.repaint();
                            parentChange.revalidate();
                            parentChange.repaint(); 

                            //remove the panels
                            Container parentTPanel = tpanel.getParent();
                            parentTPanel.remove(tpanel);
                            Container parentRPanel = rpanel.getParent();
                            parentRPanel.remove(rpanel);
                            Container parentCPanel = cpanel.getParent();
                            parentCPanel.remove(cpanel);
                            Container parentDPanel = dpanel.getParent();
                            parentDPanel.remove(dpanel);

                            parentTPanel.revalidate();
                            parentTPanel.repaint();
                            parentRPanel.revalidate();
                            parentRPanel.repaint();
                            parentCPanel.revalidate();
                            parentCPanel.repaint();
                            parentDPanel.revalidate();
                            parentDPanel.repaint();

                             

                            //getContentPane().removeAll(); 
                            //draw up the searched profile screen
                            name = new JLabel(searchedAcc.getUsername());
                            interests = new JLabel(searchedAcc.getLikes().toString());
                            abMe = new JLabel(searchedAcc.getAboutMe());
                            contact = new JLabel(searchedAcc.getContacts().toString());
                            otherFriendsList = new JButton(searchedAcc.getUsername() + "'s Friends List");
                            //addFriend = new JButton("Change information");
                            home = new JButton("Home");

                            
                            JPanel tpanel = new JPanel();
                            tpanel.add(name);
                            tpanel.add(contact);
                            content.add(tpanel, BorderLayout.NORTH);
                    
                            JPanel rpanel = new JPanel();
                            rpanel.add(interests);
                            content.add(rpanel, BorderLayout.EAST);
                    
                            JPanel cpanel = new JPanel();
                            cpanel.add(abMe);
                            content.add(cpanel, BorderLayout.WEST);
                    
                            JPanel dpanel = new JPanel();
                            dpanel.add(otherFriendsList);
                            //dpanel.add(addFriend);
                            dpanel.add(home); 
                            content.add(dpanel, BorderLayout.SOUTH);

                            */

                            

                    } else {
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
                        checkFriendRequests.setVisible(false);

                        //create new jlabels
                        searchedName = new JLabel(a.getUsername());
                        searchedInterests = new JLabel(a.getLikes().toString());
                        searchedAbMe = new JLabel(a.getAboutMe());
                        searchedContacts = new JLabel(a.getContacts().toString());
                        otherFriendsList = new JButton(searchedAcc.getUsername() + "'s Friends List");
                        addFriend = new JButton("Add Friend");
                        home = new JButton("Home");

                        JPanel bottom = new JPanel();
                        bottom.add(otherFriendsList);
                        bottom.add(home);
                        bottom.add(addFriend);  
                        content.add(bottom, BorderLayout.SOUTH); 

                        homeConditional = true;   
                        addFriendConditional = true; 
                        
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
                            addFriend.setVisible(false);
                            otherFriendsList.setVisible(false);
        
                            //profile buttons become visible again
                            fList.setVisible(true);
                            change.setVisible(true);
                            search.setVisible(true);
                            checkFriendRequests.setVisible(true);
        
                            homeConditional = false; 
        
                            /*
                            parentName.remove(name); 
                            //Container parentInterests = interests.getParent();
                            parentInterests.remove(interests);
                            //Container parentAbMe = abMe.getParent();
                            parentAbMe.remove(abMe);
                            //Container parentContact = contact.getParent();
                            parentContact.remove(contact);
                            Container parentotherFList = otherFriendsList.getParent();
                            parentotherFList.remove(otherFriendsList);
                            //Container parentSearch = search.getParent();
                            //parentSearch.remove(search); 
                            //Container parentChange = change.getParent();
                            parentChange.remove(change); 
        
                            parentName.revalidate();
                            parentName.repaint();
                            parentInterests.revalidate();
                            parentInterests.repaint();
                            parentAbMe.revalidate();
                            parentAbMe.repaint();
                            parentContact.revalidate();
                            parentContact.repaint();
                            parentotherFList.revalidate();
                            parentotherFList.repaint();
                            //parentSearch.revalidate();
                            //parentSearch.repaint();
                            parentChange.revalidate();
                            parentChange.repaint(); 
        
                            //remove the panels
                            //Container parentTPanel = tpanel.getParent();
                            parentTPanel.remove(tpanel);
                            //Container parentRPanel = rpanel.getParent();
                            parentRPanel.remove(rpanel);
                            //Container parentCPanel = cpanel.getParent();
                            parentCPanel.remove(cpanel);
                            //Container parentDPanel = dpanel.getParent();
                            parentDPanel.remove(dpanel);
        
                            parentTPanel.revalidate();
                            parentTPanel.repaint();
                            parentRPanel.revalidate();
                            parentRPanel.repaint();
                            parentCPanel.revalidate();
                            parentCPanel.repaint();
                            parentDPanel.revalidate();
                            parentDPanel.repaint();
        
                                    
        
                            //getContentPane().removeAll(); 
                            //draw up the searched profile screen
                            name = new JLabel(a.getUsername());
                            interests = new JLabel(a.getLikes().toString());
                            abMe = new JLabel(a.getAboutMe());
                            contact = new JLabel(a.getContacts().toString());
                            fList = new JButton("Friends List");
                            change = new JButton("Change information");
                            search = new JButton("Search Profiles");
        
                                
                                
                            JPanel tpanel = new JPanel();
                            tpanel.add(name);
                            tpanel.add(contact);
                            content.add(tpanel, BorderLayout.NORTH);
        
                            JPanel rpanel = new JPanel();
                            rpanel.add(interests);
                            content.add(rpanel, BorderLayout.EAST);
        
                            JPanel cpanel = new JPanel();
                            cpanel.add(abMe);
                            content.add(cpanel, BorderLayout.WEST);
        
                            JPanel dpanel = new JPanel();
                            dpanel.add(fList);
                            dpanel.add(change);
                            dpanel.add(search); 
                            content.add(dpanel, BorderLayout.SOUTH);
                            */
                
                            }
                        });
                }
                
            //friend request stuff here
            if (addFriendConditional) {
                home.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        boolean x = true; 
                        for (int i = 0; i < searchedAcc.getFriendsList().size(); i++) {
                            if (a.getUsername() == searchedAcc.getFriendsList().get(i)) {
                                x = false;
                                JOptionPane.showConfirmDialog(null, "Already Added...", "Friend Request", JOptionPane.OK_CANCEL_OPTION); 
                            }
                        }
                        if (x) {
                            searchedAcc.addFriendReq(a.getUsername());
                            JOptionPane.showConfirmDialog(null, "Added Friend!", "Friend Request", JOptionPane.OK_CANCEL_OPTION);
                        }
                       
                    }
                });
            }


            }
        });

        


        frame.setVisible(true); 

    }

    
}

//end of program
