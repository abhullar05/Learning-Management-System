import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
/**
 * Client.java
 *
 * Client.java is the main component of the client side of the project.
 * It sets up the host socket, the necessary server writers and readers,
 * the run method for the client side, and the contains the createGUI method,
 * which is the starting point for the complex GUIs. It also produces
 * loginClient to transfer the server writers and readers
 * across the GUIs.
 *
 * @author Team #85
 *
 * @version December 13, 2021
 *
 */

public class Client {
    private static JFrame frame; // the JFrame which will be passed as argument to methods of other classes
    private static String hostName;
    private static int portNumber;
    public static void main(String[] args) throws IOException {

        try {
            JOptionPane.showMessageDialog(null,
                    "Welcome to the Learning Management System", "",
                    JOptionPane.PLAIN_MESSAGE);
            hostName = JOptionPane.showInputDialog(null,
                    "Please enter the host name",
                    "LMS", JOptionPane.QUESTION_MESSAGE);
            if (hostName == null) {
                JOptionPane.showMessageDialog(null, "Thanks for using the Learning Management System");
                return;
            }

            String portNumberString = JOptionPane.showInputDialog(null,
                    "Please enter the port number",
                    "LMS", JOptionPane.QUESTION_MESSAGE);
            if (portNumberString == null) {
                JOptionPane.showMessageDialog(null, "Thanks for using the Learning Management System");
                return;
            }
            Socket socket = new Socket();
            try {
                int portNumber = Integer.parseInt(portNumberString);
                // Port number : 8000, DNS : "localhost"

                socket = new Socket(hostName, portNumber);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error establishing connection",
                        "LMS", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Connection established ! ",
                    "Client GUI", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e){
            e.printStackTrace();
        }

        Socket socket = new Socket("localhost", 8000); // creates socket with server, host name : localhost(the server and client are on the same computer), port number : 8000
        PrintWriter pw = new PrintWriter(socket.getOutputStream()); // creates print writer to write to server
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream())); // creates buffered reader to read from server
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); // open object output stream to write objects to server
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); // open object input stream to read objects from server
        // so that the GUI runs on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                frame.setLocationRelativeTo(null); // displays JFrame in center of the screen
                frame.setSize(300, 200);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        pw.close();
                        try {
                            bfr.close();
                            oos.close();
                            ois.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        frame.dispose();

                    }
                });
                try {
                    createGUI(pw, bfr, oos, ois); // this method creates GUI
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    private static void getConnectionDetails() {
        try {
            JOptionPane.showMessageDialog(null,
                    "Welcome to the Learning Management System", "",
                    JOptionPane.PLAIN_MESSAGE);
            hostName = JOptionPane.showInputDialog(null,
                    "Please enter the host name",
                    "LMS", JOptionPane.QUESTION_MESSAGE);
            if (hostName == null) {
                JOptionPane.showMessageDialog(null, "Thanks for using the Learning Management System");
                return;
            }

            String portNumberString = JOptionPane.showInputDialog(null,
                    "Please enter the port number",
                    "LMS", JOptionPane.QUESTION_MESSAGE);
            if (portNumberString == null) {
                JOptionPane.showMessageDialog(null, "Thanks for using the Learning Management System");
                return;
            }
            Socket socket = new Socket();
            try {
                int portNumber = Integer.parseInt(portNumberString);
                // Port number : 8000, DNS : "localhost"

                socket = new Socket(hostName, portNumber);
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null,
                        "Error establishing connection",
                        "LMS", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Connection established ! ",
                    "Client GUI", JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void createGUI(PrintWriter pw, BufferedReader bfr, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        LoginClient loginClient = new LoginClient(frame, pw, bfr, oos, ois); // creates a LoginClient object to pass print writer, buffered reader, object output stream and object input stream as argument
        loginClient.loginOrCreateAccountClient(loginClient); // calls loginOrCreateAccountClient to handle login related GUIs
    }

}
