public class Teacher extends TeachingStaff
{
	public Teacher(String firstName,String lastName)
	{
		super(firstName,lastName);
	}
	public Teacher(User user)
	{
		super(user);
	}

	public void accept(Visitor visitor)
	{
		visitor.visit(this);
	}
}