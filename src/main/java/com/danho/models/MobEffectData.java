package com.danho.models;

import net.minecraft.world.effect.MobEffect;

public class MobEffectData {
  public MobEffectData(
    MobEffect effect,
    int duration,
    int amplifier
  ) {
    this.effect = effect;
    this.duration = duration;
    this.amplifier = amplifier;
  }

  public MobEffect effect;
  public int duration;
  public int amplifier;
}
