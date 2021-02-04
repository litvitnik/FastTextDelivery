package ru.litvitnik;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimaryController {

    ArrayList<String> al = new ArrayList<>();
    int delayMillis = 150;
    String text = "";

    @FXML
    public Button primaryButton;
    @FXML
    Label primaryText;
    @FXML
    Label lblInfo;
    @FXML
    private void onClickChooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.getExtensionFilters().addAll(filter);
        fileChooser.setTitle("Выбор файла");
        File fileObject = fileChooser.showOpenDialog(new Stage());
        if (fileObject != null) {
            String file = fileObject.getPath();
            System.out.println(file);
            al = filterArrayList(loadFromFile(file));
            lblInfo.setText("Длина текста: " + al.size() + "\nСлов в минуту: " + 60_000/delayMillis + "\nОжидаемое время чтения: " + al.size()*delayMillis/1000 + "сек");
        }
    }
    @FXML
    private void switchToSecondary() {
        AtomicInteger i = new AtomicInteger();
        System.out.println(al);
        System.out.println("Количество слов в тексте: " + al.size());
        System.out.println("Скорость чтения: " + 60_000/delayMillis + " слов в минуту");
        System.out.println("Ожидаемое время чтения: " + al.size()*delayMillis/1000 + " сек");
            //Duration.seconds(1)
            PauseTransition pause = new PauseTransition(Duration.millis(delayMillis));
            pause.setOnFinished(event -> {
                    if(i.get() <al.size())
                    {
                        System.out.print("\"" + al.get(i.get()) + "\"\n");
                        primaryText.setText(al.get(i.getAndIncrement()));
                        pause.play();
                    }
            }
            );
            pause.play();
    }
    public ArrayList<String> filterArrayList(String source) {
        ArrayList<String> temp = new ArrayList<String>(Arrays.asList(source.split(" ")));
        for(int i = 0; i < temp.size(); i++) {
                if(temp.get(i).contains("\n")) {
                    StringBuilder sb = new StringBuilder();
                    for(char c : temp.get(i).toCharArray()) {
                        if(c!='\n') sb.append(c);
                    }
                    temp.set(i, sb.toString());
                }
        }
        return temp;
    }
    public String loadFromFile(String source) {
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(Paths.get(source))) {
            while(scanner.hasNext()) sb.append(scanner.nextLine());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return filterStringFromFile(sb.toString());
    }
    public String filterStringFromFile(String source){
        //StringBuilder sb = new StringBuilder();
        //char[] sourceAsCharArray = source.toCharArray();
        /*
         * Решение через Регулярные Выражения видится мне максимально неэффективным,
         * т.к. все это можно было бы сделать за один проход
         * А еще я так понимаю мне придется проходить его стандартным методом,
         * чтобы поставить заглавные буквы в начало предложения
         * Текущий недостаток - не выставляет заглавные.
         */
        source = source.replaceAll("\\.(?=\\S[^.])", ". ");
        source = source.replaceAll(" \\. ", ". ");
        source = source.replaceAll(",(?=\\S)", ", ");
        source = source.replaceAll(" , ", ", ");
        source = source.replaceAll("-(?=\\S)", "- ");
        source = source.replaceAll("(?<=\\S)-", " -");
        return source;
    }
}
