
/*
 *  Program: Typ wyliczeniowy gatunkow
 *     Plik: AnimalType.java
 *           -definicja publicznego enuma AnimalType
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: pazdziernik 2022 r.
 */
package lab1.data;

public enum AnimalType {
    UNKNOWN("------"),
    SSAK("ssak"),
    GAD("gad"),
    PLAZ("plaz"),
    RYBA("ryba"),
    PTAK("ptak"),
    OWAD("owad");

    String type;

    private AnimalType(String type_name){
        type = type_name;
    }

}
