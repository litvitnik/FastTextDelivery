module ru.litvitnik
{
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.litvitnik to javafx.fxml;
    exports ru.litvitnik;
}