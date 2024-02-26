package edu.bot.model;

public class RandomRange {

    static int max = 15; // Максимальное число для диапазона от 0 до max

    public static Long rnd()
    {
        return (long) (Math.random() * ++max);
    }
}
