package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.arm.algee;

public class Ballintake2 extends Command {
  private final algee m_algee;
  private final elevatorpid ele;
  private final double m_intakeSpeed;
  private final double m_velocityThreshold;
  private boolean ballDetected = false;
  private double currentVelocity;
  private double flippos;

  /**
   * Constructs the BallIntakeCommand.
   *
   * @param algeeSubsystem The algee subsystem instance.
   * @param intakeSpeed The motor speed used for intaking (e.g., -0.3 for a negative percent
   *     output).
   * @param velocityThreshold The shooter velocity (absolute value) below which we assume a ball is
   *     loaded.
   */
  public Ballintake2(
      algee algeeSubsystem,
      double intakeSpeed,
      double velocityThreshold,
      elevatorpid ele,
      double flippos) {
    this.m_algee = algeeSubsystem;
    this.m_intakeSpeed = intakeSpeed;
    this.m_velocityThreshold = velocityThreshold;
    this.ele = ele;
    this.flippos = flippos;
    addRequirements(m_algee);
  }

  @Override
  public void initialize() {
    ballDetected = false;
    // Start the shooter motor at the intake speed.

  }

  @Override
  public void execute() {

    // Read the current shooter velocity.
    currentVelocity = Math.abs(m_algee.velocity());

    if (ballDetected == false && !ele.flipcheck(flippos)) {

      ele.setMotionMagicflip(flippos);
      m_algee.setShooter(m_intakeSpeed);
    }

    if (currentVelocity < m_velocityThreshold && ele.flipcheck(flippos)) {
      ballDetected = true;
      m_algee.setShooter(0.3);
      ele.setMotionMagicflip(-3.4033203125);
    }
    // If the velocity drops below the threshold, assume the ball is fully intaken.

    // if ((currentVelocity < m_velocityThreshold)) {
    //   ballDetected = true;
    //   if ((currentVelocity < m_velocityThreshold) && ballDetected == true) {
    //     m_algee.setShooter(.5);
    //   } else {
    //     m_algee.setShooter(0);
    //   }
    // }
  }

  @Override
  public boolean isFinished() {
    // Finish the command once the ball is detected.
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    // Make sure the shooter motor is stopped.
    m_algee.setShooter(0);
  }
}
