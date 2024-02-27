import java.util.Collection;
import java.util.HashMap;

public class Catalog
{
	private static Catalog obj=null;
	private HashMap<String,Course> courses;
	private ScoreVisitor sv;

	private Catalog()
	{
		courses=new HashMap<String,Course>();
		sv=new ScoreVisitor();
	}
	public static Catalog getInstance()
	{
		if(obj==null)
			obj=new Catalog();
		return obj;
	}

	// Adauga un curs în catalog
	public void addCourse(Course course)
	{
		courses.put(course.getName(),course);
	}
	// Sterge un curs din catalog
	public void removeCourse(Course course)
	{
		courses.remove(course.getName());
	}

	public Course getCourse(String name)
	{
		return courses.get(name);
	}

	public Collection<Course> getCourses()
	{
		return courses.values();
	}

	public ScoreVisitor getScoreVisitor()
	{
		return sv;
	}
}
