package com.brahvim.nerd_tests;

/**
 * This benchmark was written by ChatGPT, a language model trained by OpenAI.
 * Date: 2023-03-24
 * 
 * This benchmark measures the time it takes to create a new OpenAL context
 * versus resetting the properties on an existing context. The results may
 * vary depending on the machine and OpenAL implementation used.
 * 
 * Use at your own risk.
 */

// Brahvim's comment: The only things written by a human here are:
// - That package declaration you see at the top,
// - The value of `iterations` down there, being set to `100` instead of `1000`.
// - Most LWJGL-OpenAL import statements because ChatGPT lives in the past with LWJGL 2 or something.
// It took 2 HOURS of instructing that guy to get this thing written!

// PS He also dropped the following explanation:

/*
// Based on the contents of `benchmark.log`, we can see that the test ran for 100 iterations and took a 
// total of 2241 milliseconds (2.241 seconds) to complete.

// The average initialization time for each iteration was 14,432 microseconds (0.014 seconds),
// while the average reset time for each iteration was 23 microseconds.

// This means that creating a new OpenAL context is significantly slower than resetting the properties 
// of an existing context. The average initialization time of 14.4 milliseconds compared to an average 
// reset time of 23 microseconds indicates that it's about 620 times slower to create a new
// context than to reset an existing one.

// Overall, this benchmark shows that it's more efficient to reuse a context object and reset its 
// properties rather than creating a new context object every time.
*/

import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

public class AlBenchmark {

	public static void main(String[] args) {
		int iterations = 100;

		// Create or open file to log benchmark results
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("benchmark.log");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Initialize OpenAL
		long start = System.nanoTime();
		long initContextTime = 0;
		long resetContextTime = 0;

		for (int i = 0; i < iterations; i++) {
			// Create context
			long createStart = System.nanoTime();
			long createContextTime = 0;

			long context = ALC10.alcOpenDevice((String) null);
			if (context != 0L) {
				ALCCapabilities deviceCaps = ALC.createCapabilities(context);
				int[] attributes = { 0 };
				long createContext = ALC10.alcCreateContext(context, attributes);
				if (createContext != 0L) {
					ALC10.alcMakeContextCurrent(createContext);
					AL.createCapabilities(deviceCaps);
					createContextTime = System.nanoTime() - createStart;

					// Reset context properties
					long resetStart = System.nanoTime();
					long resetContextPropertiesTime = 0;

					ALC10.alcMakeContextCurrent(createContext);
					AL10.alListener3f(AL10.AL_POSITION, 0f, 0f, 0f);
					AL10.alListener3f(AL10.AL_VELOCITY, 0f, 0f, 0f);
					AL10.alListenerfv(AL10.AL_ORIENTATION, new float[] { 0f, 0f, -1f, 0f, 1f, 0f });

					resetContextPropertiesTime = System.nanoTime() - resetStart;
					resetContextTime += resetContextPropertiesTime;
				}

				ALC10.alcDestroyContext(createContext);
			}

			ALC10.alcCloseDevice(context);
			initContextTime += createContextTime;
		}

		long end = System.nanoTime();
		long totalTime = end - start;

		// Print benchmark results
		System.out.printf("Iterations: %d\n", iterations);
		System.out.printf("Total Time: %dms\n", totalTime / 1000000);
		System.out.printf("Avg Init Time: %dμs\n", initContextTime / iterations / 1000);
		System.out.printf("Avg Reset Time: %dμs\n", resetContextTime / iterations / 1000);

		// Write benchmark results to file
		try {
			fileWriter.write(String.format("Iterations: %d\n", iterations));
			fileWriter.write(String.format("Total Time: %dms\n", totalTime / 1000000));
			fileWriter.write(String.format("Avg Init Time: %dμs\n", initContextTime / iterations / 1000));
			fileWriter.write(String.format("Avg Reset Time: %dμs\n", resetContextTime / iterations / 1000));
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
