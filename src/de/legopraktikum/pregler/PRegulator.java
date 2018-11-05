package de.legopraktikum.pregler;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class PRegulator {
	
	private static final int GENERAL_MOTOR_SPEED = 220;
	private static final float K_P_KRIT = 1000f;
	private static final float SHOULD_VALUE = 0.19f;
	private static final float THRESHOLD = 40f;
	
	private EV3ColorSensor colorSensor;
	private SampleProvider sampleProvider;
	
	private EV3LargeRegulatedMotor motorL;
	private EV3LargeRegulatedMotor motorR;
	
	private float speedL;
	private float speedR;
	
	/**
	 * Constructor of the PRegulator Class. It initializes all needed sensors and motors.
	 */
	public PRegulator() {
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		sampleProvider = colorSensor.getRedMode(); // red mode is chosen because of the easier handling and the lack of red light in the ambient light.
		motorL = new EV3LargeRegulatedMotor(MotorPort.B);
		motorR = new EV3LargeRegulatedMotor(MotorPort.A);
	}
	
	/**
	 * Method that implements the translation from the difference into the control value.
	 * Description: See exercise sheet 29.
	 * @param xd: Difference between measured light value and should be value
	 * @return translated control value y
	 */
	public float translate(float xd) {
		return xd * 0.5f * K_P_KRIT;
	}
	
	/**
	 * Very simple driving method to follow the edge of a line. This method has to be repeated
	 */
	public void drive() {
		// fetch a sample from the color sensor.
		float[] sample = new float[1];
		sampleProvider.fetchSample(sample, 0);
		
		// compute the difference between measured light value and should be value.
		float xd = sample[0] - SHOULD_VALUE;
		
		// translate the difference to the control value y.
		float y = translate(xd);
		
		// TODO: Only for tests: Show control values
		System.out.println(y);
		
		// TODO: Replace operators fittingly to direction change needed.
		if (y < (-1 * THRESHOLD)) {
			speedR = GENERAL_MOTOR_SPEED + Math.abs(y);
			motorL.backward();
			speedL  = 0.5f * speedR;
			motorR.forward();
		} else if (y > THRESHOLD) {
			speedL = GENERAL_MOTOR_SPEED + Math.abs(y);
			motorL.forward();
			speedR = 0.5f * speedL;
			motorR.backward();
		} else {
			motorL.forward();
			motorR.forward();
		}
		
		motorL.setSpeed(speedL);
		motorR.setSpeed(speedR);
	}

}
