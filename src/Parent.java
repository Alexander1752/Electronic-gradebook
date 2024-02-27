import java.util.Collection;
import java.util.HashMap;

public class Parent extends User implements Observer, Comparable<Parent>
{
	private HashMap<String,Notification> notifications;
	public Parent(String firstName,String lastName)
	{
		super(firstName,lastName);
		notifications=new HashMap<String,Notification>();
	}
	public Parent(User user)
	{
		this(user.getFirstName(),user.getLastName());
	}

	public void update(Notification notification)
	{
		notifications.put(notification.grade.getCourse(),notification);
	}

	public int compareTo(Parent parent) {
		return getName().compareTo(parent.getName());
	}
	public void clearNotifications()
	{
		notifications.clear();
	}
	public Collection<Notification> getNotifications()
	{
		return notifications.values();
	}
}