package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserServiceImpl userService;

    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings()
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public List<TrainingDto> getTrainingById(@PathVariable final Long id) {
        return trainingService.getTraining(id)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/delete-by-id/{id}")
    public ResponseEntity<Void>  deleteTrainingById(@PathVariable final Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public List<TrainingDto> getTrainingByUserId(@PathVariable final Long id) {
        return trainingService.getTrainingsByUserId(id)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/after/{dateTrainingsAfterDate}")
    public List<TrainingDto> getTrainingAfterDate(@PathVariable final String dateTrainingsAfterDate) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = format.parse(dateTrainingsAfterDate);
        } catch (Exception e) {}

        if (date == null) {
            date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return trainingService.getTrainingsAfterDate(date)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/activities")
    public ActivityType[] getActivities() {
        return ActivityType.values();
    }

    @GetMapping("/activities/{activity}")
    public List<TrainingDto> getTrainingByActivities(@PathVariable final ActivityType activity) {

        log.info("log.info -> trainingService.findTrainingByActivities");

        return trainingService.getTrainingsByActivities(activity)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }


    // Windows curl -X POST  http://localhost:8080/v1/trainings -H "Content-Type: application/json" -d "{\"id\": \"0\", \"user\": {\"id\": \"1\", \"firstName\": \"\", \"lastName\": \"\", \"birthdate\": \"\", \"email\":  \"\"}, \"startTime\": \"12-00-00 : 2024-05-05\", \"endTime\": \"12-45-00 : 2024-05-05\", \"activityType\": \"CYCLING\", \"distance\": \"12.3\", \"averageSpeed\": \"23.45\"}"
    @PostMapping
    public ResponseEntity<Training> addTraining(@RequestBody TrainingDto trainingDto) {

        Training newTraining =  trainingService.createTraining (new Training(
                trainingDto.user(),
                trainingDto.startTime(),
                trainingDto.endTime(),
                trainingDto.activityType(),
                trainingDto.distance(),
                trainingDto.averageSpeed()));

        URI location = URI.create("/v1/trainings" + newTraining.getId());

        return ResponseEntity.created(location).body(newTraining);
    }

    // nieprawidlowa data w user i aktualizacja pomyslana
    // Windows curl -X PUT  http://localhost:8080/v1/trainings/11 -H "Content-Type: application/json" -d "{\"id\": \"11\", \"user\": {\"id\": \"11\", \"firstName\": \"\", \"\": \"\", \"\": \"\", \"\":  \"\"}, \"startTime\": \"13-00-00 : 2024-05-06\", \"endTime\": \"13-45-00 : 2024-05-06\", \"activityType\": \"CYCLING\", \"distance\": \"23.4\", \"averageSpeed\": \"34.5\"}"
    // gdy data jest prawidlowa, atualizacja niepomyslna blad 400

    // Windows curl -X PUT  http://localhost:8080/v1/trainings/11 -H "Content-Type: application/json" -d "{\"id\": \"11\", \"startTime\": \"13-00-00 : 2024-05-06\", \"endTime\": \"13-45-00 : 2024-05-06\", \"activityType\": \"CYCLING\", \"distance\": \"23.4\", \"averageSpeed\": \"34.5\"}"

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(@PathVariable final Long id, @RequestBody TrainingDtoNoUser trainingDtoNoUser) {

        log.info("log.info -> trainingService.updateTraining = {}", id);

        Optional<User> user = userService.getUser(id);

        if (trainingDtoNoUser.Id() == null){
            return new ResponseEntity<>("Id cannot be null", HttpStatus.BAD_REQUEST);
        }

        Training updateTraining = trainingService.updateTraining(trainingDtoNoUser.Id(),
                                                                    (user.orElse(null)),
                                                                    trainingMapper.toEntityNoUser(trainingDtoNoUser));
        return ResponseEntity.ok(trainingMapper.toDtoNoUser(updateTraining));
    }

    // Windows curl -X DELETE http://localhost:8080/v1/trainings/11
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delTraining(@PathVariable final Long id) {
        trainingService.deleteTraining(id);
        return ResponseEntity.noContent().build();
    }
}