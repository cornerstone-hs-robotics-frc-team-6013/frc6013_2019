/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import static org.junit.Assert.assertNotEquals;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Compressor;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private VictorSP LTdrive;
  private VictorSP RTdrive;
  private VictorSP FTauxDrive;
  private VictorSP RRauxDrive;
  private Victor grabberLT;
  private Victor grabberRT;
  private Victor Victor3;
  private Victor Victor4;
  private Victor Victor5;
  private Joystick driverController;
  private DifferentialDrive drivetrain;
  private Compressor Aircomp;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    LTdrive = new VictorSP(0);
    RTdrive = new VictorSP(1);
    FTauxDrive = new VictorSP(2);
    RRauxDrive = new VictorSP(3);
    grabberLT = new Victor(4);
    grabberRT = new Victor(5);
    Victor3 = new Victor(6);
    Victor4 = new Victor(7);
    Victor5 = new Victor(8);
    driverController = new Joystick(0);
    drivetrain = new DifferentialDrive(LTdrive, RTdrive);
    Aircomp = new Compressor();
    
    Aircomp.setClosedLoopControl(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  final double SPEED_RATIOleft = 0.9;
	final double SPEED_RATIOright = 0.9;
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //axis 1 is left stick forward/backwards, axis 5 is right stick forward/backwards
		//constants added to slow down the robot to make it more controllable, can be tuned for drivers
		double speedLeft = -deadband(driverController.getRawAxis(1)) * SPEED_RATIOleft;
    double speedRight = -deadband(driverController.getRawAxis(5)) * SPEED_RATIOright;
    double auxDrive = driverController.getRawAxis(2)-driverController.getRawAxis(3);
    boolean grabberIN = driverController.getRawButton(5);
    boolean grabberOUT = driverController.getRawButton(6);
    int dPad = driverController.getPOV();
    
    drivetrain.tankDrive(speedLeft, speedRight);
    FTauxDrive.set(auxDrive);
    RRauxDrive.set(auxDrive);

    if(grabberIN)
    {
      grabberLT.set(-0.75);
      grabberRT.set(0.75);
    }
    else if(grabberOUT)
    {
      grabberLT.set(0.75);
      grabberRT.set(-0.75);
    }
    else
    {
      grabberLT.set(0);
      grabberRT.set(0);
    }

    if(dPad==0)
    {
      Victor3.set(1);
    }
    else if(dPad==90)
    {
      Victor4.set(1);
    }
    else if(dPad==180)
    {
      Victor5.set(1);
    }
    else
    {
      Victor3.set(0);
      Victor4.set(0);
      Victor5.set(0);
    }
  }

  
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    

  }
      
  final double DEADBAND = 0.1;
	private double deadband(double input) {
    	if((-DEADBAND < input) && (input < DEADBAND)) {
    		return 0;
    	} else {
    		return input;
    	}
	}
}
