import java.util.TreeSet;

public class BestTotalScore extends BestStudent
{
	public Student getBestStudent(TreeSet<Grade> grades)
	{
		return grades.last().getStudent();
	}
}