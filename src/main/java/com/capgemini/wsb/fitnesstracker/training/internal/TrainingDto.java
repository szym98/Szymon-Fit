package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

record TrainingDto(@Nullable Long Id, User user,
               @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date startTime,
               @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date endTime,
               ActivityType activityType,
               double distance,
               double averageSpeed) {
}

record TrainingDtoNoUser(@Nullable Long Id,
                   @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date startTime,
                   @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date endTime,
                   ActivityType activityType,
                   double distance,
                   double averageSpeed) {
}
