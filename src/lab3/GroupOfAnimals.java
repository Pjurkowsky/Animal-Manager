/*
 *  Program: Klasa do przechowywania w róznych kolekcjach klasy Animal
 *     Plik: GroupOfAnimals.java
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: listopad 2022 r.
 */
package lab3.data;

import lab1.data.Animal;
import lab1.data.AnimalException;
import lab1.data.AnimalType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static lab1.data.Animal.getFileExtension;

public class GroupOfAnimals implements Iterable<Animal>, Serializable {

    private String name;
    private GroupType type;
    private Collection<Animal> collection;


    public GroupOfAnimals(String name, GroupType type) throws AnimalException {
        setName(name);
        setType(type);
        collection = this.type.createCollection();
    }

    public GroupOfAnimals(String name, String type) throws AnimalException {
        setName(name);
        setType(type);
        collection = this.type.createCollection();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws AnimalException {
        if (name == null || name.equals(""))
            throw new AnimalException("Nazwa grupy musi istniec");
        this.name = name;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) throws AnimalException {
        if (type == null)
            throw new AnimalException("Nazwa grupy musi istniec");
        this.type = type;
        //TODO
    }

    public void setType(String type) throws AnimalException {
        if (type == null || type.equals("")) {
            this.type = GroupType.VECTOR;
            return;
        }
        for (GroupType t : GroupType.values()) {
            if (t.typeName.equals(type)) {
                setType(t);
                return;
            }
        }
        throw new AnimalException("Nie ma takiej kolekcji.");
    }

    public boolean add(Animal e) {
        return collection.add(e);
    }


    public boolean remove(Animal e) {
        return collection.remove(e);
    }

    @Override
    public Iterator<Animal> iterator() {
        return collection.iterator();
    }

    public int size() {
        return collection.size();
    }

    @Override
    public String toString() {
        return name + "#" + type + "#" + size();
    }


    public static void printToFile(PrintWriter writer, GroupOfAnimals group) {
        writer.println(group.toString());
    }

    public static void printToFile(String file_name, GroupOfAnimals group) throws AnimalException {
        String fileEx = getFileExtension(file_name);
        if (fileEx.equals(".txt") || fileEx.equals(""))
            try (PrintWriter writer = new PrintWriter(file_name)) {
                printToFile(writer, group);
                for (Animal a : group)
                    Animal.printToFile(writer, a);
            } catch (FileNotFoundException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        else if (fileEx.equals(".ser")) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file_name))) {
                ArrayList<GroupOfAnimals> list = new ArrayList<>();
                list.add(group);
                outputStream.writeObject(list);
            } catch (IOException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        }
    }

    public static void printToFile(String file_name, ArrayList<GroupOfAnimals> groups) throws AnimalException {
        String fileEx = Animal.getFileExtension(file_name);
        if (fileEx.equals(".txt") || fileEx.equals(""))
            try (PrintWriter writer = new PrintWriter(file_name)) {
                for (GroupOfAnimals g : groups) {
                    printToFile(writer, g);
                    for (Animal a : g)
                        Animal.printToFile(writer, a);
                }
            } catch (FileNotFoundException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        else if (fileEx.equals(".ser")) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file_name))) {
                outputStream.writeObject(groups);
            } catch (IOException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            }
        }
    }

    public static ArrayList<GroupOfAnimals> readFromFile(BufferedReader reader) throws AnimalException {
        try {
            ArrayList<GroupOfAnimals> groups = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] txt = line.split("#");
                GroupOfAnimals group = new GroupOfAnimals(txt[0], txt[1]);
                int size = Integer.parseInt(txt[2]);
                int counter = 0;
                while ((line = reader.readLine()) != null && counter < size - 1) {
                    String[] txt2 = line.split("#");
                    group.add(new Animal(txt2[0], txt2[1], txt2[2], txt2[3], txt2[4]));
                    counter++;
                }
                groups.add(group);
            }
            return groups;
        } catch (IOException e) {
            throw new AnimalException("Wystąpił błąd podczas odczytu danych z pliku.");
        }
    }

    public static ArrayList<GroupOfAnimals> readFromFile(String file_name) throws AnimalException {
        String fileEx = getFileExtension(file_name);
        if (fileEx.equals(".txt") || fileEx.equals(""))
            try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
                return GroupOfAnimals.readFromFile(reader);
            } catch (FileNotFoundException e) {
                throw new AnimalException("Nie odnaleziono pliku " + file_name);
            } catch (IOException e) {
                throw new AnimalException("Wystąpił błąd podczas odczytu danych z pliku.");
            }
        else if (fileEx.equals(".ser")) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file_name))) {
                return (ArrayList<GroupOfAnimals>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new AnimalException("Wystąpił błąd podczas odczytu danych z pliku.");
            }
        }
        return null;
    }

    public static GroupOfAnimals conjunction(GroupOfAnimals g1, GroupOfAnimals g2) throws AnimalException {
        GroupOfAnimals newGroup = new GroupOfAnimals("a", g1.getType());
        for (Animal a1 : g1)
            for (Animal a2 : g2)
                if (a1.equals(a2)) {
                    System.out.println(a1.getName() + a2.getName() + a1.equals(a2));
                    newGroup.add(a1);
                }
        return newGroup;
    }

    public static GroupOfAnimals disjunction(GroupOfAnimals g1, GroupOfAnimals g2) throws AnimalException {
        GroupOfAnimals newGroup = new GroupOfAnimals("a", g1.getType());

        newGroup.collection.addAll(g1.collection);
        newGroup.collection.addAll(g2.collection);
        return newGroup;
    }

    public static GroupOfAnimals difference(GroupOfAnimals g1, GroupOfAnimals g2) throws AnimalException {
        GroupOfAnimals newGroup = new GroupOfAnimals("a", g1.getType());
        newGroup.collection.addAll(g1.collection);
        for(Animal a1: g2)
            newGroup.remove(a1);
        return newGroup;
    }

    public static GroupOfAnimals exclusiveDisjunction(GroupOfAnimals g1, GroupOfAnimals g2) throws AnimalException {
        return disjunction(difference(g1,g2),difference(g2,g1));
    }
}
