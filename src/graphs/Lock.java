package graphs;

public class Lock {
	public Lock(int id, String name) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.name=name;
		this.locked = true;
	}
	public Lock(Lock other) {
		// TODO Auto-generated constructor stub
		this.id = other.id;
		this.name = other.name;
		this.locked = other.locked;
	}
	@Override
	public String toString() {
		return "Lock [id=" + id + ", name=" + ((name!=null) ?  name.toString(): "") + ", lockingStatus="
				+ locked + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLockingStatus() {
		return locked;
	}
	public void setLockingStatus(boolean lockingStatus) {
		this.locked = lockingStatus;
	}
	public boolean equals(Object other)
	{
		if(other instanceof Lock)
		{
			return this.id==((Lock) other).id && this.locked == ((Lock) other).locked;
		}
		return false;
	}
	private int id;
	private String name;
	private boolean locked;
}
