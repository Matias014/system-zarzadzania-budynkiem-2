import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

public class Budynek extends JFrame implements IBaza3 {
    private JPanel JPanel1;
    private JPanel JPanel2;
    private JPanel JPanel3;
    private JPanel JPanel4;
    private JLabel BudynekLabel;
    private JButton zapiszButton;
    private JButton wyjdźButton;
    private JLabel budynekOtwartyLabel;
    private JLabel włączoneŚwiatłaLabel;
    private JLabel zgłośUsterkęLabel;
    private JLabel włączonaSiećElektrycznaLabel;
    private JLabel monitorujTemperatureLabel;
    private JButton otwórzButton;
    private JButton zamknijButton;
    private JButton włączButton;
    private JButton wyłączButton;
    private JButton włączButton1;
    private JButton wyłączButton1;
    private JButton zgłośButton;
    private JButton sprawdźButton;
    private JLabel uzytkownikLabel;
    private JLabel temperaturaLabel;
    private JLabel CzyOtwarty;
    private JLabel CzySwiatla;
    private JLabel CzySiecElektryczna;
    private JLabel iconLabel;
    private final String loginUzytkownika;
    private ImageIcon iconHome = new ImageIcon(getClass().getResource("home.png"));

    public Budynek(String login) {
        super("Budynek");
        this.setContentPane(this.JPanel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 500);
        this.setLocationRelativeTo(null);

        loginUzytkownika = login;
        uzytkownikLabel.setText(uzytkownikLabel.getText() + " " + loginUzytkownika);
        wczytajDaneUzytkownika(loginUzytkownika);
        iconLabel.setIcon(resize(iconHome, 60, 60));


        wyjdźButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zapiszDaneBudynkuDoPliku(loginUzytkownika);
                dispose();

                Logowanie logowanie = new Logowanie();
                logowanie.setVisible(true);
            }
        });

        //sprawdzenie temperatury wody i powietrza
        sprawdźButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CzyOtwarty.getText().equals("Nie")) {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Żeby sprawdzić temperaturę wody i powietrza, trzeba otworzyć budynek!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Random rand = new Random();

                    //Ustalam sobie, że temperatury mogą tylko
                    temperaturaLabel.setText("Woda: " + (rand.nextInt(5) + 20) + "[°C] Powietrze: " + (rand.nextInt(5) + 20) + "[°C]");
                }
            }
        });

        //otwarcie drzwi
        otwórzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CzyOtwarty.setText("Tak");
            }
        });

        //zamknięcie drzwi
        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CzyOtwarty.setText("Nie");
            }
        });

        //włączenie świateł w budynku
        włączButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CzyOtwarty.getText().equals("Nie")) {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Żeby włączyć światła, trzeba otworzyć budynek!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else if (CzySiecElektryczna.getText().equals("Nie")) {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Żeby włączyć światła, trzeba włączyć sieć elektryczną!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    CzySwiatla.setText("Tak");
                }
            }
        });

        //wyłączenie świateł w budynku
        wyłączButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CzyOtwarty.getText().equals("Nie")) {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Żeby wyłączyć światła, trzeba otworzyć budynek!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    CzySwiatla.setText("Nie");
                }
            }
        });

        //włączenie sieci elektrycznej
        włączButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CzyOtwarty.getText().equals("Nie")) {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Żeby włączyć sieć elektryczną, trzeba otworzyć budynek!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    CzySiecElektryczna.setText("Tak");
                }
            }
        });

        //wyłączenie sieci elektrycznej
        wyłączButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CzyOtwarty.getText().equals("Nie")) {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Żeby wyłączyć sieć elektryczną, trzeba otworzyć budynek!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    CzySiecElektryczna.setText("Nie");
                    CzySwiatla.setText("Nie");
                }
            }
        });

        //zgłoszenie usterki
        zgłośButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usterka = "";
                usterka = JOptionPane.showInputDialog(Budynek.this,
                        "Podaj usterkę", "Usterka",
                        JOptionPane.INFORMATION_MESSAGE);

                if (!usterka.isEmpty()) {
                    zapiszUsterkeDoPliku(usterka);
                } else {
                    JOptionPane.showMessageDialog(Budynek.this,
                            "Trzeba podać usterkę!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //zapisanie aktualnych danych do pliku .txt
        zapiszButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zapiszDaneBudynkuDoPliku(loginUzytkownika);
                JOptionPane.showMessageDialog(Budynek.this,
                        "Zapisano dane!", "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    //wczytuje dane budynku użytkownika do labelów po zalogowaniu się
    public void wczytajDaneUzytkownika(String login) {
        File plik = new File("baza_uzytkownikow.txt");
        if (plik.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(plik))) {
                String aktualnaLinia;
                while ((aktualnaLinia = reader.readLine()) != null) {
                    //zakładamy, że format linii to: login,haslo,budynek,czyOtwarty,czySwiatla,czySiecElektryczna,temperaturaWody,temperaturaPowietrza
                    String[] dane = aktualnaLinia.split(",");
                    if (dane[0].equals(login)) {
                        BudynekLabel.setText("Budynek: " + dane[2]);
                        CzyOtwarty.setText(dane[3]);
                        CzySwiatla.setText(dane[4]);
                        CzySiecElektryczna.setText(dane[5]);
                        temperaturaLabel.setText("Woda: " + dane[6] + "[°C] Powietrze: " + dane[7] + "[°C]");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(Budynek.this,
                    "Nie można znaleźć pliku z bazą użytkowników!", "Błąd",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //zapisuje usterki do pliku .txt
    public void zapiszUsterkeDoPliku(String wiadomosc) {
        try (FileWriter fw = new FileWriter("zgloszenia_usterek.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(loginUzytkownika + "," + wiadomosc);
            JOptionPane.showMessageDialog(Budynek.this,
                    "Zgłoszenie usterki przebiegło pomyślnie!", "Sukces",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //zapisuje dane budynku do pliku .txt
    public void zapiszDaneBudynkuDoPliku(String login) {
        File plik = new File("baza_uzytkownikow.txt");
        File tempFile = new File(plik.getAbsolutePath() + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(plik));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String aktualnaLinia;
            while ((aktualnaLinia = reader.readLine()) != null) {
                String[] dane = aktualnaLinia.split(",");
                if (dane[0].equals(login)) {
                    dane[3] = CzyOtwarty.getText();
                    dane[4] = CzySwiatla.getText();
                    dane[5] = CzySiecElektryczna.getText();
                    dane[6] = temperaturaLabel.getText().split(" ")[1].replace("[°C]", "");
                    dane[7] = temperaturaLabel.getText().split(" ")[3].replace("[°C]", "");
                    writer.write(String.join(",", dane) + System.lineSeparator());
                } else {
                    writer.write(aktualnaLinia + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!plik.delete()) {
            System.out.println("Nie można usunąć starego pliku bazy użytkowników.");
            return;
        }
        if (!tempFile.renameTo(plik)) {
            System.out.println("Nie można zmienić nazwy pliku tymczasowego na plik bazy użytkowników.");
        }
    }

    private static ImageIcon resize(ImageIcon src, int destWidth, int destHeight) {
        return new ImageIcon(src.getImage().getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH));
    }
}
