import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main
{
	public static final String fileName="catalog.json";
	public static void main(String args[])
	{
		BufferedReader buf;
		try {
			buf = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File "+fileName+" not found");
			e.printStackTrace();
			return;
		}
		JSONObject catalog=(JSONObject)JSONValue.parse(buf);
		JSONArray courses=(JSONArray)catalog.get("courses");
		for(Object course:courses)
			Catalog.getInstance().addCourse(readCourse((JSONObject)course));
		readExamScores((JSONArray)catalog.get("examScores"));
		readPartialScores((JSONArray)catalog.get("partialScores"));

		new GraphicalInterface("Catalog UPB");

		try {
			buf.close();
		} catch (IOException e) {
			System.out.println("Closing file failed");
			e.printStackTrace();
			return;
		}
	}
	public static User readUser(JSONObject user)
	{
		if(user==null)
			return null;
		return new BlankUser((String)user.get("firstName"),(String)user.get("lastName"));
	}
	public static Assistant readAssistant(JSONObject jAssistant)
	{
		if(jAssistant==null)
			return null;
		Assistant assistant=new Assistant(readUser(jAssistant));
		search:
		{
			for(Course course:Catalog.getInstance().getCourses())
				for(Assistant temp:course.getAssistants())
					if(temp.isNamed(assistant))
					{
						assistant=temp;
						break search;
					}
		}
		return assistant;
	}
	public static Teacher readTeacher(JSONObject jTeacher)
	{
		if(jTeacher==null)
			return null;
		Teacher teacher=new Teacher(readUser(jTeacher));
		search:
		{
			for(Course course:Catalog.getInstance().getCourses())
			{
				Teacher temp=course.getTeacher();
				if(temp.isNamed(teacher))
				{
					teacher=temp;
					break search;
				}
			}
		}
		return teacher;
	}
	public static Parent readParent(JSONObject jParent)
	{
		if(jParent==null)
			return null;
		Parent parent=new Parent(readUser(jParent));
		search:
		{
			for(Course course:Catalog.getInstance().getCourses())
				for(Student temp:course.getAllStudents())
					if(parent.isNamed(temp.getFather()))
					{
						parent=temp.getFather();
						break search;
					}
					else if(parent.isNamed(temp.getMother()))
					{
						parent=temp.getMother();
						break search;
					}
		}
		return parent;
	}
	public static Group readGroup(JSONObject jGroup)
	{
		Assistant assistant=readAssistant((JSONObject)jGroup.get("assistant"));
		Group group=new Group((String)jGroup.get("ID"), assistant);
		for(Object student:(JSONArray)jGroup.get("students"))
			group.add(readStudent((JSONObject)student));
		return group;
	}

	public static Student readStudent(JSONObject jStudent)
	{
		Student student=new Student(readUser(jStudent));
		boolean parentsNeeded=true;
		search:
		{
			for(Course course:Catalog.getInstance().getCourses())
				for(Student temp:course.getAllStudents())
					if(temp.isNamed(student))
					{
						student=temp;
						parentsNeeded=false;
						break search;
					}
		}
		if(parentsNeeded)
		{
			student.setFather(readParent((JSONObject)jStudent.get("father")));
			student.setMother(readParent((JSONObject)jStudent.get("mother")));
		}
		return student;
	}

	public static Course readCourse(JSONObject jCourse)
	{
		String name=(String)jCourse.get("name");

		String strategyName=(String)jCourse.get("strategy");
		BestStudent strategy=null;
		if(strategyName.equalsIgnoreCase("BestPartialScore"))
			strategy=new BestPartialScore();
		else if(strategyName.equalsIgnoreCase("BestExamScore"))
			strategy=new BestExamScore();
		else if(strategyName.equalsIgnoreCase("BestTotalScore"))
			strategy=new BestTotalScore();

		Teacher teacher=readTeacher((JSONObject)jCourse.get("teacher"));

		Course course=null;
		String courseType=(String)jCourse.get("type");
		if(courseType.equalsIgnoreCase("FullCourse"))
			course=new FullCourse.FullCourseBuilder(name,teacher,strategy).build();
		else if(courseType.equalsIgnoreCase("PartialCourse"))
			course=new PartialCourse.PartialCourseBuilder(name,teacher,strategy).build();

		JSONArray groups=(JSONArray)jCourse.get("groups");
		for(Object group:groups)
			course.addGroup(readGroup((JSONObject)group));

		return course;
	}

	public static void readExamScores(JSONArray examScores)
	{
		for(Object o:examScores)
		{
			JSONObject examScore=(JSONObject)o;
			Student student=readStudent((JSONObject)examScore.get("student"));
			Teacher teacher=readTeacher((JSONObject)examScore.get("teacher"));
			String course=(String)examScore.get("course");

			Double grade;
			Object tempGrade=examScore.get("grade");
			if(tempGrade instanceof Double)
				grade=(Double)tempGrade;
			else if(tempGrade instanceof Long)
				grade=Double.valueOf(((Long)tempGrade).longValue());
			else
				grade=Double.valueOf(-1);

			Catalog.getInstance().getScoreVisitor().addExamScore(teacher,student,course,grade);
		}
	}

	public static void readPartialScores(JSONArray partialScores)
	{
		for(Object o:partialScores)
		{
			JSONObject partialScore=(JSONObject)o;
			Student student=readStudent((JSONObject)partialScore.get("student"));
			Assistant assistant=readAssistant((JSONObject)partialScore.get("assistant"));
			String course=(String)partialScore.get("course");

			Double grade;
			Object tempGrade=partialScore.get("grade");
			if(tempGrade instanceof Double)
				grade=(Double)tempGrade;
			else if(tempGrade instanceof Long)
				grade=Double.valueOf(((Long)tempGrade).longValue());
			else
				grade=Double.valueOf(-1);

			Catalog.getInstance().getScoreVisitor().addPartialScore(assistant,student,course,grade);
		}
	}
}