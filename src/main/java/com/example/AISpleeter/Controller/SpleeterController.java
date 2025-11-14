package com.example.AISpleeter.Controller;

import com.example.AISpleeter.Service.SpleeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Controller
@RequestMapping("/api/spleeter")
public class SpleeterController {

    @Autowired
    private SpleeterService spleeterService;

    @GetMapping("/split")
    public String splitAudio(){
        return "index";

    }

    @PostMapping("/split")
    public String splitAudio(@RequestParam("Browse_my_file") MultipartFile file, Model model) {
        try {
            Path tempDir = Files.createTempDirectory("spleeter_upload");
            Path inputFile = tempDir.resolve(file.getOriginalFilename());
            file.transferTo(inputFile);

            Path resultDir = spleeterService.separate(inputFile.toString());
            Path vocals = resultDir.resolve("vocals.wav");
            Path instruments = resultDir.resolve("accompaniment.wav");

            if (!Files.exists(vocals)) {
               model.addAttribute("error", "vocal.mp3 is not generated");
               return "index";
            }

            model.addAttribute("vocalsFile", vocals.toString());
            model.addAttribute("instrumentsFile", instruments.toString());
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> download(@RequestParam("path") String path) {
        FileSystemResource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vocals.wav")
                .body(resource);
    }
}

