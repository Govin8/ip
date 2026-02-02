public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markDone() {
        this.isDone = true;
    }

    public void unmarkDone() {
        this.isDone = false;
    }

    public boolean getDone() {
        return isDone;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

}
