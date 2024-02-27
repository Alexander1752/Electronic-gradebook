public abstract class User
{
	private String firstName,lastName;

	public User(String firstName,String lastName)
	{
		this.firstName=firstName;
		this.lastName=lastName;
	}
	public User(User user)
	{
		this.firstName=user.getFirstName();
		this.lastName=user.getLastName();
	}
	public String toString()
	{
		return firstName+" "+lastName;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getName()
	{
		return lastName+" "+firstName;
	}

	public boolean isNamed(String name)
	{
		if(name==null)
			return false;
		return getName().equalsIgnoreCase(name)||toString().equalsIgnoreCase(name);
	}
	public boolean isNamed(User user)
	{
		if(user==null)
			return false;
		return isNamed(user.getName());
	}
}