import javax.swing.*;

import java.io.*;
import java.util.ArrayList;

public class Login {

    private String username; // username of the user
    private String password; // password of the user
    private String role; // this string tells whether user is a student or teacher
    private static ArrayList<String > usernames; // contains all the usernames
    private static ArrayList<String > passwords; // contains all the passwords
    private static ArrayList<String> roles; // contains all the roles(teacher or student)
    private static Object gatekeeper = new Object(); // so that multiple threads do not update the same static fields and face race conditions

    // this constructor creates an object at the time when you create a new account and saves the details to UserDetails.txt file
    public Login(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        synchronized (gatekeeper) {
            usernames.add(username);
            passwords.add(password);
            roles.add(role);
        }
        synchronized (gatekeeper) {
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream("UserDetails.txt", true));
                pw.printf(username + " " + password + " " + role + "\n");
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // this constructor creates an object at the time when user logs in
    public Login (String username, String password) throws IndexOutOfBoundsException{
        this.username = username;
        this.password = password;
        role = roles.get(usernames.indexOf(username));

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    public static void setUsernames(ArrayList<String> usernames) {
        synchronized (gatekeeper) {
            Login.usernames = usernames;
        }
    }

    public static ArrayList<String> getPasswords() {
        return passwords;
    }

    public static void setPasswords(ArrayList<String> passwords) {
        synchronized (gatekeeper) {
            Login.passwords = passwords;
        }
    }

    public static ArrayList<String> getRoles() {
        return roles;
    }

    public static void setRoles(ArrayList<String> roles) {
        synchronized (gatekeeper) {
            Login.roles = roles;
        }
    }

    // this method checks if there exists any user details file and reads it, if a user details file does not exist then creates one.
    private static void readUserDetails() throws IOException {
        File userDetails = new File("UserDetails.txt");
        if(userDetails.createNewFile()){

        } else{
            BufferedReader bfr = new BufferedReader(new FileReader(userDetails));
            usernames = new ArrayList<>();
            passwords = new ArrayList<>();
            roles = new ArrayList<>();
            String[] lines;
            String line = bfr.readLine();
            while (line != null) {
                lines = line.split(" ");
                synchronized (gatekeeper) {
                    usernames.add(lines[0]);
                    passwords.add(lines[1]);
                    roles.add(lines[2]);
                }
                line = bfr.readLine();
            }
            bfr.close();
        }
    }



    // this method handles the server side processing for the GUI with continue, edit account, delete account ,and return to main menu button displayed by client
    public static void showAccountOptionsDialog(Login login, ObjectInputStream ois, ObjectOutputStream oos, BufferedReader bfr) throws IOException, ClassNotFoundException {
        String buttonCommand;
        do {
            JButton button = (JButton) ois.readObject(); // reads the button selected(continue, edit account, delete account or return to main menu)
            buttonCommand = button.getActionCommand(); // gets the action command for button selected

            if (buttonCommand.equals("continue")) { // processing if user clicks continue button

            } else if (buttonCommand.equals("edit account")) {
                showEditAccountOptionsDialog(login, ois, oos, bfr); // handles the server side processing for GUI which gives the user options to edit username, password or account type

            } else if (buttonCommand.equals("delete account")) {
                deleteAccount(login);
            } else if (buttonCommand.equals("return to main menu")) {
                // sets non-static fields to null so that it loop back again to main menu
                login.username = null; // sets username field to null
                login.password = null; // sets password field to null
                login.role = null; // sets role field to null
            }
        }while (buttonCommand.equals("edit account"));
    }

    // this method edits the password when edit password button is clicked
    public static void editPassword (Login login, String passwordInput){
        // passwordInput stores input for password while editing the account

        login.password = passwordInput; // changes password field for Login object
        int index = usernames.indexOf(login.username); // index of the login object in ArrayList
        synchronized (gatekeeper) {
            passwords.set(index, login.password); // changes password in the passwords ArrayList
        }
        try { // writes the updated password to the file by rewriting all the details
            synchronized (gatekeeper) {
                PrintWriter pw = new PrintWriter(new FileOutputStream("UserDetails.txt"));
                for (int i = 0; i < usernames.size(); i++) {
                    pw.printf(usernames.get(i) + " " + passwords.get(i) + " " + roles.get(i) + "\n");
                }

                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Your password has been changed successfully");
    }

    // this method edits the username when edit username button is clicked
    public static void editUsername (Login login, String usernameInput){
        // usernameInput stores input for username while editing the account
        int index = usernames.indexOf(login.username); // index of the login object in ArrayList
        login.username = usernameInput; // changes username field for Login object

        synchronized (gatekeeper) {
            usernames.set(index, login.username); // changes username in the usernames ArrayList
        }
        System.out.println(login.username);
        try { // writes the updated username to the file by rewriting all the details
            synchronized (gatekeeper) {
                PrintWriter pw = new PrintWriter(new FileOutputStream("UserDetails.txt"));
                for (int i = 0; i < usernames.size(); i++) {
                    pw.printf(usernames.get(i) + " " + passwords.get(i) + " " + roles.get(i) + "\n");
                }
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Your username has been changed successfully");
    }

    //this method edits the role when edit role button is clicked
    public static void editRole (Login login, String roleInput){

        int index = usernames.indexOf(login.username); // index of the login object in ArrayList


        login.role = roleInput; // changes role field for Login object to teacher
        synchronized (gatekeeper) {
            roles.set(index, login.role); // changes role in the roles ArrayList
        }

        try { // writes the updated role to the file by rewriting all the details
            synchronized (gatekeeper) {
                PrintWriter pw = new PrintWriter(new FileOutputStream("UserDetails.txt"));
                for (int i = 0; i < usernames.size(); i++) {
                    pw.printf(usernames.get(i) + " " + passwords.get(i) + " " + roles.get(i) + "\n");
                }
                pw.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println( "Your account type has been changed successfully");
    }
    // this method displays the edit account options :  edit username, edit password, edit account type
    public static void showEditAccountOptionsDialog (Login login, ObjectInputStream ois, ObjectOutputStream oos, BufferedReader bfr) throws IOException, ClassNotFoundException {
        String buttonCommand;
        do {
            JButton button = (JButton) ois.readObject(); // reads the button selected(edit username, edit password or edit account type) from server
            buttonCommand = button.getActionCommand(); // stores the action command of the button selected

            if (buttonCommand.equals("edit username")) { // calls the edit username method to edit the username
                oos.writeObject(usernames);// sends arrayList of usernames to client
                oos.flush();
                System.out.println(usernames.get(0));
                String usernameInput = bfr.readLine(); // gets username input from server
                editUsername(login, usernameInput);
            } else if (buttonCommand.equals("edit password")) { // calls the edit password method to edit the password
                String passwordInput = bfr.readLine(); // gets password input from server
                editPassword(login, passwordInput);
            } else if (buttonCommand.equals("edit role")) { // calls the edit role method to edit the role
                String roleInput = bfr.readLine(); // gets role input from server
                editRole(login, roleInput);
            }
        } while (!buttonCommand.equals("return"));
    }
    // this method deletes an account
    private static void deleteAccount (Login login){
        int index = usernames.indexOf(login.username); // index to be deleted from array lists
        synchronized (gatekeeper) {
            usernames.remove(index); // remove username from the array list
            passwords.remove(index); // remove password from the array list
            roles.remove(index); // remove role from the array list
        }
        login.username = null; // sets username field to null
        login.password = null; // sets password field to null
        login.role = null; // sets role field to null
        try { // removes the account from file by rewriting all the details
            synchronized (gatekeeper) {
                PrintWriter pw = new PrintWriter(new FileOutputStream("UserDetails.txt"));
                for (int i = 0; i < usernames.size(); i++) {
                    pw.printf(usernames.get(i) + " " + passwords.get(i) + " " + roles.get(i) + "\n");
                }
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // account deleted message displayed
        System.out.println( "Your account has been deleted successfully");
    }
    // this method handles most of the login and create account stuff in the server
    public static Login loginOrCreateAccountServer(PrintWriter pw, BufferedReader bfr, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException, EOFException {
        Login login = null; // Login object to be returned
        // checks if there exists any user details file and reads it, if a user details file does not exist then creates one.
        readUserDetails();
        // receives what option the user selected from client

        JButton loginOrCreateAccountButton = (JButton) ois.readObject(); // reads the button selected(login or create account)

        String loginOrCreateAccountButtonCommand = loginOrCreateAccountButton.getActionCommand(); // stores the action command of the button

        // if the user selects create new account option a new account is created
        if (loginOrCreateAccountButtonCommand.equals("Create a new account")) {
            oos.writeObject(usernames);// writes usernames array list to client
            oos.flush();
            System.out.println("written usernames arrayList to client");
            String usernameInput = bfr.readLine(); // reads username input from client
            String passwordInput = bfr.readLine(); // reads password input from client
            String roleInput = bfr.readLine(); // reads password input from client
            login = new Login(usernameInput, passwordInput, roleInput); // Login object created using Login constructor
            System.out.println("Congratulations new account successfully created !");
            // Client shows the edit, delete, continue, return to main menu options
            showAccountOptionsDialog(login, ois, oos, bfr);

            return login;

        }
        // if the user selects login option, the user details are verified
        else if (loginOrCreateAccountButtonCommand.equals("Login")) {
            System.out.println("entered login");
            boolean detailsVerified;
            do {
                System.out.println("entered do while");
                oos.writeObject(usernames);
                oos.flush();
                System.out.println("written usernames");
                oos.writeObject(passwords);
                oos.flush();
                System.out.println("written passwords");
                detailsVerified = (boolean) ois.readObject();
                System.out.println(!detailsVerified);
            } while (!detailsVerified);

            String input = bfr.readLine();// contains username entered while logging in
            String input2 = bfr.readLine();// contains password entered while logging in
            // the line below creates a Login object
            login = new Login(input, input2);
            System.out.println("Login successful");
            showAccountOptionsDialog(login, ois, oos, bfr); // shows the edit, delete, continue, return to main menu options

            return login;

        }

        return null;

    }

    // this method is a proxy method and is called in the run method of server
    public static Login loginServer (PrintWriter pw, BufferedReader bfr, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Login login = null;
        do {
            try {
                login = Login.loginOrCreateAccountServer(pw, bfr, oos, ois);
            } catch (EOFException eofException){
                eofException.printStackTrace();
                break;
            } catch (IndexOutOfBoundsException indexOutOfBoundsException){
                indexOutOfBoundsException.printStackTrace();
                break;
            }


        } while (login.username == null);
        return login;
    }

}

