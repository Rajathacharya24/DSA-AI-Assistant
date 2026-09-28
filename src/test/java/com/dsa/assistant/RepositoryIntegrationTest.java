package com.dsa.assistant;

import com.dsa.assistant.model.*;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private AttemptRepository attemptRepository;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        attemptRepository.deleteAll();
        progressRepository.deleteAll();
        problemRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        
        attemptRepository.flush();
        progressRepository.flush();
        problemRepository.flush();
        topicRepository.flush();
        userRepository.flush();

        Topic topic = topicRepository.save(new Topic("Arrays"));
        topicRepository.save(new Topic("Strings"));
        topicRepository.save(new Topic("Trees"));
        topicRepository.flush();
            
            for (int i=0; i<5; i++) {
                Problem p = new Problem();
                p.setTitle("P" + i);
                p.setDescription("D" + i);
                p.setDifficulty(Difficulty.EASY);
                p.setTopic(topic);
                problemRepository.save(p);
            }
            
            User u = new User();
            u.setName("Test");
            u.setEmail("test@test.com");
            u.setPasswordHash("pass");
            userRepository.save(u);
            
            Progress prog = new Progress();
            prog.setUser(u);
            prog.setProblem(problemRepository.findAll().get(0));
            prog.setStatus(ProgressStatus.SOLVED);
            progressRepository.save(prog);
            
            Attempt att = new Attempt();
            att.setUser(u);
            att.setProblem(prog.getProblem());
            att.setSubmittedCode("def foo(): pass");
            att.setResult(com.dsa.assistant.model.enums.AttemptResult.ACCEPTED);
            attemptRepository.save(att);
    }

    @Test
    void testInitialDataLoaded() {
        assertEquals(1, userRepository.count());
        assertTrue(topicRepository.count() >= 3);
        assertEquals(5, problemRepository.count());
    }

    @Test
    void testFindProblemsByDifficulty() {
        List<Problem> easyProblems = problemRepository.findByDifficulty(Difficulty.EASY);
        assertEquals(5, easyProblems.size());
    }

    @Test
    void testUserProgressAndAttempts() {
        User user = userRepository.findAll().get(0);
        List<Progress> progressList = progressRepository.findByUserId(user.getId());
        assertFalse(progressList.isEmpty());

        Progress progress = progressList.get(0);
        assertEquals(ProgressStatus.SOLVED, progress.getStatus());

        List<Attempt> attempts = attemptRepository.findByUserIdAndProblemId(user.getId(), progress.getProblem().getId());
        assertFalse(attempts.isEmpty());
    }
}
