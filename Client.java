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

public class Client extends JComponent implements Runnable {

    JLabel name;
    JLabel abMe;
    JLabel interests;
    JButton fList;

    Client client;

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

                if (account.equals("V")) {

                    a = new Account(username, password);
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

        SwingUtilities.invokeLater(new Client());



        writer.close();
        reader.close();


    }

    @Override
    public void run() {

        JFrame frame = new JFrame("Profile");
        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());
        client = new Client();
        content.add(client, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBackground(Color.blue);
        frame.setVisible(true);

        name = new JLabel("Name");
        interests = new JLabel("Soccer, Golf, Sports");
        abMe = new JLabel("I am Me");
        fList = new JButton("Friends List");

        JPanel tpanel = new JPanel();
        tpanel.add(name);
        tpanel.setBackground(Color.orange);
        tpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        content.add(tpanel, BorderLayout.NORTH);

        JPanel rpanel = new JPanel();
        rpanel.add(interests);
        rpanel.setBackground(Color.orange);
        rpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        content.add(rpanel, BorderLayout.EAST);

        JPanel cpanel = new JPanel();
        cpanel.add(abMe);
        cpanel.setBackground(Color.orange);
        cpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        content.add(cpanel, BorderLayout.WEST);

        JPanel dpanel = new JPanel();
        dpanel.add(fList);
        dpanel.setBackground(Color.orange);
        dpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        content.add(dpanel, BorderLayout.SOUTH);

        fList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {



            }
        });


    }
}
