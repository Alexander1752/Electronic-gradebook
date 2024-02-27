import java.util.ArrayList;

public class FullCourse extends Course
{
	public FullCourse(FullCourseBuilder builder)
	{
		super(builder);
	}

	public ArrayList<Student> getGraduatedStudents()
	{
		ArrayList<Student> arr=getAllStudents();
		for(Student student:arr)
		{
			Grade grade=getGrade(student);
			if(grade.getPartialScore()<3||grade.getExamScore()<2)
				arr.remove(student);
		}
		return arr;
	}

	public static class FullCourseBuilder extends CourseBuilder
	{
		public FullCourseBuilder(String name,Teacher teacher,BestStudent strategy)
		{
			super(name,teacher,strategy);
		}
		public Course build() {
            return new FullCourse(this);
        }
	}
}