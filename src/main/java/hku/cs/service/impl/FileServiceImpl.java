package hku.cs.service.impl;

import hku.cs.entity.FileProperties;
import hku.cs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private final Path filePath;

    @Autowired
    public FileServiceImpl(FileProperties fileProperties) {
        filePath = Paths.get(fileProperties.getDocDir()).toAbsolutePath().normalize();
    }

    @Override
    public Resource loadFileAsResource(String fileName) throws Exception {
        Path path = filePath.resolve(fileName).normalize();
        try {
            UrlResource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new Exception("file " + fileName + " not found");
        } catch (Exception e) {
            throw new Exception("file " + fileName + " not found", e);
        }
    }
}