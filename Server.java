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

        boolean x = false;
        while (true) {

            try {

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

                String command = reader.readLine();
                //if(command != null) {
                 //   System.out.println(command);
                //}

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

                                    writer.write("V:" + line);
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

                    case "D:": //Delete line from accounts file

                        delete("accounts.txt", command.substring(2));
                        break;

                    case "d:": //Delete line from usernames file

                        delete("usernames.txt", command.substring(2));
                        break;

                    case "R:":

                        apw.println(command.substring(2));
                        apw.flush();
                        break;

                    case "r:":

                        upw.println(command.substring(2));
                        upw.flush();
                        break;

                }


                if (x == true) {
                    break;
                }

            } catch (Exception e) {

            }

        }

    }

    private static void delete(String filename, String replace) throws IOException {

        File File = new File(filename);
        FileReader Read = new FileReader(File);
        BufferedReader bfr = new BufferedReader(Read);

        File newFile = new File("temp.txt");
        FileOutputStream fos = new FileOutputStream(newFile, true);
        PrintWriter pw = new PrintWriter(fos);

        while (true) {

            String line = bfr.readLine();

            if (line == null) {
                break;
            }

            if (line.equals(replace) || line.equals("")) {

            } else {
                pw.println(line);
                pw.flush();
            }

        }
        boolean y = File.delete();
        boolean x = newFile.renameTo(new File(filename));

    }

}
