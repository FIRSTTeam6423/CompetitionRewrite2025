// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package wmironpatriots;

import java.util.function.BiConsumer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import lib.LoggedCommandRobot;
import monologue.Monologue;

public class Robot extends LoggedCommandRobot {
  private Command m_autonomousCommand;

  public Robot() {
    super(kDefaultPeriod);
    // logs build data to the datalog
    final String metadata = "/BuildData/";
    Monologue.log(metadata + "RuntimeType", getRuntimeType().toString());
    Monologue.log(metadata + "ProjectName", BuildConstants.MAVEN_NAME);
    Monologue.log(metadata + "BuildDate", BuildConstants.BUILD_DATE);
    Monologue.log(metadata + "GitSHA", BuildConstants.GIT_SHA);
    Monologue.log(metadata + "GitDate", BuildConstants.GIT_DATE);
    Monologue.log(metadata + "GitBranch", BuildConstants.GIT_BRANCH);
    switch (BuildConstants.DIRTY) {
      case 0:
        Monologue.log(metadata + "GitDirty", "All changes committed");
        break;
      case 1:
        Monologue.log(metadata + "GitDirty", "Uncomitted changes");
        break;
      default:
        Monologue.log(metadata + "GitDirty", "Unknown");
        break;
    }
    
    BiConsumer<Command, Boolean> logCommandFunction =
        (Command command, Boolean active) -> {
          Monologue.log("Commands/" + command.getName(), active);
        };
    scheduler.onCommandInitialize(
        (Command command) -> {
          logCommandFunction.accept(command, true);
        });
    scheduler.onCommandFinish(
        (Command command) -> {
          logCommandFunction.accept(command, false);
        });
    scheduler.onCommandInterrupt(
        (Command command) -> {
          logCommandFunction.accept(command, false);
        });
    // ! Uncomment next line if you expirence massive lag/loop-overuns while connected to fms
    // SignalLogger.stop(); SignalLogger.enableAutoLogging(false);

    configureGameBehavior();
    configureBindings();
  }

  private void configureGameBehavior() {}

  private void configureBindings() {}

  @Override
  public Command getAutonCommand() {
    return Commands.runOnce(() -> {});
  }
}
