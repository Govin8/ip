package tommy;

import org.junit.jupiter.api.Test;
import tommy.task.Task;
import tommy.task.Todo;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void testAddAndRemove() {
        TaskList list = new TaskList();
        Task t1 = new Todo("read book");
        Task t2 = new Todo("return book");

        list.add(t1);
        list.add(t2);

        assertEquals(2, list.size());
        assertEquals(t1, list.get(0));
        assertEquals(t2, list.get(1));

        Task removed = list.remove(0);
        assertEquals(t1, removed);
        assertEquals(1, list.size());
        assertEquals(t2, list.get(0));
    }

    @Test
    public void testGetTasks() {
        TaskList list = new TaskList();
        Task t = new Todo("read book");
        list.add(t);

        assertEquals(1, list.getTasks().size());
        assertTrue(list.getTasks().contains(t));
    }
}

