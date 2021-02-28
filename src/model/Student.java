package model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.util.UUID;

/**
 * Das Objekt zum Schüler
 *
 * @author Aladin Boudouda
 *
 */

public class Student {
    private final UUID studentUUID;
    private String firstName;
    private String lastName;
    private String salutation;
    private String schoolClass;
    private Mastery mastery;
    private Image image;

    public Student(String firstName, String lastName, String schoolClass, String salutation, Mastery mastery) {
        this.studentUUID = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.schoolClass = schoolClass;
        this.salutation = salutation;
        this.mastery = mastery;
    }

    public UUID getStudentUUID() {
        return studentUUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Mastery getMastery() {
        return mastery;
    }

    public void setMastery(Mastery mastery) {
        this.mastery = mastery;
    }

    public Image getImage() {
        return image;
    }

    public void setImageFromPath(String path) {
        try {
            this.image = new Image(new FileInputStream(path));
        } catch (Exception e) {
            System.out.println("please provide valid filepath");
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }
}
