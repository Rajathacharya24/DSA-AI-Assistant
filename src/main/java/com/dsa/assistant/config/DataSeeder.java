package com.dsa.assistant.config;

import com.dsa.assistant.model.Problem;
import com.dsa.assistant.model.Topic;
import com.dsa.assistant.model.enums.Difficulty;
import com.dsa.assistant.repository.ProblemRepository;
import com.dsa.assistant.repository.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TopicRepository topicRepository;
    private final ProblemRepository problemRepository;

    public DataSeeder(TopicRepository topicRepository, ProblemRepository problemRepository) {
        this.topicRepository = topicRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (topicRepository.count() == 0) {
            Topic arrays = new Topic("Arrays");
            Topic strings = new Topic("Strings");
            topicRepository.saveAll(Arrays.asList(arrays, strings));

            Problem p1 = new Problem();
            p1.setTitle("Find Largest Element");
            p1.setDescription("Given an array of integers, find the largest element.");
            p1.setDifficulty(Difficulty.EASY);
            p1.setExampleInput("[1, 5, 3, 9, 2]");
            p1.setExampleOutput("9");
            p1.setExplanation("The largest number in the array is 9.");
            p1.setSolution("public int findLargest(int[] arr) {\n    int max = arr[0];\n    for (int num : arr) {\n        if (num > max) max = num;\n    }\n    return max;\n}");
            p1.setTopic(arrays);

            Problem p2 = new Problem();
            p2.setTitle("Find Smallest Element");
            p2.setDescription("Given an array of integers, find the smallest element.");
            p2.setDifficulty(Difficulty.EASY);
            p2.setExampleInput("[1, 5, 3, 9, 2]");
            p2.setExampleOutput("1");
            p2.setExplanation("The smallest number in the array is 1.");
            p2.setSolution("public int findSmallest(int[] arr) {\n    int min = arr[0];\n    for (int num : arr) {\n        if (num < min) min = num;\n    }\n    return min;\n}");
            p2.setTopic(arrays);

            Problem p3 = new Problem();
            p3.setTitle("Running Sum");
            p3.setDescription("Given an array nums. We define a running sum of an array as runningSum[i] = sum(nums[0]…nums[i]). Return the running sum of nums.");
            p3.setDifficulty(Difficulty.EASY);
            p3.setExampleInput("[1, 2, 3, 4]");
            p3.setExampleOutput("[1, 3, 6, 10]");
            p3.setExplanation("Running sum is obtained as follows: [1, 1+2, 1+2+3, 1+2+3+4].");
            p3.setSolution("public int[] runningSum(int[] nums) {\n    for (int i = 1; i < nums.length; i++) {\n        nums[i] += nums[i - 1];\n    }\n    return nums;\n}");
            p3.setTopic(arrays);

            Problem p4 = new Problem();
            p4.setTitle("Reverse String");
            p4.setDescription("Write a function that reverses a string. The input string is given as an array of characters.");
            p4.setDifficulty(Difficulty.EASY);
            p4.setExampleInput("['h','e','l','l','o']");
            p4.setExampleOutput("['o','l','l','e','h']");
            p4.setExplanation("The characters are swapped from both ends towards the middle.");
            p4.setSolution("public void reverseString(char[] s) {\n    int left = 0, right = s.length - 1;\n    while (left < right) {\n        char temp = s[left];\n        s[left++] = s[right];\n        s[right--] = temp;\n    }\n}");
            p4.setTopic(strings);

            Problem p5 = new Problem();
            p5.setTitle("Contains Duplicate");
            p5.setDescription("Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.");
            p5.setDifficulty(Difficulty.EASY);
            p5.setExampleInput("[1, 2, 3, 1]");
            p5.setExampleOutput("true");
            p5.setExplanation("1 appears twice in the array.");
            p5.setSolution("public boolean containsDuplicate(int[] nums) {\n    Set<Integer> set = new HashSet<>();\n    for (int num : nums) {\n        if (!set.add(num)) return true;\n    }\n    return false;\n}");
            p5.setTopic(arrays);

            problemRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
        }
    }
}
