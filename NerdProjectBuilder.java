/**
 * Run this 'script' using `java NerdProjectBuilder.java m [p] [e]`, where
 * `m` is the name of the class with the `main()` method.
 * `p` is the platform you want to build for (default's the one you are using!),
 * and `e`, is a list of extra architectures you want to support!
 */

public class NerdProjectBuilder {

	public final static String OS_NAME = System.getProperty("os.name");

	public static void main(String[] p_args) {
		String targetOs = null;

		if (p_args.length == 0) {
			targetOs = toTargetOsName(OS_NAME);
			// brEakInG cOnVentIoNs!1!1
			// i uSeD a `StaTic` fIelD wiThOuT pReFixIng tHe cLaSs nAmE!1!!
			// ...yeah, it's fine. It's not like a single file would have non-nested
			// classes anyway, right?
		}

		if (p_args.length > 0) {
			targetOs = p_args[0];
		}

		if (targetOs == null) {
			System.out.println("Could not detect a target OS.");
			System.exit(1);
		}

		System.out.printf("Building for platform \"%s\".\n", targetOs);

	}

	public static String toTargetOsName(String p_str) {
		String str = p_str.toLowerCase();

		if (str.contains("win"))
			return "windows";

		if (str.contains("linux"))
			return "linux";

		if (str.contains("mac"))
			return "mac";

		return null;
	}

	public static void showHelp() {
		System.err.printf("""
				Wrong usage! Please use this program like so:
				    `java NerdProjectBuilder.java [p] [e]`, where
				    `p` is the platform you want to build for (default is the one you are using),
				    and `e` is a list of extra architectures you want to support!
				    """);
	}

}
