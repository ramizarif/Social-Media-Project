import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class NewTest extends JComponent {

    static JLabel name;
    static JTextArea abMe;
    static JTextArea interests;
    static JLabel contact;
    static JButton fList;
    static JButton change;
    static JButton del;
    static JLabel pic;

    static Client client;

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 4242);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        Account a;

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

                        if (t.indexOf(" ") == 0) {
                            l.add(t.substring(1));
                        } else {
                            l.add(t);
                        }
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
            String newPass = null;
            ender = false;

            do {
                newUser = JOptionPane.showInputDialog(null, "Enter A Username",
                        "Create New Account", JOptionPane.QUESTION_MESSAGE);

                writer.write("C:" + newUser);
                writer.println();
                writer.flush();

                String check = reader.readLine();

                if (check.equals("Y")) {
                    break;
                }

                if(newUser == null) {
                    gui = false;
                    ender = true;
                    break;
                }

            } while (true);

            do {
                if(ender) {
                    break;
                }
                newPass = JOptionPane.showInputDialog(null, "Enter A Password",
                        "Login", JOptionPane.QUESTION_MESSAGE);

                if (newPass == null) {
                    gui = false;
                    ender = true;
                    break;
                }
                if (newPass.isBlank()) {
                } else {
                    break;
                }

            } while (true);

            if (!ender) {

                a = new Account(newUser, newPass);

                writer.write("N:" + a.toString());
                writer.println();
                writer.flush();

            } else {

                a = new Account("break", "break");
            }
        } else {
            gui = false;
            a = new Account("break", "break");
        }

        //Show profile in complex gui with loop, maybe a run method
        if (gui) {

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    JFrame frame = new JFrame("Profile");
                    frame.setLayout(new GridLayout(2,2));
                    frame.setSize(900, 600);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    name = new JLabel("<html><b>" + a.getUsername() + "</b></html>");
                    interests = new JTextArea(a.getLikes().toString());
                    abMe = new JTextArea(a.getAboutMe());
                    contact = new JLabel("<html><b>" + a.getContacts().toString() + "</b></html>");
                    fList = new JButton("Friends List");
                    change = new JButton("Change information");
                    del = new JButton("Delete Profile");
                    JButton exit = new JButton("My Profile");
                    JButton ie = new JButton("Import/Export");
                    JLabel settings = new JLabel("<html><b>Settings:</b></html>");
                    JLabel iTitle = new JLabel("<html><b>My Interests:</b></html>");
                    JLabel aTitle = new JLabel("<html><b>About Me:</b></html>");
                    JLabel pic = new JLabel("<html><b></b></html>");

                    if (a.getPicName().length() > 0) {
                        pic.setIcon(createPicture(a.getPicName()));
                    }

                    abMe.setColumns(35);
                    abMe.setRows(15);
                    abMe.setWrapStyleWord(true);
                    abMe.setLineWrap(true);
                    abMe.setEditable(false);

                    interests.setColumns(35);
                    interests.setRows(15);
                    interests.setWrapStyleWord(true);
                    interests.setLineWrap(true);
                    interests.setEditable(false);

                    JPanel tpanel = new JPanel();
                    tpanel.add(name);
                    tpanel.add(contact);
                    tpanel.add(pic, BorderLayout.SOUTH);
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
                    exit.setVisible(false);
                    frame.add(dpanel);

                    frame.setVisible(true);

                    change.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            String[] standingOptions = {"Username", "Password", "Contacts", "Interests", "About Me", "Profile Picture"};
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

                                                        if (a.contacts.get(0).equals("")) {
                                                            a.contacts.remove(0);
                                                        }

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

                                                        if (a.likes.get(0).equals("")) {
                                                            a.likes.remove(0);
                                                        }

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

                                                    boolean go;

                                                    if (a.likes.contains(newCont)) {
                                                        go = true;
                                                    } else {
                                                        JOptionPane.showMessageDialog(null, "Interest Not Found",
                                                                "Error", JOptionPane.ERROR_MESSAGE);
                                                        break;
                                                    }

                                                    if (go) {

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

                                    case "About Me":

                                        String am = JOptionPane.showInputDialog(null, "Enter your new about me",
                                                "About Me Change", JOptionPane.QUESTION_MESSAGE);

                                        if (am != null) {

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            a.setAboutMe(am);

                                            writer.write("R:" + a.toString());
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

                                            writer.write("D:" + a.toString());
                                            writer.println();
                                            writer.flush();

                                            a.setPicName(profPic);

                                            writer.write("R:" + a.toString());
                                            writer.println();
                                            writer.flush();

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
                                            change.setVisible(false);
                                            del.setVisible(false);
                                            ie.setVisible(false);
                                            fList.setVisible(false);

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
                                                    ie.setVisible(true);
                                                    fList.setVisible(true);
                                                    exit.setVisible(false);

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
            });

        }

    }

    public static ImageIcon createPicture(String s) {

        ImageIcon i = new ImageIcon(s);
        return i;

    }
}
