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
        tasks.add(task);
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
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }


    public Task get(int index) {
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

