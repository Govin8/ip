import java.util.Scanner;

public class Tommy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm Tommy");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;

            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i].toString());
                }
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                tasks[index].markDone();

                System.out.println("____________________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[index].toString());
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                tasks[index].unmarkDone();

                System.out.println("____________________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[index].toString());
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[taskCount] = new Todo(description);
                taskCount++;

                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1].toString());
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("deadline ")) {
                String[] parts = input.split(" /by ");
                String description = parts[0].substring(9);
                String by = parts[1];

                tasks[taskCount] = new Deadline(description, by);
                taskCount++;

                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1].toString());
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("event ")) {
                String[] parts = input.split(" /from ");
                String description = parts[0].substring(6);
                String[] times = parts[1].split(" /to ");
                String from = times[0];
                String to = times[1];

                tasks[taskCount] = new Event(description, from, to);
                taskCount++;

                System.out.println("____________________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1].toString());
                System.out.println("Now you have " + taskCount + " tasks in the list.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }
}
