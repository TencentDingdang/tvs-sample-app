package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.config.ObjectMapperFactory;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.JsonReader;

/**
 * A file-backed data store for TVS, capable of storing and retrieving JSON objects
 */
public class FileDataStore<T> implements DataStore<T> {
    private static final Logger log = LoggerFactory.getLogger(FileDataStore.class);
    private static final ExecutorService sExecutor = Executors.newSingleThreadExecutor();
    private final File file;

    public FileDataStore(String filename) {
        file = new File(filename);
    }

    protected ObjectReader getObjectReader() {
        return ObjectMapperFactory.getObjectReader().withType(new TypeReference<T>() {
        });
    }

    public synchronized void loadFromDisk(final ResultListener<T> listener) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;
                JsonReader parser = null;

                T payload = null;
                try {
                    fileReader = new FileReader(file);
                    bufferedReader = new BufferedReader(fileReader);

                    payload = getObjectReader().readValue(bufferedReader);

                    if (payload != null) {
                        listener.onSuccess(payload);
                    } else {
                        listener.onFailure();
                    }
                } catch (FileNotFoundException e) {
                    // This is not a fatal error
                    // The file might not have been created yet
                    listener.onSuccess(payload);
                } catch (IOException e) {
                    log.error("Failed to load file from disk.", e);
                    listener.onFailure();
                } finally {
                    IOUtils.closeQuietly(parser);
                    IOUtils.closeQuietly(bufferedReader);
                }
            }
        });

    }

    public synchronized void writeToDisk(T payload, final ResultListener<T> listener) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ObjectWriter writer = ObjectMapperFactory.getObjectWriter();
                PrintWriter out = null;
                try {
                    out = new PrintWriter(file);
                    out.print(writer.writeValueAsString(payload));
                    out.flush();
                    listener.onSuccess(payload);
                } catch (IOException e) {
                    log.error("Failed to write to disk", e);
                    listener.onFailure();
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
        });
    }
}
