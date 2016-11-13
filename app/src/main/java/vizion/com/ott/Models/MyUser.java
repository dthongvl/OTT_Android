package vizion.com.ott.Models;

/**
 * Created by Razor on 07/11/2016.
 */
public class MyUser extends User {
    private static MyUser ourInstance = new MyUser();

    public static MyUser getInstance() {
        return ourInstance;
    }

    private MyUser() {
    }
}
