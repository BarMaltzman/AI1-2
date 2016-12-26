package graphs;
import java.util.List;


public class Key {

	@Override
	public String toString() {
		return "Key [id=" + id + ", name=" + ((name != null)? name.toString() :"") + "]";
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
	public Key(int id, String name) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
	}
	public Key(Key other){
		this.id = other.id;
		this.name = other.name;
	}
	private int id;
	private String name;
	public static boolean containsKey(List<Key> list, int id2) {
		for(Key key : list)
			if(key.id == id2)
				return true;
		return false;
	}
	public boolean equals(Object other)
	{
		if(other instanceof Key)
		{
			return   this.id==((Key) other).id;
		}
		return false;
	}
}
