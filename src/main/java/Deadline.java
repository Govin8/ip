public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getBy() + ")";
    }
    @Override
    public String toFileString() {
        return "D | " + (getDone() ? "1" : "0") + " | " + getDescription() + " | " + by;
    }

}
