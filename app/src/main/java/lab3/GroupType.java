/*
 *  Program: Typ wyliczeniowy do kolekcji
 *     Plik: GroupType.java
 *
 *    Autor: Lukasz Wdowiak 264026
 *     Data: listopad 2022 r.
 */
package lab3.data;

import lab1.data.Animal;
import lab1.data.AnimalException;

import java.util.*;

public enum GroupType {
    VECTOR("Vector"),
    ARRAY_LIST("ArrayList"),
    LINKED_LIST("LinkedList"),
    HASH_SET("HashSet"),
    TREE_SET("TreeSet");

    String typeName;

    private GroupType(String type_name) {
        typeName = type_name;
    }
    @Override
    public String toString() {
        return typeName;
    }


    public static GroupType find(String type_name){
        for(GroupType type : values()){
            if (type.typeName.equals(type_name)){
                return type;
            }
        }
        return null;
    }
    public Collection<Animal> createCollection() throws AnimalException {
        return switch (this) {
            case VECTOR -> new Vector<Animal>();
            case ARRAY_LIST -> new ArrayList<Animal>();
            case HASH_SET -> new HashSet<Animal>();
            case LINKED_LIST -> new LinkedList<Animal>();
            case TREE_SET -> new TreeSet<Animal>();
            default -> throw new AnimalException("Podany typ kolekcji nie zostal zaimplementowany.");
        };
    }
}
