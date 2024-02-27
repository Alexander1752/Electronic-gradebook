import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreVisitor implements Visitor
{
	public HashMap<Teacher,List<Tuple<Student,String,Double>>> examScores;
	public HashMap<Assistant,List<Tuple<Student,String,Double>>> partialScores;

	public ScoreVisitor()
	{
		examScores=new HashMap<Teacher,List<Tuple<Student,String,Double>>>();
		partialScores=new HashMap<Assistant,List<Tuple<Student,String,Double>>>();
	}


	public void addExamScore(Teacher teacher,Student student,String course,Double score)
	{
		List<Tuple<Student,String,Double>> list=examScores.get(teacher);
		if(list==null)
		{
			list=new ArrayList<Tuple<Student,String,Double>>();
			examScores.put(teacher,list);
		}
		list.add(new Tuple<Student,String,Double>(student,course,score));
	}
	public void addPartialScore(Assistant assistant,Student student,String course,Double score)
	{
		List<Tuple<Student,String,Double>> list=partialScores.get(assistant);
		if(list==null)
		{
			list=new ArrayList<Tuple<Student,String,Double>>();
			partialScores.put(assistant,list);
		}
		list.add(new Tuple<Student,String,Double>(student,course,score));
	}

	public void visit(Assistant assistant)
	{
		List<Tuple<Student,String,Double>> list=partialScores.remove(assistant);
		for(Tuple<Student,String,Double> tuple:list)
		{
			Course course=Catalog.getInstance().getCourse(tuple.course);
			Grade grade=getGrade(course,tuple.student);
			grade.setPartialScore(tuple.score);
			course.notifyObservers(grade);
		}
	}
	public void visit(Teacher teacher)
	{
		List<Tuple<Student,String,Double>> list=examScores.remove(teacher);
		for(Tuple<Student,String,Double> tuple:list)
		{
			Course course=Catalog.getInstance().getCourse(tuple.course);
			Grade grade=getGrade(course,tuple.student);
			grade.setExamScore(tuple.score);
			course.notifyObservers(grade);
		}
	}

	private Grade getGrade(Course course,Student student)
	{
		Grade grade=course.getGrade(student);
		if(grade==null)
		{
			grade=new Grade(student,course.getName());
			course.addGrade(grade);
		}
		return grade;
	}

	private class Tuple<S,C,D>
	{
		S student;
		C course;
		D score;

		Tuple(S student,C course,D score)
		{
			this.student=student;
			this.course=course;
			this.score=score;
		}
	}

	public List<String> getUnverifiedGrades(TeachingStaff staff, Course course)
	{
		ArrayList<String> ret=new ArrayList<String>();
		List<Tuple<Student,String,Double>> list;
		if(staff instanceof Teacher)
			list=examScores.get((Teacher)staff);
		else if(staff instanceof Assistant)
			list=partialScores.get((Assistant)staff);
		else
			return null;

		if(list==null)
			return null;
		for(Tuple<Student,String,Double> tuple:list)
			if(tuple.course.equals(course.getName()))
				ret.add(tuple.student+": "+tuple.score);
		return ret;
	}
}