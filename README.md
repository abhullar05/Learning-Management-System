# Project-5
## How to run.
To compile and run the project, simply compile it on whatever system you are using and then run both Server.java and Client.java. The client should connect to the server and will start to run the starting menu. The server's port number is 8000.

## Classes
There are 20 classes in this project, most connecting within eachother to create a seamless experience. There are two types of classes that we have; Server classes are classes that the server will run, and Client classes are classes the client will run.

### Client.java
Client.java is the main component of the client side of the project. It sets up the host socket, the necessary server writers and readers, the run method for the client side, and the contains the createGUI method, which is the starting point for the complex GUIs. It also produces LoginClient loginClient to transfer the server writers and readers across the GUIs.

### Course.java
Course.java sets up the main CourseList.txt and contains other course related methods. CourseList.txt is used throughout the project to recognize the courses that have been set up by teacher accounts.

### CreateQuizClient.java and CreateQuizServer.java
The CreateQuizClient and Server classes both are used in the creation of the quizzes from the Teacher side. From the Server side, the method is:
- quizQuestionAndAnswerBuilder, which takes inputs from the client side and prints them onto a file.
And from the Client side the methods are:
- createQuiz, which asks the name, amount of questions, and if the user would like to randomize the questions.
- addSubsetPrompt asks how many subsets the user would like.
- extendedDeadlinePrompt asks if the user would like to implement extended deadlines.
- regularDeadlinePrompt asks the user what the deadline should be for the quiz.
- extendedUsernameTimePrompt is a continuation of extendedDeadlinePrompt and asks how long the time should be extended and for who.
- extendedUsernameTimePromptContinued continues extendedUsernameTimePrompt by asking for additional people to extend the time for.
- quizQuestionBuilderPrompt asks the user if they would like to change a certain question, and what the new questions and new answers should be.
- askForSubset asks the user if the would like to implement subsets.

### Deadline
Deadline class is the class that handles the timestamps given from the main method. It will also handle the timelimit set by the teacher. Using the timestamps and the timelimit, the method will calculate if the student finished the quiz within the time and return a boolean value which will then be processed in the main method to either remove the attempt of the student or continue to submit the attempt if the attempt was within the deadline/timelimit. The class has 4 methods Deadline(), timelimit(), calculateDifference(), stringToTimestamp().
- Deadline() stores the two timestamps from the student class and one integer.
- timelimit() returns a boolean depending on the value that is calculated by calculateDifference() and the integer input from the student class. If the difference calculated is bigger than that of the integer, the boolean will be false and vice versa.
- calculateDifference() will calculate the difference between the two timestamps that were taken at the start and the end of the quiz. This method returns a long value that is calculated to minutes.
- stringToTimestamp() method turns the date which is given in string to a timestamp object.

### EditQuizClient.java and EditQuizServer.java
The EditQuizClient and Server classes both are used in editing pre-existing quizzes. The server methods are:
- editQuizServerStart reads the pre-existing quiz, saves it as a Question[], and then deletes the file in preparation to create a new one.
- editQuizServerEditPortion writes onto a new file the first line as given from the previous quiz, and then the inputs from the client side.
The client methods are: 
- editQuizStart asks the user which quiz they would like to edit.
- editQuizYesOrNoQuestion asks the user if they would like to edit the current question.
- editAnsweredYesGui prompts the user to input a new question and new answers.

### GradeAnswerkeyClient.java and GradeAnswerkeyServer.java
The GradeAnswerkeyClient and Server classes are used in creating graded answerkeys for quizzes. The server methods are:
- grade reads the quiz, sends the question and answers required for the client, and takes input from the buttons on the client side to produce a file.
- choosePoints adds the points assigned to each answer to the file as designated by the user from the client.
The client methods are:
- gradeGuiAskQuiz asks the user which quiz they would like to create an answerkey for.
- gradeQuiz prompts the user to choose an answer to assign points to, and then sends the answer chosen to the server for processing.
- choosePointsGui prompts the user to choose an amount of points to assign to the answer that was chosen.

