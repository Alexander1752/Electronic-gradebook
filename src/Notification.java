public class Notification
{
	Grade grade;

	public Notification(Grade grade)
	{
		this.grade=grade;
	}

	public String toString()
	{
		boolean partialExists=false;

		StringBuffer ret=new StringBuffer("Copilul dumneavoastra, "+grade.getStudent().getName()+", a luat nota ");
		if(grade.getPartialScore()>0) // nota exista
		{
			ret.append(grade.getPartialScore()+" in partial");
			partialExists=true;
		}
		if(grade.getExamScore()>0) // nota exista
		{
			if(partialExists)
				ret.append(" si nota ");
			ret.append(grade.getExamScore()+" in examen");
		}
		ret.append(" la cursul "+grade.getCourse());

		return ret+"";
	}
}