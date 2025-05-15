// Własny wyjątek, który musi być przechwycony (extends Exception, nie RuntimeException)
public class CountryNotFoundException extends Exception {

    // Konstruktor przyjmujący nazwę nieznalezionego państwa
    public CountryNotFoundException(String countryName) {
        super(countryName); // Wpisujemy nazwę jako wiadomość wyjątku
    }

    // getMessage() będzie zwracać nazwę kraju, bo to przekazaliśmy do konstruktora
}