### Login.java
Login.java is the foundation of the login and create account proccess. It contains the methods:
- Login, a constructor with parameters for roles, usernames, and passwords
- Login, another constructor for log-ins with only username and password parameters.
- There are a multitude of Getters and Setters for the following fields: password, username, role, usernames, passwords, and roles
- readUserDetails creates a UserDetails.txt file and fills it with the usernames, passwords, and roles given by the user.
- ShowAccountOptionsDialog reads a button sent from Client to determine whether to continue on with deleting, editing, returning to the main menu, and continuing to courses and quizzes.
- editPassword, editUsername, and editRole edit their respective fields within the UserDetails.txt when told to do so by the Client.
- editAccountOptionsDialog runs editPassword, editUsername, or editRole depending on what the Client sends it.
- deleteAccount deletes the account from the UserDetails.txt
- loginOrCreateAccountServer reads the username and password from client and determines whether they match with one in UserDetails.txt. It then either lets the user continue or it asks them to try again. It also creates a Login object.
- loginOrCreateAccountServer is a proxt method that is called in Server.java

### LoginClient.java
LoginClient.java is the client portion of the login and create account proccess. It contains the methods:
- A self titled constructor containing server side delivery and reading objects, and a JFrame.
- A main method that creates a new LoginClient object and runs loginOrCreateAccountClient.
- loginOrCreateAccoutnClient calls the method showAccountOptionsDialog.
- showAccountOptionsDialog calls the method addCreateAccountLoginButtons.
- adddCreateAccountLoginButtons prompts the user to either login or create and account, and sends the answer to the server.
- showEditAccountOptionsDialog prompts the user to select what account detail to edit, or to return to the previous menu.
- showCreateAccountDialog prompts the user to create a username, password, and to choose either a student or teacher role.
- isUsernameValid checks to make sure the username is not blank and is not a copy of another username.
- isPasswordValid checks to make sure the password is not blank.
- writeUsernamePasswordRole writes to the server the given username, password, and role selection.
- showLoginDialog prompts the user to login with a username and a password, and then checks to see whether the username and password given are valid.
- verifyLoginDetails checks to see whether the given username and password are valid.
- writeUsernamePasswordLogin writes the username and password to the server.
- showAccountEditOptionsDialog displays the GUI with continue, edit account, delete account, and return to main menu buttons on the EDT.
- addButtons prompts the user to continue, edit account, delete account, or return to main menu.
- showEditUsernameDialog, showEditPasswordDialog, and showEditRoleDialog displays the prompts to edit either username, password, or role, depending on what the user entered previously

### Question.java
This class primarily is used as a storage class, as it contains the Question constructor, which stores questions and answers, and a toString, that allows for the Question constructor to be converted into a String. Below is the toString format.
```
  "question + "\n" + answerOne + "\n" + answerTwo + "\n" + answerThree + "\n" + answerFour;
```

### Randomization.java
This class implements the randomization needed for taking quizzes as well as implementing the necessary components for random subsets. It is a server-side class. It has the methods:
- randomizeTheQuiz returns a Question[] with random questions and options when given the quiz's name and course's name.
- readQuiz returns an ArrayList and is used to read the quiz file.
- getQuestions returns a String[] that stores all of the questions in a quiz.
- getOptions returns a String[][] that stores all of the options for a certain question.
- getRandomQuestion returns a String[] that stores a random array of questions.
- getRandomQuestionsAndOptions returns a String[][] that stores the question firstly and then the answer secondly, both randomized.
- getRandomQuestionsAndOptionsQuestionArray returns a Question[] that contains random questions and options.
- getRandomSubsetOfQuestions calls randomizeTheQuiz to randomize the quiz first, and then returns a Question[] with the same amount of questions as was given by the user.
 
### Server.java
This class implements the Server, including the ServerSocket and the run method. The ServerSocket is set up in a main method along with the thread that starts the run method.
- The run method contains the necessary server reading and writing tools and creates a Login object.

### Student
Student class prints out the quiz that will be taken by the student which is imported from the quiz text file into arrays and handles the submission from the students and returns their grades depending on their answers given. The class has two methods readQuiz(), and gradeQuiz().
- readQuiz() methods gets the filename and creates an arraylist of Question and uses the question class for formatting them into specific orders. Then the arraylist is used to store the questions into an array question array and returns the array to the main method
- gradeQuiz() method gets the filename, username and course from the main method so that the method can import the student submission. After doing so, the method will start to check the line of the file and the answersheet that the teacher made. If they have the right/equal answer chosen, the method will add points to the totalStudentScore and the totalScoreAvailable is calculated before the grading starts. The method will return the grade string in the format of
```
"\nTotal Score: " + totalStudentScore + "/" + totalPossibleScore
```

