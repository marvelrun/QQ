package server;

//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.util.HashMap;
import io.ObjectInputStream;
import io.ObjectOutputStream;

public class ClientManager {
	public static HashMap<Integer, ObjectOutputStream>out= new HashMap<Integer, ObjectOutputStream>();
	public static HashMap<Integer, ObjectInputStream> ois= new HashMap<Integer, ObjectInputStream>();

}