import java.util.Comparator;
import java.util.TreeSet;

public class BestExamScore extends BestStudent
{
	public Student getBestStudent(TreeSet<Grade> grades)
	{
		TreeSet<Grade> examGrades=new TreeSet<Grade>(new Comparator<Grade>()
		{
			public int compare(Grade g1,Grade g2)
			{
				return g1.getExamScore().compareTo(g2.getExamScore());
			}
			
		});
		examGrades.addAll(grades);
		return examGrades.last().getStudent();
	}
}