import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Tommy {

    private static final String LINE =
            "____________________________________________________________";
    private static final String FILE_PATH =
            "data" + File.separator + "tommy.txt";

    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasks();
        greet();

        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                String input = sc.nextLine().trim();

                if (input.equals("bye")) {
                    farewell();
                    break;
                }

                handleCommand(input);

            } catch (TommyException e) {
                printLine();
                System.out.println(e.getMessage());
                printLine();
            }
        }
    }

    /* ================= COMMAND HANDLING ================= */

    private static void handleCommand(String input) throws TommyException {
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
                    "OOPS!!! I'm sorry, but I don't know what that means :-("
            );
        }
    }

    /* ================= ADD TASKS ================= */

    private static void handleTodo(String input) throws TommyException {
        String desc = input.replaceFirst("todo", "").trim();
        if (desc.isEmpty()) {
            throw new TommyException(
                    "OOPS!!! The description of a todo cannot be empty."
            );
        }

        Task task = new Todo(desc);
        tasks.add(task);
        saveTasks();
        printAdd(task);
    }

    private static void handleDeadline(String input) throws TommyException {
        String data = input.replaceFirst("deadline", "").trim();
        String[] parts = data.split("/by", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new TommyException(
                    "OOPS!!! The description of a deadline cannot be empty."
            );
        }

        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(task);
        saveTasks();
        printAdd(task);
    }

    private static void handleEvent(String input) throws TommyException {
        String data = input.replaceFirst("event", "").trim();
        String[] parts = data.split("/from|/to");

        if (parts.length < 3 || parts[0].trim().isEmpty()) {
            throw new TommyException(
                    "OOPS!!! The description of an event cannot be empty."
            );
        }

        Task task = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        tasks.add(task);
        saveTasks();
        printAdd(task);
    }

    /* ================= LIST ================= */

    private static void listTasks() {
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        printLine();
    }

    /* ================= MARK / UNMARK ================= */

    private static void markTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.markDone();
        saveTasks();

        printLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        printLine();
    }

    private static void unmarkTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task task = tasks.get(idx);
        task.unmarkDone();
        saveTasks();

        printLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        printLine();
    }

    /* ================= DELETE ================= */

    private static void deleteTask(String input) throws TommyException {
        int idx = parseIndex(input);
        Task removed = tasks.remove(idx);
        saveTasks();

        printLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }

    /* ================= FILE SAVE / LOAD ================= */

    private static void loadTasks() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();

            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                Task task;

                switch (parts[0]) {
                    case "T":
                        task = new Todo(parts[2]);
                        break;
                    case "D":
                        task = new Deadline(parts[2], parts[3]);
                        break;
                    case "E":
                        task = new Event(parts[2], parts[3], parts[4]);
                        break;
                    default:
                        continue;
                }

                if (parts[1].equals("1")) {
                    task.markDone();
                }

                tasks.add(task);
            }
            br.close();

        } catch (IOException ignored) {
        }
    }

    private static void saveTasks() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
            for (Task task : tasks) {
                bw.write(task.toFileString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
        }
    }

    /* ================= HELPERS ================= */

    private static int parseIndex(String input) throws TommyException {
        try {
            int idx = Integer.parseInt(input.split(" ")[1]) - 1;
            if (idx < 0 || idx >= tasks.size()) {
                throw new NumberFormatException();
            }
            return idx;
        } catch (Exception e) {
            throw new TommyException(
                    "OOPS!!! Please provide a valid task number."
            );
        }
    }

    private static void printAdd(Task task) {
        printLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }

    private static void greet() {
        printLine();
        System.out.println("Hello! I'm Tommy");
        System.out.println("What can I do for you?");
        printLine();
    }

    private static void farewell() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    private static void printLine() {
        System.out.println(LINE);
    }
}

