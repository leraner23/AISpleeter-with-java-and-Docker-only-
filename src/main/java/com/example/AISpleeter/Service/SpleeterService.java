package com.example.AISpleeter.Service;


import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpleeterService {

    public Path separate(String inputFilePath) throws IOException, InterruptedException {
        Path inputPath = Paths.get(inputFilePath).toAbsolutePath();
        Path parentDir = inputPath.getParent();
        Path outputDir = Files.createTempDirectory("spleeter_output");

        Files.createDirectories(outputDir);

        // Docker command for Windows
        List<String> command = Arrays.asList(
                "docker", "run", "--rm",
                "-v", parentDir.toString() + ":/input",
                "-v", outputDir.toString() + ":/output",
                "deezer/spleeter:3.7",
                "separate", "-p", "spleeter:2stems", "-o", "/output", "/input/" + inputPath.getFileName().toString()
        );

        System.out.println("Running command: " + String.join(" ", command));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            reader.lines().forEach(System.out::println);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Spleeter failed. Exit code: " + exitCode);
        }

        return outputDir.resolve(inputPath.getFileName().toString().replace(".mp3", ""));
    }
}
