/*
 *  Program: Aplikacja okienkowa do zarzadzania klasa GroupOfAnimals
 *     Plik: GroupManagerApp.java
 *
 *    Autor: Łukasz Wdowiak 264026
 *     Data: listopad 2022 r.
 */
package lab3.data;

import lab1.data.Animal;
import lab1.data.AnimalException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class GroupManagerApp extends JFrame implements ActionListener {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 450;

    private static final int TABLE_WIDTH = 300;
    private static final int TABLE_HEIGHT = 250;

    ViewGroups viewGroups = new ViewGroups(TABLE_WIDTH, TABLE_HEIGHT);

    JButton addGroup = new JButton("Dodaj");
    JButton deleteGroup = new JButton("Usun");
    JButton editGroup = new JButton("Edytuj");
    JButton readGroup = new JButton("Wczytaj");
    JButton saveGroup = new JButton("Zapisz");
    JButton saveAllGroups = new JButton("Zapisz wszystkie");
    JButton infoGroup = new JButton("O programie");
    JButton exitGroup = new JButton("Zakoncz program");

    JButton conjunctionGroup = new JButton("A * B");
    JButton disjunctionGroup = new JButton("A + B");
    JButton differenceGroup = new JButton("A / B");
    JButton exclusiveDisjunctionGroup = new JButton("A - B");

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GroupManagerApp();
            }
        });
    }

    GroupManagerApp()  {
        setTitle("GroupManager");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        try{


        GroupOfAnimals a = new GroupOfAnimals("hotel", GroupType.VECTOR);
        a.add(new Animal("Zyrafa","Szyjus","69","samiec","ssak"));
        a.add(new Animal("Lew","Ryczek","12","samiec","ssak"));
        GroupOfAnimals b = new GroupOfAnimals("motel", GroupType.HASH_SET);
        b.add(new Animal("Zyrafa","Szyjus","69","samiec","ssak"));
        b.add(new Animal("Jaskolka","Bert","32","samica","ptak"));

        viewGroups.add(a);
        viewGroups.add(b);
        }
        catch (AnimalException ex){}


        GroupMenuBar menuBar = new GroupMenuBar(this, viewGroups);
        setJMenuBar(menuBar);

        JPanel panel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        leftPanel.add(conjunctionGroup);
        leftPanel.add(disjunctionGroup);
        leftPanel.add(differenceGroup);
        leftPanel.add(exclusiveDisjunctionGroup);
        leftPanel.setLayout(new GridLayout(4, 1, 10, 10));


        rightPanel.add(addGroup);
        rightPanel.add(deleteGroup);
        rightPanel.add(editGroup);
        rightPanel.setLayout(new GridLayout(3, 1, 10, 10));

        bottomPanel.add(readGroup);
        bottomPanel.add(saveGroup);
        bottomPanel.add(saveAllGroups);
        bottomPanel.add(infoGroup);
        bottomPanel.add(exitGroup);

        bottomPanel.setLayout(new GridLayout(4, 1, 10, 10));

        panel.add(leftPanel);
        panel.add(viewGroups);
        panel.add(rightPanel);
        panel.add(bottomPanel);

        setLayout(new GridLayout(2, 3));

        addGroup.addActionListener(this);
        deleteGroup.addActionListener(this);
        editGroup.addActionListener(this);
        readGroup.addActionListener(this);
        saveGroup.addActionListener(this);
        saveAllGroups.addActionListener(this);
        infoGroup.addActionListener(this);
        exitGroup.addActionListener(this);
        conjunctionGroup.addActionListener(this);
        disjunctionGroup.addActionListener(this);
        differenceGroup.addActionListener(this);
        exclusiveDisjunctionGroup.addActionListener(this);

        viewGroups.refreshView();
        setContentPane(panel);
    }

    static void addAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            GroupOfAnimals group = GroupOfAnimalsDialog.createNewGroupOfAnimals(parent);
            viewTable.add(group);
        }
    }

    static void editAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            int index = viewTable.getSelectedIndex();
            if (index >= 0) {
                Iterator<GroupOfAnimals> iterator = viewTable.iterator();
                while (index-- > 0)
                    iterator.next();
                GroupOfAnimalsDialog.editGroupOfAnimals(parent, iterator.next());
            }
        }
    }

    static void deleteAction(boolean b, ViewGroups viewTable) {
        if (b) {
            int index = viewTable.getSelectedIndex();
            if (index >= 0) {
                Iterator<GroupOfAnimals> iterator = viewTable.iterator();
                while (index-- > 0)
                    iterator.next();
                viewTable.remove(iterator.next());
            }
        }
    }

    static void saveAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            int index = viewTable.getSelectedIndex();
            if (index >= 0) {
                Iterator<GroupOfAnimals> iterator = viewTable.iterator();
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
                    GroupOfAnimals.printToFile(fileName, iterator.next());
                }
            }
        }
    }

    static void saveAllAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
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
                GroupOfAnimals.printToFile(fileName, viewTable.getGroups());
            }
        }
    }

    static void readAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "txt, ser", "txt", "ser");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File("."));
            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION)
                for (GroupOfAnimals a : GroupOfAnimals.readFromFile(chooser.getSelectedFile().getAbsolutePath()))
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

    static void conjunctionAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            try {
                GroupOfAnimals g1 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals g2 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals temp = GroupOfAnimals.conjunction(g1, g2);
                temp.setName(g1.getName() + "AND" +  g2.getName());
                viewTable.add(temp);

            } catch (RuntimeException e) {
                throw new AnimalException("Musi byc przynajmniej jedna grupa, aby wykonac taka operacje");
            }
        }
    }

    static void disjunctionAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            try {
                GroupOfAnimals g1 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals g2 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals temp = GroupOfAnimals.disjunction(g1, g2);
                temp.setName(g1.getName() + "OR" + g2.getName());
                viewTable.add(temp);
            } catch (RuntimeException e) {
                throw new AnimalException("Musi byc przynajmniej jedna grupa, aby wykonac taka operacje");
            }
        }
    }

    static void differenceAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            try {
                GroupOfAnimals g1 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals g2 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals temp = GroupOfAnimals.difference(g1, g2);
                temp.setName(g1.getName() + "DIF" +  g2.getName());
                viewTable.add(temp);
            } catch (RuntimeException e) {
                throw new AnimalException("Musi byc przynajmniej jedna grupa, aby wykonac taka operacje");
            }
        }
    }

    static void exclusiveDisjunctionAction(boolean b, Window parent, ViewGroups viewTable) throws AnimalException {
        if (b) {
            try {
                GroupOfAnimals g1 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals g2 = (GroupOfAnimals) JOptionPane.showInputDialog(parent,
                        "Wybierz", "",
                        JOptionPane.INFORMATION_MESSAGE, null, viewTable.getGroups().toArray(), viewTable.getGroups().toArray()[0]
                );
                GroupOfAnimals temp = GroupOfAnimals.exclusiveDisjunction(g1, g2);
                temp.setName(g1.getName() + "XOR" +  g2.getName());
                viewTable.add(temp);

            } catch (RuntimeException e) {
                throw new AnimalException("Musi byc przynajmniej jedna grupa, aby wykonac taka operacje");
            }

        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            addAction(e.getSource() == addGroup, this, viewGroups);
            editAction(e.getSource() == editGroup, this, viewGroups);
            deleteAction(e.getSource() == deleteGroup, viewGroups);
            readAction(e.getSource() == readGroup, this, viewGroups);
            saveAction(e.getSource() == saveGroup, this, viewGroups);
            saveAllAction(e.getSource() == saveAllGroups, this, viewGroups);
            infoAction(e.getSource() == infoGroup, this);
            exitAction(e.getSource() == exitGroup, this);
            conjunctionAction(e.getSource() == conjunctionGroup, this, viewGroups);
            disjunctionAction(e.getSource() == disjunctionGroup, this, viewGroups);
            differenceAction(e.getSource() == differenceGroup, this, viewGroups);
            exclusiveDisjunctionAction(e.getSource() == exclusiveDisjunctionGroup, this, viewGroups);

            viewGroups.refreshView();
        } catch (AnimalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class ViewGroups extends JScrollPane {

    private final ArrayList<GroupOfAnimals> groups = new ArrayList<>();
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ViewGroups(int width, int height) {
        String[] columnNames = {"Nazwa", "Typ", "Liczba zwierzat"};
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createTitledBorder("Lista grup:"));

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
        for (GroupOfAnimals a : groups) {
            tableModel.addRow(a.toString().split("\\W+"));
        }
    }

    public void add(GroupOfAnimals group) {
        if (group != null)
            groups.add(group);
    }

    public void remove(GroupOfAnimals group) {
        groups.remove(group);
    }

    public ArrayList<GroupOfAnimals> getGroups() {
        return groups;
    }

    int getSelectedIndex() {
        int index = table.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Zadne zwierze nie jest zaznaczone.", "Blad", JOptionPane.ERROR_MESSAGE);
        }
        return index;
    }

    Iterator<GroupOfAnimals> iterator() {
        return groups.iterator();
    }
}

