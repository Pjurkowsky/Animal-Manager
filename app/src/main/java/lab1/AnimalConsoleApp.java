/*
 *  Program: Aplikacja konsolowa do zarzadzania klasa Animal
 *     Plik: AnimalConsoleApp.java
 *
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: pazdziernik 2022 r.
 */

package lab1.data;

import java.util.Arrays;

public class AnimalConsoleApp {
    private static final String GREETING_MESSAGE =
            "Program Animal - wersja konsolowa\n" +
                    "Autor: Lukasz Wdowiak 264026 \n" +
                    "Data:  pazdziernik 2022 r.\n";
    private static final String MENU =
            "MENU GLOWNE\n" +
                    "1 - Podaj dane nowego zwierzecia \n" +
                    "2 - Usun dane zwierzecia        \n" +
                    "3 - Modyfikuj dane zwierzecia   \n" +
                    "4 - Wczytaj dane z pliku   \n" +
                    "5 - Zapisz dane do pliku   \n" +
                    "0 - Zakoncz program        \n";

    private static final String CHANGE_MENU =
            "   Co zmienic?     \n" +
                    "1 - Nazwe zwierzecia           \n" +
                    "2 - Imie zwierzecia\n" +
                    "3 - Wiek  \n" +
                    "4 - Gatunek     \n" +
                    "5 - Plec \n" +
                    "0 - Powrot do menu glownego\n";


    private static ConsoleUserDialog UI = new ConsoleUserDialog();

    private Animal currentAnimal = null;

    public static void main(String[] args) {
        AnimalConsoleApp app = new AnimalConsoleApp();
        app.run();
    }

    public void run() {
        UI.printMessage(GREETING_MESSAGE);
        while (true) {
            UI.clearConsole();
            showAnimal(currentAnimal);
            try {
                switch (UI.enterInt(MENU + "==>> ")) {
                    case 1:
                        currentAnimal = createNewAnimal();
                        break;
                    case 2:
                        currentAnimal = null;
                        UI.printInfoMessage("Dane aktualnego zwierzecia zostaly usuniete");
                        break;
                    case 3:
                        if (currentAnimal == null) throw new AnimalException("Zadne zwierze nie zostalo utworzone.");
                        changeAnimalData(currentAnimal);
                        break;
                    case 4: {
                        try {
                            String file_name = UI.enterString("Podaj nazwe pliku: ");
                            currentAnimal = Animal.readFromFile(file_name).get(0);
                            UI.printInfoMessage("Dane aktualnego zwierzecia zostaly wczytane z pliku " + file_name);
                        } catch (NullPointerException e) {
                            UI.printErrorMessage("Nie mozna odczytac danych z pliku");
                        }
                    }
                    break;
                    case 5: {
                        try {
                            String file_name = UI.enterString("Podaj nazwe pliku: ");
                            Animal.printToFile(file_name, currentAnimal);
                            UI.printInfoMessage("Dane aktualnego zwierzecia zostaly zapisane do pliku " + file_name);
                        } catch (NullPointerException e) {
                            UI.printErrorMessage("Brak danych zwierzecia");
                        }
                    }

                    break;
                    case 0:
                        UI.printInfoMessage("\nProgram zakonczy dzialanie!");
                        System.exit(0);
                }
            } catch (AnimalException e) {
                UI.printErrorMessage(e.getMessage());
            }
        }
    }

    static Animal createNewAnimal() {
        String name = UI.enterString("Podaj nazwe: ");
        String pet_name = UI.enterString("Podaj imie zwierzecia: ");
        String age = UI.enterString("Podaj wiek: ");
        String gender = UI.enterString("Podaj plec (samiec, samica): ");
        UI.printMessage("Dozwolone gatunki:" + Arrays.deepToString(AnimalType.values()));
        String type_name = UI.enterString("Podaj gatunek: ");
        Animal animal;
        try {
            animal = new Animal(name, pet_name);
            animal.setAge(age);
            animal.setType(type_name);
            animal.setGender(gender);
        } catch (AnimalException e) {
            UI.printErrorMessage(e.getMessage());
            return null;
        }
        return animal;
    }

    static void changeAnimalData(Animal animal) {
        while (true) {
            UI.clearConsole();
            showAnimal(animal);

            try {
                switch (UI.enterInt(CHANGE_MENU + "==>> ")) {
                    case 1:
                        animal.setName(UI.enterString("Podaj nazwe zwierzecia: "));
                        break;
                    case 2:
                        animal.setPetName(UI.enterString("Podaj imie zwierzecia: "));
                        break;
                    case 3:
                        animal.setAge(UI.enterString("Podaj wiek: "));
                        break;
                    case 4:
                        UI.printMessage("Dozwolone gatuneki:" + Arrays.deepToString(AnimalType.values()));
                        animal.setType(UI.enterString("Podaj gatunek: "));
                        break;
                    case 5:
                        animal.setGender(UI.enterString("Podaj plec (samiec, samica):"));
                    case 0:
                        return;
                }
            } catch (AnimalException e) {

                UI.printErrorMessage(e.getMessage());
            }
        }
    }

    static void showAnimal(Animal animal) {
        StringBuilder sb = new StringBuilder();

        if (animal != null) {
            sb.append("Aktualne zwierze: \n");
            sb.append("Nazwa zwierzecia: " + animal.getName() + "\n");
            sb.append("Imie zwierzecia: " + animal.getPetName() + "\n");
            sb.append("Wiek: " + animal.getAge() + "\n");
            sb.append("Plec: " + animal.getGenderString() + "\n");
            sb.append("Gatunek: " + animal.getType() + "\n");
        } else
            sb.append("Brak danych zwierzecia" + "\n");
        UI.printMessage(sb.toString());
    }
}
