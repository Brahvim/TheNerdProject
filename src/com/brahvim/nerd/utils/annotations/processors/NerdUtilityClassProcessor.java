package com.brahvim.nerd.utils.annotations.processors;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import com.brahvim.nerd.utils.annotations.interfaces.NerdUtilityClass;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.brahvim.nerd.utils.annotations.NerdUtilityClass")
public class NerdUtilityClassProcessor extends AbstractProcessor {

	@Override
	public boolean process(final Set<? extends TypeElement> p_annos, final RoundEnvironment p_env) {
		for (final Element e : p_env.getElementsAnnotatedWith(NerdUtilityClass.class))
			if (e.getKind() == ElementKind.CLASS) {
				String message = e.getAnnotation(NerdUtilityClass.class).value();

				if (message.isEmpty())
					message = "Please instantiate `" + e.getSimpleName()
							+ "`es the way they're supposed to be! Sorry...";

				this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, e);
			}

		return true;
	}

}
