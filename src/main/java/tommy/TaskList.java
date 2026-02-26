package tommy;

import tommy.task.Task;

import java.util.ArrayList;

/**
 * Manages the in-memory collection of tasks.
 * Provides methods to add, remove, retrieve, and search tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList initialized with an existing list of tasks.
     *
     * @param tasks initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new task to the list.
     *
     * @param task the task to add (must not be null)
     */
    public void add(Task task) {
        assert task != null : "Task being added should not be null";
        tasks.add(task);
        assert tasks.contains(task) : "Task should exist after adding";
    }

    /**
     * Returns a list of tasks whose descriptions contain all the words
     * in the given keyword phrase (case-insensitive).
     *
     * @param keyword search phrase (multiple words separated by space)
     * @return list of matching tasks (empty list if no matches)
     */
    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Search keyword should not be null";
        ArrayList<Task> matches = new ArrayList<>();
        String[] words = keyword.toLowerCase().trim().split("\\s+");
        for (Task task : tasks) {
            String description = task.getDescription().toLowerCase();
            boolean isMatch = true;
            for (String word : words) {
                if (!description.contains(word)) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the task at the specified index (0-based).
     *
     * @param index 0-based index of the task
     * @return the task at the given index
     * @throws AssertionError if index is out of bounds
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size()
                : "Index out of bounds in TaskList.get()";
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index 0-based index of the task to remove
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the current number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the internal list of tasks (direct reference).
     *
     * @return the ArrayList containing all tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
