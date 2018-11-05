package de.legopraktikum;

import de.legopraktikum.pregler.PRegulator;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;

public class Main {
	
	private static boolean buttonpressed = false;

	public static void main(String[] args) {
		PRegulator pRegulator = new PRegulator();
		
		// KeyListener to be able to stop the program.
		Button.ESCAPE.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(Key k) {
				Sound.beep();
			}

			@Override
			public void keyReleased(Key k) {
				buttonpressed = true;
			}
			
		});
		
		// TODO: Just for a test:
		//new EV3LargeRegulatedMotor(MotorPort.A).forward();
		
		while (!buttonpressed) {
			pRegulator.drive();
		}
	}
}
