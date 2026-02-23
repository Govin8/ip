package tommy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import tommy.ui.Ui;
import tommy.task.Task;
import tommy.task.Todo;
import tommy.task.Deadline;
import tommy.task.Event;

public class Tommy {

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Tommy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (TommyException e) {
            ui.showError("Loading failed, starting fresh.");
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * MainApp entry point for CLI mode
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand().trim();
            assert input != null : "User input must not be null";


            if (input.equals("bye")) {
                ui.showLine();
                ui.showGoodbye();
                ui.showLine();
                break;
            }

            String response = getResponse(input); // no need for try-catch
            assert response != null : "Response from getResponse should not be null";
            ui.showLine();
            ui.showMessage(response);
            ui.showLine();
        }
    }


    /**
     * GUI-friendly method.
     * Takes a user input and returns Tommy's response as a String.
     */
    public String getResponse(String input) {
        try {
            if (input.equals("bye")) {
                return "Bye. Hope to see you again soon!";
            }
            return executeCommand(input);
        } catch (TommyException e) {
            return e.getMessage();
        }
    }

    /* ================= COMMAND EXECUTION ================= */

    private String executeCommand(String input) throws TommyException {
        if (input.startsWith("todo")) {
            handleTodo(input);
            return "Added todo task.";
        } else if (input.startsWith("deadline")) {
            handleDeadline(input);
            return "Added deadline task.";
        } else if (input.startsWith("event")) {
            handleEvent(input);
            return "Added event task.";
        } else if (input.equals("list")) {
            return listToString();
        } else if (input.startsWith("mark")) {
            markTask(input);
            return "Task marked as done.";
        } else if (input.startsWith("unmark")) {
            unmarkTask(input);
            return "Task marked as not done.";
        } else if (input.startsWith("delete")) {
            deleteTask(input);
            return "Task deleted.";
        } else if (input.startsWith("find")) {
            return findToString(input);
        } else {
            throw new TommyException(
                    "I'm sorry, but I don't know what that means :-("
            );
        }
    }

    /* ================= ADD TASKS ================= */

    private void handleTodo(String input) throws TommyException {
        String desc = input.replaceFirst("todo", "").trim();
        if (desc.isEmpty()) {
            throw new TommyException(
                    "The description of a todo cannot be empty."
            );
        }

        Task task = new Todo(desc);
        tasks.add(task);
        storage.save(tasks);
    }

    private void handleDeadline(String input) throws TommyException {
        String data = input.replaceFirst("deadline", "").trim();
        String[] parts = data.split("/by", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new TommyException(
                    "The description of a deadline cannot be empty."
            );
        }

        try {
            LocalDate date = LocalDate.parse(parts[1].trim());
            Task task = new Deadline(parts[0].trim(), date);
            tasks.add(task);
            storage.save(tasks);
        } catch (DateTimeParseException e) {
            throw new TommyException(
                    "Please use date format yyyy-MM-dd."
            );
        }
    }

    private void handleEvent(String input) throws TommyException {
        String data = input.replaceFirst("event", "").trim();
        String[] parts = data.split("/from|/to");

        if (parts.length < 3 || parts[0].trim().isEmpty()) {
            throw new TommyException(
                    "The description of an event cannot be empty."
            );
        }

        Task task = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        tasks.add(task);
        storage.save(tasks);
    }

    /* ================= LIST ================= */

    private String listToString() {
        if (tasks.size() == 0) {
            return "Your task list is empty!";
        }

        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    /* ================= FIND ================= */

    private String findToString(String input) throws TommyException {
        String keyword = input.replaceFirst("find", "").trim();
        if (keyword.isEmpty()) {
            throw new TommyException("Please provide a keyword to search for.");
        }

        ArrayList<Task> matches = tasks.findTasks(keyword);
        if (matches.isEmpty()) {
            return "No matching tasks found.";
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(i + 1).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }

    /* ================= MARK / UNMARK ================= */

    private void markTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.markDone();
        storage.save(tasks);
    }

    private void unmarkTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.unmarkDone();
        storage.save(tasks);
    }

    /* ================= DELETE ================= */

    private void deleteTask(String input) throws TommyException {
        int idx = parseIndex(input);
        tasks.remove(idx);
        storage.save(tasks);
    }

    /* ================= HELPERS ================= */

    private int parseIndex(String input) throws TommyException {
        assert input != null : "Input to parseIndex must not be null";
        try {
            int idx = Integer.parseInt(input.split(" ")[1]) - 1;
             if (idx < 0 || idx >= tasks.size()) {
                throw new NumberFormatException();
            }
            return idx;
        } catch (Exception e) {
            throw new TommyException("Please provide a valid task number.");
        }
    }

    /* ================= MAIN ================= */

    public static void main(String[] args) {
        new Tommy("data/tommy.txt").run();
    }
}
