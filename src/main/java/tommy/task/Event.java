package tommy.task;

/**
 * Represents a task that occurs within a specific time range (start to end).
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Constructs a new Event task with description and time period.
     *
     * @param description the description of the event
     * @param from        the start time/date of the event
     * @param to          the end time/date of the event
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time of the event.
     *
     * @return the start time/date string
     */
    public String getFrom() {
        return this.from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return the end time/date string
     */
    public String getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + getFrom() + " to: " + getTo() + ")";
    }

    @Override
    public String toFileString() {
        return "E | " + (getDone() ? "1" : "0") + " | " + getDescription()
                + " | " + from + " | " + to;
    }

}
