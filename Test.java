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

        frame.setVisible(true);

    }
}

//end of program
