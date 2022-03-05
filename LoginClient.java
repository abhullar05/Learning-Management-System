import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * LoginClient.java
 *
 *
 * LoginClient.java is the client portion of the login and create
 * account process.
 *
 * @author group #85
 * @version December 13, 2021
 */

public class LoginClient {
    private static PrintWriter pw; // to write to server
    private static BufferedReader bfr; // to read from server
    private static ObjectOutputStream oos; // to write objects to server
    private static ObjectInputStream ois; // to read objects from server
    private static JFrame frame = new JFrame("Learning Management System"); // creates the JFrame which will contain everything;
    private static JPanel panel1; // the JPanel which will be used when needed in methods and will be added and removed again
    private static JPanel panel2; // the JPanel which will contain create account and login buttons
    private static String role; // stores the value of role


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LoginClient loginClient = new LoginClient(frame, pw, bfr, oos, ois); // calls loginClient to handle login related GUIs
        loginClient.loginOrCreateAccountClient(loginClient);
    }

    public LoginClient(JFrame frame, PrintWriter pw, BufferedReader bfr, ObjectOutputStream oos, ObjectInputStream ois) {
        this.pw = pw;
        this.bfr = bfr;
        this.oos = oos;
        this.ois = ois;
        this.frame = frame;
    }

    // this method is called in the main method of Client and handles login and create account GUIs
    public static void loginOrCreateAccountClient(LoginClient loginClient) throws IOException, ClassNotFoundException {
        // displays the login / create account dialog and stores the option selected

        showAccountOptionsDialog(loginClient);
    }

    // this method displays the options to create an account and login
    public static void showAccountOptionsDialog(LoginClient loginClient) {
        addCreateAccountLoginButtons(loginClient); // creates and adds create account and sign in buttons

    }

    // creates and adds create account and sign in buttons
    public static void addCreateAccountLoginButtons(LoginClient loginClient) {
        panel2 = new JPanel();
        JLabel welcomeMessage = new JLabel("Welcome to the Learning Management System."); // displays welcome message
        JButton createAccountButton = new JButton("Create a new account"); // to create a new account
        JButton loginButton = new JButton("Login"); // to login
        createAccountButton.setActionCommand("Create a new account"); // action command is set for create account button to communicate to server
        loginButton.setActionCommand("Login"); // action command is set for login button to communicate to server
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // listens for create account button
                try {
                    oos.writeObject(createAccountButton);
                    oos.flush();
                    showCreateAccountDialog(loginClient);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        loginButton.addActionListener(new ActionListener() { // listens for login button
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(loginButton);
                    oos.flush();
                    showLoginDialog(loginClient);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        panel2.add(welcomeMessage); // adds welcome message to panel
        panel2.add(createAccountButton); // adds create account button to panel
        panel2.add(loginButton); // adds login button to panel
        frame.getContentPane().removeAll();
        frame.repaint(); // so that JFrame does not freeze
        frame.add(panel2); // adds panel to the frame
        frame.setVisible(true);





    }

    // this method displays the edit account options :  edit username, edit password, edit account type
    public static void showEditAccountOptionsDialog() {
        JPanel panel = new JPanel();
        JLabel optionSelection = new JLabel("Please select the change you would like to make to your account :"); // option selection message
        JButton editUsernameButton = new JButton("Edit Username"); // to edit username
        JButton editPasswordButton = new JButton("Edit Password"); // to edit password
        JButton editRoleButton = new JButton("Edit account type"); // to edit account type
        JButton returnButton = new JButton("Return"); // to return
        editUsernameButton.setActionCommand("edit username"); // action command is set for edit username button to communicate to server
        editPasswordButton.setActionCommand("edit password"); // action command is set for edit password button to communicate to server
        editRoleButton.setActionCommand("edit role"); // action command is set for edit role button to communicate to server
        returnButton.setActionCommand("return");
        editUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // listens for edit username button

                try {
                    oos.writeObject(editUsernameButton); // to send edit username button to server
                    oos.flush();
                    showEditUsernameDialog();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        editPasswordButton.addActionListener(new ActionListener() { // listens for edit password button
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(editPasswordButton); // to send edit password button to server
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                showEditPasswordDialog();

            }
        });
        editRoleButton.addActionListener(new ActionListener() { // listens for edit role button
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(editRoleButton); // to send edit role button to server
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                showEditRoleDialog();
            }
        });
        returnButton.addActionListener(new ActionListener() { // listens for return button
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(returnButton);
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                frame.getContentPane().removeAll(); // clears the frame
                frame.repaint(); // so that JFrame does not freeze
                frame.add(panel1); // adds panel containing
                frame.setVisible(true);
            }
        });
        panel.add(optionSelection); // adds option selection message to the panel
        panel.add(editUsernameButton); // adds edit username button to panel
        panel.add(editPasswordButton); // adds edit password button to panel
        panel.add(editRoleButton); // adds edit role button to panel
        panel.add(returnButton); // adds return button to panel
        frame.getContentPane().removeAll(); // clears the frame
        frame.repaint(); // so that JFrame does not freeze
        frame.add(panel); // adds panel to the frame
        frame.setVisible(true);
    }



    // this method will display dialog to enter username, password and select role;
    public static void showCreateAccountDialog(LoginClient loginClient) throws ClassNotFoundException, IOException {
        panel1 = new JPanel(); // this JPanel will contain all things to be displayed by the method
        panel1.setSize(100, 600);
        JLabel usernamePrompt = new JLabel("Please enter a username");
        JLabel passwordPrompt = new JLabel("Please enter a password");
        JTextField usernameText = new JTextField(10); // text field to enter username
        JTextField passwordText = new JTextField(10); // text field to enter password
        JLabel rolePrompt = new JLabel("Please select whether you are a student or teacher :"); // asks whether user is a student or teacher
        JButton teacherButton = new JButton("Teacher"); // select button for teacher
        teacherButton.setActionCommand("Teacher"); // setting action command for communication with server
        JButton studentButton = new JButton("Student"); // select button for student
        studentButton.setActionCommand("Student"); // setting action command for communication with server
        JButton continueButton = new JButton("continue");
        continueButton.setActionCommand("continue"); // setting action command for communication with server

        teacherButton.addActionListener(new ActionListener() { // listens for teacher button and stores "teacher" in static variable role
            @Override
            public void actionPerformed(ActionEvent e) {

                role = "teacher";
            }
        });
        studentButton.addActionListener(new ActionListener() { // listens for student button and stores "student" in static variable role
            @Override
            public void actionPerformed(ActionEvent e) {
                role = "student";
            }
        });
        continueButton.addActionListener(new ActionListener() { // listens for continue button
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = ""; // this local variable store username


                try {
                    username = isUsernameValid(usernameText); // checking if username is valid
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                String password = isPasswordValid(passwordText); // stores password

                while (role == null){

                }

                writeUsernamePasswordRole(username, password); // sends the username, password, role to server while creating account
                addButtons(loginClient); // shows the option edit, delete account, continue, return to main menu

            }
        });
        panel1.add(usernamePrompt); // add enter username label to panel
        panel1.add(usernameText); // add username text field to panel
        panel1.add(passwordPrompt); // add enter password label to panel
        panel1.add(passwordText); // add password text field to panel
        panel1.add(rolePrompt); // add role prompt to panel
        panel1.add(teacherButton); // add teacher button to panel
        panel1.add(studentButton); // add student button to panel
        panel1.add(continueButton); // add continue button to panel
        frame.getContentPane().removeAll(); // removes whatever was there previously
        frame.repaint(); // so that JFrame does not freeze
        frame.add(panel1); // adding the JPanel created
        frame.setVisible(true); // displaying the changes



    }
    // checks if username is valid and displays error if invalid
    public static String isUsernameValid(JTextField usernameText) throws IOException, ClassNotFoundException {
        String input;

        ArrayList<String> usernames = (ArrayList<String>) ois.readObject(); // reads the String[] which contains list of courses from server


        do {

            input = usernameText.getText(); // gets input fromm text field
            if (input.isEmpty()) { // shows error message dialog if username is empty
                JOptionPane.showMessageDialog(null, "username cannot be empty", "Username", JOptionPane.ERROR_MESSAGE);
            } else if (input.contains(" ")) { // shows error message if username contains blank space
                JOptionPane.showMessageDialog(null, "Username should not contain blank spaces !", "Username", JOptionPane.ERROR_MESSAGE);
            } else if (usernames.contains(input)) { // shows error message is username is already in use
                JOptionPane.showMessageDialog(null, "Please enter a different username. This username is already in use", "Username", JOptionPane.ERROR_MESSAGE);
            }
        } while (input.isEmpty() || input.contains(" ") || usernames.contains(input)); // loops back and asks for username again when error message is thrown


        return input;

    }
    // checks if password is valid and displays error if invalid
    public static String isPasswordValid(JTextField passwordText) {

        String input2; // this variable stores input for password while creating new account
        do {
            // takes the input for password
            input2 = passwordText.getText();
            // If user clicks on close button, then program closes with thank you message.

            if (input2.isEmpty()) { // shows error message dialog if password is empty
                JOptionPane.showMessageDialog(null, "password cannot be empty", "Password", JOptionPane.ERROR_MESSAGE);
            } else if (input2.contains(" ")) { // shows error message if password contains blank space
                JOptionPane.showMessageDialog(null, "Password should not contain blank spaces !", "Password", JOptionPane.ERROR_MESSAGE);
            }
        } while (input2.isEmpty() || input2.contains(" ")); // loops back and asks for password again when error message is thrown
        return input2;
    }

    // writes username, password and role to server while creating a new account
    public static void writeUsernamePasswordRole(String username, String password) {
        pw.println(username);

        pw.flush();
        pw.println(password);
        pw.flush();
        pw.println(role);
        pw.flush();
    }
    // shows the dialog for login
    public static void showLoginDialog(LoginClient loginClient){
        panel1 = new JPanel(); // this JPanel will contain all things to be displayed by the method
        panel1.setSize(100, 600);
        JLabel usernamePrompt = new JLabel("Please enter your username");
        JLabel passwordPrompt = new JLabel("Please enter your password");
        JTextField usernameText = new JTextField(10); // text field to enter username
        JTextField passwordText = new JTextField(10); // text field to enter password
        JButton loginButton = new JButton("Login");
        loginButton.setActionCommand("Login"); // setting action command for communication with server

        loginButton.addActionListener(new ActionListener() { // listens for login button
            @Override
            public void actionPerformed(ActionEvent e) {

                // the code below checks to see whether an account with the provided login details exists
                ArrayList<String> usernames = new ArrayList<>();
                ArrayList<String> passwords = new ArrayList<>();

                try {
                    usernames = (ArrayList<String>) ois.readObject(); // reads the ArrayList<String> which contains list of usernames from server

                    passwords = (ArrayList<String>) ois.readObject(); // reads the ArrayList<String> which contains list of passwords from server
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                String usernameInput;

                String passwordInput;

                boolean detailsVerified = false; // tells if user details have been verified

                usernameInput = usernameText.getText(); // gets the username input
                passwordInput = passwordText.getText(); // gets the password input
                try {
                    detailsVerified = verifyLoginDetails(usernameInput, passwordInput, usernames, passwords);
                    oos.writeObject(detailsVerified);
                    oos.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }


                if(detailsVerified) {
                    writeUsernamePasswordLogin(usernameInput, passwordInput);
                    addButtons(loginClient);
                }


            }
        });
        panel1.add(usernamePrompt); // add enter username label to panel
        panel1.add(usernameText); // add username text field to panel
        panel1.add(passwordPrompt); // add enter password label to panel
        panel1.add(passwordText); // add password text field to panel
        panel1.add(loginButton); // add login button to panel
        frame.getContentPane().removeAll(); // removes whatever was there previously
        frame.repaint(); // so that JFrame does not freeze
        frame.add(panel1); // adding the JPanel created
        frame.setVisible(true); // displaying the changes
    }

    // the method below checks to see whether an account with the provided login details exists
    private static boolean verifyLoginDetails(String input, String input2, ArrayList<String> usernames, ArrayList<String> passwords) throws IOException, ClassNotFoundException {

        if (usernames.contains(input)) {
            int indexOfUsername = usernames.indexOf(input);
            if (passwords.get(indexOfUsername).equals(input2)) {
                return true;
            }
        } else {
            // shows error message if login details are not verified
            JOptionPane.showMessageDialog(null, "Please try again", "Login", JOptionPane.ERROR_MESSAGE);


        }
        return false;
    }
    // writes login info to server
    public static void writeUsernamePasswordLogin(String username, String password) {
        pw.println(username);
        pw.flush();
        pw.println(password);
        pw.flush();
    }
    // this method displays the GUI with continue, edit account, delete account ,and return to main menu button
    public static void showAccountEditOptionsDialog(LoginClient loginClient){
        // so that the GUI runs on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel1 = new JPanel();// creates the JPanel which will contain edit, continue, delete, return to main menu buttons and will be added to the top level panel
                addButtons(loginClient); // creates and adds edit, continue, delete, return to main menu buttons to the panel
                frame.getContentPane().removeAll(); // removes whatever was there previously
                frame.repaint(); // so that JFrame does not freeze
                frame.add(panel1); // adding the JPanel created
                frame.setVisible(true); // displaying the changes
            }
        });
    }
    // this method will display continue, edit account, delete account and return to main menu buttons
    public static void addButtons(LoginClient loginClient){
        panel1 = new JPanel(); // this panel will contain continue, edit account, delete account and return to main menu buttons
        JButton continueButton = new JButton("CONTINUE"); // continues
        JButton editButton = new JButton("EDIT ACCOUNT"); // edits account
        JButton deleteButton = new JButton("DELETE ACCOUNT"); // deletes account
        JButton returnToMainMenuButton = new JButton("RETURN TO MAIN MENU"); // returns to main menu
        continueButton.setActionCommand("continue"); // for communication with server
        editButton.setActionCommand("edit account"); // for communication with server
        deleteButton.setActionCommand("delete account"); // for communication with server
        returnToMainMenuButton.setActionCommand("return to main menu"); // for communication with server
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(editButton);
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                showEditAccountOptionsDialog(); // shows the dialog which gives the user options to edit username, password or account type


            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(deleteButton); // write the button to server
                    oos.flush();
                    frame.getContentPane().removeAll(); // clears the frame
                    frame.repaint(); // so that JFrame does not freeze
                    frame.add(panel2); // adds the panel2 containing login and create account buttons
                    frame.setVisible(true); // makes the changes visible
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // have to write code to loop back

            }
        });
        returnToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(returnToMainMenuButton);
                    oos.flush();
                    frame.getContentPane().removeAll(); // clears the frame
                    frame.repaint(); // so that JFrame does not freeze
                    frame.add(panel2); // adds the panel2 containing login and create account buttons
                    frame.setVisible(true); // makes the changes visible
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // have to write code to loop back
            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(continueButton);
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                frame.getContentPane().removeAll(); // removes everything from the JFrame so that next step can happen
                frame.repaint(); // so that JFrame does not freeze

                try {
                    String userRole = bfr.readLine();
                    if(userRole.equals("student")){
                        StudentClient.startOfStudentGUI(frame, loginClient, oos, ois, pw, bfr, panel1);

                          //  frame.getContentPane().removeAll(); // clears the frame
                          //  frame.repaint(); // so that JFrame does not freeze
                          //  frame.add(panel2); // adds the panel2 containing login and create account buttons
                          //  frame.setVisible(true); // makes the changes visible

                    } else if(userRole.equals("teacher")){
                        TeacherContinueClient.showTeacherContinueGUI(frame, pw, ois, oos, bfr);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
// adds continue, edit account, delete account and return to main menu buttons to the panel
        panel1.add(continueButton);
        panel1.add(editButton);
        panel1.add(deleteButton);
        panel1.add(returnToMainMenuButton);
        frame.getContentPane().removeAll(); // to clear the frame
        frame.repaint(); // so that JFrame does not freeze
        frame.add(panel1); // adds panel to the frame
        frame.setVisible(true);



    }
    // this method edits the username when edit username button is clicked
    public static void showEditUsernameDialog() throws IOException, ClassNotFoundException {
        ArrayList<String> usernames = (ArrayList<String>) ois.readObject(); // reads the ArrayList of string which contains list of usernames from server

        String input; // this variable stores input for username while editing the account
        do {
            // takes the input for username
            input = JOptionPane.showInputDialog(null, "Please enter the new username.", "Edit Account", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {

            } else if (input.isEmpty()) { // shows error message dialog if username is empty
                JOptionPane.showMessageDialog(null, "username cannot be empty", "Username", JOptionPane.ERROR_MESSAGE);
            } else if (input.contains(" ")) { // shows error message if username contains blank space
                JOptionPane.showMessageDialog(null, "username should not contain blank spaces !", "Username", JOptionPane.ERROR_MESSAGE);
            } else if (usernames.contains(input)) { // shows error message is username is already in use
                JOptionPane.showMessageDialog(null, "Please enter a different username. This username is already in use", "Username", JOptionPane.ERROR_MESSAGE);
            }
        } while (input.isEmpty() || input.contains(" ") || usernames.contains(input)  ); // loops back and asks for username again when error message is thrown

        pw.println(input); // sends the new username to server
        pw.flush();
    }
    // this method edits the password when edit password button is clicked
    public static void showEditPasswordDialog(){
        String input2; // this variable stores input for password while editing the account
        do {
            // takes the input for password
            input2 = JOptionPane.showInputDialog(null, "Please enter the new password.", "Edit Account", JOptionPane.QUESTION_MESSAGE);

            if (input2 == null) {

            } else if (input2.isEmpty()) { // shows error message dialog if password is empty
                JOptionPane.showMessageDialog(null, "password cannot be empty", "Password", JOptionPane.ERROR_MESSAGE);
            } else if (input2.contains(" ")) { // shows error message if password contains blank space
                JOptionPane.showMessageDialog(null, "Password should not contain blank spaces !", "Password", JOptionPane.ERROR_MESSAGE);
            }
        } while (input2.isEmpty() || input2.contains(" ")  ); // loops back and asks for password again when error message is thrown

        pw.println(input2); // sends the new password to server
        pw.flush();
    }
    //this method edits the role when edit role button is clicked
    public static void showEditRoleDialog(){
        String teacherOrStudentCreateAccountMessage = "Please select your new account type :" + "\n";//select new account type message
        String[] teacherOrStudentCreateAccountArray = {"Teacher", "Student"};
        int studentOrTeacher = JOptionPane.showOptionDialog(null, teacherOrStudentCreateAccountMessage, "Edit account type", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, teacherOrStudentCreateAccountArray, teacherOrStudentCreateAccountArray[0] );
        // the above line displays the required dialog


        // If user clicks on close button, then program closes with thank you message.
        if(studentOrTeacher == JOptionPane.CLOSED_OPTION){

        } else if(studentOrTeacher == JOptionPane.YES_OPTION){

            pw.println("teacher"); // sends "teacher" to server if teacher button is pressed
            pw.flush();
        } else {
            pw.println("student"); // sends "student" to server if student button is pressed
            pw.flush();
        }

    }





}
