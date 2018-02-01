import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class InetClient {

    public static void main(String[] args) {
        String serverName;
        if (args.length < 1) { //if there are no arguments added when running the class, then serverName is localhost
            serverName = "localhost";
        } else {
            serverName = args[0]; //otherwise, serverName is assigned the first argument provided
        }

        System.out.println("Chris Shikami's Inet Client, 1.8\n"); //print out in client window
        System.out.println("Using server: " + serverName + ", Port: 26050"); //print out in client window, serverName being either localhost or the string provided as argument by user
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //create object to read system input into BufferedReader
        try {
            String name;
            do {
                System.out.print("Enter a hostname or an IP address, (quit to end: "); //print out to client window
                System.out.flush(); //have to flush the output because we don't want a newline after previous printed print statement
                name = in.readLine();
                if (name.indexOf("quit") < 0) {  //if the user does not write quit in system input..
                    getRemoteAddress(name, serverName); //call getRemoteAddress method with name and serverName arguments
                }
                } while (name.indexOf("quit") < 0);  //if user input in client window is quit...
                System.out.println("Cancelled by user request."); //print out Cancelled by user request
        }
        catch (IOException x) { //if there is an exception
            x.printStackTrace(); //print stacktrace
        }
    }

    static String toText (byte ip[]) { /* Make portable for 128 bit format */
        StringBuffer result = new StringBuffer();//new StringBuffer object named result
        //format the ip address:
        for (int i = 0; i < ip.length; ++ i) {
            if (i > 0) {
                result.append(".");
            }
            result.append(0xff & ip[i]);
        }
        return result.toString(); //return StringBuffer object as string
    }

    static void getRemoteAddress (String name, String serverName) {
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try {
            //new Socket object with serverName and port number arguments
            sock = new Socket(serverName, 26050);

            //Create filter I/O streams for the socket:
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //get input stream from server
            toServer = new PrintStream(sock.getOutputStream()); //send output stream to server

            //Send machine name or IP address to server:
            toServer.println(name);
            toServer.flush();

            //Read two or three lines of response from the server,
            //and block while synchronously waiting:
            for  (int i = 1; i <= 3; i++) {
                textFromServer = fromServer.readLine();
                if (textFromServer != null) { //if textFromServer is not null
                    System.out.println(textFromServer); //print text response from server
                }
            }
            sock.close(); //close socket connection
        }
        catch (IOException x) { //print exception stack trace if there is an IOException
            System.out.println("Socket error.");  //print Socket error. above stack trace
            x.printStackTrace(); //print exception stack trace
        }

    }
}