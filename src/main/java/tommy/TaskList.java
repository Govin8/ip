package tommy;

import tommy.task.Task;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        assert task != null : "Task being added should not be null";
        tasks.add(task);
        assert tasks.contains(task) : "Task should exist after adding";
    }

    /**
     * Returns a list of tasks whose descriptions contain the given keyword.
     *
     * This method performs a case-sensitive search in the task descriptions.
     *
     * @param keyword The keyword to search for in task descriptions.
     * @return An ArrayList of tasks that match the keyword. If no tasks match,
     *         an empty list is returned.
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


    public Task get(int index) {
        assert index >= 0 && index < tasks.size()
                : "Index out of bounds in TaskList.get()";
        return tasks.get(index);
    }


    public Task remove(int index) {
        return tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}

