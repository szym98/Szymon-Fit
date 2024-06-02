package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService, TrainingProvider {

    private final TrainingRepository trainingRepository;

    @Override
    public Training createTraining(final Training training) {
        log.info("Creating Training {}", training);
        if (training.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> getTraining(final Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    @Override
    public List<Training> getTrainingsByUserId(final Long trainingId) {
        return trainingRepository.findTrainingsByUserId(trainingId);
    }

    @Override
    public List<Training> getTrainingsAfterDate(Date date) {
        return trainingRepository.findTrainingsAfterDate(date);
    }

    @Override
    public List<Training> getTrainingsByActivities(ActivityType activityType) {
        return trainingRepository.findTrainingsByActivities(activityType);
    }

    @Override
    public void deleteTraining(Long trainingId){
        log.info("log.info -> userService.deleteUserById " + trainingId);
        trainingRepository.deleteById(trainingId);

    };

    @Override
    public Training updateTraining(Long  trainingId, Training trainingUpdates) {
        trainingUpdates.setId(trainingId);
        return trainingRepository.save(trainingUpdates);
    };

    @Override
    public Training updateTraining(Long  trainingId, User user, Training trainingUpdates) {
        trainingUpdates.setId(trainingId);
        trainingUpdates.setUser(user);
        return trainingRepository.save(trainingUpdates);
    };

    @Override
    public List<Training> getAllTrainings() { return trainingRepository.findAll(); }

    @Override
    public List<Training> getAllTrainingsForUserId(Long userId) {
        return trainingRepository.findTrainingsByUserId(userId);
    }

}
