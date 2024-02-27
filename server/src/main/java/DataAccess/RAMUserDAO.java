package DataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class RAMUserDAO implements UserDAO {

    private HashMap<String, UserData> userData;

    public RAMUserDAO() {
        this.userData = new HashMap<String, UserData>();
    }

    /**
     * Clears out the database.
     */
    @Override
    public void clearAccess() {
        this.userData.clear();
    }

    /**
     * Returns an {@link UserData} user from the database.
     * @param username The requested username of the user.
     * @return Returns the userData if {@param username} exists, null otherwise.
     */
    @Override
    public UserData getUser(String username){
        return this.userData.get(username);
    }

    /**
     * Creates a new {@link UserData} user using the provided parameters. Will throw an error if
     * an attempt to make a user with an already existing username is performed.
     * @param username
     * @param email
     * @param password
     */
    @Override
    public void createUser(String username, String email, String password) {
        UserData newUser = new UserData(username, password, email);
        this.userData.put(username, newUser);
    }

    /**
     * A simple method to compare a passed-in password with a matching user in the database.
     * @param username
     * @param password
     * @return true if passwords match, false otherwise.
     */
    public boolean checkPassword(String username, String password){
        UserData user = this.userData.get(username);
        return (password.equals(user.password()));
    }


}
