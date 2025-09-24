/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author dorothyhamza
 */
public class SilverStatus implements CustomerStatus {
    @Override
    public String getStatusName() {
        return "Silver";
}
  @Override
    public int calculatePointsEarned(double amountSpent) {
        return (int) (amountSpent * 10);  // Earn 10 points per $1 spent
    }
}
  
