package ru.otus.spring.course.service;

import com.opencsv.CSVReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.otus.spring.course.data.Line;
import ru.otus.spring.course.data.User;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class AppServiceImpl implements AppService{
    private final Map<User, Integer> userPoints = new HashMap<>();
    private final LocaleTextService localeTextService;
    private final List<Line> lines = new ArrayList<>();
    private final Boolean isInfinite;
    private final String csvPathFile;

    @PostConstruct
    void initLines() throws IOException {
        Reader reader = new InputStreamReader(getClass().getResourceAsStream(csvPathFile), StandardCharsets.UTF_8);
        CSVReader csvReader = new CSVReader(reader);
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            String question = nextRecord[0];
            int answerIndex = Integer.valueOf(nextRecord[1]);
            List<String> options = new ArrayList<>(Arrays.asList(nextRecord).subList(2, nextRecord.length));
            lines.add(Line.builder()
                    .answer(answerIndex)
                    .options(options)
                    .question(question).build());
        }
        reader.close();
        csvReader.close();
    }

    @Override
    public void addShutdownHook() {
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!userPoints.isEmpty())
                localeTextService.printTotalLine();
            userPoints.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(userIntegerEntry ->
                            localeTextService.printUserScore(userIntegerEntry.getKey(), userIntegerEntry.getValue()));
            localeTextService.printProgramCompletedLine();
            mainThread.interrupt();
        }));
    }

    @Override
    public void game() {
        addShutdownHook();
        localeTextService.printHelloLine();
        localeTextService.printWelcomeLine();
        if(isInfinite) {
            while (true) {
                roundWrapper();
            }
        }
        else
            roundWrapper();
    }

    private Optional<User> createUser(String... inputData) {
        if (inputData.length != 2)
            return Optional.empty();
        User user = new User(inputData[0], inputData[1]);
        if (userPoints.containsKey(user))
            return Optional.empty();
        else
            return Optional.of(user);
    }

    private void roundWrapper() {
        String nameSurname = localeTextService.requestFullName();
        if (!nameSurname.isEmpty()) {
            Optional<User> optionalUser = createUser(nameSurname.split(" "));
            if (optionalUser.isPresent()) {
                int points = round(optionalUser.get());
                if (isInfinite)
                    localeTextService.printUserScore(optionalUser.get(), points);
            } else {
                localeTextService.printInvalidBunchFullName();
            }
            if (isInfinite)
                localeTextService.printWelcomeLine();
        }
    }

    private int round(User user) {
        int points = 0;
        for (Line line : lines) {
            int answer = localeTextService.printQuestionAndReadAnswer(line);
            if (answer == line.getAnswer())
                points++;
        }
        userPoints.put(user, points);
        return points;
    }
}
