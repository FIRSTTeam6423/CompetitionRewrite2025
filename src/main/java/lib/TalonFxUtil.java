// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package lib;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DriverStation;
import java.util.ArrayList;

public class TalonFxUtil {
  private static final Orchestra orchestra = new Orchestra();
  private static final ArrayList<TalonFX> globalTalonList = new ArrayList<>();

  private static boolean songLoaded = false;

  public static TalonFXConfiguration getDefaultTalonCfg() {
    TalonFXConfiguration cfg = new TalonFXConfiguration();

    cfg.Audio.BeepOnBoot = true;
    cfg.Audio.BeepOnConfig = true;

    return cfg;
  }

  /**
   * Register a talon to the global talon list
   *
   * @param talon {@link TalonFX} motor to add
   */
  public static void addTalon(TalonFX talon) {
    globalTalonList.add(talon);
  }

  /**
   * Initalizes talon orchestra
   *
   * @return true if sucessful
   */
  public static void initOrchestra() {
    AudioConfigs audoCfg = new AudioConfigs();
    audoCfg.AllowMusicDurDisable = true;

    for (TalonFX talon : globalTalonList) {
      talon.getConfigurator().apply(audoCfg);
      orchestra.addInstrument(talon);
    }
  }

  /**
   * Load chirp file of selected song
   *
   * @param song {@link Songs Song} to load
   * @return true if successful
   */
  public static boolean loadSong(Songs song) {
    songLoaded = orchestra.loadMusic(song.filePath).isOK();

    if (!songLoaded) {
      DriverStation.reportError("CHRP file failed to load. Check deploy directory", true);
      return false;
    }
    return true;
  }

  /**
   * Play loaded song (Will play 1st song in {@link Songs} if no song loaded)
   *
   * @return true if successful
   */
  public static boolean play() {
    if (!songLoaded) {
      loadSong(Songs.values()[0]);
    }

    return orchestra.play().isOK();
  }

  /**
   * Pause orchestra if playing a song
   *
   * @return true if successful
   */
  public static boolean pause() {
    if (orchestra.isPlaying()) {
      return orchestra.pause().isOK();
    }

    return true;
  }

  /**
   * Stop orchestra if playing a song
   *
   * @return true if successful
   */
  public static boolean stop() {
    if (orchestra.isPlaying()) {
      return orchestra.stop().isOK();
    }

    return true;
  }

  public static enum Songs {
    Sandstorm("");

    private final String filePath;

    private Songs(String filePath) {
      this.filePath = filePath;
    }
  }
}
