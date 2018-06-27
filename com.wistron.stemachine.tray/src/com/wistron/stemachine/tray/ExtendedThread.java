package com.wistron.stemachine.tray;

public class ExtendedThread extends Thread {
  /** Flag indicating status of the Thread */
  protected boolean stopped = false;
  /**
   * Instantiate a new Extended Thread
   * 
   * @see java.lang.Thread
   */
  public ExtendedThread() {
    super();
  }
  /**
   * Instantiate a new Extended Thread
   * 
   * @param name the name of the new thread.
   * @see java.lang.Thread
   */
  public ExtendedThread(String name) {
    super(name);
  }
  /**
   * Check if this Thread was stopped
   * 
   * @return boolean TRUE if stopped
   */
  public boolean isStopped() {
    return stopped;
  }
  /**
   * Starts this Thread
   */
  public void startThread() {
    start();
  }
  /**
   * Stops this Thread by setting the stopped-flag to TRUE
   */
  public synchronized void stopThread() {
    stopped = true;
  }
}