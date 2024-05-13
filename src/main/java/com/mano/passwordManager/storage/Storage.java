package com.mano.passwordManager.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Storage {
    private ObjectMapper objectMapper;
    public Boolean fileExists;
    private File file;

    public Storage(String path) {
        objectMapper = new ObjectMapper();
        file = new File(path);
        lookForFile();
    }

    public void savePasswordsToStorage(Map<String, Map<String, String>> domains) {
        try {
            objectMapper.writeValue(file, domains);
            fileExists = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lookForFile() {
        fileExists = false;
        if (file.exists()) {
            fileExists = true;
        }
    }

    public Map<String, Map<String, String>> readPasswordsFromStorage() {
        try {
            return objectMapper.readValue(file, new TypeReference<Map<String, Map<String, String>>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
