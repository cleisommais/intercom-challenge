package com.cleison.itercom.challenge.services;

import com.cleison.itercom.challenge.exeptions.FileStorageException;
import com.cleison.itercom.challenge.exeptions.MyFileNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Component
public class FileStorageService {

    @Getter
    private final Path fileStorageLocation;

    public FileStorageService() {
        String tempDir = System.getProperty("java.io.tmpdir");
        this.fileStorageLocation = Paths.get(tempDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Error when try create a new directory for the new file.", ex);
        }
    }

    /**
     * Store one file per time with Multpartfile format
     *
     * @param file
     * @return the path of the file stored
     */
    public Path storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Path path = this.fileStorageLocation.resolve(fileName).normalize();
            return path;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Retrieve all data from file
     * Format accepted: {"latitude": "52.833502", "user_id": 25, "name": "David Behan", "longitude": "-8.522366"}
     *
     * @param path
     * @return list of objects
     */
    public List getDataFromFile(Path path) {
        List list = null;
        try {
            list = Files.readAllLines(path);
        } catch (NotDirectoryException ex) {
            throw new MyFileNotFoundException("Directory not found.", ex);
        } catch (NoSuchFileException ex) {
            throw new MyFileNotFoundException("File not found.", ex);
        } catch (IOException ex) {
            throw new FileStorageException("Error when try read items from file.", ex);
        } catch (Exception ex) {
            throw new FileStorageException("Error getDataFromFile: ", ex);
        }
        return list;
    }
}
