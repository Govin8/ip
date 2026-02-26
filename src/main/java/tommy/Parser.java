package tommy;

import tommy.task.Deadline;
import tommy.task.Event;
import tommy.task.Task;
import tommy.task.Todo;

import java.time.LocalDate;

/**
 * Handles parsing of user input commands and saved file lines
 * into appropriate Task objects or command identifiers.
 */
public class Parser {

    /**
     * Extracts the command word (first word) from the user input.
     *
     * @param input the full user command string
     * @return the command keyword (e.g. "todo", "deadline", "list")
     */
    public static String getCommandWord(String input) {
        return input.split(" ")[0];
    }

    /**
     * Parses a single line from the storage file into a Task object.
     * The expected format is: type | done | description | [extra fields]
     *
     * @param line a line read from the data file
     * @return the corresponding Task object (Todo, Deadline, or Event)
     * @throws TommyException if the line format is invalid or corrupted
     */
    public static Task parseTaskFromFile(String line) throws TommyException {
        assert line != null : "File line should not be null";
        String[] parts = line.split(" \\| ");
        Task task;
        switch (parts[0]) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                task = new Deadline(parts[2], LocalDate.parse(parts[3]));
                break;
            case "E":
                task = new Event(parts[2], parts[3], parts[4]);
                break;
            default:
                throw new TommyException("Corrupted data file");
        }
        if (parts[1].equals("1")) {
            task.markDone();
        }
        return task;
    }
}
