

public class Customer {
    private String username;
    private String password;
    private int points;
    
    //added by dorothy
    private CustomerStatus status; //uses state pattern

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.points = 0;  
        
        //added by dorothy
        this.status = new SilverStatus(); //default status
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        
        //added by dorothy
        updateStatus(); //automatically updates status when points change
    }
    
    
    //added by dorothy
    public void earnPoints (double amountSpent) {
        int earnedPoints = status.calculatePointsEarned(amountSpent);
        setPoints(this.points + earnedPoints);
    }
    
    //added by dorothy
    public void updateStatus() {
        if (points >= 1000) {
            status = new GoldStatus();
        } else {
            status = new SilverStatus();
        }
    }
    
    public String getStatus(){
        return status instanceof GoldStatus ? "Gold" : "Silver"; // getting customers current status 
    }
}
