package com.capgemini.wsb.fitnesstracker.notification.api;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EnableScheduling
@Service
@Slf4j
public class ReportToEmail {
    private static final String TITLE = "Last week's training reports";

    private  final JavaMailSender javaMailSender;
    private  final UserProvider userProvider;
    private  final TrainingProvider trainingProvider;

    @Scheduled(cron = "0 * * * * *")
    public void generateReport() {
        log.info("Starting generation of training reports");
        userProvider.findAllUsers().forEach(user -> {
            sendReport(user);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Generation of training reports finished");
    }

    private void sendReport(User user) {
        log.info("Sending email to {}", user.getEmail());
        javaMailSender.send(createEmail(user));
    }

    private SimpleMailMessage createEmail(User user) {
        List<Training> trainings = trainingProvider.getAllTrainingsForUserId(user.getId());
        List<Training> lastWeekTrainings = filterLastWeekTrainings(trainings);

        log.info("Creating email for {}", user.getEmail());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(TITLE);
        email.setTo(user.getEmail());
        email.setText(createEmailText(lastWeekTrainings, trainings));

        log.info("Email created");
        return email;
    }

    private String createEmailText(List<Training> lastWeekTrainings, List<Training> trainings) {
        StringBuilder builder = new StringBuilder(String.format("""
                ________________________________________________________________
                Last week, you had %s workouts,
                covering a total distance of %s units.
                So far, you’ve completed %s workouts.
                ________________________________________________________________
                Below is a detailed breakdown of your workouts from last week:
                """,
                lastWeekTrainings.size(),
                lastWeekTrainings.stream().mapToDouble(Training::getDistance).sum(),
                trainings.size()
        ));

        lastWeekTrainings.forEach(training -> builder.append(String.format("""
                training start: %s
                training end: %s
                activity type: %s
                distance: %s
                average speed: %s
                ________________________________________________________________
                """,
                training.getStartTime(),
                training.getEndTime() == null ? "---" : training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        )));

        return builder.toString();
    }

    private List<Training> filterLastWeekTrainings(List<Training> trainings) {
        Date lastWeek = returnBeginningOfLastWeek();
        Date yesterday = returnYesterday();
        return trainings.stream()
                .filter(training -> training.getStartTime().after(lastWeek) && training.getStartTime().before(yesterday))
                .collect(Collectors.toList());
    }

    private Date returnYesterday() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -1);
        return now.getTime();
    }

    private Date returnBeginningOfLastWeek() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -7);
        return now.getTime();
    }

}