class GroupMenuBar extends JMenuBar implements ActionListener {
    JMenu fileMenu = new JMenu("Plik");
    JMenuItem readMenuItem = new JMenuItem("Wczytaj");
    JMenuItem writeMenuItem = new JMenuItem("Zapisz");
    JMenuItem writeAllMenuItem = new JMenuItem("Zapisz wszystko");
    JMenuItem exitMenuItem = new JMenuItem("Wyjdz");

    JMenu animalMenu = new JMenu("Zwierze");
    JMenuItem addMenuItem = new JMenuItem("Dodaj");
    JMenuItem editMenuItem = new JMenuItem("Edytuj");
    JMenuItem deleteMenuItem = new JMenuItem("Usun");

    JMenu operationMenu = new JMenu("Operacje");
    JMenuItem conjunctionItem = new JMenuItem("A * B");
    JMenuItem disjunctionItem = new JMenuItem("A + B");
    JMenuItem differenceItem = new JMenuItem("A / B");
    JMenuItem exclusiveDisjunctionItem = new JMenuItem("A - B");

    JMenu helpMenu = new JMenu("Pomoc");
    JMenuItem aboutMenuItem = new JMenuItem("O programie");


    Window parent;
    ViewGroups viewTable;

    public GroupMenuBar(Window parent, ViewGroups viewTable) {
        this.parent = parent;
        this.viewTable = viewTable;

        fileMenu.add(readMenuItem);
        fileMenu.add(writeMenuItem);
        fileMenu.add(writeAllMenuItem);
        fileMenu.add(exitMenuItem);

        animalMenu.add(addMenuItem);
        animalMenu.add(editMenuItem);
        animalMenu.add(deleteMenuItem);

        operationMenu.add(conjunctionItem);
        operationMenu.add(disjunctionItem);
        operationMenu.add(differenceItem);
        operationMenu.add(exclusiveDisjunctionItem);

        helpMenu.add(aboutMenuItem);

        readMenuItem.addActionListener(this);
        writeMenuItem.addActionListener(this);
        writeAllMenuItem.addActionListener(this);

        addMenuItem.addActionListener(this);
        editMenuItem.addActionListener(this);
        deleteMenuItem.addActionListener(this);
        aboutMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        conjunctionItem.addActionListener(this);
        disjunctionItem.addActionListener(this);
        differenceItem.addActionListener(this);
        exclusiveDisjunctionItem.addActionListener(this);

        add(fileMenu);
        add(animalMenu);
        add(operationMenu);
        add(helpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            GroupManagerApp.addAction(e.getSource() == addMenuItem, parent, viewTable);
            GroupManagerApp.deleteAction(e.getSource() == deleteMenuItem, viewTable);
            GroupManagerApp.editAction(e.getSource() == editMenuItem, parent, viewTable);
            GroupManagerApp.readAction(e.getSource() == readMenuItem, parent, viewTable);
            GroupManagerApp.saveAction(e.getSource() == writeMenuItem, parent, viewTable);
            GroupManagerApp.saveAllAction(e.getSource() == writeAllMenuItem, parent, viewTable);
            GroupManagerApp.infoAction(e.getSource() == aboutMenuItem, parent);
            GroupManagerApp.exitAction(e.getSource() == exitMenuItem, parent);
            GroupManagerApp.conjunctionAction(e.getSource() == conjunctionItem, parent, viewTable);
            GroupManagerApp.disjunctionAction(e.getSource() == disjunctionItem, parent, viewTable);
            GroupManagerApp.differenceAction(e.getSource() == differenceItem, parent, viewTable);
            GroupManagerApp.exclusiveDisjunctionAction(e.getSource() == exclusiveDisjunctionItem, parent, viewTable);

            viewTable.refreshView();
        } catch (AnimalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Blad", JOptionPane.ERROR_MESSAGE);
        }
    }
}