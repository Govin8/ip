package tommy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import tommy.ui.Ui;
import tommy.task.Task;
import tommy.task.Todo;
import tommy.task.Deadline;
import tommy.task.Event;

/**
 * Main logic class of the Tommy chatbot.
 * Manages task list, user commands, storage, and responses (both CLI and GUI modes).
 */
public class Tommy {

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a new Tommy chatbot instance.
     * Initializes UI, storage, and attempts to load existing tasks from file.
     * If loading fails, starts with an empty task list and shows an error message.
     *
     * @param filePath the path to the file where tasks are stored
     */
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
     * Runs the command-line interface (CLI) version of the Tommy chatbot.
     * Displays a welcome message, repeatedly reads user commands,
     * processes them, and shows responses until the user enters "bye".
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
     * Processes a user command and returns the appropriate response string.
     * This method is primarily used by the GUI version of the application.
     * Handles the special "bye" command and delegates other commands to executeCommand.
     *
     * @param input the raw command string entered by the user
     * @return the response message from Tommy (success message, error, or goodbye)
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

    /**
     * Executes the appropriate command handler based on the input string
     * and returns a success message (or throws exception on invalid command).
     *
     * @param input the full user command
     * @return a confirmation/success message for the executed command
     * @throws TommyException if the command is not recognized
     */
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

    /**
     * Handles the "todo" command: creates and adds a new Todo task.
     *
     * @param input the full user command (e.g. "todo read book")
     * @throws TommyException if the description is empty
     */
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

    /**
     * Handles the "deadline" command: creates and adds a new Deadline task.
     *
     * @param input the full user command (e.g. "deadline submit report /by 2025-12-31")
     * @throws TommyException if description is empty or date format is invalid
     */
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

    /**
     * Handles the "event" command: creates and adds a new Event task.
     *
     * @param input the full user command (e.g. "event meeting /from Mon 2pm /to 4pm")
     * @throws TommyException if description is empty or format is incomplete
     */
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

    /**
     * Generates a formatted string listing all current tasks.
     *
     * @return a multi-line string showing the task list (or empty message)
     */
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

    /**
     * Generates a formatted string listing tasks that match the search keyword(s).
     *
     * @param input the full find command (e.g. "find book")
     * @return a multi-line string showing matching tasks (or no-match message)
     * @throws TommyException if no keyword is provided
     */
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

    /**
     * Marks the specified task as done.
     *
     * @param input the full mark command (e.g. "mark 3")
     * @throws TommyException if the task number is invalid
     */
    private void markTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.markDone();
        storage.save(tasks);
    }

    /**
     * Marks the specified task as not done.
     *
     * @param input the full unmark command (e.g. "unmark 3")
     * @throws TommyException if the task number is invalid
     */
    private void unmarkTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.unmarkDone();
        storage.save(tasks);
    }

    /* ================= DELETE ================= */

    /**
     * Deletes the task at the specified index.
     *
     * @param input the full delete command (e.g. "delete 2")
     * @throws TommyException if the task number is invalid
     */
    private void deleteTask(String input) throws TommyException {
        int idx = parseIndex(input);
        tasks.remove(idx);
        storage.save(tasks);
    }

    /* ================= HELPERS ================= */

    /**
     * Parses a task index from commands like mark/unmark/delete.
     * Expects format: command N (where N is 1-based)
     *
     * @param input the full command string
     * @return 0-based index of the task
     * @throws TommyException if the number is missing or invalid
     */
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

    /**
     * Entry point for running Tommy in command-line mode.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Tommy("data/tommy.txt").run();
    }
}
