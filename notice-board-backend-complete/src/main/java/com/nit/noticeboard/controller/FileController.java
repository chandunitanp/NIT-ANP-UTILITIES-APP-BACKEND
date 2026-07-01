package com.nit.noticeboard.controller;

import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;

@RestController
public class FileController {

    private final Path uploadDir = Paths.get("uploads");

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        try {
            Path file = uploadDir.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
