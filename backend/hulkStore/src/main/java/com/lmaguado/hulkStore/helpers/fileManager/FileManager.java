package com.lmaguado.hulkStore.helpers.fileManager;

import com.lmaguado.hulkStore.helpers.strings.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

@Component
public class FileManager {
    private final Logger logger = LoggerFactory.getLogger(FileManager.class);
    private final Environment env;
    private String envTag = ".dev";
    public static final String EXT_FILE = ".properties";

    @Autowired
    public FileManager(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void postFileManager() throws FileManagerException {
        String errMsg;
        String[] profile = env.getActiveProfiles();
        if (profile.length > 0) {
            String loggerHeader = ">> FileManager:\n\t";
            if (profile[0].equals("prod")) {
                errMsg = loggerHeader + "Was detected production environment.";
                logger.info(errMsg);
                envTag = ".prod";
            } else {
                errMsg = loggerHeader + "Was detected a environment different to production.\n\tWas setting for default dev environment.";
                logger.info(errMsg);
                envTag = ".dev";
            }
        } else
            throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_READ_THE_ENV_PROFILE);
    }

    public boolean createFileSecret(String fileName) throws FileManagerException {
        File folder = new File(Constants.FOLDER_SECRET_PATH);
        if (!folder.exists()) folder.mkdir();
        File file = new File(folder.getAbsolutePath() + File.separator + fileName + envTag + EXT_FILE);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException ex) {
                throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_CREATED_THE_SECRET_FILE);
            }
        } else {
            return true;
        }
    }

    private Properties getFilePropertiesSecret(String fileName) throws FileManagerException {
        Properties properties = new Properties();
        File folder = new File(Constants.FOLDER_SECRET_PATH);
        try (InputStream inputStream = new FileInputStream(folder.getAbsolutePath() + File.separator + fileName + envTag + EXT_FILE)) {
            properties.load(inputStream);
        } catch (IOException ex) {
            throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_READ_THE_PROPERTIES_SECRET_FILE);
        }
        return properties;
    }

    private void saveFilePropertiesSecret(String fileName, String key) throws FileManagerException {
        Properties properties = getFilePropertiesSecret(fileName);
        try (OutputStream outputStream = new FileOutputStream(new File(Constants.FOLDER_SECRET_PATH).getAbsolutePath() + File.separator + fileName + envTag + EXT_FILE)) {
            properties.setProperty(key, Constants.PROPERTY_FILE_DEFAULT_VALUE);
            properties.store(outputStream, null);
        } catch (IOException ex) {
            throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_SAVE_THE_PROPERTIES_SECRET_FILE);
        }
    }

    public String getPropertyByKey(String fileName, String key) throws FileManagerException {
        String value = getFilePropertiesSecret(fileName).getProperty(key);
        if (value == null || value.length() == 0) {
            saveFilePropertiesSecret(fileName, key);
            return Constants.PROPERTY_FILE_DEFAULT_VALUE;
        }
        return value;
    }

    public void setPropertyByKey(String fileName, String key, String value) throws FileManagerException {
        Properties properties = getFilePropertiesSecret(fileName);
        try (OutputStream outputStream = new FileOutputStream(new File(Constants.FOLDER_SECRET_PATH).getAbsolutePath() + File.separator + fileName + envTag + EXT_FILE)) {
            properties.setProperty(key, value);
            properties.store(outputStream, null);
        } catch (IOException ex) {
            throw new FileManagerException(FileManagerException.FileManagerExceptionMessage.CANT_BE_SAVE_THE_PROPERTIES_SECRET_FILE);
        }
    }
}
