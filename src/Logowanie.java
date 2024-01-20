import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Logowanie extends JFrame implements IBaza {
    private JPanel JPanel1;
    private JPanel JPanel2;
    private JPanel JPanel3;
    private JPanel JPanel4;
    private JTextField LoginField;
    private JPasswordField PasswordField;
    private JLabel LoginLabel;
    private JLabel LogowanieLabel;
    private JLabel PasswordLabel;
    private JButton LogowanieButton;
    private JButton WyjdźButton;
    private JButton rejestracjaButton;
    private JButton usuńUżytkownikaButton;
    private JLabel iconLabel;
    private ImageIcon iconUser = new ImageIcon(getClass().getResource("user.png"));

    public Logowanie() {
        super("System zarządzania budynkiem - Logowanie");
        this.setContentPane(this.JPanel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        iconLabel.setIcon(resize(iconUser, 60, 60));

        LogowanieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = LoginField.getText();
                String haslo = new String(PasswordField.getPassword());

                if (!login.isEmpty() && !haslo.isEmpty()) {
                    if (czyUzytkownikIstnieje(login, haslo)) {
                        dispose();

                        Budynek budynek = new Budynek(login);
                        budynek.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(Logowanie.this,
                                "Użytkownik o takim loginie nie istnieje albo podałeś zły login lub hasło!", "Błąd",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Logowanie.this,
                            "Wszystkie pola muszą być wypełnione!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                Rejestracja rejestracja = new Rejestracja();
                rejestracja.setVisible(true);
            }
        });

        usuńUżytkownikaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = LoginField.getText();
                String haslo = new String(PasswordField.getPassword());

                if (!login.isEmpty() && !haslo.isEmpty()) {
                    if (czyUzytkownikIstnieje(login, haslo)) {
                        if (usunUzytkownikaZBazy(login, haslo)) {
                            JOptionPane.showMessageDialog(Logowanie.this,
                                    "Użytkownik został pomyślnie usunięty z bazy danych.", "Sukces",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(Logowanie.this,
                                    "Wystąpił błąd podczas usuwania użytkownika.", "Błąd",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Logowanie.this,
                                "Użytkownik o takim loginie nie istnieje albo podałeś zły login lub hasło!", "Błąd",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Logowanie.this,
                            "Wszystkie pola muszą być wypełnione!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        WyjdźButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public boolean czyUzytkownikIstnieje(String login, String haslo) {
        File plik = new File("baza_uzytkownikow.txt");
        if (plik.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(plik))) {
                String aktualnaLinia;
                while ((aktualnaLinia = reader.readLine()) != null) {
                    if (aktualnaLinia.split(",")[0].equals(login) && aktualnaLinia.split(",")[1].equals(haslo)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean usunUzytkownikaZBazy(String login, String haslo) {
        File plik = new File("baza_uzytkownikow.txt");
        if (plik.exists()) {
            try {
                File tempFile = new File("temp.txt");
                BufferedReader reader = new BufferedReader(new FileReader(plik));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String aktualnaLinia;
                while ((aktualnaLinia = reader.readLine()) != null) {
                    if (aktualnaLinia.split(",")[0].equals(login) && aktualnaLinia.split(",")[1].equals(haslo)) {
                        //Pomijamy tę linię, aby ją usunąć z bazy danych
                        continue;
                    }
                    writer.write(aktualnaLinia + System.getProperty("line.separator"));
                }

                reader.close();
                writer.close();

                //Usuwamy pierwotny plik i zmieniamy nazwę tymczasowego pliku na oryginalną nazwę
                if (plik.delete() && tempFile.renameTo(plik)) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static ImageIcon resize(ImageIcon src, int destWidth, int destHeight) {
        return new ImageIcon(src.getImage().getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH));
    }
}