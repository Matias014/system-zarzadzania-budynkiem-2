import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Rejestracja extends JFrame implements IBaza2 {
    private JPanel JPanel1;
    private JPanel JPanel2;
    private JPanel JPanel3;
    private JLabel RejestracjaLabel;
    private JPanel JPanel4;
    private JTextField LoginField;
    private JPasswordField PasswordField;
    private JLabel LoginLabel;
    private JLabel PasswordLabel;
    private JButton RejestracjaButton;
    private JButton WyjdźButton;
    private JTextField BudynekField;
    private JLabel BudynekLabel;
    private JLabel iconLabel;
    private JComboBox RodzajeBudynkowCB;
    private ImageIcon iconDocument = new ImageIcon(getClass().getResource("document.png"));

    public Rejestracja() {
        super("System zarządzania budynkiem - Rejestracja");
        this.setContentPane(this.JPanel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        iconLabel.setIcon(resize(iconDocument, 60, 60));

        WyjdźButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                Logowanie logowanie = new Logowanie();
                logowanie.setVisible(true);
            }
        });

        RejestracjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = LoginField.getText();
                String haslo = new String(PasswordField.getPassword());
                String budynek = (String) RodzajeBudynkowCB.getItemAt(RodzajeBudynkowCB.getSelectedIndex());

                if (!login.isEmpty() && !haslo.isEmpty() && !budynek.isEmpty()) {
                    if (czyUzytkownikIstnieje(login)) {
                        JOptionPane.showMessageDialog(Rejestracja.this,
                                "Użytkownik o tym loginie już istnieje!", "Błąd",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        zapiszDoPliku(login, haslo, budynek);
                    }
                } else {
                    JOptionPane.showMessageDialog(Rejestracja.this,
                            "Wszystkie pola muszą być wypełnione!", "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public boolean czyUzytkownikIstnieje(String login) {
        File plik = new File("baza_uzytkownikow.txt");
        if (plik.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(plik))) {
                String aktualnaLinia;
                while ((aktualnaLinia = reader.readLine()) != null) {
                    if (aktualnaLinia.split(",")[0].equals(login)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void zapiszDoPliku(String login, String haslo, String budynek) {
        try (FileWriter fw = new FileWriter("baza_uzytkownikow.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(login + "," + haslo + "," + budynek + ",Nie,Nie,Nie,20,20");
            JOptionPane.showMessageDialog(Rejestracja.this,
                    "Rejestracja przebiegła pomyślnie!", "Sukces",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ImageIcon resize(ImageIcon src, int destWidth, int destHeight) {
        return new ImageIcon(src.getImage().getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH));
    }
}
