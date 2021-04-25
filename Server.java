import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(4242);
        Socket socket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        File aFile = new File("accounts.txt");
        FileReader aRead = new FileReader(aFile);
        BufferedReader abfr = new BufferedReader(aRead);
        FileOutputStream afos = new FileOutputStream(aFile, true);
        PrintWriter apw = new PrintWriter(afos);

        File uFile = new File("usernames.txt");
        FileReader uRead = new FileReader(uFile);
        BufferedReader ubfr = new BufferedReader(uRead);
        FileOutputStream ufos = new FileOutputStream(uFile, true);
        PrintWriter upw = new PrintWriter(ufos);

        boolean x = false;
        while (true) {

            try {

                String command = reader.readLine();
                String type = command.substring(0, 2);

                //Each time the server receives a line, it will have a header with a letter that will direct it
                // to what it needs to do

                switch (type) {

                    case "A:"://Checks if username and password is in database. If so, return "v" for valid, else return "i" for invalid

                        boolean y = true;
                        String line;

                        while (true) {

                            try {

                                line = abfr.readLine();

                                if (line == null) {
                                    break;
                                }

                                int ind = command.length() - 2;

                                if (line.substring(0, ind).equals(command.substring(2))) {

                                    writer.write("V");
                                    writer.println();
                                    writer.flush();
                                    y = false;
                                    break;

                                }

                            } catch (IOException e) {

                            }
                        }

                        if (y) {
                            writer.write("X:");
                            writer.println();
                            writer.flush();
                        }
                        break;

                    case "C:": //Checks if username is not in database returns "Y" if new

                        boolean q = true;
                        String line1;

                        while (true) {

                            try {

                                line1 = ubfr.readLine();

                                if (line1 == null) {
                                    break;
                                }

                                if (command.substring(2).equals(line1)) {
                                    writer.write("N");
                                    writer.println();
                                    writer.flush();
                                    q = false;
                                    break;
                                }

                            } catch (IOException e) {

                            }
                        }

                        if (q) {
                            writer.write("Y");
                            writer.println();
                            writer.flush();
                        }
                        break;

                    case "N:": //Creates a new account in database

                        String ac = command.substring(2);
                        String user = ac.substring(0, ac.indexOf(","));

                        apw.println(ac);
                        apw.flush();

                        upw.println(user);
                        upw.flush();

                        break;



                }


                if (x == true) {
                    break;
                }

            } catch (Exception e) {

            }
        }

        writer.close();
        reader.close();

    }

}
