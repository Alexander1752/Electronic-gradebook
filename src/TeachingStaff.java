public abstract class TeachingStaff extends User implements Element
{
	public TeachingStaff(String firstName, String lastName)
	{
		super(firstName, lastName);
	}
	public TeachingStaff(User user)
	{
		super(user);
	}
}

