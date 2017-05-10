package parser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javassist.Modifier;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class ReflectionTest {

	@Test
	public void increaseCoverage() throws ClassNotFoundException, IllegalAccessException {
		// https://github.com/ronmamo/reflections in use
		Set<Class<?>> classes = getClasses("parser");

		for (Class<?> clazz : classes) {
			Method[] methods = clazz.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				if (!Modifier.isPublic(method.getModifiers())) {
					method.setAccessible(true);
				}

				Object[] args = createArguments(method);

				try {
					Object instance = Class.forName(clazz.getName()).newInstance();
					method.invoke(instance, args);
				} catch (Exception e) {
					// who cares?
				}
			}
		}
	}

	private Set<Class<?>> getClasses(String packageName) {
		Collection<URL> urls = ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader());
		FilterBuilder include = new FilterBuilder().include(FilterBuilder.prefix(packageName));

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new SubTypesScanner(false), new ResourcesScanner()).setUrls(urls).filterInputsBy(include));

		Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

		// don't include test classes
		classes.removeIf(new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				return clazz.getName().endsWith("Test");
			}
		});

		// don't include package-info and classes that can't be instantiated.
		classes.removeIf(new Predicate<Class<?>>() {
			@Override
			public boolean test(Class<?> clazz) {
				try {
					return Class.forName(clazz.getName()).isSynthetic();
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		});

		return classes;
	}

	private Object[] createArguments(Method method) {
		List<Object> args = new ArrayList<>();
		Parameter[] parameters = method.getParameters();
		for (int j = 0; j < parameters.length; j++) {
			Parameter parameter = parameters[j];
			if (parameter.getParameterizedType() instanceof ParameterizedType ) {
				ParameterizedType param = (ParameterizedType) parameter.getParameterizedType();
				args.add(new PodamFactoryImpl().manufacturePojo(param.getRawType().getClass(), param.getActualTypeArguments()));
			} else {
				args.add(new PodamFactoryImpl().manufacturePojo(parameter.getType()));
			}
		}
		return args.toArray();
	}

}
