public interface IBaza2 {
    boolean czyUzytkownikIstnieje(String login);
    void zapiszDoPliku(String login, String haslo, String budynek);
}
