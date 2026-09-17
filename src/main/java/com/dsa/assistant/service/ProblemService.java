package com.dsa.assistant.service;

import com.dsa.assistant.dto.CreateProblemDTO;
import com.dsa.assistant.dto.ProblemDTO;
import com.dsa.assistant.exception.ProblemNotFoundException;
import com.dsa.assistant.exception.TopicNotFoundException;
import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;

    public ProblemService(ProblemRepository problemRepository, TopicRepository topicRepository) {
        this.problemRepository = problemRepository;
        this.topicRepository = topicRepository;
    }

    public List<ProblemDTO> getAllProblems() {
        return problemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

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
