package tommy.task;
/**
 * Represents a Task in the Tommy chatbot.
 * A Task has a description and a completion status.
 * This is the superclass for Todo, Deadline, and Event.
 */
public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    /**
     * Returns the status icon of the task.
     * @return "X" if done, " " if not done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }
    /**
     * Marks the task as done.
     * Changes the task's status to completed.
     */
    public void markDone() {

        this.isDone = true;
    }
    /**
     * Marks the task as not done.
     */
    public void unmarkDone() {

        this.isDone = false;
    }
    /**
     * Returns whether the task is completed.
     *
     * @return true if done, false otherwise
     */
    public boolean getDone() {
        return isDone;
    }
    /**
     * Returns the description of the task.
     */
    public String getDescription() {

        return this.description;
    }
    /**
     * Returns a string representation of the task,
     * including the status icon and description.
     */
    @Override
    public String toString() {

        return "[" + getStatusIcon() + "] " + description;
    }
    /**
     * Returns a string formatted for saving to file.
     *
     * @return a string representing the task for storage
     */
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

}
