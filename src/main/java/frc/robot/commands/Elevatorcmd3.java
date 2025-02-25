package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.elevator.elevatorpid;

public class Elevatorcmd3 extends Command {
  private final elevatorpid elevator;
  private final int targetPosition;
  private final double tolerance = 0.25; // Tolerance to switch from Motion Magic to PID
  private double l0 = 0;
  private double l1 = -4.13134765625;
  private double l2 = -27.1033203125;
  private double l3 = -27.1033203125;
  private double l4 = -23.76123046875;
  

  private boolean first;
  private boolean up;

  private double flipsetpoint;

  private enum State {
    MOVING,
    HOLDING
  }

  private State currentState;

  /**
   * Creates a new ElevatorMoveAndHoldCommand.
   *
   * @param elevator The elevator subsystem.
   * @param targetPosition The target position (in sensor units) to move to.
   */
  public Elevatorcmd3(elevatorpid elevator, int targetPosition, boolean hi) {
    this.up = hi;
    this.elevator = elevator;
    this.targetPosition = targetPosition;
    addRequirements(elevator);
  }

  @Override
  public void initialize() {

    
    // Start in the MOVING state and reset encoders if needed.
    currentState = State.MOVING;
    if (elevator.getLeftPosition() < 0.1) {
      elevator.resetenc();
    }

    first = true;
  }

  @Override
  public void execute() {

    if (up) {
      // Set flipsetpoint based on the desired elevator state.
     

      // Check if the flip motor has reached its setpoint.
      // Note: Use flipsetpoint (not targetPosition) for the check.
      if (!elevator.flipcheck(flipsetpoint)) {

        // Command the flip motor until it is at its setpoint.
        elevator.setMotionMagicflip(flipsetpoint);
        // Do not start moving the elevator until the flip motor is ready.
        return;
      }

      // Once the flip motor is holding its setpoint, command the elevator.

      elevator.setMotionMagic1(targetPosition);
      elevator.setMotionMagicflip(flipsetpoint);

      // When close enough to the target, switch to PID holding mode.
      // if (Math.abs(currentPos - targetPosition) < tolerance) {
      //   currentState = State.HOLDING;
      //   elevator.initializePid(targetPosition);
      // } else if (currentState == State.HOLDING) {
      //   // Use PID to hold the position.
      //   elevator.Motionmagictoggle(targetPosition);
    } else if (up == false) {
      elevator.Motionmagictoggle(0);
      if (elevator.check(0)) {
        elevator.setMotionMagicflip(0);
        // idk
      }
    }
  }

  @Override
  public boolean isFinished() {
    // This command runs until it is interrupted (for example, by another command).
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    // Stop the elevator when the command ends.

  }
}
