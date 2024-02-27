import java.util.ArrayList;

public class PartialCourse extends Course
{
	public PartialCourse(PartialCourseBuilder builder)
	{
		super(builder);
	}

	public ArrayList<Student> getGraduatedStudents()
	{
		ArrayList<Student> arr=getAllStudents();
		for(Student student:arr)
		{
			Grade grade=getGrade(student);
			if(grade.getTotal()<5)
				arr.remove(student);
		}
		return arr;
	}
	public static class PartialCourseBuilder extends CourseBuilder
	{
		public PartialCourseBuilder(String name,Teacher teacher,BestStudent strategy)
		{
			super(name,teacher,strategy);
		}
		public Course build() {
            return new PartialCourse(this);
        }
	}
}