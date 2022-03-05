import java.io.*;
import java.util.ArrayList;

/**
 * Course.java
 *
 * Course.java sets up the main CourseList.txt and contains other
 * course related methods. CourseList.txt is used throughout the project
 * to recognize the courses that have been set up by teacher accounts.
 *
 * @author Team #85
 *
 * @version December 13, 2021
 *
 */

public class Course {
    private static ArrayList<String> courses = new ArrayList<>(); // this array list contains name of all the courses

    public static ArrayList<String> getCourses() {
        return courses;
    }

    public static void setCourses(ArrayList<String> courses) {
        Course.courses = courses;
    }

    // this method checks if there exists any course list file and reads it, if a course list file does not exist then creates one.
    public static void readCourseList() throws IOException {
        File courseList = new File("CourseList.txt");
        if (courseList.createNewFile()) {

        } else {
            BufferedReader bfr = new BufferedReader(new FileReader(courseList));
            String line = bfr.readLine();
            while (line != null) {
                courses.add(line);
                line = bfr.readLine();
            }
            bfr.close();
        }


    }
    // this method writes the new course name to the course list file
    public static void writeToCourseList(String courseName, Object gatekeeper) throws FileNotFoundException {
        File courseList = new File("CourseList.txt");
        PrintWriter pw = new PrintWriter(new FileOutputStream(courseList, true));
        synchronized (gatekeeper) {
            pw.println(courseName);
        }
        pw.close();
    }
    // this method creates a new course
    public static void createCourse(String courseName, Object gatekeeper) throws IOException {
        readCourseList();
        courses.add(courseName);
        writeToCourseList(courseName, gatekeeper);
    }


}
