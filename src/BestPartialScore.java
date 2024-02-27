import java.util.Comparator;
import java.util.TreeSet;

public class BestPartialScore extends BestStudent
{
	public Student getBestStudent(TreeSet<Grade> grades)
	{
		TreeSet<Grade> partialGrades=new TreeSet<Grade>(new Comparator<Grade>()
		{
			public int compare(Grade g1,Grade g2)
			{
				return g1.getPartialScore().compareTo(g2.getPartialScore());
			}
		});
		partialGrades.addAll(grades);
		return partialGrades.last().getStudent();
	}
}