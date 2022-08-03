package io.github.ititus.downfallRelicStats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

public final class LocalizationSorter {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private LocalizationSorter() {
    }

    public static void main(String[] args) throws IOException {
        Path localizationDir = Paths.get("src/main/resources", DownfallRelicStats.makePath("localization")).toRealPath();
        try (Stream<Path> stream = Files.walk(localizationDir)) {
            stream
                    .filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(".json"))
                    .forEach(f -> {
                        String name = localizationDir.relativize(f).toString().replace('\\', '/');
                        LOGGER.info("sorting {}", name);
                        try {
                            sortLocalization(f);
                        } catch (IOException e) {
                            throw new UncheckedIOException("error while sorting " + name, e);
                        }
                    });
        }
    }

    public static void sortLocalization(Path file) throws IOException {
        Map<String, JsonElement> localization;
        Type t = new TypeToken<Map<String, JsonElement>>() {}.getType();
        try (Reader r = Files.newBufferedReader(file)) {
            localization = GSON.fromJson(r, t);
        }

        SortedMap<String, JsonElement> sortedLocalization = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortedLocalization.putAll(localization);

        Path tmp = null;
        try {
            tmp = Files.createTempFile("", "");
            LOGGER.debug("created tmp file {}", tmp);

            try (Writer w = Files.newBufferedWriter(tmp)) {
                GSON.toJson(sortedLocalization, t, w);
                w.append('\n');
            }

            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            if (tmp != null) {
                try {
                    Files.deleteIfExists(tmp);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
