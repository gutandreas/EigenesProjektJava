
public class Player {

    private String name;
    private boolean allowedToJump = false;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAllowedToJump(boolean allowedToJump) {
        this.allowedToJump = allowedToJump;
    }

    public boolean isAllowedToJump() {
        return allowedToJump;
    }
}
