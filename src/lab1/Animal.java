/*
 *  Program: Operacje na obiektach klasy Animal
 *     Plik: Animal.java
 *           -definicja publicznej klasy Animal
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: pazdziernik 2022 r.
 */



/*
    Klasa Animal reprezentuje zwierzeta, ktore sa opisane za pomoca nastepujacych atrybutow, ktore maja swoje ogranicznia:
    - nazwa (niepusty ciag znakow)
    - imie (niepusty ciag znakow)
    - plec (przyjmuje wartosc 1 dla samicy i wartosc 0 dla samca)
    - wiek (nie moze byc ujemny)
    - gatunek (musi zawierac jedna z pozycji typu wyliczeniowego)
 */

package lab1.data;

import lab3.data.GroupOfAnimals;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Animal implements Serializable, Comparable<Animal> {
    private String name;
    private String petName;
    private boolean gender;
    private int age;
    private AnimalType type;


    public Animal(String name, String petName) throws AnimalException {
        setName(name);
        setPetName(petName);
        setType(type);
    }

    public Animal(String name, String petName, boolean gender, int age, AnimalType type) throws AnimalException {
        setName(name);
        setPetName(petName);
        setGender(gender);
        setAge(age);
        setType(type);
    }
    public Animal(String name, String petName, String age,  String gender, String type) throws AnimalException {
        setName(name);
        setPetName(petName);
        setGender(gender);
        setAge(age);
        setType(type);
    }

    public Animal(Animal animal) {
        this.name = animal.name;
        this.petName = animal.petName;
        this.gender = animal.gender;
        this.age = animal.age;
        this.type = animal.type;
    }

    public String getName() {
        return name;
    }

    public String getPetName() {
        return petName;
    }

    public int getAge() {
        return age;
    }

    public boolean getGender() {
        return gender;
    }

    public String getGenderString() {
        return gender ? "samica" : "samiec";
    }

    public AnimalType getType() {
        return type;
    }

    public void setName(String name) throws AnimalException {
        if (name == null || name.equals(""))
            throw new AnimalException("Pole <NAME> nie może być puste.");
        this.name = name;
    }

    public void setPetName(String petName) throws AnimalException {
        if (petName == null || petName.equals(""))
            throw new AnimalException("Pole <PETNAME> nie może być puste.");
        this.petName = petName;
    }

    public void setAge(int age) throws AnimalException {
        if (age < 0)
            throw new AnimalException("Wiek zwierzecia nie może być ujemny.");
        this.age = age;
    }

    public void setAge(String age) throws AnimalException {
        if (name == null || name.equals("")) {
            setAge(0);
            return;
        }
        try {
            setAge(Integer.parseInt(age));
        } catch (NumberFormatException e) {
            throw new AnimalException("Wiek musi być intem.");
        }
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setGender(String gender) throws AnimalException {
        if (gender.equalsIgnoreCase("samica"))
            setGender(true);
        else if (gender.equalsIgnoreCase("samiec"))
            setGender(false);
        else
            throw new AnimalException("Plec zwierzecia moze byc tylko okreslona jako samiec i samica.");
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public void setType(String type_name) throws AnimalException {
        if (type_name == null || type_name.equals("")) {
            this.type = AnimalType.UNKNOWN;
            return;
        }
        for (AnimalType type1 : AnimalType.values()) {
            if (type1.type.equals(type_name)) {
                this.type = type1;
                return;
            }
        }
        throw new AnimalException("Nie ma takiego gatunku.");
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf);
    }

    public static void printToFile(PrintWriter writer, Animal animal) {
        writer.println(animal.name + "#" + animal.petName +
                "#" + animal.age + "#" + animal.getGenderString() + "#" + animal.type.type);
    }

    public static void printToFile(String file_name, Animal animal) throws AnimalException {
        String fileEx = getFileExtension(file_name);
        if (fileEx.equals(".txt") || fileEx.equals(""))
            try (PrintWriter writer = new PrintWriter(file_name)) {
                printToFile(writer, animal);
            } catch (FileNotFoundException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        else if(fileEx.equals(".ser")){
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file_name))) {
                outputStream.writeObject(animal);
            } catch (IOException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        }
    }
    public static void printToFile(String file_name, GroupOfAnimals animals) throws AnimalException {
        String fileEx = getFileExtension(file_name);
        if (fileEx.equals(".txt") || fileEx.equals(""))
            try (PrintWriter writer = new PrintWriter(file_name)) {
                for(Animal a: animals){
                    printToFile(writer, a);
                }
            } catch (FileNotFoundException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        else if(fileEx.equals(".ser")){
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file_name))) {
                outputStream.writeObject(animals);
            } catch (IOException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        }
    }


    public static ArrayList<Animal> readFromFile(BufferedReader reader) throws AnimalException {
        try {
            ArrayList<Animal> animals = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
            String[] txt = line.split("#");
            animals.add(new Animal(txt[0], txt[1],txt[2],txt[3],txt[4]));
            }
            return animals;
        } catch (IOException e) {
            throw new AnimalException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }


    public static ArrayList<Animal> readFromFile(String file_name) throws AnimalException {
        String fileEx = getFileExtension(file_name);
        if (fileEx.equals(".txt") || fileEx.equals(""))
            try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
                return Animal.readFromFile(reader);
            } catch (FileNotFoundException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            } catch (IOException e) {
                throw new AnimalException("Wystąpił błąd podczas odczytu danych z pliku.");
            }
        else if (fileEx.equals(".ser")) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file_name))) {
                return (ArrayList<Animal>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new AnimalException("Wystąpił błąd podczas odczytu danych z pliku.");
            }
        }
        return null;
    }

        @Override
        public String toString() {
            return name + " " + petName + " " + age + " " + getGenderString() + " " + type.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, petName);
        }

        @Override
        public boolean equals(Object o) {
            if (getClass() != o.getClass())
                return false;
            return this.name.equals(((Animal) o).name) && this.petName.equals(((Animal) o).petName);
        }

        @Override
        public int compareTo(Animal o) {
        return this.name.compareTo(o.name) == 0 ? this.name.compareTo(o.name) : this.petName.compareTo(o.petName);
        }


    }
