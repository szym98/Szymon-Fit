package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.stereotype.Component;

@Component
class TrainingMapper {

    TrainingDto toDto(Training training) {
        return new TrainingDto(training.getId(),
                           training.getUser(),
                           training.getStartTime(),
                           training.getEndTime(),
                           training.getActivityType(),
                           training.getDistance(),
                           training.getAverageSpeed());

    }

    Training toEntity(TrainingDto trainingDto) {
        return new Training(
                        trainingDto.user(),
                        trainingDto.startTime(),
                        trainingDto.endTime(),
                        trainingDto.activityType(),
                        trainingDto.distance(),
                        trainingDto.averageSpeed());

    }

    TrainingDtoNoUser toDtoNoUser(Training training) {
        return new TrainingDtoNoUser(training.getId(),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed());

    }

    Training toEntityNoUser(TrainingDtoNoUser trainingDtoNoUser) {
        return new Training( null,
                trainingDtoNoUser.startTime(),
                trainingDtoNoUser.endTime(),
                trainingDtoNoUser.activityType(),
                trainingDtoNoUser.distance(),
                trainingDtoNoUser.averageSpeed());

    }



}
