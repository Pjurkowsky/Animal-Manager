/*
 *  Program: Okno modalne do zarzadzania klasa Animal
 *     Plik: GroupOfAnimalsWindowApp.java
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: listopad 2022 r.
 */
package lab3.data;

import lab1.data.Animal;
import lab1.data.AnimalException;
import lab1.data.AnimalType;
import lab2.data.AnimalWindowDialog;
import lab3.data.GroupOfAnimals;
import lab3.data.GroupType;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GroupOfAnimalsDialog extends JDialog implements ActionListener {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;

    private static final int TABLE_WIDTH = 300;
    private static final int TABLE_HEIGHT = 200;

    JButton addAnimal = new JButton("Dodaj");
    JButton deleteAnimal = new JButton("Usun");
    JButton editAnimal = new JButton("Edytuj");
    JButton readAnimal = new JButton("Wczytaj");
    JButton saveAnimal = new JButton("Zapisz");
    JButton saveAllAnimal = new JButton("Zapisz wszystkie");
    JButton infoAnimal = new JButton("O programie");
    JButton exitAnimal = new JButton("Wyjdz");

    ViewGroupOfAnimals viewTable;

    GroupOfAnimalsDialog(Window parent, GroupOfAnimals group) throws AnimalException {
        super(parent, ModalityType.DOCUMENT_MODAL);

        viewTable = new ViewGroupOfAnimals(TABLE_WIDTH, TABLE_HEIGHT, group);


        setTitle("Group Of Animals App");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        AnimalMenuBar menuBar = new AnimalMenuBar(this, viewTable);

        setJMenuBar(menuBar);

        panel.add(viewTable);

        rightPanel.add(addAnimal);
        rightPanel.add(deleteAnimal);
        rightPanel.add(editAnimal);
        rightPanel.setLayout(new GridLayout(3, 1, 10, 10));

        bottomPanel.add(readAnimal);
        bottomPanel.add(saveAnimal);
        bottomPanel.add(saveAllAnimal);
        bottomPanel.add(infoAnimal);
        bottomPanel.add(exitAnimal);
        bottomPanel.setLayout(new GridLayout(4, 1, 10, 10));

        panel.add(rightPanel);
        panel.add(bottomPanel);


        addAnimal.addActionListener(this);
        editAnimal.addActionListener(this);
        deleteAnimal.addActionListener(this);
        readAnimal.addActionListener(this);
        saveAnimal.addActionListener(this);
        saveAllAnimal.addActionListener(this);
        infoAnimal.addActionListener(this);
        exitAnimal.addActionListener(this);

        setLayout(new GridLayout(2, 2));

        viewTable.refreshView();
        setContentPane(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            addAction(e.getSource() == addAnimal, this, viewTable);
            deleteAction(e.getSource() == deleteAnimal, viewTable);
            editAction(e.getSource() == editAnimal, this, viewTable);
            readAction(e.getSource() == readAnimal, this, viewTable);
            saveAction(e.getSource() == saveAnimal, this, viewTable);
            saveAllAction(e.getSource() == saveAllAnimal, this, viewTable);
            infoAction(e.getSource() == infoAnimal, this);
            exitAction(e.getSource() == exitAnimal, this);

            viewTable.refreshView();
        } catch (AnimalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
        }

    }


    static void addAction(boolean b, Window parent, ViewGroupOfAnimals viewTable) {
        if (b) {
            Animal animal = AnimalWindowDialog.createAnimal(parent);
            viewTable.add(animal);
        }
    }

    static void editAction(boolean b, Window parent, ViewGroupOfAnimals viewTable) {
        if (b) {
            int index = viewTable.getSelectedIndex();
            if (index >= 0) {
                Iterator<Animal> iterator = viewTable.iterator();
                while (index-- > 0)
                    iterator.next();
                AnimalWindowDialog.editAnimal(parent, iterator.next());
            }
        }
    }

    static void deleteAction(boolean b, ViewGroupOfAnimals viewTable) {
        if (b) {
            int index = viewTable.getSelectedIndex();
            if (index >= 0) {
                Iterator<Animal> iterator = viewTable.iterator();
                while (index-- > 0)
                    iterator.next();
                viewTable.remove(iterator.next());
            }
        }
    }

    static void saveAction(boolean b, Window parent, ViewGroupOfAnimals viewTable) throws AnimalException {
        if (b) {
            int index = viewTable.getSelectedIndex();
            if (index >= 0) {
                Iterator<Animal> iterator = viewTable.iterator();
                while (index-- > 0)
                    iterator.next();
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "txt, ser", "txt", "ser");
                chooser.setFileFilter(filter);
                chooser.setCurrentDirectory(new File("."));
                int returnVal = chooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    if (fileName.equals("")) return;
                    Animal.printToFile(fileName, iterator.next());
                }
            }
        }
    }

    static void saveAllAction(boolean b, Window parent, ViewGroupOfAnimals viewTable) throws AnimalException {
        if (b) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "txt, ser", "txt", "ser");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File("."));
            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileName = chooser.getSelectedFile().getAbsolutePath();
                if (fileName.equals("")) return;
                Animal.printToFile(fileName, viewTable.getGroup());
            }
        }
    }

    static void readAction(boolean b, Window parent, ViewGroupOfAnimals viewTable) throws AnimalException {
        if (b) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "txt, ser", "txt", "ser");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File("."));
            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION)
                for (Animal a : Animal.readFromFile(chooser.getSelectedFile().getAbsolutePath()))
                    viewTable.add(a);


        }
    }

    static void infoAction(boolean b, Window parent) {
        if (b) {
            JOptionPane.showMessageDialog(parent, "Program Animal - wersja okienkowa\n" +
                    "Autor: Lukasz Wdowiak 264026 \n" +
                    "Data:  listopad 2022 r.\n");
        }
    }

    static void exitAction(boolean b, Window parent) {
        if (b) {
            parent.dispose();
        }
    }

    public static GroupOfAnimals createNewGroupOfAnimals(Window parent) throws AnimalException {
        String name = JOptionPane.showInputDialog("Podaj nazwe grupy");
        GroupType selectedValue = (GroupType) JOptionPane.showInputDialog(parent,
                "Choose one", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                GroupType.values(), GroupType.VECTOR);
        GroupOfAnimalsDialog dialog = new GroupOfAnimalsDialog(parent, new GroupOfAnimals(name,selectedValue));
        return dialog.viewTable.getGroup();
    }
    public static GroupOfAnimals editGroupOfAnimals(Window parent, GroupOfAnimals group) throws AnimalException {
        GroupOfAnimalsDialog dialog = new GroupOfAnimalsDialog(parent, group);
        return dialog.viewTable.getGroup();
    }
}

