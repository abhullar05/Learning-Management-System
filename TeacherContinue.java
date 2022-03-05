import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * TeacherContinue.java
 *
 *
 * The TeacherContinue classes serve as the pathway to the several
 * actions that Teachers can do. 
 *
 * @author group #85
 * @version December 13, 2021
 */
public class TeacherContinue {
    // this method handles server side processing for create and view a course, it is called in run method
    public static void teacherContinue(ObjectInputStream ois, ObjectOutputStream oos, BufferedReader bfr, PrintWriter pw, Object gatekeeper) throws IOException, ClassNotFoundException {
        boolean loop = false;
        ArrayList<String> courseList = readCourseDetails(gatekeeper);
        oos.writeObject(courseList);
        oos.flush();
        while(true) {

            JButton button = (JButton) ois.readObject();
            String buttonActionCommand = button.getActionCommand();
   
            if (buttonActionCommand.equals("create a new course")) {
                ArrayList<String> courses = readCourseDetails(gatekeeper);
                oos.writeObject(courses);
                oos.flush();
                String courseName = bfr.readLine();
                courses.add(courseName);
                writeCourseDetailsToFile(courses, gatekeeper);
                pw.println(courses);
                pw.flush();

            } else if (buttonActionCommand.equals("view a course")) {

                quizMenuAction(ois, bfr, oos, pw, gatekeeper);

            }
        }

    }
    // reads course details from courses file
    public static ArrayList<String> readCourseDetails(Object gatekeeper) throws IOException {
        ArrayList<String> courses = new ArrayList<>();
        File courseDetails = new File("CourseList.txt");
        synchronized (gatekeeper) {
            if (courseDetails.createNewFile()) {

            } else {
                BufferedReader coursesBfr = new BufferedReader(new FileReader(courseDetails));
                String courseLine = coursesBfr.readLine();
                while (courseLine != null) {
                    courses.add(courseLine);
                    courseLine = coursesBfr.readLine();
                }
                coursesBfr.close();
            }
        }
        return courses;
    }
    // write all the courses in courses ArrayList to courses file
    public static void writeCourseDetailsToFile(ArrayList<String> courses, Object gatekeeper) throws FileNotFoundException {
        PrintWriter coursesPw = new PrintWriter(new FileOutputStream("CourseList.txt"));
        synchronized (gatekeeper) {
            for (int i = 0; i < courses.size(); i++) {
                coursesPw.println(courses.get(i));
            }
        }
        coursesPw.close();
    }

    public static void quizMenuAction(ObjectInputStream ois, BufferedReader bfr, ObjectOutputStream oos, PrintWriter pw, Object gatekeeper) throws IOException, ClassNotFoundException {
        boolean loop2 = true;
        do {
            loop2 = true;
            JButton button = (JButton) ois.readObject();
            String quizMenuOption = button.getActionCommand();
            if (quizMenuOption.equals("create a quiz")) {
                CreateQuizServer.quizQuestionAndAnswerBuilder( bfr, gatekeeper);


            } else if (quizMenuOption.equals("edit a quiz")) {
                EditQuizServer.editQuizServerStart(ois, oos, pw, bfr, gatekeeper);

            } else if (quizMenuOption.equals("create an answer key")) {
                GradeAnswerKeyServer.grade(oos, ois, pw, bfr, gatekeeper);
            } else if (quizMenuOption.equals("view student submissions")) {
                Submission.viewStudentSubmissions(bfr, pw, ois);
            } else if (quizMenuOption.equals("delete a quiz")) {
                deleteQuiz(bfr, oos, gatekeeper);
            } else if (quizMenuOption.equals("return to course menu")) {
                loop2 = false;

            }
        }while(loop2);
    }

    public static void deleteQuiz(BufferedReader bfr, ObjectOutputStream oos, Object gatekeeper) throws IOException {
        String quizToBeDeletedFileName = bfr.readLine();
        File file = new File(quizToBeDeletedFileName);
        boolean deletedSuccess = false;
        synchronized (gatekeeper) {
            if (file.delete()) {
                deletedSuccess = true;
                oos.writeObject(deletedSuccess);

            } else {
                oos.writeObject(deletedSuccess);

            }
        }
    }
}
