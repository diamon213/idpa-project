package model;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

/**
 * Modelklasse für eine Gruppe von Schülern,
 * die man zusammenstellen kann zum lernen
 *
 * @author Aladin Boudouda
 *
 */

public class Studyset {
    private UUID studysetUUID;
    private String studysetName;
    private Vector<Student> students;
    private int mastery;
    private Boolean mostRecent = false;

    public Studyset(String studysetName) {
        this.studysetUUID = UUID.randomUUID();
        this.studysetName = studysetName;
        this.students = new Vector<>();
        this.mastery = calcMastery();
    }

    public UUID getStudysetUUID() {
        return studysetUUID;
    }

    public String getStudysetName() {
        return studysetName;
    }

    public void setStudysetName(String studysetName) {
        this.studysetName = studysetName;
    }

    public Vector<Student> getStudents() {
        return students;
    }

    public int getStudentCount() {
        return students.size();
    }

    public void setStudents(Vector<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public float getMastery() {
        return mastery;
    }

    public void setMastery(int mastery) {
        this.mastery = mastery;
    }

    public int calcMastery() {
        mastery = 0;
        for (Student student : students) {

            switch (student.getMastery()) {

                case KNOWN -> {
                    mastery += 50;
                }
                case MASTERED -> {
                    mastery += 100;
                }
            }
        }
        try {
            return mastery / students.size();
        } catch (ArithmeticException e) {
            return 0;
        }
    }

    public Boolean getMostRecent() {
        return mostRecent;
    }

    public void setMostRecent(Boolean mostRecent) {
        this.mostRecent = mostRecent;
    }
}
