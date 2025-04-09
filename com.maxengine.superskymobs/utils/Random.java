package com.maxengine.superskymobs.utils;

public final class Random {
  private static final java.util.Random RANDOM = new java.util.Random();
  
  public static boolean checkChance(int chance, int bound) {
    return (chance >= RANDOM.nextInt(bound));
  }
  
  public static int generate(int bound) {
    return RANDOM.nextInt(bound);
  }
  
  private Random() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
