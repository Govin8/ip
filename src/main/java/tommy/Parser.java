package tommy;

import tommy.task.Deadline;
import tommy.task.Event;
import tommy.task.Task;
import tommy.task.Todo;

import java.time.LocalDate;

public class Parser {

    public static String getCommandWord(String input) {
        return input.split(" ")[0];
    }

    public static Task parseTaskFromFile(String line) throws TommyException {
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
