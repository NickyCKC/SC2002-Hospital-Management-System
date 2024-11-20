/**
 * The Medication class represents a medication in the hospital's inventory system.
 * It includes details about the medication name, current stock level, low stock threshold,
 * and the amount requested for replenishment.
 * 
 * <p>The class also includes a utility method to check if the stock level is low.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package model;

public class Medication {

    /** The name of the medication. */
    private String medicineName;

    /** The current stock level of the medication. */
    private int currentStock;

    /** The stock level threshold below which the medication is considered low in stock. */
    private int lowStockLevel;

    /** The amount of medication requested for replenishment. If 0, no replenishment is requested. */
    private int replenishAmount;

    /**
     * Gets the name of the medication.
     *
     * @return the name of the medication.
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Sets the name of the medication.
     *
     * @param medicineName the name of the medication to set.
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     * Gets the current stock level of the medication.
     *
     * @return the current stock level.
     */
    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Sets the current stock level of the medication.
     *
     * @param currentStock the current stock level to set.
     */
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Gets the stock level threshold below which the medication is considered low in stock.
     *
     * @return the low stock threshold.
     */
    public int getLowStockLevel() {
        return lowStockLevel;
    }

    /**
     * Sets the stock level threshold below which the medication is considered low in stock.
     *
     * @param lowStockLevel the low stock threshold to set.
     */
    public void setLowStockLevel(int lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
    }

    /**
     * Gets the amount of medication requested for replenishment.
     *
     * @return the replenishment amount.
     */
    public int getReplenishAmount() {
        return replenishAmount;
    }

    /**
     * Sets the amount of medication requested for replenishment.
     *
     * @param replenishAmount the replenishment amount to set.
     */
    public void setReplenishAmount(int replenishAmount) {
        this.replenishAmount = replenishAmount;
    }

    /**
     * Checks if the medication stock level is considered low.
     *
     * @return {@code true} if the current stock is less than or equal to the low stock level, {@code false} otherwise.
     */
    public boolean isLowStock() {
        return currentStock <= lowStockLevel;
    }
}
