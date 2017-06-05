/*
 * Copyright (C) 2017 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.models.Command;
import nl.ordina.jtech.hackadrone.utils.ANSI;

/**
 * Class representing the artificial intelligence (AI) for a drone.
 *
 * The AI is part of a device and can be inserted into a controller.
 * The AI uses a command listener to handle commands and uses the command model for the available commands.
 * The controller uses the command listener to handle the triggered commands.
 *
 * @see Controller for more information about the working flow of the controller
 * @see CommandListener for the interface that is used to handle commands
 * @see Command for a more detailed explanation about using the commands
 *
 * @author Nils Berlijn, Nanne Huiges
 * @version 1.0
 * @since 1.0
 */
public final class AI implements Device {

    /**
     * The command listener.
     */
    private CommandListener commandListener;

    /**
     * Starts the AI.
     *
     * Take off, turn around for a bit (about 180 degrees)
     * go backwards for a bit and then land.
     * Between the commands it waits a bit doing 'nothing'
     *
     */
    @Override
    public void start() {
        try {
            chill(5000);
            takeOff();
            chill(3000);
            setYaw(127, 1000);
            chill(3000);
            setPitch(-100, 1000);
            chill(3000);
            land();
        } catch (Exception e) {
            System.err.println("Exception in AI.start");
        }

        System.out.println(ANSI.WHITE + "AI done" + ANSI.RESET);
    }

    /**
     * Do nothing for a bit.
     * Useful to let the drone level out
     *
     * @param ms how long to do nothing for
     */
    private void chill(int ms) {
        runCommandFor(new Command(), ms);
    }

    /**
     * Start the takeoff.
     * Takes 5 ms to be sure we have takeoff
     */
    private void takeOff() {
        Command takeOff = new Command();
        takeOff.setTakeOff(true);
        runCommandFor(takeOff, 5000);
    }

    /**
     * Sets landing mode.
     * Takes 5 ms to be sure we achieve landing
     */
    private void land() {
        Command land = new Command();
        land.setLand(true);
        runCommandFor(land, 5000);
    }

    /**
     * Turn.
     * Send a positive value up to 127 to turn right,
     * and negative down to -128 to turn left.
     *
     * @param yaw the amount (speed) of the turn
     * @param ms how long to keep turning
     */
    private void setYaw(int yaw, int ms) {
        Command yawCommand = new Command();
        yawCommand.setYaw(yaw);
        runCommandFor(yawCommand, ms);
    }

    /**
     * Move up or down.
     * Send a positive value up to 127 to go up,
     * and negative down to -128 to go down.
     *
     * @param throttle the amount (speed) of the turn
     * @param ms how long to keep turning
     */
    private void setThrottle(int throttle, int ms) {
        Command throttleCommand = new Command();
        throttleCommand.setYaw(throttle);
        runCommandFor(throttleCommand, ms);
    }

    /**
     * Go forward or backward
     * Send a positive value up to 127 to go forward,
     * and negative down to -128 to go backwards
     *
     * @param pitch the amount (speed) of the turn
     * @param ms how long to keep turning
     */
    private void setPitch(int pitch, int ms) {
        Command pitchCommand = new Command();
        pitchCommand.setPitch(pitch);
        runCommandFor(pitchCommand, ms);
    }

    /**
     * Go left or right
     * Send a positive value up to 127 to go right,
     * and negative down to -128 to go left
     *
     * @param roll the amount (speed) of the turn
     * @param ms how long to keep turning
     */
    private void setRoll(int roll, int ms) {
        Command rollCommand = new Command();
        rollCommand.setYaw(roll);
        runCommandFor(rollCommand, ms);
    }

    /**
     * Sends enough commands to the commandListener so it sends it
     * for the set amount of seconds
     *
     * @param command the command to send
     * @param ms how long to send it
     */
    private void runCommandFor(Command command, int ms) {
        int commandRepeat = ms / commandListener.getDelay();
        for (int i = 0; i < commandRepeat; i++) {
            commandListener.onCommandReceived(command);
        }
    }


    /**
     * Stops the AI.
     *
     * The command listener will be set to null.
     * Also the command will be reinitialised.
     */
    @Override
    public void stop() {
        commandListener = null;
    }

    /**
     * Sets the listener.
     *
     * @param commandListener the command listener to set
     */
    @Override
    public void setListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

}
