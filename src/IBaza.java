public interface IBaza {
    boolean usunUzytkownikaZBazy(String login, String haslo);
    boolean czyUzytkownikIstnieje(String login, String haslo);
}
