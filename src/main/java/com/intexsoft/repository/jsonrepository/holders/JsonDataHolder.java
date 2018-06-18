package com.intexsoft.repository.jsonrepository.holders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intexsoft.model.Book;
import com.intexsoft.model.Library;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class JsonDataHolder {


    @Value("${datasource.local.path}")
    private String localPath;
    @Value("${datasource.local.path.file-name}")
    private String fileName;
    @Autowired
    private ObjectMapper mapper;
    @Getter
    private JsonData jsonData;

    private JsonData getData() {
        List<Book> books = new CopyOnWriteArrayList<>();
        List<Library> libraries = new CopyOnWriteArrayList<>();

        return new JsonData()
                .setBookList(books)
                .setLibraryList(libraries);
    }

    private boolean initialized = false;

    private void getFileToList() {
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(getPathFile()));
            jsonData = mapper.readValue(bufferedInputStream, new TypeReference<JsonData>() {
            });
        } catch (IOException e) {
            log.error("Unable to get file", getPathFile(), e);
            throw new IllegalArgumentException("unable to get file " + getPathFile(), e);

        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    log.info("Stream not closed", e);
                }
            }
        }
    }

    @PreDestroy
    @Scheduled(fixedRateString = "${datasource.fixed.rate}")
    private void saveCollectionInFile() {
        if (initialized) {
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getPathFile()))) {
                bufferedOutputStream.write(mapper.writeValueAsBytes(jsonData));
            } catch (IOException e) {
                log.error("Unable to set file", getPathFile(), e);
                throw new IllegalArgumentException("unable to set file " + getPathFile(), e);
            }
        }
    }


    public String getPathFile() {
        File filePath = new File(localPath, fileName);
        return filePath.toString();
    }

    private void fileValidateAndCreate() {
        File dir = new File(localPath);
        File file = new File(dir, fileName);
        if (!file.exists()) {
            dir.mkdirs();
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getPathFile()))) {
                file.createNewFile();
                bufferedOutputStream.write(mapper.writeValueAsBytes(getData()));
            } catch (IOException e) {
                log.error("File not created", getPathFile(), e);
                throw new IllegalArgumentException("file not created " + getPathFile(), e);
            }
        }
    }

    @PostConstruct
    private void initializeBookCollectionHolder() {
        fileValidateAndCreate();
        getFileToList();
        initialized = true;
    }
}

