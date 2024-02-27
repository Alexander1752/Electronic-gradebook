public class Student extends User
{
	private Parent mother,father;
	public Student(String firstName,String lastName)
	{
		super(firstName,lastName);
	}
	public Student(User user)
	{
		super(user);
	}

	public void setMother(Parent mother)
	{
		this.mother=mother;
	}
	public void setFather(Parent father)
	{
		this.father=father;
	}
	public Parent getMother()
	{
		return mother;
	}
	public Parent getFather()
	{
		return father;
	}
}