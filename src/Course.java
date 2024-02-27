import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public abstract class Course implements Subject
{
	private String name;
	private Teacher teacher;
	private HashSet<Assistant> assistants;
	private TreeSet<Grade> grades;
	private HashMap<String,Group> groups;
	private int credits;
	private BestStudent strategy;
	private Snapshot snapshot;

	private TreeSet<Observer> parents;

	public Course(CourseBuilder builder)
	{
		name=builder.name;
		teacher=builder.teacher;
		credits=builder.credits;
		assistants=builder.assistants;
		grades=builder.grades;
		groups=builder.groups;
		parents=builder.parents;
		snapshot=builder.snapshot;
		strategy=builder.strategy;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}

	public Teacher getTeacher()
	{
		return teacher;
	}
	public void setTeacher(Teacher teacher)
	{
		this.teacher=teacher;
	}

	public int getCredits()
	{
		return credits;
	}
	public void setCredits(int credits)
	{
		this.credits=credits;
	}

	// Seteaza asistentul în grupa cu ID-ul indicat
	// Daca nu exista deja, adauga asistentul si în multimea asistentilor
	public void addAssistant(String ID, Assistant assistant)
	{
		groups.get(ID).setAssistant(assistant);
		assistants.add(assistant);
	}
	public Collection<Assistant> getAssistants()
	{
		Object clone=assistants.clone();
		if(clone instanceof Collection<?>)
			return (Collection<Assistant>)clone;
		return null;
	}
	// Adauga studentul în grupa cu ID-ul indicat
	public void addStudent(String ID, Student student)
	{
		groups.get(ID).add(student);
	}
	// Adauga grupa
	public void addGroup(Group group)
	{
		groups.put(group.getID(),group);
		addAssistant(group.getID(),group.getAssistant());
	}
	// Instantiaza o grupa si o adauga
	public void addGroup(String ID, Assistant assistant)
	{
		addGroup(new Group(ID,assistant));
	}
	// Instantiaza o grupa si o adauga
	public void addGroup(String ID, Assistant assist, Comparator<Student> comp)
	{
		addGroup(new Group(ID,assist,comp));
	}
	public Collection<Group> getGroups()
	{
		return groups.values();
	}
	// Returneaza nota unui student sau null
	public Grade getGrade(Student student)
	{
		for(Grade grade:grades)
			if(grade.getStudent().equals(student))
				return grade;
		return null;
	}

	public Grade getGrade(String student)
	{
		for(Grade grade:grades)
			if(grade.getStudent().isNamed(student))
				return grade;
		return null;
	}

	// Adauga o nota
	public void addGrade(Grade grade)
	{
		grades.add(grade);
	}
	// Returneaza o lista cu toti studentii
	public ArrayList<Student> getAllStudents()
	{
		ArrayList<Student> ret=new ArrayList<Student>();
		for(Group g:groups.values())
			ret.addAll(g);
		return ret;
	}
	// Returneaza un dictionar cu situatia studentilor
	public HashMap<Student, Grade> gettAllStudentGrades()
	{
		HashMap<Student,Grade> studentGrades=new HashMap<Student,Grade>(grades.size());
		for(Grade grade:grades)
			studentGrades.put(grade.getStudent(),grade);
		return studentGrades;
	}

	public String toString()
	{
		return name;
	}

	public BestStudent getStrategy()
	{
		return strategy;
	}


	public boolean isInCourse(TeachingStaff staff)
	{
		if(staff instanceof Teacher)
			return (Teacher)staff==teacher;
		else if(staff instanceof Assistant)
			return assistants.contains((Assistant)staff);
		else
			return false;
	}

	public boolean isInCourse(Student student)
	{
		return getAllStudents().contains(student);
	}

	// Metoda ce o sa fie implementata pentru a determina studentii promovati
	public abstract ArrayList<Student> getGraduatedStudents();

	public static abstract class CourseBuilder
	{
		private String name;
		private Teacher teacher;
		private HashSet<Assistant> assistants;
		private TreeSet<Grade> grades;
		private HashMap<String,Group> groups;
		private int credits;
		private BestStudent strategy;
		private Snapshot snapshot;

		private TreeSet<Observer> parents;

		public CourseBuilder(String name,Teacher teacher,BestStudent strategy)
		{
			this.name=name;
			this.teacher=teacher;
			this.strategy=strategy;
			this.credits=0; // necompletat

			assistants=new HashSet<Assistant>();
			grades=new TreeSet<Grade>();
			groups=new HashMap<String,Group>();
			parents=new TreeSet<Observer>();
			snapshot=null;
		}

		public CourseBuilder addAssistants(Collection<Assistant> c)
		{
			assistants.addAll(c);
			return this;
		}
		public CourseBuilder addGrades(Collection<Grade> c)
		{
			grades.addAll(c);
			return this;
		}
		public CourseBuilder setCredits(int credits)
		{
			this.credits=credits;
			return this;
		}
		public CourseBuilder addGroups(Collection<Group> c)
		{
			for(Group group:c)
				groups.put(group.getID(),group);
			return this;
		}
		abstract public Course build();
	}


	public void addObserver(Observer observer)
	{
		parents.add(observer);
	}
	public void removeObserver(Observer observer)
	{
		parents.remove(observer);	
	}
	public void notifyObservers(Grade grade)
	{
		Student student=grade.getStudent();
		Notification notification=new Notification(grade);
		if(student.getFather()!=null&&parents.contains(student.getFather()))
			student.getFather().update(notification);
		if(student.getMother()!=null&&parents.contains(student.getMother()))
			student.getMother().update(notification);
	}
	public boolean isParentSubscribed(Observer parent)
	{
		return parents.contains(parent);
	}

	public Student getBestStudent()
	{
		return getStrategy().getBestStudent(grades);
	}

	private class Snapshot
	{
		TreeSet<Grade> backupGrades;
		Snapshot()
		{
			backupGrades=new TreeSet<Grade>();
			for(Grade grade:grades)
				backupGrades.add((Grade)(grade.clone()));
		}
	}

	public void makeBackup()
	{ 
		snapshot=new Snapshot();
	}
	public void undo()
	{
		if(snapshot==null) // nu exista backup
			return;
		grades=new TreeSet<Grade>(snapshot.backupGrades);
	}
}
