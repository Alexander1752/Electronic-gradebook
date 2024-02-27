public class Grade implements Comparable<Grade>,Cloneable
{
	private Double partialScore, examScore;
	private Student student;
	private String course; // Numele cursului

	public Grade(Student student,String course)
	{
		this.student=student;
		this.course=course;
		partialScore=examScore=Double.valueOf(-1); // notele nu au fost trecute
	}

	public Double getPartialScore()
	{
		return partialScore;
	}
	public void setPartialScore(Double partialScore)
	{
		this.partialScore=partialScore;
	}

	public Double getExamScore()
	{
		return examScore;
	}
	public void setExamScore(Double examScore)
	{
		this.examScore=examScore;
	}


	public Student getStudent()
	{
		return student;
	}
	public String getCourse()
	{
		return course;
	}


	public Double getTotal()
	{
		return partialScore+examScore;
	}


	public int compareTo(Grade g)
	{
		int gradeCmp=getTotal().compareTo(g.getTotal());
		if(gradeCmp!=0)
			return gradeCmp;
		return getStudent().getName().compareTo(g.getStudent().getName());
	}

	public Object clone()
	{
		Grade grade=new Grade(student,course);
		grade.setExamScore(new Double(examScore.doubleValue()));
		grade.setPartialScore(new Double(partialScore.doubleValue()));
		return grade;
	}
}