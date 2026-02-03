package tommy.ui;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm tommy.Tommy");
        System.out.println("What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public void showError(String msg) {
        System.out.println("OOPS!!! " + msg);
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }
}

