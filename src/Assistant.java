public class Assistant extends TeachingStaff
{
	public Assistant(String firstName,String lastName)
	{
		super(firstName,lastName);
	}
	public Assistant(User user)
	{
		super(user);
	}

	public void accept(Visitor visitor)
	{
		visitor.visit(this);
	}
}