import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

abstract public class AOC_Runner {

    protected List<String> allLines;

    public AOC_Runner() {
       try {
           this.allLines = readLines();
       } catch (Exception e) {
           System.out.println("Exception thrown: " + e.getMessage());
       }
       executeGoal();
    }

    private List<String> readLines() throws IOException {
        String day = this.getClass().getSimpleName();
        Path path = Paths.get("resource",day + ".txt");
        return Files.readAllLines(path);
    }

    abstract protected void executeGoal();
}
