/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Student;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author conno
 */
public class ClassRosterDaoFileImpl implements classRosterDao {

    public static final String ROSTER_FILE = "roster.txt";
    public static final String DELIMITER = "::";

    private Map<String, Student> students = new HashMap<>();

    private String marshallStudent(Student aStudent) {
        return aStudent.getStudentId() + DELIMITER + aStudent.getFirstName() + DELIMITER + aStudent.getLastName() + DELIMITER + aStudent.getCohort();
    }

    /**
     * Writes all students in the roster out to a ROSTER_FILE. See loadRoster
     * for file format.
     *
     * @throws ClassRosterDaoException if an error occurs writing to the file
     */
    private void writeRoster() throws ClassRosterDaoException {
        // NOTE FOR APPRENTICES: We are not handling the IOException - but
        // we are translating it to an application specific exception and 
        // then simple throwing it (i.e. 'reporting' it) to the code that
        // called us.  It is the responsibility of the calling code to 
        // handle any errors that occur.
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(ROSTER_FILE));
        } catch (IOException e) {
            throw new ClassRosterDaoException(
                    "Could not save student data.", e);
        }

        // Write out the Student objects to the roster file.
        // NOTE TO THE APPRENTICES: We could just grab the student map,
        // get the Collection of Students and iterate over them but we've
        // already created a method that gets a List of Students so
        // we'll reuse it.
        String studentAsText;
        List<Student> studentList = this.getAllStudents();
        for (Student currentStudent : studentList) {
            // turn a Student into a String
            studentAsText = marshallStudent(currentStudent);
            // write the Student object to the file
            out.println(studentAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }

    private void loadRoster() throws ClassRosterDaoException {
        Scanner scanner;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ROSTER_FILE)));
        } catch (FileNotFoundException e) {
            throw new ClassRosterDaoException(
                    "-_- Could not load roster data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentStudent holds the most recent student unmarshalled
        Student currentStudent;
        // Go through ROSTER_FILE line by line, decoding each line into a 
        // Student object by calling the unmarshallStudent method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Student
            currentStudent = unMarshallStudent(currentLine);

            // We are going to use the student id as the map key for our student object.
            // Put currentStudent into the map using student id as the key
            students.put(currentStudent.getStudentId(), currentStudent);
        }
        // close scanner
        scanner.close();
    }

    @Override
    public Student addStudent(String studentId, Student student) throws ClassRosterDaoException {
        loadRoster();
        Student newStudent = students.put(studentId, student);
        writeRoster();
        return newStudent;
    }

    @Override
    public List<Student> getAllStudents() throws ClassRosterDaoException {
        loadRoster();
        return new ArrayList(students.values());
    }

    public Student unMarshallStudent(String studentInfo) {
        /*
        Take in a String line to break apart for student information.
        Split the String into chunks at the :: delimiter.
        Create a new Student object.
        Use the first value from the split String to set the Student id (this is passed into the constructor of the new Student object).
        Use the second value from the split String to set the Student first name.
        Use the third value from the split String to set the Student last name.
        Use the fourth value from the split String to set the cohort value.
         */

        String[] studentVariables = studentInfo.split(DELIMITER);
        Student newStudent = new Student(studentVariables[0]);
        newStudent.setFirstName(studentVariables[1]);
        newStudent.setLastName(studentVariables[2]);
        newStudent.setCohort(studentVariables[3]);
        return newStudent;
    }

    @Override
    public Student getStudent(String studentId)
            throws ClassRosterDaoException {
        loadRoster();
        return students.get(studentId);
    }

    @Override
    public Student removeStudent(String studentId)
            throws ClassRosterDaoException {
        loadRoster();
        Student removedStudent = students.remove(studentId);
        writeRoster();
        return removedStudent;
    }
}
