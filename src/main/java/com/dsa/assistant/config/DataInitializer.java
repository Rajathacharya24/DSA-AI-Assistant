package com.dsa.assistant.config;

import com.dsa.assistant.model.*;
import com.dsa.assistant.model.enums.AttemptResult;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.model.enums.ProgressStatus;
import com.dsa.assistant.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final ProblemRepository problemRepository;
    private final ProgressRepository progressRepository;
    private final AttemptRepository attemptRepository;

    @Override
    public void run(String... args) {
        if (problemRepository.count() > 0) {
            log.info("Database already contains problems. Skipping initialization.");
            return;
        }

        log.info("Initializing sample data for Phase 2...");

        // 1. Create Topics
        Topic arraysTopic = topicRepository.save(Topic.builder().name("Arrays").build());
        Topic stringsTopic = topicRepository.save(Topic.builder().name("Strings").build());
        Topic hashingTopic = topicRepository.save(Topic.builder().name("Hashing").build());

        // 2. Create Sample Problems
        Problem p1 = Problem.builder()
                .title("Find Largest Element")
                .description("Given an array of integers, find and return the largest element.")
                .difficulty(Difficulty.EASY)
                .exampleInput("[1, 8, 7, 56, 90]")
                .exampleOutput("90")
                .explanation("The largest element among [1, 8, 7, 56, 90] is 90.")
                .solution("public int findLargest(int[] nums) {\n    int max = nums[0];\n    for (int num : nums) {\n        if (num > max) max = num;\n    }\n    return max;\n}")
                .topic(arraysTopic)
                .build();

        Problem p2 = Problem.builder()
                .title("Find Smallest Element")
                .description("Given an array of integers, find and return the smallest element.")
                .difficulty(Difficulty.EASY)
                .exampleInput("[3, 4, 1, 9, 2]")
                .exampleOutput("1")
                .explanation("The smallest element among [3, 4, 1, 9, 2] is 1.")
                .solution("public int findSmallest(int[] nums) {\n    int min = nums[0];\n    for (int num : nums) {\n        if (num < min) min = num;\n    }\n    return min;\n}")
                .topic(arraysTopic)
                .build();

        Problem p3 = Problem.builder()
                .title("Running Sum")
                .description("Given an array nums, return the running sum of nums as runningSum[i] = sum(nums[0]…nums[i]).")
                .difficulty(Difficulty.EASY)
                .exampleInput("[1, 2, 3, 4]")
                .exampleOutput("[1, 3, 6, 10]")
                .explanation("Running sum is obtained as follows: [1, 1+2, 1+2+3, 1+2+3+4].")
                .solution("public int[] runningSum(int[] nums) {\n    for (int i = 1; i < nums.length; i++) {\n        nums[i] += nums[i - 1];\n    }\n    return nums;\n}")
                .topic(arraysTopic)
                .build();

        Problem p4 = Problem.builder()
                .title("Reverse String")
                .description("Write a function that reverses a string given as an array of characters.")
                .difficulty(Difficulty.EASY)
                .exampleInput("[\"h\",\"e\",\"l\",\"l\",\"o\"]")
                .exampleOutput("[\"o\",\"l\",\"l\",\"e\",\"h\"]")
                .explanation("The array is reversed in-place.")
                .solution("public void reverseString(char[] s) {\n    int left = 0, right = s.length - 1;\n    while (left < right) {\n        char temp = s[left];\n        s[left++] = s[right];\n        s[right--] = temp;\n    }\n}")
                .topic(stringsTopic)
                .build();

        Problem p5 = Problem.builder()
                .title("Contains Duplicate")
                .description("Given an integer array nums, return true if any value appears at least twice in the array.")
                .difficulty(Difficulty.EASY)
                .exampleInput("[1, 2, 3, 1]")
                .exampleOutput("true")
                .explanation("The element 1 occurs at index 0 and index 3.")
                .solution("public boolean containsDuplicate(int[] nums) {\n    Set<Integer> set = new HashSet<>();\n    for (int num : nums) {\n        if (!set.add(num)) return true;\n    }\n    return false;\n}")
                .topic(hashingTopic)
                .build();

        problemRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        // 3. Create Demo User
        User demoUser = userRepository.save(User.builder()
                .name("Alex Learner")
                .email("alex@example.com")
                .build());

        // 4. Create Initial Progress & Sample Attempt
        progressRepository.save(Progress.builder()
                .user(demoUser)
                .problem(p1)
                .status(ProgressStatus.SOLVED)
                .attempts(1)
                .hintsUsed(0)
                .solvedAt(LocalDateTime.now())
                .build());

        attemptRepository.save(Attempt.builder()
                .user(demoUser)
                .problem(p1)
                .submittedCode("public int findLargest(int[] nums) { ... }")
                .result(AttemptResult.ACCEPTED)
                .build());

        log.info("Sample data initialization completed successfully.");
    }
}
