package program;

import model.Auto;
import model.Student;
import repository.AutoRepository;
import repository.StudentRepository;

public class Program {
    public static void main(String[] args) {
        /*try (StudentRepository repository
                     = new StudentRepository()) {
            Student student = new Student(8, "Vlad", 23, 900, 2500);

            if (repository.add(student)) {
                System.out.println(student);

            } else {
                System.out.println("Not added");
            }

            System.out.println(repository.getStudents());
            System.out.println(repository.getById(2));

            System.out.println(repository.delete(student));

            Student student1 = new Student(4, "MAx", 23, 876, 1500);

            System.out.println(repository.update(student1));

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try (AutoRepository repository
                     = new AutoRepository()) {
            /*Auto auto = new Auto(6, "Toyota", 155, 2010, 4);

            if (repository.add(auto)) {
                System.out.println(auto);

            } else {
                System.out.println("Not added");
            }

            System.out.println(repository.getAuto());
            System.out.println(repository.getById(6));

            System.out.println(repository.delete(auto));
            System.out.println(repository.getAuto());

            Auto auto1 = new Auto(3, "Toyota", 155, 2015, 1);

            System.out.println(repository.update(auto1));*/

            Auto auto = new Auto("Toyota", 160, 2020, 3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
