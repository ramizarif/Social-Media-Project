import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NewServer implements Runnable {

    Socket socket;

    public NewServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

        boolean x = false;
        String f = " ";
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

                        System.out.println(command.substring(2));

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

                    case "G:":

                        String s = command.substring(2);

                        while(true) {

                            String l = abfr.readLine();

                            if (l.substring(0, l.indexOf(",")).equals(s)){

                                writer.write(l);
                                writer.println();
                                writer.flush();
                                break;
                            }
                        }

                    case "I:":

                        imprt(command.substring(2));

                    case "E:":

                        String acc = command.substring(command.indexOf(",") + 1);
                        exprt(command.substring(2), acc);

                    case "S:":

                        String s1 = command.substring(2);

                        for (int i = 0; i < 4; i++) {

                            s1 = s1.substring(s1.indexOf("]") + 1);

                        }

                        s1 = s1.substring(1);

                        writer.write(s1);
                        writer.println();
                        writer.flush();
                        break;

                    case "O:":

                        String o = command.substring(2);
                        writer.write(codeCheck(o));
                        writer.println();
                        writer.flush();
                        break;

                    case "X:":

                        String p = command.substring(2);

                        if (p.equals(f)) {

                            writer.write("Y");
                            writer.println();
                            writer.flush();
                            break;

                        } else {

                            writer.write("N:" + p);
                            writer.println();
                            writer.flush();
                            f = p;
                            break;
                        }


                }


                if (x) {
                    break;
                }

            } catch (Exception e) {

            }

        }

        } catch (IOException e) {
        }

    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(4242);

        while (true) {

            Socket socket = serverSocket.accept();
            NewServer server = new NewServer(socket);
            new Thread(server).start();

        }

    }

    public static void delete(String filename, String replace) throws IOException {

        File File = new File(filename);
        FileReader Read = new FileReader(File);
        BufferedReader bfr = new BufferedReader(Read);

        File newFile = new File("temp.txt");
        FileOutputStream fos = new FileOutputStream(newFile, true);
        PrintWriter pw = new PrintWriter(fos);

        while (true) {

            String line = bfr.readLine();
            System.out.println(line);

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

        bfr.close();
        pw.close();

    }

    public static void imprt(String filename) throws IOException {

        File File = new File(filename);
        FileReader Read = new FileReader(File);
        BufferedReader bfr = new BufferedReader(Read);

        File f = new File("accounts.txt");
        FileOutputStream afos = new FileOutputStream(f, true);
        PrintWriter pw = new PrintWriter(afos);

        File F = new File("usernames.txt");
        FileOutputStream fos = new FileOutputStream(F, true);
        PrintWriter upw = new PrintWriter(fos);

        while (true) {

            String line = bfr.readLine();

            if (line == null) {
                break;
            }

            String s = line.substring(0, line.indexOf(","));
            upw.println(s);
            upw.flush();

            pw.println(line);
            pw.flush();

        }

        upw.close();
        pw.close();
        bfr.close();

    }

    public static void exprt(String filename, String acc) throws IOException {

        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file, true);
        PrintWriter pw = new PrintWriter(fos);

        pw.println(acc);
        pw.close();

    }

    public static String codeCheck(String code) throws IOException {

        File file = new File("accounts.txt");
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);

        while (true) {

            String line = bfr.readLine();

            if (line == null) {
                break;
            }

            for (int i = 0; i < 4; i++) {

                line = line.substring(line.indexOf("]") + 1);

            }

            if (code.equals(line.substring(1))) {
                return "N";
            }
        }

        return "Y";

    }
}