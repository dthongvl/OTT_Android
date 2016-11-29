package vizion.com.ott.Models;


public class MyEnemy extends User {
    private static MyEnemy ourInstance = new MyEnemy();

    public static MyEnemy getInstance() {
        return ourInstance;
    }

    private MyEnemy() {
    }
}
