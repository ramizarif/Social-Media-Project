import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

                        case "Z:":
                        String searchedName = command.substring(2);
                        //System.out.println(searchedName); 
                        ArrayList<String> empty = new ArrayList<>(); 
                        String accLines = abfr.readLine(); 
                        while (accLines != null) { 
                            String[] accSplit = accLines.split(",");
                            String accName = accSplit[0];
                            if (accName.equals(searchedName)) {
                                writer.write("N:" + accLines);
                                writer.println();
                                writer.flush();
                                empty.add("a");
                                break; 
                            }
                            accLines = abfr.readLine();
                        }
                        //no results
                        String noResults = "No Results";
                        if (empty.size() < 1) {
                            writer.write("I:" + noResults);
                            writer.println();
                            writer.flush(); 
                        }
    
                        break;
    
                        case "K:":
                            
                            //split, find searched acc line, add a to fq lsit
                            String[] typeSplit = command.split(":");
                            String aName = typeSplit[1];
                            String bName = typeSplit[2];
                            String eachLine = abfr.readLine();
                            while (eachLine != null) {
                                String[] accSplit = eachLine.split(",");
                                String accName = accSplit[0];
                                if (accName.equals(bName)) {
                                    //b's line is found - add a's username into the last brackets
                                    //create a new line that will be added to the txt and delete the old one before 
                                    int startIndex = eachLine.lastIndexOf("[");
                                    String newFrLine = insertString(eachLine, startIndex, aName + ",");
                                    delete("accounts.txt", eachLine); 
                                    delete("usernames.txt", bName); 
                                    upw.println(bName);
                                    upw.flush();
                                    apw.println(newFrLine); 
                                    apw.flush();
                                    System.out.println(newFrLine); 
                                    break; 
                                    
                                }
                                eachLine = abfr.readLine();
                            }
    
                            break;
    
                        case "Q:": 
                            //have to do the same thing as K but in the friends list
                            //Q:dom:ram
                            typeSplit = command.split(":");
                            aName = typeSplit[1];
                            bName = typeSplit[2];
                            eachLine = abfr.readLine();
                            boolean xBool = false;
                            boolean yBool = false; 
                                while (eachLine != null) {
                                    String[] accSplit = eachLine.split(",");
                                    String accName = accSplit[0];
    
                                    if (accName.equals(bName)) {
                    
                                        String newFLine = "";
                                        int startIndex = eachLine.indexOf("]") + 2;
                                        //System.out.println(eachLine.substring(eachLine.indexOf("]") + 3, eachLine.indexOf("]") + 4));
                                        /*
                                        if (eachLine.substring(eachLine.indexOf(startIndex+3), eachLine.indexOf(startIndex+4)).equals("]")) {
                                            newFLine = insertString(eachLine, startIndex, aName);
                                        } else {
                                            newFLine = insertString(eachLine, startIndex, "," + aName);
                                        }
                                        */
                                        newFLine = insertString(eachLine, startIndex, aName + ",");
                                        delete("accounts.txt", eachLine); 
                                        delete("usernames.txt", aName); 
                                        upw.println(aName);
                                        upw.flush();
                                        apw.println(newFLine); 
                                        apw.flush();
                                        System.out.println(newFLine + "\n");
                                        xBool = true;       
                                    }
                                
                                    if (accName.equals(aName)) {
                                        //this is where i have to delete whats in friend request
                            
                                        int startIndex = eachLine.indexOf("]") + 2;
                                        String newFLine = insertString(eachLine, startIndex, bName + ",");
                                        int strSplice = newFLine.indexOf(bName, newFLine.indexOf(bName) + 1);
                                        String fLine = newFLine.substring(0, strSplice) + "" + newFLine.substring(strSplice + bName.length());
                                        delete("accounts.txt", eachLine); 
                                        delete("usernames.txt", aName); 
                                        upw.println(aName);
                                        upw.flush();
                                        apw.println(fLine); 
                                        apw.flush();
                                        System.out.println(newFLine + "\n");
                                        yBool = true;
                                        
                                    }
    
                                    if(xBool && yBool) {
                                        break;
                                    }
    
                                    eachLine = abfr.readLine(); 
                                } 
    
                    
                            break;
                            
                        case "s:": 
                            System.out.println(command);
                            String[] infoSplit = command.split(":");
                            String nameUser = infoSplit[1];
                            String target = infoSplit[2];
                            String checkLines = abfr.readLine();
                            while (checkLines != null) {
                                String[] accSplit = checkLines.split(",");
                                String accName = accSplit[0];
                                if (nameUser.equals(accName)) {
                                    System.out.println("Not the problem");
                                    //delete whats in the frq slot now and replace newFq
                                    String newLine = checkLines.replace(target + ",", "");
                                    System.out.println(newLine);
                                    delete("accounts.txt", checkLines);
                                    apw.println(newLine);
                                    apw.flush();
                                    break; 
                                }
                                checkLines = abfr.readLine();
                            }
                            break;        


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

        System.out.println(y + " " + x);

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

    public static String insertString(String originalString, int position,
  String toBeInserted) {

        StringBuffer buffer = new StringBuffer(originalString);
        buffer.insert(position + 1, toBeInserted);
        String newString = buffer.toString();

 return newString;
}


}
