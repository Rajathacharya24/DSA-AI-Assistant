package com.dsa.assistant.config;

import com.dsa.assistant.model.*;
import com.dsa.assistant.model.enums.AttemptResult;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final ProblemRepository problemRepository;
    private final ProgressRepository progressRepository;
    private final AttemptRepository attemptRepository;

    public DataInitializer(UserRepository userRepository,
                           TopicRepository topicRepository,
                           ProblemRepository problemRepository,
                           ProgressRepository progressRepository,
                           AttemptRepository attemptRepository) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.problemRepository = problemRepository;
        this.progressRepository = progressRepository;
        this.attemptRepository = attemptRepository;
    }

    @Override
    public void run(String... args) {
        if (problemRepository.count() > 0) {
            log.info("Database already contains problems. Skipping initialization.");
            return;
        }

        log.info("Initializing sample data for Phase 2...");

        // 1. Create Topics
        Topic arraysTopic = topicRepository.save(new Topic("Arrays"));
        Topic stringsTopic = topicRepository.save(new Topic("Strings"));
        Topic hashingTopic = topicRepository.save(new Topic("Hashing"));

        // 2. Create Sample Problems
        Problem p1 = new Problem(
                null,
                "Find Largest Element",
                "Given an array of integers, find and return the largest element.",
                Difficulty.EASY,
                "[1, 8, 7, 56, 90]",
                "90",
                "The largest element among [1, 8, 7, 56, 90] is 90.",
                "public int findLargest(int[] nums) {\n    int max = nums[0];\n    for (int num : nums) {\n        if (num > max) max = num;\n    }\n    return max;\n}",
                arraysTopic
        );

        Problem p2 = new Problem(
                null,
                "Find Smallest Element",
                "Given an array of integers, find and return the smallest element.",
                Difficulty.EASY,
                "[3, 4, 1, 9, 2]",
                "1",
                "The smallest element among [3, 4, 1, 9, 2] is 1.",
                "public int findSmallest(int[] nums) {\n    int min = nums[0];\n    for (int num : nums) {\n        if (num < min) min = num;\n    }\n    return min;\n}",
                arraysTopic
        );

        Problem p3 = new Problem(
                null,
                "Running Sum",
                "Given an array nums, return the running sum of nums as runningSum[i] = sum(nums[0]…nums[i]).",
                Difficulty.EASY,
                "[1, 2, 3, 4]",
                "[1, 3, 6, 10]",
                "Running sum is obtained as follows: [1, 1+2, 1+2+3, 1+2+3+4].",
                "public int[] runningSum(int[] nums) {\n    for (int i = 1; i < nums.length; i++) {\n        nums[i] += nums[i - 1];\n    }\n    return nums;\n}",
                arraysTopic
        );

        Problem p4 = new Problem(
                null,
                "Reverse String",
                "Write a function that reverses a string given as an array of characters.",
                Difficulty.EASY,
                "[\"h\",\"e\",\"l\",\"l\",\"o\"]",
                "[\"o\",\"l\",\"l\",\"e\",\"h\"]",
                "The array is reversed in-place.",
                "public void reverseString(char[] s) {\n    int left = 0, right = s.length - 1;\n    while (left < right) {\n        char temp = s[left];\n        s[left++] = s[right];\n        s[right--] = temp;\n    }\n}",
                stringsTopic
        );

        Problem p5 = new Problem(
                null,
                "Contains Duplicate",
                "Given an integer array nums, return true if any value appears at least twice in the array.",
                Difficulty.EASY,
                "[1, 2, 3, 1]",
                "true",
                "The element 1 occurs at index 0 and index 3.",
                "public boolean containsDuplicate(int[] nums) {\n    Set<Integer> set = new HashSet<>();\n    for (int num : nums) {\n        if (!set.add(num)) return true;\n    }\n    return false;\n}",
                hashingTopic
        );

        problemRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        // 3. Create Demo User
        User demoUser = new User(null, "Alex Learner", "alex@example.com", null);
        demoUser = userRepository.save(demoUser);

        // 4. Create Initial Progress & Sample Attempt
        // Problem 1: Solved today
        Progress progress1 = new Progress(null, demoUser, p1, ProgressStatus.SOLVED, 2, 1, LocalDateTime.now());
        progressRepository.save(progress1);

        Attempt attempt1_1 = new Attempt(null, demoUser, p1, "public int findLargest(int[] nums) { return 0; }", AttemptResult.WRONG_ANSWER, LocalDateTime.now().minusDays(2));
        attemptRepository.save(attempt1_1);
        
        Attempt attempt1_2 = new Attempt(null, demoUser, p1, "public int findLargest(int[] nums) { ... }", AttemptResult.ACCEPTED, LocalDateTime.now());
        attemptRepository.save(attempt1_2);

        // Problem 2: Solved yesterday (for streak)
        Progress progress2 = new Progress(null, demoUser, p2, ProgressStatus.SOLVED, 1, 0, LocalDateTime.now().minusDays(1));
        progressRepository.save(progress2);

        Attempt attempt2 = new Attempt(null, demoUser, p2, "public int findSmallest(int[] nums) { ... }", AttemptResult.ACCEPTED, LocalDateTime.now().minusDays(1));
        attemptRepository.save(attempt2);

        // Problem 3: Attempted today, hints used
        Progress progress3 = new Progress(null, demoUser, p3, ProgressStatus.IN_PROGRESS, 1, 2, null);
        progressRepository.save(progress3);

        Attempt attempt3 = new Attempt(null, demoUser, p3, "public int[] runningSum(int[] nums) { ... }", AttemptResult.WRONG_ANSWER, LocalDateTime.now());
        attemptRepository.save(attempt3);

        log.info("Sample data initialization completed successfully.");
    }
}
