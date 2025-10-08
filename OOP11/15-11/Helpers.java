import java.io.*;
import java.util.*;

public class Helpers {
    private List<Subject> subjects = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    
    public void readFromConsole() {
        Scanner scanner = new Scanner(System.in);
        int sCount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < sCount; i++) {
            String[] subjectData = scanner.nextLine().split(" ");
            subjects.add(new Subject(subjectData[0], subjectData[1]));
        }
    
        int tCount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < tCount; i++) {
            String[] teacherData = scanner.nextLine().split(" ");
            Teacher teacher = new Teacher(teacherData[0], teacherData[1], teacherData[2], teacherData[3]);
            teachers.add(teacher);
    
            for (int j = 4; j < teacherData.length; j++) {
                String subjectName = teacherData[j];
                subjects.stream()
                        .filter(subject -> subject.getName().equals(subjectName))
                        .findFirst()
                        .ifPresent(teacher::addSubject);
            }
        }
    } 

    
    public void printToConsole() {
        System.out.println("Subjects:");
        subjects.forEach(System.out::println);
        System.out.println("Teachers:");
        teachers.forEach(System.out::println);
    }

    public void readFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int sCount = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < sCount; i++) {
                String[] subjectData = scanner.nextLine().split(" ");
                subjects.add(new Subject(subjectData[0], subjectData[1]));
            }

            int tCount = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < tCount; i++) {
                String[] teacherData = scanner.nextLine().split(" ");
                Teacher teacher = new Teacher(teacherData[0], teacherData[1], teacherData[2], teacherData[3]);
                teachers.add(teacher);

                for (int j = 4; j < teacherData.length; j++) {
                    String subjectName = teacherData[j];
                    subjects.stream()
                            .filter(subject -> subject.getName().equals(subjectName))
                            .findFirst()
                            .ifPresent(teacher::addSubject);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public void printToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println(subjects.size());
            for (Subject subject : subjects) {
                writer.println(subject.getName() + " " + subject.getTeacher().getFullName());
            }
            writer.println(teachers.size());
            for (Teacher teacher : teachers) {
                writer.print(teacher.getFullName() + " " + teacher.phone + " " + teacher.email);
                for (Subject subject : teacher.subjects) {
                    writer.print(" " + subject.getName());
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void printToObject(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(teachers);
        } catch (IOException e) {
            System.out.println("Error serializing object: " + e.getMessage());
        }
    }

    public void readFromObject(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            teachers = (List<Teacher>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error deserializing object: " + e.getMessage());
        }
    }
}
