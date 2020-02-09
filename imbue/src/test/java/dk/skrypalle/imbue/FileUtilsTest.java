package dk.skrypalle.imbue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class FileUtilsTest {

    private File tempDir;
    private File nestedDir;
    private File emptyDir;

    private File testFile1;
    private File testFile2;
    private File nestedTestFile1;
    private File nestedTestFile2;


    @BeforeEach
    void setup() throws Exception {
        tempDir = Files.createTempDirectory(UUID.randomUUID().toString()).toFile();
        nestedDir = newDir("nested");
        emptyDir = newDir("empty");

        testFile1 = newFile(tempDir, "testFile1");
        testFile2 = newFile(tempDir, "testFile2");
        nestedTestFile1 = newFile(nestedDir, "nestedTestFile1");
        nestedTestFile2 = newFile(nestedDir, "nestedTestFile2");
    }

    @Test
    void listFiles_with_recursion__lists_proper_files() {
        var files = FileUtils.listFiles(tempDir, true);

        assertThat(files)
                .containsExactlyInAnyOrder(testFile1, testFile2, nestedTestFile1, nestedTestFile2);
    }

    @Test
    void listFiles_without_recursion__lists_proper_files() {
        var files = FileUtils.listFiles(tempDir, false);

        assertThat(files)
                .containsExactlyInAnyOrder(testFile1, testFile2);
    }

    @Test
    void listFiles_no_dir_given__throws_exception() {
        var error = catchThrowable(() -> FileUtils.listFiles(testFile1, true));

        assertThat(error)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void listFiles_null_filter__throws_exception() {
        var error = catchThrowable(() -> FileUtils.listFiles(tempDir, null, true));

        assertThat(error)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void listFiles_on_empty_dir__returns_empty_list() {
        var files = FileUtils.listFiles(emptyDir, true);

        assertThat(files)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void listFiles_with_filter_and_recursion__lists_proper_files() {
        var files = FileUtils.listFiles(tempDir, entry -> entry.getName().endsWith("1"), true);

        assertThat(files)
                .containsExactlyInAnyOrder(testFile1, nestedTestFile1);
    }

    @Test
    void listFiles_with_filter_without_recursion__lists_proper_files() {
        var files = FileUtils.listFiles(tempDir, entry -> entry.getName().endsWith("1"), false);

        assertThat(files)
                .containsExactlyInAnyOrder(testFile1);
    }

    private File newFile(File dir, String name) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException();
        }
        var file = new File(dir, name);
        if (!file.createNewFile()) {
            throw new IOException();
        }
        return file;
    }

    private File newDir(String dirName) throws IOException {
        var dir = new File(tempDir, dirName);
        if (!dir.mkdirs()) {
            throw new IOException();
        }
        return dir;
    }

}
