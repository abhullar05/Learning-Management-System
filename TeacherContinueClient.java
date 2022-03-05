import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
/**
 * TeacherContinueClient.java
 *
 *
 * The TeacherContinue classes serve as the pathway to the several
 * actions that Teachers can do.
 *
 * @author group #85
 * @version December 13, 2021
 */
public class TeacherContinueClient {
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static PrintWriter pw;
    private static BufferedReader bfr;
    private static ArrayList<String> courses;
    private static JFrame frame;
    private static JPanel panel;
    private static JPanel panel1;
    private static JPanel panelTwo;
    private static String courseSelected;
    // this method will be called in the createGUI method of Client class and will display every GUI related stuff after teacher presses continue button
    public static void showTeacherContinueGUI(JFrame frame1, PrintWriter printWriter, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        pw = printWriter;
        ois = objectInputStream;
        oos = objectOutputStream;
        bfr = bufferedReader;
        frame = frame1;
        selectCourseAction();
    }
    // this will ask to create or view a course
    public static void selectCourseAction() throws IOException, ClassNotFoundException {
        courses = (ArrayList<String>) ois.readObject();
        panel = new JPanel();
        JLabel viewOrCreate = new JLabel("Do you want to create a new course or view a course ?");
        JButton createCourseButton = new JButton("Create a new course");
        createCourseButton.setActionCommand("create a new course");
        JButton viewCourseButton = new JButton("View a course");
        viewCourseButton.setActionCommand("view a course");
        createCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(createCourseButton);
                    oos.flush();
                    showCourseInputDialog();

                    String coursesAsAString = bfr.readLine();
                    String[] coursesAlmostSeparatedStartBracket = coursesAsAString.split("\\[");
                    String[] coursesAlmostSeparatedEndBracket = coursesAlmostSeparatedStartBracket[1].split("]");
                    String[] coursesSeparated = coursesAlmostSeparatedEndBracket[0].split(",");
                    courses.clear();
                    for (int i = 0; i < coursesSeparated.length; i++) {
                        courses.add(coursesSeparated[i]);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });

        viewCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(viewCourseButton);
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                boolean isCourseListEmpty = showEmptyCourseListError();
                if(!isCourseListEmpty) {
                    showCourseList();


                }

            }
        });
        panel.add(createCourseButton);
        panel.add(viewCourseButton);
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.add(panel);
        frame.setVisible(true);

    }

    public static void showCourseInputDialog() throws IOException, ClassNotFoundException {
        courses = (ArrayList<String>) ois.readObject();
        String input; // this variable stores input for course while creating a new course
        do {
            // takes the input for course
            input = JOptionPane.showInputDialog(null, "Please enter the name of the course you would like to create.", "Course name", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {

            } else if (input.isEmpty()) { // shows error message dialog if username is empty
                JOptionPane.showMessageDialog(null, "course name cannot be empty", "Course name", JOptionPane.ERROR_MESSAGE);
            } else if (courses.contains(input)) { // shows error message is username is already in use
                JOptionPane.showMessageDialog(null, "Please enter a different course name. This course name is already in use", "Course name", JOptionPane.ERROR_MESSAGE);
            }
        } while (input.isEmpty() || courses.contains(input)  ); // loops back and asks for username again when error message is thrown

        pw.println(input); // sends the new course name to server
        pw.flush();
    }

    public static void showCourseList(){
        String[] courseList = createStringArrayOfCourses(); // string[] containing courses
        panelTwo = new JPanel();
        JLabel selectCoursePrompt = new JLabel("Please select the course you want to view .");
        JComboBox<String> courseListDropDown = new JComboBox<>(courseList);
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseSelected = courseListDropDown.getItemAt(courseListDropDown.getSelectedIndex());
                showQuizMenuGUI();
            }
        });
        panelTwo.add(selectCoursePrompt);
        panelTwo.add(courseListDropDown);
        panelTwo.add(enterButton);


        frame.getContentPane().removeAll();
        frame.repaint();
        frame.add(panelTwo);
        frame.setVisible(true);



    }

    public static boolean showEmptyCourseListError(){
        if(courses.size() == 0)
            JOptionPane.showMessageDialog(null, "The course list cannot be empty");
        return courses.size() == 0;
    }

    public static String[] createStringArrayOfCourses(){
        String[] courseList = new String[courses.size()];
        for(int i = 0; i < courses.size(); i++){
            courseList[i] = courses.get(i);
        }
        return courseList;
    }

    public static void deleteQuizGUI(String courseSelected) throws IOException, ClassNotFoundException {

        String quizToBeDeleted = JOptionPane.showInputDialog( "Please tell the name" +
                " of the quiz you would like to delete.");
        if(quizToBeDeleted != null) {
            String quizToBeDeletedFileName = courseSelected +
                    "_" + quizToBeDeleted + ".txt";
            pw.println(quizToBeDeletedFileName);
            pw.flush();
            boolean deleteSuccessful = (boolean) ois.readObject();
            if (deleteSuccessful) {
                JOptionPane.showMessageDialog(null, "Succe" +
                        "ssfully " +
                        "deleted" +
                        " the quiz", "delete quiz", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Quiz could not" +
                        " be deleted successfully", "delete quiz", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void showQuizMenuGUI(){
        panel1 = new JPanel();
        JButton createQuizButton = new JButton("Create a new quiz");
        createQuizButton.setActionCommand("create a new quiz");
        JButton editQuizButton = new JButton("Edit a quiz");
        editQuizButton.setActionCommand("edit a quiz");
        JButton createAnswerKeyButton = new JButton("Create an answer key");
        createAnswerKeyButton.setActionCommand("create an answer key");
        JButton deleteQuizButton = new JButton("Delete a quiz");
        deleteQuizButton.setActionCommand("delete a quiz");
        JButton viewStudentSubmissionButton = new JButton("View Student Submissions");
        viewStudentSubmissionButton.setActionCommand("view student submissions");
        JButton returnToCourseMenuButton = new JButton("Return to course menu");
        returnToCourseMenuButton.setActionCommand("return to course menu");
        viewStudentSubmissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(viewStudentSubmissionButton);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                SubmissionClient.viewStudentSubmissionsGUI(frame, pw, ois, oos, bfr, courseSelected);
            }
        });
        createQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(createQuizButton);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                CreateQuizClient.createQuiz(frame, pw, oos, ois, bfr, courseSelected);
            }
        });
        editQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(editQuizButton);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    EditQuizClient.editQuizStart(frame, courseSelected, oos, ois, pw, bfr);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        createAnswerKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(createAnswerKeyButton);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                GradeAnswerKeyClient.createAnswerKey(frame, oos, ois, pw, bfr, courseSelected );
            }
        });
        deleteQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    oos.writeObject(deleteQuizButton);
                    deleteQuizGUI(courseSelected);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        returnToCourseMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject(returnToCourseMenuButton);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    showCourseInputDialog();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });



        panel1.add(createQuizButton);
        panel1.add(editQuizButton);
        panel1.add(createAnswerKeyButton);
        panel1.add(deleteQuizButton);
        panel1.add(viewStudentSubmissionButton);
        panel1.add(returnToCourseMenuButton);
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.add(panel1);
        frame.setVisible(true);

    }
}
