package com.dsa.assistant.service;

import com.dsa.assistant.dto.CreateProblemDTO;
import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.exception.ProblemNotFoundException;
import com.dsa.assistant.exception.TopicNotFoundException;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Progress;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.User;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.ProgressRepository;
import com.dsa.assistant.repository.TopicRepository;
import com.dsa.assistant.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final ProgressService progressService;

    public ProblemService(ProblemRepository problemRepository,
                          TopicRepository topicRepository,
                          ProgressRepository progressRepository,
                          UserRepository userRepository,
                          ProgressService progressService) {
        this.problemRepository = problemRepository;
        this.topicRepository = topicRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.progressService = progressService;
    }

    @Cacheable(value = "problems", key = "'all'")
    public List<ProblemDTO> getAllProblems() {
        return problemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "problems", key = "#id")
    public ProblemDTO getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ProblemNotFoundException("Problem not found with id: " + id));
        return mapToDTO(problem);
    }

    public List<ProblemDTO> getProblemsByTopic(String topicName) {
        Topic topic = topicRepository.findByName(topicName.toUpperCase())
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with name: " + topicName));
        return problemRepository.findByTopicId(topic.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProblemDTO> getProblemsByDifficulty(String difficultyStr) {
        try {
            Difficulty difficulty = Difficulty.valueOf(difficultyStr.toUpperCase());
            return problemRepository.findByDifficulty(difficulty).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid difficulty level: " + difficultyStr);
        }
    }

    public List<ProblemDTO> searchProblems(String query) {
        if (query == null || query.isBlank()) {
            return getAllProblems();
        }

        String normalized = query.trim().toLowerCase();
        return problemRepository.findAll().stream()
                .filter(problem -> matchesQuery(problem, normalized))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private boolean matchesQuery(Problem problem, String query) {
        String title = problem.getTitle() == null ? "" : problem.getTitle().toLowerCase();
        String description = problem.getDescription() == null ? "" : problem.getDescription().toLowerCase();
        String topicName = problem.getTopic() == null || problem.getTopic().getName() == null
                ? ""
                : problem.getTopic().getName().toLowerCase();
        return title.contains(query) || description.contains(query) || topicName.contains(query);
    }

    public ProblemDTO getProblem(String topic, String difficultyStr) {
        List<Problem> problems;
        Difficulty difficulty = null;
        if (difficultyStr != null && !difficultyStr.isEmpty()) {
            try {
                difficulty = Difficulty.valueOf(difficultyStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }
        
        if (topic != null && !topic.isEmpty() && difficulty != null) {
            problems = problemRepository.findByTopicNameIgnoreCaseAndDifficulty(topic, difficulty);
        } else if (topic != null && !topic.isEmpty()) {
            Topic t = topicRepository.findByName(topic.toUpperCase()).orElse(null);
            if (t != null) {
                problems = problemRepository.findByTopicId(t.getId());
            } else {
                problems = List.of();
            }
        } else if (difficulty != null) {
            problems = problemRepository.findByDifficulty(difficulty);
        } else {
            problems = problemRepository.findAll();
        }

        if (problems.isEmpty()) {
            throw new ProblemNotFoundException("No problem found matching criteria");
        }
        
        // Return first one for now, could be random
        return mapToDTO(problems.get(0));
    }


    public String getHint(Long userId, Long problemId, int hintLevel) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem not found with id: " + problemId));

        progressService.incrementHintsUsed(userId, problemId);

        return "Problem Title: " + problem.getTitle() + "\n" +
               "Explanation/Solution context: " + problem.getExplanation() + "\n" +
               "Solution code: " + problem.getSolution() + "\n\n" +
               "The user requested Hint Level " + hintLevel + ".\n" +
               "Please generate a suitable hint based on the level (1=conceptual direction, 2=specific approach, 3=near-solution). " +
               "Do NOT give away the full solution.";
    }

    @Cacheable(value = "daily-challenges", key = "#userId + ':' + T(java.time.LocalDate).now().toString()")
    public ProblemDTO getDailyChallenge(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Problem> allProblems = problemRepository.findAll();
        List<Long> solvedProblemIds = progressRepository.findByUserId(userId).stream()
                .filter(progress -> progress.getStatus() == ProgressStatus.SOLVED)
                .map(progress -> progress.getProblem().getId())
                .collect(Collectors.toList());

        List<Problem> availableProblems = allProblems.stream()
                .filter(problem -> !solvedProblemIds.contains(problem.getId()))
                .collect(Collectors.toList());

        if (availableProblems.isEmpty()) {
            return mapToDTO(allProblems.stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No problems available for the daily challenge.")));
        }

        int index = Math.floorMod((int) (userId + LocalDate.now().toEpochDay()), availableProblems.size());
        return mapToDTO(availableProblems.get(index));
    }

    @CacheEvict(value = {"problems", "daily-challenges"}, allEntries = true)
    public ProblemDTO createProblem(CreateProblemDTO createProblemDTO) {
        String topicName = createProblemDTO.getTopic().toUpperCase();
        Topic topic = topicRepository.findByName(topicName)
                .orElseGet(() -> topicRepository.save(new Topic(topicName)));

        Problem problem = new Problem();
        problem.setTitle(createProblemDTO.getTitle());
        problem.setDescription(createProblemDTO.getDescription());
        problem.setDifficulty(createProblemDTO.getDifficulty());
        problem.setTopic(topic);
        problem.setExampleInput(createProblemDTO.getExampleInput());
        problem.setExampleOutput(createProblemDTO.getExampleOutput());
        problem.setExplanation(createProblemDTO.getExplanation());
        problem.setSolution(createProblemDTO.getSolution());

        Problem savedProblem = problemRepository.save(problem);
        return mapToDTO(savedProblem);
    }

    private ProblemDTO mapToDTO(Problem problem) {
        ProblemDTO dto = new ProblemDTO();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setTopic(problem.getTopic() != null ? problem.getTopic().getName() : null);
        dto.setDifficulty(problem.getDifficulty());
        dto.setDescription(problem.getDescription());
        return dto;
    }
}
