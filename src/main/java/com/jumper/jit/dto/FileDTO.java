package com.jumper.jit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private MultipartFile[] files;
    private String path;
    private String baseurl;
    private List<String> fileNames;
}
