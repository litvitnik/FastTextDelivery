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
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimaryController {

    @FXML
    public Slider sliderSpeed;
    @FXML
    public Button primaryButton;
    @FXML
    Label primaryText;
    @FXML
    Label lblInfo;


    ArrayList<String> allWordsList = new ArrayList<>();
    int delayMillis = 150;
    String text = "";
    int wordsPerMinute = 400;


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
            allWordsList = filterArrayList(loadFromFile(file));
            lblInfo.setText("Длина текста: " + allWordsList.size() + "\nСлов в минуту: " + 60_000/delayMillis + "\nОжидаемое время чтения: " + allWordsList.size()*delayMillis/1000 + "сек");
        }
    }
    @FXML
    private void switchToSecondary() {
        AtomicInteger i = new AtomicInteger();
        System.out.println(allWordsList);
        System.out.println("Количество слов в тексте: " + allWordsList.size());
        System.out.println("Скорость чтения: " + 60_000/delayMillis + " слов в минуту");
        System.out.println("Ожидаемое время чтения: " + allWordsList.size()*delayMillis/1000 + " сек");
            PauseTransition pause = new PauseTransition(Duration.millis(delayMillis));
            pause.setOnFinished(event -> {
                    if(i.get() < allWordsList.size())
                    {
                        System.out.print("\"" + allWordsList.get(i.get()) + "\"\n");
                        primaryText.setText(allWordsList.get(i.getAndIncrement()));
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
        //Заглавные буквы после точек
        char[] sourceAsCharArray = source.toCharArray();
        for(int i = 2; i < sourceAsCharArray.length; i++){
            if(sourceAsCharArray[i-2]=='.') sourceAsCharArray[i] = Character.toUpperCase(sourceAsCharArray[i]);
        }
        source = String.valueOf(sourceAsCharArray);
        return source;
    }

    public void sliderFinished() {
        sliderSpeed.setValue(valueRoundToTen(sliderSpeed.getValue()));
        System.out.println(sliderSpeed.getValue());
        delayMillis = (int) (60_000 / valueRoundToTen(sliderSpeed.getValue()));
    }
    public double valueRoundToTen(double value){
        return Math.round(value/10) * 10;
    }
}

