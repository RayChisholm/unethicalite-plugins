package net.unethicalite.plugins.goonherbrun.utils.tasks;

import net.unethicalite.plugins.goonherbrun.utils.api.Activity;

public abstract class Task {

  public Activity getActivity() {
    return Activity.IDLE;
  }

  public abstract String getStatus();

  public abstract boolean validate();

  public abstract void execute();
}
