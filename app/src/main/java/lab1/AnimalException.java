/*
 *  Program: Operacje na obiektach klasy Animal
 *     Plik: AnimalException.java
 *           -definicja publicznej klasy AnimalException
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: pazdziernik 2022 r.
 */
package lab1.data;

public class AnimalException extends Exception {

    private static final long serialVersionUID = 1L;

    public AnimalException(String message) {
        super(message);
    }

}
