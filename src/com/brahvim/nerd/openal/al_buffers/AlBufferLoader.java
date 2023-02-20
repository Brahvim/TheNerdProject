package com.brahvim.nerd.openal.al_buffers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.stb.STBVorbis;

public class AlBufferLoader {

	public static ShortBuffer loadOgg(File p_file) {
		try {
			return STBVorbis.stb_vorbis_decode_filename(p_file.getCanonicalPath(), null, null);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// [https://stackoverflow.com/a/70050617/]
	public static ByteBuffer loadWav(File p_file) {
		// If the size is more than an `int` can handle, wrap!
		ByteArrayOutputStream bytes = new ByteArrayOutputStream(
				(int) Math.min((long) Integer.MAX_VALUE, p_file.length()));

		try (AudioInputStream ais = AudioSystem.getAudioInputStream(p_file)) {
			for (int b = 0; (b = ais.read()) != -1;)
				bytes.write(b);
			return ByteBuffer.wrap(bytes.toByteArray());

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	// `onAlc()`, a method from the older days!
	/*
	 * // Nope! I'm not supporting multithreading here O_O
	 * private static /<asterisk>`synchronized`<asterisk>/ void onAlc(long p_ctx,
	 * Runnable p_runnable) {
	 * long prevCtx = ALC11.alcGetCurrentContext();
	 * 
	 * if (!ALC11.alcMakeContextCurrent(p_ctx))
	 * throw new AlException(0);
	 * 
	 * p_runnable.run();
	 * 
	 * if (!ALC11.alcMakeContextCurrent(prevCtx))
	 * throw new AlException(0);
	 * }
	 */

}
