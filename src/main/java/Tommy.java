import java.util.Scanner;

public class Tommy {
    private static final String LINE =
            "____________________________________________________________";

    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        printWelcome();

        while (true) {
            try {
                String input = sc.nextLine().trim();

                if (input.equals("bye")) {
                    printBye();
                    break;
                } else if (input.equals("list")) {
                    printList();
                } else if (input.startsWith("todo")) {
                    handleTodo(input);
                } else if (input.startsWith("deadline")) {
                    handleDeadline(input);
                } else if (input.startsWith("event")) {
                    handleEvent(input);
                } else if (input.startsWith("mark")) {
                    handleMark(input);
                } else if (input.startsWith("unmark")) {
                    handleUnmark(input);
                } else {
                    throw new TommyException("I'm sorry, I don't know what that means.");
                }

            } catch (TommyException e) {
                printError(e.getMessage());
            }
        }
    }

    /* ---------------- Printing helpers ---------------- */

    private static void printWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Tommy");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void printBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static void printError(String msg) {
        System.out.println(LINE);
        System.out.println("OOPS!!! " + msg);
        System.out.println(LINE);
    }

    private static void printAdd(Task task) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printList() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void printMark(Task task, boolean isMark) {
        System.out.println(LINE);
        if (isMark) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /* ---------------- Command handlers ---------------- */

    private static void handleTodo(String input) throws TommyException {
        if (input.length() <= 4) {
            throw new TommyException("The description of a todo cannot be empty.");
        }
        Task t = new Todo(input.substring(5));
        tasks[taskCount++] = t;
        printAdd(t);
    }

    private static void handleDeadline(String input) throws TommyException {
        String[] parts = input.split(" /by ");
        if (parts.length < 2 || parts[0].length() <= 8) {
            throw new TommyException("Deadline must have a description and /by time.");
        }
        Task t = new Deadline(parts[0].substring(9), parts[1]);
        tasks[taskCount++] = t;
        printAdd(t);
    }

    private static void handleEvent(String input) throws TommyException {
        String[] parts = input.split(" /from | /to ");
        if (parts.length < 3 || parts[0].length() <= 5) {
            throw new TommyException("Event must have description, /from and /to.");
        }
        Task t = new Event(parts[0].substring(6), parts[1], parts[2]);
        tasks[taskCount++] = t;
        printAdd(t);
    }

    private static void handleMark(String input) throws TommyException {
        int index = parseIndex(input, 4);
        tasks[index].markDone();
        printMark(tasks[index], true);
    }

    private static void handleUnmark(String input) throws TommyException {
        int index = parseIndex(input, 6);
        tasks[index].unmarkDone();
        printMark(tasks[index], false);
    }

    private static int parseIndex(String input, int offset) throws TommyException {
        try {
            int idx = Integer.parseInt(input.substring(offset).trim()) - 1;
            if (idx < 0 || idx >= taskCount) {
                throw new TommyException("Task number is invalid.");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new TommyException("Please provide a valid task number.");
        }
    }
}