### StudentClient.java and StudentServer.java
The StudentClient and Server classes contain the code for using the student role, including taking quizzes and viewing graded submissions. The server methods are:
- studentServer starts a loop whose condition depends on the quizMenuStudent method it calls.
- quizMenuStudent starts a loop whose condition depends on one of the two methods it can call, either through gradeQuizAction or takeQuizAction. It can also immediately break the loop called in studentServer if return to main is selected.
- gradeQuizAction gets the user's selected quiz from the client and then runs the gradeQuiz method.
- gradeQuiz prints the graded quiz to a gradedanswersheet file and then sends a String[][] to the client so that the client can show the student the results.
- takeQuizAction gets the user's selected quiz from the client, checks for randomization, subsets, timelimit, and extended deadline, and sends the questions and answers from the quiz to the client for answering. At the end of the quiz, it sends the client a boolean that checks if the student either was within the timelimit or out of bounds.
- coursesReturn returns a String[] of the courses.
- answerAction prints the question and the answer provided by the client to the answersheet file.
The client methods are:
- startOfStudentGui prompts the user to choose a course and then continue. The chosen course is sent to the server.
- studentMainMenu prompts the user to either take a quiz, grade a quiz, or return to the main menu.
- gradeQuizPrompt prompts the user to enter the quiz they would like to have graded. 
- gradeQuizGui prints the question, answer that the user selected, and the amount of points they have earned for question.
- finalGradeGui prints the total amount of points earned by the user compared to the total possible points.
- takeQuizPromptGui prompts the user to enter the quiz they would like to grade. After they have entered the quiz, a timestamp starts, which is sent to the server.
- takeQuiz gets the questions and answers from the server and displays them to the user, prompting them to choose an answer. Once an answer is chosen, the result is sent back to the server. At the end of the quiz, a boolean is recieved from the server that says to either delete the quiz attempt because the student has gone over the time limit, or to continue with processing the quiz.

### Submission.java and SubmissionClient.java
The Submission and SubmissionClient classes are the components necessary for the teacher to view student submissions. The server methods are:
- viewStudentSubmissions gets the quiz name and the student name from the client and then continues to the method readSubmissionFile, where the method returns a String. This String is then printed to the client.
- readSubmissionFile reads the requested file and then returns it as a String.
The client methods are:
- viewStudentSubmissionGui restates static fields with the given parameters, and then calls the method addPrompts
- addPrompts prompts the user to input a quiz name and a student name, which is sent to the server for processing. It then recieves the student submission String and runs displaySubmissionContent with the String as a parameter.
- displaySubmissionContent prompts the user to return to the menu after showing them the student submission string.

### TeacherContinue.java and TeacherContinueClient.java
The TeacherContinue classes serve as the pathway to the several actions that Teachers can do. The server methods are:
- teacherContinue handles server side processing for create and view a course and is called in a run method.
- readCourseDetails reads the course details from CourseDetails.txt and returns them as an ArrayList.
- writeCourseDetailsToFile writes the ArrayList onto CourseDetails.txt.
- quizMenuAction redirects the server to a different method depending on the user's choice of action. The methods available are: CreateQuizServer.quizQuestionAndAnswerBuilder, EditQuizServer.editQuizServerStart, GradeAnswerKeyServer.grade, Submission.viewStudentSubmissions and deleteQuiz. There is also a return to main menu option.
- deleteQuiz reads from the server and deletes the quiz that the server tells it to delete.
The client methods are:
- showTeacherContinueGui is called in the createGui method of Client class and will display every GUI related thing after teacher presses continue button.
- selectCourseAction prompts the user to either create or a view a course.
- showCourseInputDialog prompts the user to enter a course name, then sends the course name to the server.
- showCourseList prompts the user to choose a course by providing all of the courses that have already been created.
- showEmptyCourseListError returns the user if there has been no course created and the CourseList.txt is empty.
- createStringArrayOfCourses returns a String[] of the courses available.
- deleteQuizGui prompts the user to enter the name of the quiz they would like to delete, which is then sent to the server. Depending on the server's successfulness, an error message may appear, saying that the server could not delete the quiz.
- showQuizMenuGui prompts the user to choose either to create a quiz, to edit a quiz, to create an answerkey, to view student submissions, to delete a quiz, or to return to course menu. It then redirects to certain guis depending on what the user inputted.
