import javax.swing.*;
import java.io.*;
/**
 * Submission.java
 *
 *
 * The Submission and SubmissionClient classes are the components necessary
 * for the teacher to view student submissions.
 *
 * @author group #85
 * @version December 13, 2021
 */
public class Submission {
    public static void viewStudentSubmissions(BufferedReader bfr, PrintWriter pw, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        JButton button = (JButton) ois.readObject();
        String buttonActionCommand = button.getActionCommand();
        if(buttonActionCommand.equals("continue")) {
            String filename = bfr.readLine();
            String submissionContent = readSubmissionFile(filename); // submission content string contains everything in the submission file
            pw.println(submissionContent); // send all the content to be displayed to client
            pw.flush();
        }

    }
    // reads the file containing student submission
    public static String readSubmissionFile(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String toBePrinted = "";
        String ss  = bufferedReader.readLine();
        while (ss != null) {
            toBePrinted = toBePrinted + ss + "\n";
            ss = bufferedReader.readLine();
        }
        bufferedReader.close();

        return toBePrinted;
    }
}
