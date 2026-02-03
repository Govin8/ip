package tommy;

import org.junit.jupiter.api.Test;
import tommy.task.Deadline;
import tommy.task.Event;
import tommy.task.Todo;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTodoCreationAndMark() {
        Todo todo = new Todo("read book");

        // Test initial state
        assertEquals("[T][ ] read book", todo.toString());
        assertEquals("T | 0 | read book", todo.toFileString());

        // Mark as done
        todo.markDone();
        assertEquals("[T][X] read book", todo.toString());
        assertEquals("T | 1 | read book", todo.toFileString());

        // Unmark
        todo.unmarkDone();
        assertEquals("[T][ ] read book", todo.toString());
        assertEquals("T | 0 | read book", todo.toFileString());
    }

    @Test
    void testDeadlineCreationAndMark() {
        LocalDate date = LocalDate.of(2023, 2, 10);
        Deadline deadline = new Deadline("submit report", date);

        assertEquals("[D][ ] submit report (by: Feb 10 2023)", deadline.toString());
        assertEquals("D | 0 | submit report | 2023-02-10", deadline.toFileString());

        deadline.markDone();
        assertEquals("[D][X] submit report (by: Feb 10 2023)", deadline.toString());
        assertEquals("D | 1 | submit report | 2023-02-10", deadline.toFileString());
    }

    @Test
    void testEventCreationAndMark() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");

        assertEquals("[E][ ] meeting (from: Mon 2pm to: 4pm)", event.toString());
        assertEquals("E | 0 | meeting | Mon 2pm | 4pm", event.toFileString());

        event.markDone();
        assertEquals("[E][X] meeting (from: Mon 2pm to: 4pm)", event.toString());
        assertEquals("E | 1 | meeting | Mon 2pm | 4pm", event.toFileString());
    }

    @Test
    void testTaskStatusIcon() {
        Todo todo = new Todo("borrow book");
        assertEquals(" ", todo.getStatusIcon());
        todo.markDone();
        assertEquals("X", todo.getStatusIcon());
    }
}
