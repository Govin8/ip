import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand().trim();

                if (input.equals("bye")) {
                    ui.showLine();
                    ui.showGoodbye();
                    ui.showLine();
                    break;
                }

                handleCommand(input);

            } catch (TommyException e) {
                ui.showLine();
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
    }

    /* ================= COMMAND HANDLING ================= */

    private void handleCommand(String input) throws TommyException {
        if (input.startsWith("todo")) {
            handleTodo(input);
        } else if (input.startsWith("deadline")) {
            handleDeadline(input);
        } else if (input.startsWith("event")) {
            handleEvent(input);
        } else if (input.equals("list")) {
            listTasks();
        } else if (input.startsWith("mark")) {
            markTask(input);
        } else if (input.startsWith("unmark")) {
            unmarkTask(input);
        } else if (input.startsWith("delete")) {
            deleteTask(input);
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

        printAdd(task);
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

            printAdd(task);
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

        printAdd(task);
    }

    /* ================= LIST ================= */

    private void listTasks() {
        ui.showLine();
        ui.showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage((i + 1) + "." + tasks.get(i));
        }
        ui.showLine();
    }

    /* ================= MARK / UNMARK ================= */

    private void markTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.markDone();
        storage.save(tasks);

        ui.showLine();
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("  " + task);
        ui.showLine();
    }

    private void unmarkTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.unmarkDone();
        storage.save(tasks);

        ui.showLine();
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("  " + task);
        ui.showLine();
    }

    /* ================= DELETE ================= */

    private void deleteTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task removed = tasks.remove(idx);
        storage.save(tasks);

        ui.showLine();
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("  " + removed);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }

    /* ================= HELPERS ================= */

    private int parseIndex(String input) throws TommyException {
        try {
            int idx = Integer.parseInt(input.split(" ")[1]) - 1;
            if (idx < 0 || idx >= tasks.size()) {
                throw new NumberFormatException();
            }
            return idx;
        } catch (Exception e) {
            throw new TommyException(
                    "Please provide a valid task number."
            );
        }
    }

    private void printAdd(Task task) {
        ui.showLine();
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + task);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }

    public static void main(String[] args) {
        new Tommy("data/tommy.txt").run();
    }
}
