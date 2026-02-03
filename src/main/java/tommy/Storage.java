package tommy;

import tommy.task.Task;

import java.io.*;
import java.util.ArrayList;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
