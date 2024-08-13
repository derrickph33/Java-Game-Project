import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    public int compare(Event lft, Event rht) {
        return (int) (1000 * (lft.time - rht.time));
    }
}