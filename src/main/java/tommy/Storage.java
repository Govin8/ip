package tommy;

import tommy.task.Task;

import java.io.*;
import java.util.ArrayList;

/**
 * Manages loading tasks from and saving tasks to a local text file.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage object that uses the specified file path for task persistence.
     *
     * @param filePath path to the data file (relative or absolute)
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from the storage file.
     * Creates the file and parent directories if they do not exist.
     *
     * @return an ArrayList of Task objects loaded from file
     * @throws TommyException if there is an I/O error or file corruption
     */
    public ArrayList<Task> load() throws TommyException {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                Task task = Parser.parseTaskFromFile(line);
                tasks.add(task);
            }
            br.close();
        } catch (IOException e) {
            throw new TommyException("Error loading data");
        }
        return tasks;
    }

    /**
     * Saves the current list of tasks to the storage file.
     * Overwrites the existing file content.
     *
     * @param tasks the TaskList containing tasks to save
     * @throws TommyException if there is an I/O error during writing
     */
    public void save(TaskList tasks) throws TommyException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (Task t : tasks.getTasks()) {
                bw.write(t.toFileString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new TommyException("Error saving data");
        }
    }
}
