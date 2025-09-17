package com.cliapp.interfaces;

/**
 * Interface for learnable objects
 * SOLID: Interface Segregation - specific to learning functionality
 */
public interface Learnable {
    
    /**
     * Start learning process
     */
    void startLearning();
    
    /**
     * Complete learning
     */
    boolean completeLearning();
    
    /**
     * Get learning progress (0.0 to 1.0)
     */
    double getProgress();
    
    /**
     * Check if learning is completed
     */
    boolean isCompleted();
}
