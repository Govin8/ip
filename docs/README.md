📝 Tommy – Your Personal Task Assistant

“Your mind is for having ideas, not holding them.” 

Tommy helps you manage your tasks quickly and efficiently. It supports adding todos, deadlines, events, marking tasks done, searching tasks, and much more! 🚀

✨ Features

✅ Manage tasks – add, delete, mark/unmark

🔍 Flexible search – find tasks by keyword

📅 Deadlines & events – track dates and time periods

💻 GUI & CLI modes – use Tommy on desktop or terminal

💾 Persistence – automatically saves tasks to a local file

⚠️ Error handling – warns you about invalid commands or dates

🚀 Getting Started

Make sure you have Java 17 installed.

Open the Tommy JAR file from your terminal:

java -jar duke.jar

Tommy runs in GUI mode by default. Use CLI mode if you prefer a terminal experience.


📚 Commands
Adding Tasks

Todo

todo <description>

Deadline

deadline <description> /by <yyyy-MM-dd>

Event

event <description> /from <start> /to <end>
Managing Tasks

Mark a task as done

mark <task number>

Unmark a task

unmark <task number>

Delete a task

delete <task number>
Searching

Find tasks containing a keyword

find <keyword>



Miscellaneous

List all tasks

list

Exit Tommy

bye


💡 Example Usage

Add a task:

todo Read book 📖

Add a deadline:

deadline Submit report /by 2026-03-01 🗓️

Add an event:

event Team meeting /from 10:00 /to 11:00 👥

Find tasks containing "report":

find report 🔍



📝 Notes

Tommy saves your tasks in data/tommy.txt by default.

If the file doesn’t exist, Tommy will create a new one automatically.

Commands are case-insensitive, but dates must follow yyyy-MM-dd.
