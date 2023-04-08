/*
 *  Program: Okno modalne do dodawania/edytowania instancji klasy Animal
 *     Plik: AnimalWindowDialog.java
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: listopad 2022 r.
 */

package lab2.data;

import lab1.data.Animal;
import lab1.data.AnimalException;
import lab1.data.AnimalType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimalWindowDialog extends JDialog implements ActionListener {

    private static final int WINDOW_WIDTH = 220;
    private static final int WINDOW_HEIGHT = 220;

    private Animal animal;

    JLabel animalNameLabel = new JLabel("Nazwa:");
    JTextField animalNameText = new JTextField(12);

    JLabel animalPetNameLabel = new JLabel("Imie:");
    JTextField animalPetNameText = new JTextField(12);

    JLabel animalGenderLabel = new JLabel("Plec:");
    JRadioButton animalGenderRadio1 = new JRadioButton("Samiec");
    JRadioButton animalGenderRadio2 = new JRadioButton("Samica");
    ButtonGroup groupRadio = new ButtonGroup();


    JLabel animalAgeLabel = new JLabel("Wiek:");
    JTextField animalAgeText = new JTextField(12);

    JLabel animalTypeLabel = new JLabel("Type:");
    JComboBox<AnimalType> typeBox = new JComboBox<AnimalType>(AnimalType.values());


    JButton saveAnimal = new JButton("Zapisz");
    JButton exitAnimal = new JButton("Wyjdz");


    private AnimalWindowDialog(Window parent, Animal animal) {
        super(parent, ModalityType.DOCUMENT_MODAL);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(parent);
        setResizable(false);

        if (animal == null)
            setTitle("Nowe zwierze");
        else
            setTitle("Zwierze - " + animal.getPetName());

        this.animal = animal;
        if (animal != null) {
            animalNameText.setText(animal.getName());
            animalPetNameText.setText(animal.getPetName());
            animalGenderRadio2.setSelected(animal.getGender());
            animalAgeText.setText("" + animal.getAge());
            typeBox.setSelectedItem(animal.getType());
        }

        JPanel panel = new JPanel();
        panel.add(animalNameLabel);
        panel.add(animalNameText);
        panel.add(animalPetNameLabel);
        panel.add(animalPetNameText);
        panel.add(animalGenderLabel);
        panel.add(animalGenderRadio1);
        panel.add(animalGenderRadio2);
        panel.add(animalAgeLabel);
        panel.add(animalAgeText);
        panel.add(animalTypeLabel);
        panel.add(typeBox);
        panel.add(saveAnimal);
        panel.add(exitAnimal);

        groupRadio.add(animalGenderRadio1);
        groupRadio.add(animalGenderRadio2);
        animalGenderRadio1.setSelected(true);

        animalGenderRadio1.addActionListener(this);
        animalGenderRadio2.addActionListener(this);

        saveAnimal.addActionListener(this);
        exitAnimal.addActionListener(this);
        setContentPane(panel);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveAnimal) {
            try {
                if (animal == null)
                    animal = new Animal(animalNameText.getText(), animalPetNameText.getText());
                else {
                    animal.setName(animalNameText.getText());
                    animal.setPetName(animalPetNameText.getText());
                }
                animal.setGender(animalGenderRadio2.isSelected());
                animal.setAge(animalAgeText.getText());
                animal.setType((AnimalType) typeBox.getSelectedItem());
                dispose();
            } catch (AnimalException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == exitAnimal)
            dispose();
    }

    public static Animal createAnimal(Window parent) {
        AnimalWindowDialog dialog = new AnimalWindowDialog(parent, null);
        return dialog.animal;
    }

    public static Animal editAnimal(Window parent, Animal animal) {
        AnimalWindowDialog dialog = new AnimalWindowDialog(parent, animal);
        return dialog.animal;
    }
}