class AnimalMenuBar extends JMenuBar implements ActionListener {
    JMenu fileMenu = new JMenu("Plik");
    JMenuItem readMenuItem = new JMenuItem("Wczytaj");
    JMenuItem writeMenuItem = new JMenuItem("Zapisz");
    JMenuItem writeAllMenuItem = new JMenuItem("Zapisz wszystko");
    JMenuItem exitMenuItem = new JMenuItem("Wyjdz");

    JMenu animalMenu = new JMenu("Zwierze");
    JMenuItem addMenuItem = new JMenuItem("Dodaj");
    JMenuItem editMenuItem = new JMenuItem("Edytuj");
    JMenuItem deleteMenuItem = new JMenuItem("Usun");

    JMenu helpMenu = new JMenu("Pomoc");
    JMenuItem aboutMenuItem = new JMenuItem("O programie");


    Window parent;
    ViewGroupOfAnimals viewTable;

    public AnimalMenuBar(Window parent, ViewGroupOfAnimals viewTable) {
        this.parent = parent;
        this.viewTable = viewTable;

        fileMenu.add(readMenuItem);
        fileMenu.add(writeMenuItem);
        fileMenu.add(writeAllMenuItem);
        fileMenu.add(exitMenuItem);

        animalMenu.add(addMenuItem);
        animalMenu.add(editMenuItem);
        animalMenu.add(deleteMenuItem);

        helpMenu.add(aboutMenuItem);

        readMenuItem.addActionListener(this);
        writeMenuItem.addActionListener(this);
        writeAllMenuItem.addActionListener(this);

        addMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);
        aboutMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        add(fileMenu);
        add(animalMenu);
        add(helpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            GroupOfAnimalsDialog.addAction(e.getSource() == addMenuItem, parent, viewTable);
            GroupOfAnimalsDialog.deleteAction(e.getSource() == deleteMenuItem, viewTable);
            GroupOfAnimalsDialog.editAction(e.getSource() == editMenuItem, parent, viewTable);
            GroupOfAnimalsDialog.readAction(e.getSource() == readMenuItem, parent, viewTable);
            GroupOfAnimalsDialog.saveAction(e.getSource() == writeMenuItem, parent, viewTable);
            GroupOfAnimalsDialog.saveAllAction(e.getSource() == writeAllMenuItem, parent, viewTable);
            GroupOfAnimalsDialog.infoAction(e.getSource() == aboutMenuItem, parent);
            GroupOfAnimalsDialog.exitAction(e.getSource() == exitMenuItem, parent);

            viewTable.refreshView();
        } catch (AnimalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class ViewGroupOfAnimals extends JScrollPane {

    private final GroupOfAnimals group;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ViewGroupOfAnimals(int width, int height, GroupOfAnimals group) {
        this.group = group;

        String[] columnNames = {"Nazwa", "Imie", "Wiek", "Plec", "Gatunek"};
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createTitledBorder("Lista zwierzat:"));


        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        setViewportView(table);
    }

    public void refreshView() {
        tableModel.setRowCount(0);
        for (Animal a : group) {
            tableModel.addRow(a.toString().split("\\W+"));
        }
    }

    public void add(Animal animal) {
        if (animal != null)
            group.add(animal);
    }

    public void remove(Animal animal) {
        group.remove(animal);
    }

    public GroupOfAnimals getGroup() {
        return group;
    }

    int getSelectedIndex() {
        int index = table.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Zadne zwierze nie jest zaznaczone.", "Blad", JOptionPane.ERROR_MESSAGE);
        }
        return index;
    }

    Iterator<Animal> iterator() {
        return group.iterator();
    }
}



