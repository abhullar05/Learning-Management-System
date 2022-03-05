import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
/**
 * Randomization.java
 *
 *
 * This class implements the randomization needed for taking quizzes
 * as well as implementing the necessary components for random subsets.
 * It is a server-side class.
 *
 * @author group #85
 * @version December 13, 2021
 */
public class Randomization {


    // this method has everything related to making questions and options random and returns a Question[] with random questions and options(called in main method)
    public static Question[] randomizeTheQuiz(String quizName, String courseName) throws IOException { // quizName is the name of the quiz to be randomized and courseName is the name of course in which the quiz exists
        String filename = courseName + "_" + quizName; // string is filename to be read by readQuiz method
        ArrayList<String> lines = readQuiz(filename, courseName);// String[] questions consists of all the questions and options
        String[] questions = getQuestions(lines); // questions is a String[] which stores all the questions
        String[][] options = getOptions(lines); // options is 2D String[][] which contains all the options
        String[] randomQuestions = getRandomQuestions(lines); // randomQuestions is a String[] of random questions
        String[][] randomQuiz = getRandomQuestionsAndOptions(questions, options); // randomQuiz is a 2-D String[][] which contains random questions and their respective random options
        Question[] randomQuizQuestionArray = getRandomQuestionsAndOptionsQuestionArray(randomQuiz);
        return randomQuizQuestionArray;

    }


    // this method reads the contents of the Quiz file and stores them in an arrayList
    public static ArrayList<String> readQuiz(String filename, String course) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader bfr = new BufferedReader(new FileReader( course + "_" + filename + ".txt"));
        String line = bfr.readLine();
        while(line != null){
            lines.add(line);
            line = bfr.readLine();
        }
        bfr.close();
        return lines;
    }
    // this method gives a String[] which stores all the questions
    public static String[] getQuestions(ArrayList<String> lines){
        String[] questions = new String[lines.size()/5];
        for(int i = 0; i < questions.length; i++){
            questions[i] = lines.get(5 * i + 1); // copying the questions from lines arrayList into the questions array
        }
        return questions;
    }
    // returns a 2D String[][] which contains all the options
    public static String[][] getOptions(ArrayList<String> lines){
        String[][] options = new String[lines.size()/5][4];
        for(int i = 0; i < options.length; i++ ){
            for(int j = 0; j < 4; j++){
                options[i][j] = lines.get(5 * i + j +2);
            }
        }
        return options;
    }


    // returns a String[] of random questions
    public static String[] getRandomQuestions(ArrayList<String> lines){
        String[] questions = getQuestions(lines);
        String[] randomQuestions = new String[questions.length];
        ArrayList<String> questionsArrayList = new ArrayList<>();
        for(String question : questions){
            questionsArrayList.add(question);
        }
        Random r = new Random();

        for(int i = 0; i < questions.length; i++){
            String randomQuestion = questionsArrayList.get(r.nextInt(questionsArrayList.size())); // gets a random question so that String[] of random questions can be made
            randomQuestions[i] = randomQuestion;
            questionsArrayList.remove(randomQuestion); // ensures questions are not repeated
        }
        return randomQuestions;
    }
    // returns a 2-D String[][] which contains random questions and their respective random options
    public static String[][] getRandomQuestionsAndOptions(String[] questions, String[][] options){
        String[][] array = new String[questions.length][5]; // array is a 2-D array which contains all the questions and their respective questions in original order
        for(int i = 0; i < questions.length; i++ ){
            array[i][0] = questions[i]; // putting questions in the 2-D array
            for(int j = 1; j < 5; j++){
                array[i][j] = options[i][j - 1]; // putting respective options in the 2-D array
            }
        }
        ArrayList<Integer> integerArrayList = new ArrayList<>(); // integerArrayList contains integers from 0 to questions.length
        for(int i = 0; i < questions.length; i++){
            integerArrayList.add(i);
        }
        String[][] randomQuestionsAndOptions = new String[questions.length][5]; // randomQuestionsAndOptions is a 2-D String[][] which contains random questions and their respective random options
        Random r = new Random();
        // the for loop below puts random questions and their respective options into a 2D[]
        for(int i = 0; i < questions.length; i++){
            int randomIndex = integerArrayList.get(r.nextInt(integerArrayList.size())); // a random index is generated
            randomQuestionsAndOptions[i] = array[randomIndex];
            integerArrayList.remove(randomIndex); // to ensure index number is not repeated
        }
        // the for loop below makes the options of questions randomized
        for(int i = 0; i < questions.length; i++ ){
            ArrayList<Integer> list = new ArrayList<>();
            for(int j = 0; j < 4; j++){
                list.add(j);
            }
            for(int j = 0; j < 4; j++ ) {
                int randomIndex = list.get(r.nextInt(list.size())); // random index is generated using arrayList
                randomQuestionsAndOptions[i][j + 1] = array[i][randomIndex + 1]; // options are made random
                list.remove(randomIndex); // to ensure index number is not repeated
            }
        }
        return randomQuestionsAndOptions;
    }
    // returns Question[] which contains random questions and random options
    public static Question[] getRandomQuestionsAndOptionsQuestionArray(String[][] randomQuestionsAndOptions){
        Question[] questions = new Question[randomQuestionsAndOptions.length];
        for(int i = 0; i < randomQuestionsAndOptions.length; i++){
            questions[i] = new Question(randomQuestionsAndOptions[i][0], randomQuestionsAndOptions[i][1], randomQuestionsAndOptions[i][2], randomQuestionsAndOptions[i][3], randomQuestionsAndOptions[i][4]);
        }
        return questions;
    }
    // returns a random subset of questions from a large pool of questions
    public static Question[] getRandomSubsetOfQuestions(String quizName, String courseName, int subsetSize) throws IOException {
        Question[] questions = randomizeTheQuiz(quizName, courseName); // Question[] with random questions
        Question[] randomSubsetOfQuestions = new Question[subsetSize]; // Question[] with random subset of questions
        ArrayList<Question> randomQuestionsArrayList = new ArrayList<>(); // random questions arrayList
        Random r = new Random();
        for(int i = 0; i < questions.length; i++ ){ // creating random questions ArrayList
            randomQuestionsArrayList.add(questions[i]);
        }
        for(int i = 0; i < subsetSize; i++){
            int index = r.nextInt(randomQuestionsArrayList.size()); // generating random index for creating random subset of questions
            randomSubsetOfQuestions[i] = randomQuestionsArrayList.get(index);
            randomQuestionsArrayList.remove(index); // to ensure questions are not repeated
        }
        return randomSubsetOfQuestions;
    }
}
