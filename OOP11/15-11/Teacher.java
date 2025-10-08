import java.io.*;
import java.util.*;

class Teacher implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private List<Subject> subjects;

    public Teacher(){
        firstName = null;
        lastName = null;
        phone=null;
        email=null;
        subjects = new ArrayList<>();
    }

    public Teacher(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        subject.setTeacher(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}

