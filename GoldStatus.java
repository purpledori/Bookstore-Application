/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author dorothyhamza
 */
public class GoldStatus implements CustomerStatus {
    @Override
    public String getStatusName() {
        return "Gold";
    }

    @Override
    public int calculatePointsEarned(double amountSpent) {
        return (int) (amountSpent * 15);  // Earn 15 points per $1 spent (bonus for Gold)
    }
}
