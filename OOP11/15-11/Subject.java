import java.io.*;
import java.util.*;

class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String grade;
    private Teacher teacher;

    public Subject(){
        name = null;
        grade = null;
    }

    public Subject(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    @Override
    public String toString() {
        return "Subject{name='" + name + "', grade='" + grade + "', teacher=" + (teacher != null ? teacher.getFullName() : "None") + '}';
    }
}